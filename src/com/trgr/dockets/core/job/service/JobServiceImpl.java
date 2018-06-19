/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.job.service;

import static com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum.ALL;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum.IC_TO_END;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum.IS_IC_PB_NL;
import static com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum.PB_TO_END;
import static com.trgr.dockets.core.job.JobParameterKey.BATCH_ID;
import static com.trgr.dockets.core.job.JobParameterKey.COLLECTION_ID;
import static com.trgr.dockets.core.job.JobParameterKey.COLLECTION_NAME;
import static com.trgr.dockets.core.job.JobParameterKey.COLLECTION_NUMBER;
import static com.trgr.dockets.core.job.JobParameterKey.DELETE_OVERRIDE;
import static com.trgr.dockets.core.job.JobParameterKey.DISPATCHER_WORK_ID;
import static com.trgr.dockets.core.job.JobParameterKey.HOST_NAME;
import static com.trgr.dockets.core.job.JobParameterKey.METADOC_COLLECTION_NAME;
import static com.trgr.dockets.core.job.JobParameterKey.NOVUS_LOAD_REQUEST;
import static com.trgr.dockets.core.job.JobParameterKey.PRISM_CLIP_DATE_OVERRIDE;
import static com.trgr.dockets.core.job.JobParameterKey.PUBLISHING_REQUEST_ID;
import static com.trgr.dockets.core.job.JobParameterKey.REQUEST_TYPE;
import static com.trgr.dockets.core.job.JobParameterKey.TIMESTAMP;
import static com.trgr.dockets.core.job.JobParameterKey.UBER_BATCH_ID;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.domain.rest.JobStartRequest;
import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.CollectionEntity;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.job.JobParameterKey;
import com.trgr.dockets.core.job.dao.JobDao;
import com.trgr.dockets.core.job.domain.JobFilter;
import com.trgr.dockets.core.job.domain.JobSort;
import com.trgr.dockets.core.job.domain.JobSummary;
import com.trgr.dockets.core.service.DocketService;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public class JobServiceImpl implements JobService {
	
	private static final Logger log = Logger.getLogger(JobServiceImpl.class);
	
	public static final String PREPROCESSOR_JOB_NAME = "preprocessorJob";

	private JobDao jobDao;
	private JobExplorer jobExplorer;
	private JobRegistry jobRegistry;
	private JobOperator jobOperator;
	private JobLauncher jobLauncher;
	private DocketService docketService;
	@Autowired
	private ThreadPoolTaskExecutor springBatchTaskExecutor;
	
	public JobServiceImpl(JobRegistry jobRegistry, JobOperator jobOperator,
			 			  JobExplorer jobExplorer, JobLauncher jobLauncher,
			 			  JobDao jobDao,
			 			  DocketService docketService) {
		this.jobRegistry = jobRegistry;
		this.jobOperator = jobOperator;
		this.jobExplorer = jobExplorer;
		this.jobLauncher = jobLauncher;
		this.jobDao = jobDao;
		this.docketService = docketService;
	}
	
	@Override
	public JobParameters createJobParameters(JobStartRequest jobStartRequest) {
		log.debug(jobStartRequest);
		
		Map<String,JobParameter> jobParamMap = new HashMap<String,JobParameter>();
		jobParamMap.put(DISPATCHER_WORK_ID, new JobParameter(jobStartRequest.getWorkId()));
		// The host on which job is being run
		jobParamMap.put(HOST_NAME, new JobParameter(fetchHostName()));

		// Determine various JobParameter values as a function of the batch ID
		final String batchId = jobStartRequest.getBatchId();
		if (batchId != null) {
			Batch batch = docketService.findBatchByPrimaryKey(batchId);
			PublishingRequest publishingRequest = batch.getPublishingRequest();
			addPubReqJobParams(jobParamMap,publishingRequest);
			
			jobParamMap.put(UBER_BATCH_ID, new JobParameter(batch.getParentBatch().getPrimaryKey()));
			jobParamMap.put(BATCH_ID, new JobParameter(batchId));

			// This is used to make every job instance/parameters combination unique so that every job will run even if
			// all the other job params are the same
			jobParamMap.put(TIMESTAMP, new JobParameter(new Date()));
			
			CollectionEntity collectionEntity = docketService.findBatchCollection(batchId);
			if(collectionEntity != null){
				jobParamMap.put(COLLECTION_NAME, new JobParameter(collectionEntity.getName()));
				jobParamMap.put(METADOC_COLLECTION_NAME, new JobParameter(collectionEntity.getMetadocCollectionName())); 
				jobParamMap.put(COLLECTION_ID, new JobParameter(collectionEntity.getPrimaryKey()));
				jobParamMap.put(COLLECTION_NUMBER, new JobParameter(collectionEntity.getCollectionNumber()));
			}
		}
		return new JobParameters(jobParamMap);
	}
	
	public static void addPubReqJobParams(
			final Map<String,JobParameter> jobParamMap, final PublishingRequest publishingRequest) {
		jobParamMap.put(PUBLISHING_REQUEST_ID, new JobParameter(publishingRequest.getRequestId().toString()));
		
		final RequestTypeEnum requestType = publishingRequest.getRequestType();
		
		// PBUI specific
		jobParamMap.put(REQUEST_TYPE, new JobParameter(requestType.name()));
		jobParamMap.put(DELETE_OVERRIDE, new JobParameter(Boolean.toString(publishingRequest.isDeleteOverride())));
		jobParamMap.put(PRISM_CLIP_DATE_OVERRIDE,
				new JobParameter(Boolean.toString(publishingRequest.isPrismClipDateOverride())));

		//Is request involves Novus load
		if(requestType == ALL
				|| requestType == IC_TO_END 
				|| requestType == PB_TO_END
				|| requestType == IS_IC_PB_NL) {
			jobParamMap.put(NOVUS_LOAD_REQUEST, new JobParameter(Boolean.TRUE.toString()));
		}
	}

	@Override
	public JobExecution runJob(final JobStartRequest jobStartRequest) throws Exception {
		final JobParameters jobParameters = createJobParameters(jobStartRequest);
		final String jobName =
				(jobStartRequest.getBatchId() == null) ? PREPROCESSOR_JOB_NAME : getJobName(jobParameters);
		return runJob(jobName, jobParameters);		
	}

	/**
	 * Immediately run a job as defined in the specified JobRunRequest.
	 * JobParameters for the job are loaded from a database table as keyed by the book identifier.
	 * The launch set of JobParameters also includes a set of pre-defined "well-known" parameters, things
	 * like the username of person who started the job, the host on which the job is running, and others.
	 * See the Job Parameters section of the Job Execution Details page of the dashboard web app.
	 * to see the complete list for any single JobExecution.
	 * @throws Exception on unable to find job name, or in launching the job
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED)
	private JobExecution runJob(String jobName, JobParameters jobParameters) throws Exception {
		JobExecution jobExecution = null;
		Job job = jobRegistry.getJob(jobName);
		if (job == null) {
			throw new IllegalArgumentException("Job definition <" + jobName + "> was not found in the job registry!");
		}
		
		if (log.isDebugEnabled()) {
				log.debug(String.format("Attempting to start Spring Batch job %s with job parameters %s", job.getName(), jobParameters.toString()));
				log.debug(String.format("Thread Pool Before: activeCount=%d, corePoolSize=%d, maxPoolSize=%d, poolSize=%d",
					springBatchTaskExecutor.getActiveCount(),
					springBatchTaskExecutor.getCorePoolSize(),
					springBatchTaskExecutor.getMaxPoolSize(),
					springBatchTaskExecutor.getPoolSize()));
		}
		try{
			log.info ("About to run job: "+job.getName()+" with parameters of: " + jobParameters.toString());
			jobExecution = jobLauncher.run(job, jobParameters);
			log.info ("Just ran job: "+job.getName()+" with parameters of: " + jobParameters.toString());
		}catch(Exception e){
			log.info(String.format("Error starting Spring Batch job %s with job parameters %s", job.getName(), jobParameters.toString()), e);
			/*if(e instanceof CannotSerializeTransactionException){ //happens when the same batch is picked up different instances
				log.debug(String.format("Handling CannotSerializeTransactionException for job with job parameters %s", jobParameters.toString()));
				Thread.sleep(5000); //wait 5 seconds
					//see if the work item has job attached to it. If not set the start time to null so it can be picked back
				DispatcherWorkItem dwiPersisted = docketService.findDispatcherWorkItemByBatchId(jobParamMap.get(JobParameterKey.BATCH_ID).toString());
				if(dwiPersisted != null && dwiPersisted.getJobInstanceId() == null && dwiPersisted.getJobExecutionId() == null){
					dwiPersisted.setStartTime(null);
					docketService.update(dwiPersisted);
				}
			}*/
		}
		if (log.isDebugEnabled()) {
			log.debug(String.format("Thread Pool After: activeCount=%d, corePoolSize=%d, maxPoolSize=%d, poolSize=%d",
					springBatchTaskExecutor.getActiveCount(),
					springBatchTaskExecutor.getCorePoolSize(),
					springBatchTaskExecutor.getMaxPoolSize(),
					springBatchTaskExecutor.getPoolSize()));
			if(jobExecution != null){
				log.debug(String.format("----- Successfully started job: %s (%d/%d) -----",
					job.getName(), jobExecution.getJobId(), jobExecution.getId()));
			}
		}
		return jobExecution;
	}
	
	// Remove these logs after 12/31/2014
	@Override
	@Transactional(readOnly=true)
	public BatchStatus getAggregateBatchStatus(JobExecution excludeJobExecution, UUID publishingRequestUuid) {
		BatchStatus aggregateBatchStatus = BatchStatus.COMPLETED;
		//Random delay to prevent batches from getting the job executions at the same time.
		randomDelay();
		List<JobExecution> jobExecutions = findJobExecutionsByPublishingRequestId(publishingRequestUuid);
		log.info(String.format("Job executions returned for request id "+publishingRequestUuid+" are :" + jobExecutions));

		// Remove the current job from the collection because of what appears to be a bug in SB where the
		// database persisted JobExecution status does not match the JobExecution object passed to afterJob() listener method.
		jobExecutions.remove(excludeJobExecution);  // Remove execution with the STARTED BatchStatus
		jobExecutions.add(excludeJobExecution);		// Add back the execution received by afterJob() which has the proper BatchStatus state

		// Aggregate the worst case processing state for all the associated SB jobs
		for (JobExecution jobExecution : jobExecutions) {
			log.info("Job status return for jobInstanceId :"+ jobExecution.getJobId() +" is :"+jobExecution.getStatus() +" jobExecution:"+jobExecution); 
			if (jobExecution.getStatus().isRunning()) { // STARTED | STARTING
				
				log.info("Status returned :"+BatchStatus.STARTED);
				return BatchStatus.STARTED;	// At least one job is still running 
				
			} else if (jobExecution.getStatus().equals(BatchStatus.STOPPING) || jobExecution.getStatus().equals(BatchStatus.STOPPED)) { // STARTED | STARTING
				log.info("Status returned :"+BatchStatus.STOPPED);
				return BatchStatus.STOPPED;	// At least one job is stopped
				
			} else {
				
				log.info("aggregateBatchStatus :"+aggregateBatchStatus+" jobExecution status :"+jobExecution.getStatus() +" and Max of both is :"+BatchStatus.max(aggregateBatchStatus, jobExecution.getStatus()));
				aggregateBatchStatus = BatchStatus.max(aggregateBatchStatus, jobExecution.getStatus());
				
			}
		}
		return aggregateBatchStatus;
	}
	
	/*
	 *	Sleeping for a random amount of time to prevent batches from finishing at the same time.
	 *  Batches that finish within the same amount of time as each other may cause the next step
	 *  to not get kicked off, due to the database call latency, which results in multiple batches
	 *  receiving information that others are still running.
	 */
	private void randomDelay() {
		Random gen = new Random();
		int rnd = gen.nextInt(10000);
		log.debug("Sleeping for " + rnd + " milliseconds.");
		try {
			Thread.sleep(rnd);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}		
	}

	/* (non-Javadoc)
	 * @see com.thomsonreuters.uscl.dockets.batch.service.JobService#getAggregateBatchMonitorStatus(org.springframework.batch.core.JobExecution, com.westgroup.publishingservices.uuidgenerator.UUID)
	 */
	@Override
	@Transactional(readOnly=true)
	public StatusEnum getAggregateBatchMonitorStatus(JobExecution excludeJobExecution, UUID publishingRequestUuid){
		int failedBatchCount = 0;
		StatusEnum requestStatusEnum = StatusEnum.FAILED;
		List<JobExecution> jobExecutions = findJobExecutionsByPublishingRequestId(publishingRequestUuid);
		// Remove the current job from the collection because of what appears to be a bug in SB where the
		// database persisted JobExecution status does not match the JobExecution object passed to afterJob() listener method.
		jobExecutions.remove(excludeJobExecution);  // Remove execution with the STARTED BatchStatus
		jobExecutions.add(excludeJobExecution);		// Add back the execution received by afterJob() which has the proper BatchStatus state
		
		//Get number of batches
		JobParameters jobParameters = excludeJobExecution.getJobParameters();
		Batch parentBatch = new Batch(jobParameters.getString(JobParameterKey.UBER_BATCH_ID));
		List<Batch> batches = docketService.findBatchesByParentBatch(parentBatch);
		for(JobExecution jobExecution : jobExecutions){
			if(!(jobExecution.getStatus().equals(BatchStatus.STOPPED) || jobExecution.getStatus().isRunning())){
				StatusEnum batchStatusEnum = (StatusEnum) jobExecution.getExecutionContext().get("batchMonitorStatus");
				requestStatusEnum = getRequestLevelStatusFromBatchMonitorStatus(requestStatusEnum, batchStatusEnum);
    			if(requestStatusEnum == StatusEnum.FAILED){
    				failedBatchCount++;
    				requestStatusEnum = StatusEnum.COMPLETED_WITH_ERRORS;
    			}
			}
		}
		if(failedBatchCount == batches.size()){
			requestStatusEnum = StatusEnum.FAILED;
		}
		return requestStatusEnum;
	}
	
	private static StatusEnum getRequestLevelStatusFromBatchMonitorStatus(StatusEnum status1, StatusEnum status2){
		if (status2 == StatusEnum.FAILED) {
			return StatusEnum.FAILED;
		}
		if (status1 == StatusEnum.COMPLETED_WITH_ERRORS || status2 == StatusEnum.COMPLETED_WITH_ERRORS) {
			return StatusEnum.COMPLETED_WITH_ERRORS;
		}
		if (status1 == StatusEnum.COMPLETED_SUCCESSFULLY || status2 == StatusEnum.COMPLETED_SUCCESSFULLY) {
			return StatusEnum.COMPLETED_SUCCESSFULLY;
		}
		return StatusEnum.FAILED;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<JobExecution> findJobExecutionsByPublishingRequestId(UUID publishingRequestUuid) {
		List<Long> jobExecutionIds = jobDao.findJobExecutionIdsByPublishingRequestId(publishingRequestUuid);
		log.info("job execution Ids return by jobDao for given requestId: "+publishingRequestUuid +" are :"+jobExecutionIds);
		return getJobExecutionList(jobExecutionIds);
	}
	
	/* (non-Javadoc)
	 * @see com.thomsonreuters.uscl.dockets.batch.service.JobService#findJobExecutionsByBatchId(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<JobExecution> findJobExecutionsByBatchId(String batchId){
		List<Long> jobExecutionIds = jobDao.findJobExecutionIdsByBatchId(batchId);
		log.info("job execution Ids return by jobDao for given batchId: "+batchId +" are :"+jobExecutionIds);
		return getJobExecutionList(jobExecutionIds);
	}
	
	/**
	 * Return one job execution per job instance
	 * Checks the batch id and gets the newest execution for the batch
	 * @param jobExecutionIds
	 * @return
	 */
	private List<JobExecution> getJobExecutionList(List<Long> jobExecutionIds){
		Map<String, JobExecution> batchJobs = new HashMap<String, JobExecution>();
		for (Long jobExecutionId : jobExecutionIds) {
			final JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);
			log.info("jobExecution returned by jobExplorer :" + jobExecution);
			if(jobExecution != null){
				String batchId = jobExecution.getJobParameters().getString("batchId");
				JobExecution otherJob = batchJobs.get(batchId);
				if (otherJob != null) {
					if (jobExecution.getId() > otherJob.getId()) {
						batchJobs.put(batchId, jobExecution);
					}
				} else {
					batchJobs.put(batchId, jobExecution);
				}
			}
		}
		List<JobExecution> jobExecutionList = new ArrayList<JobExecution>();
		jobExecutionList.addAll(batchJobs.values());
		log.info("Returned jobExecutionList: "+jobExecutionList);
		return jobExecutionList;
	}
	
	/**
	 * Resume a stopped job.  Required that it already be in a STOPPED or FAILED status, but makes no attempt to 
	 * verify this before attempting to restart it.
	 * @param jobExecutionId of the job to be resumed
	 * @return the job execution ID of the restarted job
	 * @throws Exception on restart errors
	 */
	@Override
	public Long restartJob(long jobExecutionId) throws Exception {
		Long restartedJobExecutionId = jobOperator.restart(jobExecutionId);
		return restartedJobExecutionId;
	}
	
	@Override
	public void stopJob(long jobExecutionId) throws Exception {
		jobOperator.stop(jobExecutionId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<JobSummary> findJobSummary(List<Long> jobExecutionIds) {
		return jobDao.findJobSummary(jobExecutionIds);
	}
	
	@Override
	@Transactional(readOnly = true)
	public JobExecution findJobExecution(long jobExecutionId) {
		return jobExplorer.getJobExecution(jobExecutionId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<JobExecution> findJobExecutions(List<Long> jobExecutionIds) {
		List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
		for (Long jobExecutionId : jobExecutionIds) {
			JobExecution jobExecution = jobExplorer.getJobExecution(jobExecutionId);
			if (jobExecution != null) {
				jobExecutions.add(jobExecution);
			}
		}
		return jobExecutions;
	}
	
	@Override
	public List<JobExecution> findJobExecutions(JobInstance jobInstance) {
		List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
		return jobExecutions;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Long> findJobExecutions(JobFilter filter, JobSort sort) {
		return jobDao.findJobExecutions(filter, sort);
	}
	
	@Override
	public JobInstance findJobInstance(long jobInstanceId) {
		return jobExplorer.getJobInstance(jobInstanceId);
	}
	
	@Override
	public StepExecution findStepExecution(long jobExecutionId, long stepExecutionId) {
		StepExecution stepExecution = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
		return stepExecution;
	}
	
	/**
	 * Return the name of the Spring Batch job that is to be run.
	 * @param jobParameters used to determine the job name
	 * @return the id of the SB job to run.
	 */
	private static String getJobName(JobParameters jobParameters) {
		return "docketsJob";
	}
	
	private static String fetchHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException uhe) {
			return null;
		}
	}
}
