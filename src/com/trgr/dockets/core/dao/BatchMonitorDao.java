package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.Status;

public interface BatchMonitorDao 
{

	public void saveBatchMonitor(BatchMonitor batchMonitor);
	public BatchMonitor findBatchMonitorByBatchId(String batchId);
	public List<BatchMonitor> findBatchesByRequestId(String requestId);	
	public List<BatchMonitor> findBatchesByRequestIdForUpdate(String requestId);	
	public List<BatchMonitor> findBatchesByRequestIdAndStatus(String requestId, Status status);
}
