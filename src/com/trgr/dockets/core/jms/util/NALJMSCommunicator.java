/**
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and 
 * Confidential information of TRGR. Disclosure, Use or Reproduction without the 
 * written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.jms.util;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;



public class NALJMSCommunicator extends BaseJmsCommunicator
{
	private JmsTemplate jmsTemplate;
	private Queue queue;
	private Destination replyTo;
	private String correlationId;
	private String messageText;  
    
	public void sendMessage() throws JmsException
	{
		jmsTemplate.send(queue, new MessageCreator()
		{
			public Message createMessage(Session session) throws JMSException
			{
				Message msg = session.createTextMessage();
				msg.setJMSCorrelationID(correlationId); 
				
				if(replyTo != null)
				{
					msg.setJMSReplyTo(replyTo);
				}
				
				return session.createTextMessage(messageText);  
			}
		});
	}

	@Override
	public void sendMessage(String textMessage) throws JMSException
	{
		setMessageText(textMessage);
		try 
		{
			jmsTemplate.send(queue, new MessageCreator()
			{
				public Message createMessage(Session session) throws JMSException
				{
					Message msg = session.createTextMessage();
					msg.setJMSCorrelationID(correlationId); 
					
					if(replyTo != null)
					{
						msg.setJMSReplyTo(replyTo);
					}
					
					return session.createTextMessage(getMessageText());  
				}
			});
		} 
		catch (JmsException e) 
		{

		}
	}
	public void setJmsTemplate(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}

	public void setReplyTo(Destination replyQueue)
	{
		this.replyTo = replyQueue;
	}
	
	public void setCorrelationId(String id)
	{
		this.correlationId = id;
	}
	
	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}  

	public String getMessageText()
	{
		return this.messageText;
	}

	/**
	 * @return the queueName
	 */
	public Queue getQueue() {
		return queue;
	}

	/**
	 * @param queueName the queueName to set
	 */
	public void setQueue(Queue queue) {
		this.queue = queue;
	}



}
