package com.atherapp.common.api.modules.io.providers

import com.atherapp.common.util.DataUnit
import java.time.Duration

/**
 * Pair of the provider name/id and the config entry
 */
typealias ProviderConfig = Pair<String, ProviderConfigEntry>

/**
 * Store basic information about a provider in the config
 */
abstract class ProviderConfigEntry {
    /**
     * The type of provider (drive, ftp, etc.)
     */
    abstract var type: String
}

/**
 * Specifies the default settings for a job.
 *
 * This should be stored in the database to share between servers,
 * but have a local version with only overrides
 */
open class JobConfig(
        /**
         * Max time difference to be considered the same
         */
        val modifyWindow: Duration = Duration.ofNanos(1),

        /**
         * Number of checkers to run on a job by default
         */
        val checkers: Int = 8,

        /**
         * Number of transfers to run in parallel on a job by default
         */
        val transfers: Int = 4,

        /**
         * Skip based on checksum and size, not mod-time and size
         */
        val checksum: Boolean = false,

        /**
         * Skip based on size only, not mod-time or checksum
         */
        val sizeOnly: Boolean = false,

        /**
         * Don't skip files that match size and time - transfer all files
         */
        val ignoreTimes: Boolean = false,

        /**
         * Skip all files that exist on destination
         */
        val ignoreExisting: Boolean = false,

        /**
         * Delete even if there are I/O errors
         */
        val ignoreErrors: Boolean = false,

        /**
         * Connection timeout
         */
        val connectionTimeout: Duration = Duration.ofSeconds(60),

        /**
         * IO idle timeout
         */
        val timeout: Duration = Duration.ofMinutes(5),

        /**
         * Do not verify the server SSL certificate.
         * This is insecure and not recommended.
         */
        val noCheckCertificates: Boolean = false,

        /**
         * Deletion policy while synchronizing
         */
        val deleteMode: DeleteMode = DeleteMode.After,

        /**
         * When synchronizing, limit the number of deletes
         */
        val maxDelete: Int = -1,

        /**
         * When synchronizing, track file renames and do a server-side move if possible
         */
        val trackRenames: Boolean = true,

        /**
         * Number of low-level-retries to do on a request
         */
        val lowLevelRetries: Int = 10,

        /**
         * Skip files that are newer on the destination
         */
        val skipNewer: Boolean = false,

        /**
         * Use server modified time instead of object metadata
         */
        val useServerModtime: Boolean = false,

        /**
         * Don't set Accept-Encoding: gzip
         */
        val noGzipEncoding: Boolean = false,

        /**
         * If set, it limits the recursion depth
         */
        val maxDepth: Int = -1,

        /**
         * Ignore size when skipping use mod-time or checksum
         */
        val ignoreSize: Boolean = false,

        /**
         * Skip post copy check of checksum
         */
        val ignoreChecksum: Boolean = false,

        /**
         * Don't update destination mod-time if files identical.
         */
        val noUpdateModtime: Boolean = false,

        /**
         * Make backups into hierarchy based in DIR.
         */
        val backupDir: String? = null,

        /**
         * Suffix to use with [backupDir].
         */
        val backupSuffix: String? = null,

        /**
         * Uses recursive list if available. This uses more memory, but fewer transactions.
         */
        val fastList: Boolean = false,

        /**
         * Limit HTTP transactions per second to this.
         */
        val tpsLimit: Double = 0.0,

        /**
         * Max burst of transactions for [tpsLimit].
         */
        val tpsLimitBurst: Int = 1,

        /**
         * Local address to bind to for outgoing connections,
         * IPv4, IPv6, or name.
         */
        val bindAddress: String = "",

        /**
         * Set the user-agent to a specified string. The default is athermediaserver-provider/version
         * TODO Add Git properties to Maven, and use the version from there
         */
        val userAgent: String = "athermediaserver-provider/0.0.1",

        /**
         * Do not modify files. Fail if existing files have been modified.
         */
        val immutable: Boolean = false,

        /**
         * If enabled, do not request user confirmation
         */
        val autoConfirm: Boolean = true,

        /**
         * Max file name length in stats. 0 for no limit
         */
        val statsFileNameLength: Int = 40,

        /**
         * Bandwidth limit in Kilobits/second
         */
        val bandwidthLimit: Long = 0,

        /**
         * Buffer size in Kilobytes
         */
        val bufferSize: Long = DataUnit.Megabyte.toKilobyte(16).toLong(),

        /**
         * Cutoff for switching to chunked upload if file size is unknown.
         * Upload starts after reaching cutoff or when file ends.
         * Represented in Kilobytes
         */
        val streamingUploadCutoff: Long = 100
)