/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.TableExpansionsDAO;
import com.trgr.dockets.core.entity.TableExpansions;

/**
 * Spring service that handles CRUD requests for TableExpansions entities
 * 
 */

@Transactional
public class TableExpansionsServiceImpl implements TableExpansionsService {

	/**
	 * DAO injected by Spring that manages TableExpansions entities
	 * 
	 */
	@Autowired
	private TableExpansionsDAO tableExpansionsDAO;

	/**
	 * Instantiates a new TableExpansionsServiceImpl.
	 *
	 */
	public TableExpansionsServiceImpl() {
	}

	/**
	 */
	@Transactional
	public TableExpansions findTableExpansionsByPrimaryKey(Integer courtId, String recordType, String columnName, String key) {
		return tableExpansionsDAO.findTableExpansionsByPrimaryKey(courtId, recordType, columnName, key);
	}

	/**
	 * Save an existing TableExpansions entity
	 * 
	 */
	@Transactional
	public void saveTableExpansions(TableExpansions tableexpansions) {
		TableExpansions existingTableExpansions = tableExpansionsDAO.findTableExpansionsByPrimaryKey(tableexpansions.getCourtId(), tableexpansions.getRecordType(), tableexpansions.getColumnName(), tableexpansions.getKey());

		if (existingTableExpansions != null) {
			if (existingTableExpansions != tableexpansions) {
				existingTableExpansions.setCourtId(tableexpansions.getCourtId());
				existingTableExpansions.setRecordType(tableexpansions.getRecordType());
				existingTableExpansions.setColumnName(tableexpansions.getColumnName());
				existingTableExpansions.setKey(tableexpansions.getKey());
				existingTableExpansions.setValue(tableexpansions.getValue());
				existingTableExpansions.setLuDate(tableexpansions.getLuDate());
			}
			tableexpansions = tableExpansionsDAO.persist(existingTableExpansions);
		} else {
			tableexpansions = tableExpansionsDAO.persist(tableexpansions);
		}

	}

	/**
	 * Delete an existing TableExpansions entity
	 * 
	 */
	@Transactional
	public void deleteTableExpansions(TableExpansions tableexpansions) {
		tableExpansionsDAO.remove(tableexpansions);
	}

	@Required
	public void setTableExpansionsDAO(TableExpansionsDAO tableExpansionsDAO) {
		this.tableExpansionsDAO = tableExpansionsDAO;
	}
}
