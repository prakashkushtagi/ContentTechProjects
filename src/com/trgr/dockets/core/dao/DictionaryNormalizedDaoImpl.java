/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.DictionaryNormalized;
import com.trgr.dockets.core.entity.DictionaryNormalizedKey;

@Transactional
public class DictionaryNormalizedDaoImpl implements DictionaryNormalizedDao {

	private SessionFactory sessionFactory;

	public DictionaryNormalizedDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public DictionaryNormalizedDaoImpl(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public DictionaryNormalized findNormalizedNamesByPrimaryKey(DictionaryNormalizedKey primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (DictionaryNormalized) session.get(DictionaryNormalized.class, primaryKey);
	}

	@Transactional
	public void persist(DictionaryNormalized dictNorm) {
		sessionFactory.getCurrentSession().save(dictNorm);
	}

}
