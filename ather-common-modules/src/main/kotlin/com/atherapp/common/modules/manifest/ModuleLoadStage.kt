package com.atherapp.common.modules.manifest

/**
 * When to load a module
 */
enum class ModuleLoadStage {
    /**
     * When the server is first being started up
     */
    STARTUP,
    /**
     * After the API is able to receive requests
     */
    POST_API
}