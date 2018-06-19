/**
 * Copyright 2013: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or Reproduction without the written 
 * authorization of TRGR is prohibited
 *
 * Project Name : DocktsBatch
 * Name         : JobServiceTest.java
 * Author       : Tom Hall (C114868)
 * Date         : Aug 9, 2012
 * Purpose      : Unit tests for JobService
 ********************************************************************************************************
 */
package com.trgr.dockets.core.job.service;

import static org.springframework.batch.core.BatchStatus.COMPLETED;
import static org.springframework.batch.core.BatchStatus.FAILED;
import static org.springframework.batch.core.BatchStatus.STARTED;
import static org.springframework.batch.core.BatchStatus.STOPPED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;

import com.trgr.dockets.core.domain.rest.JobStartRequest;
import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.job.JobParameterKey;
import com.trgr.dockets.core.job.dao.JobDao;
import com.trgr.dockets.core.job.domain.JobFilter;
import com.trgr.dockets.core.job.domain.JobSort;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public class JobServiceTest {
	private static final UUID PUB_REQ_ID = UUIDGenerator.createUuid();
	private static final Long JOB_EXECUTION_ID = 100l;
	private static final JobExecution EXPECTED_JOB_EXECUTION = new JobExecution(JOB_EXECUTION_ID);
	private JobExplorer mockJobExplorer;
	private JobDao mockJobDao;
	private JobService jobService;
	private DocketService mockDocketService;
	
	@Before
	public void setUp() {
		this.mockJobExplorer = EasyMock.createMock(JobExplorer.class);
		this.mockJobDao = EasyMock.createMock(JobDao.class);
		this.mockDocketService = EasyMock.createMock(DocketService.class);
		this.jobService = new JobServiceImpl(null, null, mockJobExplorer, null, mockJobDao, mockDocketService);
	}
	
	/**
	 * Test the case where all the jobs are in a COMPLETED state.
	 * Should return a COMPLETED aggregate status.
	 */
	@Test
	public void testGetAggregateStatusCompleted() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L, 10L };
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		for (Long executionId : jobExecutionIds) {
			JobExecution jobExecution = newCompletedJobExecution(executionId);
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus batchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(BatchStatus.COMPLETED, batchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	
	/**
	 * Test the case where all the jobs are in a COMPLETED state except one which is in STOPPED.
	 * Should return a STOPPED aggregate status.
	 */
	@Test
	public void testGetAggregateStatusCompletedStoppedExecution() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L, 10L };
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		for (Long executionId : jobExecutionIds) {
			JobExecution jobExecution =
					newJobExecution(executionId,(executionId.longValue() == 2) ? STOPPED : COMPLETED);
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus batchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(BatchStatus.STOPPED, batchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	
	@Test
	public void testGetAggregateStatusFailed() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L, 10L };
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		boolean toggle = true;
		for (Long executionId : jobExecutionIds) {
			JobExecution jobExecution = newJobExecution(executionId,(toggle) ? COMPLETED : FAILED);
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
			toggle = !toggle;
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus aggregateBatchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(BatchStatus.FAILED, aggregateBatchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	
	@Test
	public void testGetAggregateStatusFailedStoppedExecution() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L, 10L };
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		boolean toggle = true;
		for (Long executionId : jobExecutionIds) {
			JobExecution jobExecution = newJobExecution(executionId,
					(executionId.longValue() == 2) ? STOPPED : (toggle) ? COMPLETED : FAILED);
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
			toggle = !toggle;
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus aggregateBatchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(BatchStatus.STOPPED, aggregateBatchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	

	/**
	 * Test the condition where the last job among the list of jobs is STARTED, and the rest are COMPLETED.
	 * This should return a BatchStatus of STARTED.
	 */
	@Test
	public void testGetAggregateStatusStarted() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L, 10L };
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		for (Long executionId : jobExecutionIds) {
			// Pretend the last job is STARTED and the rest are COMPLETED
			JobExecution jobExecution = newJobExecution(executionId, (executionId == 10L) ? STARTED : COMPLETED);
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus batchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(BatchStatus.STARTED, batchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	
	/**
	 * Test the condition where the last job among the list of jobs is STARTED, first one is stopped and the rest are STARTED/COMPLETED.
	 * This should return a BatchStatus of STARTED.
	 */
	@Test
	public void testGetAggregateStatusStoppedExecution() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L, 10L };
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		for (Long executionId : jobExecutionIds) {
			// Pretend the last job is STARTED and the rest are COMPLETED
			// override the first one to stopped
			JobExecution jobExecution = newJobExecution(executionId,
					(executionId == 2L) ? STOPPED : (executionId == 10L) ? STARTED : COMPLETED);
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus batchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(STOPPED, batchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	
	/**
	 * Test the condition where the first job among the list of jobs is STARTED, last one is stopped and the rest are STARTED/COMPLETED.
	 * This should return a BatchStatus of STARTED.
	 */
	@Test
	public void testGetAggregateStatusStartedStoppedExecution_1() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L, 10L };
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		for (Long executionId : jobExecutionIds) {
			// Pretend the last job is STARTED and the rest are COMPLETED
			// override the first one to stopped
			JobExecution jobExecution = newJobExecution(executionId,
					(executionId == 10L) ? STOPPED : (executionId == 2L) ? STARTED : COMPLETED);
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
			if(executionId.longValue() == 10){
				jobExecution.setStatus(STOPPED);
			}
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus batchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(BatchStatus.STARTED, batchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	
	
	@Test
	public void testGetAggregateStatusUnpausedBatches() {
		Long excludedExecutionId = 4L;
		Long[] jeids = { 2L, 4L, 6L, 8L};
		List<Long> jobExecutionIds = new ArrayList<Long>();
		for (Long jeid : jeids) {
			jobExecutionIds.add(jeid);
		}
		JobExecution excludedJobExecution = null;
		EasyMock.expect(mockJobDao.findJobExecutionIdsByPublishingRequestId(PUB_REQ_ID)).andReturn(jobExecutionIds);
		for (Long executionId : jobExecutionIds) {
			JobExecution jobExecution = newJobExecution(executionId,
					(executionId % 4L != 0) ? STOPPED : COMPLETED,
					((executionId - 1) / 4L) + "");
			if (executionId == excludedExecutionId) {
				excludedJobExecution = jobExecution;
			}
		}
		
		EasyMock.replay(mockJobDao);
		EasyMock.replay(mockJobExplorer);
		
		BatchStatus batchStatus = jobService.getAggregateBatchStatus(excludedJobExecution, PUB_REQ_ID);
		Assert.assertEquals(BatchStatus.COMPLETED, batchStatus);
		
		EasyMock.verify(mockJobDao);
		EasyMock.verify(mockJobExplorer);
	}
	
	@Test
	public void testCreateJobParameters() throws Exception {
		String theBatchId = "theBatchId";
		PublishingRequest pubReq = new PublishingRequest(UUIDGenerator.createUuid().toString());
		pubReq.setRequestType(RequestTypeEnum.PB_ONLY);
		pubReq.setDeleteOverride(true);
		pubReq.setPrismClipDateOverride(true);
		Batch uberBatch = new Batch("theUberBatchId", null, pubReq);
		Batch batch = new Batch(theBatchId, uberBatch, pubReq);
		
		// Create mockJobStartRequest object
		JobStartRequest mockJobStartRequest = EasyMock.createNiceMock(JobStartRequest.class);
		
		// Record mockJobStartRequest behavior
		EasyMock.expect(mockJobStartRequest.getBatchId()).andReturn(theBatchId).times(4); // times value includes method invocation in assert statement below
		EasyMock.replay(mockJobStartRequest);
		
		// Record mockDocketService behavior
		EasyMock.expect(mockDocketService.findBatchByPrimaryKey(theBatchId)).andReturn(batch);
		EasyMock.expect(mockDocketService.findBatchCollection("theBatchId")).andReturn(null);
		EasyMock.replay(mockDocketService);

		// Invoke the method under test
		JobParameters jobParameters = jobService.createJobParameters(mockJobStartRequest);
		
		// Make sure we don't miss checking any of the defined job parameters
		Assert.assertEquals(9, jobParameters.getParameters().size());
		Assert.assertEquals(mockJobStartRequest.getBatchId(), jobParameters.getString(JobParameterKey.BATCH_ID));
		Assert.assertEquals(uberBatch.getPrimaryKey(), jobParameters.getString(JobParameterKey.UBER_BATCH_ID));
		Assert.assertEquals(pubReq.getRequestId().toString(), jobParameters.getString(JobParameterKey.PUBLISHING_REQUEST_ID));
		
		// PBUI related
		Assert.assertEquals(pubReq.getRequestType(), RequestTypeEnum.valueOf(jobParameters.getString(JobParameterKey.REQUEST_TYPE)));
		Assert.assertEquals(pubReq.isDeleteOverride(), Boolean.parseBoolean(jobParameters.getString(JobParameterKey.DELETE_OVERRIDE)));
		Assert.assertEquals(pubReq.isPrismClipDateOverride(), Boolean.parseBoolean(jobParameters.getString(JobParameterKey.PRISM_CLIP_DATE_OVERRIDE)));
		
		// Misc 
		Assert.assertNotNull(jobParameters.getString(JobParameterKey.HOST_NAME));
		Assert.assertNotNull(jobParameters.getString(JobParameterKey.TIMESTAMP));
		
		EasyMock.verify(mockDocketService);
	}

	@Test
	public void testFindJobExecutionByPrimaryKey() {
		EasyMock.expect(mockJobExplorer.getJobExecution(JOB_EXECUTION_ID)).andReturn(EXPECTED_JOB_EXECUTION);
		EasyMock.replay(mockJobExplorer);
		
		JobExecution actualJobExecution = jobService.findJobExecution(JOB_EXECUTION_ID);
		Assert.assertNotNull(actualJobExecution);
		Assert.assertEquals(EXPECTED_JOB_EXECUTION.getId(), actualJobExecution.getId());
		
		EasyMock.verify(mockJobExplorer);
	}
	
	@Test
	public void testFindJobExecutionsFiltered() {
		int SIZE = 5;
		JobFilter filter = new JobFilter();
		JobSort sort = new JobSort();
		List<Long> listOfLong = new ArrayList<Long>();
		for (int i = 0; i < SIZE; i++) {
			listOfLong.add(new Long(i));
		}
		EasyMock.expect(mockJobDao.findJobExecutions(filter,sort)).andReturn(listOfLong);
		EasyMock.replay(mockJobDao);
		
		List<Long> ids = jobService.findJobExecutions(filter, sort);
		Assert.assertNotNull(ids);
		Assert.assertEquals(SIZE, ids.size());
		
		EasyMock.verify(mockJobDao);
	}
	
	@Test
	public void testFindJobExecutionByPrimaryKeys() {
		int SIZE = 3;
		Long[] ids = new Long[SIZE];
		List<JobExecution> expectedJobExecutions = new ArrayList<JobExecution>();
		for (int i = 0; i < SIZE; i++) {
			ids[i] = new Long(i + 101L);
			expectedJobExecutions.add(new JobExecution(ids[i]));
			EasyMock.expect(mockJobExplorer.getJobExecution(ids[i])).andReturn(expectedJobExecutions.get(i));
		}
		EasyMock.replay(mockJobExplorer);
		
		List<JobExecution> actualJobExecutions = jobService.findJobExecutions(Arrays.asList(ids));
		Assert.assertNotNull(actualJobExecutions);
		Assert.assertEquals(SIZE, actualJobExecutions.size());
		for (int i = 0; i < SIZE; i++) {
			Assert.assertEquals(expectedJobExecutions.get(i).getId(), actualJobExecutions.get(i).getId());
		}
		EasyMock.verify(mockJobExplorer);
	}
	
	private JobExecution newJobExecution(final Long executionId, final BatchStatus status, final String batchId) {
		Map<String, JobParameter> map = new HashMap<String, JobParameter>();
		map.put("batchId", new JobParameter(batchId));
		final JobExecution jobExecution = new JobExecution(executionId, new JobParameters(map), null);
		jobExecution.setStatus(status);
		EasyMock.expect(mockJobExplorer.getJobExecution(executionId)).andReturn(jobExecution);
		return jobExecution;
	}
	
	private JobExecution newJobExecution(final Long executionId, final BatchStatus status) {
		return newJobExecution(executionId, status, ""+executionId);
	}
	
	private JobExecution newCompletedJobExecution(final Long executionId) {
		return newJobExecution(executionId,COMPLETED);
	}
}
