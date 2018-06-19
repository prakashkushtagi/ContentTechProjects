package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.Process;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public interface ProcessDao {
	
	public Process findProcessBatchSubBatchTypeId(String batchId, String subBatchId, long processTypeId);
	public boolean processExistsByBatchSubBatchTypeId(String batchId, String subBatchId, long processTypeId);
	public Process findProcess(UUID primaryKey);
	public void saveProcess(Process process);
	public int findNumberProcessByBatchTypeId(String batchId, long processTypeId);
	public List<Process> findProcessesByBatchTypeId(String batchId,long processTypeId);
	public int findNumberProcessesFailedByBatchId(String batchId);
	public List<Process> findProcessesByBatchId(String batchId);
	public List<Process> findProcessesStillRunning(int l);
	public Process findProcessByRequestBatchTypeId(String requestId,String batchId, long processTypeId);
	public List<Process> findProcessesByRequestId(String requestId);
	public List<Process> findProcessWithErrorDescByRequestId(String requestId);

}
