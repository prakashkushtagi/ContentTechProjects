/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.hibernate.ScrollableResults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.domain.DroppedDocketHistory;
import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.BatchDocket;
import com.trgr.dockets.core.entity.BatchDocketKey;
import com.trgr.dockets.core.entity.BatchDroppedDocket;
import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CodeTableValues.DroppedProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.trgr.dockets.core.entity.CollectionEntity;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.CourtCollectionMapKey;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingControl;
import com.trgr.dockets.core.entity.PublishingProcessControl;
import com.trgr.dockets.core.entity.PublishingProcessControlKey;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.Status;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

/**
 * Integration tests for DAO's that the DocketService invokes.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional 
public class DocketServiceIntegrationTest {
	/**
	 * Maintain this integraton test by providing valid primary key values below.
	 * These need to be actual working values in the schema you are testing against.
	 */
	public static final String BATCH_ID = "I70BDC4C0D73C11E1881A8802DADA7C88-B0001";
	public static final String SUB_BATCH_ID = "I70BDC4C0D73C11E1881A8802DADA7C88-S0001";
	public static final String SUB_BATCH_ID2 = "I70BDC4C0D73C11E1881A8802DADA7C88-S0002";
	public static final String PROCESS_PK = "I94133750d74811e195b8efc44a0a2d73";
	public static final String PUB_REQ_PK = "I90d22c30d73f11e1af2dc5d0ca0406ae";
	public static final Long DROPPED_DOCKET_PK = 67469l;


	@Autowired
	private DocketService service;
	
	@Before
	public void setUp() {
	}
	
//Broken Test
	
//	@Test
//	public void testGetDocketContentStream() throws Exception {
//		UUID existingContentUuid = new UUID("I355feea0c8ed11e299a4ce1a23edc918");
//		InputStream rawStream = service.getDocketContentStream(existingContentUuid);
//		Assert.assertNotNull(rawStream);
//		try {
//			BufferedInputStream bis = new BufferedInputStream(rawStream);
//			InputStreamReader isr = new InputStreamReader(bis);
//			BufferedReader br = new BufferedReader(isr);
//			String line = null;
//			System.out.println("DOCKET content: " + existingContentUuid);
//			int lineCount = 0;
//			while ((line = br.readLine()) != null) {
//				lineCount++;
//				System.out.println(line);
//			}
//			Assert.assertTrue(lineCount > 0);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			rawStream.close();
//		}
//	}
	@Test
    public void testLoadCourts() {
    List<Court> courtList = new ArrayList<Court>();
          courtList = service.loadCourt();
          Assert.assertNotNull(courtList);
          if(courtList.get(0)!= null)
          {
                Assert.assertNotNull(courtList.get(0).getCourtConfig());    
          }
	}
	
	@Test
	public void testFindBatch() {
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);
		
		Batch expectedBatch = new Batch();
		expectedBatch.setPrimaryKey(BATCH_ID);
		expectedBatch.setPublishingRequest(publishingRequest);
		service.save(expectedBatch);
		
		Batch batch = service.findBatchByPrimaryKey(BATCH_ID);
		Assert.assertNotNull(batch);
		Assert.assertEquals(BATCH_ID, batch.getPrimaryKey());
		System.out.println(batch);
	}
	
//	@Test
	public void testFindBatchDocket() throws Exception {
		BatchDocketKey BATCH_DOCKET_PK = new BatchDocketKey("JPML2431", new UUID("I034ef56060f011e2852ad4bed99e6c80"));
		BatchDocket batchDocket = service.findBatchDocketByPrimaryKey(BATCH_DOCKET_PK);
		Assert.assertNotNull(batchDocket);
		Assert.assertEquals(BATCH_DOCKET_PK, batchDocket.getPrimaryKey());
		System.out.println(batchDocket);
	}
	
	@Test
	public void testfindBatchesByParentBatchList() {
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);
		
		Batch expectedBatch = new Batch();
		expectedBatch.setPrimaryKey(BATCH_ID);
		expectedBatch.setPublishingRequest(publishingRequest);
		service.save(expectedBatch);
		
		List<Batch> expectedSubBatchList = new ArrayList<Batch>();
		Batch expectedSubBatch = new Batch();
		expectedSubBatch.setPrimaryKey(SUB_BATCH_ID);
		expectedSubBatch.setParentBatch(expectedBatch);
		expectedSubBatch.setPublishingRequest(publishingRequest);
		service.save(expectedSubBatch);
		expectedSubBatchList.add(expectedSubBatch);
		
		Batch expectedSubBatch2 = new Batch();
		expectedSubBatch2.setPrimaryKey(SUB_BATCH_ID2);
		expectedSubBatch2.setParentBatch(expectedBatch);
		expectedSubBatch2.setPublishingRequest(publishingRequest);
		service.save(expectedSubBatch2);	
		expectedSubBatchList.add(expectedSubBatch2);

		List<Batch> batchList = (List<Batch>) service.findBatchesByParentBatch(expectedBatch);
		Assert.assertNotNull(batchList);
		for (Batch b : batchList)
		{
			System.out.println(b.getPrimaryKey());
			Assert.assertTrue(expectedSubBatchList.contains(b));

		}
	}
	
	
