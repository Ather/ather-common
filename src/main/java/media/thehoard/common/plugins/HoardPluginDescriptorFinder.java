package media.thehoard.common.plugins;

import org.pf4j.PluginDescriptor;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginException;
import org.pf4j.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class HoardPluginDescriptorFinder implements PluginDescriptorFinder {
	private static final String DESCRIPTOR_NAME = "plugin.json";

	@Override
	public boolean isApplicable(Path pluginPath) {
		return Files.exists(pluginPath) && (Files.isDirectory(pluginPath) || FileUtils.isJarFile(pluginPath));
	}

	@Override
	public HoardPluginDescriptor find(Path pluginPath) throws PluginException {
		if (FileUtils.isJarFile(pluginPath)) {
			try {
				JarFile jar = new JarFile(pluginPath.toFile());
				JarEntry entry = jar.getJarEntry(DESCRIPTOR_NAME);

				if (entry == null)
					throw new PluginException("Failed to find manifest: " + DESCRIPTOR_NAME);

				return new HoardPluginDescriptor(jar.getInputStream(entry));
			} catch (IOException e) {
				throw new PluginException(e);
			}
		} else {
			Path descriptorPath = Files.isDirectory(pluginPath) ? FileUtils.findFile(pluginPath, DESCRIPTOR_NAME) : null;

			if (descriptorPath == null)
				throw new PluginException("Failed to find manifest: " + DESCRIPTOR_NAME);

			try {
				return new HoardPluginDescriptor(Files.newInputStream(descriptorPath));
			} catch (IOException e) {
				throw new PluginException(e);
			}
		}
	}
}
