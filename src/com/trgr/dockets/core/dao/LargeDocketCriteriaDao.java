/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.LargeDocketCriteria;

public interface LargeDocketCriteriaDao extends BaseDao{
	public List<LargeDocketCriteria> findAllLargeDocketCriteria();
	public LargeDocketCriteria findLargeDocketCriteriaByPrimaryKey(long productId);
}