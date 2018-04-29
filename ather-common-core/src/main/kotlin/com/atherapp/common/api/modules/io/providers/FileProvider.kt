package com.atherapp.common.api.modules.io.providers

import com.atherapp.common.io.providers.EntryType
import com.atherapp.common.io.providers.FileSystemExceptions
import com.atherapp.common.io.providers.Hash
import com.atherapp.common.io.providers.OpenOption
import kotlinx.coroutines.experimental.channels.Channel
import java.io.IOException
import java.io.InputStream
import java.time.Duration
import javax.tools.FileObject

/**
 * Provides information about a providers
 */
interface ProviderRegistrationInfo {
    /**
     * Name of the provider
     */
    var name: String

    /**
     * Description of this provider
     * Defaults to name
     */
    var description: String

    /**
     * Provider configuration options
     */
    var options: Array<ProviderOption>

    /**
     * Create a new file system. If the root refers to an existing object, it
     * should return a provider which points to the parent of that object, and ErrorIsFile
     *
     * @throws java.io.IOException if any issue is encountered. This can include more specific errors,
     * depending on the provider implementation
     */
    fun newFileSystem(name: String, root: String): Provider

    /**
     * Config helper function
     */
    fun config(str: String)
}

/**
 * An option describes an option for the provider config
 */
interface ProviderOption {
    var name: String

    var help: String

    var provider: String

    var optional: Boolean

    var isPassword: Boolean
}

/**
 * Provides a read-only interface for information about a [Provider]
 */
interface ProviderInfo {
    /**
     * Name of the remote (as passed to [ProviderRegistrationInfo.newFileSystem])
     */
    val name: String

    /**
     * Root of the remote (as passed to [ProviderRegistrationInfo.newFileSystem]]
     */
    val root: String

    /**
     * Description of the provider
     */
    val description: String

    /**
     * Precision of the provider mod times
     */
    val precision: Duration

    /**
     * Returns the supported hash types of the provider
     */
    val hashes: Hash

    /**
     * Returns the optional features of the provider
     */
    val features: ProviderFeatures?
}

/**
 * The interface a cloud storage system must provide
 */
interface Provider : ProviderInfo {
    /**
     * List the objects and directories in [dir] into entries.
     * The entries can be returned in any order but should be for a complete directory.
     *
     * [dir] should be "" to list the root, and should not have trailing slashes.
     *
     * @throws com.atherapp.common.io.providers.FileSystemExceptions.DirectoryNotFoundException if the directory isn't found
     */
    fun list(dir: String): DirectoryEntries

    /**
     * Find the object on the remote.
     *
     * @throws com.atherapp.common.io.providers.FileSystemExceptions.ObjectNotFoundException if the file can't be found
     */
    fun newObject(remote: String): ProviderObject

    /**
     * Put into the remote path with the modTime given of the given size
     *
     * May create the object even if it returns an error - if so, it will return
     * the object, and the error.
     *
     * @throws java.io.IOException if an error occurs when creating the file
     */
    fun put(input: InputStream, src: ProviderObjectInfo, vararg options: OpenOption): Pair<ProviderObject, IOException?>

    /**
     * Makes the directory/container/bucket
     *
     * Should not throw an exception if the directory already exists.
     */
    fun mkdir(dir: String)

    /**
     * Removes the directory/container/bucket
     *
     * @throws java.io.IOException if it doesn't exists or isn't empty
     */
    fun rmdir(dir: String)
}

typealias RecursiveListCallback = (DirectoryEntries) -> Unit

/**
 * Returned by the about call
 * If a value is `null`, then it isn't supported by that [Provider]
 */
data class ProviderUsage(
        /**
         * Quota of bytes that can be used
         */
        var total: Long?,
        /**
         * Bytes in use
         */
        var used: Long?,
        /**
         * Bytes in trash
         */
        var trashed: Long?,
        /**
         * Other usage, e.g. gmail in drive
         */
        var other: Long?,
        /**
         * Bytes which can be uploaded before reaching the quota
         */
        var free: Long?,
        /**
         * Objects in the storage system
         */
        var objects: Long?
)

/**
 * Describe the optional features of the [Provider]
 */
open class ProviderFeatures(
        /**
         * Has case insensitive files
         */
        var caseInsensitive: Boolean,
        /**
         * Allows duplicate files
         */
        var duplicateFiles: Boolean,
        /**
         * Can read the mime type of [ProviderObject]s
         */
        var readMimeType: Boolean,
        /**
         * Can set the mime type of [ProviderObject]s
         */
        var writeMimeType: Boolean,
        /**
         * Can have empty directories
         */
        var canHaveEmptyDirectories: Boolean,
        /**
         * Is bucket based (S3, Swift, etc.)
         */
        var bucketBased: Boolean
)

/**
 * An optional interface for [Provider]
 */
interface Purger {
    /**
     * Purge all files in the root and the root directory
     *
     * Implement this if you have a way of deleting all the files
     * quicker than just running [ProviderObject.remove] on the result of [Provider.list]
     *
     * @throws IOException if the root doesn't exist
     * @throws UnsupportedOperationException if the provider doesn't support the operation
     */
    fun purge(): Nothing = throw UnsupportedOperationException()
}

/**
 * An optional interface for [Provider]
 */
