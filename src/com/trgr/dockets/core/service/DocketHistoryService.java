package com.trgr.dockets.core.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.DocketVersionRelHistoryKey;
import com.trgr.dockets.core.entity.PublishingRequest;


/**
 * Spring service that handles CRUD requests for DocketHistory entities
 * 
 */
public interface DocketHistoryService {

	public DocketHistory save(DocketHistory docketHistory);
	public List<DocketHistory> findDocketHistoryByLegacyId(String legacyId);
	public List<DocketHistory> findDocketHistoryByLegacyIdType(String legacyId, DocketHistoryTypeEnum type);
	
	public DocketVersionRel save(DocketVersionRel docketVersionRel);
	public DocketVersionRel findDocketVersionRel(String relId);
	public DocketVersionRel findByLatestVersionRelBylegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType);
	public List<DocketVersionRel> findDocketVersionRelsByLegacyIdAndType(String legacyId, DocketVersionRelTypeEnum docketVersionRelType);

	public DocketVersionRelHistory save(DocketVersionRelHistory docketVersionRelHistory);
	public DocketVersionRelHistory findDocketVersionRelHistoryByRelId(String relId);
	public DocketVersionRelHistory findDocketVersionRelHistoryByPrimaryKey(DocketVersionRelHistoryKey pk);
	
	/**
	 * Update DOCKET_HISTORY, DOCKET_VERSION_REL, and DOCKET_VERSION_REL_HISTORY tables with docket connection info.
	 * @param publishingRequest
	 * @param in earlier version
	 * @param out later version
	 * @param historyType the docket history type
	 * @param errMessage
	 * @param file the file for the input docket, may be null
	 * @param relationType
	 */
	public void saveDocketHistoryRelation(PublishingRequest publishingRequest,
			DocketVersion in, DocketVersion out, Date versionTimestamp,
			DocketHistoryTypeEnum historyType, String errMessage, File file,
			DocketVersionRelTypeEnum relationType);
}