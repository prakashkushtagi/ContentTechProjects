/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.content.service;

import java.util.Date;

import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.AcquisitionStatus;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.util.UUIDGenerator;

public class FailedAcquisitionDocketContentServiceImpl extends AbstractContentService implements FailedAcquisitionDocketContentService
{

	public void persistFailedDocket(SourceDocketMetadata sourceDocketMetadata) throws Exception
	{
		String legacyId = sourceDocketMetadata.getLegacyId();
		Date versionTimestamp = new Date();
		DocketVersion sourceDocketVersion = null;
		AcquisitionStatus acquisitionStatus = docketService.findAcquisitionStatusForGivenStatus(sourceDocketMetadata.getStatus());
		String docketVersionId = "";
		
		DocketEntity docketEntityFound = docketEntityServiceImpl.findDocketByLegacyId(legacyId);
		if(docketEntityFound != null ){
			sourceDocketVersion = docketService.findLatestDocketVersionForProductCourtLegacyIdPhase(legacyId, sourceDocketMetadata.getCourt(), sourceDocketMetadata.getProduct().getId(), Phase.ATTEMPTED);
		}else {
			docketEntityFound = createNewDocket(sourceDocketMetadata,versionTimestamp);
			docketEntityFound.setAcquired("N");
			persistTheDocket(docketEntityFound);
		}
		
		if (sourceDocketVersion != null){
			docketVersionId = sourceDocketVersion.getContentId();
		}
		else {
			docketVersionId = UUIDGenerator.createUuid().toString();
		}
		sourceDocketVersion = createDocketVersion(sourceDocketVersion, sourceDocketMetadata, docketVersionId, versionTimestamp, Phase.ATTEMPTED);
		sourceDocketVersion.setAcquisitionStatus(acquisitionStatus.getStatusId());
		persistTheDocketVersion(sourceDocketVersion);
	}
}
