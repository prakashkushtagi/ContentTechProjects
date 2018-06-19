/**
 * Copyright 2017: Thomson Reuters.
 * All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class DateFormatter {
	private DateFormatter() {
	}

	/**
	 * MM-dd-yyyy
	 */
	public static final DateFormat MM_DD_YYYY = new SimpleDateFormat("MM-dd-yyyy");
	/**
	 * yyyyMMdd
	 */
	public static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	/**
	 * yyyyMMddhhmmss
	 */
	public static final DateFormat YYYYMMDD24HMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * mm/dd/yyyy
	 */
	public static final DateFormat MMSDDSYYYY = new SimpleDateFormat("MM/dd/yyyy");
	

	/**
	 * Handles input formats:
	 * <ul>
	 * <li>MM-dd-yyyy</li>
	 * <li>MM/dd/yyyy</li>
	 * <li>yyyyMMdd</li>
	 * </ul>
	 * @param curDate
	 * @return
	 */
	public synchronized static Date formatDate(String curDate) {
		Date date = null;
		if (isInFormatYYYYMMDD(curDate)) {
			date = getDateInFormat(curDate, YYYYMMDD);
		} else if (isInFormatMM_DD_YYYY(curDate)) {
			date = getDateInFormat(curDate, MM_DD_YYYY);
		} else if (isInFormatMMSDDSYYYY(curDate)) {
			date = getDateInFormat(curDate, MMSDDSYYYY);
		}
		return date;
	}

	public synchronized static String formatDateTo(String curDate, DateFormat dateFormat) {
		Date date = null;
		if (isInFormatYYYYMMDD(curDate)) {
			date = getDateInFormat(curDate, YYYYMMDD);
		} else if (isInFormatMM_DD_YYYY(curDate)) {
			date = getDateInFormat(curDate, MM_DD_YYYY);
		} else if (isInFormatMMSDDSYYYY(curDate)) {
			date = getDateInFormat(curDate, MMSDDSYYYY);
		} else if(isInFormatYYYYMMDD24HMMSS(curDate)) {
			date = getDateInFormat(curDate, YYYYMMDD24HMMSS);
		}

		if (date != null) {
			return dateFormat.format(date);
		} else {
			return curDate;
		}
	}
	
	/**
	 * Determines whether or not a string is a date.
	 * @param curDate
	 * @return
	 */
	public synchronized static boolean isDate(String curDate) {
		Date date = null;
		if (isInFormatYYYYMMDD(curDate)) {
			date = getDateInFormat(curDate, YYYYMMDD);
		} else if (isInFormatMM_DD_YYYY(curDate)) {
			date = getDateInFormat(curDate, MM_DD_YYYY);
		} else if (isInFormatMMSDDSYYYY(curDate)) {
			date = getDateInFormat(curDate, MMSDDSYYYY);
		} else if(isInFormatYYYYMMDD24HMMSS(curDate)) {
			date = getDateInFormat(curDate, YYYYMMDD24HMMSS);
		}

		return date != null;
	}
	
	/**
	 * @param date - String representing a date
	 * @param inputFormat - Input format mask for the date parameter
	 * @param outputFormat - Output format mask for the date parameter
	 * @return
	 */
	public static synchronized String formatDate(String date, DateFormat inputFormat, DateFormat outputFormat){
		try {
			return outputFormat.format(inputFormat.parse(date));
		} catch (ParseException e) {
			return null;
		}
		
	}

	private static final Date getDateInFormat(String curDate, DateFormat dateFormat) {
		try {
			return dateFormat.parse(curDate);
		} catch (ParseException e) {
			return null;
		}
	}

	private static boolean isInFormatYYYYMMDD(String curDate) {
		if (curDate.length() != 8) {
			return false;
		}
		if (!curDate.matches("\\d+")) {
			return false;
		}
		int year = Integer.parseInt(curDate.substring(0, 4));
		int month = Integer.parseInt(curDate.substring(4, 6));
		int day = Integer.parseInt(curDate.substring(6));
		return isDateValid(year, month, day);
	}

	private static final String DATE_VALIDATION_FORMAT = "d-M-yyyy";

	private static boolean isDateValid(int year, int month, int day) {		
		DateFormat dateValidationFormat = new SimpleDateFormat(DATE_VALIDATION_FORMAT);
		dateValidationFormat.setLenient(false);
		
		try {
			dateValidationFormat.parse(day + "-" + month + "-" + year);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param curDate
	 * @return true, if curDate looks like 10-31-2002 or 1-31-2012 false,
	 *         otherwise.
	 */
	private static final boolean isInFormatMM_DD_YYYY(String curDate) {
		String[] datePieces = curDate.split("-");
		if (datePieces.length != 3) {
			return false;
		}
		for (String piece : datePieces) {
			if (!piece.matches("\\d+")) {
				return false;
			}
		}
		int month = Integer.parseInt(datePieces[0]);
		int day = Integer.parseInt(datePieces[1]);
		int year = Integer.parseInt(datePieces[2]);
		return isDateValid(year, month, day);
	}

	private static final boolean isInFormatMMSDDSYYYY(String curDate) {
		String[] datePieces = curDate.split("/");
		if (datePieces.length != 3) {
			return false;
		}
		for (String piece : datePieces) {
			if (!piece.matches("\\d+")) {
				return false;
			}
		}
		int month = Integer.parseInt(datePieces[0]);
		int day = Integer.parseInt(datePieces[1]);
		int year = Integer.parseInt(datePieces[2]);
		String strYear = Integer.toString(year);
		if (strYear.length() != 4){
			return false;
		}
		return isDateValid(year, month, day);
	}
	
	public static boolean isInFormatYYYYMMDD24HMMSS(String curDate)
	{
		boolean validFormat = false;
		
		if(curDate.length()==14 && StringUtils.isNumeric(curDate)) {
			validFormat = true;
		}
		return validFormat;
	}
	
	public static final boolean isFirstDateMoreRecent(String firstDate, String secondDate) {
		
		if(firstDate == null && secondDate ==null){
			return false;
		}
		
		if (firstDate == null) {
			return true;
		} else if (secondDate == null) {
			return false;
		}
		Date first = formatDate(firstDate);
		Date second = formatDate(secondDate);
		if(first == null && second ==null){
			return false;
		}
		if (first == null) {
			return true;
		} else if (second == null) {
			return false;
		}
		 else {
			return (first.compareTo(second) < 0);			
		}
	}
	
	public static final boolean isFirstSameAsSecond(String firstDate, String secondDate) {
		
		if(firstDate == null && secondDate ==null) {
			return false;
		}
		
		if (firstDate == null) {
			return false;
		} else if (secondDate == null) {
			return false;
		}
		Date first = formatDate(firstDate);
		Date second = formatDate(secondDate);
		if (first == null) {
			return false;
		} else if (second == null) {
			return false;
		} else {
			return (first.compareTo(second) == 0);			
		}
	}
	
	public static String formatTimeto24hour(String timeValue){
		String timeRegex = "(.*?):(.*?)(AM|PM)";
		Pattern timePattern = Pattern.compile(timeRegex);
		String time = timeValue;
		Matcher m = timePattern.matcher(timeValue);
		if (m.find()){
			String hours = m.group(1);
			String minutes = m.group(2);
			String amPm = m.group(3);
			Integer militaryHours = Integer.parseInt(hours);
			if (amPm.toUpperCase().equals("PM") && militaryHours != 12){
				militaryHours += 12;
			} else if (amPm.toUpperCase().equals("AM") && militaryHours == 12){
				militaryHours = 00;
			}
			hours = militaryHours.toString();			
			time = hours+minutes;
		}
		
		return time;
	}
}
