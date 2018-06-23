package com.atherappx.common.io.drive

import com.atherapp.common.api.modules.io.providers.DefaultProviderOption
import com.atherapp.common.api.modules.io.providers.DefaultProviderRegistrationInfo
import com.atherapp.common.api.modules.io.providers.Provider
import com.atherapp.common.config.ConfigurationKey
import com.atherapp.common.io.providers.FileProviders
import com.google.common.base.CaseFormat
import java.time.format.DateTimeFormatter

enum class DriveConfigurationKeys(name: String = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.toString())) : ConfigurationKey {
    // Basic Drive Options
    /**
     * Drive API Client ID
     */
    CLIENT_ID,
    /**
     * Drive API Client Secret
     */
    CLIENT_SECRET,
    /**
     * Drive API Scope.
     *
     * @see DefaultDriveProviderScope]
     */
    CLIENT_SCOPE(DefaultDriveProviderScope.DRIVE.name),

    /**
     * Cutoff for switching to chunked upload
     */
    UPLOAD_CUTOFF,
    /**
     * Upload chunk size in Kilobytes. Must be a power of 2 >= 256k
     */
    CHUNK_SIZE,

    /**
     * Only consider files owned by the authenticated user
     */
    AUTH_OWNER_ONLY,
    /**
     * Send files to the trash instead of deleting permanently.
     */
    USE_TRASH,
    /**
     * Skip all Google Doc files
     */
    SKIP_GDOCS,
    /**
     * Only show files that are shared with me
     */
    SHARED_WITH_ME_ONLY,
    /**
     * Only show files that are in the trash
     */
    TRASHED_ONLY,
    /**
     * Comma separated list of preferred formats for downloading Google docs.
     */
    FORMATS,
    /**
     * Use created date instead of modified date.
     */
    USE_CREATED_DATE,
    /**
     * Size of listing chunk 1Z00-1000. 0 to disable.
     */
    LIST_CHUNK,
    /**
     * Impersonate this user when using a service account.
     */
    IMPERSONATE,
    /**
     * Use alternate export URLs for google documents export.
     */
    ALTERNATE_EXPORT
}

interface DriveProviderScope {
    val name: String
}

/**
 * Default Google Drive Access scopes
 */
enum class DefaultDriveProviderScope(name: String = this.toString().toLowerCase().replace("_", ".")) : DriveProviderScope {
    /**
     * Full, permission scope to access a user's files, excluding the Application Data Folder (which must be explicitly requested)
     *
     * @see <a href="https://developers.google.com/drive/api/v3/appdata">Application Data folder info</a>
     */
    DRIVE,
    /**
     * Allows read-only access to file metadata and file content
     */
    DRIVE_READONLY,
    /**
     * Allows access to the Application Data Folder
     *
     * @see <a href="https://developers.google.com/drive/api/v3/appdata">Application Data folder info</a>
     */
    DRIVE_APPFOLDER,
    /**
     * Per-file access to files created or opened by the app. File authorization is granted on a per-user basis and is revoked when the user deauthorizes the app.
     */
    DRIVE_FILE,
    /**
     * Special scope used to let users approve installation of an app.
     */
    DRIVE_INSTALL,
    /**
     * Allows read-write access to file metadata (excluding downloadUrl and contentHints.thumbnail), but does not allow any access to read, download, write or upload file content.
     * Does not support file creation, trashing or deletion. Also does not allow changing folders or sharing in order to prevent access escalation.
     */
    DRIVE_METADATA,
    /**
     * Allows read-only access to file metadata (excluding downloadUrl and contentHints.thumbnail), but does not allow any access to read or download file content
     */
    DRIVE_METADATA_READONLY,
    /**
     * Allows access to the Apps Script files
     *
     * @see <a href="https://developers.google.com/apps-script/">Apps Script file info</a>
     */
    DRIVE_SCRIPTS,
    /**
     * Allows read-only access to installed apps.
     */
    DRIVE_APPS_READONLY;
}

class DriveProvider {
    init {
        if (!isRegistered) {
            FileProviders.register(object : DefaultProviderRegistrationInfo(
                    name = "drive",
                    description = "Google Drive",
                    options = arrayOf(
                            DefaultProviderOption(
                                    name = "clientId",
                                    help = "Google Application Client Id - leave blank normally"
                            ),
                            DefaultProviderOption(
                                    name = "clientSecret",
                                    help = "Google Application Client Secret - leave blank normally"
                            ),
                            DefaultProviderOption(
                                    name = "scope",
                                    help = "Scope that the provider system should use when requesting access from drive."
                            ),
                            DefaultProviderOption(
                                    name = "serviceAccount",
                                    help = "Service Account JSON contents. Used if you want a service account instead of regular login."
                            )
                    )
            ) {
                override fun newFileSystem(name: String, root: String): Provider {
                    TODO("not implemented")
                }

                override fun config(str: String) {
                    TODO("not implemented")
                }

            }
            )
            isRegistered = true
        }
    }

    companion object {
        private var isRegistered = false

        const val defaultClientId = ""
        const val defaultEncryptedClientSecret = ""
        const val driveFolderType = "application/vnd.google-apps.folder"
        val dateTimeFormatter = DateTimeFormatter.ISO_INSTANT!!
    }
}