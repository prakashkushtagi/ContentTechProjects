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

import com.trgr.dockets.core.entity.DocketEntity;


/**
 * DAO to manage Docket entities.
 * 
 */

@Transactional
public class DocketEntityDaoImpl implements DocketEntityDao
{
	
	private SessionFactory sessionFactory;
	
	public DocketEntityDaoImpl()
	{
		
	}
	public DocketEntityDaoImpl(SessionFactory hibernateSessionFactory) 
	{
		this.sessionFactory = hibernateSessionFactory;
	}
	

	@Transactional
	public DocketEntity persist(DocketEntity toPersist)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(toPersist);
		return toPersist;
	}


	@Override
	public DocketEntity findDocketByLegacyId(String legacyId) 
	{
		 final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketEntity.class);
			criteria.add(Restrictions.eq(DocketEntity.LEGACY_ID, legacyId));
	        return (DocketEntity) criteria.uniqueResult();
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



