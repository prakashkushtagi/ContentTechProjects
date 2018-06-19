/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.trgr.dockets.core.domain.LtcAggregateFileInfo;
import com.trgr.dockets.core.domain.LtcRequestInfo;
import com.trgr.dockets.core.entity.CollectionEntity;

/**
 * @author C047166
 *
 */
public class LtcServiceTest
{
	private LtcService ltcService;
		
	private String collectionName1 = "testCollection1";
	private String collectionName2 = "testCollection2";
	
	@Before
	public void setUp(){
		LtcRequestInfo ltcRequestInfo = new LtcRequestInfo();
		ltcRequestInfo.setDataTypeName("NYDKT");
		ltcRequestInfo.setDataResourceName("NYDocketsDEV");
		ltcRequestInfo.setSubscriptionEvents("Load Complete;Failed to Initiate;Failed in Process;Suspended;Terminated");
		ltcRequestInfo.setQueueHost("jdgdevmq.int.westgroup.com");
		ltcRequestInfo.setQueueName("CONTECH.DOCKETS.LTC.REQ");
		ltcRequestInfo.setQueueChannel("CLIENTCONNECTION");
		ltcRequestInfo.setRequestUrl("http://ltcqa.int.westgroup.com/API/AutoRequest.htm");
		ltcService = new LtcServiceImpl(ltcRequestInfo);
	}
	
	@Test
	public void testGenerateNovusOnlyLtcRequestMessage(){
		File novusFile = new File("/test/novus1.xml");
		String expectedRequestMessage = "<?xml version=\"1.0\"?><ltc.autorequest><requests><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test121</request.collaboration.key><datatype><datatypename>NYDKT</datatypename><variant>Ordered Load</variant></datatype><datasets><dataset><datasetname>novus1.xml</datasetname><datasetpath>\\test\\</datasetpath><resource><datasetresource.name>NYDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request></requests><subscription.collection><subscription.data><subscription.owner>Dockets</subscription.owner><event.filter><event.publisher>LTC</event.publisher><event.type>SYSTEM.EVENT</event.type><event.owner>LTC</event.owner><event.extended.attributes><status>Load Complete</status><status>Failed to Initiate</status><status>Failed in Process</status><status>Suspended</status><status>Terminated</status></event.extended.attributes></event.filter><notification.criteria><notification><notification.type>queue</notification.type><notification.data><queues><queue><host.name>jdgdevmq.int.westgroup.com</host.name><queue.manager/><queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name><queue.channel>CLIENTCONNECTION</queue.channel><message.format>String</message.format></queue></queues></notification.data></notification></notification.criteria></subscription.data></subscription.collection></ltc.autorequest>";
		String ltcRequestMessage = ltcService.generateLtcRequestMessage(novusFile, null, null, "test121");
		Assert.assertEquals(expectedRequestMessage, ltcRequestMessage);
	}
	
	/*@Test
	public void testSendRequestToLTCURL() throws Exception{
		String request = "<?xml version=\"1.0\"?><ltc.autorequest><requests><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test4428822dd</request.collaboration.key><datatype><datatypename>STATEDKTLNK01_DEV</datatypename><variant>Ordered Load</variant></datatype><datasets><dataset><datasetname>novus.test9809000.130503.142947.xml.zip</datasetname><datasetpath>/dockets/data/</datasetpath><resource><datasetresource.name>BKRDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request></requests><subscription.collection><subscription.data><subscription.owner>Dockets</subscription.owner><event.filter><event.publisher>LTC</event.publisher><event.type>SYSTEM.EVENT</event.type><event.owner>LTC</event.owner><event.extended.attributes><status>Load Complete</status><status>Failed to Initiate</status><status>Failed in Process</status><status>Suspended</status><status>Terminated</status></event.extended.attributes></event.filter><notification.criteria><notification><notification.type>queue</notification.type><notification.data><queues><queue><host.name>jdgdevmq.int.westgroup.com</host.name><queue.manager/><queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name><queue.channel>CLIENTCONNECTION</queue.channel><message.format>String</message.format></queue></queues></notification.data></notification></notification.criteria></subscription.data></subscription.collection></ltc.autorequest>";
		System.out.println(ltcService.sendRequestToLTCURL(request));
	}*/
	
