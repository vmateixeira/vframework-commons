package com.vframework.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLiteJDBC {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteJDBC.class);
	
	private Connection connection;
	private boolean memory;
	
	private SQLiteJDBC() {
		memory = Boolean.parseBoolean(System.getProperties().getProperty("jdbc.sqlite.Memory"));
	}
	
	public static SQLiteJDBC getInstance() {
		return SQLiteJDBCHolder.INSTANCE;
	}
	
	public Connection getConnection() {
		if(null == connection) {
			if(memory)
				return getMemoryConnection();
			return getFileConnection();
		}
		return connection;
	}
	
	private Connection getFileConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + System.getProperties().getProperty("jdbc.sqlite.File-Path"));
		} catch (ClassNotFoundException classNotFoundException) {
			LOGGER.error("Error finding JDBC driver: {}", classNotFoundException.getMessage());
		} catch (SQLException sqlException) {
			LOGGER.error("Error retrieving DB connection (file): {}", sqlException.getMessage());
		}
		return connection;
	}
	
	private Connection getMemoryConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite::memory:");
		} catch (ClassNotFoundException classNotFoundException) {
			LOGGER.error("Error finding JDBC driver: {}", classNotFoundException.getMessage());
		} catch (SQLException sqlException) {
			LOGGER.error("Error retrieving DB connection (memory): {}", sqlException.getMessage());
		}
		return connection;
	}
	
	public void memoryBackup() {
		try {
			Statement statement = getConnection().createStatement();
			
			if(memory) {
				statement.executeUpdate("backup to " + System.getProperties().getProperty("jdbc.sqlite.Memory-Backup-Path"));
			}
			statement.close();
		} catch (SQLException sqlException) {
			LOGGER.error("Error backing up DB: {}", sqlException.getMessage());
		}
	}
	
	public void memoryRestore() {
		try {
			Statement statement = getConnection().createStatement();
			
			if(memory) {	
				statement.executeUpdate("restore from " + System.getProperties().getProperty("jdbc.sqlite.Memory-Backup-Path"));
			}
			statement.close();
		} catch (SQLException sqlException) {
			LOGGER.error("Error restoring DB: {}", sqlException.getMessage());
		}
	}
	
	private static class SQLiteJDBCHolder {
		private static final SQLiteJDBC INSTANCE = new SQLiteJDBC();
	}
}
