package media.thehoard.common.configuration;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.SystemUtils;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import java.io.*;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicReference;

//TODO Create default config if it doesn't exist.

public class Configuration {
	private static final String GROUP_NAME =
			SystemUtils.IS_OS_WINDOWS ? "The Hoard Media" : "hoardmedia"; // Used for configuration storage purposes

	/*
	 * SLF4J LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

	/*
	 * Default configuration file name
	 */
	private static final String CONFIGURATION_FILE_NAME = "hoardmediaserver.json";

	private static final String APPLICATION_NAME =
			SystemUtils.IS_OS_WINDOWS ? "The Hoard Media Server" : "hoardmediaserver";

	/*
	 * Default configuration file location This is either
	 * ~/.config/FanartDiscordBot/config.ini on Non-windows operating systems, and
	 * %LOCALAPPDATA%\FanartDiscordBot\config.ini on Windows.
	 */
	public static final String CONFIGURATION_LOCATION = SystemUtils.IS_OS_WINDOWS
			? System
			.getenv("LOCALAPPDATA") + File.separator + GROUP_NAME + File.separator + APPLICATION_NAME + File.separator
			: System.getProperty("user.home") + File.separator + ".config" + File.separator + GROUP_NAME + File.separator + APPLICATION_NAME + File.separator;

	private static final String CONFIGURATION_FILE_PATH = CONFIGURATION_LOCATION + CONFIGURATION_FILE_NAME;

	private static final File CONFIGURATION_PATH = new File(CONFIGURATION_LOCATION);
	private static final File CONFIGURATION_FILE = new File(CONFIGURATION_FILE_PATH);

	private static ConfigurationContents configurationContents = new ConfigurationContents();

	// TODO Test for concurrency issues and determine if Atomic variables are
	// required. For now, this adapter will remain.
	private static Gson gson = new GsonBuilder().setPrettyPrinting()
			.registerTypeAdapter(new TypeToken<AtomicReference<String>>() {
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

			// Save the new config with default values
			saveJsonConfig();
		} else {
			LOGGER.info("Loading configuration file from " + CONFIGURATION_FILE_PATH);
			try {
				configurationContents = gson
						.fromJson(new JsonReader(new FileReader(CONFIGURATION_FILE)), ConfigurationContents.class);
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				LOGGER.error(MarkerFactory.getMarker("FATAL"), "Failed to read config file from disk.", e);
				// Because this is such an essential part, it must exit if it fails to load.
				System.exit(1);
			}
		}
	}

	private static synchronized boolean saveJsonConfig() {
		try (FileWriter configWriter = new FileWriter(CONFIGURATION_FILE)) {
			configWriter.write(gson.toJson(configurationContents));
			LOGGER.info("Succesfully updated configuration file on disk.");
			return true;
		} catch (IOException e) {
			LOGGER.error("Failed to save configuration file to disk.\nIt persists in memory, but will be lost after restart.", e);
			return false;
		}
	}

	public static ConfigurationContents contents() {
		return configurationContents;
	}

	public static org.jooq.Configuration getJooqConfiguration(Connection databaseConnection) {
		DefaultConfiguration conf = new DefaultConfiguration();
		conf.setSQLDialect(configurationContents.getDatabaseInfo().getSqlDialect());
		conf.setConnection(databaseConnection);
		return conf;
	}
}
