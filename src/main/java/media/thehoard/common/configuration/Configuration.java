package media.thehoard.common.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.SystemUtils;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import media.thehoard.common.configuration.ConfigurationContents.DatabaseConfiguration;
import media.thehoard.hoardmediaserver.common.util.Network;

//TODO Create default config if it doesn't exist.

public class Configuration {
	private static final String GROUP_NAME = SystemUtils.IS_OS_WINDOWS ? "The Hoard Media" : "hoardmedia"; // Used for configuration storage purposes

	/*
	 * SLF4J LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

	/*
	 * Default configuration file name
	 */
	private static final String CONFIGURATION_FILE_NAME = "hoardmediaserver.json";

	private static final String APPLICATION_NAME = SystemUtils.IS_OS_WINDOWS ? "The Hoard Media Server" : "hoardmediaserver";

	/*
	 * Default configuration file location This is either
	 * ~/.config/FanartDiscordBot/config.ini on Non-windows operating systems, and
	 * %LOCALAPPDATA%\FanartDiscordBot\config.ini on Windows.
	 */
	public static final String CONFIGURATION_LOCATION = SystemUtils.IS_OS_WINDOWS
			? System.getenv("LOCALAPPDATA") + File.separator + GROUP_NAME + File.separator + APPLICATION_NAME + File.separator
			: System.getProperty("user.home") + File.separator + ".config" + File.separator + GROUP_NAME + File.separator + APPLICATION_NAME + File.separator;

	private static final String CONFIGURATION_FILE_PATH = CONFIGURATION_LOCATION + CONFIGURATION_FILE_NAME;

	private static final File CONFIGURATION_PATH = new File(CONFIGURATION_LOCATION);
	private static final File CONFIGURATION_FILE = new File(CONFIGURATION_FILE_PATH);

	private static ConfigurationContents configurationContents = new ConfigurationContents();

	// TODO Test for concurrency issues and determine if Atomic variables are
	// required. For now, this adapter will remain.
	private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(new TypeToken<AtomicReference<String>>() {
	}.getType(), new AtomicReferenceStringAdapter()).create();

	public static class AtomicReferenceStringAdapter extends TypeAdapter<AtomicReference<String>> {
		@Override
		public void write(JsonWriter out, AtomicReference<String> value) throws IOException {
			out.value(value.get());
		}

		@Override
		public AtomicReference<String> read(JsonReader in) throws IOException {
			return new AtomicReference<>(in.nextString());
		}
	}

	static {

		if (!CONFIGURATION_FILE.exists()) {
			LOGGER.warn("No configuration file found, generating a default at " + CONFIGURATION_FILE_PATH);

			CONFIGURATION_PATH.mkdirs();
			try {
				CONFIGURATION_FILE.createNewFile();
			} catch (IOException e) {
				LOGGER.error(MarkerFactory.getMarker("FATAL"), "Failed to create configuration file.", e);
				System.exit(1);
			}

			// Initialize default values
			configurationContents.autoUpdate = true;
			configurationContents.runOnStartup = false;

			configurationContents.portNumber = 30121;
			configurationContents.bindAddress = "0.0.0.0";

			configurationContents.publicUrl = Network.getExternalAddress();

			// Default database values
			configurationContents.databaseInfo = new DatabaseConfiguration();
			configurationContents.databaseInfo.sqlDialect = SQLDialect.MYSQL;
			configurationContents.databaseInfo.databaseIp = "";
			configurationContents.databaseInfo.databasePassword = "";
			configurationContents.databaseInfo.databasePort = 3306;
			configurationContents.databaseInfo.databaseSchema = "hoardmediaserver";
			configurationContents.databaseInfo.databaseUser = "";
			configurationContents.databaseInfo.connectionPoolInitial = 5;
			configurationContents.databaseInfo.connectionPoolMaxIdle = 10;
			configurationContents.databaseInfo.connectionPoolMax = 20;

			configurationContents.ffmpegLocation = CONFIGURATION_LOCATION + "ffmpeg" + File.separator + "ffmpeg";
			configurationContents.ffprobeLocation = CONFIGURATION_LOCATION + "ffmpeg" + File.separator + "ffprobe";

			configurationContents.rcloneLocation = CONFIGURATION_LOCATION + "rclone" + File.separator + "rclone";

			configurationContents.analyzeOnScan = true;

			// Save the new config with default values
			saveJsonConfig();
		} else {
			LOGGER.info("Loading configuration file from " + CONFIGURATION_FILE_PATH);
			try {
				configurationContents = gson.fromJson(new JsonReader(new FileReader(CONFIGURATION_FILE)), ConfigurationContents.class);
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				LOGGER.error(MarkerFactory.getMarker("FATAL"), "Failed to read config file from disk.", e);
				// Because this is such an essential part, it must exit if it fails to load.
				System.exit(1);
			}
		}
	}

