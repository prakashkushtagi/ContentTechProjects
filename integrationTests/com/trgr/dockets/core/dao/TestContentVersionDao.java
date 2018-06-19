package com.trgr.dockets.core.dao;

import java.util.Collection;
import java.util.List;

import com.trgr.dockets.core.entity.ContentVersion;

public interface TestContentVersionDao {
	
	public List<ContentVersion> findContentVersionByUuid(String contentUuid);

	<T> void delete(Collection<T> entities);

	<T> void delete(T cv);
}
