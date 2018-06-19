package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.dao.ContentVersionDao;
import com.trgr.dockets.core.entity.ContentVersion;

/**
 * Spring service that handles CRUD requests for ContentVersion entities
 * 
 */
public interface ContentVersionService 
{

	/**
	 * Save an existing ContentVersion entity
	 * 
	 */
	public ContentVersion saveContentVersion(ContentVersion contentVersion);

	public List<ContentVersion> findContentVersionByUuid(String contentUuid);
	
	public ContentVersion findContentVersionByUuidAndVersion(String contentUuid, Long version);

	public void setContentVersionDao(ContentVersionDao contentDao);
}