//	@Test
	public void testFindBatchDocketByContentKey() throws Exception {
		UUID BD_CONTENT_ID = new UUID("I3511e9c5ae3d11e191598982704508d1");
		BatchDocket entity = service.findBatchDocketByContentId(BD_CONTENT_ID);
		Assert.assertNotNull(entity);
		Assert.assertEquals(BD_CONTENT_ID, entity.getContentUuid());
		System.out.println(entity);
	}
	
//	@Test
	public void testFindBatchDockets() {
		ScrollableResults cursor = service.findBatchDocketKeys("I22CC5BB060ED11E29F07E199427DC6D2-B0001");
		int count = 0;
		while (cursor.next()) {
			count++;
			BatchDocketKey key = (BatchDocketKey) cursor.get(0);
			Assert.assertNotNull(key);
			System.out.println("BDK: " + key);
		}
		Assert.assertTrue(count > 0);
	}
//	@Test
	public void testFindBatchDroppedDocket() {
		BatchDroppedDocket droppedDocket = service.findBatchDroppedDocket(DROPPED_DOCKET_PK);
		Assert.assertNotNull(droppedDocket);
		Assert.assertEquals(DROPPED_DOCKET_PK, droppedDocket.getPrimaryKey());
		System.out.println(droppedDocket);
	}
	@Test
	public void testFindBatchMonitor() throws Exception {
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);
		
		Batch expectedBatch = new Batch();
		expectedBatch.setPrimaryKey(BATCH_ID);
		expectedBatch.setPublishingRequest(publishingRequest);
		service.save(expectedBatch);
		

		
		BatchMonitorKey BATCH_MONITOR_PK= new BatchMonitorKey(requestId, BATCH_ID);
		BatchMonitor expectedBatchMonitor = new BatchMonitor();
		expectedBatchMonitor.setPrimaryKey(BATCH_MONITOR_PK);
		expectedBatchMonitor.setPublishingRequestId(publishingRequest.getRequestId());
		service.save(expectedBatchMonitor);
		BatchMonitor entity = service.findBatchMonitorByPrimaryKey(BATCH_MONITOR_PK);
		System.out.println(entity);
		Assert.assertNotNull(entity);
		Assert.assertEquals(BATCH_MONITOR_PK, entity.getPrimaryKey());
		List<BatchMonitor> list = service.findBatchMonitorByRequestId(BATCH_MONITOR_PK.getPublishingRequestUuid());
		Assert.assertTrue(list.size() > 0);
	}

	@Test
	public void testFindCourtByCourtCluster() {
		String cluster = "N_DFEDJPML";
		Court entity = service.findCourtByCourtCluster(cluster);
		Assert.assertNotNull(entity);
		Assert.assertEquals(cluster, entity.getCourtCluster());
		Assert.assertEquals("JPML", entity.getCourtNorm());
	}
	
	@Test
	public void testFindCourtByCourtNorm() {
		String courtNorm = "AK-BKR";
		Court entity = service.findCourtByCourtNorm(courtNorm);
		Assert.assertNotNull(entity);
		Assert.assertEquals(courtNorm, entity.getCourtNorm());
	}
	
	@Test
	public void testFindProcess() throws Exception  {
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);
		
		Batch batch = new Batch();
		batch.setPrimaryKey(BATCH_ID);
		batch.setPublishingRequest(publishingRequest);
		service.save(batch);
		
		Process expected = new Process(UUIDGenerator.createUuid());
		expected.setAuditKey(new Long(3845l));
		expected.setBatchId(BATCH_ID);
		expected.setSubBatchId(BATCH_ID+"-FOO12345678");
		expected.setEndDate(new Date(5000));
		expected.setErrorCode("errorCode");
		expected.setErrorDescription("errorDescription");
		expected.setProcessType(ProcessTypeEnum.CREATE_BATCH);
		expected.setPublishingRequestId(requestId);
		service.save(expected);

		Process process = service.findProcessByPrimaryKey(expected.getPrimaryKey());
		Assert.assertNotNull(process);
		Assert.assertEquals(expected.getPrimaryKey().toString(), process.getPrimaryKey().toString());
		System.out.println(process);
	}
	
