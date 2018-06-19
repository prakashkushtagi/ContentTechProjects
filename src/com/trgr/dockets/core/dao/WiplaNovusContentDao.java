package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.WiplaNovusContent;

public interface WiplaNovusContentDao extends BaseDao {

	WiplaNovusContent findContentByLegacyId(final String legacyId);

	@Override
	<T> T findEntityByField(final Class<T> entityClass, final String fieldName, final Object fieldValue);
}
