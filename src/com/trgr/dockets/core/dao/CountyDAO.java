/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.County;

/**
 * DAO to manage County entities.
 * 
 */
public interface CountyDAO  {

	/**
	 * Query - findCountyByPrimaryKey
	 *
	 */
	public County findCountyByPrimaryKey(Integer countyId) throws DataAccessException;

	/**
	 * Query - findCountyByCourtIdAndCountyCode
	 *
	 */
	public County findCountyByCourtIdAndCountyCode(Integer courtId, Integer countyCode) throws DataAccessException;

	public County findCountyByColKey(String colKey) throws DataAccessException;

	public County persist(County county);

	public void remove(County county);

	public County findCountyByNameAndCourt(String countyName, Integer courtId);
	
	public County findCountyByName(String countyName);

	public List<County> loadCounty();

	public County findCountyByCourtId(Integer courtId); 


}