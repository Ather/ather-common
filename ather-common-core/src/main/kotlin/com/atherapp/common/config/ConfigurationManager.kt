package com.atherapp.common.config

import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance

/**
 * Global configuration manager
 *
 * Configs are stored internally, and on the relevant file system (database or local)
 * They are accessed with the [] operator with a string config name, or a [ConfigurationKey] passed as a key.
 * These should be unique for each section of the config used
 */
object ConfigurationManager {
    //TODO Get this from the argument config of the main application
    private val configDirectory = ""

    init {
        //TODO Load configs from configDirectory
    }

    /**
     * Private map of configuration names and configs
     */
    private val configs: MutableMap<String, BaseConfiguration> = mutableMapOf()

    /**
     * Get the config relating to a name, optionally create new configs of the specified type.
     * If non exists, return null
     *
     * @param config Configuration name to use
     * @param createNew Whether to create a new configuration if it doesn't exist, defaults to true
     * @param newConfigClass The BaseConfiguration class to use if creating a new configuration, defaults to [LocalConfiguration]
     */
    operator fun get(config: String, createNew: Boolean = true, newConfigClass: KClass<out BaseConfiguration>? = if (createNew) LocalConfiguration::class else null): BaseConfiguration? {
        // Return the config from the internal list, otherwise return a new config if createNew is true and the config class isn't null
        return configs[config] ?: return if (createNew && newConfigClass != null) {
            val companion = newConfigClass.companionObjectInstance
            return if (companion != null && companion is ConfigurationCompanion)
                companion.newConfig(config)
            else null
        } else null
    }

    /**
     * Non-nullable equivalent to [get] which returns a new config of the specified name if one doesn't exist, or the config if it does.
     */
    operator fun get(config: String, newConfigClass: KClass<out BaseConfiguration> = LocalConfiguration::class): BaseConfiguration = this[config, true, newConfigClass]!!

    /**
     * Get a [UnionConfiguration] of all passed configs, meaning you can use them as one config
     * This will not create new configurations if they don't exist
     */
    operator fun get(vararg configs: String) = UnionConfiguration(*configs.mapNotNull { this[it, false] }.toTypedArray())

    /**
     * Get the config relating to a [ConfigurationKey].
     * The usage of keys allows for easier control of config names
     */
    operator fun get(configKey: ConfigurationKey) = configs[configKey.name]

    /**
     * Get a [UnionConfiguration] for several [ConfigurationKey]s
     */
    operator fun get(vararg configKeys: ConfigurationKey) = UnionConfiguration(*configs.mapNotNull { this[it as ConfigurationKey] }.toTypedArray())

    /**
     * Return a [UnionConfiguration] of all configs inside the manager
     */
    fun getAll() = UnionConfiguration(*configs.map { it.value }.toTypedArray())

    val ARGUMENT_CONFIG
        get() = this["argument"]

    //TODO Come up with a few default configs
    val DEFAULT_CONFIG
        get() = this["default", LocalConfiguration::class]
}

enum class DefaultConfigurationKey(name: String = this.toString().toLowerCase()) : ConfigurationKey {
    DEFAULT,
    ARGUMENT,
    DATABASE,
    MODULES,
    PROVIDERS
}

interface ConfigurationKey {
    val name: String
}