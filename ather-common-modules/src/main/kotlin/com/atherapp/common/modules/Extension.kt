package com.atherapp.common.modules

interface Extension

/**
 * Extension that allows module-aware creation.
 */
interface InstantiableExtension<T : InstantiableExtension<T>> : Extension {
    fun createInstance(wrapper: ModuleWrapper): T
}

