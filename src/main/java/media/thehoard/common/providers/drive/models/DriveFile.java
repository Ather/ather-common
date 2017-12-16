/**
 * 
 */
package media.thehoard.common.providers.drive.models;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import media.thehoard.common.providers.drive.DriveConfiguration;
import media.thehoard.common.providers.drive.Drive;
import media.thehoard.common.providers.drive.authorization.DriveAuthorization;
import media.thehoard.common.providers.drive.authorization.DriveCredential;
import media.thehoard.common.providers.drive.service.DriveService;
import media.thehoard.common.providers.generic.models.ProviderFile;
import media.thehoard.common.providers.generic.models.ProviderFileRelationship;

/**
 * @author Michael Haas
 *
 */
public class DriveFile extends ProviderFile<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService> {
	private com.google.api.services.drive.model.File driveFile;

	public DriveFile(Drive drive, com.google.api.services.drive.model.File driveFile, boolean attemptLoad, boolean persist) {
		super(drive);

		this.driveFile = driveFile;

		PROVIDER_FILE_LOGGER.info("Updating temporary DriveFile with information: " + driveFile.getId());
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

		if (attemptLoad)
			this.updatePersistence();

		if (this.getUuid() == null && persist) {
			this.setUuid(UUID.randomUUID());
			PROVIDER_FILE_LOGGER.info("File not persisted, generating parents: " + this.getUuid().toString());
			this.setParentFiles(generateParentFiles(driveFile.getParents()));
		}

		if (persist) {
			PROVIDER_FILE_LOGGER.info("Persisting file: " + this.getUuid().toString());
			this.persist();
			PROVIDER_FILE_LOGGER.info("Finished persisting file: " + this.getUuid().toString());
		}
	}
	
	public DriveFile(Drive drive, com.google.api.services.drive.model.File driveFile, boolean doPersist) {
		this(drive, driveFile, doPersist, doPersist);
	}

	public DriveFile(Drive drive, com.google.api.services.drive.model.File driveFile) {
		this(drive, driveFile, true, true);
		PROVIDER_FILE_LOGGER.info("Finished persisting file via alternate constructor: " + this.getUuid().toString());
	}

	/**
	 * @param parents
	 * @return
	 */
	private List<ProviderFileRelationship> generateParentFiles(List<String> parentIds) {
		if (parentIds != null && !parentIds.isEmpty()) {
			try {
				List<ProviderFileRelationship> parentFiles = new ArrayList<>();
				for (String fileId : parentIds)
					parentFiles.add(new ProviderFileRelationship(this.getUuid(), this.getProvider().getAuthorization().getProviderService().files().get(fileId).getUuid(), true));
			} catch (IOException e) {
				PROVIDER_FILE_LOGGER.error("Failed to get parent file for file: " + this.getUuid(), e);
			}
		}

		return null;
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

	@Override
	protected DriveFile self() {
		return this;
	}

	private Byte getStatusId() {
		if (driveFile.getTrashed())
			return FileStatus.TRASHED;
		if (driveFile.getCapabilities().getCanDownload())
			return FileStatus.AVAILABLE;
		else
			return FileStatus.UNAVAILABLE;
	}
}