//	@Test
	public void testFindPublishingControlByPrimaryKey() throws Exception  {
		long pk = 5;
		PublishingControl publishingControl = service.findPublishingControlByPrimaryKey(pk);
		Assert.assertNotNull(publishingControl);
		Assert.assertEquals(pk, publishingControl.getPrimaryKey());
		System.out.println(publishingControl);
	}
//	@Test
	public void testFindPublishingProcessControlByPrimaryKey() throws Exception  {
		PublishingProcessControlKey key = new PublishingProcessControlKey(2l, 17l);
		PublishingProcessControl publishingProcessControl = service.findPublishingProcessControlByPrimaryKey(key);
		Assert.assertNotNull(publishingProcessControl);
		Assert.assertEquals(key, publishingProcessControl.getPrimaryKey());
		System.out.println(publishingProcessControl);
	}
		
	@Test
	public void testFindPublishingRequest() throws Exception {
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);
		
		PublishingRequest pubReq = service.findPublishingRequestByPrimaryKey(publishingRequest.getPrimaryKey());
		Assert.assertNotNull(pubReq);
		Assert.assertEquals(publishingRequest.getRequestId(), pubReq.getRequestId());
//		System.out.println(pubReq);
	}

	@Test
	public void testSaveBatch() throws Exception {
//		String parentId = "junit_test_" + System.currentTimeMillis();
//		Batch parentBatch = new Batch(parentId);
//		parentBatch.setAuditKey(234l);
//		parentBatch.setParentBatch(null);
		PublishingRequest pubRequest = new PublishingRequest(PUB_REQ_PK);
//		PublishingRequest pr = new PublishingRequest(PUB_REQ_PK);
//		parentBatch.setPublishingRequest(pr);
//		service.save(parentBatch);
		
		// Lookup the record just saved, and verify it
//		Batch actualParentBatch = service.findBatchByPrimaryKey(parentId);
//		Assert.assertNotNull(actualParentBatch);
//		Assert.assertEquals(parentBatch, actualParentBatch);
		
		String childId = "junit_childBatchId";
		Batch childBatch = new Batch(childId);
		childBatch.setAuditKey(9384l);
		childBatch.setPublishingRequest(pubRequest);
		service.save(childBatch);
		
		// Lookup the record just saved, and verify it
		Batch actualChildBatch = service.findBatchByPrimaryKey(childId);
		Assert.assertNotNull(actualChildBatch);
		Assert.assertEquals(childBatch, actualChildBatch);
	}
	
	@Test
	public void testSaveBatchDocket() throws UUIDException {
		BatchDocketKey pk = new BatchDocketKey("TPH1965", new UUID(PUB_REQ_PK));
		BatchDocket expected = new BatchDocket(pk, new Batch(BATCH_ID), UUIDGenerator.createUuid(), null);
		
		service.save(expected);
		
		// Lookup the record just saved, and verify it
		BatchDocket actual = service.findBatchDocketByPrimaryKey(pk);
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected,actual);
	}
	@Test
	public void testFindCollection() {
		CollectionEntity actual = service.findCollectionByPrimaryKey(2l);
		Assert.assertNotNull(actual);
	}
	@Test
	public void testSaveDocket() throws UUIDException {
		Court court = new Court(2l);
		
		DocketEntity entity = new DocketEntity("foo", "theDocketNumber", court);
		service.save(entity);
		// Lookup the record just saved, and verify it
		DocketEntity actual = service.findDocketByPrimaryKey(entity.getPrimaryKey());
		Assert.assertNotNull(actual);
		Assert.assertEquals(entity, actual);
	}