	@Test
	public void testGenerateNovusOnlyLtcRequestMessageMultipleFiles(){
		File novusFile1 = new File("/test/novus1.xml");
		File novusFile2 = new File("/test/novus2.xml");
		List<File> novusFileList = new ArrayList<File>();
		novusFileList.add(novusFile1);
		novusFileList.add(novusFile2);
		String expectedRequestMessage = "<?xml version=\"1.0\"?><ltc.autorequest><requests><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test</request.collaboration.key><datatype><datatypename>NYDKT</datatypename><variant>Ordered Load</variant></datatype><datasets><dataset><datasetname>novus1.xml</datasetname><datasetpath>\\test\\</datasetpath><resource><datasetresource.name>NYDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test</request.collaboration.key><datatype><datatypename>NYDKT</datatypename><variant>Ordered Load</variant></datatype><datasets><dataset><datasetname>novus2.xml</datasetname><datasetpath>\\test\\</datasetpath><resource><datasetresource.name>NYDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request></requests><subscription.collection><subscription.data><subscription.owner>Dockets</subscription.owner><event.filter><event.publisher>LTC</event.publisher><event.type>SYSTEM.EVENT</event.type><event.owner>LTC</event.owner><event.extended.attributes><status>Load Complete</status><status>Failed to Initiate</status><status>Failed in Process</status><status>Suspended</status><status>Terminated</status></event.extended.attributes></event.filter><notification.criteria><notification><notification.type>queue</notification.type><notification.data><queues><queue><host.name>jdgdevmq.int.westgroup.com</host.name><queue.manager/><queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name><queue.channel>CLIENTCONNECTION</queue.channel><message.format>String</message.format></queue></queues></notification.data></notification></notification.criteria></subscription.data></subscription.collection></ltc.autorequest>";
		String ltcRequestMessage = ltcService.generateLtcRequestMessage(novusFileList, new ArrayList<File>(), null, "test");
		Assert.assertEquals(expectedRequestMessage, ltcRequestMessage);
	}
	
	@Test
	public void testGenerateNovusAndMetadocLtcRequestMessage(){
		File novusFile1 = new File("/test/novus1.xml");
		File novusFile2 = new File("/test/novus2.xml");
		List<File> novusFileList = new ArrayList<File>();
		List<File> metadocFileList = new ArrayList<File>();
		novusFileList.add(novusFile1);
		novusFileList.add(novusFile2);
		metadocFileList.addAll(novusFileList);
		String expectedRequestMessage = "<?xml version=\"1.0\"?><ltc.autorequest><transaction><requests><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test</request.collaboration.key><datatype><datatypename>NYDKT</datatypename><variant>Ordered Load</variant></datatype><datasets><dataset><datasetname>novus1.xml</datasetname><datasetpath>\\test\\</datasetpath><resource><datasetresource.name>NYDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test</request.collaboration.key><datatype><datatypename>NYDKT</datatypename><variant>Ordered Load</variant></datatype><datasets><dataset><datasetname>novus2.xml</datasetname><datasetpath>\\test\\</datasetpath><resource><datasetresource.name>NYDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test</request.collaboration.key><datatype><datatypename>NYDKT</datatypename><variant>Metadoc Ordered Load</variant></datatype><datasets><dataset><datasetname>novus1.xml</datasetname><datasetpath>\\test\\</datasetpath><resource><datasetresource.name>NYDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request><request><requesttype>Load</requesttype><source>Dockets</source><requestdata><request.collaboration.key>test</request.collaboration.key><datatype><datatypename>NYDKT</datatypename><variant>Metadoc Ordered Load</variant></datatype><datasets><dataset><datasetname>novus2.xml</datasetname><datasetpath>\\test\\</datasetpath><resource><datasetresource.name>NYDocketsDEV</datasetresource.name></resource></dataset></datasets></requestdata></request></requests></transaction><subscription.collection><subscription.data><subscription.owner>Dockets</subscription.owner><event.filter><event.publisher>LTC</event.publisher><event.type>SYSTEM.EVENT</event.type><event.owner>LTC</event.owner><event.extended.attributes><status>Load Complete</status><status>Failed to Initiate</status><status>Failed in Process</status><status>Suspended</status><status>Terminated</status></event.extended.attributes></event.filter><notification.criteria><notification><notification.type>queue</notification.type><notification.data><queues><queue><host.name>jdgdevmq.int.westgroup.com</host.name><queue.manager/><queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name><queue.channel>CLIENTCONNECTION</queue.channel><message.format>String</message.format></queue></queues></notification.data></notification></notification.criteria></subscription.data></subscription.collection></ltc.autorequest>";
		String ltcRequestMessage = ltcService.generateLtcRequestMessage(novusFileList, metadocFileList, null, "test");
		Assert.assertEquals(expectedRequestMessage, ltcRequestMessage);
	}
	
