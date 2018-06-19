/** 
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.NotificationDao;
import com.trgr.dockets.core.dao.ProcessDao;
import com.trgr.dockets.core.domain.NotificationInfo;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.DataCaptureRequest;
import com.trgr.dockets.core.entity.NotificationRequest;
import com.trgr.dockets.core.entity.NotificationTypeEvent;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.util.Environment;

public class NotificationServiceImpl implements NotificationService{
	
	private static final Logger log = Logger.getLogger(NotificationServiceImpl.class);
	
	private static final int ERROR_DESCRIPTION_CHARACTER_LIMIT = 3500;
	
	private NotificationDao notificationDao;
	private ProcessDao processDao;

	public NotificationServiceImpl (NotificationDao notificationDao, ProcessDao processDao){
		this.notificationDao = notificationDao;
		this.processDao = processDao;
	}
	
	public NotificationServiceImpl() {

	}

	@Override
	@Transactional
	public Object save(Object entity) {
		return notificationDao.save(entity);
	}

	@Override
	@Transactional
	public void update(Object entity) {
		notificationDao.update(entity);
	}	
	@Override
	@Transactional
	public void delete(Object entity) {
		notificationDao.delete(entity);
	}
	
	@Override
	@Transactional(readOnly=true)
	public NotificationTypeEvent findNotifEventByTypeAndStatus(String notificationType, String status) {
		return notificationDao.findNotifEventByTypeAndStatus(notificationType, status);
	}
	
	@Override
	@Transactional(readOnly=true)
	public String findNotificationGroupByTypeAndStatus(String notificationType, String status) {
		return notificationDao.findNotificationGroupByTypeAndStatus(notificationType, status);
	}


	@Override
	@Transactional
	public void createNotificationForRequest(PublishingRequest pubReq, StatusEnum status, boolean ppOnly) {
		
		if (pubReq != null) {			
			log.debug("Start creating notification Request for Request: "+ pubReq.getRequestName());
		}
		
		String notificationType = "REQUEST";
		String statusKey = status.getKey().toString();
		
		List<String> batchWithProcessErr = new ArrayList<String>();	
		List<Process> processList = null;
		
		if (pubReq != null) {	
			processList = processDao.findProcessWithErrorDescByRequestId(pubReq.getRequestId());		
		}
		
		try
		{		
			if(processList != null && !processList.isEmpty())
			{
				int errorDescCharCount = 0;
				for(Process process : processList)
				{
					//We are no more creating 2 process for NY so the below IF condition can be commented - Bug 146499
//					if(!process.getProcessType().getName().equalsIgnoreCase("DCRSourceContentLoad")) //ignore this process for notification details.
//					{
						String errorMsg = 
								"\tBatchId: " + process.getBatchId() + "\n"
								+ "\tProcess: " + process.getProcessType().getName() + "\n"
								+ "\tError Description: " + process.getErrorDescription() + " \n";
					
						errorDescCharCount += errorMsg.length();
						if (errorDescCharCount <= ERROR_DESCRIPTION_CHARACTER_LIMIT) {
							batchWithProcessErr.add(errorMsg);
						} else {
							batchWithProcessErr.add("\tSee Batch Monitor page for more errors.");
							break;
						}
//					}						
				}
			}
			else
			{
				batchWithProcessErr.add("No Batch/Process errors available.");
			}
		
			String env = Environment.getInstance().getEnv();
			//ppOnly request
			if((status.name().equalsIgnoreCase("FAILED") || (status.name().equalsIgnoreCase("COMPLETED_WITH_ERRORS"))) && ppOnly)
			{
				NotificationTypeEvent notificationTypeList = notificationDao.findNotifEventByTypeAndStatus(notificationType, statusKey);			
				String notificationGroupList = notificationDao.findNotificationGroupByTypeAndStatus(notificationType, statusKey);				
	
				NotificationInfo notificationInfo = new NotificationInfo();
				notificationInfo.setNotificationTypeEvent(notificationTypeList);
				notificationInfo.setNotificationGroupString(notificationGroupList);
				notificationInfo.setPubReq(pubReq);
				notificationInfo.setBatchWithProcessErr(batchWithProcessErr);
				notificationInfo.setEnv(env);
				
				NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo);
				
				notificationDao.save(notificationRequest);					
			}
			//all other requests.
			else if((status.name().equalsIgnoreCase("FAILED") || (status.name().equalsIgnoreCase("COMPLETED_WITH_ERRORS"))) && !ppOnly)
			{
				NotificationTypeEvent notificationTypeList = notificationDao.findNotifEventByTypeAndStatus(notificationType, statusKey);			
				String notificationGroupList = notificationDao.findNotificationGroupByTypeAndStatus(notificationType, statusKey);								

				NotificationInfo notificationInfo = new NotificationInfo();
				notificationInfo.setNotificationTypeEvent(notificationTypeList);
				notificationInfo.setNotificationGroupString(notificationGroupList);
				notificationInfo.setPubReq(pubReq);
				notificationInfo.setBatchWithProcessErr(batchWithProcessErr);
				notificationInfo.setEnv(env);
				
				NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo);
				
				notificationDao.save(notificationRequest);
			}
		}
		catch(Exception e)
		{
			if (pubReq != null) {	
				log.error("Notification Request could not be created for Request: "+ pubReq.getRequestName());
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(outputStream);
			e.printStackTrace(ps);
			ps.close();
			log.error("Failed to create Notification Request. " + outputStream.toString());
			
			
		}
	}
		
	/**
	 * Notification request for "Process" like RPX or MetaDoc,  in case of failure or "Complete with errors" Email can be sent to user group.
	 * 
	 */
	@Override
	@Transactional
	public void createNotificationForProcess(PublishingRequest pubReq, String batchId, StatusEnum status,String statusMessage, String collectionName, String metadocInputFile) {
		log.error("Start creating notification Request for Process: "+ batchId +" of Request: "+ pubReq.getRequestName());
		if((status.name().equalsIgnoreCase("FAILED") || (status.name().equalsIgnoreCase("COMPLETED_WITH_ERRORS"))))
		{
			
			String notificationType;
			if (StringUtils.isNotBlank(metadocInputFile))
			{
				notificationType = "METADOC";
				
			}else{
				notificationType = "PROCESS";
			}
			String statusKey = status.getKey().toString();
			String errorDesc = statusMessage;
			
			//Create Notification Request for PBUI service
			try
			{

				//Get Notification Type Event.
				NotificationTypeEvent notificationTypeList = notificationDao.findNotifEventByTypeAndStatus(notificationType, statusKey);			
				//Get Notification Group subscribed for the Event.
				String notificationGroupList = notificationDao.findNotificationGroupByTypeAndStatus(notificationType, statusKey);				
				//Get Environment name
				String env = Environment.getInstance().getEnv();				
				
				NotificationInfo notificationInfo = new NotificationInfo();
				notificationInfo.setNotificationTypeEvent(notificationTypeList);
				notificationInfo.setNotificationGroupString(notificationGroupList);
				notificationInfo.setPubReq(pubReq);
				notificationInfo.setBatchId(batchId);
				notificationInfo.setErrorDesc(errorDesc);
				notificationInfo.setEnv(env);
				notificationInfo.setCollectionName(collectionName);
				notificationInfo.setPubId(String.valueOf(pubReq.getCourt().getPublicationId()));
				notificationInfo.setMetaDocInputFile(metadocInputFile);
				
				NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo);
				
				notificationDao.save(notificationRequest);				
			}
			catch(Exception e)
			{
				
				log.error("Notification Request could not be created for process: "+ batchId +" of Request: "+ pubReq.getRequestName()+" " + Arrays.toString(e.getStackTrace()));
				log.debug("Failed to create Notification Request. "+ e);
			}		
		}
	}	
	
	/**
	 * Notification request for "Acquisition Web error" .
	 * 
	 */
	@Override
	@Transactional
	public void createNotificationForAcquisitionError(String mergeFileName,PublishingRequest pubReq, StatusEnum status,String errorDescription,String receiptId ) {
		log.error("Start creating notification Request for Acquisition Error: of Request: "+ pubReq.getRequestName());
			
			String notificationType = "ACQUISITION";
			
			
			String statusKey = status.getKey().toString();

			
			//Create Notification Request for PBUI service
			try
			{

				//Get Notification Type Event.
				NotificationTypeEvent notificationTypeList = findNotifEventByTypeAndStatus(notificationType, statusKey);
													
				
				//Get Notification Group subscribed for the Event.
				String notificationGroupList = notificationDao.findNotificationGroupByTypeAndStatus(notificationType, statusKey);				
				//Get Environment name
				String env = Environment.getInstance().getEnv();				
				
				NotificationInfo notificationInfo = new NotificationInfo();
				notificationInfo.setMergeFileName(mergeFileName);
				notificationInfo.setErrorDesc(errorDescription);
				notificationInfo.setReceiptId(receiptId);
				notificationInfo.setNotificationTypeEvent(notificationTypeList);
				notificationInfo.setNotificationGroupString(notificationGroupList);
				notificationInfo.setPubReq(pubReq);
				
				notificationInfo.setEnv(env);
				notificationInfo.setPubId(String.valueOf(pubReq.getCourt().getPublicationId()));
				
				NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo);
				
				notificationDao.save(notificationRequest);				
			}
			catch(Exception e)
			{
				
				log.error("Notification Request could not be created for process: with  Request: "+ pubReq.getRequestName()+" " + Arrays.toString(e.getStackTrace()));
				log.debug("Failed to create Notification Request. "+ e);
			}		
	}	

	
	
	@Override
	@Transactional
	public void createNotificationForFTP(PublishingRequest pubReq, StatusEnum status, String FTPInfo, String errorMessage) {
		log.debug("Start creating notification Request for Request: "+ pubReq.getRequestName());
		
		String notificationType = "FTP";
		String statusKey = status.getKey().toString();
		
		try
		{

				//Get Notification Type Event.
				NotificationTypeEvent notificationTypeList = notificationDao.findNotifEventByTypeAndStatus(notificationType, statusKey);			
				//Get Notification Group subscribed for the Event.
				String notificationGroupList = notificationDao.findNotificationGroupByTypeAndStatus(notificationType, statusKey);				
				//Get Environment name
				String env = Environment.getInstance().getEnv();				
				//Call Notification Util to get new Notification Request
				NotificationInfo notificationInfo = new NotificationInfo();
				notificationInfo.setNotificationTypeEvent(notificationTypeList);
				notificationInfo.setNotificationGroupString(notificationGroupList);
				notificationInfo.setPubReq(pubReq);
				notificationInfo.setFTPFile(FTPInfo);
				notificationInfo.setEnv(env);
				notificationInfo.setStatusName(status.name());
				
				if((status.name().equalsIgnoreCase("FAILED") || (status.name().equalsIgnoreCase("COMPLETED_WITH_ERRORS"))))
				{
					if (errorMessage == null || errorMessage.isEmpty())
					{
						notificationInfo.setErrorDesc("FTP Failed");
					}
					else
					{
						notificationInfo.setErrorDesc(errorMessage);
					}
				}
					
				NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationTypes(notificationInfo); 

				notificationDao.save(notificationRequest);
		}
		catch(Exception e)
		{
			log.error("FTP Notification Request could not be created for Request: "+ pubReq.getRequestName());
			log.debug("Failed to create Notification Request. "+ e);
		}
	}
	
	@Transactional
	public void createNotificationForDcRequest(DataCaptureRequest dcRequest, String errorMessage) {
		log.debug("Start createNotificationForDcRequest: "+ dcRequest.getRequestName());
		
		String notificationType = "REQUEST";
		StatusEnum status = StatusEnum.FAILED;
		
		List<String> batchWithProcessErr = new ArrayList<String>();	
		batchWithProcessErr.add(errorMessage);
		
		String statusKey = status.getKey().toString();
		
		try
		{
				System.out.println("");
				System.out.println("createNotificationForPublishingRequestError" + dcRequest);

				//Get Notification Type Event.
				NotificationTypeEvent notificationTypeList = notificationDao.findNotifEventByTypeAndStatus(notificationType, statusKey);			
				//Get Notification Group subscribed for the Event.
				String notificationGroupList = notificationDao.findNotificationGroupByTypeAndStatus(notificationType, statusKey);				
				//Get Environment name
				String env = Environment.getInstance().getEnv();				
				//Call Notification Util to get new Notification Request
				NotificationInfo notificationInfo = new NotificationInfo();
				notificationInfo.setNotificationTypeEvent(notificationTypeList);
				notificationInfo.setNotificationGroupString(notificationGroupList);
				notificationInfo.setDcReq(dcRequest);
				notificationInfo.setEnv(env);
				notificationInfo.setStatusName(status.name());
				notificationInfo.setBatchWithProcessErr(batchWithProcessErr);
				notificationInfo.setErrorDesc("Request Failed");	
				NotificationRequest notificationRequest = NotificationServiceUtil.createNotificationForNotificationDcTypes(notificationInfo); 
				
				notificationDao.save(notificationRequest);
		}
		catch(Exception e)
		{
			log.error("createNotificationForDcRequest Notification Request could not be created for Request: "+ dcRequest.getRequestName());
			log.debug("Failed to create Notification Request. "+ e);
		}
	}
}
