package com.trgr.dockets.core.service;

import com.trgr.dockets.core.entity.Dictionary;

/**
 * Spring service that handles CRUD requests for Dictionary entities
 * 
 */
public interface DictionaryService {

	/**
	 */
	public Dictionary findDictionaryByPrimaryKey(Integer countyId, Integer type, String key);

	/**
	 * Save an existing Dictionary entity
	 * 
	 */
	public void saveDictionary(Dictionary dictionary);

	/**
	 * Delete an existing Dictionary entity
	 * 
	 */
	public void deleteDictionary(Dictionary dictionary);
}