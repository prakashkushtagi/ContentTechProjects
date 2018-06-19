/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util.standalone;


import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;

import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.processor.SourceContentLoaderProcessor;
import com.trgr.dockets.core.processor.SourceLoadRequest;
import com.trgr.dockets.core.service.EventLogServiceImpl;
import com.trgr.dockets.core.util.EventMarshllerAndUnmarshller;

public class SourceContentLoadCall 
{
	
	
	public static void main(String[] args)
	{
		// this is spring JMSTemplate and are injected right now 
		JmsTemplate jmsTemplate = null;
		// so is this.  
		Destination ltcResponseQueue = null;
		
		EventMarshllerAndUnmarshller eventMarshllerAndUnmarshller = new EventMarshllerAndUnmarshller();
		EventLogServiceImpl eventLogService = new EventLogServiceImpl(null, eventMarshllerAndUnmarshller, jmsTemplate, ltcResponseQueue);
		//After all this for eventlogService to work we need to change the service to include
		//a method for calling to send messages that is not spring integrated.
		//which means lower performance to open and close queue session.
		
		
		//current version of SCL doesn't have this constructor, easy to add it.		
		SourceContentLoaderProcessor sourceContentLoaderProcessor = new SourceContentLoaderProcessor("dev");
		
		sourceContentLoaderProcessor.setEventLogService(eventLogService);
		BankruptcyContentService bankruptcyContentService = new BankruptcyContentService(true);
		sourceContentLoaderProcessor.setBankruptcyContentService(bankruptcyContentService);
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod("");
		sourceLoadRequest.setAcquisitionStart("");
		sourceLoadRequest.setAcquisitionType("");
		sourceLoadRequest.setBatchId("");
		sourceLoadRequest.setDeleteFlag(false);
		sourceLoadRequest.setProductCode("");
		sourceLoadRequest.setRequestId(0L);
		sourceLoadRequest.setSourcePath("");
		try 
		{
		 sourceContentLoaderProcessor.processRequest(sourceLoadRequest);
		} 
		catch (SourceContentLoaderException e) 
		{
			e.printStackTrace();
		}
		
	}

}
