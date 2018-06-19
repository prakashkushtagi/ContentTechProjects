/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.LmsWorkDao;
import com.trgr.dockets.core.dao.LoadMonitorLtcMessageDao;
import com.trgr.dockets.core.domain.ltc.LtcCollaborationKey;
import com.trgr.dockets.core.entity.LmsWorkItem;
import com.trgr.dockets.core.entity.LoadMonitorLtcMessage;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

public class LoadMonitorLtcMessageService
{

	private LoadMonitorLtcMessageDao loadMonitorLtcMessageDao;
	private LmsWorkDao lmsWorkDao;
	
	/**
	 * @param loadMonitorLtcMessage
	 * 
	 * @throws UUIDException 
	 * @throws NumberFormatException 
	 */
	public void createLmsWorkItem(LoadMonitorLtcMessage loadMonitorLtcMessage) throws NumberFormatException, UUIDException
	{
		LtcCollaborationKey ltcCollaborationKey = new LtcCollaborationKey(loadMonitorLtcMessage.getCollaborationKey());
		LmsWorkItem lmsWorkItem = new LmsWorkItem(new UUID(ltcCollaborationKey.getRequestId()), loadMonitorLtcMessage.getMessageId(), ltcCollaborationKey.getProductCode());
		lmsWorkDao.save(lmsWorkItem);
	}

	@Transactional
	public void saveOrUpdateLoadMonitorLtcMessage(LoadMonitorLtcMessage loadMonitorLtcMessage) throws ParseException
	{
		loadMonitorLtcMessageDao.saveOrUpdatePublishingControl(loadMonitorLtcMessage);
	}
	
	@Transactional
	public void deleteLoadMonitorLtcMessagesByFilename(String filename) 
	{
		loadMonitorLtcMessageDao.deleteLtcMessagesByFilename(filename);
	}

	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> findLoadMonitorLtcMessageByEventId(long eventId)
	{
		return loadMonitorLtcMessageDao.getLtcMessageByEventId(eventId);
	}
	
	@Transactional(readOnly = true)
	public LoadMonitorLtcMessage findLoadMonitorLtcMessageByMessageId(String messageId)
	{
		return loadMonitorLtcMessageDao.getLtcMessageByMessageId(messageId);
	}
	
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> findLoadMonitorLtcMessageByBatchIdSubBatchId(String batchId, String subBatchId)
	{
		String collaborationKey = batchId+"_"+subBatchId;
		return loadMonitorLtcMessageDao.getLtcMessageByCollaborationKey(collaborationKey);
	}
	@Transactional(readOnly = true)
	public LoadMonitorLtcMessage findLoadMonitorLtcMessageByBatchIdSubBatchIdMessageType(String batchId, String subBatchId,String messageType)
	{
		String collaborationKey = batchId+"_"+subBatchId;
		return loadMonitorLtcMessageDao.getLtcMessageByCollaborationKeyMessageType(collaborationKey,messageType);
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> findLoadMonitorLtcMessageForProcessing() 
	{
		//currently only 301
		return loadMonitorLtcMessageDao.getLtcMessageByStatusId();
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> findLoadMonitorLtcMessageForProcessingFor100() 
	{
		//currently only 301
		return loadMonitorLtcMessageDao.getLtcMessageByStatusIdFor100();
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> findLoadMonitorLtcMessageForMonitorProcessing(int numberOfRecords) 
	{
		//currently only 301
		List<String> statusesNeedProcessing = new ArrayList<String>(); 
		statusesNeedProcessing.add("301");
		return loadMonitorLtcMessageDao.getLtcMessageByStatusIdFor100(statusesNeedProcessing,numberOfRecords);
	}
	@Transactional(readOnly = true)
	public LoadMonitorLtcMessage findByCollaborationKeyFilenameStep(String collaborationKey, String ltcFilename, String loadElement) 
	{
		return loadMonitorLtcMessageDao.findByCollaborationKeyFilenameStep( collaborationKey, ltcFilename, loadElement);
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> findLoadMonitorLtcMessagesByFilename(String ltcFilename)
	{
		return loadMonitorLtcMessageDao.findLtcMessagesByFilename(ltcFilename);
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> findLoadMonitorLtcMessageForMessageProcessing(int numberOfRecords) 
	{
		//currently only 100
		List<String> statusesNeedProcessing = new ArrayList<String>(); 
		statusesNeedProcessing.add("100");
		return loadMonitorLtcMessageDao.getLtcMessageByStatusIdFor100(statusesNeedProcessing,numberOfRecords);
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> getLtcMessageByStatusIdForMaxRecords(String statusId, int maxRecords)
	{
		return loadMonitorLtcMessageDao.getLtcMessageByStatusIdForMaxRecords(statusId,maxRecords);
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> getLtcMessageForDocketsProcessing(int maxRecords)
	{
		return loadMonitorLtcMessageDao.getLtcMessageByStatusIdForMaxRecords("100",maxRecords);
	}
	@Transactional(readOnly = true)
	public List<LoadMonitorLtcMessage> getLtcMessageForJrecsProcessing(int maxRecords)
	{
		return loadMonitorLtcMessageDao.getLtcMessageByStatusIdForMaxRecords("200",maxRecords);
	}
	/**
	 * @return the loadMonitorLtcMessageDao
	 */
	public LoadMonitorLtcMessageDao getLoadMonitorLtcMessageDao() {
		return loadMonitorLtcMessageDao;
	}

	/**
	 * @param loadMonitorLtcMessageDao the loadMonitorLtcMessageDao to set
	 */
	public void setLoadMonitorLtcMessageDao(LoadMonitorLtcMessageDao loadMonitorLtcMessageDao) {
		this.loadMonitorLtcMessageDao = loadMonitorLtcMessageDao;
	}

	/**
	 * @return the lmsWorkDao
	 */
	public LmsWorkDao getLmsWorkDao() {
		return lmsWorkDao;
	}

	/**
	 * @param lmsWorkDao the lmsWorkDao to set
	 */
	public void setLmsWorkDao(LmsWorkDao lmsWorkDao) {
		this.lmsWorkDao = lmsWorkDao;
	}


}