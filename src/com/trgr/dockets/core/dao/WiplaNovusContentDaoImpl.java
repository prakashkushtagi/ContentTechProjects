package com.trgr.dockets.core.dao;

import static com.trgr.dockets.core.entity.WiplaNovusContent.LEGACY_ID;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.WiplaNovusContent;

public class WiplaNovusContentDaoImpl extends BaseDaoImpl implements WiplaNovusContentDao {
	
	public WiplaNovusContentDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public WiplaNovusContent findContentByLegacyId(final String legacyId) {
		return findEntityByField(WiplaNovusContent.class, LEGACY_ID, legacyId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T findEntityByField(final Class<T> entityClass, final String fieldName, final Object fieldValue) {
		final Criteria criteria = createCriteria(entityClass);
		criteria.add(Restrictions.eq(fieldName,fieldValue));
		return (T)criteria.uniqueResult();
	}
}
