/**
 * 
 */
package media.thehoard.common.util.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import media.thehoard.thirdparty.application.ffmpeg.pixelformats.PixelFormat;

/**
 * @author Michael Haas
 *
 */
public class PixelFormatDeserializer implements JsonDeserializer<PixelFormat> {
	@Override
	public PixelFormat deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return PixelFormat.getSupportedPixelFormats().get(json.getAsString());
	}
}
