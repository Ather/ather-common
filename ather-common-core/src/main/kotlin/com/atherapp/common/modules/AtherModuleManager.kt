package com.atherapp.common.modules

import com.atherapp.common.configuration.AtherConfiguration
import org.pf4j.*
import java.nio.file.Paths

class AtherModuleManager : AbstractPluginManager() {
    protected var pluginClasspath: PluginClasspath? = null

    override fun createPluginRepository(): PluginRepository {
        return DefaultPluginRepository(Paths.get(AtherConfiguration.contents().pluginConfigurationInfo
                .pluginsRoot), isDevelopment)
    }

    override fun createPluginFactory() = DefaultPluginFactory()

    override fun createExtensionFactory() = DefaultExtensionFactory()

    override fun createPluginDescriptorFinder() = AtherModuleDescriptorFinder()

    override fun createExtensionFinder(): ExtensionFinder {
        val extensionFinder = DefaultExtensionFinder(this)
        addPluginStateListener(extensionFinder)

        return extensionFinder
    }

    override fun createPluginStatusProvider() = DefaultPluginStatusProvider(pluginsRoot)

    override fun createPluginLoader(): PluginLoader {
        return CompoundPluginLoader().add(DefaultPluginLoader(this, pluginClasspath))
                .add(JarPluginLoader(this))
    }

    override fun createVersionManager() = DefaultVersionManager()

    override fun createPluginsRoot() = Paths.get(AtherConfiguration.contents().pluginConfigurationInfo.pluginsRoot)
}
