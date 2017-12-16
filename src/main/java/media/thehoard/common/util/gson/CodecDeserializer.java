/**
 * 
 */
package media.thehoard.common.util.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import media.thehoard.thirdparty.application.ffmpeg.codecs.Codec;

/**
 * @author Michael Haas
 *
 */
public class CodecDeserializer implements JsonDeserializer<Codec> {
	@Override
	public Codec deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return Codec.getSupportedCodecs().get(json.getAsString());
	}
}
