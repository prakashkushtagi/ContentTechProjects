/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.DocketEntity;


/**
 * DAO to manage Docket entities.
 * 
 */


public interface DocketEntityDao
{
	public DocketEntity persist(DocketEntity toPersist);

	public DocketEntity findDocketByLegacyId(String legacyId);
}
