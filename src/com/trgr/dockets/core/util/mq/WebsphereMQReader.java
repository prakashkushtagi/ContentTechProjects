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
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.trgr.dockets.core.exception.QueueException;

public class WebsphereMQReader implements QueueReader {
	
	private  MQConnectionFactory factory;
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
			factory = createFactory();
			
			//Config
			factory.setHostName(hostName);
			factory.setQueueManager(queueManager);
			factory.setChannel("CLIENTCONNECTION");
			factory.setPort(port);
			factory.setTransportType(1);
			
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			destination = session.createQueue(queueName);
			consumer = session.createConsumer(destination);
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
	 * Reads the next message from the queue and returns it.
	 * Returns null if no message exists.
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
			if (null != connection) {
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
			//do nothing? assume we're already disconnected?
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

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}	
}
