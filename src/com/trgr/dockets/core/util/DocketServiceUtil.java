/*
 * Copyright 2016: Thomson Reuters.
 * All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.domain.SourceMetadata;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.service.DocketService;
import com.westgroup.publishingservices.uuidgenerator.UUID;


public class DocketServiceUtil {
	/** Format for parsing the date values as they appear in the acquisition log file. */
	public static final SimpleDateFormat METADATA_SDF = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
	
	public static Collection<UUID> createUuidObjectsFromStrings(Collection<String> uuidStrings) throws Exception {		
		Collection<UUID> uuids = new ArrayList<UUID>(uuidStrings.size());
		// Convert string representation of content UUID to an object representation
		for (String uuidString : uuidStrings) {
			uuids.add(new UUID(uuidString));
		}
		return uuids;
	}

	public static final void processMetadataFileLine(String line, SourceMetadata metadata,
			Map<String, RequestInitiatorTypeEnum> acquisitionTypeMap, DocketService docketService) {

		final Matcher docketNumberAttributeMatcher = Pattern.compile(".*docket.number=\\\"(.*?)\\\".*acquisition.date=\\\"(.*?)\\\".*").matcher(line); //Bug 133903
		
		// Read all the lines of the metadata file one-by-one
		if (StringUtils.contains(line, "docket.number")) {
			if (docketNumberAttributeMatcher.matches()) {
				String rawDocketNumberValue = docketNumberAttributeMatcher.group(1).trim();  // eg "MDLNo2382"
				Assert.isTrue(!rawDocketNumberValue.isEmpty(), "The value of the docket.number attribute is empty");
				String aqDate = docketNumberAttributeMatcher.group(2).trim();
				metadata.addDocNoAq(rawDocketNumberValue, aqDate);
				metadata.addDocketNumber(rawDocketNumberValue);
			} else {
				throw new RuntimeException("The docket.number attribute is missing");
			}
		}
		
	}
	
	
	
	public static String convertDktNumberToLegacyIdByCourt(SourceDocketMetadata sourceDocketMetadata, String courtClusterName){
		String legacyId = "";
		String countyName = ""; //CNTY
		String caseType = ""; //CASE
		String docketNumber = ""; //DN
		String filingDate = ""; //FD
		String miscellaneous = ""; //MISC
		
		courtClusterName = courtClusterName.toUpperCase();
		
		HashMap<String,String> sdmHash = new HashMap<String,String>();
		if(sourceDocketMetadata.getCountyName() != null){
			countyName = sourceDocketMetadata.getCountyName().replace(" ", "").toUpperCase();
		}else{
			countyName = "";
		}
			
		if(sourceDocketMetadata.getCaseType() != null){
			caseType = sourceDocketMetadata.getCaseType().toUpperCase();
		}else{
			caseType = "";
		}
		
		if(sourceDocketMetadata.getDocketNumber() != null){
			docketNumber = sourceDocketMetadata.getDocketNumber().toUpperCase();
		}else{
			docketNumber = "";
		}
		
		if(sourceDocketMetadata.getMiscellaneous() != null){
			miscellaneous = sourceDocketMetadata.getMiscellaneous().toUpperCase();
		}else{
			miscellaneous = "";
		}
		
		filingDate = sourceDocketMetadata.getFilingDateAsString();
		if (StringUtils.isNotBlank(filingDate))
			sdmHash.put("FD", filingDate);
		//Common Hash Values that could be used in legacy.id creation
		sdmHash.put("CNTY", countyName);
		sdmHash.put("CASE", caseType);
		sdmHash.put("DN", docketNumber);
		sdmHash.put("MISC", miscellaneous);
		
		//The whole string from the config file
		String[] legacyIdRules = getLegacyIdFormat(courtClusterName).split("\\|");
		
		//Only the format information
		String legacyIdFormat[] = legacyIdRules[0].split(";");
		
		//Each of these will be run through a replaceAll with nothing
		String legacyIdRemoveChars[] = legacyIdRules[1].split(";");
		
		//Use the following if you need to move around parts of the docket number to construct the legacy id
		if (legacyIdRules.length > 2){
			String docketNumberRegEx = legacyIdRules[2];
			String allRegexPatterns[] = docketNumberRegEx.split(";", -1);
			for (String regexPattern : allRegexPatterns){
				String regexParts[] = regexPattern.split(":", -1);
				String regexCapture = regexParts[0];
				String regexReplace = regexParts[1];		
				if (docketNumber.matches(regexCapture)){
					sdmHash.put("DN", docketNumber.replaceAll(regexCapture, regexReplace));
					break;
				}
			}			
		}
		
		
		//Construct the legacy.id
		for(String legacyIdItem : legacyIdFormat){
			if(sdmHash.containsKey(legacyIdItem)){
				legacyId = legacyId + sdmHash.get(legacyIdItem);
			}else{
				legacyId = legacyId + legacyIdItem.replaceAll("'", "");
			}
		}
		
		//Remove unwanted characters
		for(String removeChar : legacyIdRemoveChars){
			legacyId = legacyId.replaceAll(removeChar, "");
		}
		
		return legacyId;
	}
	
	//If SourceDocketMetadata not available, this can be used instead but is more clunky
	public static String convertDktNumberToLegacyIdByCourt(String docketNumber, String caseType, String countyName, String clusterName){
		String legacyId = "";
		HashMap<String,String> sdmHash = new HashMap<String,String>();
		
		//Common Hash Values that could be used in legacy.id creation
		sdmHash.put("CNTY", countyName);
		sdmHash.put("CASE", caseType);
		sdmHash.put("DN", docketNumber);
		
		//The whole string from the config file
		String[] legacyIdRules = getLegacyIdFormat(clusterName).split("\\|");
		
		//Only the format information
		String legacyIdFormat[] = legacyIdRules[0].split(";");
		
		//Each of these will be run through a replaceAll with nothing
		String legacyIdRemoveChars[] = legacyIdRules[1].split(";");
		
		//Construct the legacy.id
		for(String legacyIdItem : legacyIdFormat){
			if(sdmHash.containsKey(legacyIdItem)){
				legacyId = legacyId + sdmHash.get(legacyIdItem);
			}else{
				legacyId = legacyId + legacyIdItem.replaceAll("'", "");
			}
		}
		
		//Remove unwanted characters
		for(String removeChar : legacyIdRemoveChars){
			legacyId = legacyId.replaceAll(removeChar, "");
		}
		
		return legacyId;
	}
	
	public static String getLegacyIdFormat(String clusterName){
		Properties props = new Properties();
		String configFileLoc = "/resources/LegacyIdFormats.xml";
		InputStream config = null;
		try {
			config = DocketServiceUtil.class.getResourceAsStream(configFileLoc);
			props.loadFromXML(config);
		} catch (Exception e1){
			e1.printStackTrace();
		}
		
		String legacyIdFormat = props.getProperty(clusterName);
		
		if (StringUtils.isNotBlank(legacyIdFormat)){
			return legacyIdFormat;
		}
		throw new NotImplementedException("Programming error/gap: could not determine the legacy id format for: " + clusterName);
	}
	
	
}
