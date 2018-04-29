package com.atherapp.common.api.modules.io.providers

import com.atherapp.common.io.providers.Hash
import com.atherapp.common.io.providers.OpenOption
import kotlinx.coroutines.experimental.channels.Channel
import java.io.InputStream
import java.time.Instant

/**
 * Provides read-only information about the common subset of a [Directory] or [ProviderObject].
 * Returned from directory listings. They must be cast into the correct items.
 */
interface DirectoryEntry {
    /**
     * String description of the object
     */
    val description: String

    /**
     * Remote path
     */
    val remote: String

    /**
     * Modification date of a file.
     * Return the best guess if none is explicitly available
     */
    val modTime: Instant

    /**
     * The size of the file
     */
    val size: Long
}

/**
 * Provides read-only information about an object
 */
interface ProviderObjectInfo : DirectoryEntry {
    /**
     * Read only access to the provider that this object is a part of
     */
    val provider: ProviderInfo

    /**
     * Whether the object can be stored
     */
    val storable: Boolean

    /**
     * @returns the selected checksum of the file,
     * "" if no checksum is available
     */
    fun hash(type: Hash): String
}

interface ProviderObject : ProviderObjectInfo {
    /**
     * Updates the modtime on the metadata object
     */
    fun setModTime(time: Instant)

    /**
     * Opens the file for reading, call [InputStream.close] on the returned [InputStream]
     */
    fun open(vararg options: OpenOption): InputStream

    /**
     * Update into this object with the modTime given of the given size
     */
    fun update(input: InputStream, src: ProviderObjectInfo, vararg options: OpenOption)

    /**
     * Remove this object
     */
    fun remove()
}

/**
 * A provider-like directory provided by a provider
 */
interface Directory : DirectoryEntry {
    /**
     * The count of items in the directory or
     * the directory and subdirectories if known,
     * -1 for unknown
     */
    val items: Long

    /**
     * The internal ID of this directory if known,
     * "" otherwise
     */
    val id: String
}

/**
 * Optional interface for a [ProviderObject]
 */
interface MimeTyper {
    /**
     * The content type of the [ProviderObject] if known,
     * "" otherwise
     */
    val mimeType: String
}

/**
 * Option interface for a [ProviderObject]
 */
interface FileObjectUnwrapper {
    /**
     * Returns the [ProviderObject] that the current [ProviderObject]
     * is wrapping, or `null` if it isn't wrapping anything
     */
    fun unwrap(): ProviderObject?
}

/**
 * A channel of objects
 */
typealias ProviderObjectsChannel = Channel<ProviderObject>

/**
 * A slice of Object(s)
 */
typealias ProviderObjects = Array<ProviderObject>

/**
 * Pair of objects used to describe a potential copy operation
 */
typealias ProviderObjectPair = Pair<ProviderObject, ProviderObject>

/**
 * A channel of [ProviderObjectPair]
 */
typealias ProviderObjectPairChannel = Channel<ProviderObjectPair>