//	@Test
	public void testUpdateDocketLastDroppedReason() {
		String docketNumber = "0000000/5000";
		String legacyId = "TESTDroppedReasonDocNum";
		String errMsg = "IC - No Source DocketVersion Found";
		Long droppedReasonId = 206l;
		
		Court court = new Court(2l);
		
		PublishingRequest publishingRequest = new PublishingRequest();
		publishingRequest.setRequestName("nahnah");
		publishingRequest.setStartDate(new Date());
				
		BatchDroppedDocket badDocket = new BatchDroppedDocket(null, docketNumber,
				errMsg, null, court.getCourtNorm(), legacyId);
		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(badDocket, legacyId, DroppedProcessTypeEnum.IC, droppedReasonId);
		service.saveNegativeHistory(droppedDocketHistory, publishingRequest, DocketHistoryTypeEnum.CONVERSION_HISTORY);
		
		DocketEntity docketEntity = service.findDocketByPrimaryKey(legacyId);
		Assert.assertNotNull(docketEntity);
		Assert.assertEquals("206", docketEntity.getLastIcDroppedReason().toString());	
	}
	@Test
	public void testSaveDocketVersion() throws UUIDException {
		DocketVersion entity = new DocketVersion(new DocketVersionKey("theLegacyId", 9L, Phase.JUDICIAL, new Date())); 
		entity.setProductId(1L);
		entity.setCourt(new Court(2L));
		entity.setVersion(99L);
		service.save(entity);
		Assert.assertNotNull(entity.getPrimaryKey());
		// Lookup the record just saved, and verify it	
		DocketVersion actual = service.findLatestDocketVersionForProductCourtLegacyIdPhase("theLegacyId", entity.getCourt(), entity.getProductId(), Phase.JUDICIAL);
		Assert.assertNotNull(actual);
		Assert.assertEquals(entity, actual);
	}	
	@Test
	public void testSaveBatchDroppedDocket() throws Exception {
		BatchDroppedDocket expected = new BatchDroppedDocket(
				null, "docketNumber", "errorDescription", new Process(new UUID(PROCESS_PK)), "courtNorm", "testLegacyId");

		service.save(expected);
		
		// Lookup the record just saved, and verify it
		BatchDroppedDocket actual = service.findBatchDroppedDocket(expected.getPrimaryKey());
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getPrimaryKey());
		Assert.assertEquals(expected, actual);
	}

	// TODO: fix the parent key not found constraint violation here...
//	@Test
//	public void testSaveBatchMonitor() throws Exception {
//		UUID requestKey = new UUID("I99819310E0AF11E1BCC600219B88737E".toLowerCase());
//		String batchId = "bogusBatchKey";
//		BatchMonitorKey pk = new BatchMonitorKey(requestKey, batchId);
//		BatchMonitor expected = new BatchMonitor(pk);
//		expected.setDocsInCount(23l);
//		expected.setDocsInSize(44l);
//		expected.setDocsOutCount(55l);
//		expected.setDocsOutSize(66l);
//		expected.setErrorDirectory(new File("/the/error/dir"));
//		expected.setNovusLoadDirectory(new File("/the/novus/load/dir"));
//		expected.setProcessDirectory(new File("/the/process/dir"));
//		expected.setServerHostname("bogusHostName");
//		expected.setStatus(new Status(StatusEnum.PENDING));
//		expected.setWorkDirectory(new File("/the/work/directory"));
//		
//		service.saveBatchMonitor(expected);
//		
//		// Lookup the record just saved, and verify it
//		BatchMonitor actual = service.findBatchMonitor(pk);
//		Assert.assertNotNull(actual);
//		Assert.assertEquals(expected, actual);
//	}
	
	@Test
	public void testSaveProcess() throws Exception {
		
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);
		
		Batch batch = new Batch();
		batch.setPrimaryKey(BATCH_ID);
		batch.setPublishingRequest(publishingRequest);
		service.save(batch);
		
		Process expected = new Process(UUIDGenerator.createUuid());
		expected.setAuditKey(new Long(3845l));
		expected.setBatchId(BATCH_ID);
		expected.setSubBatchId(BATCH_ID+"-FOO12345678");
		expected.setEndDate(new Date(5000));
		expected.setErrorCode("errorCode");
		expected.setErrorDescription("errorDescription");
		expected.setProcessType(ProcessTypeEnum.CREATE_BATCH);
		expected.setPublishingRequestId(PUB_REQ_PK);
		

		service.save(expected);

		// Lookup the record just saved, and verify it
		Process actual = service.findProcessByPrimaryKey(expected.getPrimaryKey());
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testEndProcessBatchRequestAndNegativeHistory() throws Exception {
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);

		
		Batch batch = new Batch();
		batch.setPrimaryKey(BATCH_ID);
		batch.setPublishingRequest(publishingRequest);
		service.save(batch);
		
		Process expected = new Process(UUIDGenerator.createUuid());
		expected.setBatchId(BATCH_ID);
