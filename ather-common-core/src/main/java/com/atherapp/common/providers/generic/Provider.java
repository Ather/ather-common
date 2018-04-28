/**
 *
 */
package com.atherapp.common.providers.generic;

import com.atherapp.common.configuration.AtherConfiguration;
import com.atherapp.common.database.jooq.tables.records.ProvidersRecord;
import com.atherapp.common.providers.generic.authorization.ProviderAuthorization;
import com.atherapp.common.providers.generic.authorization.ProviderCredential;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.service.ProviderService;
import com.atherapp.common.util.Persistable;
import com.atherapp.common.database.DatabaseConnector;
import com.atherapp.common.providers.ProviderCache;
import com.atherapp.common.util.gson.GsonUtil;
import com.atherapp.common.util.hashes.HashType;
import com.atherapp.common.providers.generic.authorization.ProviderAuthorization;
import com.atherapp.common.providers.generic.authorization.ProviderCredential;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.service.ProviderService;
import com.atherapp.common.util.Persistable;
import com.atherapp.common.util.ThreadManager;
import com.atherapp.common.util.gson.GsonUtil;
import com.atherapp.common.util.hashes.HashType;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.UpdateSetMoreStep;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.Future;

import static com.atherapp.common.database.jooq.Tables.PROVIDERS;
import static com.atherapp.common.util.ThreadManager.PRIMARY_THREAD_POOL;

/**
 * @author Michael Haas
 */
