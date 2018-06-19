/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.TableExpansions;

/**
 * DAO to manage TableExpansions entities.
 * 
 */
public interface TableExpansionsDAO  {

	/**
	 * Query - findTableExpansionsByPrimaryKey
	 *
	 */
	public TableExpansions findTableExpansionsByPrimaryKey(Integer courtId, String recordType, String columnName, String key) throws DataAccessException;

	/**
	 * Query - findTableExpansionsByCourtId
	 *
	 */
	public List<TableExpansions> findTableExpansionsByCourtId(Integer courtId) throws DataAccessException;

	public TableExpansions persist(TableExpansions tableExpansions);

	public void remove(TableExpansions tableExpansions);

}