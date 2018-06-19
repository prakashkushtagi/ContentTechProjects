/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.NovusLoadFileDao;
import com.trgr.dockets.core.entity.NovusLoadFile;
import com.trgr.dockets.core.entity.NovusLoadFileKey;


public class NovusLoadFileServiceImpl implements NovusLoadFileService
{
	private NovusLoadFileDao novusLoadFileDao;
	
	
	public NovusLoadFileServiceImpl() 
	{

	}

	@Override
	@Transactional(readOnly=true)
	public NovusLoadFile findNovusLoadFileByPrimaryKey(NovusLoadFileKey primaryKey) 
	{
		return novusLoadFileDao.findNovusLoadFileByPrimaryKey(primaryKey);
	}
	@Override
	@Transactional(readOnly=true)
	public NovusLoadFile findByBatchIdSubBatchIdFileName(String batchId, String subBatchId, String filename) 
	{
		return novusLoadFileDao.findByBatchIdSubBatchIdFileName(batchId,  subBatchId,  filename);
	}
	@Override
	@Transactional(readOnly=true)
	public NovusLoadFile findByBatchIdSubBatchId(String batchId, String subBatchId) 
	{
		// TODO Auto-generated method stub
		return novusLoadFileDao.findByBatchIdSubBatchId(batchId,  subBatchId);
	}
	@Override
	public List<NovusLoadFile> findByBatchIdCollectionIdFileName(String batchId,Long collectionId, String filename) {
		return novusLoadFileDao.findByBatchIdCollectionIdFileName(batchId,  collectionId,  filename);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<NovusLoadFile> findNovusLoadFilesByBatchIdAndRequestId(String batchId, String requestId){
		
		return novusLoadFileDao.findNovusLoadFilesByBatchIdAndRequestId(batchId, requestId);
	}
	@Override
	public void saveNovusLoadFile(NovusLoadFile novusLoadFile) 
	{
		novusLoadFileDao.saveNovusLoadFile(novusLoadFile);		
	}
	
	public NovusLoadFileDao getNovusLoadFileDao() {
		return novusLoadFileDao;
	}

	public void setNovusLoadFileDao(NovusLoadFileDao novusLoadFileDao) {
		this.novusLoadFileDao = novusLoadFileDao;
	}

	@Override
	@Transactional(readOnly=true)
	public NovusLoadFile findByRequestIdSubBatchId(String requestId,String subBatchId) 
	{
		return novusLoadFileDao.findByRequestIdSubBatchId(requestId,  subBatchId);
	}

	
	
}
