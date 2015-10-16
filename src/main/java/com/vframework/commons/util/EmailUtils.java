package com.vframework.commons.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtils.class);
	
	public static synchronized void sendEmail(String sender, String toRecipients[], String ccRecipients[], String bccRecipients[], String subject, String message, String[] attachments, String attachmentsPath) {
		
		Properties systemProperties = System.getProperties();
		EmailAuthenticator authentication = new EmailAuthenticator(
				systemProperties.getProperty("mail.smtp.user"),
				systemProperties.getProperty("mail.smtp.password"));
		
		Session session = Session.getDefaultInstance(systemProperties, authentication);
		session.setDebug(true);
		
		MimeMessage mimeMessage = new MimeMessage(session);
		BodyPart messageBodyPart = new MimeBodyPart();
		
		try {
			messageBodyPart.setText(message);
			messageBodyPart.setHeader("Content-Type", systemProperties.getProperty("mail.type"));
			
			Multipart multiPart = new MimeMultipart();
			multiPart.addBodyPart(messageBodyPart);
			
			DataSource dataSource = null;
			DataHandler dataHandler = null;
			
			//Multipart (with attachments)
			if(null != attachments && attachments.length > 0) {	
				for(String attachment : attachments) {
					messageBodyPart = new MimeBodyPart();
					dataSource = new FileDataSource(attachmentsPath + attachment);
					dataHandler = new DataHandler(dataSource);
					messageBodyPart.setDataHandler(dataHandler);
					messageBodyPart.setFileName(attachment);
					multiPart.addBodyPart(messageBodyPart);
				}
			}
			
			mimeMessage.setContent(multiPart);
			mimeMessage.setSubject(subject);
			mimeMessage.setFrom(new InternetAddress(sender));
			
			if(null != toRecipients) {
				for(String recipient : toRecipients) {
					mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
				}
			}
			
			if(null != ccRecipients) {
				for(String recipient : ccRecipients) {
					mimeMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(recipient));
				}
			}
			
			if(null != bccRecipients) {
				for(String recipient : bccRecipients) {
					mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(recipient));
				}
			}
			
			Transport transport = session.getTransport("smtp");
			transport.connect(
					systemProperties.getProperty("mail.smtp.host"),
					Integer.parseInt(systemProperties.getProperty("mail.smtp.port")),
					systemProperties.getProperty("mail.smtp.user"),
					systemProperties.getProperty("mail.smtp.password"));
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
		} catch (MessagingException messagingException) {
			LOGGER.error("Error sending email: {}", messagingException.getMessage());
		}
	}
}

class EmailAuthenticator extends Authenticator {
	private String username;
	private String password;
	
	public EmailAuthenticator(String username, String password) {
		this.username  = username;
		this.password = password;
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username , password);
	}
}
