/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.Batch;

public class BatchDaoImpl implements  BatchDao 
{
	private SessionFactory sessionFactory;

	public BatchDaoImpl() 
	{
	}


	@Override
	public Batch findBatchByBatchId(String batchId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Batch.class);
		criteria.add(Restrictions.eq("primaryKey",batchId));
		return (Batch)criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Batch> findBatchByParentBatchId(String parentBatchId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Batch.class);
		criteria.add(Restrictions.eq("Batch.parent",new Batch(parentBatchId)));
		return (List<Batch>)criteria.list();
	}
	

	@Override
	public int findNumberOfSubBatches(String parentBatchId) 
	{
		String hql = "select count(*) from Batch"
	             + " where parent_batch_id = '" + parentBatchId + "'";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		return ((Number) query.uniqueResult()).intValue();
	}
	
	@Override
	public void saveBatch(Batch batch)
	{
		save(batch);
		
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
}
