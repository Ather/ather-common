package com.atherapp.common.json

import com.atherapp.common.json.gson.*
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.Reader
import java.io.StringWriter
import java.lang.reflect.Type
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

object Json {
    /**
     * Standard adapters used for java.time classes
     */
    private val STANDARD_ADAPTERS = arrayOf(
            DayOfWeek::class to DayOfWeekTypeAdapter,
            LocalDate::class to LocalDateTypeAdapter,
            LocalTime::class to LocalTimeTypeAdapter,
            //TODO ZonedDateTime::class to ZonedDateTimeTypeAdapter(),
            ZoneId::class to ZoneIdTypeAdapter
    )

    /**
     * Mapping adapters for Interfaces to Classes used in the client
     */
    private val DEFAULT_INTERFACE_MAPPERS = DefaultInterfaceAdapters.adapters

    /**
     * Array of currently-in-use type adapters stored as ( Type, IsCustom )
     */
    private var currentAdapters = arrayOf<Pair<Type, Boolean>>()

    var gson: Gson = newGson()
        internal set

    fun serialize(src: Any?, prettyPrint: Boolean = false): String {
        if (src != null && prettyPrint) {
            val writer = StringWriter()
            val jsonWriter = JsonWriter(writer)
            jsonWriter.setIndent("  ")
            gson.toJson(src, src::class.java, jsonWriter)
            return writer.toString()
        }

        return gson.toJson(src)
    }

    fun serializeToTree(src: Any?): JsonElement = gson.toJsonTree(src)

    fun <T> deserialize(json: String?, typeOfT: Type): T = gson.fromJson(json, typeOfT)

    fun <T> deserialize(jsonElem: JsonElement?, typeOfT: Type): T = gson.fromJson(jsonElem, typeOfT)

    fun <T> deserialize(jsonReader: JsonReader?, typeOfT: Type): T = gson.fromJson(jsonReader, typeOfT)

    inline fun <reified T : Any> deserialize(json: String): T = gson.fromJson(json)

    inline fun <reified T : Any> deserialize(jsonElem: JsonElement?): T = gson.fromJson(jsonElem, T::class.java)

    inline fun <reified T : Any> deserialize(jsonReader: JsonReader?): T = gson.fromJson(jsonReader, T::class.java)

    fun registerAdapters(vararg typeAdapters: Pair<Type, Any>, retainAdapters: Boolean = true) = newGson(*typeAdapters, retainAdapters = retainAdapters)

    @Synchronized
    internal fun newGson(vararg typeAdapters: Pair<Type, Any>, retainAdapters: Boolean = true): Gson {
        val builder = GsonBuilder()

        // List of adapters being registered to Gson
        val usedAdapters = mutableListOf<Pair<Type, Boolean>>()

        // Register the new adapters
        for ((type, adapter) in typeAdapters) {
            builder.registerTypeAdapter(type, adapter)
            usedAdapters.add(type to true)
        }

        // Register any old adapters not already replaced if we want to retain the old adapters
        if (retainAdapters) {
            for ((type, adapter) in currentAdapters) {
                if (type to true !in usedAdapters) {
                    builder.registerTypeAdapter(type, adapter)
                    usedAdapters.add(type to true)
                }
            }
        }

        // Register standard adapters
        for ((TYPE, ADAPTER) in STANDARD_ADAPTERS) {
            // Verify that a custom adapter wasn't already registered
            if ((TYPE.java to true) !in usedAdapters) {
                builder.registerTypeAdapter(TYPE.java, ADAPTER)
                usedAdapters.add(TYPE.java to false)
            }
        }

        // Register implementation adapters
        for ((INTERFACE_CLASS, INTERFACE_MAPPER) in DEFAULT_INTERFACE_MAPPERS)
            if ((INTERFACE_CLASS.java to true) !in usedAdapters)
                builder.registerTypeAdapter(INTERFACE_CLASS.java, INTERFACE_MAPPER)

        currentAdapters = usedAdapters.toTypedArray()

        return builder.create()
    }
}