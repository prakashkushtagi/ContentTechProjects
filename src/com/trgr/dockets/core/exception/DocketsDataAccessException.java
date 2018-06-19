/*
Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.exception;

/**
 * KCCMDataAccessException.
 * This class is used for KCCM DAO classes .	
 */
public class DocketsDataAccessException extends Exception
{
	
	private static final long serialVersionUID = 1L;
	String errorMessage;
	
	public DocketsDataAccessException(String errorMessage){
		super();
		this.errorMessage = errorMessage;
	}
	
	public DocketsDataAccessException(String message, Throwable cause){
		super(message, cause);
	}
}
