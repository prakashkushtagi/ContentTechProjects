package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.entity.NovusLoadFile;
import com.trgr.dockets.core.entity.NovusLoadFileKey;

public interface NovusLoadFileService 
{

//	NovusLoadFile findByLoadRequestIdFileName(String loadRequestId,String filename);

	NovusLoadFile findByBatchIdSubBatchIdFileName(String batchId,String subBatchId, String filename);

	void saveNovusLoadFile(NovusLoadFile novusLoadFile);

	List<NovusLoadFile> findByBatchIdCollectionIdFileName(String batchId,Long collectionId, String filename);
	
	NovusLoadFile findNovusLoadFileByPrimaryKey(NovusLoadFileKey primaryKey);

	NovusLoadFile findByRequestIdSubBatchId(String requestId, String subBatchId);

	NovusLoadFile findByBatchIdSubBatchId(String batchId, String subBatchId);

	public List<NovusLoadFile> findNovusLoadFilesByBatchIdAndRequestId(String batchId, String requestId);

}
