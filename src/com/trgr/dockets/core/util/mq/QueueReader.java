/**
 *Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util.mq;

import javax.jms.Message;

import com.trgr.dockets.core.exception.QueueException;

public interface QueueReader {

	public void connect(String hostName, String queueManager, String queueName, Integer port) throws QueueException;
	
	public Message readMessage() throws QueueException;
	
	public void close() throws QueueException;
	
	public void reconnect() throws QueueException;
}
