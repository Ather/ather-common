package com.atherapp.common.modules.manifest

import com.atherapp.common.json.gson.nextStringOrNull
import com.atherapp.common.modules.manifest.permissions.MutableDataPermission
import com.atherapp.common.modules.manifest.permissions.PermissionDefault
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.string
import com.github.salomonbrys.kotson.toJson
import com.github.salomonbrys.kotson.typeAdapter
import com.google.gson.*
import com.google.gson.stream.JsonToken
import io.vertx.core.http.HttpMethod
import java.lang.reflect.Type

val moduleManifestTypeAdapter = typeAdapter<ModuleManifest> {
    read {
        MutableModuleManifest().apply {
            if (peek() == JsonToken.BEGIN_OBJECT) {
                beginObject()
                while (hasNext()) {
                    if (peek() == JsonToken.NAME) {
                        when (nextName()) {
                            "group" -> {
                                if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal group name, must be a string: $path")
                                group = nextString().takeIf { it.isNotBlank() } ?: throw IllegalManifestException("Illegal group name, cannot be blank: $path")
                            }
                            "artifact" -> {
                                if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal artifact name, must be a string: $path")
                                artifact = nextString().takeIf { it.isNotBlank() } ?: throw IllegalManifestException("Illegal artifact name, cannot be blank: $path")
                            }
                            "tag" -> {
                                if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal tag, must be a string: $path")
                                tag = nextString().takeIf { it.isNotBlank() } ?: throw IllegalManifestException("Illegal tag, cannot be blank: $path")
                            }
                            "alias" -> {
                                if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal alias, must be a string or null: $path")
                                alias = nextStringOrNull().also { if (it != null && it.isBlank()) throw IllegalManifestException("Illegal alias, cannot be blank: $path") }
                            }
                            "description" -> {
                                if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal description, must be a string or null: $path")
                                description = nextStringOrNull()
                            }
                            "loadStage" -> {
                                if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal load stage, must be a string or null: $path")
                                loadStage = nextStringOrNull()?.let {
                                    try {
                                        ModuleLoadStage.valueOf(it)
                                    } catch (e: IllegalArgumentException) {
                                        throw IllegalManifestException("Illegal load stage: $path")
                                    }
                                } ?: ModuleLoadStage.POST_API
                            }
                            "authors" -> {
                                if (peek() == JsonToken.BEGIN_ARRAY) {
                                    beginArray()
                                    while (hasNext()) {
                                        if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal author: $path")
                                        authors.add(nextString().takeIf { it.isNotBlank() }
                                                ?: throw IllegalManifestException("Illegal author: $path"))
                                    }
                                    if (peek() == JsonToken.END_ARRAY) endArray()
                                    else throw IllegalManifestException("The authors section does not end with an array token \"]\": $path")
                                } else
                                    throw IllegalManifestException("The authors section does not start with an array token \"[\": $path")
                            }
                            "website" -> {
                                if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal website, must be a string or null: $path")
                                website = nextStringOrNull().also { if (it != null && it.isBlank()) throw IllegalManifestException("Illegal website, cannot be blank: $path") }
                            }
                            "mainClass" -> {
                                if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal entry point (mainClass), must be a string: $path")
                                mainClass = nextString().takeIf { it.isNotBlank() } ?: throw IllegalManifestException("Illegal entry point (mainClass), cannot be blank: $path")
                            }
                            "database" -> {
                                if (peek() != JsonToken.BOOLEAN) throw IllegalManifestException("Illegal database usage value, must be a boolean: $path")
                                database = nextBoolean()
                            }
                            "jarDependencies" -> {
                                if (peek() == JsonToken.BEGIN_ARRAY) {
                                    beginArray()
                                    while (hasNext()) {
                                        if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal jar dependencies section, must contain only strings: $path")
                                        val dependency = nextString()
                                        jarDependencies.add(dependency.split(":").run {
                                            when (size) {
                                                2 -> DataJarDependency(groupId = this[0], artifactId = this[1])
                                                3 -> DataJarDependency(groupId = this[0], artifactId = this[1], version = this[3])
                                                else -> throw IllegalManifestException("Invalid jar dependency: $dependency @ $path")
                                            }
                                        })
                                    }
                                    if (peek() == JsonToken.END_ARRAY) endArray()
                                    else throw IllegalManifestException("The jar dependencies section does not end with an array token \"]\": $path")
                                } else
                                    throw IllegalManifestException("The jar dependencies section does not start with an array token \"[\": $path")
                            }
                            "dependencies" -> {
                                if (peek() == JsonToken.BEGIN_ARRAY) {
                                    beginArray()
                                    while (hasNext()) {
                                        if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal dependency section, must contain only strings: $path")
                                        dependencies.add(moduleDependencyFromString(nextString()))
                                    }
                                    if (peek() == JsonToken.END_ARRAY) endArray()
                                    else throw IllegalManifestException("The dependencies section does not end with an array token \"]\": $path")
                                } else
                                    throw IllegalManifestException("The dependencies section does not start with an array token \"[\": $path")
                            }
                            "softDependencies" -> {
                                if (peek() == JsonToken.BEGIN_ARRAY) {
                                    beginArray()
                                    while (hasNext()) {
                                        if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal soft dependency: $path")
                                        dependencies.add(moduleDependencyFromString(nextString()))
                                    }
                                    if (peek() == JsonToken.END_ARRAY) endArray()
                                    else throw IllegalManifestException("The soft dependencies section does not end with an array token \"]\": $path")
                                } else
                                    throw IllegalManifestException("The soft dependencies section does not start with an array token \"[\": $path")
                            }
                            "logPrefix" -> {
                                if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal log prefix, must be a string or null: $path")
                                logPrefix = nextStringOrNull()
                            }
                            "loadBefore" -> {
                                if (peek() == JsonToken.BEGIN_ARRAY) {
                                    beginArray()
                                    while (hasNext()) {
                                        if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal load before id: $path")
                                        val moduleId = nextString()
                                        loadBefore.add(moduleId.split("/").run {
                                            if (size != 2)
                                                throw IllegalManifestException("Invalid module id format: $moduleId @ $path")
                                            DataDependency(group = this[0], artifact = this[1])
                                        })
                                    }
                                    if (peek() == JsonToken.END_ARRAY) endArray()
                                    else throw IllegalManifestException("The load before section does not end with an array token \"]\": $path")
                                } else
                                    throw IllegalManifestException("The load before section does not start with an array token \"[\": $path")
                            }
                            "permissionPrefix" -> {
                                if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal module permission prefix, must be a string or null: $path")
                                permissionPrefix = nextStringOrNull().also { if (it != null && it.isBlank()) throw IllegalManifestException("Illegal module permission prefix, cannot be blank: $path") }
                            }
                            "endpoints" -> {
                                if (peek() == JsonToken.BEGIN_OBJECT) {
                                    beginObject()
                                    while (hasNext()) {
                                        lateinit var endpoint: String
                                        val endpointData = MutableDataEndpoint().apply {
                                            if (peek() == JsonToken.NAME) {
                                                if (group == "" || artifact == "")
                                                    throw IllegalManifestException("The group and artifact names must be declared before the endpoints section")
                                                endpoint = expandEndpoint(group, artifact, nextName())
                                                beginObject()
                                                when (nextName()) {
                                                    "methods" -> {
                                                        if (peek() == JsonToken.BEGIN_ARRAY) {
                                                            beginArray()
                                                            // Clear the default GET method from the array, since it is being declared
                                                            if (hasNext()) methods.clear()
                                                            while (hasNext()) {
                                                                if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal endpoint declaration: $path")
                                                                methods.add(nextString()?.let {
                                                                    try {
                                                                        HttpMethod.valueOf(it)
                                                                    } catch (e: IllegalArgumentException) {
                                                                        throw IllegalManifestException("Illegal endpoint HTTP method: $it @ $path")
                                                                    }
                                                                }
                                                                        ?: throw IllegalManifestException("Illegal endpoint HTTP method: $path"))
                                                            }
                                                            if (peek() == JsonToken.END_ARRAY) endArray()
                                                            else throw IllegalManifestException("An endpoint's method list does not end with an array token \"]\": $path")
                                                        } else
                                                            throw IllegalManifestException("An endpoint's method list does not start with an array token \"[\": $path")
                                                    }
                                                    "aliases" -> {
                                                        if (peek() == JsonToken.BEGIN_ARRAY) {
                                                            beginArray()
                                                            while (hasNext()) {
                                                                if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal endpoint alias declaration: $path")
                                                                aliases.add(expandEndpoint(group, artifact, nextString(), true))
                                                            }
                                                            if (peek() == JsonToken.END_ARRAY) endArray()
                                                            else throw IllegalManifestException("An endpoint's alias list does not end with an array token \"]\": $path")
                                                        }
                                                        else
                                                            throw IllegalManifestException("An endpoint's alias list does not start with an array token \"[\": $path")
                                                    }
                                                    "description" -> {
                                                        if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal endpoint description: $path")
                                                        description = nextStringOrNull()
                                                    }
                                                    "permissions" -> {
                                                        if (peek() == JsonToken.BEGIN_ARRAY) {
                                                            beginArray()
                                                            while (hasNext()) {
                                                                if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal endpoint permission: $path")
                                                                permissions.add(expandPermission(permissionPrefix, nextString()))
                                                            }
                                                            if (peek() == JsonToken.END_ARRAY) endArray()
                                                            else throw IllegalManifestException("An endpoint's permission list does not end with an array token \"]\": $path")
                                                        }
                                                        else
                                                            throw IllegalManifestException("An endpoint's permission list does not start with an array token \"[\": $path")
                                                    }
                                                    "hidden" -> {
                                                        if (peek() != JsonToken.BOOLEAN) throw IllegalManifestException("Illegal 'hidden' value for endpoint, must be a boolean: $path")
                                                        hidden = nextBoolean()
                                                    }
                                                    "permissionMessage" -> {
                                                        if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal endpoint permission message, must be a string or null: $path")
                                                        permissionMessage = nextStringOrNull() ?: "You don't have permission to use this endpoint."
                                                    }
                                                    "usage" -> {
                                                        if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal endpoint usage message, must be a string or null: $path")
                                                        usage = nextStringOrNull()
                                                    }
                                                    else -> throw IllegalManifestException("Extra section in endpoint declaration: $path")
                                                }
                                                if (peek() == JsonToken.END_OBJECT) endObject()
                                                else throw IllegalManifestException("An endpoint node does not end with an object token \"}\": $path")
                                            } else
                                                throw IllegalManifestException("An endpoint node does not start with an object token \"{\": $path")
                                        }
                                        endpoints[endpoint] = endpointData
                                    }
                                    if (peek() == JsonToken.END_OBJECT) endObject()
                                    else throw IllegalManifestException("The endpoints section does not end with an object token \"}\": $path")
                                } else
                                    throw IllegalManifestException("The endpoints section does not start with an object token \"{\": $path")
                            }
                            "permissions" -> {
                                if (peek() == JsonToken.BEGIN_OBJECT) {
                                    beginObject()
                                    while (hasNext()) {
                                        lateinit var permission: String
                                        val permissionData = MutableDataPermission().apply {
                                            if (peek() == JsonToken.NAME) {
                                                permission = expandPermission(permissionPrefix, nextName())
                                                beginObject()
                                                when (nextName()) {
                                                    "description" -> {
                                                        if (peek() != JsonToken.STRING && peek() != JsonToken.NULL) throw IllegalManifestException("Illegal permission description, must be a string or null: $path")
                                                        description = nextStringOrNull()
                                                    }
                                                    "children" -> {
                                                        if (peek() == JsonToken.BEGIN_OBJECT) {
                                                            beginObject()
                                                            while (hasNext()) {
                                                                if (peek() == JsonToken.NAME) {
                                                                    val child = expandPermission(permissionPrefix, nextString())
                                                                    if (peek() == JsonToken.BOOLEAN) {
                                                                        children[child] = nextBoolean()
                                                                    } else
                                                                        throw IllegalManifestException("Illegal permission child, must use a boolean value: $path")
                                                                } else
                                                                    throw IllegalManifestException("Illegal permission child, it must start with a JSON name: $path")
                                                            }
                                                            if (peek() == JsonToken.END_OBJECT) endObject()
                                                            else throw IllegalManifestException("A permission child does not end with an object token \"}\": $path")
                                                        } else
                                                            throw IllegalManifestException("A permission child does not start with an object token \"{\": $path")
                                                    }
                                                    "default" -> {
                                                        if (peek() != JsonToken.STRING) throw IllegalManifestException("Illegal permission default: $path")
                                                        default = nextString()?.let {
                                                            try {
                                                                PermissionDefault.valueOf(it)
                                                            } catch (e: IllegalArgumentException) {
                                                                throw IllegalManifestException("Illegal permission default value: $it @ $path")
                                                            }
                                                        } ?: throw IllegalManifestException("Illegal permission default value cannot be null: $path")
                                                    }
                                                }
                                                if (peek() == JsonToken.END_OBJECT) endObject()
                                                else throw IllegalManifestException("A permission node does not end with an object token \"}\": $path")
                                            } else
                                                throw IllegalManifestException("A permission node does not start with an object token \"{\": $path")
                                        }
                                        permissions[permission] = permissionData
                                    }
                                    if (peek() == JsonToken.END_OBJECT) endObject()
                                    else throw IllegalManifestException("The permissions section does not end with an object token \"}\": $path")
                                } else
                                    throw IllegalManifestException("The permissions section does not start with an object token \"{\": $path")
                            }
                            else -> {
                                if (peek() != JsonToken.NULL)
                                    additionalProperties[nextName()] = nextString().toJson()
                            }
                        }
                    } else
                        throw IllegalManifestException("All top level values in the manifest must use JSON name tokens: $path")
                }
                if (peek() == JsonToken.END_OBJECT) endObject()
                else throw IllegalManifestException("The manifest does not end with an object token \"}\"")
            } else
                throw IllegalManifestException("The manifest does not start with an object token \"{\"")
        }
    }
    write {
        beginObject()

        name("group")
        value(it.group)

        name("artifact")
        value(it.artifact)

        name("tag")
        value(it.tag)

        if (it.alias != null) {
            name("alias")
            value(it.alias)
        }

        if (it.description != null) {
            name("description")
            value(it.description)
        }

        name("loadStage")
        value(it.loadStage.toString())

        if (it.authors.isNotEmpty()) {
            name("authors")
            beginArray()
            for (author in it.authors)
                value(author)
            endArray()
        }

        if (it.website != null) {
            name("website")
            value(it.website)
        }

        name("mainClass")
        value(it.mainClass)

        name("database")
        value(it.database)

        if (it.jarDependencies.isNotEmpty()) {
            name("jarDependencies")
            beginArray()
            for (jarDependency in it.jarDependencies)
                value("${jarDependency.groupId}:${jarDependency.artifactId}:${jarDependency.version}")
            endArray()
        }

        if (it.dependencies.isNotEmpty()) {
            name("dependencies")
            beginArray()
            for (dependency in it.dependencies)
                value("${dependency.group}/${dependency.artifact}:${dependency.tag}")
            endArray()
        }

        if (it.softDependencies.isNotEmpty()) {
            name("softDependencies")
            beginArray()
            for (softDependency in it.softDependencies)
                value("${softDependency.group}/${softDependency.artifact}:${softDependency.tag}")
            endArray()
        }

        if (it.logPrefix != null) {
            name("logPrefix")
            value(it.logPrefix)
        }

        if (it.loadBefore.isNotEmpty()) {
            name("loadBefore")
            beginArray()
            for (loadBefore in it.loadBefore)
                value("${loadBefore.group}/${loadBefore.artifact}")
            endArray()
        }

        if (it.permissionPrefix != null) {
            name("permissionPrefix")
            value(it.permissionPrefix)
        }

        if (it.endpoints.isNotEmpty()) {
            name("endpoints")
            beginObject()
            for((endpoint, data) in it.endpoints) {
                name(endpoint)
                beginObject()
                if (data.methods.isNotEmpty()) {
                    name("methods")
                    beginArray()
                    for (method in data.methods)
                        value(method.toString())
                    endArray()
                }
                if (data.aliases.isNotEmpty()) {
                    name("aliases")
                    beginArray()
                    for (alias in data.aliases)
                        value(alias)
                    endArray()
                }
                if (data.description != null) {
                    name("description")
                    value(data.description)
                }
                if (data.permissions.isNotEmpty()) {
                    name("permissions")
                    beginArray()
                    for (permission in data.permissions)
                        value(permission)
                    endArray()
                }

                name("hidden")
                value(data.hidden)

                name("permissionMessage")
                value(data.permissionMessage)

                if (data.usage != null) {
                    name("usage")
                    value(data.usage)
                }
                endObject()
            }
            endObject()
        }

        if (it.permissions.isNotEmpty()) {
            name("permissions")
            beginObject()
            for ((permission, data) in it.permissions) {
                name(permission)
                beginObject()
                if (data.description != null) {
                    name("description")
                    value(data.description)
                }
                if (data.children != null && data.children?.isNotEmpty() == true) {
                    name("children")
                    beginObject()
                    for ((child, inheritance) in data.children ?: emptyMap()) {
                        name(child)
                        value(inheritance)
                    }
                    endObject()
                }

                name("default")
                value(data.default.toString())

                endObject()
            }
            endObject()
        }

        endObject()
    }
}

