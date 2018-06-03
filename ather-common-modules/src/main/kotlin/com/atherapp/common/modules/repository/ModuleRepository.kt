package com.atherapp.common.modules.repository

/**
 * List of [ModuleRepository] stored inside a JSON file
 */
typealias ModuleRepositories = List<ModuleRepository>

/**
 * Remote module repository configuration
 *
 * This tells the module system about where to check for updates, releases, etc.
 *
 * When looking for an artifact, the system will iterate over the repositories in the order they appear in their configuration,
 * looking for the required artifact. The local module repository (which is not specified in the config) is tried first.
 */
interface ModuleRepository {
    /**
     * The locally unique ID of the repository
     */
    val id: String

    /**
     * The friendly name of the repository
     */
    val name: String

    /**
     * The base URL of the repository.
     * For the official repository, this would be https://modules.ather.app/,
     * where groups and artifacts are appended directly to that.
     */
    val url: String

    /**
     * Options for handling of modules in release mode
     * A module in release mode cannot (normally) replace the artifact of a given tag.
     */
    val releases: ReleaseOptions

    /**
     * Options for handling of modules in snapshot mode
     * Snapshots basically are designed to allow updating of the same tag.
     */
    val snapshots: ReleaseOptions
}

data class DataModuleRepository(
        override val id: String,
        override val name: String = id,
        override val url: String,
        override val releases: ReleaseOptions = DataReleaseOptions(),
        override val snapshots: ReleaseOptions = DataReleaseOptions()
) : ModuleRepository

interface ReleaseOptions {
    /**
     * Whether the release type is used in the repository/should be checked for updates.
     */
    val enabled: Boolean

    /**
     * The update policy to use for the release type
     */
    val updatePolicy: UpdatePolicy
}

data class DataReleaseOptions(
        override val enabled: Boolean = true,
        override val updatePolicy: UpdatePolicy = DefaultUpdatePolicy.Daily
) : ReleaseOptions

/**
 * Update policy for a release type
 *
 * An [interval] of -2 indicates [DefaultUpdatePolicy.Always],
 * an [interval] of -1 indicates [DefaultUpdatePolicy.Never]
 */
interface UpdatePolicy {
    /**
     * Interval in minutes indicating how frequently to check for updates
     */
    val interval: Int
}

/**
 * Default update policies
 */
enum class DefaultUpdatePolicy(
        override val interval: Int
) : UpdatePolicy {
    /**
     * Whenever the modules system is started
     */
    Always(-2),
    /**
     * Every day during the maintenance period
     */
    Daily(1440),
    /**
     * Never update
     */
    Never(-1);
}

/**
 * Custom update policy set on an interval in minutes
 */
data class IntervalUpdatePolicy(
        override val interval: Int
) : UpdatePolicy