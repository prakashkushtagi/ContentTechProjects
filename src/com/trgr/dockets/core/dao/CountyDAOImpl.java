/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.County;

/**
 * DAO to manage County entities.
 * 
 */

@Transactional
public class CountyDAOImpl implements CountyDAO {

	private SessionFactory sessionFactory;

	/**
	 * Instantiates a new CountyDAOImpl
	 *
	 */
	public CountyDAOImpl() {
		super();
	}

	public CountyDAOImpl(SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}

	/**
	 * Query - findCountyByCountyId
	 *
	 */
	@Transactional
	public County findCountyByCourtIdAndCountyCode(Integer courtId, Integer countyCode) throws DataAccessException {
		try {
		Query query = createNamedQuery("findCountyByCourtIdAndCountyCode");
		query.setInteger("courtId", courtId);
		query.setInteger("countyCode", countyCode);		
		return (com.trgr.dockets.core.entity.County) query.uniqueResult();
	} catch (NonUniqueObjectException nre) {
		return null;
	}
	}
	/**
	 * Get unique county as result if not return null. 
	 * @param courtId
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public County findCountyByCourtId(Integer courtId)  {
			try {
			Query query = createNamedQuery("findCountyByCourtId");
			query.setInteger("courtId", courtId);
			return (com.trgr.dockets.core.entity.County) query.uniqueResult();
		} catch (NonUniqueObjectException nre) {
			return null;
		}catch(NonUniqueResultException nre2){
			return null;
		}
	}

	
	@Transactional
	public County findCountyByColKey(String colKey) throws DataAccessException {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(County.class);
		criteria.add(Restrictions.eq("colKey", colKey).ignoreCase());
		return (County) criteria.list().get(0);
	}


	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.CountyDAO#findCountyByName(java.lang.String)
	 */
	@Transactional
	public County findCountyByNameAndCourt(String countyName, Integer courtId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(County.class);
		criteria.add(Restrictions.eq("name", countyName).ignoreCase());
		criteria.add(Restrictions.eq("courtId", courtId));		
		return (County) criteria.uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.CountyDAO#findCountyByName(java.lang.String)
	 */
	@Transactional
	public County findCountyByName(String countyName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(County.class);
		criteria.add(Restrictions.eq("name", countyName).ignoreCase());
		return (County) criteria.uniqueResult();
	}
	
	/**
	 * Query - findCountyByPrimaryKey
	 *
	 */
	@Transactional
	public County findCountyByPrimaryKey(Integer countyId) throws DataAccessException {
		try {
			Query query = createNamedQuery("findCountyByPrimaryKey");
			query.setInteger("countyId", countyId);
			return (com.trgr.dockets.core.entity.County) query.uniqueResult();
		} catch (NonUniqueObjectException nre) {
			return null;
		}
	}

	/**
	 * Query - findCountyByPrimaryKey
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<County> loadCounty()  {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(County.class)
				.addOrder(Order.desc("courtNorm"));
		return criteria.list();
	}
	/**
	 * Used to determine whether or not to merge the entity or persist the entity when calling Store
	 * @see store
	 * 
	 *
	 */
	public boolean canBeMerged(County entity) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public Query createNamedQuery(String queryName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(
				queryName);
		return query;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public County persist(County toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void remove(County toRemove) {
		toRemove = (County) sessionFactory.getCurrentSession()
				.merge(toRemove);
		sessionFactory.getCurrentSession().delete(toRemove);
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void update(County toUpdate) {
		sessionFactory.getCurrentSession().update(toUpdate);
	}

}
