package com.atherapp.common.config

import com.atherapp.common.json.Json
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.set
import com.google.gson.JsonElement
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.javaType

/**
 * Basic Configuration Element used by the [ConfigurationManager].
 * This allows basic property getting and setting from the config.
 * When getting an element, a class is specified to deserialize to.
 */
interface ConfigurationElement {
    /**
     * Get a [BaseConfigurationElement] using a [ConfigurationKey] of type [T]
     */
    operator fun <T : BaseConfigurationElement> get(configKey: ConfigurationKey, klass: KClass<T>): T

    /**
     * Get a [BaseConfigurationElement] using a [String] of type [T]
     */
    operator fun <T : BaseConfigurationElement> get(key: String, klass: KClass<T>): T

    /**
     * Update/Set a key to pair to a [BaseConfigurationElement] of type [T]
     */
    operator fun <T : BaseConfigurationElement> set(configKey: ConfigurationKey, value: T)

    /**
     * Update/Set a key to pair to a [BaseConfigurationElement] of type [T]
     */
    operator fun <T : BaseConfigurationElement> set(key: String, value: T)
}

/**
 * Use reified generics to omit [KClass] usage.
 * @see ConfigurationElement.get
 */
inline operator fun <reified T : BaseConfigurationElement> ConfigurationElement.get(configKey: ConfigurationKey): T = this[configKey, T::class]

/**
 * Use reified generics to omit [KClass] usage.
 * @see ConfigurationElement.get
 */
inline operator fun <reified T : BaseConfigurationElement> ConfigurationElement.get(key: String): T = this[key, T::class]

/**
 * A simple configuration element used for delegating into other objects.
 * All special classes used inside a [Configuration] must implement this,
 * and should handle auto updating via a [BaseConfigurationElement.Delegate]
 */
abstract class BaseConfigurationElement(
        protected val config: PersistentConfiguration,
        protected val json: JsonElement
) {
    inner class Delegate<T : Any>(element: BaseConfigurationElement) {
        private val config: PersistentConfiguration = element.config
        private val json: JsonElement = element.json

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T = Json.deserialize(json[property.name], property.returnType.javaType)

        operator fun setValue(baseElement: BaseConfigurationElement, property: KProperty<*>, value: T?) {
            //FIXME Serialization like this will not work properly with a generic type T. Use inline reified functions instead
            json[property.name] = Json.serializeToTree(value)
            if (config.autoUpdate)
                config.save()
        }
    }

    protected fun <T : Any> delegate() = Delegate<T>(this)
}

class DefaultConfigurationElement(config: PersistentConfiguration, json: JsonElement) : BaseConfigurationElement(config, json), ConfigurationElement {
    override operator fun <T : BaseConfigurationElement> get(configKey: ConfigurationKey, klass: KClass<T>): T = this[configKey.name, klass]

    override operator fun <T : BaseConfigurationElement> get(key: String, klass: KClass<T>): T = Json.deserialize(json[key], klass.java)

    override operator fun <T : BaseConfigurationElement> set(configKey: ConfigurationKey, value: T) {
        this[configKey.name] = value
    }

    override operator fun <T : BaseConfigurationElement> set(key: String, value: T) {
        //FIXME Same as previous fixme, change to reified inline functions to get type information for serializeToTree
        json[key] = Json.serializeToTree(value)
        if (config.autoUpdate)
            config.save()
    }
}