	private static synchronized boolean saveJsonConfig() {
		try (FileWriter configWriter = new FileWriter(CONFIGURATION_FILE);) {
			configWriter.write(gson.toJson(configurationContents));
			LOGGER.info("Succesfully updated configuration file on disk.");
			return true;
		} catch (IOException e) {
			LOGGER.error("Failed to save configuration file to disk.\nIt persists in memory, but will be lost after restart.", e);
			return false;
		}
	}

	/**
	 * @return the autoUpdate
	 */
	public static Boolean getAutoUpdate() {
		return configurationContents.autoUpdate;
	}

	/**
	 * @param autoUpdate
	 *            the autoUpdate to set
	 */
	public static synchronized boolean setAutoUpdate(Boolean autoUpdate) {
		configurationContents.autoUpdate = autoUpdate;
		return saveJsonConfig();
	}

	/**
	 * @return the runOnStartup
	 */
	public static Boolean getRunOnStartup() {
		return configurationContents.runOnStartup;
	}

	/**
	 * @param runOnStartup
	 *            the runOnStartup to set
	 */
	public static synchronized boolean setRunOnStartup(Boolean runOnStartup) {
		configurationContents.runOnStartup = runOnStartup;
		return saveJsonConfig();
	}

	/**
	 * @return the portNumber
	 */
	public static Integer getPortNumber() {
		return configurationContents.portNumber;
	}

	/**
	 * @param portNumber
	 *            the portNumber to set
	 */
	public static synchronized boolean setPortNumber(Integer portNumber) {
		configurationContents.portNumber = portNumber;
		return saveJsonConfig();
	}

	/**
	 * @return the bindAddress
	 */
	public static String getBindAddress() {
		return configurationContents.bindAddress;
	}

	/**
	 * @param bindAddress
	 *            the bindAddress to set
	 */
	public static synchronized boolean setBindAddress(String bindAddress) {
		configurationContents.bindAddress = bindAddress;
		return saveJsonConfig();
	}

	/**
	 * @return the publicUrl
	 */
	public static String getPublicUrl() {
		return configurationContents.publicUrl;
	}

	/**
	 * @param publicUrl
	 *            the publicUrl to set
	 */
	public static synchronized boolean setPublicUrl(String publicUrl) {
		configurationContents.publicUrl = publicUrl;
		return saveJsonConfig();
	}

	public static SQLDialect getSqlDialect() {
		return configurationContents.databaseInfo.sqlDialect;
	}

	public static org.jooq.Configuration getJooqConfiguration(Connection databaseConnection) {
		DefaultConfiguration conf = new DefaultConfiguration();
		conf.setSQLDialect(getSqlDialect());
		conf.setConnection(databaseConnection);
		return conf;
	}

	public static synchronized boolean setSqlDialect(SQLDialect sqlDialect) {
		configurationContents.databaseInfo.sqlDialect = sqlDialect;
		return saveJsonConfig();
	}

	/**
	 * @return the databaseIp
	 */
	public static String getDatabaseIp() {
		return configurationContents.databaseInfo.databaseIp;
	}

	/**
	 * @param databaseIp
	 *            the databaseIp to set
	 */
	public static synchronized boolean setDatabaseIp(String databaseIp) {
		configurationContents.databaseInfo.databaseIp = databaseIp;
		return saveJsonConfig();
	}

	/**
	 * @return the databasePort
	 */
	public static Integer getDatabasePort() {
		return configurationContents.databaseInfo.databasePort;
	}

