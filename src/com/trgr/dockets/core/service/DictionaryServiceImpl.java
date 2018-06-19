/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.DictionaryDAO;
import com.trgr.dockets.core.entity.Dictionary;

/**
 * Spring service that handles CRUD requests for Dictionary entities
 * 
 */

@Transactional
public class DictionaryServiceImpl implements DictionaryService {

	/**
	 * DAO injected by Spring that manages Dictionary entities
	 * 
	 */
	@Autowired
	private DictionaryDAO dictionaryDAO;

	/**
	 * Instantiates a new DictionaryServiceImpl.
	 *
	 */
	public DictionaryServiceImpl() {
	}

	/**
	 */
	@Transactional
	public Dictionary findDictionaryByPrimaryKey(Integer courtId, Integer type, String key) {
		return dictionaryDAO.findDictionaryByPrimaryKey(courtId, type, key);
	}

	/**
	 * Save an existing Dictionary entity
	 * 
	 */
	@Transactional
	public void saveDictionary(Dictionary dictionary) {
		Dictionary existingDictionary = dictionaryDAO.findDictionaryByPrimaryKey(dictionary.getCountyId(), dictionary.getType(), dictionary.getKey());

		if (existingDictionary != null) {
			if (!existingDictionary.equals(dictionary) || !Arrays.equals(existingDictionary.getValue(),dictionary.getValue())) {
				existingDictionary.setCountyId(dictionary.getCountyId());
				existingDictionary.setType(dictionary.getType());
				existingDictionary.setKey(dictionary.getKey());
				existingDictionary.setValue(dictionary.getValue());
				existingDictionary.setSourceFilename(dictionary.getSourceFilename());
				existingDictionary.setLuDate(dictionary.getLuDate());
				dictionary = dictionaryDAO.persist(existingDictionary);
			}			
		} else {
			dictionary = dictionaryDAO.persist(dictionary);
		}

	}

	/**
	 * Delete an existing Dictionary entity
	 * 
	 */
	@Transactional
	public void deleteDictionary(Dictionary dictionary) {
		dictionaryDAO.remove(dictionary);
	}

	@Required
	public void setDictionaryDAO(DictionaryDAO dictionaryDAO) {
		this.dictionaryDAO = dictionaryDAO;
	}
}
