/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.FrmTableDao;
import com.trgr.dockets.core.entity.FrmTable;

/**
 * Spring service that handles CRUD requests for FrmTable entities
 * 
 */

@Transactional
public class FrmTableServiceImpl implements FrmTableService {

	/**
	 * DAO injected by Spring that manages FrmTable entities
	 * 
	 */
	@Autowired
	private FrmTableDao frmTableDAO;

	/**
	 * Instantiates a new FrmTableServiceImpl.
	 *
	 */
	public FrmTableServiceImpl() {
	}

	/**
	 * Save an existing FrmTable entity
	 * 
	 */
	@Transactional
	public void saveFrmTable(FrmTable frmtable) {
		FrmTable existingFrmTable = frmTableDAO.findFrmTableByPrimaryKey(frmtable.getCountyCode(), frmtable.getAttorneyCode());

		if (existingFrmTable != null) {
			if (existingFrmTable != frmtable) {
				existingFrmTable.setCountyCode(frmtable.getCountyCode());
				existingFrmTable.setAttorneyCode(frmtable.getAttorneyCode());
				existingFrmTable.setWestlawClusterName(frmtable.getWestlawClusterName());
				existingFrmTable.setFrmTableText(frmtable.getFrmTableText());
			}
			frmtable = frmTableDAO.persist(existingFrmTable);
		} else {
			frmtable = frmTableDAO.persist(frmtable);
		}

	}

	/**
	 */
	@Transactional
	public FrmTable findFrmTableByPrimaryKey(String countyCode, String attorneyCode) {
		return frmTableDAO.findFrmTableByPrimaryKey(countyCode, attorneyCode);
	}

	/**
	 * Delete an existing FrmTable entity
	 * 
	 */
	@Transactional
	public void deleteFrmTable(FrmTable frmtable) {
		frmTableDAO.remove(frmtable);
	}

	@Required
	public void setFrmTableDAO(FrmTableDao frmTableDAO) {
		this.frmTableDAO = frmTableDAO;
	}
}
