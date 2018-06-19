/**
 * Copyright 2015: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and Confidential information of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
 * ********************************************************************
 *  PROPERTY OF WEST, A THOMSON BUSINESS
 *  
 *  COMPANY CONFIDENTIAL 
 *  
 *  "UNPUBLISHED--ALL RIGHTS RESERVED UNDER THE COPYRIGHT LAWS."
 *  "THE INFORMATION CONTAINED HEREIN IS PROPRIETARY, TRADE SECRET
 *  INFORMATION. ACCESS TO SAID INFORMATION IS STRICTLY LIMITED 
 *  TO SPECIFICALLY AUTHORIZED PERSONNEL. SAID INFORMATION 
 *  SHALL BE KEPT STRICTLY CONFIDENTIAL AND SECRET."
 *  
 *  Name        : ConnectionUtility.java
 *  @authors    : Tom Manion
 *  @Copyright  : 2013 West, A Thomson Business
 *  Purpose     : Utility to be used for external content.
 *  Notes       : Created 01/15/2013
 * ********************************************************************
 */
package com.trgr.dockets.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class ConnectionUtility {
	
	protected final static Logger log = Logger.getLogger(ConnectionUtility.class);
	
	public static final int CONNECT_TIMEOUT_IN_MS = 2000;
	public static final int READ_TIMEOUT_IN_MS = 120000;
	
	private ConnectionUtility(){}
	
	public static String doGetRequest(URL url) throws IOException {
		URLConnection urlConnection = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = in.readLine()) != null) {
			response.append(line + '\n');
		}
		return response.toString();
	}
	
	/**
	 * Returns response for given URL with connection timeout after {@value #CONNECT_TIMEOUT_IN_MS} milliseconds and read timeout after {@value #READ_TIMEOUT_IN_MS} milliseconds. 
	 * Will throw SocketTimeoutException in case of both timeouts.
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String doGetRequestWithReadTimeout(URL url) throws IOException {
		URLConnection urlConnection = url.openConnection();
		urlConnection.setConnectTimeout(CONNECT_TIMEOUT_IN_MS);
		urlConnection.setReadTimeout(READ_TIMEOUT_IN_MS);
		long startTime = System.currentTimeMillis();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		long elapsedTime = System.currentTimeMillis() - startTime;
		log.info(String.format("Elapsed time to return a response for request %s - %d ms", url.toString(), elapsedTime));
		String line;
		StringBuilder response = new StringBuilder();
		while ((line = in.readLine()) != null) {
			response.append(line + '\n');
		}
		return response.toString();
	}
}


