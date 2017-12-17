package media.thehoard.common.configuration;

import media.thehoard.common.util.Network;
import org.jooq.SQLDialect;

import java.io.File;

/**
 * @author Michael Haas
 *
 */
public class ConfigurationContents {
	// TODO protected LogLevel logLevel

	private Boolean autoUpdate = true;

	// protected ReleaseBranch releaseBranch;
	private Boolean runOnStartup = false;
	private Integer portNumber = 30121;
	private String bindAddress = "0.0.0.0";
	private String publicUrl = Network.getExternalAddress();

	private DatabaseConfigurationContents databaseInfo;

	public static class DatabaseConfigurationContents {
		private SQLDialect sqlDialect = SQLDialect.MYSQL;
		private String databaseIp = "";
		private Integer databasePort = 3306;
		private String databaseSchema = "hoardmediaserver";
		private String databaseUser = "";
		private String databasePassword = "";
		private Integer connectionPoolInitial = 5;
		private Integer connectionPoolMaxIdle = 10;
		private Integer connectionPoolMax = 20;

		public SQLDialect getSqlDialect() {
			return sqlDialect;
		}

		public synchronized DatabaseConfigurationContents setSqlDialect(SQLDialect sqlDialect) {
			this.sqlDialect = sqlDialect;
			return this;
		}

		public String getDatabaseIp() {
			return databaseIp;
		}

		public synchronized DatabaseConfigurationContents setDatabaseIp(String databaseIp) {
			this.databaseIp = databaseIp;
			return this;
		}

		public Integer getDatabasePort() {
			return databasePort;
		}

		public synchronized DatabaseConfigurationContents setDatabasePort(Integer databasePort) {
			this.databasePort = databasePort;
			return this;
		}

		public String getDatabaseSchema() {
			return databaseSchema;
		}

		public synchronized DatabaseConfigurationContents setDatabaseSchema(String databaseSchema) {
			this.databaseSchema = databaseSchema;
			return this;
		}

		public String getDatabaseUser() {
			return databaseUser;
		}

		public synchronized DatabaseConfigurationContents setDatabaseUser(String databaseUser) {
			this.databaseUser = databaseUser;
			return this;
		}

		public String getDatabasePassword() {
			return databasePassword;
		}

		public synchronized DatabaseConfigurationContents setDatabasePassword(String databasePassword) {
			this.databasePassword = databasePassword;
			return this;
		}

		public Integer getConnectionPoolInitial() {
			return connectionPoolInitial;
		}

		public synchronized DatabaseConfigurationContents setConnectionPoolInitial(Integer connectionPoolInitial) {
			this.connectionPoolInitial = connectionPoolInitial;
			return this;
		}

		public Integer getConnectionPoolMaxIdle() {
			return connectionPoolMaxIdle;
		}

		public synchronized DatabaseConfigurationContents setConnectionPoolMaxIdle(Integer connectionPoolMaxIdle) {
			this.connectionPoolMaxIdle = connectionPoolMaxIdle;
			return this;
		}

		public Integer getConnectionPoolMax() {
			return connectionPoolMax;
		}

		public synchronized DatabaseConfigurationContents setConnectionPoolMax(Integer connectionPoolMax) {
			this.connectionPoolMax = connectionPoolMax;
			return this;
		}
	}

	private Boolean analyzeOnScan = true;

	private String ffmpegLocation = Configuration.CONFIGURATION_LOCATION + "ffmpeg" + File.separator + "ffmpeg";
	private String ffprobeLocation = Configuration.CONFIGURATION_LOCATION + "ffmpeg" + File.separator + "ffprobe";
	private String rcloneLocation = Configuration.CONFIGURATION_LOCATION + "rclone" + File.separator + "rclone";

	public Boolean getAutoUpdate() {
		return autoUpdate;
	}

	public synchronized ConfigurationContents setAutoUpdate(Boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
		return this;
	}

	public Boolean getRunOnStartup() {
		return runOnStartup;
	}

	public synchronized ConfigurationContents setRunOnStartup(Boolean runOnStartup) {
		this.runOnStartup = runOnStartup;
		return this;
	}

	public Integer getPortNumber() {
		return portNumber;
	}

	public synchronized ConfigurationContents setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
		return this;
	}

	public String getBindAddress() {
		return bindAddress;
	}

	public synchronized ConfigurationContents setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
		return this;
	}

	public String getPublicUrl() {
		return publicUrl;
	}

	public synchronized ConfigurationContents setPublicUrl(String publicUrl) {
		this.publicUrl = publicUrl;
		return this;
	}

	public DatabaseConfigurationContents getDatabaseInfo() {
		return databaseInfo;
	}

	public synchronized ConfigurationContents setDatabaseInfo(DatabaseConfigurationContents databaseInfo) {
		this.databaseInfo = databaseInfo;
		return this;
	}

	public Boolean getAnalyzeOnScan() {
		return analyzeOnScan;
	}

	public synchronized ConfigurationContents setAnalyzeOnScan(Boolean analyzeOnScan) {
		this.analyzeOnScan = analyzeOnScan;
		return this;
	}

	public String getFfmpegLocation() {
		return ffmpegLocation;
	}

	public synchronized ConfigurationContents setFfmpegLocation(String ffmpegLocation) {
		this.ffmpegLocation = ffmpegLocation;
		return this;
	}

	public String getFfprobeLocation() {
		return ffprobeLocation;
	}

	public synchronized ConfigurationContents setFfprobeLocation(String ffprobeLocation) {
		this.ffprobeLocation = ffprobeLocation;
		return this;
	}

	public String getRcloneLocation() {
		return rcloneLocation;
	}

	public synchronized ConfigurationContents setRcloneLocation(String rcloneLocation) {
		this.rcloneLocation = rcloneLocation;
		return this;
	}
}