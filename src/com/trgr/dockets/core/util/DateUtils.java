/*
 * Copyright 2016: Thomson Reuters.
 * All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author C047166
 *
 */
public class DateUtils
{
	static Logger LOG = Logger.getLogger(DateUtils.class);

	public static String getIsoDate24HourTimeStampWithMS() {
		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);
		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		String second = Integer.toString(cal.get(Calendar.SECOND));
		String milliSecond = Integer.toString(cal.get(Calendar.MILLISECOND));
		
		if (month.length() < 2) { month = "0" + month; }
		if (day.length() < 2) { day = "0" + day; }
		if (hour.length() < 2) { hour = "0" + hour; }
		if (minute.length() < 2) { minute = "0" + minute; }
		if (second.length() < 2) { second = "0" + second; }
		if (milliSecond.length() < 3 && milliSecond.length() > 1) {
			second = "0" + second; 
		} else if (milliSecond.length() < 3 && milliSecond.length() > 0) {
			second = "00" + second; 
		}
		
		StringBuffer sb=new StringBuffer();
		sb.append(year).append(month).append(day).append(hour).append(minute).append(second).append(milliSecond);
		return (sb.toString());
	}
	
	/**
	 * @param dateStr
	 * @param formatStr
	 * @return - Date instance or null when a failure occurs.
	 */
	public static Date getFormattedDate(String dateStr, String formatStr)
    {
		if (dateStr == null || formatStr == null) {
			return null;
		}

		//Sync actual Date String passed with the requested format.
		if (!formatStr.contains("-"))
		{
			dateStr = dateStr.replaceAll("-", "");
		} else if (formatStr.contains("-") && !dateStr.contains("-"))
		{
			dateStr = dateStr.replaceAll("^([0-9]{4})([0-9]{2})([0-9]{2})$", "$1-$2-$3");
		}
		
        //Tue May 25 16:22:39 CDT 2004
        SimpleDateFormat simple = new SimpleDateFormat(formatStr);
        try
        {
            return simple.parse(dateStr);
		} catch (ParseException e)
		{
			LOG.warn("Unexpected date format!  Expected format is " + formatStr + " but date value is " + dateStr);
		}
        return null;
    }
	
	/**
	 * @param date - date to be formatted
	 * @param formatStr - format string
	 * @return - String instance or null when a failure occurs.
	 */
	public static String getFormattedDateString(Date date, String formatStr)
    {
        //Tue May 25 16:22:39 CDT 2004
        SimpleDateFormat simple = new SimpleDateFormat(formatStr);
        try
        {
            return simple.format(date);
        }
        catch (Exception e)
        {
        }
        return null;
    }
	
	/**
	 * Changes the timeZone
	 * @param date
	 * @param formatStr
	 * @param timeZone
	 * @return
	 */
	public static String getFormattedDateTimeZone(Date date, String formatStr, String timeZone)
    {
        SimpleDateFormat simple = new SimpleDateFormat(formatStr);
        try
        {
        	simple.setTimeZone(TimeZone.getTimeZone(timeZone));
            return simple.format(date);
        }
        catch (Exception e)
        {
        }
        return null;
    }
	
	/**
	 * Convert a date string of format {M}M-{D}D-YYYY to YYYYMMDD format. The characters mentioned in {} are optional.
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String convertDateStringToYYYYMMDD(String dateStr){
		String formatttedDateStr = null;
		if (dateStr.length() > 3 && dateStr.contains("-")){
			String year = dateStr.substring(dateStr.length()-4);
			String month = dateStr.substring(0, dateStr.indexOf('-'));
			if(month != null && month.length() == 1){
				month = "0" + month;
			}
			String day = StringUtils.substringBetween(dateStr, "-", "-");
			if(day != null && day.length() == 1){
				day = "0" + day;
			}
			formatttedDateStr = year + month + day;
		}
		return formatttedDateStr;
	}
	
	public static String addCentury(String firstPart) {
		String century = "";
		if (StringUtils.isNumeric(firstPart) && firstPart.length() == 2) {
			century = determineFullYear(firstPart);
		}
		return century;
	}

	private static String determineFullYear(String yy) {
		DateFormat df = new SimpleDateFormat("dd.mm.yy");
		Date date = null;
		try {
			date = df.parse("01.01." + yy);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat sdff = new SimpleDateFormat("yyyy");
		return sdff.format(date);
	}
	
	// Converts MM-DD-YY to MM-DD-YYYY
	public static String addCenturyToDod(String mm_dd_yy) {
		String outputDate = mm_dd_yy;
		String regex = "(\\d{1,2})-(\\d{1,2})-(\\d{2})";
		Calendar currentDate = Calendar.getInstance();
		int currentYear = currentDate.get(Calendar.YEAR) % 100;
		int currentMonth = currentDate.get(Calendar.MONTH) + 1;
		int currentDay = currentDate.get(Calendar.DATE);

		String yyyy = "";
		String deathYear, deathDay, deathMonth;

		if (mm_dd_yy.matches(regex)) {
			Matcher m = Pattern.compile(regex).matcher(mm_dd_yy);
			if (m.find()) {
				deathMonth = (m.group(1));
				deathDay = (m.group(2));
				deathYear = (m.group(3));
				String previousCentury = "19";
				String currentCentury = "20";
				if (Integer.parseInt(deathYear) > currentYear) {
					yyyy = previousCentury + deathYear;
				} else if (Integer.parseInt(deathYear) < currentYear) {
					yyyy = currentCentury + deathYear;
				} else {
					// Compare months
					if (Integer.parseInt(deathMonth) > currentMonth) {
						yyyy = previousCentury + deathYear;
					} else if (Integer.parseInt(deathMonth) < currentMonth) {
						yyyy = currentCentury + deathYear;
					} else {
						// Compare days
						if (Integer.parseInt(deathDay) >= currentDay) {
							yyyy = previousCentury + deathYear;
						} else {
							yyyy = currentCentury + deathYear;
						}
					}
				}
			}
		}
		return outputDate.replaceAll("\\d{2}$", yyyy);
	}
	
}
