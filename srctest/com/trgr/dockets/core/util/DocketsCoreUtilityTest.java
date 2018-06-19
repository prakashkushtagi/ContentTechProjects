package com.trgr.dockets.core.util;

/*
 * Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Court;
import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.DroppedDocket;
import com.trgr.dockets.core.exception.SourceContentLoaderException;

/**
 * Test dockets number parsing in case of lci deletes for FBR and JPML
 * 
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com)
 *
 */
public class DocketsCoreUtilityTest {

	@Before
	public void setUp() {

	}

	/**
	 * In case of bankruptcy "2012bk014174" for delete LCI/Oragone delete scenario docket number will always be with out location code i.e "nn:"
	 * 
	 * @param docketNumber
	 * @return
	 */
	@Test
	public void docketNumberParsingFBRTest() {
		String docketsNumber = "2012FBR1234567";
		HashMap<String, String> values = DocketsCoreUtility.docketNumberParsing(docketsNumber, "FBR");
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.product.toString()).equalsIgnoreCase("FBR"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.year.toString()).equalsIgnoreCase("2012"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.sequenceNo.toString()).equalsIgnoreCase("1234567"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.docketNo.toString()).equalsIgnoreCase("2012FBR1234567"));
	}

	@Test
	public void docketNumberParsingFBROragoneTest() {
		String docketsNumber = "2012FBR1234567";
		HashMap<String, String> values = DocketsCoreUtility.docketNumberParsing(docketsNumber, "FBR");
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.product.toString()).equalsIgnoreCase("FBR"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.year.toString()).equalsIgnoreCase("2012"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.sequenceNo.toString()).equalsIgnoreCase("1234567"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.docketNo.toString()).equalsIgnoreCase("2012FBR1234567"));
	}

	/**
	 * if product code doesnt match any of the expected project use FBR as default product
	 */
	@Test
	public void docketNumberParsingFBRdefaultTest() {
		String docketsNumber = "2012FBR1234567";
		HashMap<String, String> values = DocketsCoreUtility.docketNumberParsing(docketsNumber, "sadfasdfsdf");
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.product.toString()).equalsIgnoreCase("FBR"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.year.toString()).equalsIgnoreCase("2012"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.sequenceNo.toString()).equalsIgnoreCase("1234567"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.docketNo.toString()).equalsIgnoreCase("2012FBR1234567"));
	}

	@Test
	public void docketNumberParsingJpmlTest() {
		String docketsNumber = "MDLNo1234567";
		HashMap<String, String> values = DocketsCoreUtility.docketNumberParsing(docketsNumber, "JPML");
		Assert.notNull(values);
		Assert.notEmpty(values);
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.sequenceNo.toString()).equalsIgnoreCase("1234567"));
		Assert.isTrue(values.get(DocketsCoreUtility.KeysEnum.docketNo.toString()).equalsIgnoreCase("MDLNo1234567"));
	}

	@Test
	public void getEventsStartTimeStampTest() {
		String formatedDate = null;
		try {
			formatedDate = DocketsCoreUtility.getEventsStartTimeStamp("05092012021156");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Assert.isTrue(formatedDate.equalsIgnoreCase("09/05/2012 02:11:56.000000 AM"));
	}

	@Test
	public void getEventsCurrentTimeStampTest() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
		String strDate = sdf.format(cal.getTime());

		/* gets current timestamp in events date format i.e MM/dd/yyyy 24HH:mm:ss.SSSSSS a */
		String formattedDate = null;

		formattedDate = DocketsCoreUtility.getEventsCurrentTimeStamp();

		if (formattedDate.length() >= 16) // contains ms
		{
			Assert.isTrue(strDate.equals(formattedDate.substring(0, 16)), formattedDate + " should be " + strDate);
		} else {
			Assert.isTrue(strDate.equals(formattedDate), formattedDate + " failed. Should be " + strDate);
		}
	}

	@Test
	public void createAcquisitionTimeStampTest() {
		String formattedDate = null;
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(2012, 8, 5, 2, 11, 56);
		Date expectedDate = c1.getTime();

		formattedDate = DocketsCoreUtility.createAcquisitionTimeStamp(expectedDate, "JPML");

		Assert.isTrue(formattedDate.equalsIgnoreCase("20120905021156"), formattedDate);
	}

	@Test
	public void createAcquisitionTimeStampFBRTest() {
		String formattedDate = null;
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(2012, 8, 5, 2, 11, 56);
		Date expectedDate = c1.getTime();

		formattedDate = DocketsCoreUtility.createAcquisitionTimeStamp(expectedDate, "FBR");

		Assert.isTrue(formattedDate.equalsIgnoreCase("05092012021156"), formattedDate);
	}

	@Test
	public void getAcquisitionTimeStampTest() {
		Date formattedDate = null;
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(2012, 8, 5, 2, 11, 56);
		String expectedDate = "20120905021156";

		formattedDate = DocketsCoreUtility.getAcquisitionTimeStamp(expectedDate, "JPML");

		Assert.isTrue(formattedDate.toString().equals(c1.getTime().toString()), formattedDate.toString() + " should be " + c1.getTime());
	}

	@Test
	public void getAcquisitionTimeStampFBRTest() {
		Date formattedDate = null;
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(2012, 8, 5, 2, 11, 56);
		String expectedDate = "05092012021156";

		formattedDate = DocketsCoreUtility.getAcquisitionTimeStamp(expectedDate, "FBR");

		Assert.isTrue(formattedDate.toString().equals(c1.getTime().toString()), formattedDate.toString() + " should be " + c1.getTime());
	}

	@Test
	public void getFormattedTimeStampTest() {
		// String stringDate,String product
		String strDateFBR = "05092012021156";// 30082012034559
		String strDateJPML = "20120905021156";
		String strDateFBR_2 = "20120905";// yyyyMMdd
		Date formatedDateFBR = null;
		Date formatedDateJPML = null;
		Date formatedDateJPML_2 = null;

		try {
			formatedDateFBR = DocketsCoreUtility.getFormatedTimeStamp(strDateFBR, "FBR");
			formatedDateJPML = DocketsCoreUtility.getFormatedTimeStamp(strDateJPML, "JPML");
			formatedDateJPML_2 = DocketsCoreUtility.getFormatedTimeStamp(strDateFBR_2, "FBR");
		} catch (SourceContentLoaderException e) {
			e.printStackTrace();
		}
		// Wed Sep 05 02:11:56 CDT 2012
		Assert.isTrue("Wed Sep 05 02:11:56 CDT 2012".equalsIgnoreCase(formatedDateFBR.toString())||"Wed Sep 05 02:11:56 IST 2012".equalsIgnoreCase(formatedDateFBR.toString())||"Wed Sep 05 02:11:56 BST 2012".equalsIgnoreCase(formatedDateFBR.toString()), "FBR - Date should be 'Wed Sep 05 02:11:56 CDT 2012' but is: " + formatedDateFBR.toString());
		Assert.isTrue("Wed Sep 05 02:11:56 CDT 2012".equalsIgnoreCase(formatedDateJPML.toString())||"Wed Sep 05 02:11:56 IST 2012".equalsIgnoreCase(formatedDateJPML.toString())||"Wed Sep 05 02:11:56 BST 2012".equalsIgnoreCase(formatedDateJPML.toString()), "JPML - Date should be 'Wed Sep 05 02:11:56 CDT 2012' but is: " + formatedDateJPML.toString());
		Assert.isTrue("Wed Sep 05 00:00:00 CDT 2012".equalsIgnoreCase(formatedDateJPML_2.toString())||"Wed Sep 05 00:00:00 IST 2012".equalsIgnoreCase(formatedDateJPML_2.toString())||"Wed Sep 05 00:00:00 BST 2012".equalsIgnoreCase(formatedDateJPML_2.toString()), "JPML_2 - Date should be 'Wed Sep 05 00:00:00 CDT 2012' but is: " + formatedDateJPML_2.toString());
	}

	@Test
	public void getSourceRequestDateObjectTest() {
		Date formattedDate = null;
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(2012, 8, 5, 2, 11, 56);
		String expectedDate = "05092012021156";

		try {
			formattedDate = DocketsCoreUtility.getSourceRequestDateObject(expectedDate);
		} catch (ParseException e) {
			e.printStackTrace();

		}

		Assert.isTrue(formattedDate.toString().equals(c1.getTime().toString()), formattedDate.toString() + " should be " + c1.getTime());
	}

	@Test
	public void getCurrentTimeStampTest() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmm");
		String strDate = sdf.format(cal.getTime());

		/* gets current timestamp in dockets prefered format i.e ddMMyyyyhhmmss */
		String formattedDate = null;

		formattedDate = DocketsCoreUtility.getCurrentTimeStamp();

		if (formattedDate.length() >= 14) // contains ms
		{
			Assert.isTrue(strDate.equals(formattedDate.substring(0, 12)), formattedDate + " should be " + strDate);
		} else {
			Assert.isTrue(strDate.equals(formattedDate), formattedDate + " failed. Should be " + strDate);
		}
	}

	@Test
	public void testGetCourtClusterNormMap() {
		Court court = new Court();
		court.setCourtID(99);
		court.setCourtCluster("KGCluster");
		court.setCourtNorm("KGCourtNorm");

		List<Court> courtList = new ArrayList<Court>();
		courtList.add(court);

		Court court2 = new Court();
		court2.setCourtID(999);
		court2.setCourtCluster("KGCluster2");
		court2.setCourtNorm("KGCourtNorm2");
		courtList.add(court2);

		HashMap<String, String> courtClusterNormHashMap = DocketsCoreUtility.getCourtClusterNormMap(courtList);

		Assert.notNull(courtClusterNormHashMap, "Error in testGetCourtClusterNormMap - notNull");
		Assert.isTrue(courtClusterNormHashMap.containsKey("KGCluster2"), "Error in testGetCourtClusterNormMap - isTrue");
	}

	@Test
	public void testcreateDocketNumberFromValidJPMLLegacyId() {
		String legacyId = "JPML1998";
		String docketNumber = DocketsCoreUtility.createDocketNumberFromJPMLLegacyId(legacyId);
		Assert.notNull(docketNumber);
		Assert.isTrue("MDLNo1998".equalsIgnoreCase(docketNumber), "Error in testcreateDocketNumberFromValidJPMLLegacyId - isTrue");
	}

	@Test
	public void testcreateDocketNumberFromInValidJPMLLegacyId() {
		String legacyId = "PML1998";
		String docketNumber = DocketsCoreUtility.createDocketNumberFromJPMLLegacyId(legacyId);
		Assert.isNull(docketNumber, "Error in testcreateDocketNumberFromInValidJPMLLegacyId - isNull");
	}

	@Test
	public void testcreateDocketNumberFromNullValidJPMLLegacyId() {
		String legacyId = null;
		String docketNumber = DocketsCoreUtility.createDocketNumberFromJPMLLegacyId(legacyId);
		Assert.isNull(docketNumber, "Error in testcreateDocketNumberFromNullValidJPMLLegacyId - isNull");
	}

	@Test
	public void testCreateDroppedDocketValue() {
		List<DroppedDocket> docketList = new ArrayList<DroppedDocket>();

		String xmlReturned = DocketsCoreUtility.createDroppedDocketXml(docketList);
		Assert.isTrue(xmlReturned.trim().equals(""), "Not constructed as required :: it is /n" + xmlReturned.trim());

		String expectedDroppedDocketString = "<dropped.dockets.info><docket court_norm=\"WI-WBKR\" number=\"3:2012BK15152\" reason=\"Invalid scrape timestamp\"/><docket court_norm=\"MN-WBKR\" number=\"3:2012BK15153\" reason=\"Invalid acq timestamp\"/></dropped.dockets.info>";

		docketList.add(new DroppedDocket("WI-WBKR", "3:2012BK15152", null, null, "Invalid scrape timestamp"));
		docketList.add(new DroppedDocket("MN-WBKR", "3:2012BK15153", null, null, "Invalid acq timestamp"));

		xmlReturned = DocketsCoreUtility.createDroppedDocketXml(docketList);

		Assert.isTrue(xmlReturned.trim().equals(expectedDroppedDocketString), "Not constructed as required :: it is /n" + xmlReturned.trim());
	}

	@Test
	public void testPrepareBkrLegacyId() {
		String expectedLegacyId = "OR-BKR12BK31311";
		String court = "N_DORBKR";
		String docketNumber = "2012bk31311";
		String actualLegaycId = DocketsCoreUtility.prepareBkrLegacyID(docketNumber, court);
		Assert.isTrue(actualLegaycId.equals(expectedLegacyId), "Expected legacyId is " + expectedLegacyId + "  but is " + actualLegaycId);

		docketNumber = "1:2008ap01789";
		court = "N_DNYSBKR";
		expectedLegacyId = "NY-SBKR1:08AP01789";

		actualLegaycId = DocketsCoreUtility.prepareBkrLegacyID(docketNumber, court);
		Assert.isTrue(actualLegaycId.equals(expectedLegacyId), "Expected legacyId is " + expectedLegacyId + "  but is " + actualLegaycId);
	}

	@Test
	public void testReplaceSpacesHyphens() {
		/* Input MDL No. 1456 (JPML Docket number with spaces) Output MDL-NO.-1456 (JPML Docket Number without spces) Only used for JPML filters */
		String expectedDocketNumber = "MDL-No.-1456";
		String docketNumber = "MDL No. 1456";
		String actualDocketNumber = DocketsCoreUtility.replaceSpacesHyphens(docketNumber);
		Assert.isTrue(actualDocketNumber.equals(expectedDocketNumber), "Expected DocketNumber is " + expectedDocketNumber + "  but is " + actualDocketNumber);
	}

