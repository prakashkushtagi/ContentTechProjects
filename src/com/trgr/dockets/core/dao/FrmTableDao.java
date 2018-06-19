/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.FrmTable;

/**
 * DAO to manage FrmTable entities.
 * 
 */
public interface FrmTableDao  {

	/**
	 * Query - findFrmTableByAttorneyCode
	 *
	 */
	public List<FrmTable> findFrmTableByAttorneyCode(String attorneyCode) throws DataAccessException;

	/**
	 * Query - findFrmTableByWestlawClusterName
	 *
	 */
	public List<FrmTable> findFrmTableByWestlawClusterName(String westlawClusterName_1) throws DataAccessException;

	/**
	 * Query - findFrmTableByPrimaryKey
	 *
	 */
	public FrmTable findFrmTableByPrimaryKey(String countyCode_1, String attorneyCode_2) throws DataAccessException;

	/**
	 * Query - findFrmTableByFrmTableText
	 *
	 */
	public List<FrmTable> findFrmTableByFrmTableText(String frmTableText) throws DataAccessException;

	/**
	 * Query - findFrmTableByCountyCode
	 *
	 */
	public List<FrmTable> findFrmTableByCountyCode(String countyCode_2) throws DataAccessException;

	public FrmTable persist(FrmTable frmTable);
	public void remove(FrmTable frmTable);

}