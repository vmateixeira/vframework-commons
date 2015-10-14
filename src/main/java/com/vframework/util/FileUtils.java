package com.vframework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public class FileUtils {
	
	public static String readFile(final String fileLocation) {
		File file = new File(fileLocation);
		//Only supports files up to (2^31 - 1) bytes (2147483647)
		byte[] byteArray = new byte[(int) file.length()];
		
		int length = byteArray.length;
		int total = 0;
		
		InputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream(file);
			
			while(total < length) {
				int result = inputStream.read(byteArray, total, length - total);
				if(result == -1)
					break;
				total += result;
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		return new String(byteArray, StandardCharsets.UTF_8);
	}
	
	public static void writeFile(final InputStream inputStream, final String fileLocation) {
		OutputStream outputStream = null;
		
		try {
			File fileToUpload = new File(fileLocation);
			outputStream = new FileOutputStream(fileToUpload);
			
			byte[] buffer = new byte[2048];
			int length = 0;
			
			while((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}		
	}
	
	public static void copyFile(final File sourceFile, final File destinationFile) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(destinationFile);
			
			byte[] buffer = new byte[2048];
			int length = 0;
			
			while((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	public static String readFileJava7(final String fileLocation) throws IOException {
		return new String(Files.readAllBytes(Paths.get(fileLocation)), StandardCharsets.UTF_8);
	}
	
	public static void writeFileJava7(final InputStream inputStream, final String fileLocation) throws IOException {
		Files.copy(inputStream, Paths.get(fileLocation));
	}
	
	public static void copyFileJava7(final File sourceFile, final File destinationFile) throws IOException {
	    Files.copy(sourceFile.toPath(), destinationFile.toPath());
	}
	
	public static String getMD5HashJava7(final String fileLocation) throws IOException, NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(Files.readAllBytes(Paths.get(fileLocation)));
		
		byte[] digest = messageDigest.digest();
		return Arrays.toString(digest);
	}
	
	public static String getMD5HashHexJava7(final String fileLocation) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(Files.readAllBytes(Paths.get(fileLocation)));
		
		byte[] digest = messageDigest.digest();
		return DatatypeConverter.printHexBinary(digest).toUpperCase();
	}
}
