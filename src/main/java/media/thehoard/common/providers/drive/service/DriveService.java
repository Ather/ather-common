/**
 *
 */
package media.thehoard.common.providers.drive.service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import media.thehoard.common.providers.drive.authorization.DriveCredential;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.FileList;

import media.thehoard.common.providers.drive.DriveConfiguration;
import media.thehoard.common.providers.drive.Drive;
import media.thehoard.common.providers.drive.authorization.DriveAuthorization;
import media.thehoard.common.providers.drive.models.DriveFile;
import media.thehoard.common.providers.generic.service.ProviderService;

/**
 * @author Michael Haas
 */
public class DriveService extends
		ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService> {
	public static Logger DRIVE_SERVICE_LOGGER = LoggerFactory.getLogger(DriveService.class);

	public static final String APPLICATION_NAME = "The Hoard Media Server Google Drive HoardPlugin";

	private static final String PARTIAL_FIELDS = "id, name, mimeType, description, trashed, parents, createdTime, modifiedTime, teamDriveId, md5Checksum, size, teamDriveId, capabilities(canDownload)";
	private static final String LIST_PARTIAL_FIELDS = String.format("nextPageToken, files(%s)", PARTIAL_FIELDS);
	private static final String CHANGE_PARTIAL_FIELDS = String
			.format("newStartPageToken, changes(file(%s))", PARTIAL_FIELDS);

	private static final ExponentialBackOff DEFAULT_BACKOFF = new ExponentialBackOff.Builder()
			.setInitialIntervalMillis(50)
			.setMaxElapsedTimeMillis((int) TimeUnit.MINUTES.toMillis(5))
			.setMaxIntervalMillis((int) TimeUnit.SECONDS.toMillis(6)).setMultiplier(1.5).setRandomizationFactor(0.5)
			.build();

	private Drive driveProvider;
	private com.google.api.services.drive.Drive driveService;
	private HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();

	public DriveService(Drive drive, DriveCredential credential) {
		this.driveProvider = drive;
		try {
			this.driveService = new com.google.api.services.drive.Drive.Builder(GoogleNetHttpTransport
					                                                                    .newTrustedTransport(), JacksonFactory
					                                                                    .getDefaultInstance(), credential
					                                                                    .getCredential(drive))
					.setApplicationName(APPLICATION_NAME).build();
		} catch (GeneralSecurityException | IOException e) {
			DRIVE_SERVICE_LOGGER.error("Failed to initialize drive service: ", e);
		}
	}

	public DriveService(Drive drive) {
		this(drive, drive.getCredential());
	}

	private <Result> Result executeRequest(DriveRequest<Result> request) throws IOException {
		request.buildHttpRequest()
				.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(DEFAULT_BACKOFF));
		return request.execute();
	}

	@Override
	public About about() {
		return new About();
	}

	public class About extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.About {

	}

	@Override
	public Cache cache() {
		return new Cache();
	}

	public class Cache extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.Cache<DriveFile> {
		@Override
		public void build() {
			try {
				driveProvider.persistChangeToken(changes().getChangeToken());
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.error("Failed to build cache, couldn't store change token: " + driveProvider.getUuid(), e);
			}

			com.google.api.services.drive.Drive.Files.List request = null;
			try {
				request = driveService.files().list().setIncludeTeamDriveItems(true).setSupportsTeamDrives(true)
						.setFields(LIST_PARTIAL_FIELDS).setPageSize(1000);
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.error("An error occurred while building the provider cache: " + driveProvider.getUuid()
								.toString(), e);
			}
			do {
				try {
					FileList resultFiles = executeRequest(request);

					for (com.google.api.services.drive.model.File file : resultFiles.getFiles()) {
						DRIVE_SERVICE_LOGGER.info("Processing file in cache: " + file.getId() + "->" + file.getName());
						DriveFile driveFile = new DriveFile(driveProvider, file);
						DRIVE_SERVICE_LOGGER
								.info("Finished processing file: " + driveFile.getUuid() + "->" + driveFile.getName());
						// TODO OnCacheFileProcessed event
					}

					if (request != null)
						request.setPageToken(resultFiles.getNextPageToken());
				} catch (IOException e) {
					DRIVE_SERVICE_LOGGER
							.error("An error occurred while building the provider cache: " + driveProvider.getUuid()
									.toString(), e);
					if (request != null)
						request.setPageToken(null);
				}
			} while ((request != null ? request.getPageToken() : null) != null && request.getPageToken().length() > 0);

			// TODO OnCacheCompleted event
		}

		@Override
		public void update() throws IOException {
			changes().process();
		}

		@Override
		public void update(DriveFile updatedFile) {
			// TODO OnCacheFileProcessed event
		}
	}

	@Override
	public Changes changes() {
		return new Changes();
	}

	public class Changes extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.Changes {
		@Override
		public String getChangeToken() throws IOException {
			return executeRequest(driveService.changes().getStartPageToken()).getStartPageToken();
		}

		@Override
		public void process() throws IOException {
			String pageToken = driveProvider.getChangeToken();
			while (pageToken != null) {
				ChangeList fileChanges = executeRequest(
						driveService.changes().list(pageToken).setFields(CHANGE_PARTIAL_FIELDS).setPageSize(1000)
								.setSupportsTeamDrives(true).setIncludeTeamDriveItems(true));
				for (Change change : fileChanges.getChanges()) {
					cache().update(new DriveFile(driveProvider, change.getFile()));
				}
			}
		}
	}

	@Override
	public Files files() {
		return new Files();
	}

	public class Files extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.Files<DriveFile> {
		@Override
		public DriveFile get(String providerFileId, boolean doPersist) throws IOException {
			DRIVE_SERVICE_LOGGER.info("Getting ProviderFile from drive service: " + providerFileId);
			return new DriveFile(driveProvider, executeRequest(driveService.files().get(providerFileId)
					                                                   .setSupportsTeamDrives(true)
					                                                   .setFields(PARTIAL_FIELDS)), doPersist,
			                     doPersist);
		}

		@Override
		public DriveFile mkdir(String virtualPath) {
			return null;
		}

		@Override
		public List<DriveFile> ls(String virtualPath) {
			return null;
		}

		@Override
		public byte[] cat(DriveFile file, long byteStart, long byteEnd) throws IOException {
			return IOUtils.toByteArray(client.execute(RequestBuilder.get(getTemporaryUrl(file))
					                                          .addHeader("Range", String
							                                          .format("Bytes=%d-%d", byteStart, byteEnd))
					                                          .build())
					                           .getEntity().getContent());
		}

		@Override
		public File catToTempFile(DriveFile file, long byteStart, long byteEnd) {
			try {
				File tempFile = File
						.createTempFile("thehoardmedia-driveservice-temp-", String.valueOf(System.currentTimeMillis()));
				FileUtils.writeByteArrayToFile(tempFile, cat(file, byteStart, byteEnd));
				return tempFile;
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER.error("Failed to download chunk of Google Drive File to temporary file", e);
			}

			return null;
		}

		@Override
		public boolean rm(DriveFile file) {
			try {
				executeRequest(driveService.files()
						               .update(file.getProviderFileId(), new com.google.api.services.drive.model.File()
								               .setTrashed(true)).setSupportsTeamDrives(true));
				return true;
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER.error("Failed to delete Google Drive file", e);
			}
			return false;
		}

		@Override
		public String getTemporaryUrl(DriveFile file, boolean doProxy) {
			if (!doProxy) {
				try {
					return client.execute(RequestBuilder
							                      .head(String.format("https://www.googleapis.com/drive/v3/files/%s?alt=media&access_token=%s", file
									                                          .getProviderFileId(),
							                                          driveProvider.getCredential()
									                                          .getCredential(driveProvider)
									                                          .getAccessToken())).build())
							.getHeaders("Location")[0].getValue();
				} catch (IOException e) {
					DRIVE_SERVICE_LOGGER.error("Failed to generate temporary URL for Google Drive", e);
				}
			}

			// TODO Implement file proxy via Spring
			return null;
		}

		/*
		 * @Override public String generateVirtualPath(String providerFileId) {
		 * driveService.files().get(providerFileId).setFields(PARTIAL_FIELDS); }
		 */
	}

	@Override
	public Permissions permissions() {
		return new Permissions();
	}

	public class Permissions extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.Permissions {

	}

	/**
	 * This is a temporary return method, which should be removed once the file service is fully implemented.
	 *
	 * @return Google Drive API service
	 */
	@Deprecated
	public com.google.api.services.drive.Drive getDriveService() {
		return this.driveService;
	}
}
