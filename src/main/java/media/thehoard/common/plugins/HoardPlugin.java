package media.thehoard.common.plugins;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public abstract class HoardPlugin extends Plugin {
	public HoardPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}
}
