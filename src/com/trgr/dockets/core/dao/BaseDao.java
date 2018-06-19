package com.trgr.dockets.core.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao {
	
	public <T> void delete(T entity);
	
	public <T> void deleteAndFlush(T entity);
	
	public <T> List<T> findAllEntities(final Class<T> entityClass);

	public <T> T findEntityById(final Class<T> entityClass, final Serializable id);

	public <T> T findEntityByField(final Class<T> entityClass, final String fieldName, final Object fieldValue);

	public <T> void save(T entity);

	public void clearAndFlush();
}
