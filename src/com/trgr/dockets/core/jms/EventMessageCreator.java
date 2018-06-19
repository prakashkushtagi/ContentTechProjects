/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class EventMessageCreator implements MessageCreator {

	String writer; 
	
	public EventMessageCreator(String writer) {
		super();
		this.writer = writer;
	}

	public Message createMessage(Session session) throws JMSException{
		Message message = session.createTextMessage(writer);
		return message;
	}


}
