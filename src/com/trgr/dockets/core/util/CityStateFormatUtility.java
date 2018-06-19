/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;


import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


/**
 * City State formatting utility to simplify (re)formatting of City States throughout filters.
 * If a state can be validated from the cityState element, then use city and state otherwise use the cityState.
 * @author  Kirsten Gunn 
 */
public class CityStateFormatUtility {

	private static  String US_STATES[] = {"alabama", "alaska", "arizona", "arkansas", "california", "colorado", "connecticut", "delaware", 
		"districtofcolumbia","district of columbia", "florida", "georgia", "guam", "hawaii","hawai'i", "idaho", "illinois", "indiana", "iowa", "kansas", "kentucky", "louisiana", 
		"maine", "maryland", "massachusetts", "michigan", "minnesota", "mississippi", "missouri", "montana", "national park", "nebraska", "nevada", 
		"newhampshire","new hampshire", "newjersey", "new jersey", "newmexico", "new mexico", "newyork", "new york",
		"northcarolina", "north carolina","n. carolina", "northdakota", "north dakota","n. dakota","Northern Mariana Islands", "ohio", "oklahoma", "oregon", 
		"pennsylvania", "puertorico","puerto rico", "rhodeisland", "rhode island", "southcarolina",  "south carolina","s. carolina", 
		"southdakota", "south dakota","s. dakota", "tennessee", "texas", "utah", "vermont", "virginia", 
		"washington", "westvirginia","west virginia", "wisconsin", "wyoming"
		}; 

	private static  String US_STATES_ABBREV[] = {"AK","AL","AR","AS","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID",
			"IL","IN","KS","KY","LA","MA","MD","ME","MH","MI","MN","MO","MP","MS","MT","NC","ND","NE","NH","NJ","NM","NP","NV","NY",
			"OH","OK","OR","PA","PR","PW","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY", 
			"ALA", "ARK", "ARIZ", "CAL", "CALIF","COL","COLO", "CONN", "DEL", "FLA", "IDA", "ILL", "IND", "KAN", "KANS", 
			"KEN", "MASS", "MICH", "MINN", "MISS", "MONT", "NEB", "NEBR", "NEV", "NMEX", "NYORK", "NDAK", "OKLA", "ORE", 
			"PENN", "PENNA", "SDAK", "TENN", "TEX", "USVI", "WASH", "WVA", "WIS", "WISC", "WYO"
			};
	/**
	 * compared two char states like MN,UT,NY..
	 * @param input
	 * @return
	 */

	private static  String US_STATES_ABBREV_2[] = {"AK","AL","AR","AS","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID",
			"IL","IN","KS","KY","LA","MA","MD","ME","MH","MI","MN","MO","MP","MS","MT","NC","ND","NE","NH","NJ","NM","NP","NV","NY",
			"OH","OK","OR","PA","PR","PW","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY"
			};

	private static final Pattern CITY_STATE_ZIP = Pattern.compile("(([^\\d\\s][\\w\\.]*\\s?)?[^\\d\\s][\\w\\.]*\\s?\\w+\\s*\\w{2}\\s*\\d{5}(?!\\d)(-\\d{4})?)(?=$)");
	private static final Pattern NINE_DIGIT_ZIP_PATTERN = Pattern.compile("^(\\d{5})-?(\\d{4})$");
	
    /**
     * CityStateFormatUtility
     */
	public CityStateFormatUtility()
	{
		
	}
	public static String extractZip(String input) // Only if a valid zip exists.
	{
		// return if there is nothing to do
		if(input == null || input.isEmpty() || input.length() < 2) {
			return "";
		}
		
		String zipRegex = new String("\\d{5}(?:[-\\s])?(?:\\d{4})?");
		Pattern n = Pattern.compile(zipRegex);
		Matcher nameMatcher = n.matcher(input);
		boolean foundMatch = nameMatcher.find();

		String zipStr = "";
		if (foundMatch) {
			zipStr = nameMatcher.group(0).trim();
		}
		return zipStr;
	}
	
