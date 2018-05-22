package com.atherapp.common.configuration;

import com.atherapp.common.util.Network;
import com.atherapp.common.util.Network;
import org.jooq.SQLDialect;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

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

	private HashMap<String, DatabaseProfile> databaseProfiles = new HashMap<>();

	public static class DatabaseProfile {
		private String name;
		private Boolean isDefault = false;
		private SQLDialect sqlDialect = SQLDialect.MYSQL;
		private String databaseIp = "127.0.0.1";
		private Integer databasePort = 3306;
		private String databaseSchema = "athermediaserver";
		private String databaseUser = "";
		private String databasePassword = "";
		private Integer connectionPoolInitial = 5;
		private Integer connectionPoolMaxIdle = 10;
		private Integer connectionPoolMax = 20;

		public String getConnectionAlias() { return name; }

		public synchronized DatabaseProfile setName(String name) {
			this.name = name;
			return this;
		}

		public Boolean isDefault() {
			return this.isDefault;
		}

		public synchronized DatabaseProfile setIsDefault(Boolean isDefault) {
			this.isDefault = isDefault;
			return this;
		}

		public SQLDialect getSqlDialect() {
			return sqlDialect;
		}

		public synchronized DatabaseProfile setSqlDialect(SQLDialect sqlDialect) {
			this.sqlDialect = sqlDialect;
			return this;
		}

		public String getDatabaseIp() {
			return databaseIp;
		}

		public synchronized DatabaseProfile setDatabaseIp(String databaseIp) {
			this.databaseIp = databaseIp;
			return this;
		}

		public Integer getDatabasePort() {
			return databasePort;
		}

		public synchronized DatabaseProfile setDatabasePort(Integer databasePort) {
			this.databasePort = databasePort;
			return this;
		}

		public String getDatabaseSchema() {
			return databaseSchema;
		}

		public synchronized DatabaseProfile setDatabaseSchema(String databaseSchema) {
			this.databaseSchema = databaseSchema;
			return this;
		}

		public String getDatabaseUser() {
			return databaseUser;
		}

		public synchronized DatabaseProfile setDatabaseUser(String databaseUser) {
			this.databaseUser = databaseUser;
			return this;
		}

		public String getDatabasePassword() {
			return databasePassword;
		}

		public synchronized DatabaseProfile setDatabasePassword(String databasePassword) {
			this.databasePassword = databasePassword;
			return this;
		}

		public Integer getConnectionPoolInitial() {
			return connectionPoolInitial;
		}

		public synchronized DatabaseProfile setConnectionPoolInitial(Integer connectionPoolInitial) {
			this.connectionPoolInitial = connectionPoolInitial;
			return this;
		}

		public Integer getConnectionPoolMaxIdle() {
			return connectionPoolMaxIdle;
		}

		public synchronized DatabaseProfile setConnectionPoolMaxIdle(Integer connectionPoolMaxIdle) {
			this.connectionPoolMaxIdle = connectionPoolMaxIdle;
			return this;
		}

		public Integer getConnectionPoolMax() {
			return connectionPoolMax;
		}

		public synchronized DatabaseProfile setConnectionPoolMax(Integer connectionPoolMax) {
			this.connectionPoolMax = connectionPoolMax;
			return this;
		}
	}

	private Boolean analyzeOnScan = true;

	private String ffmpegLocation = AtherConfiguration.CONFIGURATION_LOCATION + "ffmpeg" + File.separator + "ffmpeg";
	private String ffprobeLocation = AtherConfiguration.CONFIGURATION_LOCATION + "ffmpeg" + File.separator + "ffprobe";
	private String rcloneLocation = AtherConfiguration.CONFIGURATION_LOCATION + "rclone" + File.separator + "rclone";

	private PluginConfigurationContents pluginConfiguration;

	public static class PluginConfigurationContents {
		private String pluginsRoot = AtherConfiguration.CONFIGURATION_LOCATION + "modules" + File.separator;

		public String getPluginsRoot() {
			return pluginsRoot;
		}

		public synchronized PluginConfigurationContents setRcloneLocation(String pluginsRoot) {
			this.pluginsRoot = pluginsRoot;
			return this;
		}
	}

	public Boolean getAutoUpdate() {
		return autoUpdate;
	}

	public synchronized ConfigurationContents setAutoUpdate(Boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public Boolean getRunOnStartup() {
		return runOnStartup;
	}

	public synchronized ConfigurationContents setRunOnStartup(Boolean runOnStartup) {
		this.runOnStartup = runOnStartup;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public Integer getPortNumber() {
		return portNumber;
	}

	public synchronized ConfigurationContents setPortNumber(Integer portNumber) {
		this.portNumber = portNumber;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public String getBindAddress() {
		return bindAddress;
	}

	public synchronized ConfigurationContents setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public String getPublicUrl() {
		return publicUrl;
	}

	public synchronized ConfigurationContents setPublicUrl(String publicUrl) {
		this.publicUrl = publicUrl;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public HashMap<String, DatabaseProfile> getDatabaseProfiles() { return this.databaseProfiles; }

	public DatabaseProfile getDatabaseProfile(String profileName) {
		return databaseProfiles.get(profileName);
	}

	public DatabaseProfile getDatabaseProfile() {
		Optional<DatabaseProfile> found = AtherConfiguration.contents().getDatabaseProfiles()
				.values().stream()
				.filter(ConfigurationContents.DatabaseProfile::isDefault).findFirst();
		if (found.isPresent())
			return getDatabaseProfile(found.get().getConnectionAlias());

		//TODO
		throw new RuntimeException("Default database profile not found.");
	}

	public synchronized ConfigurationContents putDatabaseProfile(DatabaseProfile databaseProfile) {
		this.databaseProfiles.put(databaseProfile.getConnectionAlias(), databaseProfile);
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public Boolean getAnalyzeOnScan() {
		return analyzeOnScan;
	}

	public synchronized ConfigurationContents setAnalyzeOnScan(Boolean analyzeOnScan) {
		this.analyzeOnScan = analyzeOnScan;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public String getFfmpegLocation() {
		return ffmpegLocation;
	}

	public synchronized ConfigurationContents setFfmpegLocation(String ffmpegLocation) {
		this.ffmpegLocation = ffmpegLocation;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public String getFfprobeLocation() {
		return ffprobeLocation;
	}

	public synchronized ConfigurationContents setFfprobeLocation(String ffprobeLocation) {
		this.ffprobeLocation = ffprobeLocation;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public String getRcloneLocation() {
		return rcloneLocation;
	}

	public synchronized ConfigurationContents setRcloneLocation(String rcloneLocation) {
		this.rcloneLocation = rcloneLocation;
		AtherConfiguration.saveJsonConfig();
		return this;
	}

	public PluginConfigurationContents getPluginConfigurationInfo() {
		return pluginConfiguration;
	}

	public synchronized ConfigurationContents setPluginConfigurationInfo(PluginConfigurationContents pluginInfo) {
		this.pluginConfiguration = pluginInfo;
		AtherConfiguration.saveJsonConfig();
		return this;
	}
}