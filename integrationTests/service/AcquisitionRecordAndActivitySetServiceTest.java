/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.hibernate.ScrollableResults;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.acquisition.AcquisitionResponse;
import com.trgr.dockets.core.acquisition.ActivitySet;
import com.trgr.dockets.core.acquisition.WorkflowInformation;
import com.trgr.dockets.core.acquisition.service.AcquisitionRecordAndActivitySetService;
import com.trgr.dockets.core.entity.AcquisitionLookup;
import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.BatchDocket;
import com.trgr.dockets.core.entity.BatchDocketKey;
import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.Status;
import com.trgr.dockets.core.service.ContentService;
import com.trgr.dockets.core.service.ContentVersionService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.DateUtils;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * @author C047166
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional
public class AcquisitionRecordAndActivitySetServiceTest
{
	@Rule
    public TemporaryFolder folder= new TemporaryFolder();
	
	@Autowired
	private AcquisitionRecordAndActivitySetService acquisitionRecordAndActivitySetService;
	
	@Autowired
	private DocketService docketService;
	
	@Autowired
	private DocketHistoryService docketHistoryService;
	
	@Autowired
	private ContentVersionService contentVersionService;
	
	@Autowired
	private ContentService contentService;
	
	private String pathString;

	@Before
	public void setUp()
	{
		URL path = getClass().getResource("AcquisitionRecordAndActivitySetServiceTest.class");
		pathString = path.getPath();
		pathString = pathString.substring(0,pathString.indexOf("service"));
		pathString = pathString + "/resources/";
	}

//	@Test
//	public void testProcessBKRAcqLogsLocal() 
//	{
//		String fileLocation = "/dockets/deletecolumn/";
//		
//		File aqLogDir = new File(fileLocation);
//		
//		String[] listOfaqLogs = aqLogDir.list();
//		WorkflowInformation workflowInformation = new WorkflowInformation("\\temp\\workfolder", "\\temp\\errorfolder", "\\temp\\processfolder","\\temp\\novusFolder","localjunit");
//		
//		try 
//		{
//			for(String aqLog : listOfaqLogs)
//			{
//				File aqLogFile = new File(fileLocation+aqLog);
//				AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processBKRAcquisitionRecord(aqLogFile, RequestTypeEnum.WCW, WorkflowTypeEnum.STATEDOCKET_PREDOCKET, workflowInformation);
//			}
//		} 
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//			Assert.fail();
//		}
//	}
	
	@Test
	public void testProcessBKRAcquisitionRecordHappyPath() throws Exception
	{
	
		String fileName = "acquisitionRecord.xml";
		String aqLogFileLocation = pathString + fileName;
		WorkflowInformation workflowInformation = new WorkflowInformation("\\temp\\workfolder", "\\temp\\errorfolder", "\\temp\\processfolder","\\temp\\novusFolder","localjunit");
		File aqLogFile = new File(aqLogFileLocation);
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processBKRAcquisitionRecord(aqLogFile, RequestTypeEnum.WCW, WorkflowTypeEnum.STATEDOCKET_PREDOCKET, workflowInformation);
		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(publishingRequest);
		Assert.assertEquals("n_dlawdct.5:2013cv00143.012313.114640", publishingRequest.getRequestName());
		Assert.assertEquals(RequestTypeEnum.WCW, publishingRequest.getRequestType());
		Assert.assertEquals(WorkflowTypeEnum.STATEDOCKET_PREDOCKET, publishingRequest.getWorkflowType());
		Assert.assertEquals("Data Capture", publishingRequest.getRequestOwner());
		Assert.assertEquals(ProductEnum.STATE, publishingRequest.getProduct().getPrimaryKey());
		Assert.assertEquals(new Status(StatusEnum.RUNNING), publishingRequest.getPublishingStatus());
		Assert.assertEquals(RequestInitiatorTypeEnum.DAILY, publishingRequest.getRequestInitiatorType());
		
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertNull(batch.getParentBatch());
		
		batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertNotNull(batch.getParentBatch());
		Assert.assertEquals(batch.getParentBatch().getPrimaryKey().toString(),acquisitionResponse.getPublishingRequestId());
		
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
		Assert.assertEquals(new Status(StatusEnum.RUNNING), batchMonitor.getStatus());
		Assert.assertEquals(Long.valueOf(1), batchMonitor.getDocsInCount()); 
		Assert.assertEquals(batchMonitor.getServerHostname(),workflowInformation.getAppServerName());
		Assert.assertEquals(batchMonitor.getWorkDirectory().getPath(),workflowInformation.getWorkFolder());
		Assert.assertEquals(batchMonitor.getProcessDirectory().getPath(),workflowInformation.getProcessFolder());
		Assert.assertEquals(batchMonitor.getErrorDirectory().getPath(),workflowInformation.getErrorFolder());
		/*ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys("I5f4f02f0b68711e2a117a297e3a25cca");
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);	*/	
	}
	
