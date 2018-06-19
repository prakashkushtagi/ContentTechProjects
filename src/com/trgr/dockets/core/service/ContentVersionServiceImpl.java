package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.ContentVersionDao;
import com.trgr.dockets.core.entity.ContentVersion;

/**
 * Spring service that handles CRUD requests for ContentVersion entities
 * 
 */

@Transactional
public class ContentVersionServiceImpl implements ContentVersionService 
{

	/**
	 * DAO injected by Spring that manages ContentVersion entities
	 * 
	 */
	private ContentVersionDao contentVersionDao;

	/**
	 * Instantiates a new ContentServiceImpl.
	 *
	 */
	public ContentVersionServiceImpl() 
	{
	}

	/**
	 * Save an existing ContentVersion entity
	 * 
	 */
	@Transactional
	public ContentVersion saveContentVersion(ContentVersion contentVersion) 
	{
		contentVersion = contentVersionDao.persist(contentVersion);
		//contentVersionDao.flush();
		return contentVersion;
	}
	
	@Transactional
	public ContentVersion findContentVersionByUuidAndVersion(String contentUuid, Long version){
		return contentVersionDao.findContentVersionByUuidAndVersion(contentUuid, version);
	}

	@Override
	public List<ContentVersion> findContentVersionByUuid(String contentUuid) 
	{
		
		return contentVersionDao.findContentVersionByUuid(contentUuid);
	}

	/**
	 * @return the contentVersionDao
	 */
	public ContentVersionDao getContentVersionDao() {
		return contentVersionDao;
	}

	/**
	 * @param contentVersionDao the contentVersionDao to set
	 */
	public void setContentVersionDao(ContentVersionDao contentDao) {
		this.contentVersionDao = contentDao;
	}
		
}
