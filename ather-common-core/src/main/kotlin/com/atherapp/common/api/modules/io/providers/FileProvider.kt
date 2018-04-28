package com.atherapp.common.api.modules.io.providers

import java.io.IOException
import java.io.InputStream
import java.time.Duration

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
    val hashes: hashset //TODO

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
    fun newObject(remote: String): FileObject

    /**
     * Put into the remote path with the modTime given of the given size
     *
     * May create the object even if it returns an error - if so, it will return
     * the object, and the error.
     *
     * @throws java.io.IOException if an error occurs when creating the file
     */
    fun put(input: InputStream, src: FileObjectInfo, vararg options: OpenOption): Pair<FileObject, IOException?>

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