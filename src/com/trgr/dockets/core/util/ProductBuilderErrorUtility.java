/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 Proprietary and Confidential information of Thomson Reuters. 
 Disclosure, Use or Reproduction without the written 
 authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.net.ConnectException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.trgr.dockets.core.util.Environment;

public class ProductBuilderErrorUtility {

	private static final String ERROR_REPORTING_URL_BASE = "http://"+ getFormattedEnvironment()	+ "/ProductBuilderUI/productbuildererror";
	private static final String ERROR_CODE_PARAM_BASE = "?errorCode=";
	private static final String RECEIPT_DATE_PARAM_BASE = "&receiptDate=";
	private static final String DOCKET_NUMBER_PARAM_BASE = "&docketNumber=";
	private static final String COURT_ID_PARAM_BASE = "&courtId=";
	private static final String FILING_COUNTY_PARAM_BASE = "&filingCounty=";
	private static final String CASE_TYPE_PARAM_BASE = "&caseType=";
	private static final String CASE_SUBTYPE_PARAM_BASE = "&caseSubType=";
	private static final String DESCRIPTION_PARAM_BASE = "&description=";
	private static final String PRODUCT_PARAM_BASE = "&product=";
	private static final String LEGACY_ID_PARAM_BASE = "&legacyId=";
	private static final int MAX_LENGTH_ERROR = 199;

	private static Logger log = Logger.getLogger(ProductBuilderErrorUtility.class);

	public ProductBuilderErrorUtility() {

	}

	/**
	 * @param ERROR_CODE
	 * @param docketLine
	 * @param stateSubdivision
	 * @param caseType
	 * @param caseSubtype
	 * @param errorDescription
	 * @param courtId
	 * @param productDescription
	 * @return
	 * @throws Exception
	 */
	public static String formatAndSendReport(String ERROR_CODE,	String docketLine, String stateSubdivision, String caseType,
			String caseSubtype, String errorDescription, String courtId, String productDescription,String legacyId) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (StringUtils.isNotBlank(stateSubdivision)) {
			stateSubdivision = URLEncoder.encode(stateSubdivision, "UTF-8");
		}
		if (StringUtils.isNotBlank(caseSubtype)) {
			caseSubtype = URLEncoder.encode(caseSubtype, "UTF-8");
		}
		sb.append(ERROR_REPORTING_URL_BASE)
			.append(ERROR_CODE_PARAM_BASE + ERROR_CODE)
			.append(RECEIPT_DATE_PARAM_BASE + buildRequestDate())
			.append(DOCKET_NUMBER_PARAM_BASE + convertNullToMissingString(docketLine, "Docket"))
			.append(COURT_ID_PARAM_BASE + courtId)
			.append(FILING_COUNTY_PARAM_BASE + convertNullToMissingString(stateSubdivision, "County"))
			.append(CASE_TYPE_PARAM_BASE + convertNullToMissingString(caseType, "CaseType"))
			.append(CASE_SUBTYPE_PARAM_BASE + convertNullToEmptyString(caseSubtype))
			.append(DESCRIPTION_PARAM_BASE + convertNullToEmptyString(trimToFit(errorDescription, MAX_LENGTH_ERROR)))
			.append(PRODUCT_PARAM_BASE + productDescription)
			.append(LEGACY_ID_PARAM_BASE + legacyId);
		String request = sb.toString().replaceAll("\\s", "%20");

		String response = "";
		try {
			response = ConnectionUtility.doGetRequest(new URL(request));
		} catch (ConnectException ce) {
			log.error("Unable to connect to " + request + " for sending of error report.");
		}
		
		if (response.contains("returnCode=\"Error\"")) {
			log.warn(ERROR_CODE + " ERROR Request: " + request);
			log.warn(ERROR_CODE + " ERROR Response: " + response);
			throw new Exception(response);
		}
		return (response);
	}

	public static String formatAndSendReport(String ERROR_CODE,	String docketLine, String stateSubdivision, String caseType,
			String caseSubtype, String errorDescription, int courtId, String productDescription,String legacyId) throws Exception {
		
		return formatAndSendReport(ERROR_CODE,	docketLine, stateSubdivision, caseType,
				caseSubtype, errorDescription, String.valueOf(courtId), productDescription,legacyId);
		
	}
	private static String getFormattedEnvironment() {
		if (Environment.getInstance().getEnv().toLowerCase() == null)
			return "dev.productbuilderui.dockets.int.westgroup.com:8180";

		String envString = Environment.getInstance().getEnv().toLowerCase();
		if (envString.contains("uat"))
			return "uat.productbuilderui.dockets.int.westgroup.com:8180";
		else if (envString.contains("test"))
			return "test.productbuilderui.dockets.int.westgroup.com:8180";
		else if (envString.contains("qa"))
			return "qa.productbuilderui.dockets.int.westgroup.com";
		else if (envString.contains("prod"))
			return "prod.productbuilderui.dockets.int.westgroup.com";
		else if (envString.contains("dev")) 
			return "dev.productbuilderui.dockets.int.westgroup.com:8180";
		else
			return "localhost:8080";
	}

	public static String convertNullToEmptyString(String value) {
		if (value != null) {
			return value.trim();
		} else {
			return "";
		}
	}

	public static String convertNullToMissingString(String value, String type) {
		if (value != null) {
			return value.trim();
		} else {
			return "Missing" + type;
		}
	}
	/**
	 * Format to month/day/year 24hour:min:sec from current time
	 * 
	 * @return
	 */	private static String buildRequestDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String strDate = sdf.format(cal.getTime());

		// i.e.: 09/30/2013 09:22:53

		return strDate;
	}

	/**
	 * DB table DOCKETS_PUB.PRODUCT_BUILDER_ERRORS doesn't allow error
	 * description more than 200 characters trim data before sending it to
	 * 
	 * @param content
	 * @param maxSize
	 * @return
	 */
	private static String trimToFit(String content, int maxSize) throws Exception {
		String trimmedContent;
		if (StringUtils.isNotBlank(content) && content.length() > maxSize) {
			trimmedContent = content.substring(0,
					content.length() - (content.length() - maxSize));
		} else {
			trimmedContent = content;
		}
		trimmedContent = URLEncoder.encode(trimmedContent, "UTF-8");
		return trimmedContent;
	}
}
