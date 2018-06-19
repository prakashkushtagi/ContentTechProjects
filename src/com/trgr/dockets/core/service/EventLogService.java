/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

import java.util.Date;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.EventLog;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 *
 */
@Deprecated
public interface EventLogService {
	
	public String addEventLog(EventLog eventLog);
	
	public void deleteEventLog(long eventLogId);
	
	public EventLog findEventLogbyId(long eventLogId) throws DataAccessException;

	public void sendEventMessage(String writer);

	public boolean logEventMessage(String eventName, String batchId,
			String serverName, int docCount, String fileName, String fileSize,
			String errorCode, String errorDescription, String droppedDockets,
			Date eventDate) throws Exception;
	
}
