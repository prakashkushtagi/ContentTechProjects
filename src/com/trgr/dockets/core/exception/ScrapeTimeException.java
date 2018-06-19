/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.exception;

public class ScrapeTimeException extends Exception {
	
	private static final long serialVersionUID = 1L;
	String errorMessage;
	
	public ScrapeTimeException(String errorMessage){
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public ScrapeTimeException(String message, Throwable cause){
		super(message, cause);
	}

}
