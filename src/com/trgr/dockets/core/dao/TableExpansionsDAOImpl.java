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

import com.trgr.dockets.core.entity.TableExpansions;

/**
 * DAO to manage TableExpansions entities.
 * 
 */
@Transactional
public class TableExpansionsDAOImpl implements TableExpansionsDAO {

//	private static final Integer DEFAULT_FIRST_RESULT_INDEX = 1000;

	private SessionFactory sessionFactory;

	/**
	 * Instantiates a new TableExpansionsDAOImpl
	 *
	 */
	public TableExpansionsDAOImpl() {
		super();
	}

	public TableExpansionsDAOImpl(SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}

	/**
	 * Query - findTableExpansionsByPrimaryKey
	 *
	 */
	@Transactional
	public TableExpansions findTableExpansionsByPrimaryKey(Integer courtId, String recordType, String columnName, String key) throws DataAccessException {

		try {
			Query query = createNamedQuery("findTableExpansionsByPrimaryKey");
			query.setInteger("courtId", courtId);
			query.setString("recordType", recordType);
			query.setString("columnName", columnName);			
			query.setString("key", key);
			return (com.trgr.dockets.core.entity.TableExpansions) query.uniqueResult();
		} catch (NonUniqueObjectException nre) {
			return null;
		}
	}

	/**
	 * Query - findTableExpansionsByCourtId
	 *
	 */
	@Transactional
	public List<TableExpansions> findTableExpansionsByCourtId(Integer courtId) throws DataAccessException {
		Query query = createNamedQuery("findTableExpansionsByCourtId");
		query.setInteger("courtId", courtId);
		@SuppressWarnings("unchecked")
		List<TableExpansions> tableExpansionList = query.list();
		return tableExpansionList;
	}

	/**
	 * Used to determine whether or not to merge the entity or persist the entity when calling Store
	 * @see store
	 * 
	 *
	 */
	public boolean canBeMerged(TableExpansions entity) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public TableExpansions persist(TableExpansions toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void remove(TableExpansions toRemove) {
		toRemove = (TableExpansions) sessionFactory.getCurrentSession().merge(
				toRemove);
		sessionFactory.getCurrentSession().delete(toRemove);
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void update(TableExpansions toUpdate) {
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
