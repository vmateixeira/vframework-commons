package com.vframework.commons.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.vframework.commons.exception.FrameworkException;

public class DateUtils {
	public static String yyyyMdd_HHmmss = "yyyy-M-dd HH:mm:ss";
	public static String ddMMyyyy_HHmmss = "dd-MM-yyyy HH:mm:ss";
	public static String ddMMYYYY = "dd/MM/YYYY";
	public static String YYYYMMdd = "YYYY-MM-dd";
	
	public static String formatDate(Date date, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		return simpleDateFormat.format(date);
	}
	
	public static Date parseDate(String dateString, String dateFormat) throws FrameworkException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		try {
			return simpleDateFormat.parse(dateString);
		} catch (ParseException parseException) {
			throw new FrameworkException("Error parsing date string {} with format {}", dateString, dateFormat);
		}
	}
	
	public static java.sql.Date toSqlDate(java.util.Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}
	
	public static java.util.Date toUtilDate(java.sql.Date sqlDate) {
		return new java.util.Date(sqlDate.getTime());
	}
	
	public static java.sql.Date toSqlDate(Calendar calendar) {
		return new java.sql.Date(calendar.getTimeInMillis());
	}
	
	public static java.util.Date toUtilDate(Timestamp timestamp) {
		return new java.util.Date(timestamp.getTime());
	}
	
	public static Calendar toCalendar(int day, int month, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year + 1);
		return calendar;
	}
}
