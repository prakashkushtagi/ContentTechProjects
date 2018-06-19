package com.trgr.dockets.core.service;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.util.Assert;

import com.trgr.dockets.core.util.UUIDGenerator;


public class LtcEventHandlerTest {
	
	//Test to prepare Event Id(Guid)
	@Test
	public void prepareEventIdTest(){

		String expectedEventId = UUIDGenerator.createUuid().toString();
		int length = expectedEventId.length();
		
		Assert.notNull(expectedEventId);
		assertEquals(33, length);

	}
	
	//Test for preparing batch and subbatch id
	@Test
	public void batchSubBatchSeparationTest(){
		
		String collaborationKey = "213123dasdsdasdfr5674rty342efr432_dsaddssad1231231d6t7y8uio90poi87u";
		String batchId = null;
		String subBatchId = null;
		
		
		if(collaborationKey.contains("_")){

			String str[]=collaborationKey.split("_");
			batchId = str[0];
			subBatchId = str[1];
			
			assertEquals("213123dasdsdasdfr5674rty342efr432", batchId);
			assertEquals("dsaddssad1231231d6t7y8uio90poi87u", subBatchId);	
		
		} 
	}
	
	//Test for transforming event timestamp
	@Test
	public void eventTimestampTest() throws ParseException{
		String oldDate = "2005-09-23 10:02:07.017";
		String expectedDate = "23-Sep-05 10:02:07.000000017 AM";
		String actualDate = null;
		SimpleDateFormat formatter, FORMATTER;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = formatter.parse(oldDate);
		FORMATTER = new SimpleDateFormat("dd-MMM-yy HH:mm:ss.SSSSSSSSS a");
		actualDate = FORMATTER.format(date);
		
		Assert.notNull(actualDate);
		assertEquals(expectedDate, actualDate);
		
		
	}
}
