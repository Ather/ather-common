package com.atherapp.common.modules

import org.pf4j.DefaultPluginManager
import org.pf4j.PluginManager
import kotlin.reflect.KClass

private val moduleManager = DefaultPluginManager()

object DefaultModuleManager : PluginManager by moduleManager {
    init {
        moduleManager.loadPlugins()
        moduleManager.startPlugins()
    }
    //TODO Use configuration information from the ArgumentConfig and other configs to choose directories, etc.

    /**
     * Allow [] operator calls inside the manager
     */
    operator fun <T : Any> get(type: KClass<out T>, moduleId: String? = null): MutableList<out T> {
        return if (moduleId == null)
            getExtensions(type.java)
        else
            getExtensions(type.java, moduleId)
    }
}

/**
 * Simple extension method for any object to get modules of its type from the default module manager
 */
inline fun <reified T : Any> KClass<T>.modules(moduleId: String? = null) = DefaultModuleManager[this, moduleId]