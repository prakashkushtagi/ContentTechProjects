/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.ProcessDao;
import com.trgr.dockets.core.entity.Process;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public class ProcessServiceImpl implements ProcessService
{
	private ProcessDao processDao;
	
	public ProcessServiceImpl() 
	{

	}
	@Override
	@Transactional(readOnly=true)
	public Process findProcessByBatchSubBatchTypeId(String batchId, String subBatchId, long processTypeId) 
	{
		return processDao.findProcessBatchSubBatchTypeId(batchId,subBatchId,processTypeId);//Long.parseLong(processTypeId));
	}
	@Override
	@Transactional(readOnly=true)
	public Process findProcessByRequestBatchTypeId(String requestId, String batchId, long processTypeId) 
	{
		return processDao.findProcessByRequestBatchTypeId(requestId,batchId,processTypeId);//Long.parseLong(processTypeId));
	}
	@Override
	@Transactional(readOnly=true)
	public boolean processExistsByBatchSubBatchTypeId(String batchId, String subBatchId, long processTypeId) 
	{
		return processDao.processExistsByBatchSubBatchTypeId(batchId,subBatchId,processTypeId);//Long.parseLong(processTypeId));
	}
	@Override
	@Transactional(readOnly=true)
	public Process findProcess(UUID primaryKey)
	{
		return processDao.findProcess(primaryKey);
	}
	@Override
	@Transactional
	public void saveProcess(Process process) 
	{
		processDao.saveProcess(process);
	}
	@Override
	@Transactional(readOnly = true)
	public int findNumberProcessByBatchTypeId(String batchId, long processTypeId)
	{
		
		return processDao.findNumberProcessByBatchTypeId(batchId, processTypeId);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Process> findProcessesByBatchTypeId(String batchId,long processTypeId) 
	{
		return processDao.findProcessesByBatchTypeId(batchId,processTypeId);
	}

	@Override
	@Transactional(readOnly = true)
	public int findNumberProcessesFailedByBatchId(String batchId) 
	{
		return processDao.findNumberProcessesFailedByBatchId(batchId);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Process> findProcessesByBatchId(String batchId) 
	{
		return processDao.findProcessesByBatchId(batchId);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Process> findProcessesStillRunning(int l)
	{
		return this.processDao.findProcessesStillRunning(l);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Process> findProcessesByRequestId(String requestId) 
	{
		return this.processDao.findProcessesByRequestId(requestId);
	}
	public ProcessDao getProcessDao() 
	{
		return processDao;
	}

	public void setProcessDao(ProcessDao processDao) 
	{
		this.processDao = processDao;
	}

}
