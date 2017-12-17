package media.thehoard.common.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//Deprecated because this is being replaced by a better exception system.
@Deprecated
public class ExceptionBuilder extends Exception {
	private static final long serialVersionUID = 1L;
	
	//Store the current time in string format
	private String timeString;
	//Store the "exception type", i.e. "com.TripleFrequency.Logger.LoggerException"
	private String exceptionType;
	//Store the logger of the exception, i.e. "Logger"
	private String logger;
	//Store the calling exception stack trace.
	private Exception e;
	//Store the error level of the exception
	private int errorLevel;
	
	/**
	 * Build an exception.
	 * @param e The calling exception stack trace.
	 * @param logger The logger of the exception (source)
	 * @param exceptionType The type of exception
	 * @param errorLevel The importance of the error.
	 */
	public ExceptionBuilder(Exception e, String logger, String exceptionType, int errorLevel) {
		//Format all current times in yyyy-MM-ddTHH:mmZ format.
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		//If the exceptionType is not specified, then parse it from the calling class's canonical name. i.e. com.TripleFrequency.Logger becomes com.TripleFrequency.Logger.LoggerException
		if (exceptionType == null) {
			String callClass = e.getClass().getCanonicalName();
			String[] exceptionSplit = callClass.split(".");
			String exceptionCall = "";
			if (exceptionSplit.length != 0)
				exceptionCall = exceptionSplit[callClass.length() - 1];
			exceptionType = callClass + "." + exceptionCall + "Exception";
		}
		
		//Store all inputed variables.
		this.timeString = df.format(new Date());
		this.exceptionType = exceptionType;
		this.logger = logger;
		this.e = e;
		this.errorLevel = errorLevel;
	}
	
	/**
	 * Build an exception. This assumes the exceptionType will be generated based on hte calling class's canonical name.
	 * @param e The calling exception stack trace.
	 * @param logger The logger of the exception (source)
	 * @param errorLevel The importance of the error.
	 */
	public ExceptionBuilder(Exception e, String logger, int errorLevel) {
		this(e, logger, null, errorLevel);
	}
	
	public String getMessage() {
		return e.getMessage();
	}
	
	/**
	 * @return The time of the exception.
	 */
	public String getTime() {
		return timeString;
	}
	
	/**
	 * @return The logger of the exception.
	 */
	public String getLogger() {
		return logger;
	}
	
	/**
	 * @return The stack trace of the exception.
	 */
	public String getException() {
		//Basically write into a PrintWriter which writes to a string.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	/**
	 * @return The exception type.
	 */
	public String getExceptionType() {
		return exceptionType;
	}
	
	/**
	 * @return The string level of the error.
	 */
	public String getLevel() {
		return getLogLevel(errorLevel);
	}
	
	/**
	 * Get the error level based on an integer input.
	 * @param logLevel The integer level of the error.
	 * @return The string corresponding to the inputed level.
	 */
	private String getLogLevel(int logLevel) {
		/*
		if (logLevel == Log.INFO)
			return "Info";
		else if (logLevel == Log.WARN)
			return "Warn";
		else
			return "Error";
			*/
		return null;
	}
}
