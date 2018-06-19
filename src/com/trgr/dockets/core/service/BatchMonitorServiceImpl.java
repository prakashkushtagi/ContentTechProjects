/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.BatchMonitorDao;
import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.Status;

public class BatchMonitorServiceImpl implements BatchMonitorService
{
	private BatchMonitorDao batchMonitorDao;
	
	public BatchMonitorServiceImpl() 
	{

	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveBatchMonitor(BatchMonitor batchMonitor)
	{
		batchMonitorDao.saveBatchMonitor(batchMonitor);
	}

	public BatchMonitorDao getBatchMonitorDao()
	{
		return batchMonitorDao;
	}

	public void setBatchMonitorDao(BatchMonitorDao batchMonitorDao) 
	{
		this.batchMonitorDao = batchMonitorDao;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchMonitor findBatchMonitorByBatchId(String batchId) 
	{
		return batchMonitorDao.findBatchMonitorByBatchId(batchId);
	}

	@Override
	@Transactional(readOnly = true, isolation=Isolation.READ_COMMITTED)
	public List<BatchMonitor> findBatchesByRequestId(String requestId) 
	{
		return batchMonitorDao.findBatchesByRequestId(requestId);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<BatchMonitor> findBatchesByRequestIdForUpdate(String requestId) 
	{
		return batchMonitorDao.findBatchesByRequestIdForUpdate(requestId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BatchMonitor> findBatchesByRequestIdAndStatus(String requestId, Status status)
	{
		return batchMonitorDao.findBatchesByRequestIdAndStatus(requestId, status);
	}

}
