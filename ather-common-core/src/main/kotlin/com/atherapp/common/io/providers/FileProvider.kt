package com.atherapp.common.io.providers

import com.atherapp.common.api.modules.io.providers.ProviderRegistrationInfo
import java.io.IOException
import java.time.LocalTime

typealias EntryType = Int

object FileProviders {
    internal var registry: MutableList<ProviderRegistrationInfo> = mutableListOf()

    /**
     * Used to match a provider URL
     */
    internal val matcher = Regex.fromLiteral("^([\\w_ -]+):(.*)$")

    /**
     * Register a Provider using its [ProviderRegistrationInfo]
     */
    fun register(info: ProviderRegistrationInfo) = registry.add(info)

    /**
     * Looks for a [ProviderRegistrationInfo] object for the name passed in
     * Services are looked for inside the registry
     */
    fun find(name: String) = registry.firstOrNull { it.name == name } ?: throw FileSystemExceptions.FileSystemNotFoundException()

    /**
     * Deconstructs a path into its config name, provider path,
     * looking up the provider name in the backing config location
     *
     * @throws FileSystemExceptions.FileSystemNotFoundException if the provider is not found in the config
     *
     * TODO TODO TODO
     */
    fun parseRemote(path: String) : Triple<ProviderRegistrationInfo, String, String> {
        val parts = matcher.findAll(path).toList()

        var providerName: String = "local"
        var configName: String = "local"
        var providerPath: String = path

        if (!DriveLetter.isDriveLetter(parts[1].value)) {
            configName = parts[1].value
            providerPath = parts[2].value
            //providerName = TODO Config implementation
        }
    }
}

object FileSystemExceptions {
    class FileSystemNotFoundException(cause: Throwable? = null) : IOException("File system not found in database", cause)
    class CannotPurgeException(cause: Throwable? = null) : IOException("Can't purge directory", cause)
    class CannotCopyException(cause: Throwable? = null) : IOException("Can't copy object, incompatible remotes", cause)
    class CannotMoveException(cause: Throwable? = null) : IOException("Can't move object, incompatible remotes", cause)
    class CannotMoveDirectoryException(cause: Throwable? = null) : IOException("Can't move directory - incompatible remotes", cause)
    class DirectoryAlreadyExistsException(cause: Throwable? = null) : IOException("Can't copy directory - destination already exists", cause)
    class CannotSetModificationTimeException(cause: Throwable? = null) : IOException("Can't set modified time", cause)
    class CannotSetModificationTimeWithoutDelete(cause: Throwable? = null) : IOException("Can't set modified time without deleting existing object", cause)
    class DirectoryNotFoundException(cause: Throwable? = null) : IOException("Directory not found", cause)
    class ObjectNotFoundException(cause: Throwable? = null) : IOException("Object not found", cause)
    class LevelNotSupportedException(cause: Throwable? = null) : IOException("Level value not supported", cause)
    class ListAbortedException(cause: Throwable? = null) : IOException("List aborted", cause)
    class BucketNameRequiredException(cause: Throwable? = null) : IOException("Bucket or container name is needed in remote", cause)
    class IsFileException(cause: Throwable? = null) : IOException("Is a file, not a directory", cause)
    class NotAFileException(cause: Throwable? = null) : IOException("Is not a regular file", cause)
    class NotDeletingException(cause: Throwable? = null) : IOException("Not deleting files, IO errors occurred", cause)
    class NotDeletingDirectoriesException(cause: Throwable? = null) : IOException("Not deleting directories, IO errors occurred", cause)
    class OverlappingRemotesException(cause: Throwable? = null) : IOException("Cannot move files on overlapping remotes", cause)
    class DirectoryNotEmptyException(cause: Throwable? = null) : IOException("Directory not empty", cause)
    class ImmutableFileException(cause: Throwable? = null) : IOException("Immutable file modified", cause)
    class PermissionDeniedException(cause: Throwable? = null) : IOException("Permission denied", cause)
}

/**
 * Constants
 */
object FileSystemConstants {
    /**
     * A very large precision value to show mod time isn't supported on a given providers
     */
    val MOD_TIME_NOT_SUPPORTED = 100 * 365 * 24 * LocalTime.now().hour

    /**
     * Represent maximum depth
     */
    val MAX_LEVEL = Int.MAX_VALUE

    /**
     * Used to classify remote paths in directories
     */
    val ENTRY_DIRECTORY: EntryType = 0

    /**
     * Classify remote paths in objects
     */
    val EntryObject = 1
}