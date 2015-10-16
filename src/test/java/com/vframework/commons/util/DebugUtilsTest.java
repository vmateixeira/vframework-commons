package com.vframework.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugUtilsTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DebugUtilsTest.class);
	
	@Test
	public void getClassLocationTest() {
		LOGGER.info("" + DebugUtils.getClassLocation(DebugUtils.class));
	}
}
