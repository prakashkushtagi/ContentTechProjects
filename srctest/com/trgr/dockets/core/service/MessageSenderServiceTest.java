/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

import javax.jms.Destination;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class MessageSenderServiceTest 
{

	 @Test
	    public void testMessageSenderTest()
	    {
	        // mock objects used within the method under test
	        Destination mockDestination = EasyMock.createMock(Destination.class);
	        JmsTemplate mockJmsTemplate = EasyMock.createMock(JmsTemplate.class);
	        mockJmsTemplate.send(EasyMock.eq(mockDestination), EasyMock.isA(MessageCreator.class));
	        EasyMock.replay(mockJmsTemplate);

	        // instantiate the class, set its properties, and execute the method under test
	        MessageSenderService messageSenderService = new MessageSenderService();
	        messageSenderService.setLtcResponseQueue(mockDestination);
	        messageSenderService.setJmsTemplate(mockJmsTemplate);
	        messageSenderService.send("<TestMessage>message</TestMessage>");

	        // verify that the mocks behaved as expected
	        EasyMock.verify(mockJmsTemplate);
	    }
	
}
