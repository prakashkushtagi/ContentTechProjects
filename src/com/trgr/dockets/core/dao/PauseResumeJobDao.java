/* Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.DispatcherWorkPaused;

/**
 * DAO to manage County entities.
 * 
 */
public interface PauseResumeJobDao  {

	/**
	 * Save an PauseResumeJobService
	 * 
	 */
	public DispatcherWorkPaused savePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused);

	/**
	 * Delete an existing PauseResumeJob
	 * 
	 */
	public void deletePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused);

	/**
	 * 
	 * @param workId
	 * @return
	 */
	public DispatcherWorkPaused findPauseResumeJobByWorkId(Long workId);
	
	/**
	 * 
	 * @param jobExecutionId
	 * @return
	 */
	
	public DispatcherWorkPaused findPauseResumeJobByJobExecutionId(Long jobExecutionId);
	
	/**
	 *
	 * @return
	 */
	
	public List<DispatcherWorkPaused> findPauseResumeJobByReadyToResumeStatus(String status);

	public void updatePauseResumeJobList(List<DispatcherWorkPaused> listToUnpause);

	public void updatePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused);

}