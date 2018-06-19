/**
 Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.trgr.dockets.core.util.NormalizationUtility;


public class NormalizationUtilityTest 
{
	@Test
	public void testCaseSentsitivePrefixFix() {
		String name = "JOHN APPLESEED, DR.";
		String normalizedName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Prefix should retain its case.", "DR. JOHN APPLESEED", normalizedName );
		name = "John Appleseed, Dr.";
		normalizedName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Prefix should retain its case.", "Dr. John Appleseed", normalizedName );
		name = "john appleseed, dr.";
		normalizedName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Prefix should retain its case.", "dr. john appleseed", normalizedName );
		name = "John Appleseed, dR.";
		normalizedName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Prefix should retain its case.", "dR. John Appleseed", normalizedName );
		
	}
	
	@Test
	public void testSplit()
	{
		String suffix = "Jr.";
		Assert.assertTrue("Doesn't end with .", suffix.endsWith("."));
		String[] splitStrings = suffix.split("\\.");
		Assert.assertTrue("Only Jr should be there but length is " + splitStrings.length, splitStrings.length==1);
		Assert.assertEquals("Jr should be without .", "Jr", suffix.substring(0, suffix.lastIndexOf(".")));
		suffix = "Ph.d.";
		splitStrings = suffix.split("\\.");
		Assert.assertTrue("Only Ph and d should be there but length is " + splitStrings.length, splitStrings.length==2);
		String lastName = "lastname,";
		splitStrings = lastName.split("\\s");
		
		Assert.assertTrue("Only Jr should be there but length is " + splitStrings.length, splitStrings.length==1);	
	}
	
	@Test
	public void testEncodedAmp(){
		String name= "My $#amp; Mi, FL";
		Assert.assertTrue(NormalizationUtility.isFirm(name));
	}

	@Test
	public void testSkip()
	{
		String name = "SHOPRITE A/K/A SHOPRITE OF FOREST AND RICHMOND AVENUE, WAKEFERN FOOD CORP.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("SHOPRITE A/K/A SHOPRITE OF FOREST AND RICHMOND AVENUE, WAKEFERN FOOD CORP.", actualname);	
	}
	
	@Test
	public void testApostropheStriping()
	{
		String name = "INTERNATIONAL SECURITIES EXCHANGE, LLC’S PETITION";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("INTERNATIONAL SECURITIES EXCHANGE, LLC’S PETITION", actualname);
		
		name = "INTERNATIONAL SECURITIES EXCHANGE, LLC’S PETITION";
		Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("’", "'");
		name = NormalizationUtility.convertToASCIICharacters(name, mapping.entrySet());
		Assert.assertEquals("INTERNATIONAL SECURITIES EXCHANGE, LLC'S PETITION", name);
		
		actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("INTERNATIONAL SECURITIES EXCHANGE, LLC'S PETITION", actualname);
	}
	
	@Test
	public void testNull()
	{
		String name = null;
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertNull(actualname);	
	}
	
	@Test
	public void testInvestment()
	{
		String name = "FIRST AMERICAN INVESTMENT, (METRIS BANK)";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("FIRST AMERICAN INVESTMENT, (METRIS BANK)", actualname);
	}
	
	@Test
	public void testNameCommas(){
		String name = ",,";
		String normalizedName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("", normalizedName);
	}
	
	
	@Test
	public void testSimpleName()
	{
		String name = "Ayyagari, Suman S.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman S. Ayyagari", actualname);
	}
	
	@Test
	public void testNamesWithSuffix()
	{
		String name = "Ayyagari, Suman Jr.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman Ayyagari, Jr.", actualname);
	}
	
	@Test
	public void testNamesWithSuffix1()
	{
		String name = "Ayyagari, Suman S. Jr.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman S. Ayyagari, Jr.", actualname);
	}
	
	@Test
	public void testNamesWithSuffix2()
	{
		String name = "Ayyagari, Suman Ph.d.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman Ayyagari, Ph.d.", actualname);
	}
	
	@Test
	public void testNamesWithSuffix3()
	{
		String name = "Doan, Quynh Thu \"Gigi\", MD";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Quynh Thu \"Gigi\" Doan, MD", actualName);
	}
	
	@Test
	public void testListofNames()
	{
		String name = "Ayyagari, Suman, Suresh";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Ayyagari, Suman, Suresh", actualname);
	}
	
	@Test
	public void testSingleNameOnly()
	{
		String name = "Ayyagari";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Ayyagari", actualname);
	}
	
	@Test
	public void testNameAlreadyInTheCorrectFormat()
	{
		String name = "Suman S. Ayyagari";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman S. Ayyagari", actualname);
	}
	
	@Test
	public void testNoSpaceAfterPeriodFormat()
	{
		String name = "Suman S.Ayyagari";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman S. Ayyagari", actualname);
	}
	
	@Test
	public void testNoSpaceWillBeGivenForMoreThanOneInitial()
	{
		String name = "Longitude Flash Memory System S.a.r.l.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Longitude Flash Memory System S.a.r.l.", actualname);
	}
	
	@Test
	public void testLastNameFirstNameOnly()
	{
		String name = "Ayyagari, Suman";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman Ayyagari", actualname);
	}
	
	@Test
	public void testLastNameNoSpaceFirstNameOnly()
	{
		String name = " Ayyagari,Suman ";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Suman Ayyagari", actualname);
	}
	
	@Test
	public void testParensInName()
	{
		String name = "Clark, Lois (SLL) ";
		String actualname = NormalizationUtility.removeUnwantedParens(name);
		actualname = NormalizationUtility.normalizeNameWithTitle(actualname);
		Assert.assertEquals("Lois Clark", actualname);
	}
	
	@Test
	public void testProSeInName()
	{
		String name = "MARY ANN HORSTMAN, PRO SE";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MARY ANN HORSTMAN, PRO SE", actualname);
	}	
	
	@Test
	public void testDoctor()
	{
		String name = "Kirsten Gunn, MD.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Kirsten Gunn, MD.", actualname);
	}
	
	@Test
	public void testEsq()
	{
		String name = "MARTIN POLLACK, ESQ";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MARTIN POLLACK, ESQ", actualname);
	}
	
	@Test
	public void testEsquire()
	{
		String name = "JILL A. MASSEY, Esquire";
		String actualname = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("JILL A. MASSEY, Esquire", actualname);
	}
	
	@Test
	public void testSAapostrophy()
	{
		String name = "Wyatt, Sa'Miriam L";
		String actualname = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Sa'Miriam L Wyatt", actualname);
	}
		
	@Test
	public void testPrefixAtEnd(){
		String name = "Frank P. Geraci, Jr. Hon.";
		String actualname = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Hon. Frank P. Geraci, Jr.", actualname);
	}
	
	@Test
	public void testPrefixInMiddle(){
		String name = "Frank P. Geraci, Hon. Jr.";
		String actualname = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Hon. Frank P. Geraci, Jr.", actualname);
	}
	
	@Test
	public void testPrefixInWord(){
		String name = "Frank P. Geraci, Hon.Solo";
		String actualname = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Hon. Solo Frank P. Geraci", actualname);
	}
	
	@Test
	public void testEsq2()
	{
		String name = "MARTIN POLLACK, ESQ.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MARTIN POLLACK, ESQ.", actualname);
	}
	
	@Test
	public void testEsq3()
	{
		String name = "MARTIN POLLACK, ESQS.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MARTIN POLLACK, ESQS.", actualname);
	}
	  
	@Test
	public void testEsqFoo()
	{
		String name = "MARTIN POLLACK, FOO";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("FOO MARTIN POLLACK", actualname);
	}
	
	@Test
	public void testNameNormNoSwitchWithComma()
	{
		String name = "Gameloft, S.A.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name, true, false);
		Assert.assertEquals("Gameloft, S.A.", actualname);
	}
	
	@Test
	public void testnamewith$()
	{
		String name = "CENEX $39.53";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("CENEX $39.53", actualname);
	}

	@Test
	public void testOutNoChange()
	{
		String name = "CARDON OUTREACH";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("CARDON OUTREACH", actualname);
	}

	@Test
	public void testMAPrefix1()
	{
		String name = "MA, Zheng Yue"; //If ma is MA or ma then it is a Title. Bug 189436.
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Zheng Yue, MA", actualname);
	}
	
	@Test
	public void testmaPrefix2()
	{
		String name = "ma, Zheng Yue"; //If ma is MA or ma then it is a Title. Bug 189436.
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Zheng Yue, ma", actualname);
	}
	
	@Test
	public void testMaPrefix2()
	{
		String name = "Ma, Zheng Yue"; //If ma is Ma (upper case M and lower case a - then it is not a Title). Bug 189436.
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Zheng Yue Ma", actualname);
	}
	
	@Test
	public void testMRSufix()
	{
		String name = "SHIHADEH, JOHN, MR.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MR. JOHN SHIHADEH", actualname);
	}
	
	@Test
	public void testMRSufixWithoutComma()
	{
		String name = "SHIHADEH, JOHN MR.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MR. JOHN SHIHADEH", actualname);
	}
	
	@Test
	public void testESQPrefix6()
	{
		String name = "ESQ. * FLORENCE M. RICHARDSON";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("FLORENCE M. RICHARDSON, ESQ.", actualname);
	}
	
	@Test
	public void testESQAsteriskPrefix7()
	{
		String name = "ESQ* FLORENCE M. RICHARDSON";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("FLORENCE M. RICHARDSON, ESQ", actualname);
	}

	@Test
	public void testAttornyName()
	{
		String name = "FRANKLIN SCOTT SPEARS JR";
		String actualname = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("FRANKLIN SCOTT SPEARS, JR", actualname);
	}

	@Test
	public void testESQASPrefix9()
	{
		String name = "ESQAS ALBERT P. KOLAKOWSKI";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ALBERT P. KOLAKOWSKI, ESQAS", actualname);
	}

	@Test
	public void testESQLPrefix11()
	{
		String name = "ESQL HARVEY C. SHAPIRO";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("HARVEY C. SHAPIRO, ESQL", actualname);
	}
	
	@Test
	public void testESQExecutorPrefix12()
	{
		String name = "ESQ. -EXECUTOR MEL HIGGINS";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("EXECUTOR MEL HIGGINS, ESQ.", actualname);
	}
	
	@Test
	public void testESQExecutor2Prefix12()
	{
		String name = "ESQ. MEL HIGGINS EXECUTOR";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MEL HIGGINS EXECUTOR, ESQ.", actualname);
	}
	
	@Test
	public void testESQExecutor3Prefix12()
	{
		String name = "MEL HIGGINS, ESQ. EXECUTOR";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("EXECUTOR MEL HIGGINS, ESQ.", actualname);
	}
	
	@Test
	public void testESQSlashPrefix13()
	{
		String name = "ESQ. /GREENWALD ERNO C. POLL";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("GREENWALD ERNO C. POLL, ESQ.", actualname);
	}
	
	@Test
	public void testESQRefereePrefix14()
	{
		String name = "ESQ. -REFEREE SCOTT GOTTLIEB	";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("REFEREE SCOTT GOTTLIEB, ESQ.", actualname);
	}
	
	@Test
	public void testESQdotPrefix15()
	{
		String name = "ESQ. BERKSHIR GEORGE BERGER";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("BERKSHIR GEORGE BERGER, ESQ.", actualname);
	}

	@Test
	public void testNoCommaNoChange()
	{
		String name = "WENMAR-KULKA CONSTRUCTION J.V";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("WENMAR-KULKA CONSTRUCTION J.V", actualname);
	}

	@Test
	public void testInitials()
	{
		String name = "GOMBOS, AA.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("AA. GOMBOS", actualname);
	}
	
	@Test
	public void testInitialsPE()
	{
		String name = "Andrew Kirchman, PE.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Andrew Kirchman, PE.", actualname);
	}

	@Test
	public void testExampleForCityOF()
	{
		String name = "FORT WORTH, CITY OF";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("CITY OF FORT WORTH", actualname);
	}	
	@Test
	public void testInitialsPEperiods()
	{
		String name = "Andrew Kirchman, P.E.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Andrew Kirchman, P.E.", actualname);
	}
	
	@Test
	public void testInitialsAA()
	{
		String name = "MILNE, A.A.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("A.A. MILNE", actualname);
	}
	
	@Test
	public void testInitialsMustache()
	{
		String name = "MAGNUM P.I.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MAGNUM P.I.", actualname);
	}
	
	@Test
	public void testESQEmbedded()
	{
		String name = "MENESES, ESQ., JORGE OBED V.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("JORGE OBED V. MENESES, ESQ.", actualname);
	}
	
	@Test
	public void testNoChange()
	{
		String name = "FERRARI R.V.";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("FERRARI R.V.", actualname);
	}
	
	@Test
	public void testRomanNumeral()
	{
		String name = "LUCIANO, ANGEL III";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ANGEL LUCIANO, III", actualname);
	}
	
	@Test
	public void testOfficeName()
	{
		String name = "ALAN B.MCGEORGE,OFFICE OF";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		// We have removed the hasOf rule so that these no longer look nice.
		Assert.assertEquals("ALAN B.MCGEORGE,OFFICE OF", actualName); 
	}
	
	@Test
	public void testAKAName()
	{
		String name = "SRL  BRANDOW, DAVID A., JR. A/K/A";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("DAVID A. BRANDOW, JR. A/K/A SRL", actualName);
	}
	
	@Test
	public void testAKAName2()
	{
		String name = "SWIFT, WILLIAM E., JR. A/K/A";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("WILLIAM E. SWIFT, JR. A/K/A", actualName);
	}
	
	@Test
	public void testAddComma1()
	{
		String name = "Collins J Seitz Jr";
		String actualName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Collins J Seitz, Jr", actualName);
	}
	@Test
	public void testAssesorNormalize()
	{
		String name = "ASSESSOR OF MARION COUNTY";
		String actualName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("ASSESSOR OF MARION COUNTY", actualName);
	}
	@Test
	public void testAKAName3()
	{
		String name = "MORALES, ENRIQUE JR A/K/A HENRY MORALES";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ENRIQUE HENRY MORALES MORALES, JR A/K/A", actualName);
	}	
	
	@Test
	public void testJudgeNameWithExtraText()
	{
		String name = "BERNARD J. GRAHAM, COM6";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("COM6 BERNARD J. GRAHAM", actualName);
	}
	
	@Test
	public void testJudgeNameWithSuffix()
	{
		String name = "ALEXANDER W. HUNTER, JR";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ALEXANDER W. HUNTER, JR", actualName);
	}
	
	@Test
	public void testJudgeNameWithSuffix2()
	{
		String name = "ALEXANDER HUNTER, JR";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ALEXANDER HUNTER, JR", actualName);
	}
	
	@Test
	public void testJudgeNameWithSuffixPeriod()
	{
		String name = "ALEXANDER HUNTER, JR.";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ALEXANDER HUNTER, JR.", actualName);
	}
	
	@Test
	public void testPartyNameWithComplexSuffixSRL()
	{
		String name = "FAMIANO, ALEXANDER A. II SRL";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ALEXANDER A. FAMIANO, II SRL", actualName);
	}
	
	@Test
	public void testPartyNameWithComplexSuffixAsst()
	{
		String name = "CHARLES L. SAWYER, ASST. C.A.";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("CHARLES L. SAWYER, ASST. C.A.", actualName);
	}
	
	@Test
	public void testPartyNameWithSuffixSRL()
	{
		String name = "ALEXANDER HUNTER, JR. SRL";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ALEXANDER HUNTER, JR. SRL", actualName);
	}
	
	@Test
	public void testPartyNameWithATTY()
	{
		String name = "JAMES M. NESPER, AMNS TWN ATTY";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("AMNS TWN ATTY JAMES M. NESPER", actualName);
	}
	@Test
	public void testPartyNameWithOFATTY()
	{
		String name = "NYS OFFICE OF ATTY GENERAL";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("NYS OFFICE OF ATTY GENERAL", actualName);
	}
	
	@Test
	public void testPartyNameWithAKAPeriod()
	{
		String name = "JAMES M. NESPER, A.K.A.";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("JAMES M. NESPER, A.K.A.", actualName);
	}
	
	@Test
	public void testPartyNameWithAKAslash()
	{
		String name = "SRL  LESCAULT, AIMEE, A/K/A"; // Changed A/K/A to be a skip word.
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("AIMEE LESCAULT, A/K/A SRL", actualName);
	}
	
	@Test
	public void testPartyNameWithAKA()
	{
		String name = "JAMES M. NESPER, AKA";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("JAMES M. NESPER, AKA", actualName);
	}
	@Test
	public void testPartyNameWithAKASubString()
	{
		String name = "Willie, Biunca Lakay";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Biunca Lakay Willie", actualName);
	}
	
	@Test
	public void testPartyNameWithPROSE()
	{
		String name = "LORY A. PRESTON PRO SE";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("LORY A. PRESTON PRO SE", actualName);
	}
	
	@Test
	public void testPartyNameWithATTORNEYGENERAL()
	{
		String name = "ELIOT SPITZER, ATNY GENERAL";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ATNY GENERAL ELIOT SPITZER", actualName);
	}
	
	@Test
	public void testJudgeNameWith2Suffix()
	{
		String name = "ROY CARLISI, JR., ESQ.";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ROY CARLISI, JR. ESQ.", actualName);
	}
	
	@Test
	public void testPartyNameWithStuff()
	{
		String name = "MICHAEL MILONOPOULOS 02R6234";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MICHAEL MILONOPOULOS 02R6234", actualName);
	}	
	
	@Test
	public void testPartyNameWithStuff2()
	{
		String name = "MICHAEL MILONOPOULOS 2";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("MICHAEL MILONOPOULOS 2", actualName);
	}	
	
	@Test
	public void testPartyNameWithDIN()
	{
		String name = "NATHAN FULLER     DIN#12B1992";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("NATHAN FULLER DIN#12B1992", actualName);
	}
	
	@Test
	public void testPartyNameWithDIN0()
	{
		String name = "ANTOINE ABRAMS    DIN#06R";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ANTOINE ABRAMS DIN#06R", actualName);
	}
	
	@Test
	public void testPartyNameWithDIN1()
	{
		String name = "PHILLIP DIN#07B1202 YATES";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("PHILLIP YATES DIN#07B1202", actualName);
	}
	
	@Test
	public void testPartyNameWithDIN2()
	{
		String name = "REUBEN-DIN #07A0885 MCDOWELL";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("REUBEN MCDOWELL DIN #07A0885", actualName);
	}
	
	@Test
	public void testPartyNameWithDIN3()
	{
		String name = "JERRY DIN #06A0730 RIVERA";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("JERRY RIVERA DIN #06A0730", actualName);
	}
	
    @Test
    public void testPartyNameWithDIN4()
    {
           String name = "SCOTTDIN#98A5199";
           String actualName = NormalizationUtility.normalizeNameWithTitle(name);
           System.out.println(name + " should be " + actualName);
           Assert.assertEquals("SCOTT DIN#98A5199", actualName);
    }
    
    @Test
    public void testPartyNameWithDIN5()
    {
           String name = "SCOTTDIN#98A5199 YATES";
           String actualName = NormalizationUtility.normalizeNameWithTitle(name);
           System.out.println(name + " should be " + actualName);
           Assert.assertEquals("SCOTT YATES DIN#98A5199", actualName);
    }
    
	@Test
    public void testPartyNameWithDIN6()
    {
           String name = "NATHAN FULLER     DIN#12B1992";
           String actualName = NormalizationUtility.normalizeNameWithTitle(name);
           System.out.println(name + " should be " + actualName);
           Assert.assertEquals("NATHAN FULLER DIN#12B1992", actualName);
    } 
 
    @Test
    public void testPartyNameWithDIN7()
    {
        String name = "ANTOINE ABRAMS    DIN#06R";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("ANTOINE ABRAMS DIN#06R", actualName);
    }    
    
    @Test
    public void testPartyNameWithDIN8()
    {
        String name = "DIN # 05B1411 ELMIRA D.O.C.";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("DIN # 05B1411 ELMIRA D.O.C.", actualName);
    }    
    
    @Test
    public void testPartyNameWithNumericLastName()
    {
        String name = "BRYAN BOONE, #09B1089";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("BRYAN BOONE, #09B1089", actualName);
    }    
    
    @Test
    public void testPartyNameWithNumericLastName2()
    {
        String name = "BOONE, BRYAN #09B1089";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("BRYAN BOONE, #09B1089", actualName);
    }    
 
    @Test
    public void testPartyNameWithNumericLastName3()
    {
        String name = "DONTE CURRY, # 953666";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("DONTE CURRY, # 953666", actualName);
    }  
    
    @Test
    public void testPartyNameWithNumericLastNameNoComma()
    {
        String name = "BRYAN BOONE #09B1089";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("BRYAN BOONE #09B1089", actualName);
    }    
    
    @Test
    public void testPartyNameWithNoSpace()
    {
        String name = "KEVIN M.DOWD";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("KEVIN M. DOWD", actualName);
    }   
    
    @Test
    public void testPartyNameWithNoSpace2()
    {
        String name = "M.RITA CONNERTON";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("M. RITA CONNERTON", actualName);
    }   
    
    @Test
    public void testPartyNameWithDashAAG()
    {
        String name = "DONALD E. SHEHIGIAN-AAG";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("DONALD E. SHEHIGIAN, AAG", actualName);
    }   
    @Test
    public void testPartyNameWithCommaAAG()
    {
        String name = "DONALD E. SHEHIGIAN, AAG";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("DONALD E. SHEHIGIAN, AAG", actualName);
    }   
    @Test
    public void testPartyNameWithAAG()
    {
        String name = "AAG DENNIS MCCABE";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("DENNIS MCCABE, AAG", actualName);
    }   
    
    @Test
    public void testPartyNameWithAAGInLastName()
    {
    	  String name = "SUZANNE DEE AAGESON";
          String actualName = NormalizationUtility.normalizeNameWithTitle(name);
          System.out.println(name + " should be " + actualName);
          Assert.assertEquals("SUZANNE DEE AAGESON", actualName);
    }
    
    @Test
    public void testPartyNameWithOLGA()
    {
        String name = "FROZEN, OLGA JR";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("OLGA FROZEN, JR", actualName);
    }   
    
    @Test
    public void testPartyNameWithLG()
    {
        String name = "JOHN DAGON LG";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("JOHN DAGON, LG", actualName);
    }   
    @Test
    public void testPartyNameWithCommaLG()
    {
        String name = "JOHN DAGON, LG";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("JOHN DAGON, LG", actualName);
    }   


    @Test
    public void testPartyNameWithStars()
    {
        String name = "ANTOINE **ABRAMS*";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("ANTOINE ABRAMS", actualName);
    }     
    @Test
    public void testPartyNameWithQuestionMarks()
    {
        String name = "ROCK HILL BAKEHOUSE ??????";
        String actualName = NormalizationUtility.normalizeNameWithTitle(name);
        System.out.println(name + " should be " + actualName);
        Assert.assertEquals("ROCK HILL BAKEHOUSE", actualName);
    }     
    
    @Test
	public void testValidNameAsPhoneNumber()
	{
		String name = "914 949-2909";
		boolean validName = NormalizationUtility.isValidName(name);
		Assert.assertFalse(validName);
	}
    
    @Test
	public void testRemoveNumberFromName()
	{
		String name = "(914)949-2909";
		String actualName = NormalizationUtility.removeNumberFromName(name);
		Assert.assertEquals("", actualName);
	}
    
    @Test
  	public void testValidNameAsNull()
  	{
  		String name = null;
  		boolean validName = NormalizationUtility.isValidName(name);
  		Assert.assertFalse(validName);
  	}
    
    @Test
  	public void testValidNameAsNumbers()
  	{
  		String name = "12345";
  		boolean validName = NormalizationUtility.isValidName(name);
  		Assert.assertFalse(validName);
  	}
    
    @Test
	public void testValidNameAsName()
	{
		String name = "Kirsten Gunn";
		boolean validName = NormalizationUtility.isValidName(name);
		Assert.assertTrue(validName);
	}
    
    @Test
	public void testPartyAsPhoneNumber()
	{
		String name = "914 949-2909";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("", actualName);
	}
    
	@Test
	public void testPartyAsPhoneNumber1()
	{
		String name = "949 2909";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("", actualName);
	}
	
	@Test
	public void testPartyAsPhoneNumber2() // Currently removing phone numbers only
	{
		String name = "Angel 867-5309";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Angel 867-5309", actualName);
	}
	
	@Test
	public void testPartyAsPhoneNumber3()
	{
		String name = "1 914 949 2909";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("", actualName);
	}		
	
	@Test
	public void testIsFirm()
	{
		String name = "Clark, Lois (SLL) ";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertFalse(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	@Test
	public void testIsFirmSA(){
		assertEquals(NormalizationUtility.isFirm("Gameloft, SA"), true);
	}
	
	@Test
	public void testIsFirmSA2Period(){
		assertEquals(NormalizationUtility.isFirm("Gameloft, S.A."), true);
	}
	
	@Test
	public void testIsFirmSA1Period(){
		assertEquals(NormalizationUtility.isFirm("Gameloft, S.A"), true);
	}
	
	@Test
	public void testIsFirmSE(){
		assertEquals(NormalizationUtility.isFirm("Gameloft, SE"), true);
	}
	
	@Test
	public void testIsFirmSE2Period(){
		assertEquals(NormalizationUtility.isFirm("Gameloft, S.E."), true);
	}
	
	@Test
	public void testIsFirmSE1Period(){
		assertEquals(NormalizationUtility.isFirm("Gameloft, S.E"), true);
	}
	
	@Test
	public void testIsFirmProSe(){
		assertEquals(NormalizationUtility.isFirm("Pro Se"), false);
	}
	
	@Test
	public void testIsFirmNull(){
		String name = null;
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertFalse(retVal);
	}
	
	@Test
	public void testIsFirmWithOf(){
		String name = "ORDER OF MAGNITUDE";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithDEPT(){
		String name = "ORDER OF DEPT MAGNITUDE";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithCompany(){
		String name = "Company Firm";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithETSpaceAL(){
		String name = "Gunn, McPherson, ET AL";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithETAL(){
		String name = "UZELAC, STEVEN,ETAL";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithINC(){
		String name = "PEA BRIDGE REALTY, INC";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithASSOCIATES(){
		String name = "KIRSTEN GUNN AND ASSOCIATES";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithTOWN()
	{
		String name = "NORTH CASTLE, TOWN OF, ET AL";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testOfName()
	{
		String name = "NORTH CASTLE, TOWN OF, ET AL";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		// We have removed the hasOf rule so that these no longer look nice.
		Assert.assertEquals("NORTH CASTLE, TOWN OF, ET AL", actualName); // TOWN OF NORTH CASTLE, ET AL
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithOFC()
	{
		String name = "MARK E. SEITELMAN, LAW OFC OF";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testOfOFCName()
	{
		String name = "MARK E. SEITELMAN, LAW OFC OF";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		// We have removed the hasOf rule so that these no longer look nice.
		Assert.assertEquals("MARK E. SEITELMAN, LAW OFC OF", actualName); // LAW OFC OF MARK E. SEITELMAN
	}
	
	@Test
	public void testOfNameETAL()
	{
		String name = "NORTH CASTLE, TOWN OF, ET. AL.";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		// We have removed the hasOf rule so that these no longer look nice.
		Assert.assertEquals("NORTH CASTLE, TOWN OF, ET. AL.", actualName); // TOWN OF NORTH CASTLE, ET. AL.
	}
	@Test
	public void testIsNotFirmWithDENISOF()
	{
		String name = "ALEXIS DENISOF";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertFalse(name + " was supposed to be a firm.", retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	@Test
	public void testEndswithOf()
	{
		String name = "DENISOF, ELOF";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("ELOF DENISOF", actualName);
	}
	
	@Test
	public void testLLP()
	{
		String name = "KIRSTEN GUNN L.L.P.";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("KIRSTEN GUNN L.L.P.", actualName);
	}
	
	@Test
	public void testIsFirmWithCORPORATION()
	{
		String name = "650 PARK AVENUE CORPORATION";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithCORPERATE()
	{
		String name = "CORPERATE SQUARE";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", !retVal);
	}
	
	@Test
	public void testIsFirmWithCORPeol()
	{
		String name = "DOCTOR WHO CORP";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithCO()
	{
		String name = "ALLSTATE INS CO";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithCOMPANY()
	{
		String name = "ALLSTATE INSURANCE COMPANY";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}	
	@Test
	public void testIsFirmWithCITY()
	{
		String name = "YONKERS, CITY OF, ETAL";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}	
	
	@Test
	public void testCITYOF()
	{
		String name = "YONKERS, CITY OF, ETAL";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		// We have removed the hasOf rule so that these no longer look nice.
		Assert.assertEquals("YONKERS, CITY OF, ETAL", actualName);
	}	
	
	@Test
	public void testIsFirmWithTOWNOrdered()
	{
		String name = "TOWN OF ORANGETOWN";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}	
	
	@Test
	public void testTOWNOF()
	{
		String name = "TOWN OF ORANGETOWN";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("TOWN OF ORANGETOWN", actualName);
	}
	
	@Test
	public void testIsFirmWithOF()
	{
		String name = "NEW YORK, STATE OF";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testSTATEWithOF()
	{
		String name = "NEW YORK, STATE OF";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		// We have removed the hasOf rule so that these no longer look nice.

		Assert.assertEquals("NEW YORK, STATE OF", actualName);
	}
	
	@Test
	public void testIsFirmWithINCORPORATED()
	{
		String name = "DESIGNATRONICS INCORPORATED";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}	
	
	@Test
	public void testIsFirmWithDEPARTMENT()
	{
		String name = "NEW YORK DEPARTMENT OF";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithPLLC()
	{
		String name = "KIRSTEN GUNN, PLLC";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	
	@Test
	public void testIsFirmWithCORP()
	{
		String name = "CARINCI, ROSEMARIE &ALFRED    WAKEFERN FOOD CORP.,ETAL";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmLP()
	{
		String name = "Redcats Usa L.P. (F/K/A Brylane, L.P.)";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmStateStreet()
	{
		String name = "215 W State Street";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(!retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	@Test
	public void testIsFirmStateRoad()
	{
		String name = "215 W State Road";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(!retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	@Test
	public void testIsFirmStateAve()
	{
		String name = "215 W State Ave";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(!retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	@Test
	public void testIsFirmStateAvenue()
	{
		String name = "215 W State Avenue";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(!retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	@Test
	public void testIsFirmStatehouse()
	{
		String name = "215 West Statehouse";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(!retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	
	@Test
	public void testIsFirmWithCoEnding()
	{
		String name = "Gunn, McPherson, CO";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithInsCoEnding()
	{
		String name = "ALLSTATE INSURANCE COMPANY";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithINSURANCEEnding()
	{
		String name = "AMERICAN TRANSIT INSURANCE";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithGROUP()
	{
		String name = "ETAL MID HUDSON MEDICAL GROUP";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithMANAGEMENT()
	{
		String name = "ALANA PROPERTY MANAGEMENT";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithSERVICE()
	{
		String name = "BENEFICIAL HOMEOWNER SERVICE";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithSERVICES()
	{
		String name = "INC CANON FINANCIAL SERVICES";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithSTATE()
	{
		String name = "NEW YORK STATE";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithOFFICE()
	{
		String name = "BROOKFIELD OFFICE PROPERTIES";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testOFFICE()
	{
		String name = "BROOKFIELD OFFICE PROPERTIES";
		String actualName = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("BROOKFIELD OFFICE PROPERTIES", actualName);
	}
	
	@Test
	public void testIsFirmWithPC()
	{
		String name = "DALAL MEDICAL PC";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithPCWithTrailingSpace()
	{
		String name = "PITZER SNODGRASS PC  ";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithLTD()
	{
		String name = "REALTY, LTD. G.B.H.";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithLIMITED()
	{
		String name = "GBR VALLEY COTTAGE LIMITED";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithPARTNERS()
	{
		String name = "HOWLANDS LAKE PARTNERS";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithCOMPANIES()
	{
		String name = "TJX COMPANIES";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithFUND()
	{
		String name = "STABILIS FUND";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	@Test
	public void testIsFirmWithCommaCoEnding()
	{
		String name = "Gunn, McPherson,CO";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testIsFirmWithCommaCoAtty()
	{
		String name = "Gunn Co Atty";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertFalse(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is not a firm.");
	}
	
	
	@Test
	public void testIsFirmNegative()
	{
		String name = "Gunn, McPherson";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertFalse(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is not a firm.");
	}
	@Test
	public void testCityOF(){
		String name = "FORT WORTH, CITY OF";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertFalse(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is not a firm.");					
	}
	@Test
	public void testIsFirmWithCoPeriod()
	{
		String name = "Gunn CO. and McPherson";
		boolean retVal = NormalizationUtility.isFirm(name);
		Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
		System.out.println(name  + " is a firm.");
	}
	
	@Test
	public void testRemoveComment()
	{
		String address= "SEE COMMENT SCREEN FOR NAME 1500 Pennsylvania Avenue, Washington DC";
		String newAddress = "";
		
		Assert.assertEquals(newAddress, NormalizationUtility.stripComment(address));
		System.out.println("\"" +address  + "\" comment removed");
	}
	
	@Test
	public void testNoRemoval()
	{
		String address = "SEE NO EVIL WAY, Minneapolis, MN";
		Assert.assertEquals(address, NormalizationUtility.stripComment(address));
		System.out.println("\"" +address  + "\" comment not removed");
	}
	
	@Test
	public void testRemovalSecondComment(){
		String address= "1502 ADDRESS & PHONE NO HAMMER WAY";
		String newAddress="";
		Assert.assertEquals(newAddress, NormalizationUtility.stripComment(address));
	}
	
	@Test
	public void testNoRemovalSecondComment(){
		String address = "RODGERS & HAMMERSTEIN AVENUE";
		Assert.assertEquals(address, NormalizationUtility.stripComment(address));
	}
	
	@Test
	public void removeAllComments(){
		String address = "SEE COMMENT SCREEN FOR NAME BANANARAMA ADDRESS & PHONE NO WAY";
		String newAddress = "";
		Assert.assertEquals(newAddress, NormalizationUtility.stripComment(address));
	}
	
	@Test
	public void removeAllCommentsPartTwo(){
		String address = "SEE COMMENT SCREEN FOR NAME BANANARAMA ADDRESS &amp; PHONE NO WAY";
		String newAddress = "";
		Assert.assertEquals(newAddress, NormalizationUtility.stripComment(address));
	}
	
	
	@Test
	public void testShuffleSuffix(){
		String name = "ROBERT A (JR) MCALLISTER";
		String newName = "ROBERT A MCALLISTER JR";
		String actualName = NormalizationUtility.shuffleSuffix(name);
		Assert.assertEquals(newName, actualName);					
	}
	
	@Test
	public void testNoShuffleSuffix(){
		String name = "ROBERT A JR MCALLISTER";
		String newName = "ROBERT A JR MCALLISTER";
		Assert.assertEquals(newName, NormalizationUtility.shuffleSuffix(name));				
	}
	
	@Test
	public void testNoShuffleSuffixWithParens(){
		String name = "ROBERT A (COOL STORY BRO) MCALLISTER";
		String newName = "ROBERT A (COOL STORY BRO) MCALLISTER";
		Assert.assertEquals(newName, NormalizationUtility.shuffleSuffix(name));				
	}
	
	@Test
	public void testNameWithAmount()
	{
		String name = "$33,560.00 US Dollars";
		Assert.assertFalse(NormalizationUtility.isSkip(name));
	}
	
	@Test
	public void testFirmWithOrg()
	{
		String name = "SHEYENNE CARE CENTER, A NON-PROFIT ORG.";
		Assert.assertFalse(NormalizationUtility.isSkip(name));
	}
	
	@Test
	public void testNormalizeTextContainsBank(){
		final Map<Integer, String> testMap = new HashMap<Integer, String>();
		final Map<Integer, String> resultMap = new HashMap<Integer, String>();
		
		testMap.put(1, "Joe, Banks");
		testMap.put(2, "Eubank, Joe");
		testMap.put(3, "Banks, Joe");
		testMap.put(4, "bank, Joe");
		testMap.put(5, "banks, Joe");
		testMap.put(6, "eubank, Joe");
		testMap.put(7, "bank of New York");
		testMap.put(8, "Bank of New York");
		testMap.put(9, "US Bank");
		testMap.put(10, "Citibank, N.A.");
		
		resultMap.put(1, "Banks Joe");
		resultMap.put(2, "Joe Eubank");
		resultMap.put(3, "Joe Banks");
		resultMap.put(4, "bank, Joe");
		resultMap.put(5, "Joe banks");
		resultMap.put(6, "Joe eubank");
		resultMap.put(7, "bank of New York");
		resultMap.put(8, "Bank of New York");
		resultMap.put(9, "US Bank");
		resultMap.put(10, "Citibank, N.A.");
		
		for(int i=1; i<=testMap.size(); i++){
				Assert.assertEquals(resultMap.get(i), NormalizationUtility.normalizePartyName(testMap.get(i)));
		}
	}
	
	@Test
	public void normalizeJudgeNameBank(){
		final Map<String, String> testMap = new HashMap<String, String>();
		
		testMap.put("NO  ASSIGNED", "No Judge Assigned");
		testMap.put("Smith, Alex", "Alex Smith");
		
		for (Map.Entry<String, String> entry : testMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			Assert.assertEquals(value, NormalizationUtility.normalizeJudgeName(key, true));
		}
	}
	
	@Test
	public void normalizeSmallClaimsJudge(){
		String name = "SMALL CLAIMS JUDGE, NORTH";
		String newName = "SMALL CLAIMS JUDGE, NORTH";
		
		Assert.assertEquals(newName, NormalizationUtility.normalizeJudgeName(name, true));
	}
	
	@Test
	public void doNotNormalizeDollarNumberCommaNumberTest1(){
		String name = "$3, 688 IN U S CURRENCY";
		String newName = "$3, 688 IN U S CURRENCY";
		
		Assert.assertEquals(newName, NormalizationUtility.normalizePartyName(name));
	}
	
	@Test
	public void doNotNormalizeDollarNumberCommaNumberTest2(){
		String name = "$3,688 IN U S CURRENCY";
		String newName = "$3,688 IN U S CURRENCY";
		
		Assert.assertEquals(newName, NormalizationUtility.normalizePartyName(name));
	}
	
	 @Test
	 public void testSuffixTwoWords()
	 {
	  String name = "JENNY LEAK, ET ANO";
	  String actualname = NormalizationUtility.normalizeNameWithTitle(name);
	  Assert.assertEquals("JENNY LEAK, ET ANO", actualname); 
	  name = "jenny leak, et ano";
	  actualname = NormalizationUtility.normalizeNameWithTitle(name);
	  Assert.assertEquals("jenny leak, et ano", actualname);
	  name = "Jenny Leak, Et Ano";
	  actualname = NormalizationUtility.normalizeNameWithTitle(name);
	  Assert.assertEquals("Jenny Leak, Et Ano", actualname);
	 }
	@Test
	public void testNameTwoComma1()
	{
		String name = "Chase, John M., Personal Representative,";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("Chase, John M., Personal Representative", actualname);	
	}
	@Test
	public void testNameTwoComma2()
	{
		String name = "DANIELA KLEBECK, LISA R BARE, AMERICA";
		String actualname = NormalizationUtility.normalizeNameWithTitle(name);
		Assert.assertEquals("DANIELA KLEBECK, LISA R BARE, AMERICA", actualname);	
	}
	
	// Test with 2 $'s because this a case that happens in PB and should not be normalized.
	@Test
	public void testNameCurrency(){
		String name = "$21,000 U.S. Currency";
		String actualName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("$21,000 U.S. Currency", actualName);
		name = "$$21,000 U.S. Currency";
		actualName = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("$$21,000 U.S. Currency", actualName);  
	}		
	
	@Test
	public void testStateRoadFoods(){
		String name= "State Road Foods, LLC";
		Assert.assertTrue(NormalizationUtility.isFirm(name));
	}
	
	@Test
	public void testAsAssigneeOf(){
		String name = "BOND W THOMAS DC PA  AS ASSIGNEE OF- FOR  GUZMAN, KAREN";
		String actual = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("BOND W THOMAS DC PA AS ASSIGNEE OF- FOR GUZMAN, KAREN", actual);
	}
	
	@Test
	public void testEsqNames() {
		String name = "Taylir K. Linden Esq";
		String actual = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Taylir K. Linden, Esq", actual);
	
		name = "Maryellen O$#apos;Brien Esq";
		actual = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("Maryellen O$#apos;Brien, Esq", actual);
	}
	
	@Test
	public void testOfficerName(){
		String name = "DISAIVO, OFFICER";
		String actual = NormalizationUtility.normalizePartyName(name);
		Assert.assertEquals("OFFICER DISAIVO", actual);
	}
}

