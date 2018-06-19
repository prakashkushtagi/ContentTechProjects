/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;


import java.util.List;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.FrmTable;
import com.trgr.dockets.core.entity.FrmTablePK;

/**
 * DAO to manage FrmTable entities.
 * 
 */

@Transactional
public class FrmTableDaoImpl implements
		FrmTableDao {

	
	private static final Integer DEFAULT_FIRST_RESULT_INDEX = 1000;
	private SessionFactory sessionFactory;
	
	public FrmTableDaoImpl(SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}
	
	
	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public FrmTable persist(FrmTable toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void remove(FrmTable toRemove) {
		toRemove = (FrmTable) sessionFactory.getCurrentSession().merge(
				toRemove);
		sessionFactory.getCurrentSession().delete(toRemove);
	}
	
	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void update(FrmTable toUpdate) {
		sessionFactory.getCurrentSession().update(toUpdate);
	}

	public FrmTable findFrmTableByPrimaryKey(FrmTablePK pk) {
		Session session = sessionFactory.getCurrentSession();
		return (FrmTable) session.get(FrmTable.class, pk);
	}

	/**
	 * Query - findFrmTableByPrimaryKey
	 *
	 */
	@Transactional
	public FrmTable findFrmTableByPrimaryKey(String countyCode, String attorneyCode) throws DataAccessException {

		return findFrmTableByPrimaryKey(countyCode, attorneyCode, -1, -1);
	}

	/**
	 * Query - findFrmTableByPrimaryKey
	 *
	 */

	@Transactional
	public FrmTable findFrmTableByPrimaryKey(String countyCode, String attorneyCode, int startResult, int maxRows) throws DataAccessException {
		try {
			Query query = createNamedQuery("findFrmTableByPrimaryKey");
			query.setString("countyCode", countyCode);
			query.setString("attorneyCode", attorneyCode);
			return (com.trgr.dockets.core.entity.FrmTable) query.uniqueResult();
		} catch (NonUniqueObjectException nre) {
			return null;
		}
	}

	/**
	 * Query - findFrmTableByFrmTableTextContaining
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByFrmTableTextContaining(String frmTableText) throws DataAccessException {

		return findFrmTableByFrmTableTextContaining(frmTableText, -1, -1);
	}

	/**
	 * Query - findFrmTableByFrmTableTextContaining
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByFrmTableTextContaining(String frmTableText, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByFrmTableTextContaining", startResult, maxRows, frmTableText);
		query.setString("frmTableText", frmTableText);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findFrmTableByAttorneyCode
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByAttorneyCode(String attorneyCode) throws DataAccessException {

		return findFrmTableByAttorneyCode(attorneyCode, -1, -1);
	}

	/**
	 * Query - findFrmTableByAttorneyCode
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByAttorneyCode(String attorneyCode, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByAttorneyCode", startResult, maxRows, attorneyCode);
		query.setString("attorneyCode", attorneyCode);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findFrmTableByWestlawClusterNameContaining
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByWestlawClusterNameContaining(String westlawClusterName) throws DataAccessException {

		return findFrmTableByWestlawClusterNameContaining(westlawClusterName, -1, -1);
	}

	/**
	 * Query - findFrmTableByWestlawClusterNameContaining
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByWestlawClusterNameContaining(String westlawClusterName, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByWestlawClusterNameContaining", startResult, maxRows, westlawClusterName);
		query.setString("westlawClusterName", westlawClusterName);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findFrmTableByCountyCode
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByCountyCode(String countyCode) throws DataAccessException {

		return findFrmTableByCountyCode(countyCode, -1, -1);
	}

	/**
	 * Query - findFrmTableByCountyCode
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByCountyCode(String countyCode, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByCountyCode", startResult, maxRows, countyCode);
		query.setString("countyCode", countyCode);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findAllFrmTables
	 *
	 */
	@Transactional
	public List<FrmTable> findAllFrmTables() throws DataAccessException {

		return findAllFrmTables(-1, -1);
	}

	/**
	 * Query - findAllFrmTables
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findAllFrmTables(int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findAllFrmTables", startResult, maxRows);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findFrmTableByAttorneyCodeContaining
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByAttorneyCodeContaining(String attorneyCode) throws DataAccessException {

		return findFrmTableByAttorneyCodeContaining(attorneyCode, -1, -1);
	}

	/**
	 * Query - findFrmTableByAttorneyCodeContaining
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByAttorneyCodeContaining(String attorneyCode, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByAttorneyCodeContaining", startResult, maxRows, attorneyCode);
		query.setString("attorneyCode", attorneyCode);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findFrmTableByCountyCodeContaining
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByCountyCodeContaining(String countyCode) throws DataAccessException {

		return findFrmTableByCountyCodeContaining(countyCode, -1, -1);
	}

	/**
	 * Query - findFrmTableByCountyCodeContaining
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByCountyCodeContaining(String countyCode, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByCountyCodeContaining", startResult, maxRows, countyCode);
		query.setString("countyCode", countyCode);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findFrmTableByWestlawClusterName
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByWestlawClusterName(String westlawClusterName) throws DataAccessException {

		return findFrmTableByWestlawClusterName(westlawClusterName, -1, -1);
	}

	/**
	 * Query - findFrmTableByWestlawClusterName
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByWestlawClusterName(String westlawClusterName, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByWestlawClusterName", startResult, maxRows, westlawClusterName);
		query.setString("westlawClusterName", westlawClusterName);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Query - findFrmTableByFrmTableText
	 *
	 */
	@Transactional
	public List<FrmTable> findFrmTableByFrmTableText(String frmTableText) throws DataAccessException {

		return findFrmTableByFrmTableText(frmTableText, -1, -1);
	}

	/**
	 * Query - findFrmTableByFrmTableText
	 *
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<FrmTable> findFrmTableByFrmTableText(String frmTableText, int startResult, int maxRows) throws DataAccessException {
		Query query = createNamedQuery("findFrmTableByFrmTableText", startResult, maxRows, frmTableText);
		query.setString("frmTableText", frmTableText);
		List<FrmTable> frmList = query.list();
		return frmList;
	}

	/**
	 * Used to determine whether or not to merge the entity or persist the entity when calling Store
	 * @see store
	 * 
	 *
	 */
	public boolean canBeMerged(FrmTable entity) {
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


	@Transactional
	public Query createNamedQuery(String queryName, Integer firstResult, Integer maxResults, Object... parameters)  {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(
				queryName);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i, parameters[i]);
			}
		}
		
		query.setFirstResult(firstResult == null || firstResult < 0 ? DEFAULT_FIRST_RESULT_INDEX : firstResult);
		if (maxResults != null && maxResults > 0)
			query.setMaxResults(maxResults);
	
		return query;
	}
}
