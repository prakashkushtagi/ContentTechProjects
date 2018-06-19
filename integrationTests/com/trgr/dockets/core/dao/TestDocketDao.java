package com.trgr.dockets.core.dao;

import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketVersion;

public interface TestDocketDao {

	public List<DocketVersion> findAllDocketVersionsForLegacyId(String legacyId);
	
	public List<DocketHistory> findAllDocketHistoryForLegacyId(String legacyId);
	
	public List<DocketEntity> findAllDocketEntitiesForLegacyId(String legacyId);

	public 
	<T> void delete(T entity);

	<T> void delete(Collection<T> entities);



}
