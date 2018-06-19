package com.trgr.dockets.core.jms.util;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


public abstract class BaseJmsCommunicator 
{
	   private JmsTemplate jmsTemplate;
	   private String destinationName;

	   /**
	    * This method sends the message to destination queue defined in spring xml.
	    */
	   public abstract void sendMessage(final String textMessage) throws JMSException;

	   /**
	    * This method create a jms message from KCCM message passed in as an argument.
	    * @param message
	    * @return
	    */
	   protected MessageCreator createMessageCreator(final String textMessage) {

	         MessageCreator messageCreator = new MessageCreator() {
	               public javax.jms.TextMessage createMessage(Session session)
	                           throws JMSException {
	                     TextMessage txtMessage = session.createTextMessage();
	                     txtMessage.setJMSType(TextMessage.class.getName());
	                     txtMessage.setText(textMessage);
	                     return txtMessage;
	               }
	         };
	         return messageCreator;
	   }

	   /**
	    * @return the jmsTemplate
	    */
	   public JmsTemplate getJmsTemplate() {
	         return jmsTemplate;
	   }


	   /**
	    * @param jmsTemplate
	    *            the jmsTemplate to set
	    */
	   public void setJmsTemplate(JmsTemplate jmsTemplate) {
	         this.jmsTemplate = jmsTemplate;
	   }

	/**
	 * @return the destinationName
	 */
	public String getDestinationName() {
		return destinationName;
	}

	/**
	 * @param destinationName the destinationName to set
	 */
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

}

