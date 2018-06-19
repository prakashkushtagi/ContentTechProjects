/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.trgr.dockets.core.entity.Dictionary;

/**
 * DAO to manage Dictionary entities.
 * 
 */
public interface DictionaryDAO {

	/**
	 * Query - findDictionaryByCourtId
	 *
	 */
	public List<Dictionary> findDictionaryByCountyId(Integer countyId) throws DataAccessException;

	/**
	 * Query - findDictionaryByPrimaryKey
	 *
	 */
	public Dictionary findDictionaryByPrimaryKey(Integer countyId, Integer type_1, String key_2) throws DataAccessException;

	public Dictionary persist(Dictionary dictionary);

	public void remove(Dictionary dictionary);

}