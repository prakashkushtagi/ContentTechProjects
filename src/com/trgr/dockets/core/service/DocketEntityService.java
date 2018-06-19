package com.trgr.dockets.core.service;

import com.trgr.dockets.core.dao.DocketEntityDao;
import com.trgr.dockets.core.entity.DocketEntity;

/**
 * Spring service that handles CRUD requests for Docket entities
 * 
 */
public interface DocketEntityService 
{

	/**
	 * Save an existing Docket entity
	 * 
	 */
	public DocketEntity saveDocketEntity(DocketEntity content);

	public DocketEntity findDocketByLegacyId(String legacyId);

	public void setDocketEntityDao(DocketEntityDao docketEntityDao);
	
}