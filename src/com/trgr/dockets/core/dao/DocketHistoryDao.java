/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.DocketVersionRelHistoryKey;


/**
 * DAO to manage DocketHistory entities.
 * 
 */


public interface DocketHistoryDao
{
	public DocketHistory persist(DocketHistory toPersist);
	public List<DocketHistory> findDocketHistoryByLegacyId(String legacyId);
	public List<DocketHistory> findDocketHistoryByLegacyIdType(String legacyId, DocketHistoryTypeEnum type);
	
	public DocketVersionRel persist(DocketVersionRel toPersist);
	public DocketVersionRel findDocketVersionRel(String relId);
	public DocketVersionRel findByLatestVersionRelBylegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType);
	public List<DocketVersionRel> findDocketVersionRelsByLegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType);
	
	public DocketVersionRelHistory persist(DocketVersionRelHistory toPersist);
	public DocketVersionRelHistory findDocketVersionRelHistoryByPrimaryKey(DocketVersionRelHistoryKey pk);
	public DocketVersionRelHistory findDocketVersionRelHistoryByRelId(String relId);
}



