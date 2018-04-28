package com.atherapp.common.io.providers

import com.atherapp.common.api.modules.io.providers.ProviderRegistrationInfo
import java.io.IOException
import java.time.LocalTime

typealias EntryType = Int

object FileProviders {
    internal var registry: MutableList<ProviderRegistrationInfo> = mutableListOf()

    fun register(info: ProviderRegistrationInfo) = registry.add(info)
}

object FileSystemExceptions {
    class FileSystemNotFoundException(cause: Throwable) : IOException("File system not found in database", cause)
    class CannotPurgeException(cause: Throwable) : IOException("Can't purge directory", cause)
    class CannotCopyException(cause: Throwable) : IOException("Can't copy object, incompatible remotes", cause)
    class CannotMoveException(cause: Throwable) : IOException("Can't move object, incompatible remotes", cause)
    class CannotMoveDirectoryException(cause: Throwable) : IOException("Can't move directory - incompatible remotes", cause)
    class DirectoryAlreadyExistsException(cause: Throwable) : IOException("Can't copy directory - destination already exists", cause)
    class CannotSetModificationTimeException(cause: Throwable) : IOException("Can't set modified time", cause)
    class CannotSetModificationTimeWithoutDelete(cause: Throwable) : IOException("Can't set modified time without deleting existing object", cause)
    class DirectoryNotFoundException(cause: Throwable) : IOException("Directory not found", cause)
    class ObjectNotFoundException(cause: Throwable) : IOException("Object not found", cause)
    class LevelNotSupportedException(cause: Throwable) : IOException("Level value not supported", cause)
    class ListAbortedException(cause: Throwable) : IOException("List aborted", cause)
    class BucketNameRequiredException(cause: Throwable) : IOException("Bucket or container name is needed in remote", cause)
    class IsFileException(cause: Throwable) : IOException("Is a file, not a directory", cause)
    class NotAFileException(cause: Throwable) : IOException("Is not a regular file", cause)
    class NotDeletingException(cause: Throwable) : IOException("Not deleting files, IO errors occurred", cause)
    class NotDeletingDirectoriesException(cause: Throwable) : IOException("Not deleting directories, IO errors occurred", cause)
    class OverlappingRemotesException(cause: Throwable) : IOException("Cannot move files on overlapping remotes", cause)
    class DirectoryNotEmptyException(cause: Throwable) : IOException("Directory not empty", cause)
    class ImmutableFileException(cause: Throwable) : IOException("Immutable file modified", cause)
    class PermissionDeniedException(cause: Throwable) : IOException("Permission denied", cause)
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