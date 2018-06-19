/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.ErrorLog;

/**
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com) 
 *
 */
@Deprecated
public interface ErrorLogService {
	

	/**
	 * @param errorLog
	 * @return
	 */
	public Long addErrorLog(ErrorLog errorLog);
	
	/**
	 * @param errorLogId
	 */
	public void deleteErrorLog(long errorLogId);

	/**
	 * @param errorLogId
	 * @return
	 * @throws DataAccessException
	 */
	public ErrorLog findErrorLogById(long errorLogId) throws DataAccessException;

	/**
	 * @param errorLog
	 * @return
	 */
	public Long saveErrorLog(ErrorLog errorLog);

	/**
	 * @param requestId
	 * @return
	 */
	public List<ErrorLog> findErrorLogByRequestId(long requestId);

}
