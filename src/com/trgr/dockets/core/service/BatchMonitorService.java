package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.Status;

/**
 * A BatchMonitor service.
 */
public interface BatchMonitorService 
{
	public BatchMonitor findBatchMonitorByBatchId(String batchId);
	public void saveBatchMonitor(BatchMonitor batchMonitor);
	public List<BatchMonitor> findBatchesByRequestId(String requestId);
	public List<BatchMonitor> findBatchesByRequestIdForUpdate(String requestId);
	
	public List<BatchMonitor> findBatchesByRequestIdAndStatus(String requestId, Status status);
}
