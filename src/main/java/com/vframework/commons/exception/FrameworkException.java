package com.vframework.commons.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrameworkException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkException.class);
	
	public FrameworkException(String message) {
		super(message);
	}
	
	public FrameworkException(String message, Object... params) {
		super(message);
		LOGGER.error(message, params);
	}
	
	public FrameworkException(String message, Throwable throwable) {
		super(message);
		LOGGER.error("Framework Exception: ", message, throwable);
	}
	
	public FrameworkException(Throwable fillInStackTrace) {
		super(fillInStackTrace);
		LOGGER.error("Framework Exception: ", fillInStackTrace); 
	}
}
