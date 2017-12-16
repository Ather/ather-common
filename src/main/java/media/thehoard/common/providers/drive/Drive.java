/**
 * 
 */
package media.thehoard.common.providers.drive;

import java.util.UUID;

import media.thehoard.common.providers.drive.authorization.DriveCredential;
import media.thehoard.common.providers.generic.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.drive.model.About;

import media.thehoard.common.providers.drive.authorization.DriveAuthorization;
import media.thehoard.common.providers.drive.models.DriveFile;
import media.thehoard.common.providers.drive.service.DriveService;
import media.thehoard.common.providers.generic.models.ProviderFile.FileType;
import media.thehoard.common.util.hashes.HashType;

/**
 * @author Michael Haas
 *
 */
public class Drive extends
		Provider<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService> {
	public static Logger DRIVE_LOGGER = LoggerFactory.getLogger(Drive.class);

	/**
	 * @param uuid
	 */
	public Drive(UUID uuid) {
		super(uuid);
		this.setAuthorization(new DriveAuthorization(this));
		this.load();
	}
	
	public Drive update(DriveAuthorization authorization, DriveCredential credential, About about) {
		//TODO Allow modifying/passing this via the web client
		this.setAlias(about.getUser().getEmailAddress());
		this.setProviderAlias(about.getUser().getPermissionId());
		this.setStatus(ProviderStatus.DISABLED);
		this.setCredential(credential);
		this.setTypeId(ProviderTypes.DRIVE);
		this.setUsageId(ProviderUsages.REPOSITORY);
		this.setAuthorization(authorization);
		
		this.persist();
		
		return this;
	}
	
	@Override
	public FileType getFileType(String mimeType) {
		switch (mimeType) {
		case "application/vnd.google-apps.folder":
			return FileType.Directory;
		default:
			return FileType.File;
		}
	}
	
	@Override
	public HashType getDefaultHashType() {
		return HashType.md5;
	}
	
	@Override
	protected Drive self() {
		return this;
	}

	@Override
	protected Class<DriveConfiguration> configurationClass() { return DriveConfiguration.class; }

	@Override
	protected Class<DriveCredential> credentialClass() { return DriveCredential.class; }
}
