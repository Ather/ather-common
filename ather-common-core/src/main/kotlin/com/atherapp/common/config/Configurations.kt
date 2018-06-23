package com.atherapp.common.config

import com.github.salomonbrys.kotson.set
import com.github.salomonbrys.kotson.toJson
import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface Configuration {
    val configName: String

    operator fun get(key: String): ConfigurationElement?
    
    operator fun set(key: String, value: ConfigurationElement?)
}

interface PersistentConfiguration : Configuration {
    val autoUpdate: Boolean

    fun load()

    fun save()
}

/**
 * A basic interface used for Configurations that support construction.
 * To support this functionality, simply make the configuration's
 * companion object inherit this interface, and call the constructor of the configuration class
 */
interface ConfigurationCompanion {
    fun newConfig(configName: String): BaseConfiguration
}

/**
 * A [BaseConfiguration] is designed for storing configurations inside JSON structures.
 * It provides basic information about a given configuration, and allows the loading and saving
 * of objects.
 *
 * It automatically calls the [save] function when a value is updated, and automatically calls
 * [load] (and [loadJson]) when the constructor is called
 *
 * More complex objects can be stored inside the configuration.
 *
 * All implementing classes must have a single-arg constructor AS THE FIRST CONSTRUCTOR accepting only a config name to be properly supported by the [ConfigurationManager]
 */
abstract class BaseConfiguration : PersistentConfiguration {
    abstract override val configName: String

    override var autoUpdate: Boolean = true

    lateinit var json: JsonObject

    init {
        this.load()
    }

    /**
     * Get the JsonElement at the given key, or null if it doesn't exist
     * TODO Support default values, perhaps with an elvis operator calling a default json object
     */
    override operator fun get(key: String): ConfigurationElement? = DefaultConfigurationElement(this, json[key])

    /**
     * Set the value at the given key to the nullable [JsonElement]
     */
    override operator fun set(key: String, value: ConfigurationElement?) {
        json[key] = value
        if (autoUpdate)
            this.save()
    }

    /**
     * Retrieve the string representation of the Json from the persistence layer
     */
    abstract fun loadJson(): String

    /**
     * Call loadJson and set the internal [json] value
     */
    override fun load() {
        this.json = this.loadJson().toJson().asJsonObject
    }

    /**
     * Save the [json] value to the persistence location
     */
    abstract override fun save()
}

/**
 * Combination of any number of underlying configurations
 *
 * This should only be used for reading values, and updating existing values only.
 * By creating new values, they will end up in configs you possibly don't want.
 */
class UnionConfiguration(
        vararg val configs: BaseConfiguration
) : BaseConfiguration() {
    override val configName: String = configs.joinToString("+") { it.configName }

    /**
     * Return the first matching value inside the union
     */
    override operator fun get(key: String): ConfigurationElement? {
        for (config in configs) {
            val confVal = config[key]
            if (confVal != null)
                return confVal
        }

        return null
    }

    /**
     * Return a list of all matching config values inside the union
     */
    fun getAll(key: String) = configs.mapNotNull { it[key] }

    /**
     * Update a value inside a config. This only works for updating values, not creating new values.
     */
    override operator fun set(key: String, value: ConfigurationElement?) {
        for (config in configs) {
            val confVal = config[key]
            if (confVal != null) {
                config[key] = value
                if (autoUpdate)
                    config.save()
            }
        }
    }

    /**
     * Update or set a value inside a specified config.
     */
    operator fun set(configName: String, key: String, value: ConfigurationElement?) {
        val config = configs.find { it.configName == configName }
        if (config != null) {
            config[key] = value
            if (autoUpdate)
                config.save()
        }

    }

    override fun loadJson(): String = ""

    override fun load() {}

    /**
     * Save a specific config inside the union
     */
    fun save(configName: String) {
        configs.find { it.configName == configName }?.save()
    }

    /**
     * Save a specific config using a [ConfigurationKey]
     */
    fun save(configKey: ConfigurationKey) = save(configKey.name)

    /**
     * Save all configs in the union
     */
    override fun save() {
        for (config in configs)
            config.save()
    }
}

/**
 * Local configuration stored on the local filesystem.
 * It is created by passing a relative path to the default config location of the running JVM instance
 */
class LocalConfiguration(
        override val configName: String,
        val relativePath: String = configName
) : BaseConfiguration() {
    override fun loadJson(): String {
        TODO("not implemented")
    }

    override fun save() {
        TODO("not implemented")
    }

    companion object: ConfigurationCompanion {
        override fun newConfig(configName: String): LocalConfiguration = LocalConfiguration(configName)
    }
}

/**
 * Database configuration stored on a database system.
 * To create one, you pass the ID/Name of a database profile configured on the system
 *
 * TODO Consider that this will have to assume a local configuration exists to get database info
 *
class DatabaseConfiguration(
override val configName: String,
//TODO Use default database profile
val databaseProfile: String = ConfigurationManager
) : BaseConfiguration()*/