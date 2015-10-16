package com.vframework.commons.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vframework.commons.exception.FrameworkException;
import com.vframework.commons.util.HttpUtils;

public class HttpUtilsTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtilsTest.class);
	
	@Test
	public void doGetTest() throws FrameworkException {
		LOGGER.info(HttpUtils.doGet("https://www.priberam.pt/DLPO/bola"));
	}
	
	@Test
	public void doPostTest() throws FrameworkException {
		LOGGER.info(HttpUtils.doPost("https://selfsolve.apple.com/wcResults.do", "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345"));
	}
}
