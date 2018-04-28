package com.atherapp.common.json.gson

import com.atherapp.common.json.Json
import com.google.gson.*
import java.lang.reflect.Type
import kotlin.reflect.KClass

class InterfaceImplementationAdapter<InterfaceClass : Any, ImplementationClass : InterfaceClass>(
        private val interfaceClass: KClass<InterfaceClass>,
        private val implementationClass: KClass<ImplementationClass>
) : JsonDeserializer<InterfaceClass>, JsonSerializer<InterfaceClass> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): InterfaceClass = Json.deserialize(json, implementationClass.java)

    override fun serialize(src: InterfaceClass, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement = Json.serializeToTree(src)
}

inline infix fun <reified InterfaceClass : Any, reified ImplementationClass : InterfaceClass> KClass<InterfaceClass>.toImpl(implClass: KClass<ImplementationClass>) = this to InterfaceImplementationAdapter(this, implClass)