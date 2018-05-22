package com.atherapp.common.api.modules.io.providers

enum class DeleteMode {
    /**
     * Don't delete files during sync
     */
    Off,
    /**
     * Delete files before transferring
     */
    Before,
    /**
     * Delete files while transferring
     */
    During,
    /**
     * Delete files after transferring (default)
     */
    After;
}