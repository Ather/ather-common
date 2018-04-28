package com.atherapp.common.modules

import com.atherapp.common.json.Json
import org.apache.commons.io.IOUtils
import org.pf4j.PluginDependency
import org.pf4j.PluginDescriptor
import java.io.InputStream

class AtherModuleDescriptor(inputStream: InputStream) : PluginDescriptor {
    private val content: DescriptorContent = Json.deserialize(IOUtils.toString(inputStream, "UTF-8"))

    override fun getProvider(): String = content.author ?: ""

    override fun getVersion(): String = content.version ?: ""

    override fun getPluginDescription(): String = content.description ?: ""

    override fun getDependencies(): List<PluginDependency> = content.depend ?: listOf()

    override fun getPluginClass(): String = content.main ?: ""

    override fun getRequires(): String = content.apiVersion ?: ""

    override fun getPluginId(): String = content.name ?: ""

    override fun getLicense(): String = content.license ?: ""

    private inner class DescriptorContent {
        /**
         * Module name
         */
        internal var name: String? = null

        /**
         * Module SemVer
         */
        internal var version: String? = null

        /**
         * Module Description
         */
        internal var description: String? = null

        //TODO Consider some type of load-stage config. Meaning to load the plugin at startup, or later.

        /**
         * Module author
         */
        internal var author: String? = null

        /**
         * List of module authors
         */
        internal var authors: List<String>? = listOf()

        /**
         * Module website
         */
        internal var website: String? = null

        /**
         * Module entry point
         */
        internal var main: String? = null

        /**
         * Whether the module needs database access
         * TODO
         */
        internal var database: Boolean? = null

        /**
         * Module dependencies
         */
        internal var depend: List<PluginDependency>? = listOf()

        //TODO Possibly restrict to not allow "media.thehoard"
        /**
         * Module logging prefix to use instead of using the full name
         */
        internal var prefix: String? = null

        //TODO Define API endpoints

        //TODO Define Permissions

        /**
         * Module license
         */
        internal var license: String? = null

        /**
         * Base API Version the module is built on
         */
        internal var apiVersion: String? = null
    }
}
