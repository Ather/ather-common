package com.atherapp.common.database

import java.sql.Driver

object JDBCLoader {

}

class JDBCShim(d: Driver) : Driver by d