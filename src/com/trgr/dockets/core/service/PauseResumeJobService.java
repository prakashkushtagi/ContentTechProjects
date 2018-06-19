package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.entity.DispatcherWorkPaused;

/**
 * Spring service that handles CRUD requests for County entities
 * 
 */
public interface PauseResumeJobService {

	/**
	 * Save an PauseResumeJobService
	 * 
	 */
	public void savePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused);
	
	/**
	 * update an PauseResumeJobService
	 * 
	 */
	public void updatePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused);


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
	
}