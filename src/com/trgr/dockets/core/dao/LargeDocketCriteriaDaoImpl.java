/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.SessionFactory;

import com.trgr.dockets.core.entity.LargeDocketCriteria;

public class LargeDocketCriteriaDaoImpl extends BaseDaoImpl implements LargeDocketCriteriaDao{
	
	
	public LargeDocketCriteriaDaoImpl(SessionFactory sessionFactory){
		super(sessionFactory);
	}

	@Override
	public List<LargeDocketCriteria> findAllLargeDocketCriteria() {
		return findAllEntities(LargeDocketCriteria.class);
	}
	
	@Override
	public LargeDocketCriteria findLargeDocketCriteriaByPrimaryKey(long productId) {
		return findEntityById(LargeDocketCriteria.class, productId);
	}
}