	/**
	 * @param databasePort
	 *            the databasePort to set
	 */
	public static synchronized boolean setDatabasePort(Integer databasePort) {
		configurationContents.databaseInfo.databasePort = databasePort;
		return saveJsonConfig();
	}

	/**
	 * @return the databaseSchema
	 */
	public static String getDatabaseSchema() {
		return configurationContents.databaseInfo.databaseSchema;
	}

	/**
	 * @param databaseSchema
	 *            the databaseSchema to set
	 */
	public static synchronized boolean setDatabaseSchema(String databaseSchema) {
		configurationContents.databaseInfo.databaseSchema = databaseSchema;
		return saveJsonConfig();
	}

	/**
	 * @return the databaseUser
	 */
	public static String getDatabaseUser() {
		return configurationContents.databaseInfo.databaseUser;
	}

	/**
	 * @param databaseUser
	 *            the databaseUser to set
	 */
	public static synchronized boolean setDatabaseUser(String databaseUser) {
		configurationContents.databaseInfo.databaseUser = databaseUser;
		return saveJsonConfig();
	}

	/**
	 * @return the databasePassword
	 */
	public static String getDatabasePassword() {
		return configurationContents.databaseInfo.databasePassword;
	}

	/**
	 * @param databasePassword
	 *            the databasePassword to set
	 */
	public static synchronized boolean setDatabasePassword(String databasePassword) {
		configurationContents.databaseInfo.databasePassword = databasePassword;
		return saveJsonConfig();
	}

	/**
	 * @return the connectionPoolInitial
	 */
	public static Integer getConnectionPoolInitial() {
		return configurationContents.databaseInfo.connectionPoolInitial;
	}

	/**
	 * @param connectionPoolInitial
	 *            the connectionPoolInitial to set
	 */
	public static synchronized boolean setConnectionPoolInitial(Integer connectionPoolInitial) {
		configurationContents.databaseInfo.connectionPoolInitial = connectionPoolInitial;
		return saveJsonConfig();
	}

	/**
	 * @return the connectionPoolMaxIdle
	 */
	public static Integer getConnectionPoolMaxIdle() {
		return configurationContents.databaseInfo.connectionPoolMaxIdle;
	}

	/**
	 * @param connectionPoolMaxIdle
	 *            the connectionPoolMaxIdle to set
	 */
	public static synchronized boolean setConnectionPoolMaxIdle(Integer connectionPoolMaxIdle) {
		configurationContents.databaseInfo.connectionPoolMaxIdle = connectionPoolMaxIdle;
		return saveJsonConfig();
	}

	/**
	 * @return the connectionPoolMax
	 */
	public static Integer getConnectionPoolMax() {
		return configurationContents.databaseInfo.connectionPoolMax;
	}

	/**
	 * @param connectionPoolMax
	 *            the connectionPoolMax to set
	 */
	public static synchronized boolean setConnectionPoolMax(Integer connectionPoolMax) {
		configurationContents.databaseInfo.connectionPoolMax = connectionPoolMax;
		return saveJsonConfig();
	}

	/**
	 * @return the analyzeOnScan
	 */
	public static Boolean getAnalyzeOnScan() {
		return configurationContents.analyzeOnScan;
	}

	/**
	 * @param analyzeOnScan
	 *            the analyzeOnScan to set
	 */
	public static synchronized boolean setAnalyzeOnScan(Boolean analyzeOnScan) {
		configurationContents.analyzeOnScan = analyzeOnScan;
		return saveJsonConfig();
	}

	public static String getFFmpegLocation() {
		return configurationContents.ffmpegLocation;
	}

	public static synchronized boolean setFFmpegLocation(String newLocation) {
		configurationContents.ffmpegLocation = newLocation;
		return saveJsonConfig();
	}

	public static String getFFprobeLocation() {
		return configurationContents.ffprobeLocation;
	}

	public static synchronized boolean setFFprobeLocation(String newLocation) {
		configurationContents.ffprobeLocation = newLocation;
		return saveJsonConfig();
	}

	public static String getRcloneLocation() {
		return configurationContents.rcloneLocation;
	}

	public static synchronized boolean setRcloneLocation(String newLocation) {
		configurationContents.rcloneLocation = newLocation;
		return saveJsonConfig();
	}
}
