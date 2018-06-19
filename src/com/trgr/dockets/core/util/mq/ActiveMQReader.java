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
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.trgr.dockets.core.exception.QueueException;

public class ActiveMQReader implements QueueReader {
	
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Destination destination;
	private MessageConsumer consumer;

	/**
	 * Opens a connection to a queue for reading messages.
	 * @param brokerUrl
	 * @param queueName
	 * @throws QueueException
	 */
	public void connect(String hostName, String queueManager, String queueName, Integer port) throws QueueException {
		try {
			factory = createFactory(hostName);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			consumer = session.createConsumer(destination);
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
	 * Reads the next message from the queue and returns it.
	 * @return
	 * @throws QueueException
	 */
	public Message readMessage() throws QueueException {
		try {
			return consumer.receiveNoWait();
		} catch (JMSException e) {
			throw new QueueException("Error while reading message; " + e.getMessage());
		}
	}
	
	/**
	 * Closes all queue connections.
	 * @throws QueueException
	 */
	public void close() throws QueueException {
		try {
			if (null != session) {
				session.close();
			}
			if (null != connection ) {
				connection.close();
			}
		} catch (JMSException e) {
			throw new QueueException("Error while trying to close connection; " + e.getMessage()); 
		}
	}

	public void reconnect() throws QueueException {
		try {
			close();
		} catch (QueueException qe) {
			//do nothing? who cares?
		}
		
		try {
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			consumer = session.createConsumer(destination);
		} catch (JMSException e) {
			throw new QueueException("Error reconnecting to queue; " + e.getMessage());
		}
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

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}
}
