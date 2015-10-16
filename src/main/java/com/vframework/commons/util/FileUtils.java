package com.vframework.util;

import java.io.BufferedInputStream;
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
import java.util.zip.CRC32;

import javax.xml.bind.DatatypeConverter;

public class FileUtils {
	
	public static String readFile(String filePath) {
		File file = new File(filePath);
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
	
	public static void writeFile(InputStream inputStream, String filePath) {
		OutputStream outputStream = null;
		
		try {
			File fileToUpload = new File(filePath);
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
	
	public static void copyFile(File sourceFilePath, File destinationFilePath) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		
		try {
			inputStream = new FileInputStream(sourceFilePath);
			outputStream = new FileOutputStream(destinationFilePath);
			
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
	
	public static String readFileJava7(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
	}
	
	public static void writeFileJava7(InputStream inputStream, String filePath) throws IOException {
		Files.copy(inputStream, Paths.get(filePath));
	}
	
	public static void copyFileJava7(File sourceFilePath, File destinationFilePath) throws IOException {
	    Files.copy(sourceFilePath.toPath(), destinationFilePath.toPath());
	}
	
	public static String getMD5SumJava7(String filePath) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(Files.readAllBytes(Paths.get(filePath)));
		
		byte[] digest = messageDigest.digest();
		return DatatypeConverter.printHexBinary(digest).toUpperCase();
	}
	
	public static String getSHA1SumJava7(String filePath) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		messageDigest.update(Files.readAllBytes(Paths.get(filePath)));
		
		byte[] digest = messageDigest.digest();
		return DatatypeConverter.printHexBinary(digest).toUpperCase();
	}
	
	public static String getMD5Sum(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		
		byte[] buffer = new byte[2048];
		int length = 0;
		
		while((length = inputStream.read(buffer)) > 0) {
			messageDigest.update(buffer, 0, length);
		}
		
		byte[] hashBytes = messageDigest.digest();
		
		StringBuffer hexStringBuffer = new StringBuffer();
		for(int i = 0; i < hashBytes.length; i ++) {
			hexStringBuffer.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return hexStringBuffer.toString();
	}
	
	public static boolean checkMD5Sum(InputStream inputStream, String sum) throws NoSuchAlgorithmException, IOException {
		return getMD5Sum(inputStream).equals(sum);
	}
	
	public static String getSHA1Sum(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
		
		byte[] buffer = new byte[2048];
		int length = 0;
		
		while((length = inputStream.read(buffer)) > 0) {
			messageDigest.update(buffer, 0, length);
		}
		
		byte[] hashBytes = messageDigest.digest();
		
		StringBuffer hexStringBuffer = new StringBuffer();
		for(int i = 0; i < hashBytes.length; i ++) {
			hexStringBuffer.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return hexStringBuffer.toString();
	}
	
	public static boolean checkSHA1Sum(InputStream inputStream, String sum) throws NoSuchAlgorithmException, IOException {
		return getSHA1Sum(inputStream).equals(sum);
	}
	
	public static long getCRC32(FileInputStream fileInputStream) throws IOException {
		CRC32 crc = new CRC32();
		
		InputStream inputStream = new BufferedInputStream(fileInputStream);
		int length = 0;
		
		while((length = inputStream.read()) > 0) {
			crc.update(length);
		}
		
		return crc.getValue();
	}
	
	public static boolean checkCRC32(FileInputStream fileInputStream, long crc) throws IOException {
		return getCRC32(fileInputStream) == crc;
	}
	
	public static String getSHA256Sum(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
		
		byte[] buffer = new byte[2048];
		int length = 0;
		
		while((length = inputStream.read(buffer)) > 0) {
			messageDigest.update(buffer, 0, length);
		}
		
		byte[] hashBytes = messageDigest.digest();
		
		StringBuffer hexStringBuffer = new StringBuffer();
		for(int i = 0; i < hashBytes.length; i ++) {
			hexStringBuffer.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
        
        return hexStringBuffer.toString();
    }
	
	public static boolean checkSHA256Sum(InputStream inputStream, String sum) throws NoSuchAlgorithmException, IOException {
		return getSHA256Sum(inputStream).equals(sum);
	}
	
	//TODO - cleanup this utility class (be consistent, either use BufferedInputStream/FileInputStream/InputStream)
	//TODO - wrap everything around FrameworkException
}