public abstract class Provider<AuthorizationClass extends ProviderAuthorization<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>, ConfigurationClass extends ProviderConfiguration<ConfigurationClass>, CredentialClass extends ProviderCredential<CredentialClass>, FileClass extends ProviderFile<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>, ProviderClass extends Provider<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>, ServiceClass extends ProviderService<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>>
		implements Persistable {
	public static Logger PROVIDER_LOGGER = LoggerFactory.getLogger(Provider.class);

	private boolean isPersisted;
	private int databaseId;
	/**
	 * Database UUID of the provider
	 */
	private UUID uuid;
	/**
	 * Provider Id (Drive, Local, etc)
	 */
	private byte typeId;
	/**
	 * Provider Type (repo/backup/slave)
	 *
	 * @see {@link ProviderUsages}
	 */
	private byte usageId;
	/**
	 * Status of the provider
	 *
	 * @see {@link ProviderStatus}
	 */
	private byte status;
	private Timestamp dateCreated;
	private Timestamp dateUpdated;
	/**
	 * Local alias of provider
	 */
	private String alias;
	/**
	 * Identifier used by the provider (perhaps email or username)
	 */
	private String providerAlias;
	private ConfigurationClass configuration;
	private CredentialClass credential;
	private String changeToken;
	/**
	 * Authorization class used to authorize with the provider
	 */
	private AuthorizationClass authorization;

	public Provider(UUID uuid) {
		this.uuid = uuid;
	}

	public Provider() {
		this.isPersisted = false;
	}

	protected abstract ProviderClass self();

	protected abstract Class<ConfigurationClass> configurationClass();

	protected abstract Class<CredentialClass> credentialClass();

	/**
	 *
	 */
	@Override
	public synchronized void load() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			Record rs = DSL.using(AtherConfiguration.getJooqConfiguration(conn)).select(PROVIDERS.fields())
					.from(PROVIDERS).where(PROVIDERS.UUID.eq(this.uuid.toString())).fetchOne();

			if (rs != null) {
				this.isPersisted = true;

				this.databaseId = rs.get(PROVIDERS.ID);
				this.typeId = rs.get(PROVIDERS.TYPEID);
				this.usageId = rs.get(PROVIDERS.USAGEID);
				this.status = rs.get(PROVIDERS.STATUS);
				this.dateCreated = rs.get(PROVIDERS.DATECREATED);
				this.dateUpdated = rs.get(PROVIDERS.DATEUPDATED);
				this.alias = rs.get(PROVIDERS.ALIAS);
				this.providerAlias = rs.get(PROVIDERS.PROVIDERALIAS);
				this.configuration = GsonUtil.getGson().fromJson(rs.get(PROVIDERS.CONFIGURATION), configurationClass());
				this.credential = GsonUtil.getGson().fromJson(rs.get(PROVIDERS.CREDENTIAL), credentialClass());
				this.changeToken = rs.get(PROVIDERS.CHANGETOKEN);
			} else {
				this.isPersisted = false;
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_LOGGER.error("Failed to load provider: " + this.uuid.toString(), e);
		}
	}

	@Override
	public <T> Future<Boolean> persistField(Field<T> field, T value) {
		return ThreadManager.PRIMARY_THREAD_POOL.submit(() -> {
			PROVIDER_LOGGER.trace("Updating provider field: " + this.uuid.toString() + "->" + field.getName());
			if (!this.isPersisted) return persist();

			try (Connection conn = DatabaseConnector.getConnection()) {
				DSL.using(AtherConfiguration.getJooqConfiguration(conn)).update(PROVIDERS).set(field, value)
						.where(PROVIDERS.ID.eq(this.databaseId)).execute();

				// ProviderCache.providers().invalidate(this.uuid);
			} catch (DataAccessException e) {
				PROVIDER_LOGGER.error("Failed to update provider: " + this.uuid.toString(), e);
				return false;
			}

			return true;
		});
	}

	@Override
	public boolean persist() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			updatePersistence(conn);

			if (this.isPersisted) {
				PROVIDER_LOGGER.trace("Updating provider: " + this.uuid.toString());
				UpdateSetMoreStep<ProvidersRecord> updateQuery = DSL
						.using(AtherConfiguration.getJooqConfiguration(conn)).update(PROVIDERS)
						.set(PROVIDERS.UUID, this.uuid.toString());
				updateQuery.set(PROVIDERS.TYPEID, this.typeId);
				updateQuery.set(PROVIDERS.USAGEID, this.usageId);
				updateQuery.set(PROVIDERS.STATUS, this.status);
				updateQuery.set(PROVIDERS.DATEUPDATED, new Timestamp(System.currentTimeMillis()));
				updateQuery.set(PROVIDERS.ALIAS, this.alias);
				updateQuery.set(PROVIDERS.PROVIDERALIAS, this.providerAlias);

				updateQuery.set(PROVIDERS.CONFIGURATION, GsonUtil.getPrettyGson().toJson(this.configuration));
				updateQuery.set(PROVIDERS.CREDENTIAL, GsonUtil.getPrettyGson().toJson(this.credential));
				updateQuery.set(PROVIDERS.CHANGETOKEN, this.changeToken);

				updateQuery.where(PROVIDERS.ID.eq(this.databaseId)).execute();
				if (conn != null) conn.close();
			} else {
				PROVIDER_LOGGER.trace("Inserting provider: " + this.uuid.toString());
				DSL.using(AtherConfiguration.getJooqConfiguration(conn)).insertInto(PROVIDERS)
						.columns(PROVIDERS.ALIAS, PROVIDERS.CONFIGURATION, PROVIDERS.CREDENTIAL, PROVIDERS.PROVIDERALIAS, PROVIDERS.STATUS, PROVIDERS.TYPEID, PROVIDERS.USAGEID, PROVIDERS.UUID)
						.values(this.alias, GsonUtil.getPrettyGson().toJson(this.configuration), GsonUtil
								.getPrettyGson()
								.toJson(this.credential), this.providerAlias, this.status, this.typeId, this.usageId, this.uuid
								        .toString()).execute();
				conn.close();
				load();
			}
			// ProviderCache.providers().invalidate(this.uuid);
		} catch (DataAccessException | SQLException e) {
			PROVIDER_LOGGER.warn("Failed to persist provider: " + this.uuid.toString(), e);
			return false;
		}

		return true;
	}

	// TODO Implement this better to only test when applicable.
	private synchronized boolean updatePersistence(Connection conn) {
		try {
			PROVIDER_LOGGER.trace("Detecting similar entries for provider: " + this.uuid.toString());
			Record result = DSL.using(AtherConfiguration.getJooqConfiguration(conn))
					.select(PROVIDERS.ID, PROVIDERS.UUID).from(PROVIDERS).where(PROVIDERS.TYPEID.eq(this.typeId)
							                                                            .and(PROVIDERS.USAGEID
									                                                                 .eq(this.usageId)
									                                                                 .and(PROVIDERS.PROVIDERALIAS
											                                                                      .eq(this.providerAlias))))
					.fetchOne();
			if (result != null) {
				this.databaseId = result.get(PROVIDERS.ID);
				this.uuid = UUID.fromString(result.get(PROVIDERS.UUID));
				this.isPersisted = true;
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_LOGGER.error("Failed to attempt loading provider from database: " + this.uuid.toString(), e);
		}

		return this.isPersisted;
	}

	@Override
	public void delete() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			PROVIDER_LOGGER.warn("Deleting provider: " + this.uuid.toString());
			if (this.isPersisted) {
				DSL.using(AtherConfiguration.getJooqConfiguration(conn)).deleteFrom(PROVIDERS)
						.where(PROVIDERS.ID.eq(this.databaseId)).execute();
				ProviderCache.providers().invalidate(this.uuid);
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_LOGGER.error("Failed to delete provider: " + this.uuid.toString(), e);
		}
	}

	public abstract ProviderFile.FileType getFileType(String mimeType);

	public abstract HashType getDefaultHashType();

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public synchronized ProviderClass setUuid(UUID uuid) {
		this.uuid = uuid;
		return self();
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public synchronized ProviderClass setAlias(String alias) {
		this.alias = alias;
		return self();
	}

	/**
	 * @return the authorization
	 */
	public AuthorizationClass getAuthorization() {
		return authorization;
	}

	/**
	 * @param authorization the authorization to set
	 */
	public synchronized ProviderClass setAuthorization(AuthorizationClass authorization) {
		this.authorization = authorization;
		return self();
	}

	/**
	 * @return the databaseId
	 */
	public int getDatabaseId() {
		return databaseId;
	}

	/**
	 * @return the typeId
	 */
	public byte getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public synchronized ProviderClass setTypeId(byte typeId) {
		this.typeId = typeId;
		return self();
	}

	/**
	 * @return the usageId
	 */
	public byte getUsageId() {
		return usageId;
	}

	/**
	 * @param usageId the usageId to set
	 */
	public synchronized ProviderClass setUsageId(byte usageId) {
		this.usageId = usageId;
		return self();
	}

	/**
	 * @return the status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public synchronized ProviderClass setStatus(byte status) {
		this.status = status;
		return self();
	}

	/**
	 * @return the dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	public synchronized ProviderClass setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
		return self();
	}

	/**
	 * @return the dateUpdated
	 */
	public Timestamp getDateUpdated() {
		return dateUpdated;
	}

	/**
	 * @param dateUpdated the dateUpdated to set
	 */
	public synchronized ProviderClass setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
		return self();
	}

	/**
	 * @return the providerAlias
	 */
	public String getProviderAlias() {
		return providerAlias;
	}

	/**
	 * @param providerAlias the providerAlias to set
	 */
	public synchronized ProviderClass setProviderAlias(String providerAlias) {
		this.providerAlias = providerAlias;
		return self();
	}

	/**
	 * @return the configuration
	 */
	public ConfigurationClass getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	public synchronized ProviderClass setConfiguration(ConfigurationClass configuration) {
		this.configuration = configuration;
		return self();
	}

	/**
	 * @return the credential
	 */
	public CredentialClass getCredential() {
		return credential;
	}

	/**
	 * @param credential the credential to set
	 */
	public synchronized ProviderClass setCredential(CredentialClass credential) {
		this.credential = credential;
		return self();
	}

	/**
	 * @return the changeToken
	 */
	public String getChangeToken() {
		return changeToken;
	}

	/**
	 * @param changeToken the changeToken to set
	 */
	public synchronized ProviderClass setChangeToken(String changeToken) {
		this.changeToken = changeToken;
		return self();
	}

	/**
	 * @return the isPersisted
	 */
	public boolean isPersisted() {
		return isPersisted;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public synchronized ProviderClass persistUuid(UUID uuid) {
		setUuid(uuid);
		persistField(PROVIDERS.UUID, uuid.toString());
		return self();
	}

	/**
	 * @param alias the alias to set
	 */
	public synchronized ProviderClass persistAlias(String alias) {
		setAlias(alias);
		persistField(PROVIDERS.ALIAS, alias);
		return self();
	}

	/**
	 * @param typeId the typeId to set
	 */
	public synchronized ProviderClass persistTypeId(byte typeId) {
		setTypeId(typeId);
		persistField(PROVIDERS.TYPEID, typeId);
		return self();
	}

	/**
	 * @param usageId the usageId to set
	 */
	public synchronized ProviderClass persistUsageId(byte usageId) {
		setUsageId(usageId);
		persistField(PROVIDERS.USAGEID, usageId);
		return self();
	}

	/**
	 * @param status the status to set
	 */
	public synchronized ProviderClass persistStatus(byte status) {
		setStatus(status);
		persistField(PROVIDERS.STATUS, status);
		return self();
	}

	/**
	 * @param providerAlias the providerAlias to set
	 */
	public synchronized ProviderClass persistProviderAlias(String providerAlias) {
		setProviderAlias(providerAlias);
		persistField(PROVIDERS.PROVIDERALIAS, providerAlias);
		return self();
	}

	/**
	 * @param configuration the configuration to set
	 */
	public synchronized ProviderClass persistConfiguration(ConfigurationClass configuration) {
		setConfiguration(configuration);
		persistField(PROVIDERS.CONFIGURATION, GsonUtil.getPrettyGson().toJson(configuration));
		return self();
	}

	/**
	 * @param credential the credential to set
	 */
	public synchronized ProviderClass persistCredential(CredentialClass credential) {
		setCredential(credential);
		persistField(PROVIDERS.CREDENTIAL, GsonUtil.getPrettyGson().toJson(credential));
		return self();
	}

	/**
	 * @param changeToken the changeToken to set
	 */
	public synchronized ProviderClass persistChangeToken(String changeToken) {
		setChangeToken(changeToken);
		persistField(PROVIDERS.CHANGETOKEN, changeToken);
		return self();
	}

	/**
	 * Provider types representing which provider is used.
	 */
	public final class ProviderTypes {
		public static final byte DRIVE = 0;
	}

	/**
	 * Provider types representing how the provider should be used
	 */
	public final class ProviderUsages {
		public static final byte REPOSITORY = 0;
		public static final byte BACKUP_DESTINATION = 1;
		public static final byte NO_CACHE = 2;
	}

	/**
	 * Status codes for authorization status.
	 */
	public final class ProviderStatus {
		public static final byte ACTIVE = 0;
		public static final byte DISABLED = 1;
	}
}
