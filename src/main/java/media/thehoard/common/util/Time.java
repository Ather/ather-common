/**
 * 
 */
package media.thehoard.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Michael Haas
 *
 */
@Deprecated
public class Time {
	private static final DateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	private static final DateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static boolean isRootServer = true;

	static {
		ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		STANDARD_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public static String getISODate() {
		return ISO_DATE_FORMAT.format(new Date());
	}

	public static Date getISODate(String iso) {
		try {
			return ISO_DATE_FORMAT.parse(iso);
		} catch (ParseException e) {
			return new Date();
		}
	}

	public static String getDateTime() {
		return STANDARD_DATE_FORMAT.format(new Date());
	}

	public static Date getDateTime(String datetime) {
		try {
			return STANDARD_DATE_FORMAT.parse(datetime);
		} catch (ParseException e) {
			return new Date();
		}
	}
}
