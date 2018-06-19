/*
 Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service("messageSenderService")
public class MessageSenderService
{
	private JmsTemplate jmsTemplate;
	private Destination ltcResponseQueue;
	
	public void send(String writer){
		LtcResponseCreator ltcResponseCreator = new LtcResponseCreator(writer);
	
		jmsTemplate.send(ltcResponseQueue, ltcResponseCreator);
	
	}

	/**
	 * @return the jmsTemplate
	 */
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	/**
	 * @param jmsTemplate the jmsTemplate to set
	 */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * @return the ltcResponseQueue
	 */
	public Destination getLtcResponseQueue() {
		return ltcResponseQueue;
	}

	/**
	 * @param ltcResponseQueue the ltcResponseQueue to set
	 */
	public void setLtcResponseQueue(Destination ltcResponseQueue) {
		this.ltcResponseQueue = ltcResponseQueue;
	}

}


class LtcResponseCreator implements MessageCreator{
	private String writer;

	public LtcResponseCreator(String writer) {
		super();
		this.writer = writer;
	}

	public Message createMessage(Session session) throws JMSException{
		Message message = session.createTextMessage(writer);
		return message;
	}
}