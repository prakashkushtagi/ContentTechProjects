/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.exception;


public class ContentParserException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private String errorMessage;

	public ContentParserException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public ContentParserException(String message, Throwable cause) {
        super(message, cause);
    }

	@Override
	public String toString() {
		return "ContentParseException [errorMessage=" + errorMessage
				+ "]";
	}
	
	

}
