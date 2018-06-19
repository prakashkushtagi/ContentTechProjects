/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.exception;

public class NoSourceException extends Exception{
	
	private static final long serialVersionUID = 1L;
	String errorMessage;
	
	public NoSourceException(String errorMessage){
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public NoSourceException(String message, Throwable cause){
		super(message, cause);
	}
}