//		expected.setSubBatchId(BATCH_ID.substring(0, 28)+"-FOO");
		expected.setEndDate(new Date(5000));
		expected.setErrorCode("errorCode");
		expected.setErrorDescription("errorDescription");
		expected.setProcessType(ProcessTypeEnum.CREATE_BATCH);
		expected.setPublishingRequestId(publishingRequest.getPrimaryKey());
		expected.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		service.save(expected);		
		
		BatchDroppedDocket batchDroppedDocket = new BatchDroppedDocket();
		String legacy_id = "TESTLEGACYID";
		long bdPrimaryKey = 1234L;
		batchDroppedDocket.setPrimaryKey(bdPrimaryKey);
		batchDroppedDocket.setLegacyId(legacy_id);
		batchDroppedDocket.setErrorDescription("TEST DROPPED ERROR DESCRIPTION");
		batchDroppedDocket.setDocketNumber("TEST_DOCKET_NUM");
		batchDroppedDocket.setCourtNorm("TEST_COURT_NORM");
		batchDroppedDocket.setProcess(expected);
		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(batchDroppedDocket, legacy_id);

		DocketHistoryTypeEnum docketHistoryTypeEnum = DocketHistoryTypeEnum.PUBLISH_HISTORY;
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		droppedDocketHistoryList.add(droppedDocketHistory);

		BatchMonitor batchmonitor = new BatchMonitor();
		BatchMonitorKey bmk = new BatchMonitorKey();
		bmk.setBatchId("BATCH_IDTEST");
		bmk.setPublishingRequestId(requestId.toString());
		batchmonitor.setPrimaryKey(bmk);
		batchmonitor.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);

		service.endProcessBatchRequestAndNegativeHistory(expected, batchmonitor, droppedDocketHistoryList,
				  publishingRequest, docketHistoryTypeEnum, null);
		
		// Lookup the record just saved, and verify it
		Process actual = service.findProcessByPrimaryKey(expected.getPrimaryKey());
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected, actual);
		
		// Lookup the record just saved, and verify it
		BatchDroppedDocket actualBatchDropDocket = service.findBatchDroppedDocket(batchDroppedDocket.getPrimaryKey());
		Assert.assertNotNull(actualBatchDropDocket);
		Assert.assertEquals(batchDroppedDocket, actualBatchDropDocket);
		
	}
	
	@Test
	public void testEndProcessNegativeHistory() throws Exception {
		
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_RPX_LINKED_PRT);
		publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
		publishingRequest.setRequestOwner("DocketServiceTest");
		publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
		service.save(publishingRequest);

		
		Batch batch = new Batch();
		batch.setPrimaryKey(BATCH_ID);
		batch.setPublishingRequest(publishingRequest);
		service.save(batch);
		
		Process expected = new Process(UUIDGenerator.createUuid());
		expected.setBatchId(BATCH_ID);
