package com.atherapp.common.modules.manifest

import com.github.salomonbrys.kotson.string
import com.github.salomonbrys.kotson.toJson
import com.google.gson.*
import java.lang.reflect.Type

/**
 * Gson type adapter for any [Dependency].
 * Uses a "group/artifact" format
 */
object DependencyTypeAdapter : JsonDeserializer<Dependency>, JsonSerializer<Dependency> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Dependency = json.string.split("/").run {
        if (this.size != 2)
            throw IllegalManifestException("Invalid Dependency Format: ${json.string}")

        //if (this.all {  }) TODO Name Requirements / Illegal Character Check

        DataDependency(group = this[0], artifact = this[1])
    }

    override fun serialize(src: Dependency, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = "${src.group}/${src.artifact}".toJson()
}

/**
 * Gson type adapter for any [ModuleDependency].
 * Uses a "group/artifact:tag" format
 */
object ModuleDependencyTypeAdapter : JsonDeserializer<ModuleDependency>, JsonSerializer<ModuleDependency> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ModuleDependency = json.string.split("/").run {
        if (this.size != 2)
            throw IllegalManifestException("Invalid Dependency Format: ${json.string}")

        //if (this.all {  }) TODO Name Requirements / Illegal Character Check

        val artifactAndVersion = this[2].split(":")

        return when(artifactAndVersion.size) {
            1 -> DataModuleDependency(group = this[0], artifact = artifactAndVersion[0])
            2 -> DataModuleDependency(group = this[0], artifact = artifactAndVersion[0], tag = artifactAndVersion[1])
            else -> throw IllegalManifestException("Invalid Dependency Format: ${json.string}")
        }
    }

    override fun serialize(src: ModuleDependency, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = "${src.group}/${src.artifact}:${src.tag}".toJson()
}

/**
 * Gson type adapter for any [JarDependency].
 * This uses Gradle-like format, "groupId:artifactId:version", but uses maven to resolve dependencies.
 */
object JarDependencyTypeAdapter : JsonDeserializer<JarDependency>, JsonSerializer<JarDependency> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): JarDependency = json.string.split(":").run {
        return when (size) {
            2 -> DataJarDependency(groupId = this[0], artifactId = this[1])
            3 -> DataJarDependency(groupId = this[0], artifactId = this[1], version = this[3])
            else -> throw IllegalManifestException("Invalid Dependency Format: ${json.string}")
        }
    }

    override fun serialize(src: JarDependency, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = "${src.groupId}:${src.artifactId}:${src.version}".toJson()
}