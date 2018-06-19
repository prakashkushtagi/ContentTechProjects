package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.Batch;

public interface BatchDao 
{
	public List<Batch> findBatchByParentBatchId(String requestId);

	int findNumberOfSubBatches(String parentBatchId);

	public void saveBatch(Batch batch);

	Batch findBatchByBatchId(String batchId);

}
