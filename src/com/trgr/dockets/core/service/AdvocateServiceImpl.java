/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.AdvocateDAO;
import com.trgr.dockets.core.entity.Advocate;

/**
 * Spring service that handles CRUD requests for Advocate entities
 * 
 */

@Transactional
public class AdvocateServiceImpl implements AdvocateService {

	/**
	 * DAO injected by Spring that manages Advocate entities
	 * 
	 */
	
	private AdvocateDAO advocateDAO;

	/**
	 * Instantiates a new AdvocateServiceImpl.
	 *
	 */
	public AdvocateServiceImpl() {
	}

	/**
	 * Save an existing Advocate entity
	 * 
	 */
	@Transactional
	public void saveAdvocate(Advocate advocate) {
		Advocate existingAdvocate = advocateDAO.findAdvocateByPrimaryKey(advocate.getCountyId(), advocate.getType(), advocate.getId());

		if (existingAdvocate != null ) {
			if (!existingAdvocate.equals(advocate) || !Arrays.equals(existingAdvocate.getValue(),(advocate.getValue()))) {
				existingAdvocate.setCountyId(advocate.getCountyId());
				existingAdvocate.setType(advocate.getType());
				existingAdvocate.setId(advocate.getId());
				existingAdvocate.setValue(advocate.getValue());
				existingAdvocate.setFilename(advocate.getFilename());
				existingAdvocate.setLuDate(advocate.getLuDate());
				advocate = advocateDAO.persist(existingAdvocate);
			}			
		} else {
			advocate = advocateDAO.persist(advocate);
		}
		
	}

	/**
	 */
	@Transactional(readOnly=true)
	public Advocate findAdvocateByPrimaryKey(Integer countyId, Integer type, String id) {
		return advocateDAO.findAdvocateByPrimaryKey(countyId, type, id);
	}

	/**
	 * Delete an existing Advocate entity
	 * 
	 */
	@Transactional
	public void deleteAdvocate(Advocate advocate) {
		advocateDAO.remove(advocate);
	}

	@Required
	public void setAdvocateDAO(AdvocateDAO advocateDAO) {
		this.advocateDAO = advocateDAO;
	}
}
