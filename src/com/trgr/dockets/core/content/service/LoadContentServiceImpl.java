/**
 * 
 */
package com.trgr.dockets.core.content.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.apache.commons.lang.StringUtils;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.NovusDocketMetadata;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * @author C047166
 *
 */
public class LoadContentServiceImpl extends AbstractContentService implements LoadContentService
{
	private static Logger logger = Logger.getLogger(LoadContentServiceImpl.class);
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.content.service.LoadContentService#createLoadHistory()
	 */
	@Override
	public List<NovusDocketMetadata> createLoadHistory(List<NovusDocketMetadata> novusDocketMetadataList, PublishingRequest publishingRequest, Date loadTimestamp, String loadFileName, String droppedDocketReason)
	{
		 List<NovusDocketMetadata> droppedDockets = new ArrayList<NovusDocketMetadata>();
		 for(NovusDocketMetadata novusDocketMetadata : novusDocketMetadataList){
			DocketEntity docketEntity = docketService.findDocketByPrimaryKey(novusDocketMetadata.getLegacyId());
			logger.debug("About to try updating the docket entity with the load timestamp for " + novusDocketMetadata.getLegacyId());
			if(StringUtils.isBlank(droppedDocketReason) && docketEntity != null)
			{
				logger.debug("Docket has a blank dropped reason and is not null for" + novusDocketMetadata.getLegacyId());
				updateDocketEntity(docketEntity, loadTimestamp, novusDocketMetadata.getControl());
				logger.debug("Updated the docket entity");
			}
			if (docketEntity == null){
				logger.warn ("OH NOES! In DocketsCore createLoadHistory we received a null docketEntity when calling docketService.findDocketByPrimaryKey(novusDocketMetadata.getLegacyId) ");
			}
			DocketVersion novusDocketVersion = null;
		
			if (novusDocketMetadata.getVendorCode() != null && !novusDocketMetadata.getVendorCode().equals("0")){
				novusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct(novusDocketMetadata.getLegacyId(), docketEntity.getCourt(), Long.valueOf(novusDocketMetadata.getVendorCode()), publishingRequest.getProduct().getId());
			} else {
				novusDocketVersion = docketService.findNovusDocketVersionWithHighestVersionForProductAndCourt(novusDocketMetadata.getLegacyId(), docketEntity.getCourt() , publishingRequest.getProduct().getId());
			}
			
			
			if(novusDocketVersion != null){
				logger.debug("Novus Docket version is not null for legacy id of " + novusDocketMetadata.getLegacyId());
				try{
					logger.debug("Trying to persist docket history for legacy Id of " + novusDocketMetadata.getLegacyId());
					DocketVersionKey loadDocketVersionKey = new DocketVersionKey(novusDocketMetadata.getLegacyId(), novusDocketVersion.getPrimaryKey().getVendorKey(), Phase.LOAD, loadTimestamp);
					DocketVersion loadDocketVersion = new DocketVersion(loadDocketVersionKey);
					loadDocketVersion.setContentUuid(new UUID(novusDocketMetadata.getUuid()));
					loadDocketVersion.setVersion(novusDocketVersion.getVersion());
					persistDocketHistory(docketEntity, novusDocketVersion, loadDocketVersion, publishingRequest, loadTimestamp, loadFileName, droppedDocketReason);
				}catch(Exception e){
					 NovusDocketMetadata droppedDocketMetadata = new NovusDocketMetadata("");
					 droppedDocketMetadata.setLegacyId(novusDocketMetadata.getLegacyId());
					 droppedDocketMetadata.setCaseStatus(e.getMessage());
					 droppedDockets.add(droppedDocketMetadata);
				}
			 }else{
				 logger.debug ("Novus docket version is null for legacy id of : "+ novusDocketMetadata.getLegacyId());
				 NovusDocketMetadata droppedDocketMetadata = new NovusDocketMetadata("");
				 droppedDocketMetadata.setLegacyId(novusDocketMetadata.getLegacyId());
				 droppedDocketMetadata.setCaseStatus("Novus Docket Version not found");
				 droppedDockets.add(droppedDocketMetadata);
			 }
		 }
		 return droppedDockets;
	}

	private void updateDocketEntity(DocketEntity docketEntity, Date loadTimestamp, String control)
	{
		
		//load timestamp
		if (loadTimestamp != null && (docketEntity.getLastLoadDate() == null || docketEntity.getLastLoadDate().before(loadTimestamp)))
		{
			logger.debug("About to try setting the last load date because old load date is either null or old");
			docketEntity.setLastLoadDate(loadTimestamp); 
		}
		boolean publishFlag = false, docLoadedFlag = false;
		if (control.equals("ADD") || control.equals("UPDATE")) 
		{
			publishFlag = true;
			docLoadedFlag = true;
		} 
		// DOC_LOADED_FLAG
		if ((docLoadedFlag && docketEntity.getDocLoadedFlag().equals("N")) ||
				(!docLoadedFlag && docketEntity.getDocLoadedFlag().equals("Y")))
		{
			if (docLoadedFlag) 
			{
				docketEntity.setDocLoadedFlag("Y");
			}
			else
			{
				docketEntity.setDocLoadedFlag("N");
			}
		}
		// PUBLISH_FLAG
		if ((publishFlag && docketEntity.getPublishFlag().equals("N")) ||
				(!publishFlag && docketEntity.getPublishFlag().equals("Y")))
		{
			if (publishFlag) 
			{
				docketEntity.setPublishFlag("Y");
			}
			else
			{
				docketEntity.setPublishFlag("N");
			}
		}	
		//update last_nl_dropped_reason for docket since NL completed successfully.
		docketEntity.setLastNlDroppedReason(null);
	}

	private void persistDocketHistory(DocketEntity docketEntity, DocketVersion novusDocketVersion,
			DocketVersion loadDocketVersion, PublishingRequest publishingRequest, Date loadTimestamp, String loadFileName, String droppedDocketReason)
	{
		if(StringUtils.isNotBlank(droppedDocketReason))
		{
			if(droppedDocketReason.length() > 500)
			{
				droppedDocketReason = droppedDocketReason.substring(0, 500);
			}
			//only update the attempted last loaded date as it was dropped during the load process.
			docketEntity.setLastLoadDate(loadTimestamp);
			docketService.update(docketEntity);
		}
		else
		{
			logger.debug ("Dropped reason is blank so we only update the docketEntity for legacy id of" + docketEntity.getPrimaryKey());
			docketService.update(docketEntity);
		}
		//save docket history, rel and rel history	
		docketHistoryService.saveDocketHistoryRelation(publishingRequest, novusDocketVersion, loadDocketVersion, new Date(), DocketHistoryTypeEnum.LOAD_HISTORY, droppedDocketReason, new File(loadFileName), DocketVersionRelTypeEnum.NOVUS_TO_LOAD);
		
	}

}
