/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.Advocate;

/**
 * DAO to manage Advocate entities.
 * 
 */
public interface AdvocateDAO {

	/**
	 * Query - findAdvocateByCourtId
	 *
	 */
	public List<Advocate> findAdvocateByCountyId(Integer countyId) throws DataAccessException;

	/**
	 * Query - findAdvocateByPrimaryKey
	 *
	 */
	public Advocate findAdvocateByPrimaryKey(Integer countyId, Integer type, String id) throws DataAccessException;

	public Advocate persist(Advocate advocate);

	public void remove(Advocate advocate);

}