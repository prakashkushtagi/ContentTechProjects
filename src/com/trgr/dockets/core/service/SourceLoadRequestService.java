/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.SourceLoadRequest;
import com.trgr.dockets.core.exception.SourceContentLoaderException;


/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 *
 */
@Deprecated
public interface SourceLoadRequestService {
	


	public Long addSourceLoadRequest(SourceLoadRequest request) throws SourceContentLoaderException;
	
	public void deleteSourceLoadRequest(long requestId);

	public SourceLoadRequest findSourceLoadRequestById(long requestId) throws DataAccessException;

	public Long updateSourceLoadRequest(long request);
	



}