	/**
	 * - If the zip code is all zeros or is blank then it will return nothing.<br>
	 * - If the zip code contains 5 digits followed by a hyphen it will just return the five digits.<br>
	 * - If the zip code contains 9 digits and the last four are not all 0s it will return 5 of them followed by a hyphen followed by the next four digits.<br>
	 * - If the zip code contains 9 digits and the last four are all zeros then it will only return the first 5 digits.<br>
	 * - If the zip code contains 5 digits followed by a hyphen followed by 4 zeros it will return the first 5 digits.<br>
	 * - All other cases the zip code will return what was in the input.
	 */
	public static String formatZip(String input) {
		if(StringUtils.isBlank(input)) {
			return "";
		}
		
		if(input.matches("^\\d{5}-$")) {
			return input.replace("-", "");
		}
		
		Matcher nineDigitMatcher = NINE_DIGIT_ZIP_PATTERN.matcher(input);
		if(nineDigitMatcher.find()) {
			return nineDigitMatcher.group(1) + "-" + nineDigitMatcher.group(2);
		}
		
		return input;
	}
	
	public static String formatCity(String input) // Only if a validated state exists.
	{
		// return if there is nothing to do
		if(input == null || input.isEmpty() || input.length() < 2) {
			return "";
		}
		
		boolean commaSeperator = input.contains(",");

		// trim trailing seperators
		input = input.replaceAll(",+$", "").trim();
		input = input.replaceAll("/+$", "").trim();

		String cityStr = "";
		int idxSeperator = indexOfStateSeperator(input, "city");
		
		
		if (idxSeperator > 1 ) {
			cityStr = input.substring(0, idxSeperator).trim();
			// trim separators
			if (cityStr.charAt(cityStr.length()-1)   == ',' ||
					cityStr.charAt(cityStr.length()-1)   == '/' )
			{
				cityStr = cityStr.substring(0, cityStr.length()-1).trim();
			}
		}
		else if ( commaSeperator  ) // if comma then, regardless of no valid state, put everything in city tag.
		{
			idxSeperator = input.lastIndexOf(",");
			if (idxSeperator < 0)
				idxSeperator = input.length();
			cityStr = input.substring(0, idxSeperator).trim();
		}
		else if (idxSeperator < 0)
		{
			cityStr = input;

		}
		return cityStr;
	}
	public static String formatState(String input)
	{

		// trim trailing seperators
		if (input != null && !input.isEmpty() && ( input.charAt(input.length()-1)   == ',' ||
			input.charAt(input.length()-1)   == '/' ))
		{
				input = input.substring(0, input.length()-1).trim();
		}
		// return if there is nothing to do
		if(input == null || input.isEmpty() || input.length() < 2) {
			return "";
		}

		
		String stateStr = "";
		int idxSeperator = indexOfStateSeperator(input, "state");

		if (idxSeperator < 0 || idxSeperator== input.length()) {
//			log.debug(String.format("No Seperator Found for : [%s]", input));
			if (input.contains(",")) // Put whatever follows the comma as the state (handles "NEW Y" state entries)
			{
				idxSeperator = input.lastIndexOf(",")+1;
				stateStr = input.substring( idxSeperator).trim();		
			}
		}
		else	{
		
			stateStr = input.substring(idxSeperator ).trim();		
		}
		return stateStr;

	}
	
