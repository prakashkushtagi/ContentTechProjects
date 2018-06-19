package com.trgr.dockets.core.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class BaseDaoImpl implements BaseDao {
	
	private SessionFactory sessionFactory;

	public BaseDaoImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected Criteria createCriteria(final Class<?> entityClass) {
		return getCurrentSession().createCriteria(entityClass);
	}

	@Override
	public <T> void delete(final T entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	public <T> void deleteAndFlush(final T entity) {
		delete(entity);
		getCurrentSession().flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public <T> List<T> findAllEntities(final Class<T> entityClass) {
		final Criteria criteria = createCriteria(entityClass);
		final List<T> entities = criteria.list();
		return entities;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T findEntityByField(final Class<T> entityClass, final String fieldName, final Object fieldValue) {
		final Criteria criteria = createCriteria(entityClass);
		criteria.add(Restrictions.eq(fieldName,fieldValue));
		return (T)criteria.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T findEntityById(final Class<T> entityClass, final Serializable id) {
		return (T) getCurrentSession().get(entityClass, id);
	}

	@Override
	@Transactional
	public <T> void save(T entity) {
		getCurrentSession().saveOrUpdate(entity);
	}	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public final <T> T merge(final T entity) {
		return (T)getCurrentSession().merge(entity);
	}
	
	@Transactional
	public final <T> void update(final T entity) {
		getCurrentSession().update(entity);
	}
	
	@Transactional
	public final <T> void mergeAndUpdate(final T entity) {
		update(merge(entity));
	}
	
	@Override
	public void clearAndFlush() {
		final Session session = getCurrentSession();
		session.flush();
		session.clear();
	}

}
