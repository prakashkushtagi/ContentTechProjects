package com.trgr.dockets.core.service;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.ContentDao;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * Spring service that handles CRUD requests for Content entities
 * 
 */

@Transactional
public class ContentServiceImpl implements ContentService 
{

	/**
	 * DAO injected by Spring that manages Content entities
	 * 
	 */
	private ContentDao contentDao;

	/**
	 * Instantiates a new ContentServiceImpl.
	 *
	 */
	public ContentServiceImpl() 
	{
	}

	/**
	 * Save an existing Content entity
	 * 
	 */
	@Transactional
	public Content saveContent(Content content) 
	{
		if(null==content.getContentUuid())
		{
			UUID contentUuid = UUIDGenerator.createUuid();
			content.setContentUuid(contentUuid.toString());
		}
		content = contentDao.persist(content);
		//contentDao.flush();
		return content;
	}

	@Override
	public Content findContentByUuid(String contentUuid) 
	{
		
		return contentDao.findContentByUuid(contentUuid);
	}

	/**
	 * @return the contentDao
	 */
	public ContentDao getContentDao() {
		return contentDao;
	}

	/**
	 * @param contentDao the contentDao to set
	 */
	public void setContentDao(ContentDao contentDao) {
		this.contentDao = contentDao;
	}
		
}
