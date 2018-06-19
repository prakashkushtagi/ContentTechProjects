/*
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util.mq;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.trgr.dockets.core.exception.QueueException;

public class WebsphereMQHandlerTest {

	@Mock
	private MQQueueConnectionFactory mockFactory;
	@Mock
	private Connection mockConnection;
	@Mock
	private Session mockSession;
	@Mock
	private Destination mockDestination;
	@Mock
	private MessageConsumer mockConsumer;
	@Mock
	private MessageProducer mockProducer;
	@Mock
	private TextMessage mockTextMessage;
	@Mock
	private MapMessage mockUnwantedMessage;
	@Mock
	private Queue mockQueue;
	
	private WebsphereMQSender mockSender;
	private WebsphereMQReader mockReader;
	
	/**
	 * Sets up mocks for sender.
	 * @return
	 */
	public WebsphereMQSender mockSender() {
		MockitoAnnotations.initMocks(this);
		
		WebsphereMQSender sender = new WebsphereMQSender();
		sender.setFactory(mockFactory);
		sender.setConnection(mockConnection);
		sender.setSession(mockSession);
		sender.setDestination(mockDestination);
		sender.setProducer(mockProducer);
		mockSender = spy(sender);
		return mockSender;
	}
	
	/**
	 * Sets up mocks for reader.
	 * @return
	 */
	public WebsphereMQReader mockReader() {
		MockitoAnnotations.initMocks(this);
		WebsphereMQReader reader = new WebsphereMQReader();
		reader.setFactory(mockFactory);
		reader.setConnection(mockConnection);
		reader.setSession(mockSession);
		reader.setDestination(mockDestination);
		reader.setConsumer(mockConsumer);
		mockReader = spy(reader);
		return mockReader;
	}
	
	@Test
	public void testSendToQueue() throws QueueException, JMSException {
		WebsphereMQSender sender = mockSender();
		
		//Connect mocks
		doReturn(mockFactory).when(sender).createFactory();
		doReturn(mockConnection).when(mockFactory).createConnection();
		doNothing().when(mockConnection).start();
		doReturn(mockSession).when(mockConnection).createSession(anyBoolean(), anyInt());
		doReturn(mockQueue).when(mockSession).createQueue(anyString());
		doReturn(mockProducer).when(mockSession).createProducer(mockQueue);
		
		//Send mocks
		doReturn(mockTextMessage).when(mockSession).createTextMessage();
		
		//Actual calls
		sender.connect("testUrl", "testQueueMgr", "testQueue", 1414);
		sender.sendMessage("Test message");
		sender.close();
		
		//Connect verifications
		verify(sender, times(1)).createFactory();
		verify(mockFactory, times(1)).createConnection();
		verify(mockConnection, times(1)).start();
		verify(mockConnection, times(1)).createSession(anyBoolean(), anyInt());
		verify(mockSession, times(1)).createQueue(anyString());
		verify(mockSession, times(1)).createProducer(mockQueue);
		
		//Send verifications
		verify(mockSession, times(1)).createTextMessage();
		verify(mockProducer, times(1)).send(mockTextMessage);
		
		//Close verifications
		verify(mockSession, times(1)).close();
		verify(mockConnection, times(1)).close();
	}
	
	@Test (expected = QueueException.class)
	public void testSendToQueueUnhappyConnection() throws QueueException, JMSException {
		WebsphereMQSender sender = mockSender();
		
		//Connect mocks
		doReturn(mockFactory).when(sender).createFactory();
		doReturn(mockConnection).when(mockFactory).createConnection();
		doNothing().when(mockConnection).start();
		doReturn(mockSession).when(mockConnection).createSession(anyBoolean(), anyInt());
		doReturn(mockQueue).when(mockSession).createQueue(anyString());
		doThrow(new JMSException("Bang!")).when(mockSession).createProducer(mockQueue);
		
		sender.connect("testUrl", "testQueueMgr", "testQueue", 1414);
		
		//Connect verifications
		verify(sender, times(1)).createFactory();
		verify(mockFactory, times(1)).createConnection();
		verify(mockConnection, times(1)).start();
		verify(mockConnection, times(1)).createSession(anyBoolean(), anyInt());
		verify(mockSession, times(1)).createQueue(anyString());
		verify(mockSession, times(1)).createProducer(mockQueue);
	}
	
	@Test (expected = QueueException.class)
	public void testSendToQueueUnhappyMessage() throws QueueException, JMSException {
		WebsphereMQSender sender = mockSender();
		
		//Send mocks
		doReturn(mockTextMessage).when(mockSession).createTextMessage();
		doThrow(new JMSException("Bang!")).when(mockProducer).send(mockTextMessage);
		
		sender.sendMessage("Test message");
		
		//Send verifications
		verify(mockSession, times(1)).createTextMessage();
		verify(mockProducer, times(1)).send(mockTextMessage);

	}
	
	@Test (expected = QueueException.class)
	public void testSendToQueueUnhappyClose() throws QueueException, JMSException {
		WebsphereMQSender sender = mockSender();
		
		doThrow(new JMSException("Bang!")).when(mockConnection).close();
		
		sender.close();
		
		//Close verifications
		verify(mockSession, times(1)).close();
		verify(mockConnection, times(1)).close();
	}
	
	@Test
	public void testReadFromQueue() throws QueueException, JMSException {
		WebsphereMQReader reader = mockReader();
		
		//Connect mocks
		doReturn(mockFactory).when(reader).createFactory();
		doReturn(mockConnection).when(mockFactory).createConnection();
		doNothing().when(mockConnection).start();
		doReturn(mockSession).when(mockConnection).createSession(anyBoolean(), anyInt());
		doReturn(mockQueue).when(mockSession).createQueue(anyString());
		doReturn(mockConsumer).when(mockSession).createConsumer(mockQueue);
		
		//Receive mocks
		doReturn(mockTextMessage).when(mockConsumer).receiveNoWait();
		
		//Actual calls
		reader.connect("testUrl", "testQueueMgr", "testQueue", 1414);
		TextMessage textMessage = (TextMessage)reader.readMessage();
		reader.close();
		
		//Assertions
		assertEquals(textMessage, mockTextMessage);
		
		//Connect verifications
		verify(reader, times(1)).createFactory();
		verify(mockFactory, times(1)).createConnection();
		verify(mockConnection, times(1)).start();
		verify(mockConnection, times(1)).createSession(anyBoolean(), anyInt());
		verify(mockSession, times(1)).createQueue(anyString());
		verify(mockSession, times(1)).createConsumer(mockQueue);
		
		//Send verifications
		verify(mockConsumer, times(1)).receiveNoWait();
		
		//Close verifications
		verify(mockSession, times(1)).close();
		verify(mockConnection, times(1)).close();
	}
	
	@Test (expected = QueueException.class)
	public void testReadFromQueueUnhappyConnection() throws QueueException, JMSException {
		WebsphereMQReader reader = mockReader();
		
		//Connect mocks
		doReturn(mockFactory).when(reader).createFactory();
		doReturn(mockConnection).when(mockFactory).createConnection();
		doNothing().when(mockConnection).start();
		doReturn(mockSession).when(mockConnection).createSession(anyBoolean(), anyInt());
		doReturn(mockQueue).when(mockSession).createQueue(anyString());
		doThrow(new JMSException("Bang!")).when(mockSession).createConsumer(mockQueue);
		
		reader.connect("testUrl", "testQueueMgr", "testQueue", 1414);
		
		//Connect verifications
		verify(reader, times(1)).createFactory();
		verify(mockFactory, times(1)).createConnection();
		verify(mockConnection, times(1)).start();
		verify(mockConnection, times(1)).createSession(anyBoolean(), anyInt());
		verify(mockSession, times(1)).createQueue(anyString());
		verify(mockSession, times(1)).createConsumer(mockQueue);
	}
	
	@Test (expected = QueueException.class)
	public void testReadFromQueueUnhappyMessage() throws QueueException, JMSException {
		WebsphereMQReader reader = mockReader();
		
		doThrow(new JMSException("Bang!")).when(mockConsumer).receiveNoWait();		
		
		reader.readMessage();
		
		verify(mockConsumer, times(1)).receiveNoWait();
	}
	
	@Ignore
	@Test (expected = RuntimeException.class)
	public void testReadFromQueueUnwantedMessage() throws QueueException, JMSException {
		WebsphereMQReader reader = mockReader();
		
		doReturn(mockUnwantedMessage).when(mockConsumer).receiveNoWait();		
		
		reader.readMessage();
		
		verify(mockConsumer, times(1)).receiveNoWait();
	}
	
	@Test (expected = QueueException.class)
	public void testReadFromQueueUnhappyClose() throws QueueException, JMSException {
		WebsphereMQReader reader = mockReader();
		
		doThrow(new JMSException("Bang!")).when(mockConnection).close();
		
		reader.close();
		
		//Close verifications
		verify(mockSession, times(1)).close();
		verify(mockConnection, times(1)).close();
	}
}
