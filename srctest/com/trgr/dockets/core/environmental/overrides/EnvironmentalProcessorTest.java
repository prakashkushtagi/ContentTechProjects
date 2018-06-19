/*Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.environmental.overrides;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.trgr.dockets.core.environment.overrides.EnvironmentalRuleProcessor;

public class EnvironmentalProcessorTest {
	
	@Before
	public void setUp(){
		System.setProperty("environment", "workstation");
	}

	@Test
	public void testSplitByCountyId() throws Exception{
		Assert.assertTrue(EnvironmentalRuleProcessor.splitByVendor(46L));
	}
		@Test
	public void testSplitImaginaryVendorId() throws Exception{
		Assert.assertTrue(!EnvironmentalRuleProcessor.splitByVendor(-1L));
	}
	
	@Test
	public void overrideFilterChain() throws Exception{
		Assert.assertTrue(EnvironmentalRuleProcessor.overrideFilterChain(46L));
	}
	
	@Test
	public void overrideFilterChainQA() throws Exception{
		System.setProperty("environment", "qa");
		Assert.assertTrue(EnvironmentalRuleProcessor.overrideFilterChain(46L));
	}
	
	@Test
	public void overrideFilterChainProd() throws Exception{
		System.setProperty("environment", "prod");
		Assert.assertTrue(EnvironmentalRuleProcessor.overrideFilterChain(46L));
	}
	
	@Test
	public void doNotOverrideFilterChain() throws Exception{
		System.setProperty("environment", "THIS IS AN IMAGINARY ENVIRONMENT");
		Assert.assertTrue(!EnvironmentalRuleProcessor.overrideFilterChain(46L));
	}
	
	@Test
	public void getFilterChainName() throws Exception{
		Assert.assertEquals("Filter chain name", "stateDocketsFilterChain", EnvironmentalRuleProcessor.getCustomFilterChain(46L));
	}
}
