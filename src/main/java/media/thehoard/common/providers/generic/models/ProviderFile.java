/**
 * 
 */
package media.thehoard.common.providers.generic.models;

import static media.thehoard.common.database.jooq.Tables.PROVIDERFILES;
import static media.thehoard.common.util.ThreadManager.PRIMARY_THREAD_POOL;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

import media.thehoard.common.configuration.HoardConfiguration;
import media.thehoard.common.providers.generic.Provider;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.UpdateSetMoreStep;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import media.thehoard.common.database.DatabaseConnector;
import media.thehoard.common.database.jooq.tables.records.ProviderfilesRecord;
import media.thehoard.common.providers.ProviderCache;
import media.thehoard.common.providers.generic.ProviderConfiguration;
import media.thehoard.common.providers.generic.authorization.ProviderAuthorization;
import media.thehoard.common.providers.generic.authorization.ProviderCredential;
import media.thehoard.common.providers.generic.service.ProviderService;
import media.thehoard.common.util.Persistable;
import media.thehoard.common.util.hashes.HashType;

/**
 * 
 * @author Michael Haas
 *
 */
public abstract class ProviderFile<
		AuthorizationClass extends ProviderAuthorization<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ConfigurationClass extends ProviderConfiguration<ConfigurationClass>,
		CredentialClass extends ProviderCredential<CredentialClass>,
		FileClass extends ProviderFile<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ProviderClass extends Provider<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>,
		ServiceClass extends ProviderService<AuthorizationClass, ConfigurationClass, CredentialClass, FileClass, ProviderClass, ServiceClass>> implements Persistable {
	public static final Logger PROVIDER_FILE_LOGGER = LoggerFactory.getLogger(ProviderFile.class);

	private boolean isPersisted;

	private int databaseId;

	public static class FileStatus {
		public static final byte AVAILABLE = 0;
		// UNAVAILABLE likely won't be used, but it covers bases
		public static final byte UNAVAILABLE = 1;
		public static final byte TRASHED = 2;
		public static final byte DELETED = 3;
	}

	private UUID uuid;

	private ProviderClass provider;

	private String name;

	private String mimeType;

	private String description;

	private byte status;

	private String providerFileId;

	private List<ProviderFileRelationship> parentFiles;

	private FileType fileType;

	public enum FileType {
		File(0), Directory(1);

		private byte id;

		FileType(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}

		public static FileType getFromId(byte id) {
			for (FileType type : FileType.values())
				if (type.getId() == id)
					return type;
			return File;
		}
	}

	private Timestamp dateCreated;

	private Timestamp dateUpdated;

	private Long size;

	// TODO Consider the implications of storing and calculating hashes like this
	private Map<HashType, String> fileHashes = new HashMap<>();

	public ProviderFile(ProviderClass provider) {
		this.provider = provider;
	}

	// TODO Implement

	/*
	 * public ProviderFile(String virtualPath) {
	 * this(parseVirtualPathToUuid(virtualPath)); }
	 */

	public ProviderFile(UUID uuid) {
		this.uuid = uuid;
		load();
	}

	// The provider cast is unchecked, but it shouldn't be an issue as long as a
	// user doesn't interfere directly with the database.
	@SuppressWarnings("unchecked")
	public synchronized void load() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			if (!this.isPersisted)
				updatePersistence(conn);

			PROVIDER_FILE_LOGGER.info("Loading provider file: " + this.uuid.toString());
			Record rs = DSL.using(HoardConfiguration
					                      .getJooqConfiguration(conn)).select(PROVIDERFILES.fields()).from(PROVIDERFILES).where(PROVIDERFILES.UUID.eq(this.uuid.toString()))
					.fetchOne();

			if (rs != null) {
				PROVIDER_FILE_LOGGER.info("Found provider file: " + this.uuid.toString());
				this.isPersisted = true;

				this.databaseId = rs.get(PROVIDERFILES.ID);
				UUID resultProviderUuid = UUID.fromString(rs.get(PROVIDERFILES.PROVIDERUUID));
				if (this.provider == null || !this.provider.getUuid().equals(resultProviderUuid))
					this.provider = (ProviderClass) ProviderCache.getProvider(resultProviderUuid);
				this.name = rs.get(PROVIDERFILES.NAME);
				this.mimeType = rs.get(PROVIDERFILES.MIMETYPE);
				this.description = rs.get(PROVIDERFILES.DESCRIPTION);
				this.status = rs.get(PROVIDERFILES.STATUS);
				this.providerFileId = rs.get(PROVIDERFILES.PROVIDERFILEID);
				this.fileType = FileType.getFromId(rs.get(PROVIDERFILES.TYPE));
				this.dateCreated = rs.get(PROVIDERFILES.DATECREATED);
				this.dateUpdated = rs.get(PROVIDERFILES.DATEUPDATED);
				this.size = rs.get(PROVIDERFILES.SIZE);
				this.fileHashes.put(provider.getDefaultHashType(), rs.get(PROVIDERFILES.HASH));
				conn.close();
				this.parentFiles = ProviderCache.getFileRelationships(this.uuid);
			} else {
				PROVIDER_FILE_LOGGER.info("Provider file not persisted: " + this.uuid.toString());
				this.isPersisted = false;
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_FILE_LOGGER.error("Failed to load file from database: " + this.uuid.toString(), e);
		}
	}

	public boolean updatePersistence(Connection conn) {
		try {
			PROVIDER_FILE_LOGGER.info("Detecting similar entries for file: " + this.provider.getUuid().toString() + "->" + this.providerFileId);
			Record result = DSL.using(HoardConfiguration
					                          .getJooqConfiguration(conn)).select(PROVIDERFILES.ID, PROVIDERFILES.UUID).from(PROVIDERFILES)
					.where(PROVIDERFILES.PROVIDERFILEID.eq(this.providerFileId).and(PROVIDERFILES.NAME.eq(this.name))).fetchOne();

			if (result != null) {
				this.databaseId = result.get(PROVIDERFILES.ID);
				this.uuid = UUID.fromString(result.get(PROVIDERFILES.UUID));
				PROVIDER_FILE_LOGGER.info("Detected similar entry, updating current file: " + this.uuid.toString());
				this.isPersisted = true;
			}
		} catch (DataAccessException e) {
			PROVIDER_FILE_LOGGER.error("Failed to attempt loading file from database: " + this.providerFileId.toString(), e);
		}

		return this.isPersisted;
	}

	public boolean updatePersistence() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			return updatePersistence(conn);
		} catch (SQLException e) {
			return this.isPersisted;
		}
	}

	public boolean persist() {
		try (Connection conn = DatabaseConnector.getConnection()) {
			if (this.isPersisted) {
				PROVIDER_FILE_LOGGER.info("Updating provider file: " + this.uuid.toString());
				UpdateSetMoreStep<
						ProviderfilesRecord> updateQuery = DSL.using(HoardConfiguration
								                                             .getJooqConfiguration(conn)).update(PROVIDERFILES).set(PROVIDERFILES.UUID, this.uuid.toString());
				updateQuery.set(PROVIDERFILES.PROVIDERUUID, this.provider.getUuid().toString());
				updateQuery.set(PROVIDERFILES.NAME, this.name);
				updateQuery.set(PROVIDERFILES.MIMETYPE, this.mimeType);
				updateQuery.set(PROVIDERFILES.DESCRIPTION, this.description);
				updateQuery.set(PROVIDERFILES.STATUS, this.status);
				updateQuery.set(PROVIDERFILES.PROVIDERFILEID, this.providerFileId);
				updateQuery.set(PROVIDERFILES.TYPE, this.fileType.getId());
				updateQuery.set(PROVIDERFILES.DATEUPDATED, this.dateUpdated);
				updateQuery.set(PROVIDERFILES.SIZE, this.size);
				updateQuery.set(PROVIDERFILES.HASH, this.fileHashes.get(this.provider.getDefaultHashType()));

				updateQuery.where(PROVIDERFILES.ID.eq(this.databaseId)).execute();
				PROVIDER_FILE_LOGGER.info("Finished generating update query: " + this.uuid.toString());
				// ProviderCache.providerFiles().invalidate(this.uuid);
				conn.close();
				if (this.parentFiles != null) {
					PROVIDER_FILE_LOGGER.info("Verifying parents for file: " + this.uuid.toString());
					verifyParents();
				} else {
					PROVIDER_FILE_LOGGER.info("No parents available, skipping file: " + this.uuid.toString());
				}
			} else {
				PROVIDER_FILE_LOGGER.info("Inserting provider file: " + this.uuid.toString());
				DSL.using(HoardConfiguration.getJooqConfiguration(conn)).insertInto(PROVIDERFILES)
						.columns(PROVIDERFILES.UUID, PROVIDERFILES.NAME, PROVIDERFILES.DESCRIPTION, PROVIDERFILES.STATUS, PROVIDERFILES.PROVIDERFILEID, PROVIDERFILES.TYPE,
								PROVIDERFILES.SIZE, PROVIDERFILES.HASH, PROVIDERFILES.PROVIDERUUID, PROVIDERFILES.MIMETYPE)
						.values(this.uuid.toString(), this.name, this.description, this.status, this.providerFileId, this.getFileType().getId(), this.getSize(),
								this.fileHashes.get(provider.getDefaultHashType()), this.provider.getUuid().toString(), this.mimeType)
						.execute();
				this.isPersisted = true;
				// ProviderCache.providerFiles().invalidate(this.uuid);
				PROVIDER_FILE_LOGGER.info("Preparing to re-load file: " + this.uuid.toString());
				conn.close();
				load();
			}

			return true;
		} catch (DataAccessException | SQLException e) {
			PROVIDER_FILE_LOGGER.error("Failed to persist provider file: " + this.uuid.toString(), e);
			return false;
		}
	}

	public <T> Future<Boolean> persistField(Field<T> field, T value) {
		return PRIMARY_THREAD_POOL.submit(() -> {
			PROVIDER_FILE_LOGGER.info("Updating field for provider file: " + this.uuid.toString() + "->" + value.toString());
			if (!this.isPersisted)
				return persist();

			try (Connection conn = DatabaseConnector.getConnection()) {
				DSL.using(HoardConfiguration
						          .getJooqConfiguration(conn)).update(PROVIDERFILES).set(field, value).where(PROVIDERFILES.ID.eq(this.databaseId))
						.execute();

				// ProviderCache.providerFiles().invalidate(this.uuid);
			} catch (DataAccessException e) {
				PROVIDER_FILE_LOGGER.error("Failed to update Provider File: " + this.uuid.toString(), e);
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
						          .getJooqConfiguration(conn)).deleteFrom(PROVIDERFILES).where(PROVIDERFILES.ID.eq(this.databaseId)).execute();
				ProviderCache.providerFiles().invalidate(this.uuid);
			}
		} catch (DataAccessException | SQLException e) {
			PROVIDER_FILE_LOGGER.error("Failed to delete provider: " + this.uuid.toString(), e);
		}
	}

	private boolean verifyParents() {
		PROVIDER_FILE_LOGGER.info("Verifying parent files for file: " + this.uuid.toString() + " (" + this.name + ")");
		List<ProviderFileRelationship> currentRelationships = ProviderCache.getFileRelationships(this.uuid);
		for (ProviderFileRelationship file : this.parentFiles) {
			boolean isFound = false;
			for (ProviderFileRelationship relationship : currentRelationships)
				if (file.getParentUuid().equals(relationship.getParentUuid()))
					isFound = true;

			if (!isFound)
				new ProviderFileRelationship(this.uuid, file.getParentUuid(), true);
		}

		for (ProviderFileRelationship relationship : currentRelationships) {
			boolean isFound = false;
			for (ProviderFileRelationship file : this.parentFiles)
				if (file.getParentUuid().equals(relationship.getParentUuid()))
					isFound = true;

			if (!isFound) {
				relationship.delete();
				ProviderCache.providerFileRelationships().invalidate(this.uuid);
			}
		}

		return true;
	}

	protected abstract FileClass self();

	/**
	 * @return the databaseId
	 */
	public int getDatabaseId() {
		return databaseId;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}

	/**
	 * @return the provider
	 */
	public ProviderClass getProvider() {
		return provider;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * @return the providerFileId
	 */
	public String getProviderFileId() {
		return providerFileId;
	}

	/**
	 * @return the parentFile
	 */
	public List<ProviderFileRelationship> getParentFiles() {
		return parentFiles;
	}

	/**
	 * @return the fileType
	 */
	public FileType getFileType() {
		return fileType;
	}

	/**
	 * @return the dateCreated
	 */
	public Timestamp getDateCreated() {
		return dateCreated;
	}

	/**
	 * @return the dateUpdated
	 */
	public Timestamp getDateUpdated() {
		return dateUpdated;
	}

	/**
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * 
	 * @param hashType
	 * @return The hash of a file in the given format. Returns null if unsupported
	 */
	public String getHash(HashType hashType) {
		return fileHashes.get(hashType);
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public synchronized FileClass setUuid(UUID uuid) {
		this.uuid = uuid;
		return self();
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public synchronized FileClass setProvider(ProviderClass provider) {
		this.provider = provider;
		return self();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public synchronized FileClass setName(String name) {
		this.name = name;
		return self();
	}

	public synchronized FileClass setMimeType(String mimeType) {
		this.mimeType = mimeType;
		return self();
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public synchronized FileClass setDescription(String description) {
		this.description = description;
		return self();
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public synchronized FileClass setStatus(Byte status) {
		this.status = status;
		return self();
	}

	/**
	 * @param providerFileId
	 *            the providerFileId to set
	 */
	public synchronized FileClass setProviderFileId(String providerFileId) {
		this.providerFileId = providerFileId;
		return self();
	}

	/**
	 * @param parentFileRelationships
	 *            the parentFile to set
	 */
	public synchronized FileClass setParentFiles(List<ProviderFileRelationship> parentFileRelationships) {
		this.parentFiles = parentFileRelationships;
		return self();
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public synchronized FileClass setFileType(FileType fileType) {
		this.fileType = fileType;
		return self();
	}

	/**
	 * @param dateCreated
	 *            the dateCreated to set
	 */
	public synchronized FileClass setDateCreated(Timestamp dateCreated) {
		this.dateCreated = dateCreated;
		return self();
	}

	/**
	 * @param dateUpdated
	 *            the dateUpdated to set
	 */
	public synchronized FileClass setDateUpdated(Timestamp dateUpdated) {
		this.dateUpdated = dateUpdated;
		return self();
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public synchronized FileClass setSize(Long size) {
		this.size = size;
		return self();
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public synchronized FileClass persistUuid(UUID uuid) {
		setUuid(uuid);
		persistField(PROVIDERFILES.UUID, this.uuid.toString());
		return self();
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public synchronized FileClass persistProvider(ProviderClass provider) {
		setProvider(provider);
		persistField(PROVIDERFILES.PROVIDERUUID, provider.getUuid().toString());
		return self();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public synchronized FileClass persistName(String name) {
		setName(name);
		persistField(PROVIDERFILES.NAME, name);
		return self();
	}

	public synchronized FileClass persistMimeType(String mimeType) {
		setMimeType(mimeType);
		persistField(PROVIDERFILES.MIMETYPE, mimeType);
		return self();
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public synchronized FileClass persistDescription(String description) {
		setDescription(description);
		persistField(PROVIDERFILES.DESCRIPTION, description);
		return self();
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public synchronized FileClass persistStatus(Byte status) {
		setStatus(status);
		persistField(PROVIDERFILES.STATUS, status);
		return self();
	}

	/**
	 * @param providerFileId
	 *            the providerFileId to set
	 */
	public synchronized FileClass persistProviderFileId(String providerFileId) {
		setProviderFileId(providerFileId);
		persistField(PROVIDERFILES.PROVIDERFILEID, providerFileId);
		return self();
	}

	/**
	 * @param parentFileRelationships
	 *            the parentFile to set
	 */
	public synchronized FileClass persistParentFiles(List<ProviderFileRelationship> parentFileRelationships) {
		setParentFiles(parentFiles);
		if (parentFiles != null)
			verifyParents();
		return self();
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public synchronized FileClass persistFileType(FileType fileType) {
		setFileType(fileType);
		persistField(PROVIDERFILES.TYPE, fileType.getId());
		return self();
	}

	/**
	 * @param dateCreated
	 *            the dateCreated to set
	 */
	public synchronized FileClass persistDateCreated(Timestamp dateCreated) {
		setDateCreated(dateCreated);
		persistField(PROVIDERFILES.DATECREATED, dateCreated);
		return self();
	}

	/**
	 * @param dateUpdated
	 *            the dateUpdated to set
	 */
	public synchronized FileClass persistDateUpdated(Timestamp dateUpdated) {
		setDateUpdated(dateUpdated);
		persistField(PROVIDERFILES.DATEUPDATED, dateUpdated);
		return self();
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public synchronized FileClass persistSize(Long size) {
		setSize(size);
		persistField(PROVIDERFILES.SIZE, size);
		return self();
	}

	/**
	 * 
	 * @return whether the file is persisted
	 */
	public boolean isPersisted() {
		return isPersisted;
	}

	/**
	 * @return the fileHashes
	 */
	public Map<HashType, String> getFileHashes() {
		return fileHashes;
	}

	/**
	 * @param fileHashes
	 *            the fileHashes to set
	 */
	public synchronized FileClass setFileHashes(Map<HashType, String> fileHashes) {
		this.fileHashes = fileHashes;
		return self();
	}

	public String putHash(HashType hashType, String hash) {
		return fileHashes.put(hashType, hash);
	}

	public static final String VIRTUAL_PATH_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}:\\/([^\\/\\s]+\\s*(\\/|\\\\)*)+";
	public static final String SEPARATOR_REGEX = "\\/|\\\\";

	/*
	 * public static UUID parseVirtualPathToUuid(String virtualPath) { if
	 * (virtualPath.matches(VIRTUAL_PATH_REGEX)) { org.jooq.HoardConfiguration config =
	 * HoardConfiguration.getJooqConfiguration();
	 * 
	 * String providerUuid = virtualPath.substring(0, virtualPath.indexOf(":/"));
	 * ProviderService providerService =
	 * ProviderCache.getProvider(UUID.fromString(providerUuid)).getAuthorization().
	 * getProviderService();
	 * 
	 * String searchUuid = null; for (String fileName :
	 * virtualPath.substring(virtualPath.indexOf(":/")).split(SEPARATOR_REGEX)) {
	 * try {
	 * 
	 * ResultSet rs =
	 * DSL.using(config).select(PROVIDERFILERELATIONSHIPS.file).from(tables)
	 * 
	 * ResultSet rs =
	 * DSL.using(config).select(PROVIDERFILES.UUID).from(PROVIDERFILES)
	 * .where(PROVIDERFILES.PARENTUUID.eq(searchUuid).and(PROVIDERFILES.NAME.eq(
	 * fileName))).fetchResultSet(); if (rs.next()) searchUuid =
	 * rs.getString(PROVIDERFILES.UUID.toString()); } catch (NullPointerException |
	 * DataAccessException | SQLException e) {
	 * PROVIDER_FILE_LOGGER.error("Failed to retrieve file from cache: " +
	 * virtualPath, e); return null; } }
	 * 
	 * return UUID.fromString(searchUuid); }
	 * 
	 * return null; }
	 * 
	 * public static ProviderFile parseVirtualPath(String virtualPath) { return
	 * ProviderCache.getProviderFile(parseVirtualPathToUuid(virtualPath)); }
	 * 
	 * public static ProviderFile parseVirtualPath(UUID providerUuid, String path) {
	 * return parseVirtualPath(String.format("%s:/%s", providerUuid.toString(),
	 * path)); }
	 */
}
