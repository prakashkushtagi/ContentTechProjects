/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/

package com.trgr.dockets.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DctDocketNumberUtility {
	
	private static final Pattern dctDocketNumberPattern = Pattern.compile("(\\w{1,2})?:(\\d{2,4})-?([A-z]+)?-?(\\d{1,5})-?(.*)");
	
	public static String normalizeDocketNumber(String docketNumber) throws Exception{
		Matcher docketNumberMatcher = dctDocketNumberPattern.matcher(docketNumber);
		if (docketNumberMatcher.find()){
			String location = docketNumberMatcher.group(1);
			String year = docketNumberMatcher.group(2);
			String caseType = docketNumberMatcher.group(3);
			String sequence = docketNumberMatcher.group(4);
			if (year.length() == 4){
				year = year.substring(2);				
			} else if (year.length() != 2){
				throw new Exception ("Docket number: " + docketNumber + " does not have a 2 or 4 digit year");
			}
			
			String formattedDocketNumber = "";

			if (null != location && !location.isEmpty()) {
				formattedDocketNumber += location + ":";
			} else {
				throw new Exception ("Docket number: " + docketNumber + " - Dropped because this docket is without location code");
			}
			
			formattedDocketNumber += year;
			
			if (null != caseType && !caseType.isEmpty()) {
				formattedDocketNumber += "-"+caseType;
			}
			
			formattedDocketNumber += "-"+sequence;
			
			return formattedDocketNumber.toUpperCase();
		} else {
			throw new Exception ("Docket number: " + docketNumber + " doesn't fit expected pattern");
		}
	}
}
