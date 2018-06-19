package com.trgr.dockets.core.content.service;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.WiplaNovusContentDao;
import com.trgr.dockets.core.entity.WiplaNovusContent;

@Transactional
public final class WiplaNovusContentServiceImpl implements WiplaNovusContentService {


	/**
	 * DAO injected by Spring that manages Advocate entities
	 */
	
    private WiplaNovusContentDao wiplaNovusContentDao;
    
    public WiplaNovusContentServiceImpl(final WiplaNovusContentDao wiplaNovusContentDao) {
    	this.wiplaNovusContentDao = wiplaNovusContentDao;
    }
    
    @Override
	@Transactional(readOnly=true)
	public WiplaNovusContent findContentByLegacyId(String legacyId)
	{
		return wiplaNovusContentDao.findContentByLegacyId(legacyId);
	}
    
    @Override
	@Transactional
    public void saveOrUpdate(WiplaNovusContent wiplaNovusContent)
    {
    	wiplaNovusContent.updateTimestamp();
    	wiplaNovusContentDao.save(wiplaNovusContent);
    }
    
    @Override
	@Transactional
    public void delete(WiplaNovusContent wiplaNovusContent)
    {
    	wiplaNovusContentDao.delete(wiplaNovusContent);
    }
}
