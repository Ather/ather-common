/**
 *
 */
package com.atherapp.common.providers.drive.models;

import com.atherapp.common.configuration.AtherConfiguration;
import com.atherapp.common.database.DatabaseConnector;
import com.atherapp.common.providers.drive.Drive;
import com.atherapp.common.providers.drive.DriveConfiguration;
import com.atherapp.common.providers.drive.authorization.DriveAuthorization;
import com.atherapp.common.providers.drive.authorization.DriveCredential;
import com.atherapp.common.providers.drive.service.DriveService;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.models.ProviderFileRelationship;
import com.google.api.services.drive.model.File;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.providers.generic.models.ProviderFileRelationship;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.atherapp.common.database.jooq.Tables.PROVIDERFILES;

/**
 * @author Michael Haas
 */
public class DriveFile
		extends ProviderFile<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService> {
	private com.google.api.services.drive.model.File driveFile;

	public DriveFile(Drive drive, com.google.api.services.drive.model.File driveFile, boolean attemptLoad, boolean persist, boolean forcePersist) {
		super(drive);

		this.driveFile = driveFile;

		PROVIDER_FILE_LOGGER.trace("Updating temporary DriveFile with information: " + driveFile.getId());
		this.setProviderFileId(driveFile.getId());
		this.setName(driveFile.getName());
		this.setMimeType(driveFile.getMimeType());
		this.setDateCreated(new Timestamp(driveFile.getCreatedTime().getValue()));
		this.setDateUpdated(new Timestamp(driveFile.getModifiedTime().getValue()));
		this.setDescription(driveFile.getDescription());
		this.setFileType(drive.getFileType(driveFile.getMimeType()));
		this.putHash(drive.getDefaultHashType(), driveFile.getMd5Checksum());

		this.setSize(driveFile.getSize());
		this.setStatus(getStatusId());

		if (attemptLoad) this.updatePersistence();

		if (this.getUuid() == null && persist) {
			this.setUuid(UUID.randomUUID());
			PROVIDER_FILE_LOGGER.trace("File not persisted, generating parents: " + this.getUuid().toString());
			this.setParentFiles(generateParentFiles(driveFile.getParents()));
		}

		if (forcePersist || (persist && !this.isPersisted())) {
			PROVIDER_FILE_LOGGER.trace("Persisting file: " + this.getUuid().toString());
			this.persist();
			PROVIDER_FILE_LOGGER.trace("Finished persisting file: " + this.getUuid().toString());
		}
	}

	public DriveFile(Drive drive, com.google.api.services.drive.model.File driveFile, boolean attemptLoad, boolean persist) {
		//TODO
		this(drive, driveFile, attemptLoad, persist, false);
	}

	public DriveFile(Drive drive, com.google.api.services.drive.model.File driveFile, boolean doPersist) {
		this(drive, driveFile, doPersist, doPersist);
	}

	public DriveFile(Drive drive, com.google.api.services.drive.model.File driveFile) {
		this(drive, driveFile, true, true);
		PROVIDER_FILE_LOGGER.trace("Finished persisting file via alternate constructor: " + this.getUuid().toString());
	}

	public DriveFile(Drive drive) {
		super(drive);
	}

	/**
	 * @param key
	 */
	public DriveFile(UUID key) {
		super(key);
	}

	/**
	 * @param parentIds
	 *
	 * @return
	 */
	private List<ProviderFileRelationship> generateParentFiles(List<String> parentIds) {
		if (parentIds != null && !parentIds.isEmpty()) {
			try {
				List<ProviderFileRelationship> parentFiles = new ArrayList<>();
				for (String fileId : parentIds) {

					UUID parentUuid = this.getProvider().getAuthorization().getProviderService().files().get(fileId)
							.getUuid();
					parentFiles.add(new ProviderFileRelationship(this.getUuid(), parentUuid, true, true));
				}
			} catch (IOException e) {
				PROVIDER_FILE_LOGGER.error("Failed to get parent file for file: " + this.getUuid(), e);
			}
		}

		return null;
	}

	@Override
	protected DriveFile self() {
		return this;
	}

	private Byte getStatusId() {
		if (driveFile.getTrashed()) return FileStatus.TRASHED;
		if (driveFile.getCapabilities().getCanDownload()) return FileStatus.AVAILABLE;
		else return FileStatus.UNAVAILABLE;
	}

	public File toDriveFile() {
		File tempFile = new File();

		List<String> parents = new ArrayList<>();
		try (Connection conn = DatabaseConnector.getConnection()) {
			for (ProviderFileRelationship relat : this.getParentFiles())
				parents.add(DSL.using(AtherConfiguration.getJooqConfiguration(conn))
						            .select(PROVIDERFILES.PROVIDERFILEID)
						            .where(PROVIDERFILES.UUID.eq(relat.getParentUuid().toString())).fetchOne()
						            .get(PROVIDERFILES.PROVIDERFILEID));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		tempFile.setParents(parents);

		tempFile.setName(this.getName());
		tempFile.setMimeType(this.getMimeType());
		tempFile.setDescription(this.getDescription());
		//tempFile.setId(this.getProviderFileId());
		//tempFile.setTrashed(this.getStatus() == FileStatus.TRASHED);
		//tempFile.setCreatedTime(new DateTime(this.getDateCreated().getTime()));
		//tempFile.setModifiedTime(new DateTime(this.getDateUpdated().getTime()));

		return tempFile;
	}
}