//		expected.setSubBatchId(BATCH_ID.substring(0, 28)+"-FOO");
		expected.setEndDate(new Date(5000));
		expected.setErrorCode("errorCode");
		expected.setErrorDescription("errorDescription");
		expected.setProcessType(ProcessTypeEnum.CREATE_BATCH);
		expected.setPublishingRequestId(publishingRequest.getPrimaryKey());
		expected.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		service.save(expected);
	
		BatchDroppedDocket batchDroppedDocket = new BatchDroppedDocket();
		String legacy_id = "TESTLEGACYID";
		long bdPrimaryKey = 1234L;
		batchDroppedDocket.setPrimaryKey(bdPrimaryKey);
		batchDroppedDocket.setLegacyId(legacy_id);
		batchDroppedDocket.setErrorDescription("TEST DROPPED ERROR DESCRIPTION");
		batchDroppedDocket.setDocketNumber("TEST_DOCKET_NUM");
		batchDroppedDocket.setCourtNorm("TEST_COURT_NORM");
		batchDroppedDocket.setProcess(expected);
		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(batchDroppedDocket, legacy_id);
		DocketHistoryTypeEnum docketHistoryTypeEnum = DocketHistoryTypeEnum.PUBLISH_HISTORY;
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		droppedDocketHistoryList.add(droppedDocketHistory);
		List<BatchDroppedDocket> batchDroppedDocketList = new ArrayList<BatchDroppedDocket>();
		batchDroppedDocketList.add(batchDroppedDocket);

		
		BatchDroppedDocket batchDroppedDocket2 = new BatchDroppedDocket();
		long bdPrimaryKeyNew = 7890L;
		batchDroppedDocket2.setPrimaryKey(bdPrimaryKeyNew);
		batchDroppedDocket2.setLegacyId(legacy_id+"2");
		batchDroppedDocket2.setErrorDescription("TEST DROPPED ERROR DESCRIPTION 2");
		batchDroppedDocket2.setDocketNumber("TEST_DOCKET_NUM2");
		batchDroppedDocket2.setCourtNorm("TEST_COURT_NORM2");
		batchDroppedDocket2.setProcess(expected);
		DroppedDocketHistory droppedDocketHistory2 = new DroppedDocketHistory(batchDroppedDocket2, legacy_id+"2");
		droppedDocketHistoryList.add(droppedDocketHistory2);
		batchDroppedDocketList.add(batchDroppedDocket2);

		service.endProcessAndNegativeHistory(expected, droppedDocketHistoryList,
				  publishingRequest, docketHistoryTypeEnum, null);
		
		// Lookup the record just saved, and verify it
		Process actual = service.findProcessByPrimaryKey(expected.getPrimaryKey());
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected, actual);
//		BatchDroppedDocket actualBatchDropDocket1 = service.findBatchDroppedDocket(batchDroppedDocket.getPrimaryKey());
//		System.out.println(actualBatchDropDocket1.getPrimaryKey());
//
//		BatchDroppedDocket actualBatchDropDocket2 = service.findBatchDroppedDocket(batchDroppedDocket2.getPrimaryKey());
//		System.out.println(actualBatchDropDocket2.getPrimaryKey());

		// Lookup the record just saved, and verify it
		List<BatchDroppedDocket> actualBatchDropDocket = service.findBatchDroppedDocketByBatchId(BATCH_ID);
