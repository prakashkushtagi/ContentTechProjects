/** Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.content.service;

import java.util.Date;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.domain.NovusDocketMetadata;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.BatchDocket;
import com.trgr.dockets.core.entity.BatchDocketKey;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.exception.DocketsPersistenceException;
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * Creates the publish history for a docket.
 * 
 * @author C047166
 *
 */
public class PublishContentServiceImpl extends AbstractContentService implements PublishContentService
{

	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.content.service.PublishContentService#createPublishHistory(com.trgr.dockets.core.domain.NovusDocketMetadata)
	 */
	@Transactional(propagation  = Propagation.REQUIRES_NEW)
	public void createPublishHistory(NovusDocketMetadata novusDocketMetadata) throws Exception
	{
		String legacyId = novusDocketMetadata.getLegacyId();
		Date versionTimestamp = new Date();
		DocketVersion judicialDocketVersion = null;
		DocketVersion novusDocketVersion = null;
		DocketVersion latestNovusDocketVersion = null;
		UUID pubReqId = new UUID(novusDocketMetadata.getPublishingRequest().getRequestId());
			
		//get the docket
		DocketEntity docketEntity = docketService.findDocketByPrimaryKey(legacyId);
		docketEntity.setLastPublishDate(versionTimestamp);
		
		Court court = docketEntity.getCourt();
		
		//get docketVersion
		judicialDocketVersion = docketService.findJudicialDocketVersionWithLatestTimestamp(legacyId, novusDocketMetadata.getPublishingRequest().getProduct().getId(), court.getPrimaryKey());			
		
		if(judicialDocketVersion == null)
			throw new Exception("Judicial Docket Version not found for legacy id " + legacyId);

		//In case of courts with multiple vendors, we only want to update the latest novus version regardless of vendor. So if court has two vendors (ie bulk and real-time) and real-time has the latest novus docket
		// version, we'll only update the latest real-time novus docket version and not the bulk.
		//NOTE: Below query will ignore predockets (which all have a vendor of 10 regardless of court)
		latestNovusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt(legacyId, court, novusDocketMetadata.getPublishingRequest().getProduct().getId());

		//set operationType as "D" in Batch Docket for a JUDICIAL docket with a "D" oper type
		if(null != judicialDocketVersion.getOperationType() && judicialDocketVersion.getOperationType().equalsIgnoreCase("D")){
			BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(new BatchDocketKey(legacyId, pubReqId));
			batchDocket.setOperationType("D");
			docketService.save(batchDocket);	
		}

		
		//create new docket version
		DocketVersionKey docketVersionKey = new DocketVersionKey(legacyId, judicialDocketVersion.getPrimaryKey().getVendorKey(), Phase.NOVUS, versionTimestamp);
		novusDocketVersion = new DocketVersion(docketVersionKey);
		if(novusDocketMetadata.getPublishingRequest().isDeleteOverride()){
			novusDocketVersion.setOperationType("D");
		}else{
			novusDocketVersion.setOperationType(judicialDocketVersion.getOperationType());
		}
		if(null!=latestNovusDocketVersion)
		{
			novusDocketVersion.setVersion((latestNovusDocketVersion.getVersion()+1L));
		}
		else
		{
			novusDocketVersion.setVersion(1L);
		}
		novusDocketVersion.setProductId(judicialDocketVersion.getProductId());
		novusDocketVersion.setAcquisitionMethod(judicialDocketVersion.getAcquisitionMethod());
		novusDocketVersion.setRequestInitiatorType(judicialDocketVersion.getRequestInitiatorType());
		novusDocketVersion.setScrapeTimestamp(judicialDocketVersion.getScrapeTimestamp());
		novusDocketVersion.setConvertedTimestamp(judicialDocketVersion.getConvertedTimestamp());
		novusDocketVersion.setCourt(judicialDocketVersion.getCourt());
		novusDocketVersion.setContentId(novusDocketMetadata.getNovusGuid());
		//set last_pb_dropped_reason to null since PB completed successfully.
		docketEntity.setLastPbDroppedReason(null);
		persistContentAndDocketTree(docketEntity, judicialDocketVersion, novusDocketVersion, novusDocketMetadata.getPublishingRequest(), versionTimestamp);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void persistContentAndDocketTree(DocketEntity docketEntity, DocketVersion judicialDocketVersion, DocketVersion novusDocketVersion, PublishingRequest publishingRequest, Date versionTimestamp) throws DocketsPersistenceException
	{
		persistTheDocket(docketEntity);
		persistTheDocketVersion(novusDocketVersion);
		//save docket history, rel and rel history	
		docketHistoryService.saveDocketHistoryRelation(publishingRequest, judicialDocketVersion, novusDocketVersion, versionTimestamp, DocketHistoryTypeEnum.PUBLISH_HISTORY, null, null, DocketVersionRelTypeEnum.JAXML_TO_NOVUS);
	}

}
