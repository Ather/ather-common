package com.atherapp.common.modules.manifest

import com.atherapp.common.modules.manifest.permissions.ModulePermissions
import com.atherapp.common.modules.manifest.permissions.Permission
import com.google.gson.JsonElement

/**
 * Exception indicating that a module manifest is invalid
 */
class IllegalManifestException(
        /**
         * Detail message of what is wrong with the manifest
         */
        override val message: String,
        /**
         * The optional cause of the exception. This is normally used if the JSON is illegal.
         */
        override val cause: Throwable? = null
) : RuntimeException(message, cause)

/**
 * Manifest for a module (module.json), which stores information about the module,
 * including how to load it, dependencies, declared endpoints, permissions, and other
 * general information.
 */
interface ModuleManifest {
    /**
     * The release group of the module.
     */
    val group: String
    /**
     * The artifact name, which is the module name
     */
    val artifact: String
    /**
     * The version tag of the module. This isn't necessarily indicative of an upgrade.
     * Upgrades are managed by the repository, by specifying a "previousVersion"
     */
    val tag: String
    /**
     * A friendly alias to the artifact name, basically the title of the module.
     */
    val alias: String?
    /**
     * Short description of the functionality of the module
     */
    val description: String?
    /**
     * When to load the module
     */
    val loadStage: ModuleLoadStage
    /**
     * List of module authors. These should be the Group/User names on a repository,
     * but don't have to be.
     */
    val authors: List<String>
    /**
     * Website of the module. This can be a regular website, or just a link to the module on a repository.
     */
    val website: String?
    /**
     * Class name of the Module entry point.
     */
    val mainClass: String
    /**
     * Whether the module will be using databases.
     * This is used if you are using the internal database system.
     */
    val database: Boolean
    /**
     * List of Maven artifacts to be used by the module. Dependencies are resolved and managed
     * by the system automatically.
     */
    val jarDependencies: List<JarDependency>
    /**
     * List of any modules this module depends on.
     */
    val dependencies: List<ModuleDependency>
    /**
     * Module dependencies that would increase the functionality of this module.
     */
    val softDependencies: List<ModuleDependency>
    /**
     * Logging prefix to use. If not specified, a class logger is normally used
     * TODO Enable this with KLogging
     */
    val logPrefix: String?
    /**
     * Modules which have to be loaded after this module.
     */
    val loadBefore: List<Dependency>
    /**
     * Prefix to expand on any permission nodes starting with a "$"
     */
    val permissionPrefix: String?
    /**
     * Endpoint declarations for if the module is going to build directly onto the internal API.
     * This is designed as a security feature to tell users what their modules can/do change/access.
     */
    val endpoints: ModuleEndpoints
    /**
     * Permission declarations for if the module has any permissions for its functionality.
     * This is primarily used along-side endpoints.
     */
    val permissions: ModulePermissions
    /**
     * Any additional/custom specifications inside the module manifest.
     */
    val additionalProperties: Map<String, Any>
}

internal data class MutableModuleManifest(
        override var group: String = "",
        override var artifact: String = "",
        override var tag: String = "",
        override var alias: String? = null,
        override var description: String? = null,
        override var loadStage: ModuleLoadStage = ModuleLoadStage.POST_API,
        override var authors: MutableList<String> = mutableListOf(),
        override var website: String? = null,
        override var mainClass: String = "",
        override var database: Boolean = false,
        override var jarDependencies: MutableList<JarDependency> = mutableListOf(),
        //TODO var externalDependencies,
        override var dependencies: MutableList<ModuleDependency> = mutableListOf(),
        override var softDependencies: MutableList<ModuleDependency> = mutableListOf(),
        override var logPrefix: String? = null,
        override var loadBefore: MutableList<Dependency> = mutableListOf(),
        override var permissionPrefix: String? = "",
        override var endpoints: MutableMap<String, Endpoint> = mutableMapOf(),
        override var permissions: MutableMap<String, Permission> = mutableMapOf(),
        override var additionalProperties: MutableMap<String, JsonElement> = mutableMapOf()
) : ModuleManifest

data class DataModuleManifest(
        override val group: String,
        override val artifact: String,
        override val tag: String,
        override val alias: String? = null,
        override val description: String? = null,
        override val loadStage: ModuleLoadStage = ModuleLoadStage.POST_API,
        override val authors: List<String> = emptyList(),
        override val website: String? = null,
        override val mainClass: String,
        override val database: Boolean = false,
        override val jarDependencies: List<JarDependency> = emptyList(),
        //TODO val externalDependencies,
        override val dependencies: List<ModuleDependency> = emptyList(),
        override val softDependencies: List<ModuleDependency> = emptyList(),
        override val logPrefix: String? = null,
        override val loadBefore: List<Dependency> = emptyList(),
        override val permissionPrefix: String? = null,
        override val endpoints: ModuleEndpoints = emptyMap(),
        override val permissions: ModulePermissions = emptyMap(),
        override val additionalProperties: Map<String, JsonElement> = emptyMap()
) : ModuleManifest