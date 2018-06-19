/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

//import javax.jms.Queue;

import java.util.Date;

import javax.jms.Destination;

import org.springframework.dao.DataAccessException;
import org.springframework.jms.core.JmsTemplate;

import com.trgr.dockets.core.dao.EventLogDao;
import com.trgr.dockets.core.domain.EventError;
import com.trgr.dockets.core.domain.EventXml;
import com.trgr.dockets.core.entity.EventLog;
import com.trgr.dockets.core.jms.EventMessageCreator;
import com.trgr.dockets.core.util.DocketsCoreUtility;
import com.trgr.dockets.core.util.EventMarshllerAndUnmarshller;
import com.trgr.dockets.core.util.UUIDGenerator;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */
@Deprecated
public class EventLogServiceImpl implements EventLogService{

	private EventLogDao sourceLoadEventDao;
	private EventMarshllerAndUnmarshller eventMarshllerAndUnmarshller ;
	private JmsTemplate jmsTemplate;
	private Destination ltcResponseQueue;

	public EventLogServiceImpl(EventLogDao sourceLoadEventDao,
			EventMarshllerAndUnmarshller eventMarshllerAndUnmarshller,
			JmsTemplate jmsTemplate, Destination ltcResponseQueue) {
		this.sourceLoadEventDao = sourceLoadEventDao;
		this.eventMarshllerAndUnmarshller = eventMarshllerAndUnmarshller;
		this.jmsTemplate = jmsTemplate;
		this.ltcResponseQueue = ltcResponseQueue;
	}
	
	@Override
	public String addEventLog(EventLog eventLog){
		return sourceLoadEventDao.saveEventLog(eventLog);
	}
	
	@Override 
	public void deleteEventLog(long eventLogId){
		this.sourceLoadEventDao.deleteEventLog(eventLogId);
	}
	
	@Override
	public EventLog findEventLogbyId(long eventLogId)
			throws DataAccessException{
		EventLog sourceLoadEvent = this.sourceLoadEventDao.findByPrimaryKey(eventLogId);
		return sourceLoadEvent;
		
	}
	/**
	 * 
	 * dockets.dcrsourceload.start
	   dockets.dcrsourceload.end
	 * @param sourceLoadRequest

	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean logEventMessage(String eventName,String batchId,String serverName,int docCount,String fileName,String fileSize,String errorCode,String errorDescription,String droppedDockets, Date eventDate) throws Exception{
		EventXml eventXml = new EventXml();
		String eventId = UUIDGenerator.createUuid().toString();
		
		eventXml.setEventId(eventId);
		eventXml.setEventName(eventName);
		if(null != eventDate){
			eventXml.setEventTimestamp(eventDate.toString());
		}else{
			eventXml.setEventTimestamp(DocketsCoreUtility.getEventsCurrentTimeStamp());	
		}
		
		eventXml.setBatchId(batchId);
		
		if(isNotNullOrEmpty(serverName))  {
			eventXml.setAppServer(serverName);
		}else {
			eventXml.setAppServer("");
		}
		
		if (docCount >= 0) {
			eventXml.setNumDocs(Integer.toString(docCount));
		}else {
			eventXml.setNumDocs("");
		}
		
		if (isNotNullOrEmpty(fileName)) {
			eventXml.setFileName(fileName);
		}else {
			eventXml.setFileName("");
		}
		
		if (isNotNullOrEmpty(fileSize)) {
			eventXml.setFileSize(fileSize);
		}else{
			eventXml.setFileSize("");
		}
		
		
		EventError eventError = new EventError();
		
		if (isNotNullOrEmpty(errorCode)) {
			eventError.setErrorCode(errorCode);
		}else{
			eventError.setErrorCode("");
		}
		
		if (isNotNullOrEmpty(errorCode)) {
			eventError.setErrorDescription(errorDescription);
		}else{
			eventError.setErrorDescription("");
		}

		eventXml.setEventError(eventError);
		
		if(isNotNullOrEmpty(droppedDockets)){
			eventXml.setDroppedDockets(droppedDockets); 
		}else {
			eventXml.setDroppedDockets(""); 
		}

		eventXml.setSubBatchId("");
		eventXml.setPayload("");
		eventXml.setErrorFolder("");
		eventXml.setWorkFolder("");
		eventXml.setNovusLoadFolder("");
		eventXml.setLogFolder("");

		String eventMessage = eventMarshllerAndUnmarshller.eventMarshal(eventXml);
		sendEventMessage(eventMessage);

		return true;
	}
	
	private Boolean isNotNullOrEmpty(String testString){
		boolean returnFlag = false; 
		if(null !=testString && !testString.equalsIgnoreCase("") ){
			returnFlag = true;
			
		}
		return returnFlag;
	}
	
	@Override
	public void sendEventMessage(String writer){
		EventMessageCreator eventCreator = new EventMessageCreator(writer);
	
		jmsTemplate.send(ltcResponseQueue, eventCreator);
	}
}
