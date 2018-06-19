/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Court;
import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.DroppedDocket;
import com.trgr.dockets.core.exception.SourceContentLoaderException;

public class DocketsCoreUtility {

	private static final Logger LOG = Logger.getLogger(DocketsCoreUtility.class);
	private static final String FBR_DATE_PATTERN = "ddMMyyyyHHmmss";
	private static final String FBR_DATE_PATTERN_2 = "yyyyMMdd";
	private static final String JPML_DATE_PATTERN = "yyyyMMddHHmmss";
	private static final String EVENT_DATE_PATTERN="MM/dd/yyyy hh:mm:ss.SSSSSS a";
	private static final String DEFAULT_DATE_FOR_MISSING_SCRAPE_DATE ="01011981010101";
	
	
	public static void main(String args[]) throws ParseException, SourceContentLoaderException{
	
		LOG.info(" even end time : "+getEventsStartTimeStamp("25092012013253"));
		
	}
	public enum KeysEnum{
		locationCode,year,product,sequenceNo,docketNo;
		KeysEnum Keys;
		}
	
	/**
	 * Utility method to parse docketNumber and extract various values like locationcode, 
	 * year, product code,sequence number.
	 * 
	 * @return
	 */
	public static HashMap<String, String> docketNumberParsing(String docketNumber,String productType){
        productType = productType.toUpperCase();
        HashMap<String, String> values = null;//new HashMap<String, String>();
        int productNo = 0;
        	productNo = getProductNo(productType);
        switch (productNo) {
            case 1:	 //"FBR"		
            		 values = processBrkDocketsNumber(docketNumber);
                     break;
            case 2:	 //"JPML"
            		 values = processJpmlDocketsNumber(docketNumber);
            		 break;
            case 3:  //"NY"
            		 processNYDocketsNumber(docketNumber);
                     break;
            default: productType = "FBR";
            		 values = processBrkDocketsNumber(docketNumber);
                     break;
        }

		  
		return values;
	}
	
	
	private static int getProductNo(String productType) {
		int productNo = 0 ;
		if(productType.equalsIgnoreCase("FBR")){
			productNo  =1;
		}
		if(productType.equalsIgnoreCase("JPML")){
			productNo  =2;
		}
		if(productType.equalsIgnoreCase("NY")){
			productNo  =3;
		}

		return productNo;
	}


	private static void processNYDocketsNumber(String docketNumber) {
		// TODO Auto-generated method stub
		
	}

	private static HashMap<String, String> processJpmlDocketsNumber(String docketNumber) {
		HashMap<String, String> valueMap = new HashMap<String, String>();
		String sequenceNumber = null;
		if(docketNumber.contains("MDLNo")){
			char[] charArray = docketNumber.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if(Character.isDigit(charArray[i])){
					if(sequenceNumber == null){
						sequenceNumber = String.valueOf(charArray[i]);
					}else{
						sequenceNumber += charArray[i];
					}
				}
				
			}

			valueMap.put("sequenceNo", sequenceNumber);
			valueMap.put("docketNo", docketNumber);

		}
		
