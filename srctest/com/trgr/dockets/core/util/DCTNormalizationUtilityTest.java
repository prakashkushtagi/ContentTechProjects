/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Created: Feb 3, 2017
 * @author Tyler Studanski (u0162605)
 */
public class DCTNormalizationUtilityTest {
	
	@Test
	public void testFindPrefix() throws Exception {
		assertEquals("Dr.", DCTNormalizationUtility.findPrefix("Dr. Alan P. Dagen"));
	}

	@Test
	public void testNormalizePartyNameString() throws Exception {
		assertEquals("Sr. Judge Robert H. Hodges, Jr", DCTNormalizationUtility.normalizePartyName("Sr. Judge Robert H. Hodges, Jr"));
		assertEquals("ALEXANDER A. FAMIANO, II, SRL", DCTNormalizationUtility.normalizePartyName("FAMIANO, ALEXANDER A. II SRL"));
		assertEquals("Michael B. Powers, PHV", DCTNormalizationUtility.normalizePartyName("Michael B. Powers , PHV"));
		assertEquals("Michael B. Powers, PHV", DCTNormalizationUtility.normalizePartyName("PHV Michael B. Powers"));
		assertEquals("Magistrate Judge James C. Francis, IV", DCTNormalizationUtility.normalizePartyName("Magistrate Judge James C. Francis IV"));
		assertEquals("Magistrate Duty Magistrate", DCTNormalizationUtility.normalizePartyName("Magistrate Duty Magistrate"));
		assertEquals("U.S. Mag Judge Claude W. Hicks, Jr.", DCTNormalizationUtility.normalizePartyName("U.S. Mag Judge Claude W. Hicks, Jr."));
	}
	
	@Test
	public void testNormalizePartyStatusString() throws Exception {
		assertEquals("legal representative of a minor child", DCTNormalizationUtility.redactNameStatus("legal representative of a minor child"));
		assertEquals("legal representatives of a minor child", DCTNormalizationUtility.redactNameStatus("legal representatives of a minor child"));
		assertEquals("on behalf minor child", DCTNormalizationUtility.redactNameStatus("on behalf her minor child"));
	}

	@Test
	public void testFindSuffix() throws Exception {
		assertEquals(" PHV", DCTNormalizationUtility.findSuffix("Michael B. Powers PHV"));
	}
	
	@Test
	public void testFixSuffix() throws Exception {
		assertEquals("Michael B. Powers PHV", DCTNormalizationUtility.fixSuffix("PHV Michael B. Powers"));
	}
	
	@Test
	public void testRedactNameStatus(){
		assertEquals("Minor", DCTNormalizationUtility.redactNameStatus("Dean Minor"));
	}
}
