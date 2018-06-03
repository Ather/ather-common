package com.atherapp.common.modules.manifest

/**
 * Basic module dependency
 */
interface Dependency {
    /**
     * Release group of a module
     */
    val group: String
    /**
     * Artifact name
     */
    val artifact: String
}

data class DataDependency(
        override val group: String,
        override val artifact: String
) : Dependency

/**
 * Module dependency which specifies a version/tag
 */
interface ModuleDependency : Dependency {
    /**
     * Version tag of the module
     */
    val tag: String
}

data class DataModuleDependency(
        override val group: String,
        override val artifact: String,
        override val tag: String
) : ModuleDependency

/**
 * Basic maven dependency
 */
interface JarDependency {
    /**
     * Maven groupId
     */
    val groupId: String
    /**
     * Maven artifactId
     */
    val artifactId: String
    /**
     * Maven version
     */
    val version: String
}

data class DataJarDependency(
        override val groupId: String,
        override val artifactId: String,
        override val version: String
) : JarDependency