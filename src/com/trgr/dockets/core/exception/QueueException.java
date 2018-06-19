/*
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */

package com.trgr.dockets.core.exception;

public class QueueException extends Exception {

	private static final long serialVersionUID = 1162767477654814003L;

	public QueueException(String message)
	{
		super(message);
	}
}
