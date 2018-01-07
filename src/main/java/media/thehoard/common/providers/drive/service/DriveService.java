/**
 *
 */
package media.thehoard.common.providers.drive.service;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import media.thehoard.common.configuration.HoardConfiguration;
import media.thehoard.common.database.DatabaseConnector;

import static media.thehoard.common.database.jooq.Tables.PROVIDERFILES;

import media.thehoard.common.providers.ProviderCache;
import media.thehoard.common.providers.drive.authorization.DriveCredential;
import media.thehoard.common.providers.generic.models.ProviderFileRelationship;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jooq.Record1;
import org.jooq.Result;
import org.jooq.impl.DSL;
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

	public Drive getProvider() {
		return this.driveProvider;
	}

	public static <Result> Result executeRequest(ExponentialBackOff backoff, DriveRequest<Result> request) throws
			IOException {
		request.buildHttpRequest()
				.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(DEFAULT_BACKOFF));
		try {
			return request.execute();
		} catch (SocketTimeoutException e) {
			DRIVE_SERVICE_LOGGER.warn("Failed to execute request. Attempting to recover.", e);
			//TODO Test this more, It could still result in an error again, or may not work at all.
			return request.execute();
		}
	}

	public static <Result> Result executeRequest(DriveRequest<Result> request) throws IOException {
		return executeRequest(DEFAULT_BACKOFF, request);
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

			try {
				update();
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER.error("Failed to update cache after build.", e);
			}
		}

		@Override
		public void verify() {
			try {
				update();
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER.error("Failed to update cache.", e);
			}

			com.google.api.services.drive.Drive.Files.List request = null;
			try {
				request = driveService.files().list().setIncludeTeamDriveItems(true).setSupportsTeamDrives(true)
						.setFields("nextPageToken, files(id)").setPageSize(1000);
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.error("An error occurred while verifying the provider cache: " + driveProvider.getUuid()
								.toString(), e);
			}
			do {
				try (Connection conn = DatabaseConnector.getConnection()) {
					FileList resultFiles = executeRequest(request);

					for (com.google.api.services.drive.model.File file : resultFiles.getFiles()) {
						DRIVE_SERVICE_LOGGER.info("Verifying file: " + file.getId());
						Record1<String> stringRecord1 = DSL.using(HoardConfiguration.getJooqConfiguration(conn))
								.select(PROVIDERFILES.UUID)
								.from(PROVIDERFILES)
								.where(PROVIDERFILES.PROVIDERFILEID.eq(file.getId()))
								.fetchAny();
						if (stringRecord1 == null || stringRecord1.get(PROVIDERFILES.UUID) == null) {
							DRIVE_SERVICE_LOGGER.info("File not in cache: " + file.getId() + ", adding...");
							new DriveFile(driveProvider, executeRequest(driveService.files().get(file.getId()).setFields(PARTIAL_FIELDS).setSupportsTeamDrives(true)), false, true);
						} else
							DRIVE_SERVICE_LOGGER
									.info("Verified file: " + file.getId());
						// TODO OnCacheFileProcessed event
					}

					if (request != null)
						request.setPageToken(resultFiles.getNextPageToken());
				} catch (IOException | SQLException e) {
					DRIVE_SERVICE_LOGGER
							.error("An error occurred while verifying the provider cache: " + driveProvider.getUuid()
									.toString(), e);
					if (request != null)
						request.setPageToken(null);
				}
			} while ((request != null ? request.getPageToken() : null) != null && request.getPageToken().length() > 0);

			try {
				update();
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER.error("Failed to update cache after build.", e);
			}

			// TODO OnCacheCompleted event
		}

		@Override
		public void update() throws IOException {
			changes().process();
		}

		@Override
		public void update(DriveFile updatedFile, boolean updateRemote) throws IOException {
			updatedFile.updatePersistence();
			updatedFile.persist();
			if (updateRemote)
				executeRequest(driveService.files().update(updatedFile.getProviderFileId(), updatedFile.toDriveFile()));
			// TODO OnCacheFileProcessed event
		}

		@Override
		public void updateParents(DriveFile driveFile, List<UUID> removeParents, List<UUID> addParents, boolean updateRemote) {
			if (removeParents != null) {
				for (UUID remove : removeParents) {
					for (ProviderFileRelationship relat : driveFile.getParentFiles()) {
						if (relat.getParentUuid().equals(remove)) {
							relat.delete();
							ProviderCache.providerFileRelationships().invalidate(driveFile.getUuid());
							driveFile.getParentFiles().remove(relat);
						}
					}
				}
			}

			if (addParents != null) {
				nextAdd:
				for (UUID add : addParents) {
					for (ProviderFileRelationship relat : driveFile.getParentFiles())
						if (relat.getParentUuid().equals(add)) continue nextAdd;
					driveFile.getParentFiles().add(new ProviderFileRelationship(driveFile.getUuid(), add, true));
					ProviderCache.providerFileRelationships().invalidate(driveFile.getUuid());
				}
			}

			if (updateRemote) {
				try {
					String additions = addParents == null ? null : String.join(", ", addParents.stream()
							.map(e -> ProviderCache.getProviderFile(e).getProviderFileId())
							.collect(Collectors.toList()));
					String removals = removeParents == null ? null : String.join(", ", addParents.stream()
							.map(e -> ProviderCache.getProviderFile(e).getProviderFileId())
							.collect(Collectors.toList()));
					executeRequest(driveService.files().update(driveFile.getProviderFileId(), null)
							               .setAddParents(additions).setRemoveParents(removals));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//TODO improve on detection by using a tree setup.
		public DriveFile parsePath(DriveFile root, String path, boolean createNew) throws IOException {
			DriveFile tempFile = root;

			for (String section : path.split("/")) {
				List<UUID> children = ProviderCache.getChildUuids(tempFile.getUuid());
				if (children.isEmpty() && createNew) {
					tempFile = new DriveFile(driveProvider, executeRequest(driveService.files()
							                                                       .create(new com.google.api.services.drive.model.File()
									                                                               .setName(section)
									                                                               .setMimeType("application/vnd.google-apps.folder")
									                                                               .setParents(Collections
											                                                                           .singletonList(tempFile.getProviderFileId())))), false, true);
				} else
					tempFile = new DriveFile(children.get(0));
			}

			return tempFile;
		}

		public DriveFile parsePath(String path, boolean createNew) throws IOException {
			return parsePath(files().get("root", false), path, createNew);
		}

		public DriveFile parsePath(String path) throws IOException {
			return parsePath(path, false);
		}

		//TODO
		public DriveFile getFile(String providerFileId) {
			try (Connection conn = DatabaseConnector.getConnection()) {
				return new DriveFile(UUID.fromString(DSL.using(HoardConfiguration.getJooqConfiguration(conn))
						                                     .select(PROVIDERFILES.UUID)
						                                     .where(PROVIDERFILES.PROVIDERUUID
								                                            .eq(driveProvider.getUuid().toString())
								                                            .and(PROVIDERFILES.PROVIDERFILEID
										                                                 .eq(providerFileId)))
						                                     .fetchAny().get(PROVIDERFILES.UUID)));
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
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
				for (Change change : fileChanges.getChanges())
					new DriveFile(driveProvider, change.getFile());
				if (fileChanges.getNewStartPageToken() != null)
					driveProvider.persistChangeToken(fileChanges.getNewStartPageToken());
				pageToken = fileChanges.getNextPageToken();
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
		public DriveFile get(String providerFileId, boolean attemptLoad, boolean doPersist) throws IOException {
			DRIVE_SERVICE_LOGGER.info("Getting ProviderFile from drive service: " + providerFileId);
			return new DriveFile(driveProvider, executeRequest(driveService.files().get(providerFileId)
					                                                   .setSupportsTeamDrives(true)
					                                                   .setFields(PARTIAL_FIELDS)), attemptLoad,
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
				//TODO Synchronize
				ProviderCache.providerFiles().invalidate(file.getUuid());
				ProviderCache.providerFileRelationships().invalidate(file.getUuid());
				file.getParentFiles().forEach(ProviderFileRelationship::delete);
				file.delete();
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

		@Override
		public DriveFile create(DriveFile file, boolean doPersist) throws IOException {
			return new DriveFile(driveProvider, executeRequest(driveService.files().create(file.toDriveFile())
					                                                   .setFields(PARTIAL_FIELDS)
					                                                   .setSupportsTeamDrives(true)), false, doPersist);
		}
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
