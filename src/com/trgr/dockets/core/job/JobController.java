/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.job;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trgr.dockets.core.domain.rest.JobRestartResponse;
import com.trgr.dockets.core.domain.rest.JobStartRequest;
import com.trgr.dockets.core.domain.rest.JobStartResponse;
import com.trgr.dockets.core.domain.rest.JobStopResponse;
import com.trgr.dockets.core.domain.rest.TaskExecutorStatus;
import com.trgr.dockets.core.job.service.JobService;

/**
 * The following REST service provider operations are implemented here.

 *	GET "/service/status/taskexecutor" -
 *		Returns a marshaled TaskExecutorStatus object containing the thread counts of the internal TaskExecutor.
 *
 *	POST "/service/job/start" -
 *		Start a data capture client job, request body is a marshaled JobStartRequest object.
 *
 *	GET "/service/job/stop/{executionId}" -
 *		Stop a running job by its job execution ID.
 *
 *	GET "/service/job/restart/{executionId}" -
 *		Restart a stopped or failed job execution by its job Execution ID (assuming it was restartable).
 *
 */
public abstract class JobController {

	private static final Logger log = Logger.getLogger(JobController.class);

	// Returns a marshaled TaskExecutorStatus object containing the thread counts of the internal TaskExecutor.
	protected static final String URI_REST_SB_STATUS_TASKEXECUTOR = "service/status/taskexecutor";
	
	// Restart a stopped or failed job execution (assuming it was restartable).
	protected static final String URI_REST_JOB_RESTART = "service/job/restart/{executionId}";
	
	// Start a Spring Batch job.
	protected static final String URI_REST_JOB_START = "service/job/start";
	
	// Stop a running job.
	protected static final String URI_REST_JOB_STOP = "service/job/stop/{executionId}";
	
	private static final String VIEW_REST_STATUS_TASKEXECUTOR = "taskExecutorStatusView";
	private static final String VIEW_REST_JOB_STOP = "jobStopView";
	private static final String VIEW_REST_JOB_RESTART = "jobRestartView";
	private static final String VIEW_REST_JOB_START = "jobStartView";
	
	private final ThreadPoolTaskExecutor taskExecutor;

	private final JobService jobService;

	protected JobController(final ThreadPoolTaskExecutor taskExecutor, final JobService jobService) {
		this.taskExecutor = taskExecutor;
		this.jobService = jobService;
	}

	/**
	 * Fetch the current thread counts of the ThreadPoolTaskExecutor used in the Spring Batch engine.
	 * @return a marshaled TaskExecutorStatus object as the body of the HTTP response
	 */
	@RequestMapping(value=URI_REST_SB_STATUS_TASKEXECUTOR, method = RequestMethod.GET)
	public ModelAndView getTaskExecutorStatusOperation(Model model) throws Exception {
		log.debug(">>>");
		log.debug("1");
		TaskExecutorStatus taskExecutorStatus = new TaskExecutorStatus(taskExecutor);
		log.debug("2");
		model.addAttribute(taskExecutorStatus.getClass().getName(), taskExecutorStatus);
		log.debug("3");
		
		return new ModelAndView(VIEW_REST_STATUS_TASKEXECUTOR);
		
	}
	
	/**
	 * Restart the execution of a failed or stopped job.
	 * @param executionId the execution id of the job to be restarted.
	 * @returns the HTTP response body is a marshalled JobRestartResponse object.
	 */
	protected ModelAndView restartJobOperation(final Long executionId, final Model model) {
		log.debug("executionId=" + executionId);
		Long restartedExecutionId = null;
		String mesg = null;
		boolean success = true;
		try {
			restartedExecutionId = jobService.restartJob(executionId);
			mesg = String.format("Job execution %d successfully restarted as %d", executionId, restartedExecutionId);
		} catch (Exception e) {
			success = false;
			restartedExecutionId = null;
			mesg = String.format("Failed to restart job execution %d - %s", executionId, e.getMessage());
		}
		JobRestartResponse jobRestartResponse = new JobRestartResponse(success, executionId, restartedExecutionId,  mesg);
		log.debug(jobRestartResponse);
		model.addAttribute(jobRestartResponse.getClass().getName(), jobRestartResponse);
		return new ModelAndView(VIEW_REST_JOB_RESTART);
	}

	/**
	 * Stop the execution of a running job.
	 * @param executionId the execution id of the job to be stopped
	 * @returns the HTTP response body is a marshalled JobStopResponse object.
	 */
	protected ModelAndView stopJobOperation(final Long executionId, final Model model) {
		log.debug("executionId=" + executionId);
		String mesg = null;
		boolean success = true;
		try {
			jobService.stopJob(executionId);
			mesg = String.format("Job execution %d was successfully stopped", executionId);
		} catch (Exception e) {
			success = false;
			mesg = String.format("Failed to stop job execution %d - %s", executionId, e.getMessage());
		}
		JobStopResponse jobStopResponse = new JobStopResponse(success, executionId,  mesg);
		log.debug(jobStopResponse);
		model.addAttribute(jobStopResponse.getClass().getName(), jobStopResponse);
		return new ModelAndView(VIEW_REST_JOB_STOP);
	}

	/**
	 * Create job parameters and invoke SB API method to actually start a job running.
	 * @param request encapsulates the job parameters for the job instance
	 * @return the response marshaler bean id with the start response object in request scope.
	 */
	protected ModelAndView startJobOperation(final JobStartRequest request, final Model model) {
		log.debug(request);
		JobStartResponse response = null;
		try {
			JobExecution jobExecution = jobService.runJob(request);
			if(jobExecution != null){
				response = new JobStartResponse(jobExecution.getJobInstance().getJobName(), true,
											jobExecution.getJobId(), jobExecution.getId(),
											request.getBatchId(), null);
				response.setJobStartTime(jobExecution.getCreateTime());
			}else{
				log.debug("Job has been locked and may have ran for job request " + request);
				response = new JobStartResponse(null, false, null, null, request.getBatchId(), "Job has been locked and may have ran");
			}
		} catch (Exception e) {
			log.error("Unexpected error while starting a job for request " + request, e);
			response = new JobStartResponse(null, false, null, null, request.getBatchId(), e.toString());
		}
		log.debug(response);
		model.addAttribute(response.getClass().getName(), response);
		return new ModelAndView(VIEW_REST_JOB_START);
	}
	
	private static String getUri(final boolean forDispatcher, final String base) {
		return "/Dockets" + (forDispatcher ? "Dispatcher" : "Batch") + "/mvc/" + base;
	}
	
	public static String getTaskExecutorUri(final boolean forDispatcher) {
		return getUri(forDispatcher,URI_REST_SB_STATUS_TASKEXECUTOR);
	}
	
	public static String getJobStartUri(final boolean forDispatcher) {
		return getUri(forDispatcher,URI_REST_JOB_START);
	}
	
	public static String getJobResumeUri(final boolean forDispatcher) {
		return getUri(forDispatcher,URI_REST_JOB_RESTART);
	}
}