//				BatchDroppedDocket actualBatchDropDocket = service.findBatchDroppedDocket(batchDroppedDocket.getPrimaryKey());
//		for (BatchDroppedDocket bd : actualBatchDropDocket)
//		{
//			System.out.println(bd.getLegacyId());
//			System.out.println(bd.getPrimaryKey());
//		}
		Assert.assertNotNull(actualBatchDropDocket);
		Assert.assertEquals(batchDroppedDocket, actualBatchDropDocket.get(0));
		Assert.assertEquals(batchDroppedDocketList, actualBatchDropDocket);
		
	}
	
	@Test
	public void testSavePublishingRequest() {
		UUID pk = UUIDGenerator.createUuid();
		PublishingRequest expected = new PublishingRequest(pk);
		expected.setAuditKey(null);
		expected.setDeleteOverride(true);
		expected.setEndDate(new Date(0));
		expected.setRequestInitiatorType(RequestInitiatorTypeEnum.UPDATE_LINK);
		expected.setLoadRequestDate(new Date(100));
		expected.setPrismClipDateOverride(true);
		expected.setProduct(new Product(ProductEnum.FBR));
		expected.setPublishing(false);
		expected.setRequestName("gbush");
		expected.setRequestOwner("rreagan");
		expected.setRequestType(RequestTypeEnum.IC_ONLY);
		expected.setStartDate(new Date(200));
		expected.setPublishingStatus(new Status(StatusEnum.PENDING));
		expected.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_UNLINKED);
		Court court = service.findCourtByCourtNorm("AK-BKR");
		expected.setCourt(court);
		service.save(expected);
		
		// Lookup the record just saved, and verify it
		PublishingRequest actual = service.findPublishingRequestByPrimaryKey(pk);
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected,actual);
	}
	
	@Test
	public void testFindCourtCollectionMapKeyByDownstateKey(){
		List<String> mapKeyValues = new ArrayList<String>();
		mapKeyValues.add("Westchester");
		mapKeyValues.add("test");
		mapKeyValues.add("test1");
		CourtCollectionMapKey courtCollectionMapKey = service.findCourtCollectionMapKeyByKeyValues(95l, mapKeyValues);
		Assert.assertNotNull(courtCollectionMapKey);
		Assert.assertNotNull("N_DKTDEVSTATE03", courtCollectionMapKey.getCollection().getName());
	}
	
	@Test
	public void testFindCourtCollectionMapKeyByUpstateKey(){
		List<String> mapKeyValues = new ArrayList<String>();
		mapKeyValues.add("NY-SCT-UP");
		mapKeyValues.add("test");
		mapKeyValues.add("test1");
		CourtCollectionMapKey courtCollectionMapKey = service.findCourtCollectionMapKeyByKeyValues(96l, mapKeyValues);
		Assert.assertNotNull(courtCollectionMapKey);
		Assert.assertEquals("N_DKTDEVSTATE01", courtCollectionMapKey.getCollection().getName());
	}
	
	@Test
	public void testFindCourtCollectionMapKeyByJpmlKey(){
		List<String> mapKeyValues = new ArrayList<String>();
		mapKeyValues.add("JPML");
		mapKeyValues.add("test");
		mapKeyValues.add("test1");
		CourtCollectionMapKey courtCollectionMapKey = service.findCourtCollectionMapKeyByKeyValues(94l, mapKeyValues);
		Assert.assertNotNull(courtCollectionMapKey);
		Assert.assertEquals("N_DKTDEVDCT01", courtCollectionMapKey.getCollection().getName());
	}
	
	@Test
	public void testFindCourtCollectionMapKeyByNonExistingKey(){
		List<String> mapKeyValues = new ArrayList<String>();
		mapKeyValues.add("test2");
		mapKeyValues.add("test");
		mapKeyValues.add("test1");
		CourtCollectionMapKey courtCollectionMapKey = service.findCourtCollectionMapKeyByKeyValues(94l, mapKeyValues);
		Assert.assertNull(courtCollectionMapKey);
	}
	
	@Test
	public void testFindDocketsByCourtFilingYearAndSequenceNumber()
	{
		Court courtForDocket = new Court(67l);
		Product productForDocket = new Product(ProductEnum.FBR);
		DocketEntity docketEntity = new DocketEntity("legacyId1", "docketid1", courtForDocket);
		docketEntity.setSequenceNumber("docketSequenceNumber1");
		docketEntity.setFilingYear(1111l);
		docketEntity.setLocationCode("1");
		docketEntity.setCaseTypeId(1l);
		docketEntity.setPublishFlag("Y");
		docketEntity.setProduct(productForDocket);
		service.save(docketEntity);
		
		docketEntity = new DocketEntity("legacyId2", "docketid2", courtForDocket);
		docketEntity.setSequenceNumber("docketSequenceNumber1");
		docketEntity.setFilingYear(1111l);
		docketEntity.setLocationCode("2");
		docketEntity.setCaseTypeId(1l);
		docketEntity.setPublishFlag("Y");
		docketEntity.setProduct(productForDocket);
		service.save(docketEntity);
		
		docketEntity = new DocketEntity("legacyId3", "docketid3", courtForDocket);
		docketEntity.setSequenceNumber("docketSequenceNumber1");
		docketEntity.setFilingYear(1111l);
		docketEntity.setLocationCode("3");
		docketEntity.setCaseTypeId(1l);
		docketEntity.setPublishFlag("Y");
		docketEntity.setProduct(productForDocket);
		service.save(docketEntity);
		
		List<DocketEntity> docketsEntityList = service.findDocketsByCourtIdFilingYearSequenceNumber(67l, 1111l, "docketSequenceNumber1");
		
		Assert.assertTrue("3 dockets should be returned",docketsEntityList.size()==3);
		
		
	}
	

	@Test
	public void batchContainNoDeletes() throws UUIDException {
		UUID pk = UUIDGenerator.createUuid();
		PublishingRequest expected = new PublishingRequest(pk);
		expected.setAuditKey(null);
		expected.setDeleteOverride(true);
		expected.setEndDate(new Date(0));
		expected.setRequestInitiatorType(RequestInitiatorTypeEnum.UPDATE_LINK);
		expected.setLoadRequestDate(new Date(100));
		expected.setPrismClipDateOverride(true);
		expected.setPublishing(false);
		expected.setRequestName("gbush");
		expected.setRequestOwner("rreagan");
		expected.setRequestType(RequestTypeEnum.IC_ONLY);
		expected.setPublishingStatus(new Status(StatusEnum.PENDING));
		expected.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_UNLINKED);
		Court court = service.findCourtByCourtNorm("AK-BKR");
		expected.setCourt(court);
		service.save(expected);
		
		
		Batch batch = new Batch(BATCH_ID);
		service.save(batch);
		
		BatchDocketKey batchDocketKey = new BatchDocketKey("TPH1965", pk);
		BatchDocket batchDocket = new BatchDocket(batchDocketKey, batch, UUIDGenerator.createUuid(), null);
		
		service.save(batchDocket);
		
		boolean batchNoDeletes = service.isBatchContainDeletes(BATCH_ID);
		Assert.assertFalse(batchNoDeletes);
	}
	
	@Test
	public void batchContainDeletes() throws UUIDException {
		UUID pk = UUIDGenerator.createUuid();
		PublishingRequest expected = new PublishingRequest(pk);
		expected.setAuditKey(null);
		expected.setDeleteOverride(true);
		expected.setEndDate(new Date(0));
		expected.setRequestInitiatorType(RequestInitiatorTypeEnum.UPDATE_LINK);
		expected.setLoadRequestDate(new Date(100));
		expected.setPrismClipDateOverride(true);
		expected.setPublishing(false);
		expected.setRequestName("gbush");
		expected.setRequestOwner("rreagan");
		expected.setRequestType(RequestTypeEnum.IC_ONLY);
		expected.setPublishingStatus(new Status(StatusEnum.PENDING));
		expected.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_UNLINKED);
		Court court = service.findCourtByCourtNorm("AK-BKR");
		expected.setCourt(court);
		service.save(expected);
		
		
		Batch batch = new Batch(BATCH_ID);
		service.save(batch);
		
		BatchDocketKey batchDocketKey = new BatchDocketKey("TPH1965", pk);
		BatchDocket batchDocket = new BatchDocket(batchDocketKey, batch, UUIDGenerator.createUuid(), null);
		batchDocket.setOperationType("D");
		
		service.save(batchDocket);
		
		boolean batchWithDeletes = service.isBatchContainDeletes(BATCH_ID);
		Assert.assertTrue(batchWithDeletes);
	}

	@Test
	public void testFindFifo()
	{
		try 
		{
			Date startDate = new Date();
			PublishingRequest publishingRequest = new PublishingRequest();
			UUID requestId = UUIDGenerator.createUuid();
			publishingRequest.setPrimaryKey(requestId);
			publishingRequest.setStartDate(startDate);
			publishingRequest.setRequestName("TEST_REQUEST_NAME");
			publishingRequest.setWorkflowType(WorkflowTypeEnum.FEDBKRDOCKET_BIGDOCKET_UNLINKED);
			publishingRequest.setRequestType(RequestTypeEnum.IC_PB);
			publishingRequest.setRequestOwner("DocketServiceTest");
			publishingRequest.setPublishingStatus(StatusEnum.RUNNING);
			service.save(publishingRequest);
			String expectedRetVal = "false"; // because there is no preprocessor work item it should be false.

			String retVal = service.findIfFifoPreprocessorWork(publishingRequest);
			Assert.assertEquals(expectedRetVal, retVal);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	//@Test
	public void testfindCourtFifo()
	{
		Court court;
		try 
		{
			court = new Court(97l);
			String fifo = service.findIfCourtIsFifo(court);
			
			Assert.assertTrue("97 is Count Clerk and it is expected to be Fifo Y but is " + fifo, "Y".equals(fifo) );
			
			court = new Court(95l);
			fifo = service.findIfCourtIsFifo(court);
			
			Assert.assertTrue("95 is Down state and it is expected to be Fifo Y but is " + fifo, "Y".equals(fifo) );
			
			court = new Court(96l);
			fifo = service.findIfCourtIsFifo(court);
			
			Assert.assertTrue("96 is Upstate and it is expected to be Fifo Y but is " + fifo, "Y".equals(fifo) );
			
			court = new Court(76l);
			fifo = service.findIfCourtIsFifo(court);
			
			Assert.assertTrue("96 is TNBKR and it is expected to be Fifo N but is " + fifo, "N".equals(fifo) );
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}
