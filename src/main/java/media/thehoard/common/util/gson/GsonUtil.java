/**
 * 
 */
package media.thehoard.common.util.gson;

import java.sql.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Michael Haas
 *
 */
public class GsonUtil {
	private static Gson gson = null;
	private static Gson prettyGson = null;
	
	public static Gson getPrettyGson() {
		if (prettyGson != null)
			return prettyGson;
		
		GsonBuilder builder = new GsonBuilder();
		
		builder.registerTypeAdapter(Date.class, new DateDeserializer());
		//builder.registerTypeAdapter(Codec.class, new CodecDeserializer());
		//builder.registerTypeAdapter(PixelFormat.class, new PixelFormatDeserializer());
		
		builder.setPrettyPrinting();
		prettyGson = builder.create();
		return prettyGson;
	}
	
	public static Gson getGson() {
		if (gson != null)
			return gson;
		
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new DateDeserializer());
		gson = builder.create();
		return gson;
	}
}
