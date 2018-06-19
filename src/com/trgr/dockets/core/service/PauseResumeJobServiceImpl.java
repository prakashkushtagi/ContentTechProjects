/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.PauseResumeJobDao;
import com.trgr.dockets.core.entity.DispatcherWorkPaused;

/**
 * Spring service that handles CRUD requests for County entities
 * 
 */

@Transactional
public class PauseResumeJobServiceImpl implements PauseResumeJobService {

	/**
	 * DAO injected by Spring that manages County entities
	 * 
	 */

	private PauseResumeJobDao pauseResumeJobDao;

	/**
	 * Instantiates a new CountyServiceImpl.
	 *
	 */
	public PauseResumeJobServiceImpl(PauseResumeJobDao pauseResumeJobDao) 
	{
		this.pauseResumeJobDao = pauseResumeJobDao;
	}

	
	public void setPauseResumeJobDao(PauseResumeJobDao pauseResumeJobDao) {
		this.pauseResumeJobDao = pauseResumeJobDao;
	}


	@Override
	public void savePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused) {
		this.pauseResumeJobDao.savePauseResumeJob(dispatcherWorkPaused);
		
	}

	@Override
	public void updatePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused) {
		this.pauseResumeJobDao.updatePauseResumeJob(dispatcherWorkPaused);
		
	}

	@Override
	public void deletePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused) {
		this.pauseResumeJobDao.deletePauseResumeJob(dispatcherWorkPaused);
	}


	@Override
	public DispatcherWorkPaused findPauseResumeJobByWorkId(Long workId) {
		return null;
	}


	@Override
	public DispatcherWorkPaused findPauseResumeJobByJobExecutionId(Long jobExecutionId) {
		return null;
	}


	@Override
	public List<DispatcherWorkPaused> findPauseResumeJobByReadyToResumeStatus(String status){
		return this.pauseResumeJobDao.findPauseResumeJobByReadyToResumeStatus(status);
	}


	@Override
	public void updatePauseResumeJobList(List<DispatcherWorkPaused> listToUnpause) 
	{
		this.pauseResumeJobDao.updatePauseResumeJobList(listToUnpause);
	}
}
