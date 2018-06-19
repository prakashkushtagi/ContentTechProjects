package com.trgr.dockets.core.job.dao;

import java.util.List;

import org.springframework.batch.core.BatchStatus;

import com.trgr.dockets.core.entity.JobExecutionEntity;
import com.trgr.dockets.core.entity.JobParameterEntity;
import com.trgr.dockets.core.entity.JobParameterEntityKey;
import com.trgr.dockets.core.job.domain.JobFilter;
import com.trgr.dockets.core.job.domain.JobSort;
import com.trgr.dockets.core.job.domain.JobSummary;
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * Queries for fetching Spring Batch job information.
 */
public interface JobDao {

	/**
	 * Returns the list of job execution ID's that all have the same publishing request ID job parameter value.
	 * @param publishingRequestUuid the job parameter value for the publishingRequest key.
	 * @return a list of job instance ID's.
	 */
	public List<Long> findJobExecutionIdsByPublishingRequestId(UUID publishingRequestUuid);
	
	/**
	 * Returns the list of job execution ID's that all have the same batch ID job parameter value.
	 * @param batchId the job parameter value for the batch key.
	 * @return a list of job instance ID's.
	 */
	public List<Long> findJobExecutionIdsByBatchId(String batchId);
	
	/**
	 * Returns the summary object representing one job execution for display
	 * on the Job Summary page.
	 * @param jobExecutionIds primary key for the job execution we are interested in
	 * @return the matching JobExecution summary
	 */
	public List<JobSummary> findJobSummary(List<Long> jobExecutionIds);
	
	/**
	 * Get the job execution ID's from the JOB_EXECUTION table that match the specified
	 * filter criteria and sorted as specified.
	 * @return a list of job execution IDs from the JOB_EXECUTION table.
	 */
	public List<Long> findJobExecutions(JobFilter filter, JobSort sort);
	
	/**
	 * Returns the JobExecution's with the specified batch status 
	 * @param status the status to search on, null indicates running
	 * @return the list of JobExecution's
	 */
	public List<JobExecutionEntity> findJobExecutions(BatchStatus status);
	
	
	/** Find a job parameter uniquely identified by its primary key. */
	public JobParameterEntity findJobParameterByPrimaryKey(JobParameterEntityKey pk);
	
	/**
	 * Determines which jobs have terminated because the JVM thread that they were running in died.
	 * It this case the Spring Batch table status will never update.
	 * @return a list of Job execution ID's for jobs that will never finish.
	 */
	public List<Long> findTimedOutJobExecutions(String hostName);
	
}
