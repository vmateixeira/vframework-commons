package com.vframework.commons.util;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vframework.commons.exception.FrameworkException;
import com.vframework.commons.util.FtpUtils;
import com.vframework.commons.util.PropertiesUtils;

public class FtpUtilsTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtilsTest.class);
	
	private FTPClient client;
	
	@Before
	public void before() throws FrameworkException {
		PropertiesUtils.loadTest();
		client = FtpUtils.connect();
		assertNotNull(client);
		FtpUtils.changeWorkingDirectory(client, "workspace");
	}
	
	@Test
	public void printWorkingDirectoryTest() throws FrameworkException {
		LOGGER.debug("PWD: " + FtpUtils.printWorkingDirectory(client));
	}
	
	@Test
	public void storeFileTest() throws FileNotFoundException, FrameworkException {
		FtpUtils.storeFile(client, "test.txt", new FileInputStream(new File("src\\test\\resources\\ftp\\send\\test.txt")));
	}
	
	@Test
	public void retrieveFileTest() throws FrameworkException {
		FtpUtils.retrieveFile(client, "test.txt", "src\\test\\resources\\ftp\\receive\\test.txt");
	}
	
	//TODO - makeDirectoryTest
	//TODO - listFilesTest
	//TODO - listFilesExtTest
	
	@After
	public void tearDown() throws FrameworkException {
		FtpUtils.disconnect(client);
	}
}
