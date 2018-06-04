package com.atherapp.common.modules

/**
 * Current module state
 */
interface ModuleState {
    /**
     * String representation of the current state
     */
    val status: String
}

/**
 * Default module states
 */
enum class DefaultModuleState : ModuleState {
    /**
     * The manager knows the module exists, knows about its contents, but hasn't been used yet.
     */
    CREATED,
    /**
     * The module is either disabled via config, or was stopped for running for any number of reasons.
     * This can include having an invalid manifest or attempting to make illegal calls, among other things.
     */
    DISABLED,
    /**
     * The module is ready to start; its dependencies, both external and internal have been loaded.
     */
    RESOLVED,
    /**
     * The module has started, with its entry point having been run, which means its extensions are loaded.
     */
    STARTED,
    /**
     * The module has been stopped via its stop function.
     */
    STOPPED;

    override val status = this.toString()
}