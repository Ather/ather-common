package com.atherapp.common.modules

interface ModuleManager {
    /**
     * Get a list of all modules
     */
    val modules: List<ModuleWrapper>

    /**
     * Get a list of modules which have their dependencies loaded.
     */
    val resolvedModules: List<ModuleWrapper>

    /**
     * Get a list of all modules with the given state
     */
    fun getModules(state: ModuleState) = this.modules.filter { it.moduleState == state }


}