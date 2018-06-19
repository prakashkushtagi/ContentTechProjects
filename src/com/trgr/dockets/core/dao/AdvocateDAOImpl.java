/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.Advocate;

/**
 * DAO to manage Advocate entities.
 * 
 */
@Transactional
public class AdvocateDAOImpl implements AdvocateDAO {

//	private static final Integer DEFAULT_FIRST_RESULT_INDEX = 1000;

	private SessionFactory sessionFactory;

	/**
	 * Instantiates a new AdvocateDAOImpl
	 * 
	 */
	public AdvocateDAOImpl() {
		super();
	}

	public AdvocateDAOImpl(SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}

	/**
	 * Query - findAdvocateByCountyId
	 * 
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Advocate> findAdvocateByCountyId(Integer countyId)
			throws DataAccessException {
		Query query = createNamedQuery("findAdvocateByCountyId");
		query.setInteger("countyId", countyId);
		List<Advocate> advocateList = query.list();
		return advocateList;
	}

	/**
	 * Query - findAdvocateByPrimaryKey
	 * 
	 */
	@Transactional(readOnly=true)
	public Advocate findAdvocateByPrimaryKey(Integer countyId, Integer type,
			String id) throws DataAccessException {

		try {
			Query query = createNamedQuery("findAdvocateByPrimaryKey");
			query.setInteger("countyId", countyId);
			query.setInteger("type", type);
			query.setString("id", id);
			return (com.trgr.dockets.core.entity.Advocate) query.uniqueResult();
		} catch (NonUniqueObjectException nre) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public Advocate persist(Advocate toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void remove(Advocate toRemove) {
		toRemove = (Advocate) sessionFactory.getCurrentSession()
				.merge(toRemove);
		sessionFactory.getCurrentSession().delete(toRemove);
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void update(Advocate toUpdate) {
		sessionFactory.getCurrentSession().update(toUpdate);
	}

	/**
	 * Used to determine whether or not to merge the entity or persist the
	 * entity when calling Store
	 * 
	 * @see store
	 * 
	 * 
	 */
	public boolean canBeMerged(Advocate entity) {
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
}
