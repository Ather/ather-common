package com.atherapp.common.api.modules.nio.file

/**
 * Store basic information about a given [AtherFileStore]
 */
abstract class AtherFileStoreAbout {
    /**
     * Total bytes available in the [AtherFileStore]
     */
    abstract val total: Long

    /**
     * Bytes used in the [AtherFileStore]
     */
    abstract val used: Long

    /**
     * Bytes in the trash of the [AtherFileStore]
     */
    abstract val trashed: Long

    /**
     * Bytes used elsewhere in the [AtherFileStore].
     * An example is GMail usage for Google Drive
     */
    abstract val other: Long

    /**
     * Bytes free to use in the [AtherFileStore]
     */
    abstract val free: Long

    /**
     * Number of objects in the [AtherFileStore]
     */
    abstract val objects: Long
}