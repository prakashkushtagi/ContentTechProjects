/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.BatchDao;
import com.trgr.dockets.core.entity.Batch;


public class BatchServiceImpl implements BatchService
{
	private BatchDao batchDao;
	
	
	public BatchServiceImpl() 
	{

	}

	@Override
	@Transactional(readOnly=true)
	public Batch findBatchByBatchId(String batchId) 
	{
		return batchDao.findBatchByBatchId(batchId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Batch> findBatchByParentBatchId(String parentBatchId) 
	{
		return batchDao.findBatchByParentBatchId(parentBatchId);
	}

	@Override
	@Transactional(readOnly = true)
	public int findNumberOfSubBatches(String parentBatchId) 
	{
		return batchDao.findNumberOfSubBatches(parentBatchId);
	}

	@Override
	@Transactional
	public void saveBatch(Batch batch) 
	{
		batchDao.saveBatch(batch);		
	}
	
	public BatchDao getBatchDao() {
		return batchDao;
	}

	public void setBatchDao(BatchDao batchDao) {
		this.batchDao = batchDao;
	}
}
