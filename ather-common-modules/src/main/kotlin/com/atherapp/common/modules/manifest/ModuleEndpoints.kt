package com.atherapp.common.modules.manifest

import io.vertx.core.http.HttpMethod

/**
 * Map of endpoint locations to [Endpoint] information
 */
typealias ModuleEndpoints = Map<String, Endpoint>

/**
 * Endpoint declaration inside a module's manifest
 */
interface Endpoint {
    /**
     * List of HTTP methods used by the endpoint
     */
    val methods: List<HttpMethod>

    /**
     * Aliases of the endpoint. These are automatically registered to the same
     * endpoint function, but undergo simple filtering.
     *
     * Aliases are designed to make it easier to produce backwards compatible code, and should be used as such.
     *
     * Aliases cannot override default endpoints in the system. So, an alias cannot be "/", "/&#42", "/api/", etc.
     */
    val aliases: List<String>

    /**
     * Short description of the endpoint's functionality.
     */
    val description: String?

    /**
     * List of possible permission requirements to use an endpoint
     */
    val permissions: List<String>

    /**
     * Whether the endpoint is hidden.
     *
     * If true, instead of returning a 403 when a user doesn't have permission,
     * it returns a 404, to indicate it doesn't exist.
     */
    val hidden: Boolean

    /**
     * Error message to return in a 403 response if the user doesn't have permission
     */
    val permissionMessage: String

    /**
     * Short description on how to use the endpoint, keep in mind this is different than
     * what the endpoint does (in the [description]).
     */
    val usage: String?
}

data class DataEndpoint(
        override val methods: List<HttpMethod> = listOf(HttpMethod.GET),
        override val aliases: List<String> = emptyList(),
        override val description: String? = null,
        override val permissions: List<String> = emptyList(),
        override val hidden: Boolean = false,
        override val permissionMessage: String = "You don't have permission to use this endpoint.",
        override val usage: String? = null
) : Endpoint