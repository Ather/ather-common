package media.thehoard.common.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import media.thehoard.common.configuration.Configuration;

public class DatabaseConnector {
	private static BasicDataSource connectionPool;

	/**
	 * Statically initialize the Database connection pool.
	 */
	static {
		connectionPool = new BasicDataSource();
		// TODO Allow dynamic choices here
		connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
		connectionPool.setUrl(String.format("jdbc:mysql://%s:%d/%s?verifyServerCertificate=false&useSSL=true", Configuration.contents().getDatabaseInfo().getDatabaseIp(), Configuration.contents().getDatabaseInfo().getDatabasePort(),
				Configuration.contents().getDatabaseInfo().getDatabaseSchema()));
		connectionPool.setUsername(Configuration.contents().getDatabaseInfo().getDatabaseUser());
		connectionPool.setPassword(Configuration.contents().getDatabaseInfo().getDatabasePassword());
		connectionPool.setInitialSize(Configuration.contents().getDatabaseInfo().getConnectionPoolInitial());
		connectionPool.setMaxTotal(Configuration.contents().getDatabaseInfo().getConnectionPoolMax());
		connectionPool.setMaxIdle(Configuration.contents().getDatabaseInfo().getConnectionPoolMaxIdle());
	}

	/**
	 * A private constructor to force the DatabaseConnector class to be entirely
	 * static.
	 */
	private DatabaseConnector() {
	}

	/**
	 * Get a new connection to the MySQL server from the pool.
	 * 
	 * @return A database connection object.
	 */
	public static synchronized Connection getConnection() {
		try {
			return connectionPool.getConnection();
		} catch (SQLException e) {
			// TODO clean this up and support console-only logging.
			System.out.println("Failed to generate connection to MySQL Database.");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Get the connection pool's object.
	 * 
	 * @return The connection pool as an object.
	 */
	protected static synchronized BasicDataSource getConnectionPool() {
		return connectionPool;
	}

	// TODO implement auto-creation of schema if non-existent.
}