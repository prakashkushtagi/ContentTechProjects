package com.trgr.dockets.core.service;

import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.NotificationTypeEvent;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.DataCaptureRequest;


public interface NotificationService {
	
	//Notification_Type_Event Dao
	public NotificationTypeEvent findNotifEventByTypeAndStatus(String notificationType, String status);

	//Method to get notification group for a given type and status
	public String findNotificationGroupByTypeAndStatus(String notificationType, String status);
	
	
	//Generalized Services
	public Object save(Object entity);
	
	public void update(Object entity);
	
	public void delete (Object Entity);
	
	//Notification Request service
	public void createNotificationForRequest(PublishingRequest pubReq, StatusEnum status, boolean ppOnly);
	public void createNotificationForProcess(PublishingRequest pubReq, String batchId, StatusEnum status, String statusMessage, String collectionName, String metadocInputFile);
	public void createNotificationForFTP(PublishingRequest pubReq, StatusEnum status, String FTPInfo, String errorMessage);
	public void createNotificationForDcRequest(DataCaptureRequest dcRequest, String errorMessage);
	public void createNotificationForAcquisitionError(String mergeFileName,PublishingRequest pubReq,  StatusEnum status, String statusMessage,String receiptId);
}
