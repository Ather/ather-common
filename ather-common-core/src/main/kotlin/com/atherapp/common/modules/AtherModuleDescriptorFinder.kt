package com.atherapp.common.modules

import org.pf4j.PluginDescriptorFinder
import org.pf4j.PluginException
import org.pf4j.util.FileUtils

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarFile

class AtherModuleDescriptorFinder : PluginDescriptorFinder {

    override fun isApplicable(pluginPath: Path) = Files.exists(pluginPath) && (Files.isDirectory(pluginPath) || FileUtils.isJarFile(pluginPath))

    override fun find(pluginPath: Path): AtherModuleDescriptor {
        if (FileUtils.isJarFile(pluginPath)) {
            try {
                val jar = JarFile(pluginPath.toFile())
                val entry = jar.getJarEntry(DESCRIPTOR_NAME)
                        ?: throw PluginException("Failed to find manifest: $DESCRIPTOR_NAME")

                return AtherModuleDescriptor(jar.getInputStream(entry))
            } catch (e: IOException) {
                throw PluginException(e)
            }

        } else {
            val descriptorPath = (if (Files.isDirectory(pluginPath)) FileUtils.findFile(pluginPath, DESCRIPTOR_NAME) else null)
                    ?: throw PluginException("Failed to find manifest: $DESCRIPTOR_NAME")

            try {
                return AtherModuleDescriptor(Files.newInputStream(descriptorPath))
            } catch (e: IOException) {
                throw PluginException(e)
            }

        }
    }

    companion object {
        private const val DESCRIPTOR_NAME = "plugin.json"
    }
}