/**
 * Expand a permission node by verifying its validity, and replacing a starting '$' with
 */
private fun expandPermission(permissionPrefix: String?, permission: String): String {
    return if (permission.startsWith("$")) {
        if (permissionPrefix == null) throw IllegalManifestException("Cannot use relative permissions if the permissionPrefix is null.")
        if (permissionPrefix.isBlank()) throw IllegalManifestException("The permission prefix must be declared before it is used in the endpoints or permissions sections.")
        permission.replaceFirst("$", permissionPrefix)
    } else permission
}

/**
 * Expand an endpoint by verifying its validity, optionally verifying if it is illegal, and replacing a starting '$' with '/api/modules/$group/$artifact'
 */
private fun expandEndpoint(group: String, artifact: String, endpoint: String, enforceValues: Boolean = false): String {
    // Relative endpoints aren't checked against any illegal declarations (such as ending with a wildcard), because they are isolated to their own area of the API.
    return if (endpoint.startsWith("$"))
        endpoint.replaceFirst("$", "/api/modules/$group/$artifact")
    else if (endpoint.isBlank()) throw IllegalManifestException("Endpoint declaration cannot be blank.")
    //TODO Consider testing against all native endpoints
    else if (enforceValues && (endpoint == "/" || endpoint == "/api" || endpoint.endsWith("*")))
        throw IllegalManifestException("Illegal alias declaration, the overloading of a root-level or wildcard endpoint must be done explicitly: '$endpoint'")
    else endpoint
}

/**
 * Create a new [ModuleDependency] from a string.
 * This can't be an extension because there is no companion object to allow it to be static in [ModuleDependency]
 */
private fun moduleDependencyFromString(dependency: String): ModuleDependency {
    return dependency.split("/").run {
        if (size != 2)
            throw IllegalManifestException("Invalid dependency format: $dependency")

        //if (this.all {  }) TODO Name Requirements / Illegal Character Check

        val artifactAndVersion = this[2].split(":")

        return@run when (artifactAndVersion.size) {
            1 -> DataModuleDependency(group = this[0], artifact = artifactAndVersion[0])
            2 -> DataModuleDependency(group = this[0], artifact = artifactAndVersion[0], tag = artifactAndVersion[1])
            else -> throw IllegalManifestException("Invalid dependency format: $dependency")
        }
    }
}

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

        return when (artifactAndVersion.size) {
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