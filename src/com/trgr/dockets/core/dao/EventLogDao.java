/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.dao;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.EventLog;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */
public interface EventLogDao {

	public String saveEventLog(EventLog eventLog);
	
	public void deleteEventLog(long eventLogId);
	
	public EventLog findByPrimaryKey(long eventLogId) throws DataAccessException;
	
}
