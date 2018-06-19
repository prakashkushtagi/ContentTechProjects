package com.trgr.dockets.core.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;

public class TestContentVersionDaoImpl implements TestContentVersionDao{
	private SessionFactory sessionFactory;

	public TestContentVersionDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<ContentVersion> findContentVersionByUuid(String contentUuid) 
	{
		 final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ContentVersion.class);
			criteria.add(Restrictions.eq(Content.CONTENT_UUID, contentUuid));
	        return (List<ContentVersion>) criteria.list();
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
