package com.atherapp.common.api.modules.io.providers

import java.time.Instant

/**
 * Describes an unspecialized directory for directory/container/bucket lists
 */
open class BasicDirectory(
        /**
         * Name of the directory
         */
        var remote: String,
        modTime: Instant = Instant.EPOCH,
        /**
         * Size of the directory and contents, -1 if unknown
         */
        var size: Long = -1,
        /**
         * Number of objects or -1 if unknown
         */
        var items: Long = -1,
        /**
         * Optional ID
         */
        var id: String? = null
) {
    /**
     * Modification or creation time, [Instant.EPOCH] if unknown
     */
    val modTime = modTime
        get() = if (field == Instant.EPOCH) Instant.now() else field

    override fun toString() = remote

    companion object {
        /**
         * @return an unspecialized copy of the [Directory] passed
         */
        fun fromDirectory(d: Directory): BasicDirectory {
            return BasicDirectory(
                    remote = d.remote,
                    modTime = d.modTime,
                    size = d.size,
                    items = d.items
            )
        }
    }
}