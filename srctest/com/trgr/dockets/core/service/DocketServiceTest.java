/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.trgr.dockets.core.dao.DocketDao;
import com.trgr.dockets.core.dao.ProcessDao;
import com.trgr.dockets.core.domain.DroppedDocketHistory;
import com.trgr.dockets.core.domain.SourceMetadata;
import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.BatchDroppedDocket;
import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.Status;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public class DocketServiceTest {

	private String acquiredDocketStatusElementTemplate;

	private DocketDao mockDocketDao;
	private ProcessDao mockProcessDao;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setUp() {
		mockDocketDao = EasyMock.createMock(DocketDao.class);
		mockProcessDao = EasyMock.createMock(ProcessDao.class);
		acquiredDocketStatusElementTemplate = "<acquired.docket.status status=\"captured\"><dct.docket docket.number=\"123456\" filename=\"123456.xml\" case.type=\"MDL\" filing.year=\"\" sequence.number=\"123456\" subfolder=\"\" filing.location=\"\" subdivision=\"\" acquisition.date=\"Fri Mar 07 09:02:00 CST 2014\"/></acquired.docket.status>";
	}

	@After
	public void cleanUp() {
		EasyMock.reset(mockDocketDao);
		EasyMock.reset(mockProcessDao);
	}

	@Test
	public void testGetAggregateBatchMonitorStatusMixedStatus() {
		UUID PUB_REQ_ID = UUIDGenerator.createUuid();
		List<BatchMonitor> batchMonitorList = new ArrayList<BatchMonitor>();
		BatchMonitor batchMonitorRunning = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdRunning"));
		batchMonitorRunning.setStatus(new Status(StatusEnum.FAILED));
		batchMonitorList.add(batchMonitorRunning);
		BatchMonitor batchMonitorSuccess = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdSuccess"));
		batchMonitorSuccess.setStatus(new Status(StatusEnum.RUNNING));
		batchMonitorList.add(batchMonitorSuccess);
		BatchMonitor batchMonitorFailed = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdFailed"));
		batchMonitorFailed.setStatus(new Status(StatusEnum.FAILED));
		batchMonitorList.add(batchMonitorFailed);

		Batch batch = new Batch();
		batch.setParentBatch(new Batch());
		EasyMock.expect(mockDocketDao.findBatchByPrimaryKey(null)).andReturn(batch).times(3);
		EasyMock.expect(mockDocketDao.findBatchMonitorByRequestId(PUB_REQ_ID)).andReturn(batchMonitorList);
		EasyMock.replay(mockDocketDao);
		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		StatusEnum aggregateStatus = service.getAggregateBatchMonitorStatus(PUB_REQ_ID);
		Assert.assertEquals(StatusEnum.RUNNING, aggregateStatus);
		EasyMock.verify(mockDocketDao);

		// Remove the FAILED state, and try again
		EasyMock.reset(mockDocketDao);
		batchMonitorList.remove(batchMonitorFailed);
		EasyMock.expect(mockDocketDao.findBatchByPrimaryKey(null)).andReturn(batch).times(2);
		EasyMock.expect(mockDocketDao.findBatchMonitorByRequestId(PUB_REQ_ID)).andReturn(batchMonitorList);
		EasyMock.replay(mockDocketDao);
		service = new DocketServiceImpl(mockDocketDao, null);
		aggregateStatus = service.getAggregateBatchMonitorStatus(PUB_REQ_ID);
		Assert.assertEquals(StatusEnum.RUNNING, aggregateStatus);
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testGetAggregateBatchMonitorStatusFailedAndCompletedWithErrors() {
		UUID PUB_REQ_ID = UUIDGenerator.createUuid();
		List<BatchMonitor> batchMonitorList = new ArrayList<BatchMonitor>();
		BatchMonitor batchMonitorRunning = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdRunning"));
		batchMonitorRunning.setStatus(new Status(StatusEnum.FAILED));
		batchMonitorList.add(batchMonitorRunning);
		BatchMonitor batchMonitorSuccess = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdSuccess"));
		batchMonitorSuccess.setStatus(new Status(StatusEnum.COMPLETED_WITH_ERRORS));
		batchMonitorList.add(batchMonitorSuccess);
		BatchMonitor batchMonitorFailed = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdFailed"));
		batchMonitorFailed.setStatus(new Status(StatusEnum.COMPLETED_WITH_ERRORS));
		batchMonitorList.add(batchMonitorFailed);

		Batch batch = new Batch();
		batch.setParentBatch(new Batch());
		EasyMock.expect(mockDocketDao.findBatchByPrimaryKey(null)).andReturn(batch).times(3);
		EasyMock.expect(mockDocketDao.findBatchMonitorByRequestId(PUB_REQ_ID)).andReturn(batchMonitorList);
		EasyMock.replay(mockDocketDao);
		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		StatusEnum aggregateStatus = service.getAggregateBatchMonitorStatus(PUB_REQ_ID);
		Assert.assertEquals(StatusEnum.COMPLETED_WITH_ERRORS, aggregateStatus);
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testGetAggregateBatchMonitorStatusAllFailed() {
		UUID PUB_REQ_ID = UUIDGenerator.createUuid();
		List<BatchMonitor> batchMonitorList = new ArrayList<BatchMonitor>();
		BatchMonitor batchMonitorRunning = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdRunning"));
		batchMonitorRunning.setStatus(new Status(StatusEnum.FAILED));
		batchMonitorList.add(batchMonitorRunning);
		BatchMonitor batchMonitorSuccess = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdSuccess"));
		batchMonitorSuccess.setStatus(new Status(StatusEnum.FAILED));
		batchMonitorList.add(batchMonitorSuccess);
		BatchMonitor batchMonitorFailed = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdFailed"));
		batchMonitorFailed.setStatus(new Status(StatusEnum.FAILED));
		batchMonitorList.add(batchMonitorFailed);

		Batch batch = new Batch();
		batch.setParentBatch(new Batch());
		EasyMock.expect(mockDocketDao.findBatchByPrimaryKey(null)).andReturn(batch).times(3);
		EasyMock.expect(mockDocketDao.findBatchMonitorByRequestId(PUB_REQ_ID)).andReturn(batchMonitorList);
		EasyMock.replay(mockDocketDao);
		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		StatusEnum aggregateStatus = service.getAggregateBatchMonitorStatus(PUB_REQ_ID);
		Assert.assertEquals(StatusEnum.FAILED, aggregateStatus);
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testGetAggregateBatchMonitorStatusAllPending() {
		UUID PUB_REQ_ID = UUIDGenerator.createUuid();
		List<BatchMonitor> batchMonitorList = new ArrayList<BatchMonitor>();
		BatchMonitor batchMonitorRunning = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdRunning"));
		batchMonitorRunning.setStatus(new Status(StatusEnum.PENDING));
		batchMonitorList.add(batchMonitorRunning);
		BatchMonitor batchMonitorSuccess = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdSuccess"));
		batchMonitorSuccess.setStatus(new Status(StatusEnum.PENDING));
		batchMonitorList.add(batchMonitorSuccess);
		BatchMonitor batchMonitorFailed = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdFailed"));
		batchMonitorFailed.setStatus(new Status(StatusEnum.PENDING));
		batchMonitorList.add(batchMonitorFailed);

		Batch batch = new Batch();
		batch.setParentBatch(new Batch());
		EasyMock.expect(mockDocketDao.findBatchByPrimaryKey(null)).andReturn(batch).times(3);
		EasyMock.expect(mockDocketDao.findBatchMonitorByRequestId(PUB_REQ_ID)).andReturn(batchMonitorList);
		EasyMock.replay(mockDocketDao);
		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		StatusEnum aggregateStatus = service.getAggregateBatchMonitorStatus(PUB_REQ_ID);
		Assert.assertEquals(StatusEnum.RUNNING, aggregateStatus);
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testGetAggregateBatchMonitorStatusAllCompletedWithErrors() {
		UUID PUB_REQ_ID = UUIDGenerator.createUuid();
		List<BatchMonitor> batchMonitorList = new ArrayList<BatchMonitor>();
		BatchMonitor batchMonitorRunning = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdRunning"));
		batchMonitorRunning.setStatus(new Status(StatusEnum.COMPLETED_WITH_ERRORS));
		batchMonitorList.add(batchMonitorRunning);
		BatchMonitor batchMonitorSuccess = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdSuccess"));
		batchMonitorSuccess.setStatus(new Status(StatusEnum.COMPLETED_WITH_ERRORS));
		batchMonitorList.add(batchMonitorSuccess);
		BatchMonitor batchMonitorFailed = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdFailed"));
		batchMonitorFailed.setStatus(new Status(StatusEnum.COMPLETED_WITH_ERRORS));
		batchMonitorList.add(batchMonitorFailed);

		Batch batch = new Batch();
		batch.setParentBatch(new Batch());
		EasyMock.expect(mockDocketDao.findBatchByPrimaryKey(null)).andReturn(batch).times(3);
		EasyMock.expect(mockDocketDao.findBatchMonitorByRequestId(PUB_REQ_ID)).andReturn(batchMonitorList);
		EasyMock.replay(mockDocketDao);
		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		StatusEnum aggregateStatus = service.getAggregateBatchMonitorStatus(PUB_REQ_ID);
		Assert.assertEquals(StatusEnum.COMPLETED_WITH_ERRORS, aggregateStatus);
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testGetAggregateBatchMonitorStatusAllCompletedSuccessfully() {
		UUID PUB_REQ_ID = UUIDGenerator.createUuid();
		List<BatchMonitor> batchMonitorList = new ArrayList<BatchMonitor>();
		BatchMonitor batchMonitorRunning = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdRunning"));
		batchMonitorRunning.setStatus(new Status(StatusEnum.COMPLETED_SUCCESSFULLY));
		batchMonitorList.add(batchMonitorRunning);
		BatchMonitor batchMonitorSuccess = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdSuccess"));
		batchMonitorSuccess.setStatus(new Status(StatusEnum.COMPLETED_SUCCESSFULLY));
		batchMonitorList.add(batchMonitorSuccess);
		BatchMonitor batchMonitorFailed = new BatchMonitor(new BatchMonitorKey(PUB_REQ_ID, "theBatchIdFailed"));
		batchMonitorFailed.setStatus(new Status(StatusEnum.COMPLETED_SUCCESSFULLY));
		batchMonitorList.add(batchMonitorFailed);

		Batch batch = new Batch();
		batch.setParentBatch(new Batch());
		EasyMock.expect(mockDocketDao.findBatchByPrimaryKey(null)).andReturn(batch).times(3);
		EasyMock.expect(mockDocketDao.findBatchMonitorByRequestId(PUB_REQ_ID)).andReturn(batchMonitorList);
		EasyMock.replay(mockDocketDao);
		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		StatusEnum aggregateStatus = service.getAggregateBatchMonitorStatus(PUB_REQ_ID);
		Assert.assertEquals(StatusEnum.COMPLETED_SUCCESSFULLY, aggregateStatus);
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testSetEndStatusWithNegativeHistory() {

		Process process = new Process();
		UUID pPrimaryKey = UUIDGenerator.createUuid();
		process.setPrimaryKey(pPrimaryKey);
		process.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		Date endDate = new Date();
		Date startDate = new Date();
		process.setEndDate(endDate);
		process.setErrorCode("-9");
		process.setErrorDescription("TEST ERROR DESCRIPTION");
		String legacy_id = "TESTLEGACYID";
		BatchDroppedDocket batchDroppedDocket = new BatchDroppedDocket();
		long bdPrimaryKey = 1234L;
		batchDroppedDocket.setPrimaryKey(bdPrimaryKey);
		batchDroppedDocket.setLegacyId(legacy_id);
		batchDroppedDocket.setErrorDescription("TEST DROPPED ERROR DESCRIPTION");
		batchDroppedDocket.setDocketNumber("TEST_DOCKET_NUM");
		batchDroppedDocket.setCourtNorm("TEST_COURT_NORM");
		batchDroppedDocket.setProcess(process);
		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(batchDroppedDocket, legacy_id);
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		droppedDocketHistoryList.add(droppedDocketHistory);
		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		DocketHistoryTypeEnum docketHistoryTypeEnum = DocketHistoryTypeEnum.PUBLISH_HISTORY;
		File mergeFile = new File("testFileDocketService");
		DocketEntity mockDocketEntity = EasyMock.createMock(DocketEntity.class);
		mockDocketDao.update(process);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(mockDocketDao.findDocketByPrimaryKey("TESTLEGACYID")).andReturn(mockDocketEntity);
		mockDocketDao.update(mockDocketEntity);
		// EasyMock.expect(mockDocketDao.save(batchDroppedDocket)).andReturn(EasyMock.anyObject()); // cannot return anyObject
		EasyMock.expect(mockDocketDao.save(EasyMock.anyObject())).andReturn(batchDroppedDocket).times(2);
		// EasyMock.expect(mockDocketDao.update(EasyMock.anyObject()));
		// EasyMock.expect(mockDocketDao.save(EasyMock.anyObject() )).andReturn(batchDroppedDocket).anyTimes();
		EasyMock.replay(mockDocketDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		service.endProcessAndNegativeHistory(process, droppedDocketHistoryList, publishingRequest, docketHistoryTypeEnum, mergeFile);

		EasyMock.verify(mockDocketDao);

		// EasyMock.expect(mockDocketDao.findBatchDroppedDocket(bdPrimaryKey)).andReturn(batchDroppedDocket);
		// EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testSetEndStatusWithNoNegativeHistory() {

		Process process = new Process();
		UUID pPrimaryKey = UUIDGenerator.createUuid();
		process.setPrimaryKey(pPrimaryKey);
		process.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		Date endDate = new Date();
		Date startDate = new Date();
		process.setEndDate(endDate);
		process.setErrorCode("-9");
		process.setErrorDescription("TEST ERROR DESCRIPTION");

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");
		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(null, null);
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		droppedDocketHistoryList.add(droppedDocketHistory);

		mockDocketDao.update(process);
		EasyMock.replay(mockDocketDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		service.endProcessAndNegativeHistory(process, droppedDocketHistoryList, publishingRequest, null, null);

		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testProcessBatchRequestWithNegativeHistory() {

		Process process = new Process();
		UUID pPrimaryKey = UUIDGenerator.createUuid();
		process.setPrimaryKey(pPrimaryKey);
		process.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		Date endDate = new Date();
		Date startDate = new Date();
		process.setEndDate(endDate);
		process.setErrorCode("-9");
		process.setErrorDescription("TEST ERROR DESCRIPTION");

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setEndDate(endDate);
		publishingRequest.setPublishingStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");

		BatchMonitor batchmonitor = new BatchMonitor();
		batchmonitor.setBatchId("BATCH_IDTEST");
		batchmonitor.setPublishingRequestId(requestId.toString());
		batchmonitor.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);

		String legacy_id = "TESTLEGACYID";
		BatchDroppedDocket batchDroppedDocket = new BatchDroppedDocket();
		long bdPrimaryKey = 1234L;
		batchDroppedDocket.setPrimaryKey(bdPrimaryKey);
		batchDroppedDocket.setLegacyId(legacy_id);
		batchDroppedDocket.setErrorDescription("TEST DROPPED ERROR DESCRIPTION");
		batchDroppedDocket.setDocketNumber("TEST_DOCKET_NUM");
		batchDroppedDocket.setCourtNorm("TEST_COURT_NORM");
		batchDroppedDocket.setProcess(process);

		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(batchDroppedDocket, legacy_id);
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		droppedDocketHistoryList.add(droppedDocketHistory);

		DocketHistoryTypeEnum docketHistoryTypeEnum = DocketHistoryTypeEnum.ACQUISITION_HISTORY;

		File mergeFile = new File("testFileDocketService");

		mockDocketDao.update(batchmonitor);
		mockDocketDao.update(publishingRequest);
		mockDocketDao.update(process);
		EasyMock.expectLastCall();
		EasyMock.expect(mockDocketDao.findDocketByPrimaryKey("TESTLEGACYID")).andReturn(EasyMock.createMock(DocketEntity.class));
		EasyMock.expect(mockDocketDao.save(EasyMock.anyObject())).andReturn(batchDroppedDocket).times(2);
		EasyMock.replay(mockDocketDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		service.endProcessBatchRequestAndNegativeHistory(process, batchmonitor, droppedDocketHistoryList, publishingRequest,
				docketHistoryTypeEnum, mergeFile);

		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testProcessBatchRequestWithMultiNegativeHistory() {

		Process process = new Process();
		UUID pPrimaryKey = UUIDGenerator.createUuid();
		process.setPrimaryKey(pPrimaryKey);
		process.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		Date endDate = new Date();
		Date startDate = new Date();
		process.setEndDate(endDate);
		process.setErrorCode("-9");
		process.setErrorDescription("TEST ERROR DESCRIPTION");

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setEndDate(endDate);
		publishingRequest.setPublishingStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");

		BatchMonitor batchmonitor = new BatchMonitor();
		batchmonitor.setBatchId("BATCH_IDTEST");
		batchmonitor.setPublishingRequestId(requestId.toString());
		batchmonitor.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);

		String legacy_id = "TESTLEGACYID";
		BatchDroppedDocket batchDroppedDocket = new BatchDroppedDocket();
		long bdPrimaryKey = 1234L;
		batchDroppedDocket.setPrimaryKey(bdPrimaryKey);
		batchDroppedDocket.setLegacyId(legacy_id);
		batchDroppedDocket.setErrorDescription("TEST DROPPED ERROR DESCRIPTION");
		batchDroppedDocket.setDocketNumber("TEST_DOCKET_NUM");
		batchDroppedDocket.setCourtNorm("TEST_COURT_NORM");
		batchDroppedDocket.setProcess(process);

		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(batchDroppedDocket, legacy_id);
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		droppedDocketHistoryList.add(droppedDocketHistory);
		long bdPrimaryKeyNew = 7890L;
		droppedDocketHistory.getBatchDroppedDocket().setPrimaryKey(bdPrimaryKeyNew);
		droppedDocketHistory.getBatchDroppedDocket().setLegacyId(legacy_id + "2");
		DroppedDocketHistory droppedDocketHistory2 = new DroppedDocketHistory(batchDroppedDocket, legacy_id + "2");
		droppedDocketHistoryList.add(droppedDocketHistory2);

		DocketHistoryTypeEnum docketHistoryTypeEnum = DocketHistoryTypeEnum.ACQUISITION_HISTORY;

		File mergeFile = new File("testFileDocketService");

		mockDocketDao.update(batchmonitor);
		mockDocketDao.update(publishingRequest);
		mockDocketDao.update(process);
		EasyMock.expectLastCall();
		EasyMock.expect(mockDocketDao.findDocketByPrimaryKey("TESTLEGACYID2")).andReturn(EasyMock.createMock(DocketEntity.class));
		EasyMock.expect(mockDocketDao.findDocketByPrimaryKey("TESTLEGACYID")).andReturn(EasyMock.createMock(DocketEntity.class));
		EasyMock.expect(mockDocketDao.save(EasyMock.anyObject())).andReturn(batchDroppedDocket).times(4);
		EasyMock.replay(mockDocketDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		
		service.endProcessBatchRequestAndNegativeHistory(process, batchmonitor, droppedDocketHistoryList, publishingRequest,
				docketHistoryTypeEnum, mergeFile);

		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindBatchDroppedDocketByBatchId() {

		Process process = new Process();
		UUID pPrimaryKey = UUIDGenerator.createUuid();
		process.setPrimaryKey(pPrimaryKey);
		process.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		Date endDate = new Date();
		Date startDate = new Date();
		process.setBatchId("BATCH_IDTEST");
		process.setEndDate(endDate);
		process.setErrorCode("-9");
		process.setErrorDescription("TEST ERROR DESCRIPTION");

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setEndDate(endDate);
		publishingRequest.setPublishingStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");

		BatchMonitor batchmonitor = new BatchMonitor();
		batchmonitor.setBatchId("BATCH_IDTEST");
		batchmonitor.setPublishingRequestId(requestId.toString());
		batchmonitor.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);

		String legacy_id = "TESTLEGACYID";
		BatchDroppedDocket batchDroppedDocket = new BatchDroppedDocket();
		long bdPrimaryKey = 1234L;
		batchDroppedDocket.setPrimaryKey(bdPrimaryKey);
		batchDroppedDocket.setLegacyId(legacy_id);
		batchDroppedDocket.setErrorDescription("TEST DROPPED ERROR DESCRIPTION");
		batchDroppedDocket.setDocketNumber("TEST_DOCKET_NUM");
		batchDroppedDocket.setCourtNorm("TEST_COURT_NORM");
		batchDroppedDocket.setProcess(process);
		List<BatchDroppedDocket> batchDroppedDocketList = new ArrayList<BatchDroppedDocket>();
		batchDroppedDocketList.add(batchDroppedDocket);

		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(batchDroppedDocket, legacy_id);
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		droppedDocketHistoryList.add(droppedDocketHistory);
		BatchDroppedDocket batchDroppedDocket2 = new BatchDroppedDocket();
		long bdPrimaryKeyNew = 7890L;
		batchDroppedDocket2.setPrimaryKey(bdPrimaryKeyNew);
		batchDroppedDocket2.setLegacyId(legacy_id + "2");
		batchDroppedDocket2.setErrorDescription("TEST DROPPED ERROR DESCRIPTION 2");
		batchDroppedDocket2.setDocketNumber("TEST_DOCKET_NUM2");
		batchDroppedDocket2.setCourtNorm("TEST_COURT_NORM2");
		batchDroppedDocket2.setProcess(process);
		DroppedDocketHistory droppedDocketHistory2 = new DroppedDocketHistory(batchDroppedDocket2, legacy_id + "2");
		droppedDocketHistoryList.add(droppedDocketHistory2);
		DocketHistoryTypeEnum docketHistoryTypeEnum = DocketHistoryTypeEnum.ACQUISITION_HISTORY;
		batchDroppedDocketList.add(batchDroppedDocket2);

		File mergeFile = new File("testFileDocketService");

		mockDocketDao.update(batchmonitor);
		mockDocketDao.update(publishingRequest);
		mockDocketDao.update(process);
		EasyMock.expectLastCall();
		EasyMock.expect(mockDocketDao.findDocketByPrimaryKey("TESTLEGACYID2")).andReturn(EasyMock.createMock(DocketEntity.class));
		EasyMock.expect(mockDocketDao.findDocketByPrimaryKey("TESTLEGACYID")).andReturn(EasyMock.createMock(DocketEntity.class));
		EasyMock.expect(mockDocketDao.save(EasyMock.anyObject())).andReturn(batchDroppedDocket).times(4);
		EasyMock.expect((List<BatchDroppedDocket>) mockDocketDao.findBatchDroppedDocketByBatchId("BATCH_IDTEST")).andReturn(
				(List<BatchDroppedDocket>) batchDroppedDocketList);
		EasyMock.replay(mockDocketDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		service.endProcessBatchRequestAndNegativeHistory(process, batchmonitor, droppedDocketHistoryList, publishingRequest,
				docketHistoryTypeEnum, mergeFile);

		List<BatchDroppedDocket> bdList = service.findBatchDroppedDocketByBatchId(process.getBatchId());
		for (BatchDroppedDocket bd : bdList) {
			System.out.println(bd.getLegacyId());
			System.out.println(bd.getPrimaryKey());
		}
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindBatchesByParentBatch() {

		Date endDate = new Date();
		Date startDate = new Date();

		PublishingRequest publishingRequest = new PublishingRequest();
		UUID requestId = UUIDGenerator.createUuid();
		publishingRequest.setPrimaryKey(requestId);
		publishingRequest.setStartDate(startDate);
		publishingRequest.setEndDate(endDate);
		publishingRequest.setPublishingStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		publishingRequest.setRequestName("TEST_REQUEST_NAME");

		Batch expectedBatch = new Batch();
		expectedBatch.setPrimaryKey("BATCH_ID");
		expectedBatch.setPublishingRequest(publishingRequest);

		List<Batch> expectedSubBatchList = new ArrayList<Batch>();
		Batch expectedSubBatch = new Batch();
		expectedSubBatch.setPrimaryKey("SUB_BATCH_ID");
		expectedSubBatch.setParentBatch(expectedBatch);
		expectedSubBatch.setPublishingRequest(publishingRequest);
		expectedSubBatchList.add(expectedSubBatch);

		Batch expectedSubBatch2 = new Batch();
		expectedSubBatch2.setPrimaryKey("SUB_BATCH_ID2");
		expectedSubBatch2.setParentBatch(expectedBatch);
		expectedSubBatch2.setPublishingRequest(publishingRequest);
		expectedSubBatchList.add(expectedSubBatch2);

		EasyMock.expect(mockDocketDao.save(EasyMock.anyObject())).andReturn(expectedBatch).anyTimes();
		EasyMock.expectLastCall();
		EasyMock.expect((List<Batch>) mockDocketDao.findBatchesByParentBatch(expectedBatch)).andReturn(
				(List<Batch>) expectedSubBatchList);
		EasyMock.replay(mockDocketDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null);

		List<Batch> bList = service.findBatchesByParentBatch(expectedBatch);
		for (Batch b : bList) {
			System.out.println(b.getPrimaryKey());
		}
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindJudicialDocketVersionWithHighestVersionForVendorAndProduct() {

		String legacyId = "legacyIdValue";
		long vendorId = 1;
		long productId = 2;
		Court court = new Court(999l);
		
		// Record expected behavior
		EasyMock.expect(mockDocketDao.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testGetDocketVersionInfoByContentUuid() {

		UUID contentUUID =  UUIDGenerator.createUuid();
		String newLegacyId = "legacyIdValue";
		Map<String, String> legacyDocket = new HashMap<String, String>();
		legacyDocket.put("DocketNumber", "DocketNumberValue");

		// Record expected behavior
		EasyMock.expect(mockDocketDao.getLegacyIdByContentUuid(contentUUID))
				.andReturn(newLegacyId);
		EasyMock.expect(mockDocketDao.getDocketNumberByLegacyId(newLegacyId))
		.andReturn(legacyDocket);
		
		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.getDocketVersionInfoByContentUuid(contentUUID);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}
	
	
	
	@Test
	public void testFindJudicialDocketVersionWithHighestVersionForVendorAndProduct_legacyIdIsNull() {
		String legacyId = null;
		long vendorId = 1;
		long productId = 2;
		Court court = new Court(999l);

		// Record expected behavior
		EasyMock.expect(mockDocketDao.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindJudicialDocketVersionWithHighestVersionForVendorAndProduct_vendorIdIsZero() {
		String legacyId = "legacyIdValue";
		long vendorId = 0;
		long productId = 2;
		Court court = new Court(999l);

		// Record expected behavior
		EasyMock.expect(mockDocketDao.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindJudicialDocketVersionWithHighestVersionForVendorAndProduct_productIdIsZero() {
		String legacyId = "legacyIdValue";
		long vendorId = 1;
		long productId = 0;
		Court court = new Court(999l);

		// Record expected behavior
		EasyMock.expect(mockDocketDao.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindSourceDocketVersionWithHighestVersionForCourtVendorAndProduct() {
		String legacyId = "legacyIdValue";
		long vendorId = 1;
		long productId = 2;
		Court court = new Court(999l);

		// Record expected behavior
		EasyMock.expect(mockDocketDao.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindSourceDocketVersionWithHighestVersionForCourtVendorAndProduct_legacyIdIsNull() {
		String legacyId = null;
		long vendorId = 1;
		long productId = 2;
		Court court = new Court(999l);

		// Record expected behavior
		EasyMock.expect(mockDocketDao.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindSourceDocketVersionWithHighestVersionForCourtVendorAndProduct_vendorIdIsZero() {
		String legacyId = "legacyIdValue";
		long vendorId = 0;
		long productId = 2;
		Court court = new Court(999l);
		// Record expected behavior
		EasyMock.expect(mockDocketDao.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testFindSourceDocketVersionWithHighestVersionForCourtVendorAndProduct_productIdIsZero() {
		String legacyId = "legacyIdValue";
		long vendorId = 1;
		long productId = 0;
		Court court = new Court(999l);

		// Record expected behavior
		EasyMock.expect(mockDocketDao.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}
	
	@Test
	public void testFindSourceDocketVersionWithHighestVersionForCourtVendorAndProduct_courtIsNull() {
		String legacyId = "legacyIdValue";
		long vendorId = 1;
		long productId = 2;
		Court court = null;

		// Record expected behavior
		EasyMock.expect(mockDocketDao.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId))
				.andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockDocketDao);

		// Run test
		DocketService service = new DocketServiceImpl(mockDocketDao, null);
		service.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId);

		// Verify behavior of mocks
		EasyMock.verify(mockDocketDao);
	}

	@Test
	public void testCreateSourceMetadata_metadataFileIsNull() {

		// Instantiate class under test
		DocketService service = new DocketServiceImpl(mockDocketDao);

		// Run test
		try {
			service.createSourceMetadata(null);
			Assert.fail("Should have thrown an Exception!!");
		} catch (Exception e) {
			// Exception expected
		}

	}

	@Test
	public void testCreateSourceMetadata_oneDocket() {

		Collection<String> docketNumbers = new ArrayList<String>();
		docketNumbers.add("373631");

		try {
			testCreateSourceMetadata(docketNumbers);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception not expected for this test.");
		}
	}

	@Test
	public void testCreateSourceMetadata_twoDockets() {

		Collection<String> docketNumbers = new ArrayList<String>();
		docketNumbers.add("373631");
		docketNumbers.add("2003348");

		try {
			testCreateSourceMetadata(docketNumbers);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception not expected for this test.");
		}
	}

	@Test
	public void testCreateSourceMetadata_threeDockets() {

		Collection<String> docketNumbers = new ArrayList<String>();
		docketNumbers.add("373631");
		docketNumbers.add("2003348");
		docketNumbers.add("123456");

		try {
			testCreateSourceMetadata(docketNumbers);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception not expected for this test.");
		}
	}

	@Test
	public void testCreateSourceMetadata_emptyDocketNumber() {

		Collection<String> docketNumbers = new ArrayList<String>();
		docketNumbers.add("");

		try {
			testCreateSourceMetadata(docketNumbers);
			Assert.fail("An IllegalArgumentException should have been thrown!!");
		} catch (IllegalArgumentException e) {
			// Expected
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("An IllegalArgumentException should have been thrown!!");
		}
	}

	@Test
	public void testCreateSourceMetadata_noDocketNumberAttribute() {
		acquiredDocketStatusElementTemplate = acquiredDocketStatusElementTemplate.replaceAll("docket.number=\"123456\" ", "");

		Collection<String> docketNumbers = new ArrayList<String>();
		docketNumbers.add("373631");

		try {
			testCreateSourceMetadata(docketNumbers);
			Assert.fail("A RuntimeException should have been thrown!!");
		} catch (RuntimeException e) {
			// Expected
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("A RuntimeException should have been thrown!!");
		}
	}

	@Test
	public void testCreateSourceMetadata_stateDocketElement() {
		acquiredDocketStatusElementTemplate = acquiredDocketStatusElementTemplate.replaceAll("dct.docket","state.docket");

		Collection<String> docketNumbers = new ArrayList<String>();
		docketNumbers.add("373631");
		
		try {
			testCreateSourceMetadata(docketNumbers);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Exception not expected for this test.");
		}
	}

	private void testCreateSourceMetadata(Collection<String> docketNumbers) throws Exception {
		// Instantiate class under test
		DocketService classUnderTest = new DocketServiceImpl(mockDocketDao);

		SourceMetadata sourceMetadata = null;

		File metadataFile = createMetadataFile(folder, docketNumbers);
		Assert.assertTrue(metadataFile.exists());
		// Run test
		sourceMetadata = classUnderTest.createSourceMetadata(metadataFile);

		Collection<String> expectedDocketNumbers = docketNumbers;
		Collection<String> actualDocketNumbers = sourceMetadata.getDocketNumbers();

		Assert.assertEquals(expectedDocketNumbers, actualDocketNumbers);
	}

	private File createMetadataFile(TemporaryFolder folder, Collection<String> docketNumbers) throws IOException {
		File metadataFile = folder.newFile("testMetadataFile.xml");
		BufferedWriter out = new BufferedWriter(new FileWriter(metadataFile));
		out.write("<new.acquisition.record dcs.receipt.id=\"74278233\" sender.id=\"Data Capture\" merged.file.name=\"N_DJPMLDCT.daily.NEW.101212.150000.merged.xml\" merged.file.size=\"26236\" "
				+ "script.start.date.time=\"Wed May 8 10:30:48 CDT 2013\" script.end.date.time=\"Wed May 8 10:44:21 CDT 2013\" retrieve.type=\"daily\">\n");
		out.write("<court westlaw.cluster.name=\"N_DCASTANIS\" acquisition.status=\"success\" court.type=\"DCT\">\n");
		out.write("<acquired.dockets>\n");

		for (String docketNumber : docketNumbers) {
			String acquiredDocketStatusElement = acquiredDocketStatusElementTemplate.replaceAll("123456", docketNumber);
			out.write(acquiredDocketStatusElement + "\n");
		}

		out.write("</acquired.dockets>\n");
		out.write("</court>\n");
		out.write("</new.acquisition.record>\n");
		out.close();
		return metadataFile;
	}

	@Test
	public void testGetpublishingRequestStatusNoPP() {

		StatusEnum aggregatedBatchStatus = StatusEnum.FAILED;
		UUID batchId = UUIDGenerator.createUuid();

		List<Process> uberBatchProcessList = new ArrayList<Process>();

		EasyMock.expect(mockProcessDao.findProcessesByBatchId(batchId.toString())).andReturn(uberBatchProcessList);
		EasyMock.replay(mockProcessDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null, mockProcessDao);
		StatusEnum publishingRequestStatusEnum = service.getPublishingRequestStatus(aggregatedBatchStatus, batchId.toString());
		Assert.assertEquals(StatusEnum.FAILED, publishingRequestStatusEnum);
		EasyMock.verify(mockProcessDao);
	}

	@Test
	public void testGetpublishingRequestStatusWithPP() {

		StatusEnum aggregatedBatchStatus = StatusEnum.COMPLETED_WITH_ERRORS;
		UUID batchId = UUIDGenerator.createUuid();

		List<Process> uberBatchProcessList = new ArrayList<Process>();
		Process process1 = new Process();
		process1.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process1.setProcessType(ProcessTypeEnum.DC);
		Process process2 = new Process();
		process2.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process2.setProcessType(ProcessTypeEnum.SCL);
		Process process3 = new Process();
		process3.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process3.setProcessType(ProcessTypeEnum.PP);
		uberBatchProcessList.add(process1);
		uberBatchProcessList.add(process2);
		uberBatchProcessList.add(process3);

		EasyMock.expect(mockProcessDao.findProcessesByBatchId(batchId.toString())).andReturn(uberBatchProcessList);
		EasyMock.replay(mockProcessDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null, mockProcessDao);
		StatusEnum publishingRequestStatusEnum = service.getPublishingRequestStatus(aggregatedBatchStatus, batchId.toString());
		Assert.assertEquals(StatusEnum.COMPLETED_WITH_ERRORS, publishingRequestStatusEnum);
		EasyMock.verify(mockProcessDao);
	}

	@Test
	public void testGetpublishingRequestStatusWithPP1() {

		StatusEnum aggregatedBatchStatus = StatusEnum.FAILED;
		UUID batchId = UUIDGenerator.createUuid();

		List<Process> uberBatchProcessList = new ArrayList<Process>();
		Process process1 = new Process();
		process1.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process1.setProcessType(ProcessTypeEnum.DC);
		Process process2 = new Process();
		process2.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process2.setProcessType(ProcessTypeEnum.SCL);
		Process process3 = new Process();
		process3.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process3.setProcessType(ProcessTypeEnum.PP);
		uberBatchProcessList.add(process1);
		uberBatchProcessList.add(process2);
		uberBatchProcessList.add(process3);

		EasyMock.expect(mockProcessDao.findProcessesByBatchId(batchId.toString())).andReturn(uberBatchProcessList);
		EasyMock.replay(mockProcessDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null, mockProcessDao);
		StatusEnum publishingRequestStatusEnum = service.getPublishingRequestStatus(aggregatedBatchStatus, batchId.toString());
		Assert.assertEquals(StatusEnum.COMPLETED_WITH_ERRORS, publishingRequestStatusEnum);
		EasyMock.verify(mockProcessDao);
	}

	@Test
	public void testGetpublishingRequestStatusWithPP2() {

		StatusEnum aggregatedBatchStatus = StatusEnum.FAILED;
		UUID batchId = UUIDGenerator.createUuid();

		List<Process> uberBatchProcessList = new ArrayList<Process>();
		Process process1 = new Process();
		process1.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		process1.setProcessType(ProcessTypeEnum.DC);
		Process process2 = new Process();
		process2.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process2.setProcessType(ProcessTypeEnum.SCL);
		Process process3 = new Process();
		process3.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		process3.setProcessType(ProcessTypeEnum.PP);
		uberBatchProcessList.add(process1);
		uberBatchProcessList.add(process2);
		uberBatchProcessList.add(process3);

		EasyMock.expect(mockProcessDao.findProcessesByBatchId(batchId.toString())).andReturn(uberBatchProcessList);
		EasyMock.replay(mockProcessDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null, mockProcessDao);
		StatusEnum publishingRequestStatusEnum = service.getPublishingRequestStatus(aggregatedBatchStatus, batchId.toString());
		Assert.assertEquals(StatusEnum.COMPLETED_WITH_ERRORS, publishingRequestStatusEnum);
		EasyMock.verify(mockProcessDao);
	}

	@Test
	public void testGetpublishingRequestStatusWithPP3() {

		StatusEnum aggregatedBatchStatus = StatusEnum.COMPLETED_SUCCESSFULLY;
		UUID batchId = UUIDGenerator.createUuid();

		List<Process> uberBatchProcessList = new ArrayList<Process>();
		Process process1 = new Process();
		process1.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		process1.setProcessType(ProcessTypeEnum.DC);
		Process process2 = new Process();
		process2.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
		process2.setProcessType(ProcessTypeEnum.SCL);
		Process process3 = new Process();
		process3.setStatus(StatusEnum.COMPLETED_WITH_ERRORS);
		process3.setProcessType(ProcessTypeEnum.PP);
		uberBatchProcessList.add(process1);
		uberBatchProcessList.add(process2);
		uberBatchProcessList.add(process3);

		EasyMock.expect(mockProcessDao.findProcessesByBatchId(batchId.toString())).andReturn(uberBatchProcessList);
		EasyMock.replay(mockProcessDao);

		DocketService service = new DocketServiceImpl(mockDocketDao, null, mockProcessDao);
		StatusEnum publishingRequestStatusEnum = service.getPublishingRequestStatus(aggregatedBatchStatus, batchId.toString());
		Assert.assertEquals(StatusEnum.COMPLETED_WITH_ERRORS, publishingRequestStatusEnum);
		EasyMock.verify(mockProcessDao);
	}

	// @Test
	public void testUUID() {
		try {
			new UUID("I6c8ee5c8cf2511e2a98ec867961a22dh");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Not a valid west UUID");
		}
	}
}
