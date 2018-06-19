/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.jms.parser;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.trgr.dockets.core.domain.ltc.LoadDataset;
import com.trgr.dockets.core.domain.ltc.LtcEvent;
import com.trgr.dockets.core.domain.ltc.LtcMessage;
import com.trgr.dockets.core.exception.MessageParseException;

public class LtcMessageParserTest 
{

	@Before
	public void setUp()
	{
		
	}
	
	@Test
	public void testLtcMessageParserOneEventOneLD()
	{
		String xmlMessage = "<event.collection><event.data><event.id>255540</event.id><event.type>SYSTEM.EVENT</event.type><event.publisher>LTC</event.publisher><event.text><ltc.load.event><load.collection id=\"5038\" envid=\"2\">w_ltc_test4</load.collection><load.groups><load.group id=\"25\">LTC Test</load.group><load.group id=\"60\">Extract</load.group><load.group id=\"69\">STG</load.group></load.groups><load.status id=\"6\">Completed</load.status><load.element id=\"35\">Novus Load</load.element><system.message>Load to w_ltc_test4 stage 6411 for file novusdataclientwlltctest4LoadAPITestFile_MQ.xml complete, Load elapsed time 2521 ms, size 38036 bytes, LoadManagerServ 10, Number: Add9 Delete0 Total9. Distribution elapsed time 37 ms, distribution server 10. Total elapsed time 3886 ms.</system.message><load.dataset id=\"82617\" load.request.id=\"58784\" request.collaboration.key=\"LTC-25144-D050923-1_slksdadlsadew4332453245\">LoadAPITestFile_MQ1.xml</load.dataset></ltc.load.event></event.text><event.datetime>2005-09-23 09:57:35.343</event.datetime><subscription.collaboration.key>QueueTest1</subscription.collaboration.key></event.data></event.collection>";
		LtcMessageParser ltcMessageParser = new LtcMessageParser();
		String systemMessage = "Load to w_ltc_test4 stage 6411 for file novusdataclientwlltctest4LoadAPITestFile_MQ.xml complete, Load elapsed time 2521 ms, size 38036 bytes, LoadManagerServ 10, Number: Add9 Delete0 Total9. Distribution elapsed time 37 ms, distribution server 10. Total elapsed time 3886 ms.";
		try 
		{
			LtcMessage ltcMessage = ltcMessageParser.parseMessage(xmlMessage);
			Assert.assertTrue("Only one event should be present but is " + ltcMessage.getEventDataList(),ltcMessage.getEventDataList().size()==1);
			LtcEvent ltcEvent = ltcMessage.getEventDataList().get(0);
			Assert.assertTrue("The event id should be 255540 but is " + ltcEvent.getEventId(), "255540".equals(ltcEvent.getEventId()));
			Assert.assertTrue("The event collection should be w_ltc_test4 but is " + ltcEvent.getCollection(), "w_ltc_test4".equals(ltcEvent.getCollection()));
			Assert.assertTrue("The event load element step should be \"Novus Load\" but is " + ltcEvent.getEventLoadElementStep(), "Novus Load".equals(ltcEvent.getEventLoadElementStep()));
			Assert.assertTrue("The event status should be \"Completed\" but is " + ltcEvent.getEventLoadStatus(), "Completed".equals(ltcEvent.getEventLoadStatus()));
			Assert.assertTrue("The event system message should be \""+systemMessage+ "\" but is " + ltcEvent.getSystemMessage(), systemMessage.equals(ltcEvent.getSystemMessage()));
			Assert.assertTrue("The event timestamp should be \"2005-09-23 09:57:35.343\" but is " + ltcEvent.getEventTimestamp(), "2005-09-23 09:57:35.343".equals(ltcEvent.getEventTimestamp()));
			Assert.assertTrue("Only one loaddataset should be present but is " + ltcEvent.getLoadDatasetList().size(),ltcEvent.getLoadDatasetList().size()==1);
			LoadDataset loadDataset = ltcEvent.getLoadDatasetList().get(0);
			Assert.assertTrue("The loaddatasetId should be \"82617\" but is " + loadDataset.getLoadDatasetId(), "82617".equals(loadDataset.getLoadDatasetId()));
			Assert.assertTrue("The loadrequestId should be \"58784\" but is " + loadDataset.getLoadRequestId(), "58784".equals(loadDataset.getLoadRequestId()));
			Assert.assertTrue("The collaborationKey should be \"LTC-25144-D050923-1_slksdadlsadew4332453245\" but is " + loadDataset.getRequestCollaborationKey(), "LTC-25144-D050923-1_slksdadlsadew4332453245".equals(loadDataset.getRequestCollaborationKey()));
			Assert.assertTrue("The batchId should be \"LTC-25144-D050923-1\" but is " + loadDataset.getLtcCollaborationKey().getBatchId(), "LTC-25144-D050923-1".equals(loadDataset.getLtcCollaborationKey().getBatchId()));
			Assert.assertTrue("The subBatchId should be \"slksdadlsadew4332453245\" but is " + loadDataset.getLtcCollaborationKey().getSubBatchId(), "slksdadlsadew4332453245".equals(loadDataset.getLtcCollaborationKey().getSubBatchId()));
			Assert.assertTrue("The filename should be \"LoadAPITestFile_MQ1.xml\" but is " + loadDataset.getFileName(), "LoadAPITestFile_MQ1.xml".equals(loadDataset.getFileName()));		
		} 
		catch (MessageParseException e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Test
	public void testLtcMessageParserOneEventTwoLDs()
	{
		String xmlMessage = "<event.collection><event.data><event.id>255540</event.id><event.type>SYSTEM.EVENT</event.type><event.publisher>LTC</event.publisher><event.text><ltc.load.event><load.collection id=\"5038\" envid=\"2\">w_ltc_test4</load.collection><load.groups><load.group id=\"25\">LTC Test</load.group><load.group id=\"60\">Extract</load.group><load.group id=\"69\">STG</load.group></load.groups><load.status id=\"6\">Completed</load.status><load.element id=\"35\">Novus Load</load.element><system.message>Load to w_ltc_test4 stage 6411 for file novusdataclientwlltctest4LoadAPITestFile_MQ.xml complete, Load elapsed time 2521 ms, size 38036 bytes, LoadManagerServ 10, Number: Add9 Delete0 Total9. Distribution elapsed time 37 ms, distribution server 10. Total elapsed time 3886 ms.</system.message><load.dataset id=\"82618\" load.request.id=\"58785\" request.collaboration.key=\"LTC-25144-D050923-1_slksdadlsadew4332453246\">LoadAPITestFile_MQ2.xml</load.dataset><load.dataset id=\"82617\" load.request.id=\"58784\" request.collaboration.key=\"LTC-25144-D050923-1_slksdadlsadew4332453245\">LoadAPITestFile_MQ1.xml</load.dataset></ltc.load.event></event.text><event.datetime>2005-09-23 09:57:35.343</event.datetime><subscription.collaboration.key>QueueTest1</subscription.collaboration.key></event.data></event.collection>";
		LtcMessageParser ltcMessageParser = new LtcMessageParser();
		String systemMessage = "Load to w_ltc_test4 stage 6411 for file novusdataclientwlltctest4LoadAPITestFile_MQ.xml complete, Load elapsed time 2521 ms, size 38036 bytes, LoadManagerServ 10, Number: Add9 Delete0 Total9. Distribution elapsed time 37 ms, distribution server 10. Total elapsed time 3886 ms.";
		try 
		{
			LtcMessage ltcMessage = ltcMessageParser.parseMessage(xmlMessage);
			Assert.assertTrue("Only one event should be present but is " + ltcMessage.getEventDataList(),ltcMessage.getEventDataList().size()==1);
			LtcEvent ltcEvent = ltcMessage.getEventDataList().get(0);
			Assert.assertTrue("The event id should be 255540 but is " + ltcEvent.getEventId(), "255540".equals(ltcEvent.getEventId()));
			Assert.assertTrue("The event collection should be w_ltc_test4 but is " + ltcEvent.getCollection(), "w_ltc_test4".equals(ltcEvent.getCollection()));
			Assert.assertTrue("The event load element step should be \"Novus Load\" but is " + ltcEvent.getEventLoadElementStep(), "Novus Load".equals(ltcEvent.getEventLoadElementStep()));
			Assert.assertTrue("The event status should be \"Completed\" but is " + ltcEvent.getEventLoadStatus(), "Completed".equals(ltcEvent.getEventLoadStatus()));
			Assert.assertTrue("The event system message should be \""+systemMessage+ "\" but is " + ltcEvent.getSystemMessage(), systemMessage.equals(ltcEvent.getSystemMessage()));
			Assert.assertTrue("The event timestamp should be \"2005-09-23 09:57:35.343\" but is " + ltcEvent.getEventTimestamp(), "2005-09-23 09:57:35.343".equals(ltcEvent.getEventTimestamp()));
			Assert.assertTrue("Only two loaddataset should be present but is " + ltcEvent.getLoadDatasetList().size(),ltcEvent.getLoadDatasetList().size()==2);
			LoadDataset loadDataset = ltcEvent.getLoadDatasetList().get(0);
			Assert.assertTrue("The loaddatasetId should be \"82618\" but is " + loadDataset.getLoadDatasetId(), "82618".equals(loadDataset.getLoadDatasetId()));
			Assert.assertTrue("The loadrequestId should be \"58785\" but is " + loadDataset.getLoadRequestId(), "58785".equals(loadDataset.getLoadRequestId()));
			Assert.assertTrue("The collaborationKey should be \"LTC-25144-D050923-1_slksdadlsadew4332453246\" but is " + loadDataset.getRequestCollaborationKey(), "LTC-25144-D050923-1_slksdadlsadew4332453246".equals(loadDataset.getRequestCollaborationKey()));
			Assert.assertTrue("The batchId should be \"LTC-25144-D050923-1\" but is " + loadDataset.getLtcCollaborationKey().getBatchId(), "LTC-25144-D050923-1".equals(loadDataset.getLtcCollaborationKey().getBatchId()));
			Assert.assertTrue("The subBatchId should be \"slksdadlsadew4332453246\" but is " + loadDataset.getLtcCollaborationKey().getSubBatchId(), "slksdadlsadew4332453246".equals(loadDataset.getLtcCollaborationKey().getSubBatchId()));
			Assert.assertTrue("The filename should be \"LoadAPITestFile_MQ2.xml\" but is " + loadDataset.getFileName(), "LoadAPITestFile_MQ2.xml".equals(loadDataset.getFileName()));	
			loadDataset = ltcEvent.getLoadDatasetList().get(1);
			Assert.assertTrue("The loaddatasetId should be \"82617\" but is " + loadDataset.getLoadDatasetId(), "82617".equals(loadDataset.getLoadDatasetId()));
			Assert.assertTrue("The loadrequestId should be \"58784\" but is " + loadDataset.getLoadRequestId(), "58784".equals(loadDataset.getLoadRequestId()));
			Assert.assertTrue("The collaborationKey should be \"LTC-25144-D050923-1_slksdadlsadew4332453245\" but is " + loadDataset.getRequestCollaborationKey(), "LTC-25144-D050923-1_slksdadlsadew4332453245".equals(loadDataset.getRequestCollaborationKey()));
			Assert.assertTrue("The batchId should be \"LTC-25144-D050923-1\" but is " + loadDataset.getLtcCollaborationKey().getBatchId(), "LTC-25144-D050923-1".equals(loadDataset.getLtcCollaborationKey().getBatchId()));
			Assert.assertTrue("The subBatchId should be \"slksdadlsadew4332453245\" but is " + loadDataset.getLtcCollaborationKey().getSubBatchId(), "slksdadlsadew4332453245".equals(loadDataset.getLtcCollaborationKey().getSubBatchId()));
			Assert.assertTrue("The filename should be \"LoadAPITestFile_MQ1.xml\" but is " + loadDataset.getFileName(), "LoadAPITestFile_MQ1.xml".equals(loadDataset.getFileName()));		
		} 
		catch (MessageParseException e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testLtcMessageParserTwoEventOneLDEach()
	{
		String xmlMessage = "<event.collection>" +
				"<event.data><event.id>255540</event.id><event.type>SYSTEM.EVENT</event.type><event.publisher>LTC</event.publisher><event.text><ltc.load.event><load.collection id=\"5038\" envid=\"2\">w_ltc_test4</load.collection><load.groups><load.group id=\"25\">LTC Test</load.group><load.group id=\"60\">Extract</load.group><load.group id=\"69\">STG</load.group></load.groups><load.status id=\"6\">Completed</load.status><load.element id=\"35\">Novus Load</load.element><system.message>systemMessage1</system.message><load.dataset id=\"82617\" load.request.id=\"58784\" request.collaboration.key=\"LTC-25144-D050923-1_slksdadlsadew4332453245\">LoadAPITestFile_MQ1.xml</load.dataset></ltc.load.event></event.text><event.datetime>2005-09-23 09:57:35.343</event.datetime><subscription.collaboration.key>QueueTest1</subscription.collaboration.key></event.data>" +
				"<event.data><event.id>255541</event.id><event.type>SYSTEM.EVENT</event.type><event.publisher>LTC</event.publisher><event.text><ltc.load.event><load.collection id=\"5038\" envid=\"2\">w_ltc_test5</load.collection><load.groups><load.group id=\"25\">LTC Test</load.group><load.group id=\"60\">Extract</load.group><load.group id=\"69\">STG</load.group></load.groups><load.status id=\"6\">Completed</load.status><load.element id=\"35\">Novus Load</load.element><system.message>systemMessage2</system.message><load.dataset id=\"82618\" load.request.id=\"58785\" request.collaboration.key=\"LTC-25144-D050923-1_slksdadlsadew4332453246\">LoadAPITestFile_MQ2.xml</load.dataset></ltc.load.event></event.text><event.datetime>2005-09-23 09:57:35.343</event.datetime><subscription.collaboration.key>QueueTest2</subscription.collaboration.key></event.data>" +
				"</event.collection>";
		LtcMessageParser ltcMessageParser = new LtcMessageParser();
		String systemMessage = "systemMessage1";
		String systemMessage2 = "systemMessage2";
		try 
		{
			LtcMessage ltcMessage = ltcMessageParser.parseMessage(xmlMessage);
			Assert.assertTrue("Only two event should be present but is " + ltcMessage.getEventDataList(),ltcMessage.getEventDataList().size()==2);
			LtcEvent ltcEvent = ltcMessage.getEventDataList().get(0);
			Assert.assertTrue("The event id should be 255540 but is " + ltcEvent.getEventId(), "255540".equals(ltcEvent.getEventId()));
			Assert.assertTrue("The event collection should be w_ltc_test4 but is " + ltcEvent.getCollection(), "w_ltc_test4".equals(ltcEvent.getCollection()));
			Assert.assertTrue("The event load element step should be \"Novus Load\" but is " + ltcEvent.getEventLoadElementStep(), "Novus Load".equals(ltcEvent.getEventLoadElementStep()));
			Assert.assertTrue("The event status should be \"Completed\" but is " + ltcEvent.getEventLoadStatus(), "Completed".equals(ltcEvent.getEventLoadStatus()));
			Assert.assertTrue("The event system message should be \""+systemMessage+ "\" but is " + ltcEvent.getSystemMessage(), systemMessage.equals(ltcEvent.getSystemMessage()));
			Assert.assertTrue("The event timestamp should be \"2005-09-23 09:57:35.343\" but is " + ltcEvent.getEventTimestamp(), "2005-09-23 09:57:35.343".equals(ltcEvent.getEventTimestamp()));
			Assert.assertTrue("Only one loaddataset should be present but is " + ltcEvent.getLoadDatasetList().size(),ltcEvent.getLoadDatasetList().size()==1);
			LoadDataset loadDataset = ltcEvent.getLoadDatasetList().get(0);
			Assert.assertTrue("The loaddatasetId should be \"82617\" but is " + loadDataset.getLoadDatasetId(), "82617".equals(loadDataset.getLoadDatasetId()));
			Assert.assertTrue("The loadrequestId should be \"58784\" but is " + loadDataset.getLoadRequestId(), "58784".equals(loadDataset.getLoadRequestId()));
			Assert.assertTrue("The collaborationKey should be \"LTC-25144-D050923-1_slksdadlsadew4332453245\" but is " + loadDataset.getRequestCollaborationKey(), "LTC-25144-D050923-1_slksdadlsadew4332453245".equals(loadDataset.getRequestCollaborationKey()));
			Assert.assertTrue("The batchId should be \"LTC-25144-D050923-1\" but is " + loadDataset.getLtcCollaborationKey().getBatchId(), "LTC-25144-D050923-1".equals(loadDataset.getLtcCollaborationKey().getBatchId()));
			Assert.assertTrue("The subBatchId should be \"slksdadlsadew4332453245\" but is " + loadDataset.getLtcCollaborationKey().getSubBatchId(), "slksdadlsadew4332453245".equals(loadDataset.getLtcCollaborationKey().getSubBatchId()));
			Assert.assertTrue("The filename should be \"LoadAPITestFile_MQ1.xml\" but is " + loadDataset.getFileName(), "LoadAPITestFile_MQ1.xml".equals(loadDataset.getFileName()));
			
			ltcEvent = ltcMessage.getEventDataList().get(1);
			Assert.assertTrue("The event id should be 255541 but is " + ltcEvent.getEventId(), "255541".equals(ltcEvent.getEventId()));
			Assert.assertTrue("The event collection should be w_ltc_test5 but is " + ltcEvent.getCollection(), "w_ltc_test5".equals(ltcEvent.getCollection()));
			Assert.assertTrue("The event load element step should be \"Novus Load\" but is " + ltcEvent.getEventLoadElementStep(), "Novus Load".equals(ltcEvent.getEventLoadElementStep()));
			Assert.assertTrue("The event status should be \"Completed\" but is " + ltcEvent.getEventLoadStatus(), "Completed".equals(ltcEvent.getEventLoadStatus()));
			Assert.assertTrue("The event system message should be \""+systemMessage2+ "\" but is " + ltcEvent.getSystemMessage(), systemMessage2.equals(ltcEvent.getSystemMessage()));
			Assert.assertTrue("The event timestamp should be \"2005-09-23 09:57:35.343\" but is " + ltcEvent.getEventTimestamp(), "2005-09-23 09:57:35.343".equals(ltcEvent.getEventTimestamp()));
			Assert.assertTrue("Only one loaddataset should be present but is " + ltcEvent.getLoadDatasetList().size(),ltcEvent.getLoadDatasetList().size()==1);
			loadDataset = ltcEvent.getLoadDatasetList().get(0);
			Assert.assertTrue("The loaddatasetId should be \"82618\" but is " + loadDataset.getLoadDatasetId(), "82618".equals(loadDataset.getLoadDatasetId()));
			Assert.assertTrue("The loadrequestId should be \"58785\" but is " + loadDataset.getLoadRequestId(), "58785".equals(loadDataset.getLoadRequestId()));
			Assert.assertTrue("The collaborationKey should be \"LTC-25144-D050923-1_slksdadlsadew4332453246\" but is " + loadDataset.getRequestCollaborationKey(), "LTC-25144-D050923-1_slksdadlsadew4332453246".equals(loadDataset.getRequestCollaborationKey()));
			Assert.assertTrue("The batchId should be \"LTC-25144-D050923-1\" but is " + loadDataset.getLtcCollaborationKey().getBatchId(), "LTC-25144-D050923-1".equals(loadDataset.getLtcCollaborationKey().getBatchId()));
			Assert.assertTrue("The subBatchId should be \"slksdadlsadew4332453246\" but is " + loadDataset.getLtcCollaborationKey().getSubBatchId(), "slksdadlsadew4332453246".equals(loadDataset.getLtcCollaborationKey().getSubBatchId()));
			Assert.assertTrue("The filename should be \"LoadAPITestFile_MQ2.xml\" but is " + loadDataset.getFileName(), "LoadAPITestFile_MQ2.xml".equals(loadDataset.getFileName()));	
					
		} 
		catch (MessageParseException e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
}
