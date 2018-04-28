/**
 *
 */
package com.atherapp.common.providers.drive;

import com.atherapp.common.providers.drive.authorization.DriveAuthorization;
import com.atherapp.common.providers.drive.authorization.DriveCredential;
import com.atherapp.common.providers.drive.models.DriveFile;
import com.atherapp.common.providers.drive.service.DriveService;
import com.atherapp.common.providers.generic.Provider;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.util.hashes.HashType;
import com.google.api.services.drive.model.About;
import com.atherapp.common.providers.drive.authorization.DriveAuthorization;
import com.atherapp.common.providers.drive.authorization.DriveCredential;
import com.atherapp.common.providers.generic.Provider;
import com.atherapp.common.providers.generic.models.ProviderFile;
import com.atherapp.common.util.hashes.HashType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Michael Haas
 */
public class Drive
		extends Provider<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService> {
	public static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
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
	public ProviderFile.FileType getFileType(String mimeType) {
		switch (mimeType) {
			case FOLDER_MIME_TYPE:
				return ProviderFile.FileType.Directory;
			default:
				return ProviderFile.FileType.File;
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
