/*
* Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
* Proprietary and Confidential information of TRGR. Disclosure, Use or
* Reproduction without the written authorization of TRGR is prohibited
*/
package com.trgr.dockets.core.job;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Required;

import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.service.DocketService;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

/**
 * The abstract base class for all Step implementations.
 */
public abstract class AbstractSbTasklet implements Tasklet {
	public static final String SEC_PROCESS_UUID = "processUuid";
	public static final String SEC_PROCESS_STATUS_KEY = "processStatus";  // Used by DocketsStepExecutionListener
	public static final String SEC_PROCESS_STATUS_MESSAGE_KEY = "processStatusMessage";  // Used by DocketsStepExecutionListener
	public static final String JOB_DIR_PARAMS = "jobDirParams";			// class JobDirParams
		
	private static final Logger log = Logger.getLogger(AbstractSbTasklet.class);
	protected DocketService docketService;

	/**
	 * Subclasses must implement this method where all business processing is performed.
	 */
	public abstract ExitStatus executeStep(StepContribution stepContribution, ChunkContext chunkContext) throws Exception;
	
	@Override
	public final RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
		StepContext stepContext = chunkContext.getStepContext();
		StepExecution stepExecution = stepContext.getStepExecution();
		JobExecution jobExecution = stepExecution.getJobExecution();
//		ExecutionContext jec = jobExecution.getExecutionContext();
		ExecutionContext sec = stepExecution.getExecutionContext();

		ExitStatus stepExitStatus = null;
		try
		{
			stepExitStatus = executeStep(stepContribution, chunkContext);
		} catch (Exception e) {
			// Fatal error, fail job on exception, job definition must handle the FAILED transition
			log.error(String.format("Job %s (%d/%d) FAILED because an exception was unexpectedly thrown from a job step.",
						jobExecution.getJobInstance().getJobName(), jobExecution.getJobId(), jobExecution.getId()), e);
			stepExitStatus = new ExitStatus(ExitStatus.FAILED.getExitCode(), e.toString());
			sec.put(AbstractSbTasklet.SEC_PROCESS_STATUS_MESSAGE_KEY, e.getMessage());
		}
		stepExecution.setExitStatus(stepExitStatus);
		return RepeatStatus.FINISHED;
	}
	
	/**
	 * Create the BatchMonitorKey from values stored in the JobParameters that were used to launch the job.
	 */
	public static BatchMonitorKey createBatchMonitorKey(JobParameters jobParameters) {
		try { 
			UUID publishingRequestUuid = new UUID(jobParameters.getString(JobParameterKey.PUBLISHING_REQUEST_ID)); 
			String batchId = jobParameters.getString(JobParameterKey.BATCH_ID);
			BatchMonitorKey batchMonitorKey = new BatchMonitorKey(publishingRequestUuid, batchId);
			return batchMonitorKey;
		} catch (UUIDException e) { throw new RuntimeException(e); }
	}

	public PublishingRequest getPublishingRequest(JobParameters jobParameters) {
		try {
			UUID pubRequestUuid = new UUID(jobParameters.getString(JobParameterKey.PUBLISHING_REQUEST_ID));
			PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(pubRequestUuid);
			return publishingRequest;
		} catch (Exception e) {
			throw new RuntimeException("Invalid UUID value " + jobParameters.getString(JobParameterKey.PUBLISHING_REQUEST_ID) , e);
		}
	}
	public Process getProcess(ExecutionContext sec) {
		UUID processPk = (UUID) sec.get(SEC_PROCESS_UUID);
		return docketService.findProcessByPrimaryKey(processPk);
	}

	protected void storeProcessStatusInStep(ExecutionContext sec, StatusEnum processStatus) {
		storeProcessStatusInStep(sec, processStatus, null);
	}
	protected void storeProcessStatusInStep(ExecutionContext sec, StatusEnum processStatus, String message) {
		sec.put(SEC_PROCESS_STATUS_KEY, processStatus);
		sec.put(SEC_PROCESS_STATUS_MESSAGE_KEY, message);
	}
	
	@Required
	public void setDocketService(DocketService service) {
		this.docketService = service;
	}
}
