package com.vframework.commons.util;

public class StringUtils {
	
	public static String getSafeValue(final String inputString) {
		return null == inputString ? "" : inputString.trim();
	}
	
	public static boolean isNullOrEmpty(final String inputString) {
		return null == inputString || inputString.trim().isEmpty(); 
	}
}
