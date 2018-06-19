/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.DocketHistoryDao;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.DocketVersionRelHistoryKey;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * Spring service that handles CRUD requests for DocketHistory entities
 * 
 */

@Transactional
public class DocketHistoryServiceImpl implements DocketHistoryService 
{

	private DocketHistoryDao dao;

	public DocketHistoryServiceImpl(DocketHistoryDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Utilities for inserting rows in tthe following tables.
	 * DOCKET_HISTORY - DOCKET_VERSION_REL_HISTORY - DOCKET_VERSION_REL 
	 */
	@Transactional
	public void saveDocketHistoryRelation(PublishingRequest publishingRequest,
											DocketVersion in, DocketVersion out, Date versionTimestamp,
											DocketHistoryTypeEnum historyType, String errMessage, File file,
											DocketVersionRelTypeEnum relationType) {
		
		String legacyId = in.getPrimaryKey().getLegacyId();

		// DOCKET_HISTORY
		String historyId = UUIDGenerator.createUuid().toString();
		DocketHistory history = new DocketHistory(historyId,
						legacyId, historyType, versionTimestamp, publishingRequest.getRequestName(),
						errMessage, file, publishingRequest.getStartDate());
		
		// DOCKET_VERSION_REL
		String relationId = UUIDGenerator.createUuid().toString();
		DocketVersionRel relation = new DocketVersionRel(relationId,
						in.getContentUuid().toString(), in.getVersion(),
						out.getContentUuid().toString(), out.getVersion(),
						relationType, new Date(), legacyId);
		
		// The join table entry (DOCKET_VERSION_REL_HISTORY)
		DocketVersionRelHistoryKey joinKey = new DocketVersionRelHistoryKey(historyId, relationId);
		DocketVersionRelHistory join = new DocketVersionRelHistory(joinKey, legacyId);

		// Insert rows
		dao.persist(history);
		dao.persist(relation);
		dao.persist(join);
	}

	// ----- DOCKET_HISTORY -----
	@Override
	@Transactional
	public DocketHistory save(DocketHistory docketHistory) 
	{
		if(null==docketHistory.getDocketHistoryId())
		{
			docketHistory.setDocketHistoryId(UUIDGenerator.createUuid().toString());
		}
		docketHistory = dao.persist(docketHistory);
		return docketHistory;
	}
	@Override
	@Transactional(readOnly=true)
	public List<DocketHistory> findDocketHistoryByLegacyId(String legacyId)
	{
		return dao.findDocketHistoryByLegacyId(legacyId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<DocketHistory> findDocketHistoryByLegacyIdType(String legacyId,	DocketHistoryTypeEnum type)
	{
		return dao.findDocketHistoryByLegacyIdType(legacyId, type);
	}
	
	// ----- DOCKET_VERSION_REL -----
	@Override
	@Transactional
	public DocketVersionRel save(DocketVersionRel docketVersionRel) 
	{
		if(null==docketVersionRel.getRelId())
		{
			UUID contentUuid = UUIDGenerator.createUuid();
			docketVersionRel.setRelId(contentUuid.toString());
		}
		docketVersionRel = dao.persist(docketVersionRel);
		return docketVersionRel;
	}

	@Override
	@Transactional(readOnly=true)
	public DocketVersionRel findDocketVersionRel(String relId) 
	{
		DocketVersionRel docketVersionRel = dao.findDocketVersionRel(relId);
		return docketVersionRel;
	}
	@Override
	@Transactional(readOnly=true)
	public DocketVersionRel findByLatestVersionRelBylegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType) 
	{
		DocketVersionRel docketVersionRel = dao.findByLatestVersionRelBylegacyIdAndType(legacyId,docketVersionRelType);
		return docketVersionRel;
	}
	@Override
	@Transactional(readOnly=true)
	public List<DocketVersionRel> findDocketVersionRelsByLegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType) 
	{
		return dao.findDocketVersionRelsByLegacyIdAndType(legacyId,docketVersionRelType);
	}
	// ----- DOCKET_VERSION_REL_HISTORY -----
	@Override
	@Transactional
	public DocketVersionRelHistory save(DocketVersionRelHistory docketVersionRelHistory) 
	{
		docketVersionRelHistory = dao.persist(docketVersionRelHistory);
		return docketVersionRelHistory;
	}
	@Override
	@Transactional(readOnly=true)
	public DocketVersionRelHistory findDocketVersionRelHistoryByRelId(String relId) {
		DocketVersionRelHistory docketVersionRelHistory = dao.findDocketVersionRelHistoryByRelId(relId);
		return docketVersionRelHistory;
	}
	@Override
	@Transactional(readOnly=true)
	public DocketVersionRelHistory findDocketVersionRelHistoryByPrimaryKey(DocketVersionRelHistoryKey pk) { 
		DocketVersionRelHistory docketVersionRelHistory = dao.findDocketVersionRelHistoryByPrimaryKey(pk);
		return docketVersionRelHistory;
	}
}
