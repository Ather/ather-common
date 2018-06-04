package com.atherapp.common.modules

import mu.KLogging
import java.lang.reflect.Modifier

/**
 * Creates a module instance
 */
interface ModuleFactory {
    fun create(moduleWrapper: ModuleWrapper): Module?
}

/**
 * Default implementation of the [ModuleFactory]
 * Uses the [java.lang.reflect.Constructor.newInstance] method
 */
object DefaultModuleFactory : KLogging(), ModuleFactory {
    override fun create(moduleWrapper: ModuleWrapper): Module? {
        val moduleEntry = moduleWrapper.moduleManifest.mainClass
        logger.debug { "Creating instance for module '$moduleEntry'" }

        val entryClass: Class<*> = try {
            moduleWrapper.moduleClassLoader.loadClass(moduleEntry)
        } catch (e: ClassNotFoundException) {
            logger.error(e) { e.message }
            return null
        }

        // Test if the class is a valid module
        val modifiers = entryClass.modifiers
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers) || (!Module::class.java.isAssignableFrom(entryClass))) {
            logger.error { "The module class '$moduleEntry' is not valid" }
            return null
        }

        // Create the module
        try {
            val constructor = entryClass.getConstructor(ModuleWrapper::class.java)
            return constructor.newInstance(moduleWrapper) as Module?
        } catch (e: Exception) {
            logger.error(e) { e.message }
        }

        return null
    }
}