	private static int indexOfStateSeperator(String input, String caller)
	{
		if (input.isEmpty())
		{
			return -1;
		}
		
		int idxCityOffset = 1;
		if ( caller.equals("city") )
		{
			idxCityOffset = 0;
		}
		int idxSeperator = input.toUpperCase().lastIndexOf(",")+idxCityOffset;
		
		if (idxSeperator <= 0 ) {
			idxSeperator = input.toUpperCase().lastIndexOf("/")+idxCityOffset;
		}
		
		if (idxSeperator <= 0 ) {
			idxSeperator = input.toUpperCase().lastIndexOf(" ");
		}
		
		// if no separator found still validate state for state caller.
		String stateVal = "";
		 if ( idxSeperator >= 0 )
		 {
			 stateVal = input.substring(idxSeperator ).replaceAll("[^A-Za-z]", "");
		 }
		 else
		 {
			 stateVal = input.replaceAll("[^A-Za-z]", "");
			 idxSeperator = 0;
		 }
		 // validate state
		 if ( !validateStateAbbrev(stateVal)) {
				idxSeperator = -1;
				// if still no separator check for long state
				idxSeperator = findStateLongName(input);
				
			}
		return idxSeperator;
	}
	static boolean validateStateAbbrev(String stateCodeVal)
	{
		boolean returnVal = false;

		// loop through states
		for (String stateAbbrevLoop: US_STATES_ABBREV)
		{
			if (stateCodeVal.toUpperCase().equals(stateAbbrevLoop))
			{
				returnVal = true;
				break; // found
			}
		}
		
		return returnVal;
	}

	static int findStateLongName(String input)
	{
		int idxSeperator = -1;
		// loop through states
		for (String stateLoop: US_STATES)
		{
			idxSeperator = input.toLowerCase().lastIndexOf(stateLoop);
			if (idxSeperator >=0) {
				break; // found
			}
			else
			{
				idxSeperator = -1;// no State Found
			}

		}
		return idxSeperator;
	}
	
	public static boolean containsState(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		
		if (containsStateAbbrev(input)) {
			return true;
		}
		return containsStateFull(input);
	}
	/**
	 * compared two char states like MN,UT,NY..
	 * @param input
	 * @return
	 */

	public static boolean containsState_2(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		
		if (containsStateAbbrev_2(input)) {
			return true;
		}
		return containsStateFull(input);
	}
	public static boolean containsStateFull(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		
		String stateAbbrevRegex = "^(?=.*\\b(" + StringUtils.join(US_STATES, "|") + ")\\b)";
		Pattern p = Pattern.compile(stateAbbrevRegex);
		Matcher m = p.matcher(input.toLowerCase());
		return m.find();
	}
	
	public static boolean containsStateAbbrev(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		
		String stateAbbrevRegex = "^(?=.*\\b(" + StringUtils.join(US_STATES_ABBREV, "|") + ")\\b)";
		Pattern p = Pattern.compile(stateAbbrevRegex);
		Matcher m = p.matcher(input.toUpperCase());
		return m.find();
	}
	/**
	 * compared two char states like MN,UT,NY..
	 * @param input
	 * @return
	 */
	public static boolean containsStateAbbrev_2(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		
		String stateAbbrevRegex = "^(?=.*\\b(" + StringUtils.join(US_STATES_ABBREV_2, "|") + ")\\b)";
		Pattern p = Pattern.compile(stateAbbrevRegex);
		Matcher m = p.matcher(input.toUpperCase());
		return m.find();
	}
	public static boolean isCityStateZipLine(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		String stateAbbrevRegex = "^(.+ \\b(" + StringUtils.join(US_STATES_ABBREV, "|") + ")\\b)";
		Matcher patternMatcher = Pattern.compile(stateAbbrevRegex).matcher(input.toUpperCase());
		return patternMatcher.find();
	}
	/**
	 * Checks to see if the input ends with a state abbreviation. Includes abbreviations longer than
	 * two letters.
	 * @param input
	 * @return
	 */
	public static boolean endsWithStateAbbrev(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		
		String stateAbbrevRegex = "^(?=.*\\b(" + StringUtils.join(US_STATES_ABBREV, "|") + ")$)";
		Pattern p = Pattern.compile(stateAbbrevRegex);
		Matcher m = p.matcher(input.toUpperCase());
		return m.find();
	}
	
