/** Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.Court;


public class AcquisitionErrorUtility {
	
	private static final String ERROR_REPORTING_URL_BASE = "http://"+ getFormattedEnvironment()	+ "/ProductBuilderUI/acquisitionerror";
	private static final String RECEIPT_DATE_PARAM_BASE = "?receiptDate=";
	private static final String COURT_ID_PARAM_BASE = "&courtId=";
	private static final String DESCRIPTION_PARAM_BASE = "&description=";
	private static final String PRODUCT_PARAM_BASE = "&product=";
	private static final int MAX_LENGTH_ERROR = 199;

	private static Logger log = Logger.getLogger(AcquisitionErrorUtility.class);
	
	public static String formatAndSendReport(Court court, StatusEnum status, String courtErrorMessage) throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(ERROR_REPORTING_URL_BASE)
			.append(RECEIPT_DATE_PARAM_BASE + buildRequestDate())
			.append(COURT_ID_PARAM_BASE + court.getCourtConfig().getCourtId())
			.append(DESCRIPTION_PARAM_BASE + convertNullToEmptyString(trimToFit(courtErrorMessage, MAX_LENGTH_ERROR)))
			.append(PRODUCT_PARAM_BASE + court.getProduct().getDisplayName());
		String request = sb.toString().replaceAll("\\s", "%20");

		String response = doGetRequest(new URL(request));
		if (response.contains("returnCode=\"Error\"")) {
			log.warn("ERROR Request: " + request);
			log.warn("ERROR Response: " + response);
			throw new Exception(response);
		}
		return (response);
	}
	
	private static String doGetRequest(URL url) throws IOException {
		URLConnection urlConnection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = in.readLine()) != null) {
			response.append(line + '\n');
		}
		return response.toString();
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
		else
			return "dev.productbuilderui.dockets.int.westgroup.com:8180";
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
	 * DB table DOCKETS_PUB.ACQUISITION_ERRORS doesn't allow error
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


