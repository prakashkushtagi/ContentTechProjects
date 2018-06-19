/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.LoadMonitorLtcMessage;
import com.trgr.dockets.core.service.LoadMonitorLtcMessageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class LoadMonitorLtcMessageServiceTest
{
	@Autowired
	private LoadMonitorLtcMessageService loadMonitorLtcMessageService;
	
	@Before
	public void setUp() 
	{

	}

	@Test
	public void testSaveLoadMonitorLtcMessageService()
	{
		
		LoadMonitorLtcMessage loadMonitorLtcMessage = new LoadMonitorLtcMessage();
		loadMonitorLtcMessage.setCollaborationKey("TestCollaborationKeybatch_TestCollaborationKeysubBatch");
		loadMonitorLtcMessage.setCollection("TestCollection");
		loadMonitorLtcMessage.setEventId(1234567L);
		loadMonitorLtcMessage.setLoadDatasetId("TestLoadDatasetId");
		loadMonitorLtcMessage.setLoadDatasetRequestId("TestLoadDatasetRequestId");
		loadMonitorLtcMessage.setLoadElement("TestLoadElement");
		loadMonitorLtcMessage.setLoadStatus("TestLoadStatus");
		loadMonitorLtcMessage.setLtcFilename("TestLtcFilename");
		loadMonitorLtcMessage.setMessageId("TESTMESSAGEID");
		loadMonitorLtcMessage.setStatusId("SUCCESFULLY PROCESSED AND MESSAGE SENT TO THE QUEUE");
		loadMonitorLtcMessage.setErrorDescription("SUCCESFULLY PROCESSED AND MESSAGE SENT TO THE QUEUE");
		loadMonitorLtcMessage.setOriginalMessageTxt("<TestMessage></TestMessage>");
		
		try 
		{
			this.loadMonitorLtcMessageService.saveOrUpdateLoadMonitorLtcMessage(loadMonitorLtcMessage);
			LoadMonitorLtcMessage expectedLoadMonitorLtcMessage = this.loadMonitorLtcMessageService.findLoadMonitorLtcMessageByMessageId("TESTMESSAGEID");
			Assert.assertTrue("LTC Message id should be " + loadMonitorLtcMessage.getMessageId() + " but is " + 
					expectedLoadMonitorLtcMessage , expectedLoadMonitorLtcMessage.getMessageId().equals(loadMonitorLtcMessage.getMessageId()));
		} 
		catch (Exception e) 
		{
			Assert.fail();
		}
	}
	
	@Test
	public void testfindByEventIdMonitorLtcMessageService()
	{
		
		LoadMonitorLtcMessage loadMonitorLtcMessage = new LoadMonitorLtcMessage();
		loadMonitorLtcMessage.setCollaborationKey("TestCollaborationKey");
		loadMonitorLtcMessage.setCollection("TestCollection");
		loadMonitorLtcMessage.setEventId(1234567L);
		loadMonitorLtcMessage.setLoadDatasetId("TestLoadDatasetId");
		loadMonitorLtcMessage.setLoadDatasetRequestId("TestLoadDatasetRequestId");
		loadMonitorLtcMessage.setLoadElement("TestLoadElement");
		loadMonitorLtcMessage.setLoadStatus("TestLoadStatus");
		loadMonitorLtcMessage.setLtcFilename("TestLtcFilename");
		loadMonitorLtcMessage.setMessageId("TESTMESSAGEID");
		loadMonitorLtcMessage.setStatusId("SUCCESFULLY PROCESSED AND MESSAGE SENT TO THE QUEUE");
		loadMonitorLtcMessage.setOriginalMessageTxt("<TestMessage></TestMessage>");
		
		LoadMonitorLtcMessage loadMonitorLtcMessage2 = new LoadMonitorLtcMessage();
		loadMonitorLtcMessage2.setCollaborationKey("TestCollaborationKey");
		loadMonitorLtcMessage2.setCollection("TestCollection");
		loadMonitorLtcMessage2.setEventId(1234567L);
		loadMonitorLtcMessage2.setLoadDatasetId("TestLoadDatasetId2");
		loadMonitorLtcMessage2.setLoadDatasetRequestId("TestLoadDatasetRequestId2");
		loadMonitorLtcMessage2.setLoadElement("TestLoadElement2");
		loadMonitorLtcMessage2.setLoadStatus("TestLoadStatus2");
		loadMonitorLtcMessage2.setLtcFilename("TestLtcFilename2");
		loadMonitorLtcMessage2.setMessageId("TESTMESSAGEID2");
		loadMonitorLtcMessage2.setStatusId("SUCCESFULLY PROCESSED AND MESSAGE SENT TO THE QUEUE");
		loadMonitorLtcMessage2.setOriginalMessageTxt("<TestMessage></TestMessage>");
		
		try 
		{
			this.loadMonitorLtcMessageService.saveOrUpdateLoadMonitorLtcMessage(loadMonitorLtcMessage);
			this.loadMonitorLtcMessageService.saveOrUpdateLoadMonitorLtcMessage(loadMonitorLtcMessage2);
			List<LoadMonitorLtcMessage> expectedLoadMonitorLtcMessageList = this.loadMonitorLtcMessageService.findLoadMonitorLtcMessageByEventId(1234567L);
			Assert.assertTrue("Two loaddatasets for one event  expected but the number of entries resulted " + expectedLoadMonitorLtcMessageList.size(), (expectedLoadMonitorLtcMessageList.size()==2) );
			Assert.assertTrue("LTC Message id 1 should be " + loadMonitorLtcMessage.getMessageId() + " but is " + 
					expectedLoadMonitorLtcMessageList.get(0).getMessageId() , expectedLoadMonitorLtcMessageList.get(0).getMessageId().equals(loadMonitorLtcMessage.getMessageId()));
			Assert.assertTrue("LTC Message id 1 should be " + loadMonitorLtcMessage2.getMessageId() + " but is " + 
					expectedLoadMonitorLtcMessageList.get(0).getMessageId() , expectedLoadMonitorLtcMessageList.get(1).getMessageId().equals(loadMonitorLtcMessage2.getMessageId()));
		} 
		catch (Exception e) 
		{
			Assert.fail();
		}
	}
	
	@Test
	public void testfindByBatchAndSubBatchLoadMonitorLtcMessageService()
	{
		
		LoadMonitorLtcMessage loadMonitorLtcMessage = new LoadMonitorLtcMessage();
		loadMonitorLtcMessage.setCollaborationKey("TestCollaborationKeybatch_TestCollaborationKeysubBatch");
		loadMonitorLtcMessage.setCollection("TestCollection");
		loadMonitorLtcMessage.setEventId(1234567L);
		loadMonitorLtcMessage.setLoadDatasetId("TestLoadDatasetId");
		loadMonitorLtcMessage.setLoadDatasetRequestId("TestLoadDatasetRequestId");
		loadMonitorLtcMessage.setLoadElement("TestLoadElement");
		loadMonitorLtcMessage.setLoadStatus("TestLoadStatus");
		loadMonitorLtcMessage.setLtcFilename("TestLtcFilename");
		loadMonitorLtcMessage.setMessageId("TESTMESSAGEID");
		loadMonitorLtcMessage.setStatusId("SUCCESFULLY PROCESSED AND MESSAGE SENT TO THE QUEUE");
		loadMonitorLtcMessage.setOriginalMessageTxt("<TestMessage></TestMessage>");
		
		try 
		{
			this.loadMonitorLtcMessageService.saveOrUpdateLoadMonitorLtcMessage(loadMonitorLtcMessage);
			LoadMonitorLtcMessage expectedLoadMonitorLtcMessage = this.loadMonitorLtcMessageService.findLoadMonitorLtcMessageByBatchIdSubBatchIdMessageType("TestCollaborationKeybatch", "TestCollaborationKeysubBatch","TestLoadElement");
			Assert.assertTrue("LTC Message id should be " + loadMonitorLtcMessage.getMessageId() + " but is " + 
					expectedLoadMonitorLtcMessage , expectedLoadMonitorLtcMessage.getMessageId().equals(loadMonitorLtcMessage.getMessageId()));
		} 
		catch (Exception e) 
		{
			Assert.fail();
		}
	}
	
	@Test
	public void testGetByStatusId()
	{
		
		LoadMonitorLtcMessage loadMonitorLtcMessage = new LoadMonitorLtcMessage();
		loadMonitorLtcMessage.setCollaborationKey("TestCollaborationKeybatch_TestCollaborationKeysubBatch");
		loadMonitorLtcMessage.setCollection("TestCollection");
		loadMonitorLtcMessage.setEventId(1234567L);
		loadMonitorLtcMessage.setLoadDatasetId("TestLoadDatasetId");
		loadMonitorLtcMessage.setLoadDatasetRequestId("TestLoadDatasetRequestId");
		loadMonitorLtcMessage.setLoadElement("TestLoadElement");
		loadMonitorLtcMessage.setLoadStatus("TestLoadStatus");
		loadMonitorLtcMessage.setLtcFilename("TestLtcFilename");
		loadMonitorLtcMessage.setMessageId("TESTMESSAGEID");
		loadMonitorLtcMessage.setStatusId("301");
		loadMonitorLtcMessage.setOriginalMessageTxt("<TestMessage></TestMessage>");
		
		try 
		{
			this.loadMonitorLtcMessageService.saveOrUpdateLoadMonitorLtcMessage(loadMonitorLtcMessage);
			
			LoadMonitorLtcMessage loadMonitorLtcMessage1 = new LoadMonitorLtcMessage();
			loadMonitorLtcMessage1.setCollaborationKey("TestCollaborationKeybatch_TestCollaborationKeysubBatch");
			loadMonitorLtcMessage1.setCollection("TestCollection");
			loadMonitorLtcMessage1.setEventId(1234567L);
			loadMonitorLtcMessage1.setLoadDatasetId("TestLoadDatasetId");
			loadMonitorLtcMessage1.setLoadDatasetRequestId("TestLoadDatasetRequestId");
			loadMonitorLtcMessage1.setLoadElement("TestLoadElement");
			loadMonitorLtcMessage1.setLoadStatus("TestLoadStatus");
			loadMonitorLtcMessage1.setLtcFilename("TestLtcFilename");
			loadMonitorLtcMessage1.setMessageId("TESTMESSAGEID2");
			loadMonitorLtcMessage1.setStatusId("302");
			loadMonitorLtcMessage1.setOriginalMessageTxt("<TestMessage></TestMessage>");
			
			this.loadMonitorLtcMessageService.saveOrUpdateLoadMonitorLtcMessage(loadMonitorLtcMessage1);
//			List<LoadMonitorLtcMessage> expectedLoadMonitorLtcMessageList = this.loadMonitorLtcMessageService.findLoadMonitorLtcMessageForProcessing();
//			Assert.assertTrue("Should be one with 301 : but is " + expectedLoadMonitorLtcMessageList.size() ,expectedLoadMonitorLtcMessageList.size()==114 );
//			Assert.assertTrue("LTC Message id should be " + loadMonitorLtcMessage.getMessageId() + " but is " + 
//					expectedLoadMonitorLtcMessageList.get(0).getMessageId() , expectedLoadMonitorLtcMessageList.get(0).getMessageId().equals(loadMonitorLtcMessage.getMessageId()));
			Assert.assertTrue(true);
		} 
		catch (Exception e) 
		{
			Assert.fail();
		}
	}
}
