/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.PublishingRequest;

public class PublishingRequestDaoImpl implements PublishingRequestDao
{

	private SessionFactory sessionFactory;

	public PublishingRequestDaoImpl() 
	{
	}

	@Override
	public void savePublishingRequest(PublishingRequest publishingRequest) 
	{
		save(publishingRequest);
	}
	
	private <T> void save(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
	}
	
	@Override
	public void deletePublishingRequest(PublishingRequest publishingRequest)
	{
		delete(publishingRequest);
	}
	
	private <T> void delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public PublishingRequest findPublishingRequestByRequestId(String requestId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PublishingRequest.class);
		criteria.add(Restrictions.eq("requestId", requestId));
		return (PublishingRequest)criteria.uniqueResult();
	}
	
	
	
}
