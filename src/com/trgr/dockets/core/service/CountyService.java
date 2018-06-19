package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.entity.County;

/**
 * Spring service that handles CRUD requests for County entities
 * 
 */
public interface CountyService {

	public County findCountyByPrimaryKey(Integer countyId);

	/**
	 * Query - findCountyByCourtIdAndCountyCode
	 *
	 */
	public County findCountyByCourtIdAndCountyCode(Integer courtId, Integer countyCode);

	public County findCountyByColKey(String colKey);

	/**
	 * Save an existing County entity
	 * 
	 */
	public void saveCounty(County county);

	/**
	 * Delete an existing County entity
	 * 
	 */
	public void deleteCounty(County county);

	/**
	 * @param subDivision/countyName
	 * @param countyId
	 * @return
	 */
	public County findCountyByNameAndCourt(String countyName, Integer courtId);
	
	/**
	 * @param subDivision/countyName
	 * @return
	 */
	public County findCountyByName(String countyName);

	public List<County> loadCounty();

	public County findCountyByCourtId(Integer courtId);
	
	
}