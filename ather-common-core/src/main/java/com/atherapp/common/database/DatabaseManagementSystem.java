package com.atherapp.common.database;

import org.jooq.SQLDialect;

public enum DatabaseManagementSystem {
	//AmazonRedshift,
	//Azure,
	//DB2_JTOpen,
	//DB2_LUW,
	//Derby_Embedded,
	//Derby_Remote,
	//Exasol,
	/*H2(SQLDialect.H2, "org.h2.Driver", new ConnectionUrl[]{
			new ConnectionUrl(ConnectionType.Remote, "jdbc:h2:tcp://%1$s:%2d/%3$s"),
			new ConnectionUrl(ConnectionType.InMemory, "jdbc:h2:mem:%3$s"),
			new ConnectionUrl(ConnectionType.InMemory, "jdbc:h2::%3$s")}),
	HSQLDB_Local(SQLDialect.HSQLDB, "", ""),
	HSQLDB_Remote(SQLDialect.HSQLDB, "", ""),
	MariaDb(SQLDialect.MARIADB, "", ""),
	MySQL(SQLDialect.MYSQL, "", ""),
	//Oracle,
	PostreSQL(SQLDialect.POSTGRES, "", ""),
	//SQLServer_jTds,
	//SQLServer_Microsoft,
	Sqlite(SQLDialect.SQLITE, "", ""),
	//Sybase_jTds,
	//Sybase_Native, */
	;

	public static class ConnectionUrl {
		public ConnectionType connectionType;
		public String formatUrl;

		public ConnectionUrl(ConnectionType connectionType, String formatUrl) {
			this.connectionType = connectionType;
			this.formatUrl = formatUrl;
		}
	}

	public enum ConnectionType {
		Remote,
		InMemory,
		Embedded
	}

	DatabaseManagementSystem(SQLDialect dialect, String driverClass, ConnectionUrl... urls) {
	}
}