	@Test
	public void testGenerateNovusAndMetadocLtcRequestMessageSingleCollection(){
		
		List<LtcAggregateFileInfo> loadFiles = new ArrayList<LtcAggregateFileInfo>();
				
		File novusFile1 = new File("/test/novus1.xml");
		File metadocFile1 = new File("/test/metadoc1.xml");
		
		LtcAggregateFileInfo loadFile = new LtcAggregateFileInfo(novusFile1, metadocFile1, collectionName1, null);
		
		loadFiles.add(loadFile);
		
		String ltcRequestMessage = ltcService.generateLtcRequestMessage(loadFiles, "test-collab-key_", null, generateCollectionDataTypeMap());
		Assert.assertEquals( expectedSingleCollRequest, ltcRequestMessage);
	}
		
	
	@Test
	public void testGenerateNovusAndMetadocLtcRequestMessageNoMetadoc(){
		
		List<LtcAggregateFileInfo> loadFiles = new ArrayList<LtcAggregateFileInfo>();
		
		File novusFile1 = new File("/test/novus1.xml");
	
		LtcAggregateFileInfo loadFile = new LtcAggregateFileInfo(novusFile1, null, collectionName1, null);
		
		loadFiles.add(loadFile);
		
		String ltcRequestMessage = ltcService.generateLtcRequestMessage(loadFiles, "test-collab-key_", null, generateCollectionDataTypeMap());
		Assert.assertEquals( expectedSingleCollRequestNoMetadoc, ltcRequestMessage);
	}
	
	
	@Test
	public void testGenerateNovusAndMetadocLtcRequestMessageMultipleCollections(){
		
		List<LtcAggregateFileInfo> loadFiles = new ArrayList<LtcAggregateFileInfo>();
		
		File novusFile1 = new File("/test/novus1.xml");
		File metadocFile1 = new File("/test/metadoc1.xml");
		LtcAggregateFileInfo loadFile = new LtcAggregateFileInfo(novusFile1, metadocFile1, collectionName1, null);
		
		loadFiles.add(loadFile);
		
		
		File novusFile2 = new File("/test/novus2.xml");
		File metadocFile2 = new File("/test/metadoc2.xml");
		
		LtcAggregateFileInfo loadFile2 = new LtcAggregateFileInfo(novusFile2, metadocFile2, collectionName2, null);
		
		loadFiles.add(loadFile2);
				
		String ltcRequestMessage = ltcService.generateLtcRequestMessage(loadFiles, "test-collab-key_", null, generateCollectionDataTypeMap());
		Assert.assertEquals(expectedMultipleCollectionRequest, ltcRequestMessage);
	}
	
	
	@Test
	public void testGenerateNovusAndMetadocLtcRequestMessageSingleCollMultipleFiles(){
		
		List<LtcAggregateFileInfo> loadFiles = new ArrayList<LtcAggregateFileInfo>();
		
		File novusFile1 = new File("/test/novus1-1.xml");
		File metadocFile1 = new File("/test/metadoc1-1.xml");
		LtcAggregateFileInfo loadFile = new LtcAggregateFileInfo(novusFile1, metadocFile1, collectionName1, null,  "1");
		
		loadFiles.add(loadFile);
		
		
		File novusFile2 = new File("/test/novus1-2.xml");
		File metadocFile2 = new File("/test/metadoc1-2.xml");
		
		LtcAggregateFileInfo loadFile2 = new LtcAggregateFileInfo(novusFile2, metadocFile2, collectionName1, null,  "2");
		
		loadFiles.add(loadFile2);
				
		String ltcRequestMessage = ltcService.generateLtcRequestMessage(loadFiles, "test-collab-key_", null, generateCollectionDataTypeMap());
		Assert.assertEquals(expectedSingleCollMultiFilesRequest, ltcRequestMessage);
	}
	

	
	private Map<String, CollectionEntity> generateCollectionDataTypeMap(){
		Map<String, CollectionEntity> collectionDataTypeMap = new HashMap<String, CollectionEntity>();
		
		CollectionEntity collEntity1 = new CollectionEntity();
		collEntity1.setLtcDatatype("NYDKT");
		collEntity1.setName(collectionName1);
		
		collectionDataTypeMap.put(collectionName1, collEntity1);
				
		CollectionEntity collEntity2 = new CollectionEntity();
		collEntity2.setLtcDatatype("NYDKT");
		collEntity2.setName(collectionName2);
		
		collectionDataTypeMap.put(collectionName2, collEntity2);
		
		return collectionDataTypeMap;
	}
	
