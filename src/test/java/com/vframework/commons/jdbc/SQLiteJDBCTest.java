package com.vframework.commons.jdbc;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

import com.vframework.commons.jdbc.SQLiteJDBC;

public class SQLiteJDBCTest {
	
	@Test
	public void getConnectionTest() {
		SQLiteJDBC sqlLiteJdbc = SQLiteJDBC.getInstance();
		Connection connection = sqlLiteJdbc.getConnection();
		assertNotNull(connection);
	}
}
