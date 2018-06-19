/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.EventLog;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */

public class EventLogDaoImpl implements EventLogDao {
	private SessionFactory sessionFactory;
	
	public EventLogDaoImpl(SessionFactory sessionFactory){
		super();
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void deleteEventLog(long eventLogId){
		Session session = sessionFactory.getCurrentSession();
		EventLog eventLog = findByPrimaryKey(eventLogId);
		session.delete(eventLog);
		session.flush();
	}
	
	@Override
	public String saveEventLog(EventLog eventLog){
		Session session = sessionFactory.getCurrentSession();
		return (String) session.save(eventLog);
		
	}
	
	@Override
	public EventLog findByPrimaryKey(long eventLogId) throws DataAccessException{
		Session session = sessionFactory.getCurrentSession();
		EventLog jobRequest = (EventLog) session.get(EventLogDao.class, eventLogId);
		return jobRequest;
	}


}