	private String expectedSingleCollRequest = "<?xml version=\"1.0\"?>"
			+"<ltc.autorequest>"
			+"<transaction>"
				+"<requests>"
					+"<request>"
						+"<requesttype>Load</requesttype>"
						+"<source>Dockets</source>"
						+"<requestdata>"
							+"<request.collaboration.key>test-collab-key_testCollection1</request.collaboration.key>"
							+"<datatype>"
								+"<datatypename>NYDKT</datatypename>"
								+"<variant>Ordered Load</variant>"
							+"</datatype>"
							+"<datasets>"
								+"<dataset>"
									+"<datasetname>novus1.xml</datasetname>"
									+"<datasetpath>\\test\\</datasetpath>"
									+"<resource>"
										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
									+"</resource>"
								+"</dataset>"
							+"</datasets>"
						+"</requestdata>"
					+"</request>"
//					+"<request>"
//						+"<requesttype>Load</requesttype>"
//						+"<source>Dockets</source>"
//						+"<requestdata>"
//							+"<request.collaboration.key>test-collab-key_testCollection1</request.collaboration.key>"
//							+"<datatype>"
//								+"<datatypename>NYDKT</datatypename>"
//								+"<variant>Metadoc Ordered Load</variant>"
//							+"</datatype>"
//							+"<datasets>"
//								+"<dataset>"
//									+"<datasetname>metadoc1.xml</datasetname>"
//									+"<datasetpath>\\test\\</datasetpath>"
//									+"<resource>"
//										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
//									+"</resource>"
//								+"</dataset>"
//							+"</datasets>"
//						+"</requestdata>"
//					+"</request>"
				+"</requests>"
			+"</transaction>"
			+"<subscription.collection>"
				+"<subscription.data>"
					+"<subscription.owner>Dockets</subscription.owner>"
					+"<event.filter>"
						+"<event.publisher>LTC</event.publisher>"
						+"<event.type>SYSTEM.EVENT</event.type>"
						+"<event.owner>LTC</event.owner>"
						+"<event.extended.attributes>"
							+"<status>Load Complete</status>"
							+"<status>Failed to Initiate</status>"
							+"<status>Failed in Process</status>"
							+"<status>Suspended</status>"
							+"<status>Terminated</status>"
						+"</event.extended.attributes>"
					+"</event.filter>"
					+"<notification.criteria>"
						+"<notification>"
							+"<notification.type>queue</notification.type>"
							+"<notification.data>"
								+"<queues>"
									+"<queue>"
										+"<host.name>jdgdevmq.int.westgroup.com</host.name>"
										+"<queue.manager/>"
										+"<queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name>"
										+"<queue.channel>CLIENTCONNECTION</queue.channel>"
										+"<message.format>String</message.format>"
									+"</queue>"
								+"</queues>"
							+"</notification.data>"
						+"</notification>"
					+"</notification.criteria>"
				+"</subscription.data>"
			+"</subscription.collection>"
		+"</ltc.autorequest>";
	
