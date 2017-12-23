package media.thehoard.common.plugins;

import media.thehoard.common.util.gson.GsonUtil;
import org.apache.commons.io.IOUtils;
import org.pf4j.PluginDependency;
import org.pf4j.PluginDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class HoardPluginDescriptor extends PluginDescriptor {
	private DescriptorContent content;

	private class DescriptorContent {
		String name;

		String version;

		String description;

		//TODO Consider some type of load-stage config. Meaning to load the plugin at startup, or later.

		String author;

		List<String> authors;

		String website;

		String main;

		Boolean database;

		List<PluginDependency> depend;

		//TODO Possibly restrict to not allow "media.thehoard"
		String prefix;

		//TODO Define API endpoints

		//TODO Define Permissions

		String license;
	}

	public HoardPluginDescriptor(InputStream inputStream) throws IOException {
		this.content = GsonUtil.getGson().fromJson(IOUtils.toString(inputStream, "UTF-8"), DescriptorContent.class);
	}

	@Override
	public String getPluginId() {
		return content.name;
	}

	@Override
	public String getPluginDescription() {
		return content.description;
	}

	@Override
	public String getPluginClass() {
		return content.main;
	}

	@Override
	public String getVersion() {
		return content.version;
	}

	//TODO
	@Override
	public String getRequires() {
		return "*";
	}

	//TODO
	@Override
	public String getProvider() {
		return "";
	}

	@Override
	public String getLicense() {
		return content.license;
	}

	@Override
	public List<PluginDependency> getDependencies() {
		return content.depend;
	}

	@Override
	public String toString() {
		return "PluginDescriptor [pluginId=" + content.name + ", pluginClass="
				+ content.main + ", version=" + content.version + ", provider="
				+ "" + ", dependencies=" + content.depend + ", description="
				+ content.description + ", requires=" + "*" + ", license="
				+ content.license + "]";
	}
}