	@Test
	public void testProcessBKRAcquisitionRecordWithMultipleCourts() throws Exception
	{
	
		String fileName = "acquisitionRecordMultipleCourts.xml";
		String aqLogFileLocation = pathString + fileName;
		WorkflowInformation workflowInformation = new WorkflowInformation("\\temp\\workfolder", "\\temp\\errorfolder", "\\temp\\processfolder","\\temp\\novusFolder","localjunit");
		File aqLogFile = new File(aqLogFileLocation);
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processBKRAcquisitionRecord(aqLogFile, RequestTypeEnum.WCW, WorkflowTypeEnum.STATEDOCKET_PREDOCKET, workflowInformation);
		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(publishingRequest);
		Assert.assertEquals("WEST.BKR.preprocout.031512.001642.xml.031512.034254", publishingRequest.getRequestName());
		Assert.assertEquals(RequestTypeEnum.WCW, publishingRequest.getRequestType());
		Assert.assertEquals(WorkflowTypeEnum.STATEDOCKET_PREDOCKET, publishingRequest.getWorkflowType());
		Assert.assertEquals("Data Capture", publishingRequest.getRequestOwner());
		Assert.assertEquals(ProductEnum.FBR, publishingRequest.getProduct().getPrimaryKey());
		Status expectedStatus = new Status(StatusEnum.RUNNING);
		Assert.assertEquals(expectedStatus, publishingRequest.getPublishingStatus());
		Assert.assertEquals(RequestInitiatorTypeEnum.DAILY, publishingRequest.getRequestInitiatorType());
		
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertNull(batch.getParentBatch());
		
		batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertNotNull(batch.getParentBatch());
		Assert.assertEquals(batch.getParentBatch().getPrimaryKey().toString(),acquisitionResponse.getPublishingRequestId());
		
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
		expectedStatus = new Status(StatusEnum.RUNNING);
		Assert.assertEquals(expectedStatus, batchMonitor.getStatus());
		Assert.assertEquals(Long.valueOf(339), batchMonitor.getDocsInCount()); 
		Assert.assertEquals(batchMonitor.getServerHostname(),workflowInformation.getAppServerName());
		Assert.assertEquals(batchMonitor.getWorkDirectory().getPath(),workflowInformation.getWorkFolder());
		Assert.assertEquals(batchMonitor.getProcessDirectory().getPath(),workflowInformation.getProcessFolder());
		Assert.assertEquals(batchMonitor.getErrorDirectory().getPath(),workflowInformation.getErrorFolder());
		/*ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);	*/	
	}
	
	@Test
	public void testProcessBKRAcquisitionRecordFromPBUIPubReqPassed() throws Exception
	{
		PublishingRequest publishingRequest = new PublishingRequest(
				UUIDGenerator.createUuid(), "WEST.BKR.preprocout.031512.001642.xml.031512.034254",
				"sadas", false, false, false,
				new Date(), null, null,
				null,
				RequestInitiatorTypeEnum.DAILY, null,
				WorkflowTypeEnum.STATEDOCKET_PREDOCKET, RequestTypeEnum.WCW,
				new Status(StatusEnum.RUNNING), null,null, null, null, 0L, false, null, null, null, null);

		
		String fileName = "acquisitionRecordMultipleCourts.xml";
		String aqLogFileLocation = pathString + fileName;
		WorkflowInformation workflowInformation = new WorkflowInformation("\\temp\\workfolder", "\\temp\\errorfolder", "\\temp\\processfolder","\\temp\\novusFolder","localjunit");
		File aqLogFile = new File(aqLogFileLocation);
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processBKRAcquisitionRecord(aqLogFile, RequestTypeEnum.WCW, WorkflowTypeEnum.STATEDOCKET_PREDOCKET, workflowInformation, publishingRequest);
		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertNull(batch.getParentBatch());
		
		batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertNotNull(batch.getParentBatch());
		Assert.assertEquals(batch.getParentBatch().getPrimaryKey().toString(),acquisitionResponse.getPublishingRequestId());
		
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
		Status expectedStatus = new Status(StatusEnum.RUNNING);
		Assert.assertEquals(expectedStatus, batchMonitor.getStatus());
		Assert.assertEquals(Long.valueOf(339), batchMonitor.getDocsInCount()); 
		Assert.assertEquals(batchMonitor.getServerHostname(),workflowInformation.getAppServerName());
		Assert.assertEquals(batchMonitor.getWorkDirectory().getPath(),workflowInformation.getWorkFolder());
		Assert.assertEquals(batchMonitor.getProcessDirectory().getPath(),workflowInformation.getProcessFolder());
		Assert.assertEquals(batchMonitor.getErrorDirectory().getPath(),workflowInformation.getErrorFolder());
		/*ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);	*/	
	}
	
	
	@Test
	public void testProcessBKRAcquisitionRecordErrorPath() throws IOException
	{
		File tempFile = folder.newFile("acquisitionRecord.xml");
		AcquisitionResponse acquisitionResponse = null;
		WorkflowInformation workflowInformation = new WorkflowInformation("/temp/workfolder", "/temp/errorfolder", "/temp/processfolder","/temp/processFolder","localjunit");
		
		try{
			acquisitionResponse = acquisitionRecordAndActivitySetService.processBKRAcquisitionRecord(tempFile, RequestTypeEnum.WCW, WorkflowTypeEnum.STATEDOCKET_PREDOCKET,workflowInformation);
		}catch(Exception e){
		}
		Assert.assertNull(acquisitionResponse);
	}
	
	@Test
	public void testProcessNYAcquisitionRecordHappyPath() throws Exception
	{
		String acquisitionRecord = 
			"<new.acquisition.record " +
		        "dcs.receipt.id=\"70219216\" " +
		        "sender.id=\"Data Capture\" " +
		        "merged.file.name=\"n_dlawdct.5:2013cv00143\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
	        		"<court westlaw.cluster.name=\"n_dnydownstate\" acquisition.status=\"success\" court.type=\"State\"> "+
	        			"<acquired.dockets> "+
	        				"<acquired.docket.status status=\"captured\"> "+
	        					"<state.docket docket.number=\"0501289/2013\" filename=\"KINGS20130501289\" subfolder=\"\" subdivision=\"\"> "+
	        						"<docket.entry> "+
	        							"<pdf.file filename = \"N_DNYDOWNSTATE.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
	        						"</docket.entry> "+
	        					"</state.docket> "+
	        				"</acquired.docket.status> "+
	        			"</acquired.dockets> "+
	        		"</court> "+
		    "</new.acquisition.record> ";
		
		URL url = AcquisitionRecordAndActivitySetServiceTest.class.getResource("/resources/n_dnykings/datacap_pre_new/KINGS20130501289.gz");
		String[] directoryTokens = url.getPath().split("n_dnykings");
		acquisitionRecordAndActivitySetService.setPreDocketRootDirectoryState(directoryTokens[0]);
		acquisitionRecordAndActivitySetService.setRootWorkDirectory(directoryTokens[0]);
		
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);

		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(publishingRequest);
		Assert.assertEquals("n_dlawdct.5:2013cv00143.012313.114640", publishingRequest.getRequestName());
		Assert.assertEquals(RequestTypeEnum.WCW, publishingRequest.getRequestType());
		Assert.assertEquals(WorkflowTypeEnum.STATEDOCKET_PREDOCKET, publishingRequest.getWorkflowType());
		Assert.assertEquals("Data Capture", publishingRequest.getRequestOwner());
		Assert.assertEquals(ProductEnum.STATE, publishingRequest.getProduct().getPrimaryKey());
		Assert.assertEquals(new Status(StatusEnum.RUNNING), publishingRequest.getPublishingStatus());
		Assert.assertEquals(RequestInitiatorTypeEnum.DAILY, publishingRequest.getRequestInitiatorType());
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertEquals(batch.getParentBatch().getPrimaryKey(), publishingRequest.getRequestId());
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
		Assert.assertEquals(new Status(StatusEnum.RUNNING), batchMonitor.getStatus());
		Assert.assertEquals(Long.valueOf(1), batchMonitor.getDocsInCount()); 
		
