package com.trgr.dockets.core.job.service;

import java.util.List;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

import com.trgr.dockets.core.domain.rest.JobStartRequest;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.job.domain.JobFilter;
import com.trgr.dockets.core.job.domain.JobSort;
import com.trgr.dockets.core.job.domain.JobSummary;
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * Service methods for accessing Spring Batch job instance and execution data from the Spring Batch table schema.
 */
public interface JobService {
	
	public JobParameters createJobParameters(JobStartRequest jobStartRequest);
	
	/**
	 * Start a job as specified using the properties of the JobRunRequest.
	 * @return the Spring Batch execution entity
	 * @throws Exception if unable to start the job within the Spring Batch engine.
	 */
	public JobExecution runJob(JobStartRequest jobStartRequest) throws Exception;
	
	/**
	 * Returns the highest status of all the SB jobs that were started by the Dispatcher to process the docket Uber-batch subbatches.
	 * This is used by the "Joiner" component to then know that all docket processing has finished within the SB engine.
	 * @param excludeJobExecution the current job execution that just finished
	 * @param uberBatchId the parent batch primary key, exists as a JobParameter.
	 */
	public BatchStatus getAggregateBatchStatus(JobExecution excludeJobExecution, UUID publishingRequestUuid);
	
	/**
	 * Returns the aggregate of the batch monitor status from the jec.
	 * 
	 * @param excludeJobExecution
	 * @param publishingRequestUuid
	 * @return
	 */
	public StatusEnum getAggregateBatchMonitorStatus(JobExecution excludeJobExecution, UUID publishingRequestUuid);
	
	public List<JobExecution> findJobExecutionsByPublishingRequestId(UUID publishingRequestUuid);
	
	public List<JobExecution> findJobExecutionsByBatchId(String batchId);
	
	/**
	 * Resume a stopped batch job. Requires that it already be in a STOPPED or FAILED status,
	 * but makes no attempt to verify this before attempting to restart it.
	 * @param jobExecutionId of the job to be resumed
	 * @return the job execution ID of the restarted job
	 * @throws Exception on restart errors, like its already completed, or it cannot be resumed because it is in an incorrect state.
	 */
	public Long restartJob(long jobExecutionId) throws Exception;
	
	/**
	 * Perform a graceful stop of a job by stopping it after its current step has finished executing.
	 * @param jobExecutionId identifies the job to be stopped.
	 * @throws Exception on any stop failure
	 */
	public void stopJob(long jobExecutionId) throws Exception;
	
	/**
	 * Find a job execution by its primary key.
	 * @param jobExecutionId primary key
	 * @return the found execution, or null if not found
	 */
	public JobExecution findJobExecution(long jobExecutionId);
	
	/**
	 * Find a list of Spring Batch job executions from a list of their primary keys.
	 * @param jobExecutionIds the primary key of the job execution we want
	 * @return the found job execution, possibly empty, never null
	 */
	public List<JobExecution> findJobExecutions(List<Long> jobExecutionIds);
	
	/**
	 * Find the Spring Batch job execution primary keys that match the provided filter criteria, and are sorted per the JobSort object constraints.
	 * @param filter the search criteria
	 * @param sort specifies the column to sort on and the sort direction, ascending or descending.
	 * @return a list of job execution id's primary keys, possibly empty, but never null
	 */
	public List<Long> findJobExecutions(JobFilter filter, JobSort sort);
	
	/**
	 * Returns all the Spring Batch job executions for a specific job instance.
	 */
	public List<JobExecution> findJobExecutions(JobInstance jobInstance);
	
	/**
	 * Find and return a Spring Batch job instance by its primary key.
	 * @param jobInstanceId the primary key of the instance
	 * @return the instance found, or null if not found
	 */
	public JobInstance findJobInstance(long jobInstanceId);
	
	/**
	 * Fetch the model for the table columns shown on the Job Summary page.
	 * @param jobExecutionIds primay key(s) for the job executions we are interested in.
	 * @return a list of JobSummary object ordered in same way as the list of jobExecutionIds provided
	 */
	public List<JobSummary> findJobSummary(List<Long> jobExecutionIds);
	
	public StepExecution findStepExecution(long jobExecutionId, long stepExecutionId);
}
