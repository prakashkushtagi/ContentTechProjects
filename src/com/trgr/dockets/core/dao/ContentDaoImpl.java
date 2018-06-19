/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.Content;


/**
 * DAO to manage Content entities.
 * 
 */

@Transactional
public class ContentDaoImpl implements ContentDao
{
	
	private SessionFactory sessionFactory;
	
	public ContentDaoImpl()
	{
		
	}
	public ContentDaoImpl(SessionFactory hibernateSessionFactory) 
	{
		this.sessionFactory = hibernateSessionFactory;
	}
	

	@Transactional
	public Content persist(Content toPersist)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(toPersist);
		return toPersist;
	}


	@Override
	public Content findContentByUuid(String contentUuid) 
	{
		 final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Content.class);
			criteria.add(Restrictions.eq(Content.CONTENT_UUID, contentUuid));
	        return (Content) criteria.uniqueResult();
	}


	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}



}



