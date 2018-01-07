package media.thehoard.common.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import media.thehoard.common.configuration.ConfigurationContents;
import media.thehoard.common.configuration.HoardConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;

public class DatabaseConnector {
	private static HashMap<String, BasicDataSource> connectionPools;

	/**
	 * Statically initialize the Database connection pool.
	 */
	static {
		connectionPools = new HashMap<>();

		generatePools();
	}

	/**
	 * A private constructor to force the DatabaseConnector class to be entirely
	 * static.
	 */
	private DatabaseConnector() {
	}

	private synchronized static void generatePools() {
		HoardConfiguration.contents().getDatabaseProfiles().forEach((K, V) -> {
			BasicDataSource source = new BasicDataSource();
			//TODO Dynamically get driver class and URLs
			source.setDriverClassName("com.mysql.jdbc.Driver");
			source.setUrl(String.format("jdbc:mysql://%s:%d/%s?verifyServerCertificate=false&useSSL=true", V
					                            .getDatabaseIp(), V.getDatabasePort(),
			                            V.getDatabaseSchema()));
			source.setUsername(V.getDatabaseUser());
			source.setPassword(V.getDatabasePassword());
			source.setInitialSize(V.getConnectionPoolInitial());
			source.setMaxTotal(V.getConnectionPoolMax());
			source.setMaxIdle(V.getConnectionPoolMaxIdle());

			source.addConnectionProperty("SQL_DIALECT", V.getSqlDialect().toString());

			connectionPools.put(K, source);
		});
	}

	public synchronized static void clearPools() throws SQLException {
		for (Map.Entry<String, BasicDataSource> pool : connectionPools.entrySet())
			pool.getValue().close();

		connectionPools.clear();
	}

	public synchronized static void reloadPools() throws SQLException {
		clearPools();

		generatePools();
	}

	/**
	 * Get a new connection to the MySQL server from the pool.
	 *
	 * @return A database connection object.
	 */
	public static synchronized Connection getConnection(String connectionName) throws SQLException {
		try {
			return connectionPools.get(connectionName).getConnection();
		} catch (SQLException e) {
			// TODO clean this up and support console-only logging.
			System.out.println("Failed to generate connection to Database: " + connectionName);
			e.printStackTrace();
			throw e;
		}
	}

	public static Connection getConnection() throws SQLException {
		ConfigurationContents.DatabaseProfile defaultProfile = HoardConfiguration.contents().getDatabaseProfile();
		if (defaultProfile != null)
			return getConnection(defaultProfile.getConnectionAlias());

		throw new SQLException("No default database profile found.");
	}

	/**
	 * Get the connection pool's object.
	 *
	 * @return The connection pool as an object.
	 */
	protected static synchronized HashMap<String, BasicDataSource> getConnectionPools() {
		return connectionPools;
	}

	// TODO implement auto-creation of schema if non-existent.
}