		AcquisitionLookup acquisitionLookup = docketService.findAcquisitionLookupByRequestKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(acquisitionLookup);
		Assert.assertEquals("70219216", acquisitionLookup.getReceiptId());
		
		ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);
		
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey("NYKINGS05012892013");
		Assert.assertNotNull(docketEntity);
		Assert.assertEquals("0501289/2013", docketEntity.getDocketNumber());
		Assert.assertEquals("0501289", docketEntity.getSequenceNumber());
		Assert.assertEquals(Long.valueOf("2013"), docketEntity.getFilingYear());
		Assert.assertEquals("Santos v. Manga", docketEntity.getTitle());
		Assert.assertEquals(null, docketEntity.getCaseSubTypeId());
		Assert.assertEquals(DateUtils.getFormattedDate("20130321", "yyyyMMdd"), docketEntity.getFilingDate());
		Assert.assertEquals(Long.valueOf(3), docketEntity.getCountyId());
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("NYKINGS05012892013", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
		Assert.assertNotNull(sourceDocketVersion);
		Assert.assertNotNull(sourceDocketVersion.getPrimaryKey().getVersionDate());
		Assert.assertEquals("n_dlawdct.5:2013cv00143", sourceDocketVersion.getSourceFile().getName());
		Assert.assertEquals(Phase.SOURCE, sourceDocketVersion.getPrimaryKey().getPhase());
		Assert.assertEquals(Long.valueOf(1), sourceDocketVersion.getVersion());
		
		DocketVersion novusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt("NYKINGS05012892013", docketEntity.getCourt(), 10L);
		Assert.assertNull(novusDocketVersion);
				
		List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("NYKINGS05012892013");
		Assert.assertEquals(1, docketHistoryList.size());
		DocketHistory docketHistory = docketHistoryList.get(0);
		Assert.assertNotNull(docketHistory);
		Assert.assertEquals("n_dlawdct.5:2013cv00143.012313.114640", docketHistory.getRequestName());
		
		Content content = contentService.findContentByUuid(sourceDocketVersion.getContentId());
		Assert.assertNotNull(content);
				
		//check source content version
		List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(sourceDocketVersion.getContentId());
		Assert.assertEquals(1, contentVersionList.size());
		
		//cleanup
		File unzipPreDocketFile = new File(directoryTokens[0] + "STATE");
		FileUtils.deleteDirectory(unzipPreDocketFile);

	}
	
	@Test
	public void testProcessDCTAcquisitionRecordHappyPath() throws Exception
	{
		String acquisitionRecord = 
			"<new.acquisition.record " +
				"dcs.receipt.id=\"111748301\" " +
				"sender.id=\"PreDocket Publisher\" " +
				"merged.file.name=\"n_dksdct.2:2015cv99999\" " +				
				"merged.file.size=\"1398\" " +
				"script.start.date.time=\"Fri Jul 22 01:28:41 CDT 2016\" " +
				"script.end.date.time=\"Fri Jul 22 01:28:41 CDT 2016\" " +
				"docket.type=\"predocket\" " +
				"retrieve.type=\"daily\" > " +
				"<court westlaw.cluster.name=\"n_dksdct\" acquisition.status=\"success\" court.type=\"DCT\"> " +
					"<acquired.dockets> " +
						"<acquired.docket.status status=\"captured\"> " +
						"<dct.docket docket.number=\"2:2015cv99999\" filename=\"KSDCT_2_15cv99999\" case.type=\"cv\" filing.year=\"2015\" sequence.number=\"99999\" subfolder=\"\" filing.location=\"2\" subdivision=\"\">" +						
								"<docket.entry> " +
									"<pdf.file filename = \"\" uuid=\"\" /> " +
								"</docket.entry> " +
							"</dct.docket> " +
						"</acquired.docket.status> " +
					"</acquired.dockets> "+
				"</court> "+
			"</new.acquisition.record> ";

		URL url = AcquisitionRecordAndActivitySetServiceTest.class.getResource("/resources/n_dksdct/datacap_pre_new/KSDCT_2_15cv99999.gz");
		String[] directoryTokens = url.getPath().split("n_dksdct");
		acquisitionRecordAndActivitySetService.setPreDocketRootDirectoryFederal(directoryTokens[0]);
		acquisitionRecordAndActivitySetService.setRootWorkDirectory(directoryTokens[0]);
		
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);

		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(publishingRequest);
		Assert.assertEquals("n_dksdct.2:2015cv99999.072216.012841", publishingRequest.getRequestName());
		Assert.assertEquals(RequestTypeEnum.WCW, publishingRequest.getRequestType());
		Assert.assertEquals(WorkflowTypeEnum.DCT_PREDOCKET, publishingRequest.getWorkflowType());
		Assert.assertEquals("PreDocket Publisher", publishingRequest.getRequestOwner());
		Assert.assertEquals(ProductEnum.DCT, publishingRequest.getProduct().getPrimaryKey());
		Assert.assertEquals(new Status(StatusEnum.RUNNING), publishingRequest.getPublishingStatus());
		Assert.assertEquals(RequestInitiatorTypeEnum.DAILY, publishingRequest.getRequestInitiatorType());
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertEquals(batch.getParentBatch().getPrimaryKey(), publishingRequest.getRequestId());
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
		Assert.assertEquals(new Status(StatusEnum.RUNNING), batchMonitor.getStatus());
		Assert.assertEquals(Long.valueOf(1), batchMonitor.getDocsInCount()); 
		
		AcquisitionLookup acquisitionLookup = docketService.findAcquisitionLookupByRequestKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(acquisitionLookup);
		Assert.assertEquals("111748301", acquisitionLookup.getReceiptId());
		
		ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);
		
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey("KS-DCT2:15CV99999");		
		Assert.assertNotNull(docketEntity);
		Assert.assertEquals("2:15-CV-99999", docketEntity.getDocketNumber());
		Assert.assertEquals("99999", docketEntity.getSequenceNumber());		
		Assert.assertEquals(Long.valueOf("2015"), docketEntity.getFilingYear());
		Assert.assertEquals("WESTPORT INSURANCE CORPORATION v. GUIDEONE MUTUAL INSURANCE COMPANY", docketEntity.getTitle());
		Assert.assertEquals(null, docketEntity.getCaseSubTypeId());
		Assert.assertEquals(DateUtils.getFormattedDate("20150102", "yyyyMMdd"), docketEntity.getFilingDate());
		
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("KS-DCT2:15CV99999", docketEntity.getCourt(), 48L, ProductEnum.DCT.getKey());		
		Assert.assertNotNull(sourceDocketVersion);
		Assert.assertNotNull(sourceDocketVersion.getPrimaryKey().getVersionDate());
		Assert.assertEquals("n_dksdct.2:2015cv99999", sourceDocketVersion.getSourceFile().getName());
		Assert.assertEquals(Phase.SOURCE, sourceDocketVersion.getPrimaryKey().getPhase());
		Assert.assertEquals(Long.valueOf(1), sourceDocketVersion.getVersion());
		
		DocketVersion novusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt("KS-DCT2:15CV99999", docketEntity.getCourt(), 48L);		
		Assert.assertNull(novusDocketVersion);
				
		List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("KS-DCT2:15CV99999");		
		Assert.assertEquals(1, docketHistoryList.size());
		DocketHistory docketHistory = docketHistoryList.get(0);
		Assert.assertNotNull(docketHistory);
		Assert.assertEquals("n_dksdct.2:2015cv99999.072216.012841", docketHistory.getRequestName());
		
		Content content = contentService.findContentByUuid(sourceDocketVersion.getContentId());
		Assert.assertNotNull(content);
				
		//check source content version
		List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(sourceDocketVersion.getContentId());
		Assert.assertEquals(1, contentVersionList.size());
		
		//cleanup
		File unzipPreDocketFile = new File(directoryTokens[0] + "DCT");
		FileUtils.deleteDirectory(unzipPreDocketFile);

		String activitySet = 
			"<activity.set receipt.id=\"111748301\">" + 
				"<activity name=\"DOCKETCONVERSION\" documents.in=\"1\" documents.out=\"1\" start.time=\"20160831055326\" end.time=\"20160831055326\">" + 
				"</activity>" + 
				"<activity name=\"SABER\" documents.in=\"1\" documents.out=\"1\" start.time=\"20160831055326\" end.time=\"20160831055326\">" + 
					"<metalog>" + 
						"<dockets uuid=\"I3753DA11647211E6A807AD48145ED9F1\" court=\"N_DKSDCT\" file.date=\"20120803\" number=\"2:2015cv99999\"/>" +
					"</metalog>" +
				"</activity>" + 
				"<activity name=\"EHANOVUSLOAD\" documents.in=\"1\" documents.out=\"1\" start.time=\"20160831055355\" end.time=\"20160831055355\">" +
				"</activity>" + 
				"<activity name=\"SUMMARYSERVICE\" documents.in=\"1\" documents.out=\"1\" start.time=\"20160831055435\" end.time=\"20160831055435\">" +
				"</activity>" + 
				"<activity documents.in=\"1\" documents.out=\"1\" end.time=\"20160831055535\" name=\"HANOVUSLOAD\" start.time=\"20160831055435\"/>" +
				"<activity.run datatype=\"predocket-publisher-pathway\" run.date=\"20160831\" run.time=\"055339717\" machine=\"c834vxrctdktf\" start.time=\"08/31/2016 05:53:26\" end.time=\"08/31/2016 05:54:35\" source.file=\"/ct-data/prod/dockets/source/feddctdocket/pre-dockets_lower_env/TEST/n_dksdct/datacap_pre_new/2:15cv99999.gz\" userid=\"datacap\" status=\"SUCCESS\">" +
				"</activity.run>" +
			"</activity.set>";
		
		acquisitionRecordAndActivitySetService.processActivitySetRecord(activitySet);
		DocketVersion newNovusDocketVersion = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase("KS-DCT2:15CV99999", docketEntity.getCourt(), ProductEnum.DCT.getKey(), Phase.NOVUS);
		Assert.assertNotNull(newNovusDocketVersion);
		
		docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("KS-DCT2:15CV99999");
		//1 for insert and 3 for update
		Assert.assertEquals(4, docketHistoryList.size());
		
		sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("KS-DCT2:15CV99999",  docketEntity.getCourt(), 48L, ProductEnum.DCT.getKey());
			
		DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType("KS-DCT2:15CV99999", DocketVersionRelTypeEnum.SOURCE_TO_NOVUS);
		Assert.assertNotNull(docketVersionRel);
		Assert.assertNotNull(docketVersionRel.getRelTimestamp());
		Assert.assertEquals(sourceDocketVersion.getPrimaryKey().getVersionDate(), docketVersionRel.getRelTimestamp());
	}
	
	@Test
	public void testProcessWithSubdivision() throws Exception
	{
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"70219216\" " +
		        "sender.id=\"PreDocket Publisher\" " +
		        "merged.file.name=\"n_dpaallegTest.GD-14-999999\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
		                        "<court westlaw.cluster.name=\"n_dpaalleg\" acquisition.status=\"success\" court.type=\"State\"> "+
		                                "<acquired.dockets> "+
		                                        "<acquired.docket.status status=\"captured\"> "+
		                                                "<state.docket docket.number=\"GD-14-999999\" filename=\"KINGS20130501289\"  subfolder=\"\" subdivision=\"GENERALDOCKET\"/> "+
		                                        "</acquired.docket.status> "+
		                                "</acquired.dockets> "+
		                        "</court> "+
		"</new.acquisition.record> ";
		
		URL url = AcquisitionRecordAndActivitySetServiceTest.class.getResource("/resources/n_dnykings/datacap_pre_new/KINGS20130501289.gz");
		String[] directoryTokens = url.getPath().split("n_dnykings");
		acquisitionRecordAndActivitySetService.setPreDocketRootDirectoryState(directoryTokens[0]);
		acquisitionRecordAndActivitySetService.setRootWorkDirectory(directoryTokens[0]);
		
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);

		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(publishingRequest);
		Assert.assertEquals("n_dpaallegTest.GD-14-999999.012313.114640", publishingRequest.getRequestName());
		Assert.assertEquals(RequestTypeEnum.WCW, publishingRequest.getRequestType());
		Assert.assertEquals(WorkflowTypeEnum.STATEDOCKET_PREDOCKET, publishingRequest.getWorkflowType());
		Assert.assertEquals("PreDocket Publisher", publishingRequest.getRequestOwner());
		Assert.assertEquals(ProductEnum.STATE, publishingRequest.getProduct().getPrimaryKey());
		Assert.assertEquals(new Status(StatusEnum.RUNNING), publishingRequest.getPublishingStatus());
		Assert.assertEquals(RequestInitiatorTypeEnum.DAILY, publishingRequest.getRequestInitiatorType());
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertEquals(batch.getParentBatch().getPrimaryKey(), publishingRequest.getRequestId());
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
		Assert.assertEquals(new Status(StatusEnum.RUNNING), batchMonitor.getStatus());
		Assert.assertEquals(Long.valueOf(1), batchMonitor.getDocsInCount()); 
		
		AcquisitionLookup acquisitionLookup = docketService.findAcquisitionLookupByRequestKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(acquisitionLookup);
		Assert.assertEquals("70219216", acquisitionLookup.getReceiptId());
		
		ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);
		
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey("PAALLEGHENYGD-14-999999GENERALDOCKET");
		Assert.assertNotNull(docketEntity);
		Assert.assertEquals("GD-14-999999", docketEntity.getDocketNumber());
		Assert.assertEquals(null, docketEntity.getSequenceNumber());
		Assert.assertEquals(Long.valueOf(0), docketEntity.getFilingYear());
		Assert.assertEquals(Long.valueOf(711), docketEntity.getCountyId());
		
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("PAALLEGHENYGD-14-999999GENERALDOCKET", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
		Assert.assertNotNull(sourceDocketVersion);
		Assert.assertNotNull(sourceDocketVersion.getPrimaryKey().getVersionDate());
		Assert.assertEquals(Long.valueOf(1), sourceDocketVersion.getVersion());
		
		DocketVersion novusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt("PAALLEGHENYGD-14-999999GENERALDOCKET", docketEntity.getCourt(), ProductEnum.STATE.getKey());
		Assert.assertNull(novusDocketVersion);
				
		List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("PAALLEGHENYGD-14-999999GENERALDOCKET");
		Assert.assertEquals(1, docketHistoryList.size());
		DocketHistory docketHistory = docketHistoryList.get(0);
		Assert.assertNotNull(docketHistory);
		Assert.assertEquals("n_dpaallegTest.GD-14-999999.012313.114640", docketHistory.getRequestName());
		
		Content content = contentService.findContentByUuid(sourceDocketVersion.getContentId());
		Assert.assertNotNull(content);
				
		//check source content version
		List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(sourceDocketVersion.getContentId());
		Assert.assertEquals(1, contentVersionList.size());
		
		//cleanup
		File unzipPreDocketFile = new File(directoryTokens[0] + "STATE");
		FileUtils.deleteDirectory(unzipPreDocketFile);
	}
	

	
	@Test
	public void testProcessAcquisitionLogWebErrors() throws Exception
	{
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"107152225\" " + 
		        "sender.id=\"Data Capture\" " +
		        "merged.file.name=\"\" " +
		        "merged.file.size=\"\" " +
		        "script.start.date.time=\"Thu Feb 18 23:00:17 CST 2016\" " + 
		        "script.end.date.time=\"Thu Feb 18 23:12:23 CST 2016\" " +
		        "retrieve.type=\"daily\" >" +
				"	<court westlaw.cluster.name=\"N_DCASTANIS\" acquisition.status=\"failure\" court.type=\"State\">" +
				"		<website.errors>" +
				"			<web.error.docket reason=\"court_error\" westget.stage=\"get_list\" status=\"failed_in_getlist\" curl.code=\"22\" http.error.code=\"502\" court.error.message=\"search_failure\">" +
				"				<state.docket docket.number=\"NULL\" filename=\"NULL\" case.type=\"\" filing.year=\"\" subfolder=\"\" subdivision=\"\" alt.docket.id=\"\"/>" +
				"			</web.error.docket>" +
		        "      </website.errors>" +
				"	</court>" +
				"</new.acquisition.record>";
				
			acquisitionRecordAndActivitySetService.processFailedDocketRecord(acquisitionRecord);
		
		

		Assert.assertTrue(true);
	}

	@Test
	public void testProcessNYAcquisitionRecordHappyPath_forUT() throws Exception
	{
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"70219216\" " +
		        "sender.id=\"PreDocket Publisher\" " +
		        "merged.file.name=\"n_dutsalt.110903302\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
		                        "<court westlaw.cluster.name=\"n_dutsalt\" acquisition.status=\"success\" court.type=\"State\"> "+
		                                "<acquired.dockets> "+
		                                        "<acquired.docket.status status=\"captured\"> "+
		                                                "<state.docket docket.number=\"110903302\" filename=\"110903302\" subfolder=\"\" subdivision=\"\"> "+
		                                                        "<docket.entry> "+
		                                                        	"<pdf.file filename = \"\" uuid=\"\" /> "+
		                                                        "</docket.entry> "+
		                                                "</state.docket> "+
		                                        "</acquired.docket.status> "+
		                                "</acquired.dockets> "+
		                        "</court> "+
		"</new.acquisition.record> ";
		
		URL url = AcquisitionRecordAndActivitySetServiceTest.class.getResource("/resources/n_dutsalt/datacap_pre_new/110903302.gz");
		String[] directoryTokens = url.getPath().split("n_dutsalt");
		acquisitionRecordAndActivitySetService.setPreDocketRootDirectoryState(directoryTokens[0]);
		acquisitionRecordAndActivitySetService.setRootWorkDirectory(directoryTokens[0]);
		
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);
		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(publishingRequest);
		Assert.assertEquals("n_dutsalt.110903302.012313.114640", publishingRequest.getRequestName());
		Assert.assertEquals(RequestTypeEnum.WCW, publishingRequest.getRequestType());
		Assert.assertEquals(WorkflowTypeEnum.STATEDOCKET_PREDOCKET, publishingRequest.getWorkflowType());
		Assert.assertEquals("PreDocket Publisher", publishingRequest.getRequestOwner());
		Assert.assertEquals(ProductEnum.STATE, publishingRequest.getProduct().getPrimaryKey());
		Assert.assertEquals(new Status(StatusEnum.RUNNING), publishingRequest.getPublishingStatus());
		Assert.assertEquals(RequestInitiatorTypeEnum.DAILY, publishingRequest.getRequestInitiatorType());
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		Assert.assertEquals(batch.getParentBatch().getPrimaryKey(), publishingRequest.getRequestId());
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
		Assert.assertEquals(new Status(StatusEnum.RUNNING), batchMonitor.getStatus());
		Assert.assertEquals(Long.valueOf(1), batchMonitor.getDocsInCount()); 
		
		AcquisitionLookup acquisitionLookup = docketService.findAcquisitionLookupByRequestKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(acquisitionLookup);
		Assert.assertEquals("70219216", acquisitionLookup.getReceiptId());
		
		ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);
		
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey("UTSALTLAKE110903302");
		Assert.assertNotNull(docketEntity);
		Assert.assertEquals("110903302", docketEntity.getDocketNumber());
		Assert.assertEquals(null, docketEntity.getSequenceNumber());
		Assert.assertEquals(Long.valueOf(0), docketEntity.getFilingYear());
		Assert.assertEquals("Smith v. Neal", docketEntity.getTitle());
		Assert.assertEquals(null, docketEntity.getCaseSubTypeId());
		Assert.assertEquals(DateUtils.getFormattedDate("20110209", "yyyyMMdd"), docketEntity.getFilingDate());
		Assert.assertEquals(Long.valueOf(269), docketEntity.getCountyId());
		
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("UTSALTLAKE110903302", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
		Assert.assertNotNull(sourceDocketVersion);
		Assert.assertNotNull(sourceDocketVersion.getPrimaryKey().getVersionDate());
		Assert.assertEquals("n_dutsalt.110903302", sourceDocketVersion.getSourceFile().getName());
		Assert.assertEquals(Phase.SOURCE, sourceDocketVersion.getPrimaryKey().getPhase());
		Assert.assertEquals(Long.valueOf(1), sourceDocketVersion.getVersion());
		
		DocketVersion novusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt("UTSALTLAKE110903302", docketEntity.getCourt(), ProductEnum.STATE.getKey());
		Assert.assertNull(novusDocketVersion);
				
		List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("UTSALTLAKE110903302");
		Assert.assertEquals(1, docketHistoryList.size());
		DocketHistory docketHistory = docketHistoryList.get(0);
		Assert.assertNotNull(docketHistory);
		Assert.assertEquals("n_dutsalt.110903302.012313.114640", docketHistory.getRequestName());
		
		Content content = contentService.findContentByUuid(sourceDocketVersion.getContentId());
		Assert.assertNotNull(content);
				
		//check source content version
		List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(sourceDocketVersion.getContentId());
		Assert.assertEquals(1, contentVersionList.size());
		
		//cleanup
		File unzipPreDocketFile = new File(directoryTokens[0] + "STATE");
		FileUtils.deleteDirectory(unzipPreDocketFile);

	}

	@Test
	public void testProcessNYAcquisitionRecordHappyPath_Update() throws Exception
	{
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"70219216\" " +
		        "sender.id=\"Data Capture\" " +
		        "merged.file.name=\"n_dlawdct.5:2013cv00143\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
		                        "<court westlaw.cluster.name=\"n_dnydownstate\" acquisition.status=\"success\" court.type=\"State\"> "+
		                                "<acquired.dockets> "+
		                                        "<acquired.docket.status status=\"captured\"> "+
		                                                "<state.docket docket.number=\"0501289/2013\" filename=\"KINGS20130501289\" subfolder=\"\" subdivision=\"\"> "+
		                                                        "<docket.entry> "+
		"<pdf.file filename = \"N_DNYDOWNSTATE.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
		                                                        "</docket.entry> "+
		                                                "</state.docket> "+
		                                        "</acquired.docket.status> "+
		                                "</acquired.dockets> "+
		                        "</court> "+
		"</new.acquisition.record> ";
		
		URL url = AcquisitionRecordAndActivitySetServiceTest.class.getResource("/resources/n_dnykings/datacap_pre_new/KINGS20130501289.gz");
		String[] directoryTokens = url.getPath().split("n_dnykings");
		acquisitionRecordAndActivitySetService.setPreDocketRootDirectoryState(directoryTokens[0]);
		acquisitionRecordAndActivitySetService.setRootWorkDirectory(directoryTokens[0]);
		
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);
		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertNotNull(publishingRequest);
		
		Batch batch = docketService.findBatchByPrimaryKey(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batch);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batch.getPublishingRequest().getPrimaryKey().toString());
		
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(new UUID(acquisitionResponse.getPublishingRequestId()), acquisitionResponse.getBatchId());
		BatchMonitor batchMonitor = docketService.findBatchMonitorByPrimaryKey(batchMonitorKey);
		Assert.assertNotNull(batchMonitor);
				
		AcquisitionLookup acquisitionLookup = docketService.findAcquisitionLookupByRequestKey(new UUID(acquisitionResponse.getPublishingRequestId()));
		Assert.assertEquals("70219216", acquisitionLookup.getReceiptId());
		
		ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(acquisitionResponse.getBatchId());
		Assert.assertNotNull(batchDocketKeysScrollableResults);
		batchDocketKeysScrollableResults.next();
		BatchDocketKey batchDoketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);
		Assert.assertEquals(acquisitionResponse.getPublishingRequestId(), batchDoketKey.getPublishingRequestId());
		
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(batchDoketKey);
		Assert.assertNotNull(batchDocket);
		
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey("NYKINGS05012892013");
		Assert.assertNotNull(docketEntity);
		Assert.assertEquals("0501289/2013", docketEntity.getDocketNumber());
		Assert.assertEquals("0501289", docketEntity.getSequenceNumber());
		Assert.assertEquals(Long.valueOf("2013"), docketEntity.getFilingYear());
		
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("NYKINGS05012892013", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
		Assert.assertNotNull(sourceDocketVersion);
		Assert.assertNotNull(sourceDocketVersion.getPrimaryKey().getVersionDate());
		Assert.assertEquals("n_dlawdct.5:2013cv00143", sourceDocketVersion.getSourceFile().getName());
		Assert.assertEquals(Phase.SOURCE, sourceDocketVersion.getPrimaryKey().getPhase());
		Assert.assertEquals(Long.valueOf(1), sourceDocketVersion.getVersion());
		
		DocketVersion novusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt("NYKINGS05012892013", docketEntity.getCourt(), ProductEnum.STATE.getKey());
		Assert.assertNull(novusDocketVersion);
		
		List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("NYKINGS05012892013");
		Assert.assertEquals(1, docketHistoryList.size());
		DocketHistory docketHistory = docketHistoryList.get(0);
		Assert.assertNotNull(docketHistory);
		
		List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(sourceDocketVersion.getContentId());
		Assert.assertEquals(1, contentVersionList.size());
		
		acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);
		
		DocketVersion sourceDocketVersionUpdated = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("NYKINGS05012892013", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
		Assert.assertNotNull(sourceDocketVersionUpdated);
		Assert.assertEquals(Phase.SOURCE, sourceDocketVersion.getPrimaryKey().getPhase());
		Assert.assertEquals(Long.valueOf(2), sourceDocketVersionUpdated.getVersion());
		
		DocketVersion novusDocketVersionUpdated = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt("NYKINGS05012892013", docketEntity.getCourt(), ProductEnum.STATE.getKey());
		Assert.assertNull(novusDocketVersionUpdated);
				
		List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyId("NYKINGS05012892013");
		Assert.assertEquals(2, docketHistoryList2.size());
		
		List<ContentVersion> contentVersionUpdatedList = contentVersionService.findContentVersionByUuid(sourceDocketVersionUpdated.getContentId());
		Assert.assertEquals(2, contentVersionUpdatedList.size());
			
		//cleanup
		File unzipPreDocketFile = new File(directoryTokens[0] + "STATE");
		FileUtils.deleteDirectory(unzipPreDocketFile);

	}
	
	@Test
	public void testProcessActivitySetRecordHappyPath() throws Exception
	{
		String receiptId = "111111111111";
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"" + receiptId + "\" "  +
		        "sender.id=\"Data Capture\" " +
		        "merged.file.name=\"n_dlawdct.5:2013cv00143\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
		                        "<court westlaw.cluster.name=\"n_dnydownstate\" acquisition.status=\"success\" court.type=\"State\"> "+
		                                "<acquired.dockets> "+
		                                        "<acquired.docket.status status=\"captured\"> "+
		                                                "<state.docket docket.number=\"0501289/2013\" filename=\"KINGS20130501289\" subfolder=\"\" subdivision=\"\"> "+
		                                                        "<docket.entry> "+
		"<pdf.file filename = \"N_DNYDOWNSTATE.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
		                                                        "</docket.entry> "+
		                                                "</state.docket> "+
		                                        "</acquired.docket.status> "+
		                                "</acquired.dockets> "+
		                        "</court> "+
		"</new.acquisition.record> ";
		
		URL url = AcquisitionRecordAndActivitySetServiceTest.class.getResource("/resources/n_dnykings/datacap_pre_new/KINGS20130501289.gz");
		String[] directoryTokens = url.getPath().split("n_dnykings");
		acquisitionRecordAndActivitySetService.setPreDocketRootDirectoryState(directoryTokens[0]);
		acquisitionRecordAndActivitySetService.setRootWorkDirectory(directoryTokens[0]);
		
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);
		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		
		String activitySet = 
				"<activity.set receipt.id=\"" + receiptId + "\">" +
						"<activity name=\"SABER\" documents.in=\"1\" documents.out=\"1\" start.time=\"20130123082248\" end.time=\"20130123082248\"/>" +
						"<activity name=\"NOVUSLOAD\" documents.in=\"1\" documents.out=\"1\" start.time=\"20130123082248\" end.time=\"20130123082248\"/>" +
	       				"<activity name=\"DOCKETCONVERSION\" documents.in=\"1\" documents.out=\"1\" start.time=\"20130123082257\" end.time=\"20130123082257\"/>" +
						"<activity.run datatype=\"predocket-publisher-pathway\" run.date=\"20130123\" run.time=\"082248\" machine=\"ctj3057-07\" start.time=\"01/23/2013 08:22:48\" end.time=\"01/23/2013 08:22:58\" source.file=\"/ct-data/prod/dockets/source/statedocket/predocket/n_dnymanhattan/datacap_pre_new/NEWYORK20130150627.gz\" userid=\"datacap\" status=\"SUCCESS\"/>" +
	       	     "</activity.set>";
		
		acquisitionRecordAndActivitySetService.processActivitySetRecord(activitySet);
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey("NYKINGS05012892013");
		DocketVersion newNovusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct("NYKINGS05012892013", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
		Assert.assertNotNull(newNovusDocketVersion);
		
		List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("NYKINGS05012892013");
		//1 for insert and 3 for update
		Assert.assertEquals(4, docketHistoryList.size());
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("NYKINGS05012892013", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
			
		DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType("NYKINGS05012892013", DocketVersionRelTypeEnum.SOURCE_TO_NOVUS);
		Assert.assertNotNull(docketVersionRel);
		Assert.assertNotNull(docketVersionRel.getRelTimestamp());
		Assert.assertEquals(sourceDocketVersion.getPrimaryKey().getVersionDate(), docketVersionRel.getRelTimestamp());
	}
	
	@Test
	public void testProcessActivitySetRecordWithEhaHappyPath() throws Exception
	{
		String receiptId = "111111111111";
		String acquisitionRecord = 
				"<new.acquisition.record " +
		        "dcs.receipt.id=\"" + receiptId + "\" "  +
		        "sender.id=\"Data Capture\" " +
		        "merged.file.name=\"n_dlawdct.5:2013cv00143\" " +
		        "merged.file.size=\"3123\" " +
		        "script.start.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "script.end.date.time=\"Wed Jan 23 11:46:40 CST 2013\" "+
		        "docket.type=\"predocket\" "+
		        "retrieve.type=\"daily\" > "+
		                        "<court westlaw.cluster.name=\"n_dnydownstate\" acquisition.status=\"success\" court.type=\"State\"> "+
		                                "<acquired.dockets> "+
		                                        "<acquired.docket.status status=\"captured\"> "+
		                                                "<state.docket docket.number=\"0501289/2013\" filename=\"KINGS20130501289\" subfolder=\"\" subdivision=\"\"> "+
		                                                        "<docket.entry> "+
		"<pdf.file filename = \"N_DNYDOWNSTATE.5:13cv00143.N_DLAWDCT.pdf\" uuid=\"Ibf342410658411e28d0bb787f93a62ef\" /> "+
		                                                        "</docket.entry> "+
		                                                "</state.docket> "+
		                                        "</acquired.docket.status> "+
		                                "</acquired.dockets> "+
		                        "</court> "+
		"</new.acquisition.record> ";
		
		URL url = AcquisitionRecordAndActivitySetServiceTest.class.getResource("/resources/n_dnykings/datacap_pre_new/KINGS20130501289.gz");
		String[] directoryTokens = url.getPath().split("n_dnykings");
		acquisitionRecordAndActivitySetService.setPreDocketRootDirectoryState(directoryTokens[0]);
		acquisitionRecordAndActivitySetService.setRootWorkDirectory(directoryTokens[0]);
		
		AcquisitionResponse acquisitionResponse = acquisitionRecordAndActivitySetService.processAcquisitionRecord(acquisitionRecord);
		Assert.assertNotNull(acquisitionResponse);
		Assert.assertNotNull(acquisitionResponse.getPublishingRequestId());
		Assert.assertNotNull(acquisitionResponse.getBatchId());
		String activitySet = 
				"<activity.set receipt.id=\"" + receiptId + "\">" +
						"<activity name=\"SABER\" documents.in=\"1\" documents.out=\"1\" start.time=\"20130123082248\" end.time=\"20130123082248\"/>" +
						"<activity name=\"EHANOVUSLOAD\" documents.in=\"1\" documents.out=\"1\" start.time=\"20130123082248\" end.time=\"20130123082248\"/>" +
	       				"<activity name=\"DOCKETCONVERSION\" documents.in=\"1\" documents.out=\"1\" start.time=\"20130123082257\" end.time=\"20130123082257\"/>" +
						"<activity.run datatype=\"predocket-publisher-pathway\" run.date=\"20130123\" run.time=\"082248\" machine=\"ctj3057-07\" start.time=\"01/23/2013 08:22:48\" end.time=\"01/23/2013 08:22:58\" source.file=\"/ct-data/prod/dockets/source/statedocket/predocket/n_dnymanhattan/datacap_pre_new/NEWYORK20130150627.gz\" userid=\"datacap\" status=\"SUCCESS\"/>" +
	       	     "</activity.set>";
		
		acquisitionRecordAndActivitySetService.processActivitySetRecord(activitySet);
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey("NYKINGS05012892013");
		DocketVersion newNovusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct("NYKINGS05012892013", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
		Assert.assertNotNull(newNovusDocketVersion);
		
		List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId("NYKINGS05012892013");
		//1 for insert and 3 for update
		Assert.assertEquals(4, docketHistoryList.size());
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("NYKINGS05012892013", docketEntity.getCourt(), 10L, ProductEnum.STATE.getKey());
			
		DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType("NYKINGS05012892013", DocketVersionRelTypeEnum.SOURCE_TO_NOVUS);
		Assert.assertNotNull(docketVersionRel);
		Assert.assertNotNull(docketVersionRel.getRelTimestamp());
		Assert.assertEquals(sourceDocketVersion.getPrimaryKey().getVersionDate(), docketVersionRel.getRelTimestamp());
	}
}