		return valueMap;
	}
	/**
	 * In case of bankruptcy "2012bk014174" for delete LCI/Oragone delete scenario docket number will always 
	 * be with out location code i.e "nn:"     
	 * @param docketNumber
	 * @return
	 */
	private static HashMap<String, String> processBrkDocketsNumber(
			String docketNumber) {
		
		char[] newStringArray = docketNumber.toCharArray();

		String year = null ; 
		String product= null;
		String sequenceNo = null;
		for(int index=0; index < newStringArray.length; index++)
		 {
			
			/************* extract year ***************/
			if(index <4){
				if(year == null){
					year = String.valueOf(newStringArray[index]);	
				}else{
					year += String.valueOf(newStringArray[index]);
				}
				
			}
			
			/************* extract product ***************/
			if(Character.isLetter(newStringArray[index])){
				if(product == null){
					product = String.valueOf(newStringArray[index]);	
				}else{
					product += String.valueOf(newStringArray[index]);
				}
				
			}

			/************* extract sequence ***************/
			if(Character.isDigit(newStringArray[index]) && index > 3){
				if(sequenceNo == null){
					sequenceNo = String.valueOf(newStringArray[index]);	
				}else{
					sequenceNo += String.valueOf(newStringArray[index]);
				}
				
			}

    
		 }
		HashMap<String, String> values  = new HashMap<String,String>();
	
		values.put("year",year);
		values.put("product",product);
		values.put("sequenceNo",sequenceNo);
		values.put("docketNo",docketNumber);
		
		return values;
	}
	/**
	 * 
	 * @param acquisitionTimeStamp
	 * @return
	 * @throws SourceContentLoaderException
	 */
	public static String createAcquisitionTimeStamp(Date acquisitionTimeStamp,String product ){
		
		
		String formatedAcquisitionTime= null;
		SimpleDateFormat format = null;
		if(product.equalsIgnoreCase("FBR")) {
			format = new SimpleDateFormat(FBR_DATE_PATTERN);	
		}else if(product.equalsIgnoreCase("JPML")){
			format = new SimpleDateFormat(JPML_DATE_PATTERN);
		} else {
			throw new IllegalArgumentException("Unexpected product type: " + product);
		}
		
	    formatedAcquisitionTime =  format.format(acquisitionTimeStamp); 
	    return formatedAcquisitionTime;
	}
	/**
	 * 
	 * @param acquisitionTimeStamp
	 * @return
	 * @throws SourceContentLoaderException
	 */
	public static Date getAcquisitionTimeStamp(String acquisitionTimeStamp,String product ){
		
		Date formatedAcquisitionTime= null;
		DateFormat format = null;
		if(product.equalsIgnoreCase("FBR")) {
			format = new SimpleDateFormat(FBR_DATE_PATTERN);	
		}else if(product.equalsIgnoreCase("JPML")){
			format = new SimpleDateFormat(JPML_DATE_PATTERN);
		} else {
			throw new IllegalArgumentException("Unexpected product type: " + product);
		}
		
	    try {
	    	formatedAcquisitionTime =  format.parse(acquisitionTimeStamp);
		} catch (ParseException e) {
			LOG.debug("Failed to parse date :" +e);	
		} 
	    return formatedAcquisitionTime;
	}

	/**
	 * Returns Court cluster-norm key value pair map. 
	 * 
	 * @param courtList2
	 * @return
	 */
	public static HashMap<String, String> getCourtClusterNormMap(
			List<Court> courtList2) {
		
		HashMap<String,String> courtMap= new HashMap<String,String>();
		for (Court court : courtList2) {
			courtMap.put(court.getCourtCluster() ,court.getCourtNorm());
		}
		
		return courtMap;
	}
	/**
	 * gets file name for passed on file Path + Name
	 * platform unix /windows independent way as different platforms will have different 
	 * representation of path /\
	 * @param filePath
	 * @return
	 */
	public static String getFileName_File( String filePath){
		
		String fileName = null;
		/**  /app/dockets/content/lexisRetrospective.xml */
			File file = new File(filePath);
			fileName  = file.getName();
			
		return fileName;
	}


	/**
	 * gets current timestamp in dockets prefered format i.e ddMMyyyyhhmmss  
	 * @return
	 */
	public static String getCurrentTimeStamp(){
		
		SimpleDateFormat s = new SimpleDateFormat(FBR_DATE_PATTERN); 
		String format = s.format(new Date());
		
		return format;
	}
	
	/**
	 * Converts passed in request date to events dateformat.   
	 * @return
	 * @throws ParseException 
	 */
	public static String getEventsStartTimeStamp(String strDate) throws ParseException{
		
		Date inputDate = null;
		SimpleDateFormat inputFormat = new SimpleDateFormat(FBR_DATE_PATTERN);
		inputDate =  inputFormat.parse(strDate);
		
		SimpleDateFormat s = new SimpleDateFormat(EVENT_DATE_PATTERN); 
		String format = s.format(inputDate);
		
		return format;
	}
	/**
	 * gets date object in Source request date format.
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date getSourceRequestDateObject(String strDate) throws ParseException{
		
		Date inputDate = null;
		/** 26-SEP-94 06.21.51.156000000 AM **/
		SimpleDateFormat inputFormat = new SimpleDateFormat(FBR_DATE_PATTERN);
		inputDate =  inputFormat.parse(strDate);
		return inputDate;
	}

	
	/**
	 * gets current timestamp in events date format i.e MM/dd/yyyy HH:mm:ss.SSSSSS a  
	 * @return
	 */
	public static String getEventsCurrentTimeStamp(){
		
		SimpleDateFormat s = new SimpleDateFormat(EVENT_DATE_PATTERN);
		String format = s.format(new Date());
		
		return format;
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath){
		
		String fileName = null;
		/** /app/dockets/content/lexisRetrospective.xml */
		File file = new File(filePath);
		fileName =file.getName();
		return fileName;
	}
	
	
	/**
	 * Converts String date to timeStamp object based on product type.
	 * @param sourceDocket
	 * @return
	 * @throws SourceContentLoaderException
	 */
	public static Date getFormatedTimeStamp(String stringDate,String product )
			throws SourceContentLoaderException {
		 
		Date scrapeTimestamp = null;
		
		if (null == stringDate || stringDate.trim().equalsIgnoreCase("")) {
			stringDate = DEFAULT_DATE_FOR_MISSING_SCRAPE_DATE;// default date if scrapedate is missing in merge file.
		}

		if(product.equalsIgnoreCase("FBR")){
			scrapeTimestamp = getFormatedTimeStampFor_FBR(stringDate);	
		}else if(product.equalsIgnoreCase("JPML")){
			scrapeTimestamp = getFormatedTimeStampFor_JPML(stringDate);
		}
		
		return scrapeTimestamp;
	}

	public static Date getFormatedTimeStampFor_FBR(String stringDate) throws SourceContentLoaderException{

		Date scrapeTimestamp = null;
		boolean tryOtherFormat = false;
		try {

			scrapeTimestamp =(Date) new SimpleDateFormat(FBR_DATE_PATTERN).parse(stringDate);
		} catch (ParseException e) {
			tryOtherFormat = true;
		}
		
		try {

			if (tryOtherFormat) {
				scrapeTimestamp = (Date) new SimpleDateFormat(FBR_DATE_PATTERN_2)
						.parse(stringDate);
			}
		} catch (ParseException e) {
			
			LOG.debug("Failed to parse FBR scrapeDateFormat.Expecting date in format ="+FBR_DATE_PATTERN +" or "+FBR_DATE_PATTERN_2);	
			throw new SourceContentLoaderException("Failed to parse FBR scrapeDateFormat.Expecting date in format ="+FBR_DATE_PATTERN +" or "+FBR_DATE_PATTERN_2 , e);
		}

		return scrapeTimestamp;

	}
	
	public static Date getFormatedTimeStampFor_JPML(String stringDate)
			throws SourceContentLoaderException {


		Date scrapeTimestamp = null;

		try {
			scrapeTimestamp = (Date) new SimpleDateFormat(JPML_DATE_PATTERN).parse(stringDate);
		} catch (ParseException e) {
			LOG.debug("Failed to parse JPML scrapeDateFormat.Expecting date in format ="+JPML_DATE_PATTERN);
			throw new SourceContentLoaderException(
					"Failed to parse JPML scrapeDateFormat.Expecting date in format ="+JPML_DATE_PATTERN , e);
		}

		return scrapeTimestamp;

	}
	
	/**
	 * Get a jpml docket number from jpml legacy id.
	 * Example: A legacy id of JPML1990 is converted to a docket number MDLNo1990
	 * 
	 * @param legacyId
	 * @return
	 */
	public static final String createDocketNumberFromJPMLLegacyId(String legacyId){
		String docketNumber = null;
		String sequenceNumber = null;
		if(null != legacyId && legacyId.contains("JPML")){
			char[] charArray = legacyId.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if(Character.isDigit(charArray[i])){
					if(sequenceNumber == null){
						sequenceNumber = String.valueOf(charArray[i]);
					}else{
						sequenceNumber += charArray[i];
					}
				}
			}
		}
		if(null != sequenceNumber){
			docketNumber = "MDLNo" + sequenceNumber;
		}
		return docketNumber;	
	}

	public static String createDroppedDocketXml(List<DroppedDocket> docketList){
		StringBuffer sb = new StringBuffer();
		if(docketList != null && docketList.size()>0)
		{
			XMLUtility.createOpenElement("dropped.dockets.info",sb);
			
//			sb.append("<dropped.dockets.info>\n");
			for(DroppedDocket droppedDocket : docketList)
			{
				Map<String,String> attributeMap = new LinkedHashMap<String,String>();
//				sb.append(" <docket");
				if(droppedDocket.getCourt() != null) 
				{
					String courtValue = "";
					
					if(droppedDocket.getCourt().indexOf('-')!=-1)
					{
						courtValue = droppedDocket.getCourt();
					}
					else if(droppedDocket.getCourt().indexOf('_')!=-1)
					{
						courtValue = droppedDocket.getCourt().substring(3, 5)+ "-" + droppedDocket.getCourt().substring(5);
					}
					else
					{
						courtValue = droppedDocket.getCourt();
					}
					attributeMap.put("court_norm", courtValue);
				}
				if(droppedDocket.getDocketNumber()!=null)
				{
					attributeMap.put("number", droppedDocket.getDocketNumber());
				}
				String reason="";
				if(droppedDocket.reason()!=null && droppedDocket.reason().trim().length() != 0)
				{
					reason=droppedDocket.reason();
				}
				else
				{
					reason="Unspecified Error";
				}
				attributeMap.put("reason", reason);
				XMLUtility.createElementWithAttributes("docket", null, attributeMap, sb, true);
			}
			XMLUtility.createCloseElement("dropped.dockets.info", sb);
		}
		return sb.toString();
	}
	
	
	public static String prepareBkrLegacyID(String docketNumber,String court)
	{
		String legacyID=null;
		// Bug #50888 - (Else part)BKR Redev - Dockets without location code are not loading into Content repository
		if(docketNumber.contains(":")){
			String str[]=docketNumber.split("\\:");
			String strDocketNumberNew=str[0]+":"+str[1].substring(2, 4)+str[1].substring(4).toUpperCase();
			String strState=court.replaceAll("[Nn]_[Dd]", "").replaceAll("[bB][Kk][Rr]", "").toUpperCase().replace("-", "");
			if(strState.length()<=2)
			{
				legacyID=strState+"-BKR"+strDocketNumberNew;
			}
			else if(strState.length()>2)
			{
				legacyID=strState.substring(0,2)+"-"+strState.substring(2)+"BKR"+strDocketNumberNew;
			}
		}else{
			String str=docketNumber;
			String strDocketNumberNew=str.substring(2, 4)+str.substring(4).toUpperCase();
			String strState=court.replaceAll("[Nn]_[Dd]", "").replaceAll("[bB][Kk][Rr]", "").toUpperCase();
			if(strState.length()<=2)
			{
				legacyID=strState+"-BKR"+strDocketNumberNew;
			}
			else //strState.length()>2
			{
				legacyID=strState.substring(0,2)+"-"+strState.substring(2)+"BKR"+strDocketNumberNew;
			}
		}
		return legacyID.replace("--", "-");
	}
	
	/**
	 * Input NYCCCCNNNNNNYYYY (i.e. NYQUEENS00246162009)	
	 * Output NYCCCCNNNNNN/YYYYTTTTT (i.e. NYQUEENS0024616/2009SUPREME)
	 * 
	 * @param newNyDownstateLegacyId
	 * @return
	 */
	public static String getOldNyDownstateLegacyId(String newNyDownstateLegacyId){
		int newNyDownstateLegacyIdLength = newNyDownstateLegacyId.length();
		StringBuilder oldNyDownstateLegacyIdBuilder = new StringBuilder();
		oldNyDownstateLegacyIdBuilder.append(newNyDownstateLegacyId.substring(0, newNyDownstateLegacyIdLength-4));
		oldNyDownstateLegacyIdBuilder.append("/");
		oldNyDownstateLegacyIdBuilder.append(newNyDownstateLegacyId.substring(newNyDownstateLegacyIdLength-4));
		oldNyDownstateLegacyIdBuilder.append("SUPREME");
		return oldNyDownstateLegacyIdBuilder.toString();
	}
	
	/**
	 * Input NYCCCCNNNNNNYYYY (i.e. NYQUEENS00246162009)
	 * Output NYCCCCNNNNNN/YYYYTTTTT (i.e. NYQUEENS0024616/2009SUPREMECIVIL)
	 *  
	 * @param newNyDownstateLegacyId
	 * @return
	 */
	public static String getOldNyUpstateLegacyId(String newNyDownstateLegacyId){
		int newNyDownstateLegacyIdLength = newNyDownstateLegacyId.length();
		StringBuilder oldNyDownstateLegacyIdBuilder = new StringBuilder();
		oldNyDownstateLegacyIdBuilder.append(newNyDownstateLegacyId.substring(0, newNyDownstateLegacyIdLength-4));
		oldNyDownstateLegacyIdBuilder.append("/");
		oldNyDownstateLegacyIdBuilder.append(newNyDownstateLegacyId.substring(newNyDownstateLegacyIdLength-4));
		oldNyDownstateLegacyIdBuilder.append("SUPREMECIVIL");
		return oldNyDownstateLegacyIdBuilder.toString();
	}
	
	/**
	 *	Input NYccccCCNNNNNNYYYY (i.e. NYNEWYORKCC6023411998)
	 * 	Output NYccNNNNNN/YYYY (i.e. NYNEWYORK602341/1998)
	 * 
	 * @param newNyDownstateLegacyId
	 * @return
	 */
	public static String getOldNyCountyClerkLegacyId(String newNyDownstateLegacyId){
		int newNyDownstateLegacyIdLength = newNyDownstateLegacyId.length();
		StringBuilder oldNyDownstateLegacyIdBuilder = new StringBuilder();
		oldNyDownstateLegacyIdBuilder.append(newNyDownstateLegacyId.substring(0, newNyDownstateLegacyIdLength-4));
		oldNyDownstateLegacyIdBuilder.append("/");
		oldNyDownstateLegacyIdBuilder.append(newNyDownstateLegacyId.substring(newNyDownstateLegacyIdLength-4));
		String oldNyDownstateLegacyId = oldNyDownstateLegacyIdBuilder.toString().replace("CC", "");
		return oldNyDownstateLegacyId;
	}
	
	/** Input MDL No. 1456 (JPML Docket number with spaces)
	 * Output MDL-NO.-1456 (JPML Docket Number without spces)
	 * Only used for JPML filters
	 */
	public static String replaceSpacesHyphens( String origDocketNumber){
		String hyphenedDocketNumber = null;
		Pattern p = Pattern.compile("((\\w:)?\\d{2})(\\w{2})(\\d{5})");
		Matcher m = p.matcher(origDocketNumber);
		if (origDocketNumber != null){
			if(m.find()){
				hyphenedDocketNumber = m.group(1) + "-" + m.group(3) + "-" + m.group(4);
				
			}else{
				hyphenedDocketNumber = origDocketNumber.replaceAll(" ", "-");
			}
		}
		return hyphenedDocketNumber;
	}
}
