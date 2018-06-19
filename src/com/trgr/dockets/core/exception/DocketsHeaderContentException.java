/*
Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.exception;

/**
 * DocketsHeaderContentException.
 * This class is used for notifying and dropping any dockets with key header information 
 * like County code, scrape date.	
 */
public class DocketsHeaderContentException extends Exception
{

	private static final long serialVersionUID = 1L;
	String errorMessage;
	
	public DocketsHeaderContentException(String errorMessage){
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public DocketsHeaderContentException(String message, Throwable cause){
		super(message, cause);
	}
	
}
