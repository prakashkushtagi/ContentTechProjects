/*
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util.mq;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.trgr.dockets.core.exception.QueueException;

/**
 * A minimal and simple class for sending messages to websphere mq.
 */
public class WebsphereMQSender implements QueueSender {

	MQQueueConnectionFactory factory;
	Connection connection;
	Session session;
	Destination destination;
	MessageProducer producer;
	
	public void connect(String hostName, String queueManager, String queueName, Integer port) throws QueueException {
		try {
			factory = createFactory();

			//Config
			factory.setHostName(hostName);
			factory.setQueueManager(queueManager);
			factory.setChannel("CLIENTCONNECTION");
			factory.setPort(port);
			factory.setTransportType(1);
			
			//Connect
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
		} catch (JMSException e) {
			throw new QueueException("Error connecting to queue " + queueManager 
					+ " at " + hostName + "; " + e.getMessage());
		}
	}
	
	/**
	 * Encapsulating new factory call for testing purposes.
	 * @return
	 */
	public MQQueueConnectionFactory createFactory() {
		return new MQQueueConnectionFactory();
	}
	
	/**
	 * Sends a message to a queue.
	 * @param message
	 */
	public void sendMessage(String message) throws QueueException {
		try {
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText(message);
			producer.send(textMessage);
		} catch (JMSException e) {
			throw new QueueException("Error putting message on queue; " + e.getMessage());
		}
	}
	
	/**
	 * Closes all queue connections.
	 * @throws QueueException
	 */
	public void close() throws QueueException {
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			throw new QueueException("Error while trying to close connection; " + e.getMessage()); 
		}
	}

	public void reconnect() throws QueueException {
		//TODO: TO BE IMPLEMENTED
	}
	
	public void setFactory(MQQueueConnectionFactory factory) {
		this.factory = factory;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void setProducer(MessageProducer producer) {
		this.producer = producer;
	}
}