interface Copier {
    /**
     * Copy [src] to this [remote] using server side copy operations.
     *
     * This is stored with the remote path given
     *
     * @return The destination [FileObject]
     * @throws [FileSystemExceptions.CannotCopyException] if the operation is unsupported
     * @throws [UnsupportedOperationException] if the provider doesn't support the operation
     */
    fun copy(src: FileObject, remote: String): FileObject = throw FileSystemExceptions.CannotCopyException()
}

/**
 * An optional interface for [Provider]
 */
interface Mover {
    /**
     * Move [src] to this [remote] using server side move operations.
     *
     * This is stored with the remote path given
     *
     * @return The destination [FileObject]
     * @throws [FileSystemExceptions.CannotMoveException] if the operation is unsupported
     * @throws [UnsupportedOperationException] if the provider doesn't support the operation
     */
    fun move(src: FileObject, remote: String): FileObject = throw FileSystemExceptions.CannotMoveException()
}

/**
 * An optional interface for [Provider]
 */
interface DirectoryMovier {
    /**
     * Move [srcRemote] to this remote at [dstRemote] using server side move operations.
     *
     * @return The destination [FileObject]
     * @throws [FileSystemExceptions.CannotMoveException] if the operation is unsupported
     * @throws [UnsupportedOperationException] if the provider doesn't support the operation
     */
    fun moveDirectory(src: Provider, srcRemote: String, dstRemote: String): Nothing = throw FileSystemExceptions.CannotMoveDirectoryException()
}

typealias ChangeNotifyFunction = () -> Pair<String, EntryType>

/**
 * An optional interface for [Provider]
 */
interface ChangeNotifier {
    /**
     * Calls the passed function with a path that has had changes.
     * If the implementation uses polling, it should adhere to the given
     * interval
     */
    fun changeNotify(notifyFunction: ChangeNotifyFunction, interval: Duration): Channel<Boolean>
}

/**
 * An optional interface for [Provider]
 */
interface Unwrapper {
    /**
     * @return The [Provider] that this [Provider] is wrapping
     */
    fun unwrap(): Provider
}

/**
 * An optional interface for [Provider]
 */
interface Wrapper {
    /**
     * @return The [Provider] that is wrapping this [Provider]
     */
    fun wrap(): Provider

    /**
     * Sets the [Provider] that is wrapping this [Provider]
     */
    fun setWrapper(provider: Provider)
}

/**
 * An optional interface for [Provider]
 */
interface DirectoryCacheFlusher {
    /**
     * Resets the directory cache
     * Used only in testing
     */
    fun flushDirectoryCache()
}

/**
 * An optional interface for [Provider]
 */
interface putUncheckedException {
    /**
     * Put into the remote with the modTime given of the given size
     *
     * May create the object even if it throws an exception
     *
     * May create duplicates or throw exceptions if the [src] already exists
     *
     * @return The object and the error if the file is created and there were errors,
     * otherwise it throw the exception
     * @throws Exception if any error occurs during put, only if the file isn't created. In that case, it is returned in the pair
     */
    fun putUnchecked(input: InputStream, src: ProviderObjectInfo, vararg options: OpenOption): Pair<ProviderObject?, Exception?>
}

/**
 * An optional interface for [Provider]
 */
interface PutStreamer {
    /**
     * Uploads to the remote path with the modTime given of indeterminate size
     *
     * May create the object even if it throws an exception
     *
     * @return The object and the error if the file is created and there were errors,
     * otherwise it throw the exception
     * @throws Exception if any error occurs during put, only if the file isn't created. In that case, it is returned in the pair
     */
    fun putStream(input: InputStream, src: ProviderObjectInfo, vararg options: OpenOption): Pair<ProviderObject?, Exception?>
}

/**
 * An optional interface for [Provider]
 */
interface PublicLinker {
    /**
     * Generates a public link to the remote path (usually readable by anyone)
     */
    fun publicLink(remote: String): String
}

/**
 * An optional interface for [Provider]
 */
interface DirectoryMerger {
    /**
     * Merges the contents of all the [directories] passed in
     * into the first one and rmdirs the other directories
     */
    fun mergeDirectories(directories: Array<Directory>)
}

/**
 * An optional interface for [Provider]
 */
interface CleanUpper {
    /**
     * Cleans up the trash in the [Provider]
     *
     * Implement this if you have a way of emptying the trash or
     * otherwise cleaning up old versions of files
     */
    fun cleanup()
}

/**
 * An optional interface for [Provider]
 */
interface RecursiveLister {
    /**
     * Lists the objects and directories of the [Provider] starting from dir recursively into out.
     *
     * [dir] should be "" to start from the root, and should not have trailing slashes.
     *
     * @throws [FileSystemExceptions.DirectoryNotFoundException] if the directory isn't found
     *
     * It should call [callback] for each tranche of entries read.
     * These need not be returned in any particular order.
     * If the callback throws an error, then the listing will stop immediately.
     *
     * Don't implement this unless you have a more efficient way of listing recursively than doing a directory traversal.
     */
    fun listRecursive(dir: String, callback: RecursiveListCallback)
}

/**
 * Interface that wraps the RangeSeek method
 *
 * Some of the returns from [ProviderObject.open] may optionally
 * implement this method for efficiency purpose.
 *
 * TODO Confirm the viability of this function
 */
interface RangeSeeker {
    /**
     * Behaves like a call to
     */
    fun rangeSeek(offset: Long, whence: Int, length: Long): Long
}

/**
 * An optional interface for [Provider]
 */
interface Abouter {
    /**
     * Gets quota information from the [Provider]
     */
    fun about(): ProviderUsage
}