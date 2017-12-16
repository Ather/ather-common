package media.thehoard.common.util.gson;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DateDeserializer implements JsonDeserializer<Date> {
	private static final String[] DATE_FORMATS = {"yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'", "yyyy-MM-dd"};
	
	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		for (String currentDateFormat : DATE_FORMATS) {
			try {
				return new SimpleDateFormat(currentDateFormat).parse(json.getAsString());
			} catch (ParseException e) {}
		}
		throw new JsonParseException("Invalid/unparseable date provided: " + json.getAsString());
	}
}
