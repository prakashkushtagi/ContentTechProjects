/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.ErrorLogDao;
import com.trgr.dockets.core.entity.ErrorLog;

/**
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com) 
 *
 */
@Deprecated
public class ErrorLogServiceImpl implements ErrorLogService{
	
	private ErrorLogDao errorLogDao;

	public ErrorLogServiceImpl(ErrorLogDao errorLogDao) {
		this.errorLogDao = errorLogDao;
	}

	@Override
	@Transactional
	public Long addErrorLog(ErrorLog errorLog){
		Long errorId;
		errorId = errorLogDao.saveErrorLog(errorLog);
		return errorId; 
	}

	@Override
	public void deleteErrorLog(long errorLogId) {
		this.errorLogDao.deleteErrorLog(errorLogId);
	}

	@Override
	public ErrorLog findErrorLogById(long errorLogId)
			throws DataAccessException {
		ErrorLog errorLog = this.errorLogDao.findByPrimaryKey(errorLogId);
		return errorLog;
	}

	@Override
	public Long saveErrorLog(ErrorLog errorLog) {
		Long errorLogId = errorLogDao.saveErrorLog(errorLog);
		return errorLogId;
	}

	@Override
	public List<ErrorLog> findErrorLogByRequestId(long requestId) {

		List<ErrorLog> errorLogList = this.errorLogDao.findErrorLogByRequestId(requestId);
		
		return errorLogList;
	}
}
