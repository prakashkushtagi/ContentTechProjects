package com.trgr.dockets.core.util;


import junit.framework.Assert;

import org.junit.Test;

public class DocketNumberFormatterTest {

	/**
	 * Change docket number format from YYYYNNNNNNN to NNNNNNN/YYYY
	 */
	@Test
	public void testNYDocketNumberFormat1(){
		String inputDocketStr = "20110000541";
		String formattedDocketStr = DocketNumberFormatter.nyDocketNumberFormat1(inputDocketStr);
		Assert.assertEquals("0000541/2011", formattedDocketStr);
	}
	/**
	 * Change docket number format from NNNNNNNYYYY to NNNNNNN/YYYY
	 */
	@Test
	public void testNYDocketNumberFormat2(){
		String inputDocketStr = "00005412011";
		String formattedDocketStr = DocketNumberFormatter.nyDocketNumberFormat2(inputDocketStr);
		Assert.assertEquals("0000541/2011", formattedDocketStr);
	}
}
