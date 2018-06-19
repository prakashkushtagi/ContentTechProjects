package com.trgr.dockets.core.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NyNormalizationUtility extends NormalizationUtility{
	
	public static String normalizeNameWithTitleNY(String name) {
		
		return normalizeNameWithTitleNY(name, true);
	}
	

	public static String  normalizeNameWithTitleNY(String name, boolean removeNumber )
    {
		String nameNy=filterNameNy(name);
        nameNy= normalizeNameWithTitle(nameNy,removeNumber);
        
   
     return nameNy;
    }
	
	public static String filterNameNy(String fullname)
	{
		//SRL - is replace with SRL, to avoid the hanging dash from Party Names.
		//fullname= fullname.replace("–", "-");
		if (fullname.startsWith("SRL"))
		{
			fullname= fullname.replace("SRL -", "SRL,");
			fullname= fullname.replaceAll("SRL ", "SRL,");
		}
		if (fullname.endsWith("SRL"))
		{
			fullname= fullname.replace("- SRL", ", SRL");
			
			if (!fullname.endsWith(", SRL")) {
				fullname= fullname.replace(" SRL", ", SRL");
			}
		}
		
		if (fullname.startsWith("ESQ"))
		{
			fullname= fullname.replace("ESQ.-OUT", "ESQ., ");
		}
		
		//OUT is removed
		fullname= fullname.replaceAll("[-\\s]+OUT\\b", "");
		
		String[] filterWords = {"[,\\s]*PART[\\pP\\d+\\s,]+", "[,\\s]*PT[\\pP\\d+\\s,]+"};

		//Part and its corresponding number is stripped from Party Names. This was mostly found for judges.
		for (String c : filterWords) {
			Pattern pattern = Pattern.compile(c);
			Matcher matcher = pattern.matcher(fullname);
			fullname = matcher.replaceAll("");
		}
		
		return fullname;
	}
}
	