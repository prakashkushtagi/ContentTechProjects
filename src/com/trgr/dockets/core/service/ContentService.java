package com.trgr.dockets.core.service;

import com.trgr.dockets.core.dao.ContentDao;
import com.trgr.dockets.core.entity.Content;

/**
 * Spring service that handles CRUD requests for Content entities
 * 
 */
public interface ContentService 
{

	/**
	 * Save an existing Content entity
	 * 
	 */
	public Content saveContent(Content content);

	public Content findContentByUuid(String contentUuid);

	public void setContentDao(ContentDao contentDao);
}