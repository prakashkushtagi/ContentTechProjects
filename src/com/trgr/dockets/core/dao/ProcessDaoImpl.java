/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.Process;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public class ProcessDaoImpl implements ProcessDao 
{
	private SessionFactory sessionFactory;

	public ProcessDaoImpl(SessionFactory sessionFactory) 
	{
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Process findProcess(UUID primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (Process) session.get(Process.class, primaryKey.toString());
	}

	@Override
	public Process findProcessBatchSubBatchTypeId(String batchId, String subBatchId, long processTypeId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Process.class);
		criteria.add(Restrictions.eq("batchId", batchId));
		criteria.add(Restrictions.eq("subBatchId", subBatchId));
		criteria.add(Restrictions.eq("processType.primaryKey",processTypeId));
		return (Process)criteria.uniqueResult();
	}

	public Process findProcessByRequestBatchTypeId(String requestId,String batchId, long processTypeId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Process.class);
		criteria.add(Restrictions.eq("batchId", batchId));
		criteria.add(Restrictions.eq("publishingRequestId", requestId));
		criteria.add(Restrictions.eq("processType.primaryKey",processTypeId));
		return (Process)criteria.uniqueResult();
	}
	
	@Override
	public boolean processExistsByBatchSubBatchTypeId(String batchId, String subBatchId, long processTypeId)
	{
		boolean exists = false;
		Process processInTable = findProcessBatchSubBatchTypeId(batchId, subBatchId, processTypeId);
		if(null!=processInTable)
		{
			exists = true;
		}
		return exists;
	}
	@Override
	public void saveProcess(Process process) 
	{
		save(process);
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
	public int findNumberProcessByBatchTypeId(String batchId, long processTypeId) 
	{
		String hql = "select count(*) from Process"
	             + " where  batchId = '" + batchId + "' and processType.primaryKey = " + processTypeId;
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findProcessesByBatchTypeId(String batchId,long processTypeId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Process.class);
		criteria.add(Restrictions.eq("batchId", batchId));
		criteria.add(Restrictions.eq("processType.primaryKey",processTypeId));
		criteria.addOrder(Order.asc("startDate"));
		return (List<Process>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Process> findProcessWithErrorDescByRequestId(String requestId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Process.class);
		criteria.add(Restrictions.eq("publishingRequestId", requestId));
		criteria.add(Restrictions.isNotNull("errorDescription"));
		criteria.addOrder(Order.asc("batchId"));
		return (List<Process>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Process> findProcessesStillRunning(int l)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -l);
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Process.class);
		criteria.add(Restrictions.eq("processType.primaryKey", 19l));
		criteria.add(Restrictions.eq("status.id", 2l));
		criteria.add(Restrictions.lt("startDate", cal.getTime()));
		return (List<Process>)criteria.list();
	}
	@Override
	public int findNumberProcessesFailedByBatchId(String batchId)
	{
		String hql = "select count(*) from Process"
	             + " where batchId = '" + batchId + "' and process_status_id = 5";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		return ((Number) query.uniqueResult()).intValue();
	}
	


	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findProcessesByBatchId(String batchId) 
	{

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Process.class);
		criteria.add(Restrictions.eq("batchId", batchId));
		return (List<Process>)criteria.list();
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Process> findProcessesByRequestId(String requestId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Process.class);
		criteria.add(Restrictions.eq("publishingRequestId", requestId));
		return (List<Process>)criteria.list();
	}



}