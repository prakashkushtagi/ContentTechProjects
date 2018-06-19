package com.trgr.dockets.core.service;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.Process;

/**
 * Methods used in recording the execution of the steps in to the PROCESS and BATCH_MONITOR tables.
 * These are used in the job steps to record the start/end of a step (PROCESS records) and at the start
 * and end of the entire job (BATCH_MONITOR records) to bookend the job execution.
 */
public class ProcessAdapterImpl implements ProcessAdapter {
	private static final Logger log = Logger.getLogger(ProcessAdapterImpl.class);
	
	private DocketService docketService;
	
	public ProcessAdapterImpl(DocketService service) {
		this.docketService = service;
	}
	
	/**
	 * Create a record in the PROCESS table that indicates the start of execution of the specified process/step.
	 * This record is updated with the end time when the step completes.
	 * Assumes that the BatchMonitorKey returned from creating the batch records exists under its well-know key in the JEC.
	 * @param processType one of the logical step (process) names.
	 * @param batchMonitorKey the primary key to the row in the BATCH_MONITOR table that represents this job run.
	 * @param startTime the start time of the step/process
	 */
	@Override
	public Process startProcess(ProcessTypeEnum processType, BatchMonitorKey batchMonitorKey, Date startTime)
	{
		Process process = Process.create(processType, batchMonitorKey, startTime);
		log.debug("Inserting: " + process + " under key: " +  batchMonitorKey);
		docketService.save(process);
		Assert.notNull(process.getPrimaryKey());
		return process;
	}
	/**
	 * Create a record in the PROCESS table that indicates the start of execution of the specified process/step.
	 * This record is updated with the end time when the step completes.
	 * Assumes that the BatchMonitorKey returned from creating the batch records exists under its well-know key in the JEC.
	 * @param processType one of the logical step (process) names.
	 * @param batchMonitorKey the primary key to the row in the BATCH_MONITOR table that represents this job run.
	 * @param startTime the start time of the step/process
	 * @param subBatchId the SubBatchId
	 */
	@Override
	public Process startProcess(ProcessTypeEnum processType, BatchMonitorKey batchMonitorKey, Date startTime, String subBatchId)
	{
		Process process = Process.create(processType, batchMonitorKey, startTime);
		process.setSubBatchId(subBatchId);
		log.debug("Inserting: " + process + " under key: " +  batchMonitorKey);
		docketService.save(process);
		Assert.notNull(process.getPrimaryKey());
		return process;
	}
	@Override
	public Process startProcess(ProcessTypeEnum processType, BatchMonitorKey batchMonitorKey) 
	{
		return startProcess(processType, batchMonitorKey, new Date());
	}
	
	/**
	 * Update the Process state to reflect the currently
	 * executing step of the job.  Persist this updated entity back to the PROCESS table.
	 * @param sec the step execution context
	 * @param process the current process entity
	 * @param processStatus the ending state of the step/process
	 * 
	 */
	@Override
	public void endProcess(Process process, StatusEnum processStatus, String statusMessage, Date endTime) 
	{
		process.setStatus(processStatus);
		process.setEndDate(endTime);
		process.setErrorDescription(statusMessage);
		log.debug("Updating: " + process);
		docketService.update(process);
	}
	@Override
	public void endProcess(Process process, StatusEnum processStatus, String statusMessage) 
	{
		endProcess(process, processStatus, statusMessage, new Date());
	}
}
