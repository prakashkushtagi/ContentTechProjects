/**
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and 
 * Confidential information of TRGR. Disclosure, Use or Reproduction without the 
 * written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.jms.util;

import javax.jms.Queue;

import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;


public class DestinationQueue {

	MQQueueConnection connection;
	
	MQQueueSession session;
	
	Queue queue;
	
	MQQueueSender sender;

	public MQQueueConnection getConnection() {
		return connection;
	}

	public void setConnection(MQQueueConnection connection) {
		this.connection = connection;
	}

	public MQQueueSession getSession() {
		return session;
	}

	public void setSession(MQQueueSession session) {
		this.session = session;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public MQQueueSender getSender() {
		return sender;
	}

	public void setSender(MQQueueSender sender) {
		this.sender = sender;
	}
}