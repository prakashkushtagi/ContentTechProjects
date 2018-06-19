/*
Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.exception;

/**
 * @author u0160964 Sagun Khanal(Sagun.Khanal@thomsonreuters.com) 
 *
 */

public class LoadMonitoringException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public LoadMonitoringException(String eventMessage){
		super(eventMessage);
	}
	
	public LoadMonitoringException(String message, Throwable cause){
		super(message, cause);
	}

}
