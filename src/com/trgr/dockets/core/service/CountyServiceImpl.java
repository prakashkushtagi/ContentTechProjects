/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.CountyDAO;
import com.trgr.dockets.core.entity.County;

/**
 * Spring service that handles CRUD requests for County entities
 * 
 */

@Transactional
public class CountyServiceImpl implements CountyService {

	/**
	 * DAO injected by Spring that manages County entities
	 * 
	 */
	@Autowired
	private CountyDAO countyDAO;

	/**
	 * Instantiates a new CountyServiceImpl.
	 *
	 */
	public CountyServiceImpl() {
	}

	/**
	 */
	@Transactional
	public County findCountyByPrimaryKey(Integer countyId) {
		return countyDAO.findCountyByPrimaryKey(countyId);
	}

	/**
	 * Query - findCountyByCourtIdAndCountyCode
	 *
	 */
	@Transactional
	public County findCountyByCourtIdAndCountyCode(Integer courtId, Integer countyCode) throws DataAccessException {
		return countyDAO.findCountyByCourtIdAndCountyCode(courtId, countyCode);
	}
	
	@Transactional
	@Override
	public County findCountyByCourtId(Integer courtId){
		return countyDAO.findCountyByCourtId(courtId);
	}

	@Transactional
	public County findCountyByColKey(String colKey) throws DataAccessException {
		return countyDAO.findCountyByColKey(colKey);
	}

	/**
	 * Save an existing County entity
	 * 
	 */
	@Transactional
	public void saveCounty(County county) {
		County existingCounty = countyDAO.findCountyByPrimaryKey(county.getCountyId());
	
		if (existingCounty != null) {
			if (existingCounty != county) {
				existingCounty.setAbbreviation(county.getAbbreviation());
				existingCounty.setColKey(county.getColKey());
				existingCounty.setCountyCode(county.getCountyCode());
				existingCounty.setCountyId(county.getCountyId());
				existingCounty.setCourtId(county.getCourtId());
				existingCounty.setCourtNorm(county.getCourtNorm());
				existingCounty.setDistrictId(county.getDistrictId());
				existingCounty.setLuDate(county.getLuDate());
				existingCounty.setName(county.getName());
			}
			county = countyDAO.persist(existingCounty);
		} else {
			county = countyDAO.persist(county);
		}
		
	}

	/**
	 * Delete an existing County entity
	 * 
	 */
	@Transactional
	public void deleteCounty(County county) {
		countyDAO.remove(county);
	}
	
	
	
	@Override
	public County findCountyByNameAndCourt(String countyName, Integer courtId)
	{
		return countyDAO.findCountyByNameAndCourt(countyName, courtId);
	}
	
	@Override
	public List<County> loadCounty()
	{
		return countyDAO.loadCounty();
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.CountyService#findCountyByName(java.lang.String)
	 */
	public County findCountyByName(String countyName){
		return countyDAO.findCountyByName(countyName);
	}

	@Required
	public void setCountyDAO(CountyDAO countyDAO) {
		this.countyDAO = countyDAO;
	}
}
