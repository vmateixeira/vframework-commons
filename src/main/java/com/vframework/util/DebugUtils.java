package com.vframework.util;

import java.io.OutputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

public class DebugUtils {
	
	/**
	 * Helpful for debugging 'Class Not Found Exception' when there are
	 * multiple libraries using different versions of the same class
	 */
	public static URL getClassLocation(final Class<?> clazz) {
		return clazz.getProtectionDomain().getCodeSource().getLocation();
	}
	
	/** 
	 * Usage example:
	 * 
	 * JAXBContext context = JAXBContext.newInstance(ObjectX.class);
	 * 
	 * Marshaller m = context.createMarshaller();
	 * m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 * 
	 * QName qName = new QName("com.vframework.util", "name");
	 * JAXBElement<ObjectX> root = new JAXBElement<ObjectX>(qName, ObjectX.class, name);
	 * m.marshal(root, new FileOutputStream("file"));
	 * 
	 * (Note: ObjectX.class is an XML annotated class)
	 * 
	 * */
	public static boolean xmlToOutputStream(final Class<?> clazz, final Object object, final OutputStream outputStream) {
		
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			
			Marshaller marshaller = context.createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    marshaller.marshal(object, outputStream);
		} catch (JAXBException exception) {
			return false;
		}
		
		return true;
	}
	
	public static boolean xmlToOutputStream(final Class<?> clazz, final Object object, final String namespaceURI, final String localPart, final OutputStream outputStream) {
		QName qName = new QName(namespaceURI, localPart);
		return xmlToOutputStream(clazz, object, qName, outputStream);
	}
	
	public static boolean xmlToOutputStream(final Class<?> clazz, final Object object, final QName qName, final OutputStream outputStream) {
		@SuppressWarnings("unchecked")
		JAXBElement<Object> rootElement = new JAXBElement<Object>(qName, (Class<Object>) clazz, object);
		return xmlToOutputStream(clazz, rootElement, outputStream);
	}
}
