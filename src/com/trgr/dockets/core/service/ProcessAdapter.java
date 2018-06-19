package com.trgr.dockets.core.service;

import java.util.Date;

import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.Process;

/**
 * Methods used in recording the execution of the steps in to the PROCESS and BATCH_MONITOR tables.
 * These are used in the job steps to record the start/end of a step (PROCESS records) and at the start
 * and end of the entire job (BATCH_MONITOR records) to bookend the job execution.
 */
public interface ProcessAdapter {

	
	/**
	 * Create a record in the PROCESS table that indicates the start of execution of the specified process/step.
	 * This record is updated with the end time when the step completes.
	 * Assumes that the BatchMonitorKey returned from creating the batch records exists under its well-know key in the JEC.
	 * @param processType one of the logical step (process) names using the FRB namespace for these names/keys.
	 * @param startTime the start time of the step/process
	 */
	public Process startProcess(ProcessTypeEnum processType, BatchMonitorKey batchMonitorKey);
	public Process startProcess(ProcessTypeEnum processType, BatchMonitorKey batchMonitorKey, Date startTime);
	public Process startProcess(ProcessTypeEnum processType, BatchMonitorKey batchMonitorKey, Date startTime, String subBatchId);
	
	
	/**
	 * Update the Process state to reflect the currently
	 * executing step of the job.  Persist this updated entity back to the PROCESS table.
	 * @param sec the step execution context
	 * @param process the current process entity
	 * @param status the ending state of the step/process
	 * @param message the status message, usually an error
	 * @param jobExecutionContext for the current job execution
	 * @param endTime the end time of the step/process
	 */
	public void endProcess(Process process, StatusEnum status, String message);
	public void endProcess(Process process, StatusEnum status, String message, Date endTime);
	
}
