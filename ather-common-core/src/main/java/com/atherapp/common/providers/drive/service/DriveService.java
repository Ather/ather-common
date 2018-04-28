/**
 *
 */
package com.atherapp.common.providers.drive.service;

import com.atherapp.common.configuration.AtherConfiguration;
import com.atherapp.common.database.DatabaseConnector;
import com.atherapp.common.providers.ProviderCache;
import com.atherapp.common.providers.drive.Drive;
import com.atherapp.common.providers.drive.DriveConfiguration;
import com.atherapp.common.providers.drive.authorization.DriveAuthorization;
import com.atherapp.common.providers.drive.authorization.DriveCredential;
import com.atherapp.common.providers.drive.models.DriveFile;
import com.atherapp.common.providers.generic.models.ProviderFileRelationship;
import com.atherapp.common.providers.generic.service.ProviderService;
import com.atherapp.common.util.ThreadManager;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveRequest;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.FileList;
import com.atherapp.common.providers.drive.models.DriveFile;
import com.atherapp.common.providers.generic.models.ProviderFileRelationship;
import com.atherapp.common.providers.generic.service.ProviderService;
import com.atherapp.common.util.ThreadManager;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jooq.BatchBindStep;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.atherapp.common.database.jooq.Tables.PROVIDERFILERELATIONSHIPS;
import static com.atherapp.common.database.jooq.Tables.PROVIDERFILES;

/**
 * @author Michael Haas
 */
