/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.ErrorLog;

/**
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com) 
 *
 */
public class ErrorLogDaoImpl implements ErrorLogDao {
	
	//private static final Logger log = Logger.getLogger(ErrorLogDaoImpl.class);
	private SessionFactory sessionFactory;
	
	
	public ErrorLogDaoImpl(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void deleteErrorLog(long errorLogId) {
		Session session = sessionFactory.getCurrentSession();
		ErrorLog errorLog = findByPrimaryKey(errorLogId);
		session.delete(errorLog);
	}
	
	@SuppressWarnings("unchecked")
	public List<ErrorLog> findAllErrorLogs() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ErrorLog.class);
		return criteria.list();
	}
	
	@Override
	public ErrorLog findByPrimaryKey(long errorLogId) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		ErrorLog jobRequest = (ErrorLog) session.get(ErrorLog.class, errorLogId);
		
		return jobRequest;  
	}
	
	@Override
	public List<ErrorLog> findErrorLogByRequestId(long requestId){
		@SuppressWarnings("unchecked")
		List<ErrorLog> jobRequestList = sessionFactory.getCurrentSession().createCriteria(ErrorLog.class)
		 .add(Restrictions.eq("errorLog.requestId", requestId)).list();

		return jobRequestList;
	}
	
	@Override
	public Long saveErrorLog(ErrorLog errorLog) {		
		Session session = sessionFactory.getCurrentSession();
		return (Long) session.save(errorLog);
	}
	
	
	
}
