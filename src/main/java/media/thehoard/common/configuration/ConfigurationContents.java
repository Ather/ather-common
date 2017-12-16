/**
 * 
 */
package media.thehoard.common.configuration;

import org.jooq.SQLDialect;

/**
 * @author Michael Haas
 *
 */
class ConfigurationContents {
	// TODO protected LogLevel logLevel

	protected Boolean autoUpdate;

	// protected ReleaseBranch releaseBranch;
	protected Boolean runOnStartup;
	protected Integer portNumber;
	protected String bindAddress;
	protected String publicUrl;

	protected DatabaseConfiguration databaseInfo;

	protected static class DatabaseConfiguration {
		protected SQLDialect sqlDialect;
		protected String databaseIp;
		protected Integer databasePort;
		protected String databaseSchema;
		protected String databaseUser;
		protected String databasePassword;
		protected Integer connectionPoolInitial;
		protected Integer connectionPoolMaxIdle;
		protected Integer connectionPoolMax;
	}

	protected Boolean analyzeOnScan;
	
	protected String ffmpegLocation;
	protected String ffprobeLocation;
	protected String rcloneLocation;
}