	private String expectedSingleCollRequestNoMetadoc = "<?xml version=\"1.0\"?>"
			+"<ltc.autorequest>"
			+"<transaction>"
				+"<requests>"
					+"<request>"
						+"<requesttype>Load</requesttype>"
						+"<source>Dockets</source>"
						+"<requestdata>"
							+"<request.collaboration.key>test-collab-key_testCollection1</request.collaboration.key>"
							+"<datatype>"
								+"<datatypename>NYDKT</datatypename>"
								+"<variant>Ordered Load</variant>"
							+"</datatype>"
							+"<datasets>"
								+"<dataset>"
									+"<datasetname>novus1.xml</datasetname>"
									+"<datasetpath>\\test\\</datasetpath>"
									+"<resource>"
										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
									+"</resource>"
								+"</dataset>"
							+"</datasets>"
						+"</requestdata>"
					+"</request>"					
				+"</requests>"
			+"</transaction>"
			+"<subscription.collection>"
				+"<subscription.data>"
					+"<subscription.owner>Dockets</subscription.owner>"
					+"<event.filter>"
						+"<event.publisher>LTC</event.publisher>"
						+"<event.type>SYSTEM.EVENT</event.type>"
						+"<event.owner>LTC</event.owner>"
						+"<event.extended.attributes>"
							+"<status>Load Complete</status>"
							+"<status>Failed to Initiate</status>"
							+"<status>Failed in Process</status>"
							+"<status>Suspended</status>"
							+"<status>Terminated</status>"
						+"</event.extended.attributes>"
					+"</event.filter>"
					+"<notification.criteria>"
						+"<notification>"
							+"<notification.type>queue</notification.type>"
							+"<notification.data>"
								+"<queues>"
									+"<queue>"
										+"<host.name>jdgdevmq.int.westgroup.com</host.name>"
										+"<queue.manager/>"
										+"<queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name>"
										+"<queue.channel>CLIENTCONNECTION</queue.channel>"
										+"<message.format>String</message.format>"
									+"</queue>"
								+"</queues>"
							+"</notification.data>"
						+"</notification>"
					+"</notification.criteria>"
				+"</subscription.data>"
			+"</subscription.collection>"
		+"</ltc.autorequest>";

