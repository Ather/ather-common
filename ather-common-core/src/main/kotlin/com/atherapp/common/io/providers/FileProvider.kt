package com.atherapp.common.io.providers

import com.atherapp.common.api.modules.io.providers.Provider
import com.atherapp.common.api.modules.io.providers.ProviderRegistrationInfo
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.time.LocalTime

typealias EntryType = Int

/**
 * Stores the Provider registration info, the config name of the provider, and the path on that provider
 */
typealias ProviderTriple = Triple<ProviderRegistrationInfo, String, String>

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
    fun find(name: String) = registry.firstOrNull { it.name == name }
            ?: throw FileSystemExceptions.FileSystemNotFoundException()

    /**
     * Deconstructs a path into its config name, provider path,
     * looking up the provider name in the backing config location
     *
     * @throws FileSystemExceptions.FileSystemNotFoundException if the provider is not found in the config
     *
     * TODO TODO TODO
     */
    fun parseRemote(path: String): ProviderTriple {
        val parts = matcher.findAll(path).toList()

        var providerName: String = "local"
        var configName: String = "local"
        var providerPath: String = path

        if (!DriveLetter.isDriveLetter(parts[1].value)) {
            configName = parts[1].value
            providerPath = parts[2].value
            providerName = "" //TODO create config system, this call is `ConfigFileGet(configName, "type")` in Go
            if (providerName == "")
                throw FileSystemExceptions.FileSystemNotFoundException()
        }

        providerPath = providerPath.replace(File.separatorChar, '/')
        return Triple(find(providerName), configName, providerPath)
    }

    /**
     * Makes a new [Provider] object from the path.
     *
     * The path is of the form remote:path
     *
     * @throws FileSystemExceptions.FileSystemNotFoundException if the provider can't be found in the config
     *
     * On windows, avoid single character remote names as they can be mixed with drive letters
     */
    fun newProvider(path: String): Provider {
        val rem = parseRemote(path)
        return rem.first.newFileSystem(rem.second, rem.third)
    }

    /**
     * Creates a local Provider in the OS's temporary directory.
     *
     * No cleanup is performed, the caller must call purge on the Provider themselves
     */
    fun temporaryLocalProvider(): Provider {
        val path = Files.createTempDirectory("ather-provider-spool")
        return newProvider(path.toAbsolutePath().toString())
    }

    /**
     * @return Whether a file remote exists,
     * false if the remote is a directory
     *
     * @throws IOException if the object retrieval fails, and the reason isn't [FileSystemExceptions.ObjectNotFoundException], [FileSystemExceptions.NotAFileException], or [FileSystemExceptions.PermissionDeniedException]
     */
    fun fileExists(provider: Provider, remote: String): Boolean {
        try {
            provider.newObject(remote)
        } catch (e: IOException) {
            if (e is FileSystemExceptions.ObjectNotFoundException || e is FileSystemExceptions.NotAFileException || e is FileSystemExceptions.PermissionDeniedException)
                return false
            throw e
        }

        return true
    }

    /**
     * Works out the modify window for providers passed int, and sets
     * the config's modifyWindow.
     *
     * This is the largest modify window of all the providers in use, adn the
     * user configured value.
     *
     * TODO This isn't used in the original application, and requires calls to the config, which
     * doesn't exist as of this date 04/29/18
     */
    fun calculateModifyWindows(vararg providers: Provider) {
        for (provider in providers) {

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