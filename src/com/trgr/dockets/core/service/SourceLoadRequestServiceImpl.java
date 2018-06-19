/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.SourceLoadRequestDao;
import com.trgr.dockets.core.entity.SourceLoadRequest;
import com.trgr.dockets.core.exception.SourceContentLoaderException;


/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */
@Deprecated
public class SourceLoadRequestServiceImpl implements SourceLoadRequestService{
	
	private SourceLoadRequestDao sourceLoadRequestDao;
	
	public SourceLoadRequestServiceImpl() {
	}
	
	public SourceLoadRequestServiceImpl(SourceLoadRequestDao dao) {
		this.sourceLoadRequestDao = dao;
	}

	@Override
	@Transactional
	public Long addSourceLoadRequest(SourceLoadRequest sourceLoadRequest) throws SourceContentLoaderException{
		return sourceLoadRequestDao.addSourceLoadRequest(sourceLoadRequest);
	}
	
	@Override
	public void deleteSourceLoadRequest(long requestId){
		this.sourceLoadRequestDao.deleteSourceLoadRequest(requestId);
	}
	
	@Override
	public SourceLoadRequest findSourceLoadRequestById(long requestId) throws DataAccessException{
		SourceLoadRequest sourceLoadRequest = this.sourceLoadRequestDao.findSourceLoadRequestById(requestId);
		return sourceLoadRequest;
	}
	
	@Override
	public Long updateSourceLoadRequest(long request){
		//TODO
		return null;
	}
}
