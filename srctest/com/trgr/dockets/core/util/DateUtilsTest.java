/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testConvertDateStringToYYYYMMDD_1(){
		String inputDateStr = "12-31-2009";
		String formattedDateStr = DateUtils.convertDateStringToYYYYMMDD(inputDateStr);
		Assert.assertEquals("20091231", formattedDateStr);
	}
	
	@Test
	public void testConvertDateStringToYYYYMMDD_2(){
		String inputDateStr = "1-31-2009";
		String formattedDateStr = DateUtils.convertDateStringToYYYYMMDD(inputDateStr);
		Assert.assertEquals("20090131", formattedDateStr);
	}
	
	@Test
	public void testConvertDateStringToYYYYMMDD_3(){
		String inputDateStr = "1-1-2009";
		String formattedDateStr = DateUtils.convertDateStringToYYYYMMDD(inputDateStr);
		Assert.assertEquals("20090101", formattedDateStr);
	}
	
	@Test
	//This test will fail after year 2099
	public void testAddCenturyToDod_yyGtCurrent() {
		String input = "01-25-99";
		String actual = DateUtils.addCenturyToDod(input);
		Assert.assertEquals("01-25-1999", actual);
	}
	
	@Test
	public void testAddCenturyToDod_yyLtCurrent() {
		String input = "01-25-14";
		String actual = DateUtils.addCenturyToDod(input);
		Assert.assertEquals("01-25-2014", actual);
	}
	
	@Test
	public void testAddCenturyToDod_yyEqCurrent() {
		String input = "01-25-16";
		String actual = DateUtils.addCenturyToDod(input);
		Assert.assertEquals("01-25-2016", actual);
	}
	
	@Test
	public void testAddCenturyToDod_yyEqOrLtCurrent_mmddLtCurrent() {
		String input = "05-03-16";
		String actual = DateUtils.addCenturyToDod(input);
		Assert.assertEquals("05-03-2016", actual);
	}
	
	@Test
	public void testGetIsoDate24HourTimeStampWithMS(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		String strDate = sdf.format(cal.getTime());

		// i.e.: 20140522122412673 

		String formattedDateStr = DateUtils.getIsoDate24HourTimeStampWithMS();
		
		if (formattedDateStr.length() >= 14) // contains ms
		{
			Assert.assertEquals(strDate, formattedDateStr.substring(0, 12));
		}
		else {
			Assert.assertEquals(strDate, formattedDateStr); // someone changed it and it should fail
		}
	}
	@Test
	public void testGetFormattedDate(){
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(1998, 8, 27, 1, 45, 00);
		Date expectedDate = c1.getTime();

		// i.e.: 20140522122412673 

		Date formattedDateStr = DateUtils.getFormattedDate( "19980927014500", "yyyyMMddHHmmss");
		
		Assert.assertEquals(expectedDate.toString(),formattedDateStr.toString());
		
	}
	
	@Test
	public void testGetFormattedDateString(){
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(1998, 8, 27, 1, 45, 00);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strDate = sdf.format(c1.getTime());

		// i.e.: 20140522122412673 

		Date expectedDate = c1.getTime();
		String formattedDateStr = DateUtils.getFormattedDateString( expectedDate, "yyyyMMddHHmmss");
		
		Assert.assertEquals(strDate,formattedDateStr);
		
	}
	
}
