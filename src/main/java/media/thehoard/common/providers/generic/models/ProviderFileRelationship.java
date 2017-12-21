/**
 * 
 */
package media.thehoard.common.providers.generic.models;

import static media.thehoard.common.database.jooq.Tables.PROVIDERFILERELATIONSHIPS;
import static media.thehoard.common.util.ThreadManager.PRIMARY_THREAD_POOL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.Future;

import media.thehoard.common.configuration.HoardConfiguration;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.UpdateSetMoreStep;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import media.thehoard.common.database.DatabaseConnector;
import media.thehoard.common.database.jooq.tables.records.ProviderfilerelationshipsRecord;
import media.thehoard.common.providers.ProviderCache;
import media.thehoard.common.util.Persistable;

/**
 * @author Michael Haas
 *
 */
public class ProviderFileRelationship implements Persistable {
	public static final Logger PROVIDER_FILE_RELATIONSHIP_LOGGER = LoggerFactory.getLogger(ProviderFileRelationship.class);

	private boolean isPersisted;

	private int databaseId;

	private UUID fileUuid;

	private UUID parentUuid;

	public ProviderFileRelationship(int databaseId, UUID fileUuid, UUID parentUuid, boolean autoPersist) {
		this.databaseId = databaseId;
		this.fileUuid = fileUuid;
		this.parentUuid = parentUuid;

		if (databaseId == -1) {
			load();
			if (!this.isPersisted && autoPersist)
				persist();
		}
	}

	public ProviderFileRelationship(int databaseId, UUID fileUuid, UUID parentUuid) {
		this(databaseId, fileUuid, parentUuid, false);
	}

	public ProviderFileRelationship(UUID fileUuid, UUID parentUuid, boolean autoPersist) {
		this(-1, fileUuid, parentUuid, autoPersist);
	}

	public ProviderFileRelationship(UUID fileUuid, UUID parentUuid) {
		this(-1, fileUuid, parentUuid, false);
	}

	@Override
	public synchronized void load() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			Record rs = DSL.using(HoardConfiguration
					                      .getJooqConfiguration(conn)).select(PROVIDERFILERELATIONSHIPS.ID).from(PROVIDERFILERELATIONSHIPS)
					.where(PROVIDERFILERELATIONSHIPS.FILEUUID.eq(this.fileUuid.toString()).and(PROVIDERFILERELATIONSHIPS.PARENTUUID.eq(this.parentUuid.toString()))).fetchOne();

			if (rs != null) {
				this.isPersisted = true;

				this.databaseId = rs.get(PROVIDERFILERELATIONSHIPS.ID);
			} else {
				this.isPersisted = false;
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_FILE_RELATIONSHIP_LOGGER.error("Failed to load file relationship for file: " + this.fileUuid.toString(), e);
		}
	}

	@Override
	public boolean persist() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			if (this.isPersisted) {
				UpdateSetMoreStep<ProviderfilerelationshipsRecord> updateQuery = DSL.using(HoardConfiguration
						                                                                           .getJooqConfiguration(conn)).update(PROVIDERFILERELATIONSHIPS)
						.set(PROVIDERFILERELATIONSHIPS.FILEUUID, this.fileUuid.toString());
				updateQuery.set(PROVIDERFILERELATIONSHIPS.PARENTUUID, this.parentUuid.toString());

				updateQuery.where(PROVIDERFILERELATIONSHIPS.ID.eq(this.databaseId)).execute();
				conn.close();
			} else {
				DSL.using(HoardConfiguration.getJooqConfiguration(conn)).insertInto(PROVIDERFILERELATIONSHIPS)
						.columns(PROVIDERFILERELATIONSHIPS.FILEUUID, PROVIDERFILERELATIONSHIPS.PARENTUUID).values(this.fileUuid.toString(), this.parentUuid.toString()).execute();
				conn.close();
				load();
			}
			ProviderCache.providerFileRelationships().invalidate(this.fileUuid);
		} catch (DataAccessException | SQLException e) {
			PROVIDER_FILE_RELATIONSHIP_LOGGER.error("Failed to persist file relationship for file: " + this.fileUuid.toString(), e);
			return false;
		}

		return true;
	}

	@Override
	public <T> Future<Boolean> persistField(Field<T> field, T value) {
		return PRIMARY_THREAD_POOL.submit(() -> {
			if (!this.isPersisted)
				return persist();

			try (Connection conn = DatabaseConnector.getConnection()) {
				DSL.using(HoardConfiguration
						          .getJooqConfiguration(conn)).update(PROVIDERFILERELATIONSHIPS).set(field, value)
						.where(PROVIDERFILERELATIONSHIPS.ID.eq(this.databaseId)).execute();

				ProviderCache.providerFileRelationships().invalidate(this.fileUuid);
			} catch (DataAccessException e) {
				PROVIDER_FILE_RELATIONSHIP_LOGGER.error("Failed to update file relationships: " + this.fileUuid.toString(), e);
				return false;
			}

			return true;
		});
	}

	@Override
	public void delete() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			if (this.isPersisted) {
				DSL.using(HoardConfiguration
						          .getJooqConfiguration(conn)).deleteFrom(PROVIDERFILERELATIONSHIPS).where(PROVIDERFILERELATIONSHIPS.ID.eq(this.databaseId)).execute();
				ProviderCache.providerFileRelationships().invalidate(this.fileUuid);
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_FILE_RELATIONSHIP_LOGGER.error("Failed to delete file relationship: " + this.fileUuid.toString() + " -> " + this.parentUuid.toString(), e);
		}
	}

	public boolean isPersisted() {
		return isPersisted;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public UUID getFileUuid() {
		return fileUuid;
	}

	public UUID getParentUuid() {
		return parentUuid;
	}

	public synchronized ProviderFileRelationship setFileUuid(UUID fileUuid) {
		this.fileUuid = fileUuid;
		return this;
	}

	public synchronized ProviderFileRelationship setParentUuid(UUID parentUuid) {
		this.parentUuid = parentUuid;
		return this;
	}

	public synchronized ProviderFileRelationship persistFileUuid(UUID fileUuid) {
		setFileUuid(fileUuid);
		persistField(PROVIDERFILERELATIONSHIPS.FILEUUID, fileUuid.toString());
		return this;
	}

	public synchronized ProviderFileRelationship persistParentUuid(UUID parentUuid) {
		setParentUuid(parentUuid);
		persistField(PROVIDERFILERELATIONSHIPS.PARENTUUID, parentUuid.toString());
		return this;
	}
}
