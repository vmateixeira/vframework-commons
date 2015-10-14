package com.vframework.jdbc;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

public class SQLiteJDBCTest {
	
	@Test
	public void getConnectionTest() {
		SQLiteJDBC sqlLiteJdbc = SQLiteJDBC.getInstance();
		Connection connection = sqlLiteJdbc.getConnection();
		assertNotNull(connection);
	}
}
