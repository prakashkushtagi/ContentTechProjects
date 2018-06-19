/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.DocketVersionRelHistoryKey;


/**
 * DAO to manage DocketHistory entities.
 * 
 */

@Transactional
public class DocketHistoryDaoImpl implements  DocketHistoryDao {
	
	private SessionFactory sessionFactory;
	
	public DocketHistoryDaoImpl() {
		super();
	}
	public DocketHistoryDaoImpl(SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}

	@Override
	public DocketHistory persist(DocketHistory toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<DocketHistory> findDocketHistoryByLegacyId(String legacyId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketHistory.class);
		criteria.add(Restrictions.eq(DocketHistory.PROPERTY_LEGACY_ID, legacyId));
        return (List<DocketHistory>) criteria.list();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<DocketHistory> findDocketHistoryByLegacyIdType(String legacyId, DocketHistoryTypeEnum type)  {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketHistory.class);
		criteria.add(Restrictions.eq(DocketHistory.PROPERTY_LEGACY_ID, legacyId));
		criteria.add(Restrictions.eq(DocketHistory.PROPERTY_TYPE, type.name()));
        return (List<DocketHistory>) criteria.list();
	}

	
	@Override
	public DocketVersionRel persist(DocketVersionRel toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}
	@Override
	public DocketVersionRel findDocketVersionRel(String relId)  {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersionRel.class);
		criteria.add(Restrictions.eq(DocketVersionRel.REL_ID, relId));
        return (DocketVersionRel) criteria.uniqueResult();
	}

	@Override
	public DocketVersionRel findByLatestVersionRelBylegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType) {
		DocketVersionRel docketVersionRel = null;
		
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersionRel.class);
		criteria.add(Restrictions.eq(DocketVersionRel.LEGACY_ID, legacyId));
		criteria.add(Restrictions.eq(DocketVersionRel.REL_TYPE, docketVersionRelType.name()));
		criteria.addOrder(Order.desc(DocketVersionRel.REL_TIMESTAMP));
		criteria.setMaxResults(1);
		docketVersionRel = (DocketVersionRel) criteria.uniqueResult();
		return docketVersionRel;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<DocketVersionRel> findDocketVersionRelsByLegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType)  {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersionRel.class);
		criteria.add(Restrictions.eq(DocketVersionRel.LEGACY_ID, legacyId));
		criteria.add(Restrictions.eq(DocketVersionRel.REL_TYPE, docketVersionRelType.name()));
        return (List<DocketVersionRel>) criteria.list();
	}


	public DocketVersionRelHistory persist(DocketVersionRelHistory toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}

	@Override
	public DocketVersionRelHistory findDocketVersionRelHistoryByPrimaryKey(DocketVersionRelHistoryKey pk) {
		return (DocketVersionRelHistory) sessionFactory.getCurrentSession().get(DocketVersionRelHistory.class, pk);
	}

	@Override
	public DocketVersionRelHistory findDocketVersionRelHistoryByRelId(String relId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersionRelHistory.class);
		criteria.add(Restrictions.eq("primaryKey.relId", relId));
        return (DocketVersionRelHistory) criteria.uniqueResult();
	}

}