	private String expectedMultipleCollectionRequest = "<?xml version=\"1.0\"?>"
			+"<ltc.autorequest>"
			+"<transaction>"
				+"<requests>"
					+"<request>"
						+"<requesttype>Load</requesttype>"
						+"<source>Dockets</source>"
						+"<requestdata>"
							+"<request.collaboration.key>test-collab-key_testCollection1</request.collaboration.key>"
							+"<datatype>"
								+"<datatypename>NYDKT</datatypename>"
								+"<variant>Ordered Load</variant>"
							+"</datatype>"
							+"<datasets>"
								+"<dataset>"
									+"<datasetname>novus1.xml</datasetname>"
									+"<datasetpath>\\test\\</datasetpath>"
									+"<resource>"
										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
									+"</resource>"
								+"</dataset>"
							+"</datasets>"
						+"</requestdata>"
					+"</request>"
//					+"<request>"
//						+"<requesttype>Load</requesttype>"
//						+"<source>Dockets</source>"
//						+"<requestdata>"
//							+"<request.collaboration.key>test-collab-key_testCollection1</request.collaboration.key>"
//							+"<datatype>"
//								+"<datatypename>NYDKT</datatypename>"
//								+"<variant>Metadoc Ordered Load</variant>"
//							+"</datatype>"
//							+"<datasets>"
//								+"<dataset>"
//									+"<datasetname>metadoc1.xml</datasetname>"
//									+"<datasetpath>\\test\\</datasetpath>"
//									+"<resource>"
//										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
//									+"</resource>"
//								+"</dataset>"
//							+"</datasets>"
//						+"</requestdata>"
//					+"</request>"
					+"<request>"
						+"<requesttype>Load</requesttype>"
						+"<source>Dockets</source>"
						+"<requestdata>"
							+"<request.collaboration.key>test-collab-key_testCollection2</request.collaboration.key>"
							+"<datatype>"
								+"<datatypename>NYDKT</datatypename>"
								+"<variant>Ordered Load</variant>"
							+"</datatype>"
							+"<datasets>"
								+"<dataset>"
									+"<datasetname>novus2.xml</datasetname>"
									+"<datasetpath>\\test\\</datasetpath>"
									+"<resource>"
										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
									+"</resource>"
								+"</dataset>"
							+"</datasets>"
						+"</requestdata>"
					+"</request>"
//					+"<request>"
//						+"<requesttype>Load</requesttype>"
//						+"<source>Dockets</source>"
//						+"<requestdata>"
//							+"<request.collaboration.key>test-collab-key_testCollection2</request.collaboration.key>"
//							+"<datatype>"
//								+"<datatypename>NYDKT</datatypename>"
//								+"<variant>Metadoc Ordered Load</variant>"
//							+"</datatype>"
//							+"<datasets>"
//								+"<dataset>"
//									+"<datasetname>metadoc2.xml</datasetname>"
//									+"<datasetpath>\\test\\</datasetpath>"
//									+"<resource>"
//										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
//									+"</resource>"
//								+"</dataset>"
//							+"</datasets>"
//						+"</requestdata>"
//					+"</request>"
				+"</requests>"
			+"</transaction>"
			+"<subscription.collection>"
				+"<subscription.data>"
					+"<subscription.owner>Dockets</subscription.owner>"
					+"<event.filter>"
						+"<event.publisher>LTC</event.publisher>"
						+"<event.type>SYSTEM.EVENT</event.type>"
						+"<event.owner>LTC</event.owner>"
						+"<event.extended.attributes>"
							+"<status>Load Complete</status>"
							+"<status>Failed to Initiate</status>"
							+"<status>Failed in Process</status>"
							+"<status>Suspended</status>"
							+"<status>Terminated</status>"
						+"</event.extended.attributes>"
					+"</event.filter>"
					+"<notification.criteria>"
						+"<notification>"
							+"<notification.type>queue</notification.type>"
							+"<notification.data>"
								+"<queues>"
									+"<queue>"
										+"<host.name>jdgdevmq.int.westgroup.com</host.name>"
										+"<queue.manager/>"
										+"<queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name>"
										+"<queue.channel>CLIENTCONNECTION</queue.channel>"
										+"<message.format>String</message.format>"
									+"</queue>"
								+"</queues>"
							+"</notification.data>"
						+"</notification>"
					+"</notification.criteria>"
				+"</subscription.data>"
			+"</subscription.collection>"
		+"</ltc.autorequest>";
	
	
	
