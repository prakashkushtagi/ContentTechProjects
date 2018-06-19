package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.NovusLoadFile;
import com.trgr.dockets.core.entity.NovusLoadFileKey;

public interface NovusLoadFileDao 
{

//	public NovusLoadFile findByLoadRequestIdFileName(String loadRequestId,String filename);

	public void saveNovusLoadFile(NovusLoadFile novusLoadFile);

	public NovusLoadFile findByBatchIdSubBatchIdFileName(String batchId,String subBatchId, String filename);

	public List<NovusLoadFile> findByBatchIdCollectionIdFileName(String batchId,Long collectionId, String filename);

	public NovusLoadFile findNovusLoadFileByPrimaryKey(NovusLoadFileKey primaryKey);

	public NovusLoadFile findByRequestIdSubBatchId(String requestId,String subBatchId);

	public NovusLoadFile findByBatchIdSubBatchId(String batchId, String subBatchId);

	public List<NovusLoadFile> findNovusLoadFilesByBatchIdAndRequestId(String batchId, String requestId);
	

}
