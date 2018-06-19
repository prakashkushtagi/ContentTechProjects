/**Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/

package com.trgr.dockets.core.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DctDocketNumberUtilityTest {

	List<Case> caseList;
	List<Case> caseListLocationCode;
	
	@Before
	public void setUp(){
		caseList = new ArrayList<Case>();
				
		caseList.add(new Case("B:2016mj90021","B:16-MJ-90021"));
		caseList.add(new Case("9:2015po05243","9:15-PO-05243"));
		caseList.add(new Case("6:2003cr10140","6:03-CR-10140"));
		caseList.add(new Case("5:2014cv03214","5:14-CV-03214"));
		caseList.add(new Case("2:1989cv00039","2:89-CV-00039"));
		caseList.add(new Case("2:2016-cv-12345-JMT","2:16-CV-12345"));
		caseList.add(new Case("Z:1995-cr-00911","Z:95-CR-00911"));
		caseList.add(new Case("0:1999CV67890","0:99-CV-67890"));
		caseList.add(new Case("10:99CV66666","10:99-CV-66666"));
		
		// Dockets without location code to be dropped
		caseListLocationCode = new ArrayList<Case>();
		caseListLocationCode.add(new Case("2005cv00520","2005-CV-00520"));
		caseListLocationCode.add(new Case("01cv99999","01-CV-99999"));
		caseListLocationCode.add(new Case("99-mj-51515","99-MJ-51515"));
	}
	
	@Test
	public void testCases() throws Exception{
		List<String> errors = new ArrayList<String>();	
		for (Case c : caseList){
			String actual = DctDocketNumberUtility.normalizeDocketNumber(c.getInputDocketNumber());
			if (!c.getExpectedDocketNumber().equals(actual)){
				errors.add("Expected: "+ c.getExpectedDocketNumber() + " but got: " + actual);
			}
		}
		
		if (errors.size() > 0){
			for (String errorMessage : errors){
				System.out.println(errorMessage);
			}
			Assert.fail();
		}
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testCasesDroppedDocketWithoutLocationCode() throws Exception{

		List<String> errors = new ArrayList<String>();	
		for (Case c : caseListLocationCode){

			thrown.expectMessage("Docket number: " + c.getInputDocketNumber() + " doesn't fit expected pattern");
			
			String actual = DctDocketNumberUtility.normalizeDocketNumber(c.getInputDocketNumber());
			if (!c.getExpectedDocketNumber().equals(actual)){				
				errors.add("Expected: "+ c.getExpectedDocketNumber() + " but got: " + actual);
			}
		}
				
		if (errors.size() > 0){
			for (String errorMessage : errors){
				System.out.println(errorMessage);
			}
			Assert.fail();
		}
	}
	
	private class Case{
		private String inputDocketNumber;
		public String getInputDocketNumber() {
			return inputDocketNumber;
		}

		public String getExpectedDocketNumber() {
			return expectedDocketNumber;
		}

		private String expectedDocketNumber;
		
		Case(String inputDocketNumber, String expectedDocketNumber){
			this.inputDocketNumber = inputDocketNumber;
			this.expectedDocketNumber = expectedDocketNumber;
		}
		
		
	}
	
	
}
