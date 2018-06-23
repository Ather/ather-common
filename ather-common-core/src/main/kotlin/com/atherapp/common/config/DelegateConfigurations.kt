package com.atherapp.common.config

import com.github.salomonbrys.kotson.byNullableArray
import com.github.salomonbrys.kotson.byNullableObject
import com.github.salomonbrys.kotson.byNullableString
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : Any?> ReadWriteProperty<Any?, T>.byConfigUpdate(config: BaseConfiguration) = UpdateDelegate(config, this)

class UpdateDelegate<T : Any?>(private val config: BaseConfiguration, private val delegate: ReadWriteProperty<Any?, T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = delegate.getValue(thisRef, property)

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        delegate.setValue(thisRef, property, value)
        config.save()
    }
}

/*
class DatabaseConfiguration(val config: BaseConfiguration) : BaseConfiguration(), PersistentConfiguration by config {

    override fun get(key: String): JsonElement? = config[key]

    override fun load() {
        config.load()
    }

    override fun loadJson(): String = config.loadJson()

    var options by config.json.byNullableObject.byConfigUpdate(config)

    //TODO Delegate into ConfigurationOptions class

    inner class DatabaseConfigurationOptions(private val optionObj: JsonObject) {
        operator fun get(opt: String): JsonElement? = optionObj[opt]

        var test by optionObj.byNullableString.byConfigUpdate(config)
    }

    var profiles by config.json.byNullableArray.byConfigUpdate(config)

    inner class Delegate(config: BaseConfiguration) {
        private var config = DatabaseConfiguration(config)

        operator fun getValue(thisRef: Any?, property: KProperty<*>): DatabaseConfiguration = config

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: BaseConfiguration) {
            config = DatabaseConfiguration(value)
        }
    }
}*/