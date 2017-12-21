package media.thehoard.common.plugins;

import media.thehoard.common.configuration.HoardConfiguration;
import org.pf4j.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HoardPluginManager extends AbstractPluginManager {
	@Override
	protected PluginRepository createPluginRepository() {
		return null;
	}

	@Override
	protected PluginFactory createPluginFactory() {
		return null;
	}

	@Override
	protected ExtensionFactory createExtensionFactory() {
		return null;
	}

	@Override
	protected PluginDescriptorFinder createPluginDescriptorFinder() {
		return null;
	}

	@Override
	protected ExtensionFinder createExtensionFinder() {
		return null;
	}

	@Override
	protected PluginStatusProvider createPluginStatusProvider() {
		return null;
	}

	@Override
	protected PluginLoader createPluginLoader() {
		return null;
	}

	@Override
	protected VersionManager createVersionManager() {
		return null;
	}

	@Override
	protected Path createPluginsRoot() {
		return Paths.get(HoardConfiguration.contents().getPluginConfigurationInfo().getPluginsRoot());
	}
}
