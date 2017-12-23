package media.thehoard.common.plugins;

import media.thehoard.common.configuration.HoardConfiguration;
import org.pf4j.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HoardPluginManager extends AbstractPluginManager {
	protected PluginClasspath pluginClasspath;

	@Override
	protected PluginRepository createPluginRepository() {
		return new DefaultPluginRepository(Paths.get(HoardConfiguration.contents().getPluginConfigurationInfo().getPluginsRoot()), isDevelopment());
	}

	@Override
	protected PluginFactory createPluginFactory() {
		return new DefaultPluginFactory();
	}

	@Override
	protected ExtensionFactory createExtensionFactory() {
		return new DefaultExtensionFactory();
	}

	@Override
	protected PluginDescriptorFinder createPluginDescriptorFinder() {
		return new HoardPluginDescriptorFinder();
	}

	@Override
	protected ExtensionFinder createExtensionFinder() {
		DefaultExtensionFinder extensionFinder = new DefaultExtensionFinder(this);
		addPluginStateListener(extensionFinder);

		return extensionFinder;
	}

	@Override
	protected PluginStatusProvider createPluginStatusProvider() {
		return new DefaultPluginStatusProvider(getPluginsRoot());
	}

	@Override
	protected PluginLoader createPluginLoader() {
		return new CompoundPluginLoader()
				.add(new DefaultPluginLoader(this, pluginClasspath))
				.add(new JarPluginLoader(this));
	}

	@Override
	protected VersionManager createVersionManager() {
		return new DefaultVersionManager();
	}

	@Override
	protected Path createPluginsRoot() {
		return Paths.get(HoardConfiguration.contents().getPluginConfigurationInfo().getPluginsRoot());
	}
}
