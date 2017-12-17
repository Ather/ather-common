/**
 * 
 */
package media.thehoard.common.json;

import media.thehoard.common.util.gson.GsonUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Michael Haas
 *
 */
public class GenericJson {
	public String toJson(boolean doPrettyPrint) {
		return doPrettyPrint? GsonUtil.getPrettyGson().toJson(this) : GsonUtil.getGson().toJson(this);
	}

	public String toJson() {
		return toJson(false);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
		/*
		StringBuilder outputBuilder = new StringBuilder();

		for (Field f : this.getClass().getDeclaredFields()) {
			Object fieldVal;
			try {
				fieldVal = f.get(new Object());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}
			if (fieldVal != null)
				outputBuilder.append(f.getName().replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " "))
						.append(": ").append(fieldVal).append("\n");
		}

		return outputBuilder.toString();*/
	}
}
