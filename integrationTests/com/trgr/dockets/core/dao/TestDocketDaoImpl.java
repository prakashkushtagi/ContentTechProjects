package com.trgr.dockets.core.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketVersion;

public class TestDocketDaoImpl implements TestDocketDao {
	private SessionFactory sessionFactory;

	public TestDocketDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<DocketVersion> findAllDocketVersionsForLegacyId(String legacyId){
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersion.class);
		criteria.add(Restrictions.eq("primaryKey.legacyId", legacyId));
		return (List<DocketVersion>) criteria.list();		
	}

	@Override
	public List<DocketHistory> findAllDocketHistoryForLegacyId(String legacyId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketHistory.class);
		criteria.add(Restrictions.eq("legacyId", legacyId));
		return (List<DocketHistory>) criteria.list();
	}
	
	@Override
	public List<DocketEntity> findAllDocketEntitiesForLegacyId(String legacyId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketEntity.class);
		criteria.add(Restrictions.eq("primaryKey", legacyId));
		return (List<DocketEntity>) criteria.list();
	}

	@Override
	public <T> void delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}
	@Override
	public <T> void delete(Collection<T> entities) {
		for (T entity : entities) {
			delete(entity);
		}
	}
}