	/**
	 * Checks to see if the input ends with a state abbreviation. Includes only two letter abbreviations.
	 * @param input
	 * @return
	 */
	public static boolean endsWithStateAbbrev_2(String input)
	{
		if(input == null || input.isEmpty()) {
			return false;
		}
		
		String stateAbbrevRegex = "^(?=.*\\b(" + StringUtils.join(US_STATES_ABBREV_2, "|") + ")$)";
		Pattern p = Pattern.compile(stateAbbrevRegex);
		Matcher m = p.matcher(input.toUpperCase());
		return m.find();
	}
	
	/**
	 * Method to return city,state and zip when passed a input string combining all of them
	 */
	public static Map<String, String> getCityStateZip(String cityStateZip) {
		String city = null;
		String state = null;
		final Map<String, String> cityStateZipMap = new HashMap<String, String>();
		final int lastSpaceIndex = cityStateZip.lastIndexOf(" ");
		if (lastSpaceIndex < 0) {
			cityStateZipMap.put("state", cityStateZip);
		}
		else {
			String cityState = "";
			final String preformattedZip = extractZip(cityStateZip.substring(lastSpaceIndex).trim());
			final String zip = formatZip(preformattedZip);
			if (zip != null) {
				if(isNotBlank(preformattedZip)) {
					cityState = cityStateZip.substring(0, cityStateZip.indexOf(preformattedZip)).trim();
				}else {
					cityState = cityStateZip;
				}
				cityStateZipMap.put("zip", zip);
			} else {
				cityState = cityStateZip;
			}
			if (containsState(cityState)) {
				state = formatState(cityState).trim();
				city = formatCity(cityState).trim();
				
				cityStateZipMap.put("city", city);
				cityStateZipMap.put("state", state);
			}
			
		}
		return cityStateZipMap;
	}	
	
	/*
	 * Returns true if input contains any full state names or abbreviations
	 */
	public static boolean isUSAddress(String input){	
		return (containsState(input) || containsState_2(input) || containsStateAbbrev(input) || containsStateAbbrev_2(input) || containsStateFull(input));
	}
	
	/**
	 * Checks for city, state, and zip code information at the end of the given String
	 * 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param line
	 *            String to check for the City, State Zip
	 * @return Returns true if this line contains city, state, and zip information at the end of the string
	 * 
	 */
	public static boolean isCityStateZip(String line) {
		Pattern stateZipPattern = Pattern.compile("^.*[A-Za-z]+\\s[A-Za-z]{2}\\s[0-9]{5,6}(-[0-9]{4})?$");
		return stateZipPattern.matcher(line).find();
	}
	
	public static boolean isCityState(String line){
		if(line.matches("^.*\\d+.*$")){
			return false;
		}
		return CityStateFormatUtility.endsWithStateAbbrev(line);
	}
	
	/*
	 * Creates Address from a single line and returns parts of address in a Map with elements:
	 * 		street	-The street address or the full address if not a US address
	 * 		city	-The city           or not in map if not a US address
	 * 		state	-The state          or not in map if not a US address
	 * 		zip		-The zip            or not in map if not a US address
	 * Only works if there are multiple spaces between street and CityStateZip like in FL Orange source data.
	 */
	public static Map<String, String> createAddressFromSingleLine(String fullAddress){
		final Map<String, String> address;
		if(isUSAddress(fullAddress)){
			Matcher m = CITY_STATE_ZIP.matcher(fullAddress);
			if(m.find()){
				String cityStateZip = m.group(1).trim();
				String streetAddress = fullAddress.replace(cityStateZip, "").trim();
				address = CityStateFormatUtility.getCityStateZip(cityStateZip);
				address.put("street", streetAddress);
			}else{
				address = new HashMap<String, String>();
				address.put("street", fullAddress);
			}
		}else{
			address = new HashMap<String, String>();
			address.put("street", fullAddress);
		}
	
		return address;
	}
}
