package com.trgr.dockets.core.service;

import com.trgr.dockets.core.entity.Advocate;

/**
 * Spring service that handles CRUD requests for Advocate entities
 * 
 */
public interface AdvocateService {

	/**
	 * Save an existing Advocate entity
	 * 
	 */
	public void saveAdvocate(Advocate advocate);

	/**
	 */
	public Advocate findAdvocateByPrimaryKey(Integer countyId, Integer type, String id);

	/**
	 * Delete an existing Advocate entity
	 * 
	 */
	public void deleteAdvocate(Advocate advocate);
}