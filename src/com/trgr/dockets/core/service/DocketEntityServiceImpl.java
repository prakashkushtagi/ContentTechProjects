package com.trgr.dockets.core.service;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.DocketEntityDao;
import com.trgr.dockets.core.entity.DocketEntity;

/**
 * Spring service that handles CRUD requests for Docket entities
 * 
 */

@Transactional
public class DocketEntityServiceImpl implements DocketEntityService 
{

	/**
	 * DAO injected by Spring that manages Docket entities
	 * 
	 */
	private DocketEntityDao docketEntityDao;

	/**
	 * Instantiates a new DocketEntityServiceImpl.
	 *
	 */
	public DocketEntityServiceImpl() 
	{
	}

	/**
	 * Save an existing Docket entity
	 * 
	 */
	@Transactional
	public DocketEntity saveDocketEntity(DocketEntity content) 
	{
		content = docketEntityDao.persist(content);
		//docketEntityDao.flush();
		return content;
	}

	@Override
	public DocketEntity findDocketByLegacyId(String legacyId) 
	{
		
		return docketEntityDao.findDocketByLegacyId(legacyId);
	}

	/**
	 * @return the docketEntityDao
	 */
	public DocketEntityDao getDocketEntityDao()
	{
		return docketEntityDao;
	}

	/**
	 * @param docketEntityDao the docketEntityDao to set
	 */
	public void setDocketEntityDao(DocketEntityDao docketEntityDao) 
	{
		this.docketEntityDao = docketEntityDao;
	}
		
}
