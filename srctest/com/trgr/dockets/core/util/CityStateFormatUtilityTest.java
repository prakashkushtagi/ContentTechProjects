/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trgr.dockets.core.util.CityStateFormatUtility;

/**
 * Date Conversion utility to simplify (re)formatting of dates throughout filters.
 * @author <a href="mailto:zubair.saiyed@thomsonreuters.com">Zubair Saiyed</a> u0158949 
 */
public class CityStateFormatUtilityTest
{
	private static final Logger LOG = LoggerFactory.getLogger(CityStateFormatUtilityTest.class);

	@Test
	public void formatSimpleCityZipString()
	{
		String input = "Bartlesville, OK 74006";
		String output = CityStateFormatUtility.extractZip(input);
		
		assertNotNull(output);
		assertEquals("74006", output);
	}
	@Test
	public void formatFullZipString()
	{
		String input = "Bartlesville, OK 74006-0000";
		String output = CityStateFormatUtility.extractZip(input);
		
		assertNotNull(output);
		assertEquals("74006-0000", output);
	}
	@Test
	public void formatWeirdCityZipString()
	{
		String input = "Bartlesville, 74006 OK";
		String output = CityStateFormatUtility.extractZip(input);
		
		assertNotNull(output);
		assertEquals("74006", output);
	}
	@Test
	public void formatPhoneNotZipString()
	{
		String input = "567-8209";
		String output = CityStateFormatUtility.extractZip(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	@Test
	public void formatMissingCityZipString()
	{
		String input = "GARDEN CITY, NY";
		String output = CityStateFormatUtility.extractZip(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	@Test
	public void formatSimpleCityString()
	{
		String input = "GARDEN CITY, NY";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("GARDEN CITY", output);
	}
	@Test
	public void formatSimpleStateString()
	{
		String input = "GARDEN CITY, NY";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NY", output);
	}
	@Test
	public void formatSlashCityString()
	{
		String input = "EAST ORANGE/NJ";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("EAST ORANGE", output);
	}
	
	@Test
	public void formatSlashStateString()
	{
		String input = "EAST ORANGE/NJ";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NJ", output);
	}
	
	@Test
	public void formatCommaSlashCityString()
	{
		String input = "BRONX,  N.Y./";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("BRONX", output);
	}
	
	@Test
	public void formatCommaSlashStateString()
	{
		String input = "BRONX,  N.Y./";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N.Y.", output);
	}
	

	@Test
	public void formatEmptyCityStateToCityString()
	{
		String input = "";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	
	@Test
	public void formatNullCityStateToCityString()
	{
		String input = null;
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	
	@Test
	public void formatEmptyCityStateToStateString()
	{
		String input = "";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	
	@Test
	public void formatNullCityStateToStateString()
	{
		String input = null;
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	
	@Test
	public void formatLongAbbrevStateToCityString()
	{
		String input = "STAMFORD, CONN";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("STAMFORD", output);
	}
	
	@Test
	public void formatLongAbbrevStateToStateString()
	{
		String input = "STAMFORD, CONN";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("CONN", output);
	}
	@Test
	public void formatLongStateToCityString()
	{
		String input = "STAMFORD, CONNECTICUT";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("STAMFORD", output);
	}
	
	@Test
	public void formatLongStateToStateString()
	{
		String input = "STAMFORD, CONNECTICUT";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("CONNECTICUT", output);
	}
	
	@Test
	public void formatNoStateToCityString()
	{
		String input = "CORNWALL ON HUDSON,";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("CORNWALL ON HUDSON", output);
	}
	
	@Test
	public void formatNoStateToStateString()
	{
		String input = "CORNWALL ON HUDSON,";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	@Test
	public void formatSpaceStatePeriodToCityString()
	{
		String input = "TARRYTOWN N.Y.";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("TARRYTOWN", output);
	}
	@Test
	public void formatSpaceStatePeriodToStateString()
	{
		String input = "TARRYTOWN N.Y.";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N.Y.", output);
	}
	
	@Test
	public void formatSpaceStateToCityString()
	{
		String input = "NEW YORK  NY";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("NEW YORK", output);
	}
	@Test
	public void formatSpaceStateToStateString()
	{
		String input = "NEW YORK  NY";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NY", output);
	}
	
	@Test
	public void formatSpacesStateToCityString()
	{
		String input = "N Y,  N Y";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("N Y", output);
	}
	@Test
	public void formatSpacesStateToStateString()
	{
		String input = "N Y,  N Y";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N Y", output);
	}
	
	@Test
	public void formatInvalidStateToCityString()
	{
		String input = "WILLIAMSVILLE, NEW Y";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("WILLIAMSVILLE", output);
	}
	@Test
	public void formatInvalidStateToStateString()
	{
		String input = "WILLIAMSVILLE, NEW Y";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NEW Y", output); // should not be WY
	}
	@Test
	public void formatExtraCommasToCityString()
	{
		String input = "99 Washington, Albany, NY";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("99 Washington, Albany", output);
	}
	@Test
	public void formatExtraCommasToStateString()
	{
		String input = "99 Washington, Albany, NY";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NY", output); 
	} 
	
	@Test
	public void formatExtraCommas2ToCityString()
	{
		String input = "POB 51,COMSTOCK, NY,";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("POB 51,COMSTOCK", output);
	}
	@Test
	public void formatExtraCommas2ToStateString()
	{
		String input = "POB 51,COMSTOCK, NY,";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NY", output);
	}
	@Test
	public void formatInvalidState2ToCityString()
	{
		String input = "STATEN ISLAND";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("STATEN ISLAND", output);
	}
	@Test
	public void formatInvalidState2ToStateString()
	{
		String input = "STATEN ISLAND";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output); // Should not be ND
	} 
	@Test
	public void formatSpelledOutCommaStateToCityString()
	{
		String input = "BROOKLYN, NEW YORK";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("BROOKLYN", output);
	}
	@Test
	public void formatSpelledOutCommaStateToStateString()
	{
		String input = "BROOKLYN, NEW YORK";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NEW YORK", output);
	}
	@Test
	public void formatSpelledOutCommaStateToCityNoSpaceString()
	{
		String input = "BROOKLYN, NEWYORK";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("BROOKLYN", output);
	}
	@Test
	public void formatSpelledOutCommaStateToStateNoSpaceString()
	{
		String input = "BROOKLYN, NEWYORK";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NEWYORK", output);
	}
	@Test
	public void formatSpelledOutStateToCityString()
	{
		String input = "BROOKLYN NEW YORK";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("BROOKLYN", output);
	}
	@Test
	public void formatSpelledOutStateToStateString()
	{
		String input = "BROOKLYN NEW YORK";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NEW YORK", output);
	}
	
	@Test
	public void formatOnlyStateToCityString()
	{
		String input = "NEW YORK";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	@Test
	public void formatOnlyStateToStateString()
	{
		String input = "NEW YORK";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NEW YORK", output);
	}
	
	@Test
	public void formatNYNYStateToCityString()
	{
		String input = "NEW YORK NEW YORK";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("NEW YORK", output);
	}
	@Test
	public void formatNYNYStateToStateString()
	{
		String input = "NEW YORK NEW YORK";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NEW YORK", output);
	}
	
	@Test
	public void formatNYNY2StateToCityString()
	{
		String input = "NEW YORK, N.Y.";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("NEW YORK", output);
	}
	@Test
	public void formatNYNY2StateToStateString()
	{
		String input = "NEW YORK, N.Y.";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N.Y.", output);
	}
	@Test
	public void formatNYNY3StateToCityString()
	{
		String input = "NYNY";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("NYNY", output);
	}
	@Test
	public void formatNYNY3StateToStateString()
	{
		String input = "NYNY";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	
	@Test
	public void formatNY1StateToCityString()
	{
		String input = "PLEASANTVILLE, N.Y.1";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("PLEASANTVILLE", output);
	}
	@Test
	public void formatNY1StateToStateString()
	{
		String input = "PLEASANTVILLE, N.Y.1";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N.Y.1", output);
	}
	@Test
	public void formatCommaNoSpaceStateToCityString()
	{
		String input = "WESTWOOD,N.J.";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("WESTWOOD", output);
	}
	@Test
	public void formatCommaNoSpaceStateToStateString()
	{
		String input = "WESTWOOD,N.J.";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N.J.", output);
	}	
	
	@Test
	public void formatCommaNoStateToCityString()
	{
		String input = "LONG ISLAND CITY,";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("LONG ISLAND CITY", output);
	}
	@Test
	public void formatCommaNoStateToStateString()
	{
		String input = "LONG ISLAND CITY,";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}	
	
	
	@Test
	public void formatCommaBadStateToCityString()
	{
		String input = "LONG ISLAND CITY, N";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("LONG ISLAND CITY", output);
	}
	@Test
	public void formatCommaBadStateToStateString()
	{
		String input = "LONG ISLAND CITY, N";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N", output);
	}	
	
	@Test
	public void formatOnlyStateCodeToCityString()
	{
		String input = "NY";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	@Test
	public void formatOnlyStateCodeToStateString()
	{
		String input = "NY";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("NY", output);
	}
	
	@Test
	public void formatNoSpaceToCityString()
	{
		String input = "GARDEN CITYNY";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("GARDEN CITYNY", output);
	}
	@Test
	public void formatNoSpaceToStateString()
	{
		String input = "GARDEN CITYNY";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	@Test
	public void formatInvalidState4ToCityString()
	{
		String input = "HOPEWELL JUNCTION, N";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("HOPEWELL JUNCTION", output);
	}
	@Test
	public void formatInvalidState4ToStateString()
	{
		String input = "HOPEWELL JUNCTION, N";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("N", output);
	}
	
	@Test
	public void formatCommaToCityString()
	{
		String input = ",";
		String output = CityStateFormatUtility.formatCity(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	
	@Test
	public void formatCommaToStateString()
	{
		String input = ",";
		String output = CityStateFormatUtility.formatState(input);
		
		assertNotNull(output);
		assertEquals("", output);
	}
	@Test
	public void testNullString()
	{
		String temp=CityStateFormatUtility.formatCity(null);
		assertEquals(temp, "");
	}
	
	@Test
	public void testShortString()
	{
		String temp=CityStateFormatUtility.formatCity("Rio");
		assertEquals(temp, "Rio");
	}
	
	@Test
	public void testValidCityState()
	{
		String temp=CityStateFormatUtility.formatCity("Rio, CT");
		assertEquals(temp, "Rio");
	}
	
	@Test
	public void testCitySpaces()
	{
		String temp=CityStateFormatUtility.formatCity("Rio CT");
		assertEquals(temp, "Rio");
	}
	
	@Test
	public void testAllSpaces()
	{
		String temp=CityStateFormatUtility.formatCity("R                   ");		
		assertEquals("R",temp);
	}
	
	@Test
	public void testAllCommas()
	{
		String temp=CityStateFormatUtility.formatCity("R,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
		assertEquals("R", temp);
	}
	@Test
	public void testAllSlashes()
	{
		String temp=CityStateFormatUtility.formatCity("R////////");
		assertEquals("R", temp);
	}

	@Test
	public void containsOnlyStateAbbrev()
	{
		String input = "ND";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertTrue(containsStateAbbrev);
	}
	
	@Test
	public void containsCityAndStateAbbrev()
	{
		String input = "FARGO, ND";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertTrue(containsStateAbbrev);
	}
	
	@Test
	public void containsCityStateAbbrevAndZip()
	{
		String input = "FARGO, ND 58104";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertTrue(containsStateAbbrev);
	}
	
	@Test
	public void containsStateAbbrevInWord()
	{
		String input = "BEND IT LIKE BECKHAM";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertFalse(containsStateAbbrev);
	}
	
	@Test
	public void containsStateAbbrevAcrossWords()
	{
		String input = "DON DANGER";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertFalse(containsStateAbbrev);
	}
	
	@Test
	public void containsNoStateAbbrev()
	{
		String input = "NORTH DAKOTA";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertTrue(containsStateAbbrev);
	}
	
	@Test
	public void containsEmptyStateAbbrev()
	{
		String input = "";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertFalse(containsStateAbbrev);
	}
	
	@Test
	public void containsCityAndStateFull()
	{
		String input = "FARGO, NORTH DAKOTA";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertTrue(containsStateAbbrev);
	}
	
	@Test
	public void containsCityAndStateFull_2()
	{
		String input = "KEN BRIAN";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState_2(input);
		
		assertFalse(containsStateAbbrev);
	}
	
	@Test
	public void containsCityAndStateFull_2_Real_State()
	{
		String input = "FARGO, ND";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState_2(input);
		
		assertTrue(containsStateAbbrev);
	}
	
	@Test
	public void containsCityStateFullvAndZip()
	{
		String input = "FARGO, NORTH DAKOTA 58104";
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertTrue(containsStateAbbrev);
	}
	
	@Test
	public void containsNullStateAbbrev()
	{
		String input = null;
		boolean containsStateAbbrev = CityStateFormatUtility.containsState(input);
		
		assertFalse(containsStateAbbrev);
	}
	
	@Test
	public void endsWithStateAbbrev(){
		String input = "ALBERT LEA, MN";
		boolean endsWithStateAbbrev = CityStateFormatUtility.endsWithStateAbbrev(input);
		assertTrue(endsWithStateAbbrev);
	}
	
	@Test
	public void containsButNotEndsWithStateAbbrev(){
		String input = "ALBERT LEA, MN 56007";
		boolean endsWithStateAbbrev = CityStateFormatUtility.endsWithStateAbbrev(input);
		assertFalse(endsWithStateAbbrev);
	}
	
	@Test
	public void nineDigitZipTest() {
		String input = "123456789";
		String expected = "12345-6789";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
	}
	
	@Test
	public void fiveDigitDashZipTest() {
		String input = "12345-";
		String expected = "12345";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
	}
	
//	@Test
	public void fiveDigitDashZerosZipTest() {
		String input = "12345-0000";
		String expected = "12345";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
	}
	
//	@Test
	public void allZerosZipTest() {
		String input = "00000-0000";
		String expected = "";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
		
		input = "00000000";
		expected = "";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
		
		input = "-0000";
		expected = "";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
		
		input = "0000-";
		expected = "";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
	}
	
	@Test
	public void garbageInGarbageOutTest() {
		String input = "448701";
		String expected = "448701";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
		
		input = "00000-4321";
		expected = "00000-4321";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
		
		input = "12345-432";
		expected = "12345-432";
		input = CityStateFormatUtility.formatZip(input);
		assertEquals(expected, input);
	}
	
	protected Logger getLogger()
	{
		return LOG;
	}
	
	@Test
	public void getCityStateZip() {
		String input = "EAGAN, MN 55123";
		String expectedCity = "EAGAN";
		String expectedState = "MN";
		String expectedZip = "55123";
		Map<String, String> actualCityStateZipMap = new HashMap<String, String>();
		actualCityStateZipMap = CityStateFormatUtility.getCityStateZip(input);
		String actualCity = (String) actualCityStateZipMap.get("city");
		String actualState = (String) actualCityStateZipMap.get("state");
		String actualZip = (String) actualCityStateZipMap.get("zip");
		
		assertEquals(expectedCity, actualCity);
		assertEquals(expectedState, actualState);
		assertEquals(expectedZip, actualZip);
		
	}
	
	@Test
	public void getCityStateNoZip() {
		String input = "IRVING TX";
		String expectedCity = "IRVING";
		String expectedState = "TX";
		String expectedZip = "";
		Map<String, String> actualCityStateZipMap = new HashMap<String, String>();
		actualCityStateZipMap = CityStateFormatUtility.getCityStateZip(input);
		String actualCity = (String) actualCityStateZipMap.get("city");
		String actualState = (String) actualCityStateZipMap.get("state");
		String actualZip = (String) actualCityStateZipMap.get("zip");
		
		assertEquals(expectedCity, actualCity);
		assertEquals(expectedState, actualState);
		assertEquals(expectedZip, actualZip);
		
	}
	
	@Test
	public void testCreateAddressFromSingleLine(){
		String input = "400 N. TAMPA STREET, SUITE 3200 TAMPA FL 33602";
		String expectedStreet = "400 N. TAMPA STREET, SUITE 3200";
		String expectedCity = "TAMPA";
		String expectedState = "FL";
		String expectedZip = "33602";
		Map<String, String> address = CityStateFormatUtility.createAddressFromSingleLine(input);
		String actualStreet = address.get("street");
		String actualCity = address.get("city");
		String actualState = address.get("state");
		String actualZip = address.get("zip");
		
		assertEquals(expectedStreet, actualStreet);
		assertEquals(expectedCity, actualCity);
		assertEquals(expectedState, actualState);
		assertEquals(expectedZip, actualZip);
				
	}
}
