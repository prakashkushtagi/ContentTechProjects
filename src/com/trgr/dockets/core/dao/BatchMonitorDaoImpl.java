/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.Status;

public class BatchMonitorDaoImpl implements  BatchMonitorDao 
{
	private SessionFactory sessionFactory;

	public BatchMonitorDaoImpl() 
	{
	}
	
	@Override
	public void saveBatchMonitor(BatchMonitor batchMonitor) {
		save(batchMonitor);
	}
	private <T> void save(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	@Override
	public BatchMonitor findBatchMonitorByBatchId(String batchId) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BatchMonitor.class);
		criteria.add(Restrictions.eq(BatchMonitor.BATCH_ID, batchId));
        return (BatchMonitor) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BatchMonitor> findBatchesByRequestId(String requestId) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BatchMonitor.class);
		criteria.add(Restrictions.eq(BatchMonitor.REQUEST_ID, requestId));
        return (List<BatchMonitor>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BatchMonitor> findBatchesByRequestIdForUpdate(String requestId) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BatchMonitor.class);
		criteria.add(Restrictions.eq(BatchMonitor.REQUEST_ID, requestId));
		criteria.setLockMode(LockMode.PESSIMISTIC_WRITE);
        return (List<BatchMonitor>) criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BatchMonitor> findBatchesByRequestIdAndStatus(String requestId, Status status)
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BatchMonitor.class);
		criteria.add(Restrictions.eq(BatchMonitor.REQUEST_ID, requestId));
		criteria.add(Restrictions.eq("status", status));
		 return (List<BatchMonitor>) criteria.list();
	}
}
