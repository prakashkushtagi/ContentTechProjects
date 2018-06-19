/*
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */

package com.trgr.dockets.core.util.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.trgr.dockets.core.exception.QueueException;

public class ActiveMQSender implements QueueSender {
	
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageProducer producer;
	
	/**
	 * Opens a connection to a queue for sending messages.
	 * @param brokerUrl
	 * @param queueName
	 */
	public void connect(String hostName, String queueManager, String queueName, Integer port) throws QueueException {
		try {
			factory = createFactory(hostName);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
		} catch (JMSException e) {
			throw new QueueException("Error connecting to queue " + queueName 
					+ " at " + hostName + "; " + e.getMessage());
		}
	}
	
	/**
	 * Encapsulating new factory call for testing purposes.
	 * @param brokerUrl
	 * @return
	 */
	public ConnectionFactory createFactory(String brokerUrl) {
		return new ActiveMQConnectionFactory(brokerUrl);
	}
	
	/**
	 * Sends a message to a queue.
	 * @param message
	 */
	public void sendMessage(String message) throws QueueException{
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
	
	public void setFactory(ConnectionFactory factory) {
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
