package com.vframework.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);
	private static final String PROPERTIES_FILE = "src\\main\\resources\\vframework.properties";
	private static final String PROPERTIES_FILE_TEST = "src\\test\\resources\\vframework-test.properties";
	
	public static void load() {
		Properties properties = System.getProperties();
		try {
			properties.load(new FileReader(new File(PROPERTIES_FILE)));
		} catch (IOException ioException) {
			LOGGER.error("Error loading properties from file: ", ioException);
			throw new RuntimeException(ioException);
		}
	}
	
	public static void loadTest() {
		Properties properties = System.getProperties();
		try {
			properties.load(new FileReader(new File(PROPERTIES_FILE_TEST)));
		} catch (IOException ioException) {
			LOGGER.error("Error loading properties from file: ", ioException);
			throw new RuntimeException(ioException);
		}
	}
	
	public static void print() {
		Properties properties = System.getProperties();
		Enumeration<Object> keys = properties.keys();
		
		String key = null, value = null;
		
		while(keys.hasMoreElements()) {
			key = (String) keys.nextElement();
			value = (String) properties.get(key);
			LOGGER.debug(key + ": " + value);
		}
	}
}
