/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/

package com.trgr.dockets.core.service;

import org.junit.Test;

import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.util.DocketServiceUtil;

import org.junit.Assert;


public class DocketServiceUtilTest {
	
	@Test
	public void testJPMLLegacyId(){
		String expected = "JPML2133";
		SourceDocketMetadata sdm = new SourceDocketMetadata("MdlNo2133");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFEDJPML");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testWoMJPMLLegacyId(){
		String expected = "JPML2133";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2133");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFEDJPML");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCaStanislausLegacyId(){
		String expected = "CASTANISLAUS2133";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2133");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASTANIS");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNYDnstateLegacyId(){
		String expected = "NYNEWYORK12345672013";
		SourceDocketMetadata sdm = new SourceDocketMetadata("1234567/2013");
		sdm.setCountyName("New York");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DNYDOWNSTATE");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNYUpstateLegacyId(){
		String expected = "NYNIAGARA12345672013";
		SourceDocketMetadata sdm = new SourceDocketMetadata("1234567/2013");
		sdm.setCountyName("Niagara");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DNYDOWNSTATE");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNYCntyClerkLegacyId(){
		String expected = "NYNEWYORKCC1234562013";
		SourceDocketMetadata sdm = new SourceDocketMetadata("123456/2013");
		sdm.setCountyName("New York");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DNYCNTYCLERK");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNDSTCTSLegacyId(){
		String expected = "NDADAMSCIVIL012014C12345";
		SourceDocketMetadata sdm = new SourceDocketMetadata("01-2014-C-12345");
		sdm.setCountyName("Adams");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DNDSTCTS");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOKSTCTSLegacyId(){
		String expected = "OKMCCURTAINWL-2014-00017";
		SourceDocketMetadata sdm = new SourceDocketMetadata("WL-2014-00017");
		sdm.setCountyName("McCurtain");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOKKELPRO");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testKSSHAWNEELegacyId(){
		String expected = "KSSHAWNEE2014-SC-000193";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2014-SC-000193");
		sdm.setCountyName("Shawnee");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DKSSHAWNEE");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testTXFTBENDLegacyId(){
		String expected = "TXFORTBEND79DCR011218";
		SourceDocketMetadata sdm = new SourceDocketMetadata("79-DCR-011218");
		sdm.setCountyName("FortBend");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DTXFTBEND");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testTXWLMSONLegacyId(){
		String expected = "TXWLMSON99475F368FC2.10312013";
		SourceDocketMetadata sdm = new SourceDocketMetadata("99-475-F368-FC2");
		sdm.setCountyName("Williamson");
		sdm.setCaseType("Civil");
		sdm.setFilingDate("10312013");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DTXWLMSON");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDECHANCERYLegacyId(){
		String expected = "DECHANCERY9991";
		SourceDocketMetadata sdm = new SourceDocketMetadata("9991");
		sdm.setCountyName("Chancery");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DDECHANCERY");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
		
		expected = "DECHANCERY2017-9991";
		sdm = new SourceDocketMetadata("2017-9991");
		sdm.setCountyName("Chancery");
		sdm.setCaseType("Civil");
		actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DDECHANCERY");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOHLAKELegacyId(){
		String expected = "OHLAKE2001L018OA";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2001-L-018OA");
		sdm.setCountyName("Ohlake");
		sdm.setCaseType("");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOHLAKE");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOHLICKINGLegacyId(){
		String expected = "OHLICKING2007CA00127";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2007-CA-00127");
		sdm.setCountyName("Ohlicking");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOHLICKING");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testINTRIALLegacyId(){
		String expected = "INTRIAL49-T-10-0803-TA-00022";
		SourceDocketMetadata sdm = new SourceDocketMetadata("49-T-10-0803-TA-00022");
		sdm.setCountyName("trial");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DINTRIAL");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testINTAXLegacyId(){
		String expected = "INTAX49-T-10-0803-TA-00022";
		SourceDocketMetadata sdm = new SourceDocketMetadata("49-T-10-0803-TA-00022");
		sdm.setCountyName("tax");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DINTAX");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testINAPPLegacyId(){
		String expected = "INAPP82-A-01-0310-CV-00421";
		SourceDocketMetadata sdm = new SourceDocketMetadata("82-A-01-0310-CV-00421");
		sdm.setCountyName("APP");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DINAPP");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testINSCTLegacyId(){
		String expected = "INSCT49-T-10-0803-TA-00022";
		SourceDocketMetadata sdm = new SourceDocketMetadata("49-T-10-0803-TA-00022");
		sdm.setCountyName("Supreme");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DINSCT");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testPTABLegacyId(){
		String expected = "PTABIPR2013-00004";
		SourceDocketMetadata sdm = new SourceDocketMetadata("IPR2013-00004");
		sdm.setCountyName("PTAB");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFEDPTAB");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testILCOOKLegacyId(){
		String expected = "2015-D-079031ILCOOK";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2015-D-079031");
		sdm.setCountyName("Cook");
		sdm.setCaseType("Civil");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DILCOOK");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testFLPINELLASLegacyId(){
		String expected = "FLPINELLAS10000033ES";
		SourceDocketMetadata sdm = new SourceDocketMetadata("10-000033-ES");
		sdm.setCountyName("Pinellas");
		sdm.setCaseType("Probate");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLPINELLAS");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}	
	@Test
	public void testPABUCKSLegacyId(){
		String expected = "PABUCKS201560780";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2015-60780");
		sdm.setCountyName("BUCKS");
		sdm.setCaseType("CIVIL");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DPABUCKS");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}	
	@Test
	public void testPABUCKSMatterCodeLegacyId(){
		String expected = "PABUCKS201548516C";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2015-48516");
		sdm.setCountyName("BUCKS");
		sdm.setCaseType("CIVIL");
		sdm.setMiscellaneous("C");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DPABUCKS");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testOHKNOXLegacyIdLegacyId(){
		String expected = "OHKNOX02EX000035";
		SourceDocketMetadata sdm = new SourceDocketMetadata("02-EX-000035");
		sdm.setCountyName("KNOX");
		sdm.setCaseType("CIVIL");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOHKNOX");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testPAALLEGHENYLegacyId(){
		String expected = "PAALLEGHENYDTD-15-007430DELINQUENTTAXDOCKET";
		SourceDocketMetadata sdm = new SourceDocketMetadata("DTD-15-007430");
		sdm.setCountyName("ALLEGHENY");
		sdm.setCaseType("CIVIL");
		sdm.setMiscellaneous("DELINQUENTTAXDOCKET");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DPAALLEG");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}	
	
	@Test
	public void testGAFULTONLegacyId(){
		String expected = "GAFULTON2015CV255506";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2015CV255506");
		sdm.setCountyName("FULTON");
		sdm.setCaseType("CIVIL");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DGAFULTON");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}	
	@Test
	public void testFLLEONLegacyId(){
		String expected = "FLLEON372011SC003125";
		SourceDocketMetadata sdm = new SourceDocketMetadata("372011-SC-003125");
		sdm.setCountyName("LEON");
		sdm.setCaseType("CIVIL");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLLEON");
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}	
		
	@Test
	public void testKsDctLegacyId(){
		String expected = "KS-DCTB:16MJ90021";
		SourceDocketMetadata sdm = new SourceDocketMetadata("B:16mj90021");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DKSDCT");
		Assert.assertEquals(expected, actual);
	}
	@Test
	public void testMtDctLegacyId(){
		String expected = "MT-DCT9:15PO05243";
		SourceDocketMetadata sdm = new SourceDocketMetadata("9:15po05243");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DMTDCT");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testNYDctLegacyId(){
		String expected = "NY-WDCT1:01CV00629";
		SourceDocketMetadata sdm = new SourceDocketMetadata("1:01-cv-00629");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DNYWDCT");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCASantaCruzLegacyId(){
		String expected = "CASANTACRUZCISCV174968";
		SourceDocketMetadata sdm = new SourceDocketMetadata("CISCV174968");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCRUZ");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraLegacyId(){
		String expected = "CASANTACLARA1-14-CV-270226";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2014-1-CV-270226");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraLegacyIdOldDktNum(){
		String expected = "CASANTACLARA1-14-CV-270226";
		SourceDocketMetadata sdm = new SourceDocketMetadata("1-14-CV-270226");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraFourDigitLead(){
		String expected = "CASANTACLARA1-92-FL-024730";
		SourceDocketMetadata sdm = new SourceDocketMetadata("1992FL024730");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraThreeDigitLead(){
		String expected = "CASANTACLARA1-13-CV-240891";
		SourceDocketMetadata sdm = new SourceDocketMetadata("113cv240891");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraTwoDigitLead(){
		String expected = "CASANTACLARA1-15-CH-006734";
		SourceDocketMetadata sdm = new SourceDocketMetadata("15CH006734");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraTwoDigitDashLead(){
		String expected = "CASANTACLARA1-15-PR-177824";
		SourceDocketMetadata sdm = new SourceDocketMetadata("15-PR-177824");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraMissingDashAfterCasetype(){
		String expected = "CASANTACLARA1-15-PR-177822";
		SourceDocketMetadata sdm = new SourceDocketMetadata("1-15-PR177822");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASantaClaraMissingDashes(){
		String expected = "CASANTACLARA6-15-FL-015671";
		SourceDocketMetadata sdm = new SourceDocketMetadata("20156FL015671");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANTCL");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGADeKalb(){
		String expected = "GADEKALB15CV10767";
		SourceDocketMetadata sdm = new SourceDocketMetadata("15CV10767");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DGADEKALB");
		Assert.assertEquals("Superior format", expected, actual);
		
		expected = "GADEKALB15A57496";
		sdm = new SourceDocketMetadata("15A57496");
		actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DGADEKALB");
		Assert.assertEquals("State format", expected, actual);
		
		expected = "GADEKALB15M57670";
		sdm = new SourceDocketMetadata("15M57670");
		actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DGADEKALB");
		Assert.assertEquals("Magistrate format", expected, actual);
	}
	
	@Test
	public void testFlBroward(){
		String expected = "FLBROWARDCOCE17005558";
		SourceDocketMetadata sdm = new SourceDocketMetadata("COCE17005558");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLBROWARD");
		Assert.assertEquals(expected, actual);
		
		expected = "FLBROWARD17004103CF10A";
		sdm = new SourceDocketMetadata("17004103CF10A");
		actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLBROWARD");
		Assert.assertEquals(expected, actual);
		
		expected = "FLBROWARD17003224MM10A";
		sdm = new SourceDocketMetadata("17003224MM10A");
		actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLBROWARD");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCASanJoaquin(){
		String expected1 = "CASANJOAQUIN39-2010-00246145-CL-CL";
		SourceDocketMetadata sdm = new SourceDocketMetadata("39-2010-00246145-CL-CL");
		String actual1 = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANJOAQ");
		Assert.assertEquals(expected1, actual1);
		
		String expected2 = "CASANJOAQUINTRA-CV-LCCR-2010-0012472";
		sdm = new SourceDocketMetadata("TRA-CV-LCCR-2010-0012472");
		String actual2 = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANJOAQ");
		Assert.assertEquals(expected2, actual2);
	}

	@Test
	public void testCASanMateo(){
		String expected  = "CASANMATEOSCN103560";
		SourceDocketMetadata sdm = new SourceDocketMetadata("SCN103560");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCASANMATEO");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testCAButte(){
		String expected1 = "CABUTTE137104";
		SourceDocketMetadata sdm = new SourceDocketMetadata("137104");
		String actual1 = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCABUTTE");
		Assert.assertEquals(expected1, actual1);
	}
	
	@Test
	public void testOHLake(){
		String expected = "OHLAKE17CV000123";
		SourceDocketMetadata sdm = new SourceDocketMetadata("17CV000123");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOHLAKE");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOHLakeRemoveDas(){
		String expected = "OHLAKE2014L049";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2014-L-049");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOHLAKE");
		Assert.assertEquals(expected, actual);
	}
	
	
	@Test
	public void testCAFresno(){
		String expected = "CAFRESNOF16906886";
		SourceDocketMetadata sdm = new SourceDocketMetadata("F16906886");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DCAFRESNO");
		Assert.assertEquals(expected, actual);
		
	}
	
	@Test
	public void testOHMontgomery(){
		String expected = "OHMONTGOMERY2007CV03124";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2007CV03124");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOHMONTGY");
		Assert.assertEquals(expected, actual);
		
	}
	
	@Test
	public void testFLALACHUA(){
		String expected = "FLALACHUA2005SC002305";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2005SC002305");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLALACHUA");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testFLLake(){
		String expected = "FLLAKE2009CR00092";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2009CR00092");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLLAKE");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testFLLakeWith35DashAndExtraCharsAtTheEnd(){
		String expected = "FLLAKE352005CV00087AXXXXX";
		SourceDocketMetadata sdm = new SourceDocketMetadata("35-2005-CV-00087-AXXX-XX");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DFLLAKE");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testOHSCT(){
		String expected = "OHSCT2016-0313";
		SourceDocketMetadata sdm = new SourceDocketMetadata("2016-0313");
		String actual = DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sdm, "N_DOHSCT");
		Assert.assertEquals(expected, actual);
	}
}