//	@Test
	public void testGetFileName_File() {
		/*
		 * gets file name for passed on file Path + Name platform unix /windows independent way as different platforms will have different representation of path /\
		 */
		String inputFileNameString = "tmp/filepath/FileName";
		String expectedFileNameString = "FileName";
		String actualFilename = DocketsCoreUtility.getFileName_File(inputFileNameString);
		Assert.isTrue(actualFilename.equals(expectedFileNameString), "Expected FileName is " + expectedFileNameString + "  but is " + actualFilename);
	}

//	@Test
	public void testGetFileName() {
		/*
		 * gets file name for passed on file Path + Name platform unix /windows independent way as different platforms will have different representation of path /\
		 */
		String inputFileNameString = "tmp/filepath/FileName";
		String expectedFileNameString = "FileName";
		String actualFilename = DocketsCoreUtility.getFileName(inputFileNameString);
		Assert.isTrue(actualFilename.equals(expectedFileNameString), "Expected FileName is " + expectedFileNameString + "  but is " + actualFilename);
	}

	@Test
	public void testgetOldNyCountyClerkLegacyId() {
		/*
		 * Input NYccccCCNNNNNNYYYY (i.e. NYNEWYORKCC6023411998) Output NYccNNNNNN/YYYY (i.e. NYNEWYORK602341/1998)
		 */
		String inputString = "NYNEWYORKCC6023411998";
		String expectedString = "NYNEWYORK602341/1998";
		String actualString = DocketsCoreUtility.getOldNyCountyClerkLegacyId(inputString);
		Assert.isTrue(actualString.equals(expectedString), "Expected LegacyId is " + expectedString + "  but is " + actualString);
	}

	@Test
	public void testgetOldNyDownstateLegacyId() {
		/*
		 * Input NYCCCCNNNNNNYYYY (i.e. NYQUEENS00246162009) Output NYCCCCNNNNNN/YYYYTTTTT (i.e. NYQUEENS0024616/2009SUPREME)
		 */
		String inputString = "NYQUEENS00246162009";
		String expectedString = "NYQUEENS0024616/2009SUPREME";
		String actualString = DocketsCoreUtility.getOldNyDownstateLegacyId(inputString);
		Assert.isTrue(actualString.equals(expectedString), "Expected LegacyId is " + expectedString + "  but is " + actualString);
	}

	@Test
	public void testgetOldNyUpstateLegacyId() {
		/*
		 * Input NYCCCCNNNNNNYYYY (i.e. NYWAYNE90402431996) Output NYCCCCNNNNNN/YYYYTTTTT (i.e. NYWAYNE9040243/1996SUPREMECIVIL)
		 */
		String inputString = "NYWAYNE90402431996";
		String expectedString = "NYWAYNE9040243/1996SUPREMECIVIL";
		String actualString = DocketsCoreUtility.getOldNyUpstateLegacyId(inputString);
		Assert.isTrue(actualString.equals(expectedString), "Expected LegacyId is " + expectedString + "  but is " + actualString);
	}
}
