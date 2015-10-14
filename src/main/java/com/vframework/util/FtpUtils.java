package com.vframework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vframework.exception.FrameworkException;

public class FtpUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FtpUtils.class);
	
	/* File types */
    public static final int FILE_TYPE = 0;
    public static final int DIRECTORY_TYPE = 1;
    public static final int SYMBOLIC_LINK_TYPE = 2;
    public static final int UNKNOWN_TYPE = 3;
	
	/**
	 * Establishes FTP connection
	 * @throws FrameworkException 
	 */
	public static FTPClient connect() throws FrameworkException {
		Properties properties = System.getProperties();
		
		String host = properties.getProperty("ftp.host");
		int port = Integer.parseInt(properties.getProperty("ftp.port"));
		String user = properties.getProperty("ftp.user");
		String password = properties.getProperty("ftp.password");
		
		FTPClient ftpClient = new FTPClient();
		
		try {
			ftpClient.connect(host, port);
			int replyCode = ftpClient.getReplyCode();
			LOGGER.debug("FTP connect reply code: {}", replyCode);
			
			if(!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FrameworkException("Server host {} refused connection on port {} {}", host, port, ftpClient.getReplyStrings());
			} else if(ftpClient.login(user, password)) {
				
				replyCode = ftpClient.getReplyCode();
				LOGGER.debug("Login reply code: {}", replyCode);
				
				//ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
				ftpClient.setFileTransferMode(Integer.parseInt(properties.getProperty("ftp.Default-Transfer-Mode")));
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.setBufferSize(Integer.parseInt(properties.getProperty("ftp.Default-Buffer-Size")));
			} else {
				throw new FrameworkException("Login failed on server {}:{} {}", host, port, ftpClient.getReplyStrings());
			}
		} catch (IOException ioException) {
			LOGGER.error("Error establishing FTP connection", ioException);
		}
		
		return ftpClient;
	}
	
	/**
	 * Change current working directory
	 * @throws FrameworkException 
	 */
	public static void changeWorkingDirectory(FTPClient ftpClient, String directoryPath) throws FrameworkException {
		Properties properties = System.getProperties();
		
		try {
			int replyCode = ftpClient.cwd((directoryPath != null && !directoryPath.isEmpty()) ? directoryPath : properties.getProperty("ftp.Main-Working-Directory"));
			LOGGER.debug("Change directory reply code: {}", replyCode);
			
			if(!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FrameworkException("Error changing working directory to: {} {}", directoryPath, ftpClient.getReplyStrings());
			}
		} catch (IOException ioException) {
			LOGGER.error("Error changing working directory", ioException);
		}
	}
	
	/**
	 * Creates a new directory under current working directory
	 * @throws FrameworkException 
	 */
	public static void makeDirectory(FTPClient ftpClient, String directoryPath) throws FrameworkException {
		try {
			int replyCode = ftpClient.mkd(directoryPath);
			LOGGER.debug("Make new directory reply code: {}", replyCode);
			
			if(!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FrameworkException("Error creating directory: {} {}", directoryPath, ftpClient.getReplyStrings());
			}
		} catch (IOException ioException) {
			LOGGER.error("Error creating directory", ioException);
		}
	}
	
	/**
	 * Prints working directory path
	 * @throws FrameworkException 
	 */
	public static String printWorkingDirectory(FTPClient ftpClient) throws FrameworkException {
		String workDir = null;
		
		try {
			int replyCode = ftpClient.pwd();
			LOGGER.debug("Print working directory reply code: {}", replyCode);
			
			if(FTPReply.isPositiveCompletion(replyCode)) {
				String reply = ftpClient.getReplyString();
				
				StringBuilder sb = new StringBuilder();
				boolean quoteStart = false;
				
				for(int i = 0; i < reply.length(); i ++) {
					char c = reply.charAt(i);
					
					if(c == '"') {
						if(quoteStart) {
							quoteStart = false;
						} else {
							quoteStart = true;
						}
					} else if(quoteStart) {
						sb.append(c);
					}
				}
				
				workDir = sb.toString();
			} else {
				throw new FrameworkException("Error printing current working directory {}", ftpClient.getReplyString());
			}
		} catch (IOException ioException) {
			LOGGER.error("Error printing working directory", ioException);
		}
		
		return workDir;
	}
	
	/**
	 * Stores a file remotely
	 * @throws FrameworkException 
	 */
	public static void storeFile(FTPClient ftpClient, String fileName, InputStream inputStream) throws FrameworkException {
		try {
			if(ftpClient.storeFile(fileName, inputStream)) {
				LOGGER.debug("Store file reply code: {}", ftpClient.getReplyCode());
			} else {
				LOGGER.debug("Store file reply code: {}", ftpClient.getReplyCode());
				throw new FrameworkException("Error storing file {} remotely", fileName, ftpClient.getReplyStrings());
			}
		} catch (IOException ioException) {
			LOGGER.error("Error storing a file remotely", ioException);
		}
	}
	
	/**
	 * Lists files in current working directory
	 * @throws FrameworkException
	 */
	public static FTPFile[] listFiles(FTPClient ftpClient) throws FrameworkException {
		FTPFile[] filesList = null;
		
		try {
			filesList = ftpClient.listFiles();
			
			if(null != filesList) {
				LOGGER.debug("List files reply code: {}", ftpClient.getReplyCode());
			} else {
				LOGGER.debug("List files reply code: {}", ftpClient.getReplyCode());
				throw new FrameworkException("Error listing files in current working directory {}", ftpClient.getReplyString());
			}
		} catch (IOException ioException) {
			LOGGER.error("Error listing files", ioException);
		}
		
		return filesList;
	}
	
	/**
	 * Extended method for listing files in current working directory
	 * @throws FrameworkException
	 */
	public static List<Map<String, String>> listFilesExt(FTPClient ftpClient) throws FrameworkException {
		FTPFile[] filesList = listFiles(ftpClient);
		
		List<Map<String, String>> fileInformationList = new ArrayList<Map<String, String>>();
		Map<String, String> fileInformation = null;
		
		for(FTPFile file : filesList) {
			fileInformation = new HashMap<String, String>();
			fileInformation.put("name", file.getName());
			fileInformation.put("type", String.valueOf(file.getType()));
			fileInformation.put("size", String.valueOf(file.getSize()));
			fileInformation.put("timestamp", String.valueOf(file.getTimestamp().getTimeInMillis()));
			fileInformationList.add(fileInformation);
		}
		
		return fileInformationList;
	}
	
	/**
	 * Retrieves remote file from current working directory
	 * @throws FrameworkException 
	 */
	public static void retrieveFile(FTPClient ftpClient, String remoteFileName, String localFilePath) throws FrameworkException {
		File file = new File(localFilePath);
		
		try {
			if(ftpClient.retrieveFile(remoteFileName, new FileOutputStream(file))) {
				LOGGER.debug("Retrieve file reply code: {}", ftpClient.getReplyCode());
			} else {
				LOGGER.debug("Retrieve file reply code: {}", ftpClient.getReplyCode());
				throw new FrameworkException("Error retrieving remote file {}", remoteFileName, ftpClient.getReplyString());
			}
		} catch (IOException ioException) {
			LOGGER.error("Error retrieving remote file", ioException);
		}
	}
	
	/**
	 * Disconnects from FTP server
	 * @throws FrameworkException 
	 */
	public static void disconnect(FTPClient ftpClient) throws FrameworkException {
		try {
			if(ftpClient.logout()) {
				LOGGER.debug("Logout reply code: {}", ftpClient.getReplyCode());
			} else {
				LOGGER.debug("Logout reply code: {}", ftpClient.getReplyCode());
				throw new FrameworkException("Error logging out from FTP server {}", ftpClient.getReplyString());
			}
			ftpClient.disconnect();
		} catch (IOException ioException) {
			LOGGER.error("Error disconnecting from FTP server", ioException);
		}
	}
	
	//TODO - sftp support
	//TODO - delete file/folder
	//TODO - recursive download
	//TODO - recursive upload
}

