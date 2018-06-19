/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;


/**
 * DAO to manage ContentVersion entities.
 * 
 */

@Transactional
public class ContentVersionDaoImpl implements ContentVersionDao
{
	
	private SessionFactory sessionFactory;
	
	public ContentVersionDaoImpl()
	{
		
	}
	public ContentVersionDaoImpl(SessionFactory hibernateSessionFactory) 
	{
		this.sessionFactory = hibernateSessionFactory;
	}
	

	@Transactional
	public ContentVersion persist(ContentVersion toPersist)
	{
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}


	@Override
	public List<ContentVersion> findContentVersionByUuid(String contentUuid) 
	{
		 final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ContentVersion.class);
			criteria.add(Restrictions.eq(Content.CONTENT_UUID, contentUuid));
	        return (List<ContentVersion>) criteria.list();
	}
	
	public ContentVersion findContentVersionByUuidAndVersion(String contentUuid, Long version){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ContentVersion.class);
		criteria.add(Restrictions.eq(Content.CONTENT_UUID, contentUuid));
		criteria.add(Restrictions.eq("versionId", version));
        return (ContentVersion) criteria.uniqueResult();
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



