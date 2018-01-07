package media.thehoard.common.providers;

import static media.thehoard.common.database.jooq.Tables.PROVIDERFILERELATIONSHIPS;
import static media.thehoard.common.database.jooq.Tables.PROVIDERFILES;
import static media.thehoard.common.database.jooq.Tables.PROVIDERS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import media.thehoard.common.configuration.HoardConfiguration;
import media.thehoard.common.providers.generic.Provider;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import media.thehoard.common.database.DatabaseConnector;
import media.thehoard.common.providers.drive.Drive;
import media.thehoard.common.providers.drive.models.DriveFile;
import media.thehoard.common.providers.generic.Provider.ProviderTypes;
import media.thehoard.common.providers.generic.models.ProviderFile;
import media.thehoard.common.providers.generic.models.ProviderFileRelationship;

/**
 * @author Michael Haas
 */
public class ProviderCache {
	public static final Logger PROVIDER_CACHE_LOGGER = LoggerFactory.getLogger(ProviderCache.class);

	private static LoadingCache<UUID,
			Provider<?, ?, ?, ?, ?, ?>> providerCache = Caffeine.newBuilder().maximumSize(30)
			.refreshAfterWrite(1, TimeUnit.HOURS).build(ProviderCache::loadProvider);

	private static LoadingCache<UUID,
			ProviderFile<?, ?, ?, ?, ?, ?>> fileCache = Caffeine.newBuilder().maximumSize(30000)
			.refreshAfterWrite(12, TimeUnit.HOURS).build(ProviderCache::loadFile);

	private static LoadingCache<UUID, List<ProviderFileRelationship>> fileRelationshipCache = Caffeine.newBuilder()
			.maximumSize(40000).refreshAfterWrite(24, TimeUnit.HOURS)
			.build(ProviderCache::loadFileRelationships);

	private static Provider<?, ?, ?, ?, ?, ?> loadProvider(UUID key) {
		try (Connection conn = DatabaseConnector.getConnection()) {
			Record rs = DSL.using(HoardConfiguration.getJooqConfiguration(conn)).select(PROVIDERS.TYPEID)
					.from(PROVIDERS).where(PROVIDERS.UUID.eq(key.toString()))
					.fetchOne();

			if (rs != null) {
				switch (rs.get(PROVIDERS.TYPEID)) {
					case ProviderTypes.DRIVE:
						return new Drive(key);
				}
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_CACHE_LOGGER.error("Failed to load provider into cache: " + key.toString(), e);
		}

		return null;
	}

	private static List<ProviderFileRelationship> loadFileRelationships(UUID key) {
		// Initialize to length 1, because most files have only one parent
		List<ProviderFileRelationship> relationships = new ArrayList<>(1);
		try (Connection conn = DatabaseConnector.getConnection()) {
			PROVIDER_CACHE_LOGGER.info("Loading file relationships for file: " + key.toString());
			List<Record2<Integer, String>> records = DSL.using(HoardConfiguration.getJooqConfiguration(conn))
					.select(PROVIDERFILERELATIONSHIPS.ID, PROVIDERFILERELATIONSHIPS.PARENTUUID)
					.from(PROVIDERFILERELATIONSHIPS).where(PROVIDERFILERELATIONSHIPS.FILEUUID.eq(key.toString()))
					.fetch();

			PROVIDER_CACHE_LOGGER.info("Adding relationship records to array: " + key.toString());
			for (Record rs : records)
				relationships.add(new ProviderFileRelationship(rs.get(PROVIDERFILERELATIONSHIPS.ID), key, UUID
						.fromString(rs.get(PROVIDERFILERELATIONSHIPS.PARENTUUID))));

			PROVIDER_CACHE_LOGGER.info("Returning file relationships: " + key.toString());
			return relationships;
		} catch (DataAccessException | SQLException e) {
			PROVIDER_CACHE_LOGGER.error("Failed to load provider file relationships into cache: " + key.toString(), e);
		}

		return relationships;
	}

	public static List<UUID> getChildUuids(UUID parentFile) {
		List<UUID> children = new ArrayList<>();

		try (Connection conn = DatabaseConnector.getConnection()) {
			Result<Record1<String>> fetch = DSL.using(HoardConfiguration.getJooqConfiguration(conn))
					.select(PROVIDERFILERELATIONSHIPS.FILEUUID).from(PROVIDERFILERELATIONSHIPS)
					.where(PROVIDERFILERELATIONSHIPS.PARENTUUID.eq(parentFile.toString())).fetch();
			for (Record rs : fetch)
				children.add(UUID.fromString(rs.get(PROVIDERFILERELATIONSHIPS.FILEUUID)));


		} catch (SQLException e) {
			PROVIDER_CACHE_LOGGER.error("Failed to load file children: " + parentFile.toString(), e);
		}

		return children;
	}

	/* TODO
	private static <FileType extends ProviderFile<?, ?, ?, ?, ?, ?>> List<FileType> getChildrenFiles(UUID parentFile) {
		getChildUuids(parentFile).stream().map(e -> new FileType(e));
	}*/

	private static ProviderFile<?, ?, ?, ?, ?, ?> loadFile(UUID key) {
		try (Connection conn = DatabaseConnector.getConnection()) {
			Record rs = DSL.using(DatabaseConnector.getConnection(), HoardConfiguration.contents().getDatabaseProfile().getSqlDialect())
					.select(PROVIDERFILES.PROVIDERUUID).from(PROVIDERFILES)
					.where(PROVIDERFILES.UUID.eq(key.toString())).fetchOne();

			if (rs != null) {
				Provider<?, ?, ?, ?, ?, ?> tempProvider = providerCache
						.get(UUID.fromString(rs.get(PROVIDERFILES.PROVIDERUUID)));
				if (tempProvider != null) {
					switch (tempProvider.getTypeId()) {
						case ProviderTypes.DRIVE:
							return new DriveFile(key);
					}
				}
			}
		} catch (DataAccessException|SQLException e) {
			PROVIDER_CACHE_LOGGER.error("Failed to load provider into cache: " + key.toString(), e);
		}

		return null;
	}

	public static ProviderFile<?, ?, ?, ?, ?, ?> getProviderFile(UUID key) {
		return fileCache.get(key);
	}

	public static LoadingCache<UUID, ProviderFile<?, ?, ?, ?, ?, ?>> providerFiles() {
		return fileCache;
	}

	public static Provider<?, ?, ?, ?, ?, ?> getProvider(UUID key) {
		return providerCache.get(key);
	}

	public static LoadingCache<UUID, Provider<?, ?, ?, ?, ?, ?>> providers() {
		return providerCache;
	}

	public static List<ProviderFileRelationship> getFileRelationships(UUID key) {
		return fileRelationshipCache.get(key);
	}

	public static List<ProviderFile<?, ?, ?, ?, ?, ?>> getFileParents(UUID key) {
		List<ProviderFile<?, ?, ?, ?, ?, ?>> parents = new ArrayList<>(1);
		List<ProviderFileRelationship> tempRelationships = fileRelationshipCache.get(key);
		if (tempRelationships != null)
			for (ProviderFileRelationship relationship : tempRelationships)
				parents.add(fileCache.get(relationship.getParentUuid()));
		return parents;
	}

	public static LoadingCache<UUID, List<ProviderFileRelationship>> providerFileRelationships() {
		return fileRelationshipCache;
	}
}
