package com.trgr.dockets.core.util;

import org.junit.Assert;
import org.junit.Test;

public final class CurrencyUtilTest {
	
	@Test
	public void testZeroDecimals(){
		String result = CurrencyUtil.returnWithTwoDecimals("188");
		Assert.assertEquals("188.00", result);
	}
	
	@Test
	public void testOneDecimals(){
		String result = CurrencyUtil.returnWithTwoDecimals("188.1");
		Assert.assertEquals("188.10", result);
	}
	
	@Test
	public void testTwoDecimals(){
		String result = CurrencyUtil.returnWithTwoDecimals("188.70");
		Assert.assertEquals("188.70", result);
	}
	
	@Test
	public void testThreeDecimals(){
		String result = CurrencyUtil.returnWithTwoDecimals("188.230");
		Assert.assertEquals("188.23", result);
	}
	
	@Test
	public void testCharacterDecimals(){
		String result = CurrencyUtil.returnWithTwoDecimals("188.AF");
		Assert.assertEquals("188.AF", result);
	}
	
}
