package com.trgr.dockets.core.util;

/**
 * DocketNumberFormat Utility is created to be used for any kind of Docket formatting that takes place in PB.
 * 
 * @author  Sagun Khanal
 */

public class DocketNumberFormatter {

	
	/**
	 * Change docket number format from YYYYNNNNNNN to NNNNNNN/YYYY
	 */
	public static String nyDocketNumberFormat1 (String docketNumber)
	{
		docketNumber=docketNumber.trim();
		if(docketNumber.isEmpty())
			return docketNumber;
		
		int docketNumberLength = docketNumber.length();
		if(docketNumberLength < 4) {
			// we have a bad docket number so lets return
			return "";
		}
		if(docketNumberLength < 11) {
			// CC happens to be 10 not 11 sometimes...
			docketNumberLength = docketNumber.length();
		} else {
			// it should never be greater than 11
			docketNumberLength = 11;
		}
		
		String year = docketNumber.substring(0, 4);
		String number = docketNumber.substring(4, docketNumberLength);
		
		docketNumber = number + "/" + year;
		
		return docketNumber;
	}
	
	/**
	 * Change docket number format from NNNNNNNYYYY to NNNNNNN/YYYY
	 */
	public static String nyDocketNumberFormat2 (String docketNumber)
	{
		if(docketNumber.isEmpty())
			return docketNumber;
		
		int docketNumberLength = docketNumber.length();
		if(docketNumberLength < 7) {
			// we have a bad docket number so lets return
			return "";
		}
		if(docketNumberLength < 11) {
			// CC happens to be 10 not 11 sometimes...
			docketNumberLength = docketNumber.length();
		} else {
			// it should never be greater than 11
			docketNumberLength = 11;
		}
		
		String year = docketNumber.substring(7, docketNumberLength);
		String number = docketNumber.substring(0, 7);
		
		docketNumber = number + "/" + year;
		
		return docketNumber;
	}

}
