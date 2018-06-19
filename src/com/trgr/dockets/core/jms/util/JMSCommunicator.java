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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


public class JMSCommunicator extends BaseJmsCommunicator
{
	@Autowired
	private JmsTemplate jmsTemplate;
	private Queue queue;
	private Destination replyTo;
	private String correlationId;
	private String messageText;
	private String destinationName;
    
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
			e.printStackTrace();
		}
	}
	public void setJmsTemplate(JmsTemplate jmsTemplate)
	{
		this.jmsTemplate = jmsTemplate;
	}
	
	public void setQueue(Queue queue)
	{
		this.queue = queue;
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

	public Queue getQueue() {
		return queue;
	}

	/**
	 * @return the destinatioName
	 */
	@Override
	public String getDestinationName() {
		if(this.getQueue()!=null)
		{
			try {
				destinationName = this.getQueue().getQueueName();
			} catch (JMSException e) {
			}
		}
		return destinationName;
	}

	/**
	 * @param destinatioName the destinatioName to set
	 */
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	} 
}
