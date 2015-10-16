package com.vframework.commons.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vframework.commons.exception.FrameworkException;

public class HttpUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
	
	public static String doGet(String url) throws FrameworkException {
		
		String response = null;
		
		Properties properties = new Properties();
	    Enumeration<?> propertieNames = properties.propertyNames();
		
		URL urlObject = null;
		HttpURLConnection connection = null;
		
		try {
			urlObject = new URL(url);
			
			connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestMethod("GET");
			
		    while(propertieNames.hasMoreElements()) {
		    	String key = (String) propertieNames.nextElement();
		    	if(key.startsWith("http.header.")) {
		    		connection.setRequestProperty(key.substring(0, 12), properties.getProperty(key));
		    	}
		    }
		    
		    LOGGER.debug("Sending 'GET' request to URL : " + url);
		    LOGGER.debug("Response Code : " + connection.getResponseCode());
			
			StringBuilder sb = new StringBuilder();
			String inputLine = null;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while((inputLine = reader.readLine()) != null) {
				sb.append(inputLine);
			}
			reader.close();
			
			response = sb.toString();
			
			LOGGER.debug("Response : " + response);
		} catch (IOException ioException) {
			throw new FrameworkException("Error performing HTTP GET request: ", ioException);
		}
		
		return response;
	}
	
	public static String doPost(String url, String payload) throws FrameworkException {
		
		String response = null;
		
		Properties properties = new Properties();
	    Enumeration<?> propertieNames = properties.propertyNames();
		
		URL urlObject = null;
		HttpURLConnection connection = null;
		
		try {
			urlObject = new URL(url);
			connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestMethod("POST");
		    
		    while(propertieNames.hasMoreElements()) {
		    	String key = (String) propertieNames.nextElement();
		    	if(key.startsWith("http.")) {
		    		connection.setRequestProperty(key.substring(0, 5), properties.getProperty(key));
		    	}
		    }
			connection.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(payload);
			wr.flush();
			wr.close();
			
			LOGGER.debug("Sending 'POST' request to URL : " + url);
			LOGGER.debug("Post parameters : " + payload);
			LOGGER.debug("Response Code : " + connection.getResponseCode());
			
			String inputLine;
			StringBuffer sb = new StringBuffer();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while ((inputLine = reader.readLine()) != null) {
				sb.append(inputLine);
			}
			reader.close();
			
			response = sb.toString();
			
			LOGGER.debug("Response : " + response);
		} catch (IOException ioException) {
			throw new FrameworkException("Error performing HTTP POST request: ", ioException);
		}
		
		return response;
	}
}