	private String expectedSingleCollMultiFilesRequest = "<?xml version=\"1.0\"?>"
			+"<ltc.autorequest>"
			+"<transaction>"
				+"<requests>"
					+"<request>"
						+"<requesttype>Load</requesttype>"
						+"<source>Dockets</source>"
						+"<requestdata>"
							+"<request.collaboration.key>test-collab-key_testCollection1_1</request.collaboration.key>"
							+"<datatype>"
								+"<datatypename>NYDKT</datatypename>"
								+"<variant>Ordered Load</variant>"
							+"</datatype>"
							+"<datasets>"
								+"<dataset>"
									+"<datasetname>novus1-1.xml</datasetname>"
									+"<datasetpath>\\test\\</datasetpath>"
									+"<resource>"
										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
									+"</resource>"
								+"</dataset>"
							+"</datasets>"
						+"</requestdata>"
					+"</request>"
//					+"<request>"
//						+"<requesttype>Load</requesttype>"
//						+"<source>Dockets</source>"
//						+"<requestdata>"
//							+"<request.collaboration.key>test-collab-key_testCollection1_1</request.collaboration.key>"
//							+"<datatype>"
//								+"<datatypename>NYDKT</datatypename>"
//								+"<variant>Metadoc Ordered Load</variant>"
//							+"</datatype>"
//							+"<datasets>"
//								+"<dataset>"
//									+"<datasetname>metadoc1-1.xml</datasetname>"
//									+"<datasetpath>\\test\\</datasetpath>"
//									+"<resource>"
//										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
//									+"</resource>"
//								+"</dataset>"
//							+"</datasets>"
//						+"</requestdata>"
//					+"</request>"
					+"<request>"
						+"<requesttype>Load</requesttype>"
						+"<source>Dockets</source>"
						+"<requestdata>"
							+"<request.collaboration.key>test-collab-key_testCollection1_2</request.collaboration.key>"
							+"<datatype>"
								+"<datatypename>NYDKT</datatypename>"
								+"<variant>Ordered Load</variant>"
							+"</datatype>"
							+"<datasets>"
								+"<dataset>"
									+"<datasetname>novus1-2.xml</datasetname>"
									+"<datasetpath>\\test\\</datasetpath>"
									+"<resource>"
										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
									+"</resource>"
								+"</dataset>"
							+"</datasets>"
						+"</requestdata>"
					+"</request>"
//					+"<request>"
//						+"<requesttype>Load</requesttype>"
//						+"<source>Dockets</source>"
//						+"<requestdata>"
//							+"<request.collaboration.key>test-collab-key_testCollection1_2</request.collaboration.key>"
//							+"<datatype>"
//								+"<datatypename>NYDKT</datatypename>"
//								+"<variant>Metadoc Ordered Load</variant>"
//							+"</datatype>"
//							+"<datasets>"
//								+"<dataset>"
//									+"<datasetname>metadoc1-2.xml</datasetname>"
//									+"<datasetpath>\\test\\</datasetpath>"
//									+"<resource>"
//										+"<datasetresource.name>NYDocketsDEV</datasetresource.name>"
//									+"</resource>"
//								+"</dataset>"
//							+"</datasets>"
//						+"</requestdata>"
//					+"</request>"
				+"</requests>"
			+"</transaction>"
			+"<subscription.collection>"
				+"<subscription.data>"
					+"<subscription.owner>Dockets</subscription.owner>"
					+"<event.filter>"
						+"<event.publisher>LTC</event.publisher>"
						+"<event.type>SYSTEM.EVENT</event.type>"
						+"<event.owner>LTC</event.owner>"
						+"<event.extended.attributes>"
							+"<status>Load Complete</status>"
							+"<status>Failed to Initiate</status>"
							+"<status>Failed in Process</status>"
							+"<status>Suspended</status>"
							+"<status>Terminated</status>"
						+"</event.extended.attributes>"
					+"</event.filter>"
					+"<notification.criteria>"
						+"<notification>"
							+"<notification.type>queue</notification.type>"
							+"<notification.data>"
								+"<queues>"
									+"<queue>"
										+"<host.name>jdgdevmq.int.westgroup.com</host.name>"
										+"<queue.manager/>"
										+"<queue.name>CONTECH.DOCKETS.LTC.REQ</queue.name>"
										+"<queue.channel>CLIENTCONNECTION</queue.channel>"
										+"<message.format>String</message.format>"
									+"</queue>"
								+"</queues>"
							+"</notification.data>"
						+"</notification>"
					+"</notification.criteria>"
				+"</subscription.data>"
			+"</subscription.collection>"
		+"</ltc.autorequest>";

}
