package com.trgr.dockets.core.util;

public class LegacyIdFormatter {
	

	//format legacyId. This class can be made more generic and used for any other product/court.
	public static String NYLegacyIdFormat(String westlawClusterName, String subDivisionName, String indexNumber, String year)
	{
		String legacyId;
		
		if(westlawClusterName.equalsIgnoreCase("N_DNYCNTYCLERK"))
		{
			 legacyId = ("NY" + subDivisionName.replace(" ", "").toUpperCase() + "CC" + indexNumber + year);
		}
		else 
		{
			legacyId = ("NY" + subDivisionName.replace(" ", "").toUpperCase()+ indexNumber + year);
		}
		
		return legacyId;
	}
	
	public static String NDLegacyIdFormat (String countyName, String docketNumber)
	{
		String legacyId;
		docketNumber = docketNumber.replaceAll("-", "");
		legacyId = ("ND" + countyName.replace(" ","").toUpperCase() + docketNumber);
		
		return legacyId;
		
	}

}
