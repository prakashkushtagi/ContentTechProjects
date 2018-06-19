package com.trgr.dockets.core.service;

import com.trgr.dockets.core.entity.TableExpansions;

/**
 * Spring service that handles CRUD requests for TableExpansions entities
 * 
 */
public interface TableExpansionsService {

	/**
	 */
	public TableExpansions findTableExpansionsByPrimaryKey(Integer courtId, String recordType, String columnName, String key);

	/**
	 * Save an existing TableExpansions entity
	 * 
	 */
	public void saveTableExpansions(TableExpansions tableexpansions);

	/**
	 * Delete an existing TableExpansions entity
	 * 
	 */
	public void deleteTableExpansions(TableExpansions tableexpansions_1);
}