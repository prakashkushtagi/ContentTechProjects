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

import com.trgr.dockets.core.entity.Dictionary;

/**
 * DAO to manage Dictionary entities.
 * 
 */
@Transactional
public class DictionaryDAOImpl implements
		DictionaryDAO {

//	private static final Integer DEFAULT_FIRST_RESULT_INDEX = 1000;

	private SessionFactory sessionFactory;

	/**
	 * Instantiates a new DictionaryDAOImpl
	 *
	 */
	public DictionaryDAOImpl() {
		super();
	}

	public DictionaryDAOImpl(SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}

	/**
	 * Query - findDictionaryByCountyId
	 *
	 */
	@Transactional
	public List<Dictionary> findDictionaryByCountyId(Integer countyId) throws DataAccessException {

		Query query = createNamedQuery("findDictionaryByCourtId");
		query.setInteger("countyId", countyId);
		@SuppressWarnings("unchecked")
		List<Dictionary> dictionaryList = query.list();
		return dictionaryList;
	}

	/**
	 * Query - findDictionaryByPrimaryKey
	 *
	 */
	@Transactional
	public Dictionary findDictionaryByPrimaryKey(Integer countyId, Integer type, String key) throws DataAccessException {
		try {
			Query query = createNamedQuery("findDictionaryByPrimaryKey");
			query.setInteger("countyId", countyId);
			query.setInteger("type", type);
			query.setString("key", key);
			return (com.trgr.dockets.core.entity.Dictionary) query.uniqueResult();
		} catch (NonUniqueObjectException nre) {
			return null;
		}
	}

	/**
	 * Used to determine whether or not to merge the entity or persist the entity when calling Store
	 * @see store
	 * 
	 *
	 */
	public boolean canBeMerged(Dictionary entity) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public Dictionary persist(Dictionary toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void remove(Dictionary toRemove) {
		toRemove = (Dictionary) sessionFactory.getCurrentSession().merge(
				toRemove);
		sessionFactory.getCurrentSession().delete(toRemove);
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void update(Dictionary toUpdate) {
		sessionFactory.getCurrentSession().update(toUpdate);
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
