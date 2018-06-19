/*
 * Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.exception;

/**
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com) 
 * class raising all the exception while parsing dockets xmls. 
 */
public class SourceContentLoaderException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public SourceContentLoaderException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	public SourceContentLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

	@Override
	public String toString() {
		return "SourceContentLoaderException [errorMessage=" + errorMessage
				+ "]";
	}
	
	

}