public class DriveService extends
		ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService> {
	public static final String APPLICATION_NAME = "The Hoard Media Server Google Drive HoardPlugin";
	private static final String PARTIAL_FIELDS = "id, name, mimeType, description, trashed, explicitlyTrashed, parents, createdTime, modifiedTime, teamDriveId, md5Checksum, size, teamDriveId, capabilities(canDownload)";
	private static final String LIST_PARTIAL_FIELDS = String.format("nextPageToken, files(%s)", PARTIAL_FIELDS);
	private static final String CHANGE_PARTIAL_FIELDS = String
			.format("nextPageToken, newStartPageToken, changes(removed, fileId, file(%s))", PARTIAL_FIELDS);
	private static final ExponentialBackOff DEFAULT_BACKOFF = new ExponentialBackOff.Builder()
			.setInitialIntervalMillis(500).setMaxElapsedTimeMillis((int) TimeUnit.MINUTES.toMillis(15))
			.setMaxIntervalMillis((int) TimeUnit.MINUTES.toMillis(1)).setMultiplier(1.5).setRandomizationFactor(0.5)
			.build();
	public static Logger DRIVE_SERVICE_LOGGER = LoggerFactory.getLogger(DriveService.class);
	private static ExecutorService asyncRequestPool = Executors.newFixedThreadPool(50);
	public BatchRequest batchRequest;
	private Drive driveProvider;
	private com.google.api.services.drive.Drive driveService;
	private HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
	private boolean doBatchRequests;
	private JsonBatchCallback<com.google.api.services.drive.model.File> fileCallback;

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

	public DriveService(Drive drive, boolean useBatchUpdates) {
		this(drive, drive.getCredential());
		this.doBatchRequests = useBatchUpdates;

		if (useBatchUpdates) {
			this.batchRequest = driveService.batch();

			this.fileCallback = new JsonBatchCallback<>() {
				@Override
				public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
					// Handle error
					DRIVE_SERVICE_LOGGER.warn(e.getMessage());
				}

				@Override
				public void onSuccess(com.google.api.services.drive.model.File file, HttpHeaders responseHeaders) {
					DRIVE_SERVICE_LOGGER.trace("File Updated: " + file.getId());
				}
			};
		}
	}

	public static <Result> Result executeRequest(ExponentialBackOff backoff, DriveRequest<Result> request) throws
			IOException {
		int retryCount = 0;
		while (retryCount < 20) {
			HttpRequest req = request.buildHttpRequest()
					.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(backoff))
					.setNumberOfRetries(30);

			try {
				return req.execute().parseAs(request.getResponseClass());
			} catch (SocketTimeoutException | HttpResponseException e) {
				DRIVE_SERVICE_LOGGER
						.warn("Failed to execute request, attempting to recover (" + retryCount + "). (SocketTimeoutException or HttpResponseException)");

				if (retryCount > 18) throw e;
				//TODO Test this more, It could still result in an error again, or may not work at all.
			}
			retryCount++;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return request.execute();
	}

	public static <Result> Future<Result> executeRequestAsync(DriveRequest<Result> request) throws IOException {
		return asyncRequestPool.submit(() -> executeRequest(DEFAULT_BACKOFF, request));
	}

	public static <Result> Result executeRequest(DriveRequest<Result> request) throws IOException {
		return executeRequest(DEFAULT_BACKOFF, request);
	}

	public Drive getProvider() {
		return this.driveProvider;
	}

	public void queueBatch(DriveRequest<com.google.api.services.drive.model.File> request) throws IOException {
		if (request != null) request.queue(batchRequest, fileCallback);

		synchronized (batchRequest) {
			if (batchRequest.size() > 99 || request == null) {
				BatchRequest req = batchRequest;
				batchRequest = driveService.batch();
				ThreadManager.PRIMARY_THREAD_POOL.submit(() -> {
					int retryCount = 0;
					while (retryCount < 20) {
						try {
							req.execute();
						} catch (IOException e) {
							DRIVE_SERVICE_LOGGER.warn("Failed to execute batch request, retrying: " + retryCount);
						}
						retryCount++;
					}
				});
			}
		}
	}

	@Override
	public About about() {
		return new About();
	}

	@Override
	public Cache cache() {
		return new Cache();
	}

	@Override
	public Changes changes() {
		return new Changes();
	}

	@Override
	public Files files() {
		return new Files();
	}

	@Override
	public Permissions permissions() {
		return new Permissions();
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

	public class About extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.About {

	}

	public class Cache extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.Cache<DriveFile> {
		@Override
		public void build() {
			DRIVE_SERVICE_LOGGER.info("Beginning cache build for " + driveProvider.getAlias());
			try {
				driveProvider.persistChangeToken(changes().getChangeToken());
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.warn("Failed to build cache, couldn't store change token: " + driveProvider.getUuid(), e);
			}

			com.google.api.services.drive.Drive.Files.List request = null;
			try {
				request = driveService.files().list().setIncludeTeamDriveItems(true).setSupportsTeamDrives(true)
						.setFields(LIST_PARTIAL_FIELDS).setPageSize(1000);
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.warn("An error occurred while building the provider cache: " + driveProvider.getUuid()
								.toString(), e);
			}

			ExecutorService fileListThreadPool = Executors.newFixedThreadPool(10);
			ExecutorService persistenceThreadPool = Executors.newFixedThreadPool(10);

			ConcurrentHashMap<String, DriveFile> folders = new ConcurrentHashMap<>();
			ConcurrentHashMap<String, UUID> unassociatedParents = new ConcurrentHashMap<>();

			try (Connection conn = DatabaseConnector.getConnection()) {
				DSLContext create = DSL.using(AtherConfiguration.getJooqConfiguration(conn));

				final BatchBindStep[] fileBatches = {
						create.batch(create.insertInto(PROVIDERFILES, PROVIDERFILES.UUID, PROVIDERFILES.NAME, PROVIDERFILES.DESCRIPTION, PROVIDERFILES.STATUS, PROVIDERFILES.PROVIDERFILEID, PROVIDERFILES.TYPE, PROVIDERFILES.SIZE, PROVIDERFILES.HASH, PROVIDERFILES.PROVIDERUUID, PROVIDERFILES.MIMETYPE, PROVIDERFILES.DATECREATED, PROVIDERFILES.DATEUPDATED)
								             .values((String) null, null, null, null, null, null, null, null, null, null, null, null)),
						create.batch(create.insertInto(PROVIDERFILERELATIONSHIPS, PROVIDERFILERELATIONSHIPS.FILEUUID, PROVIDERFILERELATIONSHIPS.PARENTUUID)
								             .values((String) null, null))};
				// We don't care about locking these values, because if they are slightly incorrect, it doesn't matter.
				final int[] batchCounts = {0, 0}; // I am a bad person for this

				do {
					try {
						FileList resultFiles = executeRequest(request);

						fileListThreadPool.submit(() -> {
							List<com.google.api.services.drive.model.File> remoteFiles = resultFiles.getFiles();
							DRIVE_SERVICE_LOGGER
									.info("Processing " + remoteFiles.size() + " files for provider: " + driveProvider
											.getAlias());
							for (com.google.api.services.drive.model.File file : remoteFiles) {
								UUID randomUuid = UUID.randomUUID();
								DriveFile driveFile = new DriveFile(driveProvider, file, false, false);
								driveFile.setUuid(randomUuid);
								if (file.getMimeType().equals(Drive.FOLDER_MIME_TYPE))
									folders.put(file.getId(), driveFile);

								if (unassociatedParents.containsKey(file.getId())) {
									synchronized (fileBatches[1]) {
										batchCounts[1]++;
										fileBatches[1] = fileBatches[1]
												.bind(unassociatedParents.get(file.getId()), randomUuid.toString());
									}
									unassociatedParents.remove(file.getId());
								}

								for (String parent : file.getParents()) {
									if (!folders.containsKey(parent))
										unassociatedParents.put(parent, driveFile.getUuid());
									else {
										synchronized (fileBatches[1]) {
											batchCounts[1]++;
											fileBatches[1] = fileBatches[1]
													.bind(driveFile.getUuid().toString(), folders.get(parent).getUuid()
															.toString());
										}
									}
								}

								synchronized (fileBatches[0]) {
									batchCounts[0]++;
									fileBatches[0] = fileBatches[0]
											.bind(driveFile.getUuid().toString(), driveFile.getName(), driveFile
													.getDescription(), driveFile.getStatus(), driveFile
													      .getProviderFileId(), driveFile.getFileType()
													      .getId(), driveFile.getSize(), driveFile.getFileHashes()
													      .get(driveProvider.getDefaultHashType()), driveProvider
													      .getUuid().toString(), driveFile.getMimeType(), driveFile
													      .getDateCreated(), driveFile.getDateUpdated());
								}
							}
							DRIVE_SERVICE_LOGGER.info("Finished processing " + remoteFiles
									.size() + " files for provider: " + driveProvider.getAlias());
						});

						if (request != null) request.setPageToken(resultFiles.getNextPageToken());
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (batchCounts[0] > 10000) {
						DRIVE_SERVICE_LOGGER
								.info(String.format("Batch insert count exceeds %d (Files: %d), executing.", 10000, batchCounts[0]));
						persistenceThreadPool.submit(() -> {
							BatchBindStep fileBinds = fileBatches[0];

							synchronized (fileBatches[0]) {
								fileBatches[0] = create
										.batch(create.insertInto(PROVIDERFILES, PROVIDERFILES.UUID, PROVIDERFILES.NAME, PROVIDERFILES.DESCRIPTION, PROVIDERFILES.STATUS, PROVIDERFILES.PROVIDERFILEID, PROVIDERFILES.TYPE, PROVIDERFILES.SIZE, PROVIDERFILES.HASH, PROVIDERFILES.PROVIDERUUID, PROVIDERFILES.MIMETYPE, PROVIDERFILES.DATECREATED, PROVIDERFILES.DATEUPDATED)
												       .values((String) null, null, null, null, null, null, null, null, null, null, null, null));
							}
							batchCounts[0] = 0;

							fileBinds.execute();

							DRIVE_SERVICE_LOGGER.info("Finished inserting rows into database.");
						});
					}

					if (batchCounts[1] > 10000) {
						DRIVE_SERVICE_LOGGER
								.info(String.format("Batch insert count exceeds %d (Relationships: %d), executing.", 10000, batchCounts[1]));
						persistenceThreadPool.submit(() -> {
							BatchBindStep relationshipBinds = fileBatches[1];
							synchronized (fileBatches[1]) {
								fileBatches[1] = create
										.batch(create.insertInto(PROVIDERFILERELATIONSHIPS, PROVIDERFILERELATIONSHIPS.FILEUUID, PROVIDERFILERELATIONSHIPS.PARENTUUID)
												       .values((String) null, null));
							}
							batchCounts[1] = 1;

							relationshipBinds.execute();

							DRIVE_SERVICE_LOGGER.info("Finished inserting rows into database.");
						});
					}
				} while ((request != null ? request.getPageToken() : null) != null && request.getPageToken()
						.length() > 0);

				fileListThreadPool.awaitTermination(3, TimeUnit.HOURS);

				Future fileInserts = fileListThreadPool.submit(fileBatches[0]::execute);
				Future relationshipInserts = fileListThreadPool.submit(fileBatches[1]::execute);

				fileInserts.get();
				relationshipInserts.get();

			} catch (SQLException | InterruptedException | ExecutionException e) {
				DRIVE_SERVICE_LOGGER.warn("Failed to build cache.", e);
			}
			DRIVE_SERVICE_LOGGER.info("Remaining unassociated parents: " + unassociatedParents.size());
			DRIVE_SERVICE_LOGGER.info("Total files processed: " + folders.size());

			unassociatedParents.clear();
			folders.clear();

			try {
				update();
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER.warn("Failed to update cache after build.", e);
			}
		}

		//private com.google.api.services.drive.Drive.Files.List listRequest = null;

		@Override
		public void recursiveBuild(String rootPath) {
			com.google.api.services.drive.Drive.Files.List listRequest = null;
			try {
				listRequest = driveService.files().list().setIncludeTeamDriveItems(true).setSupportsTeamDrives(true)
						.setFields(LIST_PARTIAL_FIELDS).setPageSize(1000);
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.warn("An error occurred while building the provider cache: " + driveProvider.getUuid()
								.toString(), e);
			}

			DRIVE_SERVICE_LOGGER.info("Beginning cache build for " + driveProvider.getAlias());
			try {
				driveProvider.persistChangeToken(changes().getChangeToken());
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.warn("Failed to build cache, couldn't store change token: " + driveProvider.getUuid(), e);
			}

			DriveFile root = null;
			try {
				root = new DriveFile(driveProvider, executeRequest(driveService.files().get("root")
						                                                   .setFields(PARTIAL_FIELDS)
						                                                   .setSupportsTeamDrives(true)), true, true, false);
			} catch (IOException e) {
				e.printStackTrace();
			}

			DriveFile currentFile = root;
			for (String sect : rootPath.split("/")) {
				try {
					currentFile = new DriveFile(driveProvider, executeRequest(listRequest.setQ(driveQ(currentFile
							                                                                                  .getProviderFileId()) + " and name = '" + driveSanitize(sect) + "'"))
							.getFiles().get(0), true, true, false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			recursiveBuilder(currentFile);

			try {
				update();
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER.warn("Failed to update cache after build.", e);
			}
		}

		private String driveQ(String fileId) {
			return String.format("'%s' in parents and trashed = false", driveSanitize(fileId));
		}

		private String driveSanitize(String str) {
			return str.replaceAll("'", "\\\\'");
		}

		private void recursiveBuilder(DriveFile root) {
			DRIVE_SERVICE_LOGGER.info("Caching children of " + root.getName());
			for (com.google.api.services.drive.model.File file : listDriveChildren(root.getProviderFileId())) {
				// file.setParents(Collections.singletonList(root.getProviderFileId()));
				DriveFile driveFile = new DriveFile(driveProvider, file, false, false, false);
				driveFile.updatePersistence();
				if (driveFile.getUuid() == null) {
					driveFile.setUuid(UUID.randomUUID());
					driveFile.persist();
				}
				ProviderFileRelationship relationship = new ProviderFileRelationship(driveFile.getUuid(), root
						.getUuid(), true, false);
				relationship.persist();
				if (file.getMimeType().equals(Drive.FOLDER_MIME_TYPE)) recursiveBuilder(driveFile);
			}
		}

		private List<com.google.api.services.drive.model.File> listDriveChildren(String parentId) {
			List<com.google.api.services.drive.model.File> files = new ArrayList<>();
			if (parentId == null) return files;

			com.google.api.services.drive.Drive.Files.List request = null;
			try {
				request = driveService.files().list().setIncludeTeamDriveItems(true).setSupportsTeamDrives(true)
						.setFields(LIST_PARTIAL_FIELDS).setPageSize(1000);
			} catch (IOException e) {
				DRIVE_SERVICE_LOGGER
						.warn("An error occurred while building the provider cache: " + driveProvider.getUuid()
								.toString(), e);
			}

			do {
				try {
					FileList fileList = DriveService.executeRequest(request.setQ(driveQ(parentId)));
					files.addAll(fileList.getFiles());
					request.setPageToken(fileList.getNextPageToken());
				} catch (IOException e) {
					request.setPageToken(null);
				}
			} while (request.getPageToken() != null && request.getPageToken().length() > 0);
			return files;
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
						DRIVE_SERVICE_LOGGER.trace("Verifying file: " + file.getId());
						Record1<String> stringRecord1 = DSL.using(AtherConfiguration.getJooqConfiguration(conn))
								.select(PROVIDERFILES.UUID).from(PROVIDERFILES)
								.where(PROVIDERFILES.PROVIDERFILEID.eq(file.getId())).fetchAny();
						if (stringRecord1 == null || stringRecord1.get(PROVIDERFILES.UUID) == null) {
							DRIVE_SERVICE_LOGGER.info("File not in cache: " + file.getId() + ", adding...");
							new DriveFile(driveProvider, executeRequest(driveService.files().get(file.getId())
									                                            .setFields(PARTIAL_FIELDS)
									                                            .setSupportsTeamDrives(true)), false, true);
						} else DRIVE_SERVICE_LOGGER.trace("Verified file: " + file.getId());
						// TODO OnCacheFileProcessed event
					}

					if (request != null) request.setPageToken(resultFiles.getNextPageToken());
				} catch (IOException | SQLException e) {
					DRIVE_SERVICE_LOGGER
							.error("An error occurred while verifying the provider cache: " + driveProvider.getUuid()
									.toString(), e);
					if (request != null) request.setPageToken(null);
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
			if (updateRemote) {
				com.google.api.services.drive.Drive.Files.Update req = driveService.files()
						.update(updatedFile.getProviderFileId(), updatedFile.toDriveFile());
				if (doBatchRequests) executeRequestAsync(req);//queueBatch(req);
				else executeRequest(req);
			}
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
					driveFile.getParentFiles().add(new ProviderFileRelationship(driveFile.getUuid(), add, true, true));
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

					com.google.api.services.drive.Drive.Files.Update req = driveService.files()
							.update(driveFile.getProviderFileId(), null).setAddParents(additions)
							.setRemoveParents(removals);
					if (doBatchRequests) executeRequestAsync(req);//queueBatch(req);
					else executeRequest(req);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		//TODO improve on detection by using a tree setup.
		public DriveFile parsePath(DriveFile root, String path, boolean createNew) throws IOException {
			DriveFile tempFile = root;

			pathLoop:
			for (String section : path.split("/")) {
				List<UUID> children = ProviderCache.getChildUuids(tempFile.getUuid());
				if (children.isEmpty() && createNew) {
					tempFile = new DriveFile(driveProvider, executeRequest(driveService.files()
							                                                       .create(new com.google.api.services.drive.model.File()
									                                                               .setName(section)
									                                                               .setMimeType("application/vnd.google-apps.folder")
									                                                               .setParents(Collections
											                                                                           .singletonList(tempFile.getProviderFileId())))), false, true);
				} else {
					for (UUID fileUuid : children) {
						DriveFile cacheFile = ProviderCache.getProviderFile(fileUuid);
						if (cacheFile.getName().equals(section)) {
							tempFile = cacheFile;
							continue pathLoop;
						}
					}
				}
			}

			return tempFile;
		}

		public DriveFile parsePath(String path, boolean createNew) throws IOException {
			//TODO Fix root call
			return parsePath(files().get("root", false), path, createNew);
		}

		public DriveFile parsePath(String path) throws IOException {
			return parsePath(path, false);
		}

		//TODO
		public DriveFile getFile(String providerFileId) {
			try (Connection conn = DatabaseConnector.getConnection()) {
				Record1<String> stringRecord1 = DSL.using(AtherConfiguration.getJooqConfiguration(conn))
						.select(PROVIDERFILES.UUID).from(PROVIDERFILES)
						.where(PROVIDERFILES.PROVIDERUUID.eq(driveProvider.getUuid().toString())
								       .and(PROVIDERFILES.PROVIDERFILEID.eq(providerFileId))).fetchAny();
				if (stringRecord1 == null) return null;
				else return new DriveFile(UUID.fromString(stringRecord1.get(PROVIDERFILES.UUID)));
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
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
				ChangeList fileChanges = executeRequest(driveService.changes().list(pageToken)
						                                        .setFields(CHANGE_PARTIAL_FIELDS).setPageSize(1000)
						                                        .setSupportsTeamDrives(true)
						                                        .setIncludeTeamDriveItems(true));
				for (Change change : fileChanges.getChanges()) {
					//TODO
					if (change.getRemoved() || (change.getFile() != null && (change.getFile()
							.getExplicitlyTrashed() || change.getFile().getTrashed()))) {
						DriveFile tempFile = cache().getFile(change.getFileId());
						if (tempFile == null) continue;
						ProviderCache.getFileRelationships(tempFile.getUuid())
								.forEach(ProviderFileRelationship::delete);
						tempFile.delete();
					} else {
						DriveFile tempFile = cache().getFile(change.getFile().getId());
						if (tempFile == null) {
							new DriveFile(driveProvider, change.getFile(), false, true, false);
							continue;
						}

						DriveFile updatedFile = new DriveFile(driveProvider, change.getFile(), false, false, false);
						updatedFile.setUuid(tempFile.getUuid());
						List<ProviderFileRelationship> relationships = ProviderCache
								.getFileRelationships(tempFile.getUuid());
						List<DriveFile> parents = ProviderCache.getFileParents(tempFile.getUuid());
						for (int i = 0; i < relationships.size(); i++) {
							if (!change.getFile().getParents().contains(parents.get(i).getProviderFileId())) ;
							relationships.get(i).delete();
						}

						//TODO Don't assume parent exists
						List<String> parentIds = new ArrayList<>(parents.size());
						for (DriveFile parent : parents)
							parentIds.add(parent.getProviderFileId());
						for (String parent : change.getFile().getParents()) {
							if (!parentIds.contains(parent)) {
								DriveFile parentMatch = null;
								for (DriveFile parentFile : parents) {
									if (parentFile.getProviderFileId().equals(parent)) {
										parentMatch = parentFile;
										break;
									}
								}
								if (parentMatch == null)
									parentMatch = new DriveFile(driveProvider, executeRequest(driveService.files()
											                                                          .get(parent)
											                                                          .setSupportsTeamDrives(true)
											                                                          .setFields(PARTIAL_FIELDS)), false, true, false);
								new ProviderFileRelationship(tempFile.getUuid(), parentMatch.getUuid(), true, true);
							}
						}
					}
				}
				if (fileChanges.getNewStartPageToken() != null)
					driveProvider.persistChangeToken(fileChanges.getNewStartPageToken());
				pageToken = fileChanges.getNextPageToken();
			}
		}
	}

	public class Files extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.Files<DriveFile> {
		@Override
		public DriveFile get(String providerFileId, boolean attemptLoad, boolean doPersist) throws IOException {
			DRIVE_SERVICE_LOGGER.trace("Getting ProviderFile from drive service: " + providerFileId);
			DriveFile file = new DriveFile(driveProvider, executeRequest(driveService.files().get(providerFileId)
					                                                             .setSupportsTeamDrives(true)
					                                                             .setFields(PARTIAL_FIELDS)), attemptLoad, doPersist);
			if (!doPersist || file.getUuid() != null) return file;
			else return null;
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
					                                          .build()).getEntity().getContent());
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
									                      .getProviderFileId(), driveProvider.getCredential()
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

	public class Permissions extends
			ProviderService<DriveAuthorization, DriveConfiguration, DriveCredential, DriveFile, Drive, DriveService>.Permissions {

	}
}
