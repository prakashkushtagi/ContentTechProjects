/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.content.service;

import java.io.File;
import java.util.Date;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.acquisition.ActivitySet;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.exception.DocketsPersistenceException;
import com.trgr.dockets.core.util.UUIDGenerator;

public class PreDocketContentServiceImpl extends AbstractContentService implements PreDocketContentService
{

	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.content.service.PreDocketContentService#persistPreDocket(com.trgr.dockets.core.domain.SourceDocketMetadata)
	 */
	@Override
	public void persistPreDocket(SourceDocketMetadata sourceDocketMetadata) throws Exception
	{
		String legacyId = sourceDocketMetadata.getLegacyId();
		Date versionTimestamp = new Date();
		ContentVersion contentVersion = null;
		Content newContent = null;
		DocketVersion sourceDocketVersion = null;
		String contentUuid = null;
		
		//check if exists in docket table
		DocketEntity docketEntityFound = docketEntityServiceImpl.findDocketByLegacyId(legacyId);
		
		//get docketVersion
		if(null != docketEntityFound) {
			sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId,docketEntityFound.getCourt(),sourceDocketMetadata.getVendorId(),sourceDocketMetadata.getProduct().getId());
		}
				
		//if source versions is null, create the content
		if(sourceDocketVersion == null)
		{
			//create new content
			newContent = createNewContent(versionTimestamp, sourceDocketMetadata);
			contentUuid = newContent.getContentUuid();
		}
		//create a new version in the ContentVersion
		contentVersion = createContentVersion(sourceDocketVersion, sourceDocketMetadata.getDocketContentFile(),contentUuid,versionTimestamp, sourceDocketMetadata);
		if(null == docketEntityFound)
		{
			//create new docket
			docketEntityFound = createNewDocket(sourceDocketMetadata,versionTimestamp);
		}
		//create a new version in the DocketVersion
		sourceDocketVersion = createDocketVersion(sourceDocketVersion,sourceDocketMetadata,contentVersion.getPrimaryKey().getContentUuid(),versionTimestamp, Phase.SOURCE);
		//create a docket history for this.
		DocketHistory acquisitionDocketHistory = createDocketHistory(sourceDocketMetadata,versionTimestamp, DocketHistoryTypeEnum.ACQUISITION_HISTORY, sourceDocketMetadata.getPublishingRequest().getStartDate());
		persistContentAndDocketTree(newContent, contentVersion, docketEntityFound, sourceDocketVersion, acquisitionDocketHistory);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.content.service.PreDocketContentService#updatePreDocket(java.lang.String, com.trgr.dockets.core.acquisition.ActivitySet, com.trgr.dockets.core.entity.PublishingRequest)
	 */
	public void updatePreDocket(String legacyId, ActivitySet activitySet, PublishingRequest publishingRequest) throws Exception{
		DocketVersion sourceDocketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, publishingRequest.getCourt(), publishingRequest.getVendorId(), publishingRequest.getProduct().getId());
		//create new docket version
		String contentUuid = UUIDGenerator.createUuid().toString();
		//Yes, the vendor Id is hardcoded, but for predockets, this is probably correct
		DocketVersionKey docketVersionKey = new DocketVersionKey(legacyId, publishingRequest.getVendorId(), Phase.NOVUS, activitySet.getNovusLoadStartTime());
		DocketVersion newDocketVersion = new DocketVersion(docketVersionKey);
		newDocketVersion.setAcquisitionMethod(AcquisitionMethod.SMARTTEMPLATE);
		newDocketVersion.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		

		
		if (sourceDocketVersion != null){
			newDocketVersion.setOperationType(sourceDocketVersion.getOperationType());
			newDocketVersion.setContentId(contentUuid);
			newDocketVersion.setVersion(1L);
			newDocketVersion.setSourceFile(new File(activitySet.getSourceFile()));
			newDocketVersion.setProductId(sourceDocketVersion.getProductId());
			newDocketVersion.setCourt(sourceDocketVersion.getCourt()); 
		}
		//If we don't already have a source docket version in the database, enter this part:
		else {
			newDocketVersion.setOperationType("I");
			newDocketVersion.setContentId(contentUuid);
			newDocketVersion.setVersion(1L);
			newDocketVersion.setSourceFile(new File(activitySet.getSourceFile()));
			newDocketVersion.setProductId(publishingRequest.getProduct().getId());
			newDocketVersion.setCourt(publishingRequest.getCourt());
		}
		
		DocketHistory conversionDocketHistory = createDocketHistory(legacyId, activitySet.getConversionStartTime(), DocketHistoryTypeEnum.CONVERSION_HISTORY, publishingRequest);
		DocketHistory publishDocketHistory = createDocketHistory(legacyId, activitySet.getSaberStartTime(), DocketHistoryTypeEnum.PUBLISH_HISTORY, publishingRequest);
		DocketHistory loadDocketHistory = createDocketHistory(legacyId, activitySet.getNovusLoadStartTime(), DocketHistoryTypeEnum.LOAD_HISTORY, publishingRequest);
		DocketVersionRel docketVersionRel = createDocketVersionRel(sourceDocketVersion, newDocketVersion, sourceDocketVersion.getPrimaryKey().getVersionDate(), legacyId, DocketVersionRelTypeEnum.SOURCE_TO_NOVUS);			
		DocketVersionRelHistory docketVersionRelHistory = createDocketVersionRelHistory(publishDocketHistory, docketVersionRel, legacyId);
		persistContentAndDocketTree(newDocketVersion, conversionDocketHistory, publishDocketHistory, loadDocketHistory, docketVersionRel, docketVersionRelHistory);	
	}

	private void persistContentAndDocketTree(Content newContent, ContentVersion contentVersion, DocketEntity docketEntityFound,
			DocketVersion sourceDocketVersion, DocketHistory acquisitionDocketHistory) throws DocketsPersistenceException
	{
		//Save Docket
		persistTheDocket(docketEntityFound);
		//save Docket Version
		persistTheDocketVersion(sourceDocketVersion);
		//save docket history
		persistTheDocketHistory(acquisitionDocketHistory);
		//save the content entry
		if(newContent != null){
			persistTheContent(newContent);
		}
		//save the contentVersion
		persistTheContentVersion(contentVersion);
	}
	
	private void persistContentAndDocketTree(DocketVersion novusDocketVersion, DocketHistory conversionDocketHistory, DocketHistory publishDocketHistory, DocketHistory loadDocketHistory,
			DocketVersionRel docketVersionRel, DocketVersionRelHistory docketVersionRelHistory) throws DocketsPersistenceException
	{
		persistTheDocketVersion(novusDocketVersion);
		//save docket history
		persistTheDocketHistory(conversionDocketHistory);
		persistTheDocketHistory(publishDocketHistory);
		persistTheDocketHistory(loadDocketHistory);
		//save docket history rel
		persistTheDocketVersionRel(docketVersionRel); 
		//save docket version rel history
		persistTheDocketVersionRelHistory(docketVersionRelHistory);
	}

}
