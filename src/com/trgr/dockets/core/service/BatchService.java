package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.entity.Batch;

/**
 * A Batch service.
 */
public interface BatchService 
{
	public List<Batch> findBatchByParentBatchId(String parentBatchId);
	public int findNumberOfSubBatches(String parentBatchId);
	public void saveBatch(Batch batch);
	Batch findBatchByBatchId(String batchId);
}
