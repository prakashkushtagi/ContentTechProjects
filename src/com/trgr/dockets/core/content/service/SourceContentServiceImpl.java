/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.content.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.domain.DroppedDocketHistory;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.BatchDroppedDocket;
import com.trgr.dockets.core.entity.CodeTableValues.DroppedProcessTypeEnum;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.exception.AcquisitionTimeException;
import com.trgr.dockets.core.exception.DocketsPersistenceException;

/**
 * @author c153421
 *
 */
public class SourceContentServiceImpl extends AbstractContentService implements SourceContentService
{
	
	private static final Logger log = Logger.getLogger(SourceContentServiceImpl.class);
	
	private int MAX_RETRY=3;
	
	private Long droppedReasonId;
	
	public List<DroppedDocketHistory> loadDockets(List<SourceDocketMetadata> sourceDocketMetadataList) throws DocketsPersistenceException
	{
		List<DroppedDocketHistory> droppedDocketHistoryList = new ArrayList<DroppedDocketHistory>();
		return loadDockets(sourceDocketMetadataList, droppedDocketHistoryList, 1);
	}
	
	
	private List<DroppedDocketHistory> loadDockets(List<SourceDocketMetadata> sourceDocketMetadataList, List<DroppedDocketHistory> droppedDocketHistoryList, int retryCount) throws DocketsPersistenceException
	{		
		List<SourceDocketMetadata> droppedSourceDocketMetadataList = new ArrayList<SourceDocketMetadata>();		
		for(SourceDocketMetadata sourceDocketMetadata : sourceDocketMetadataList)
		{
			String legacyId = sourceDocketMetadata.getLegacyId();
			Date versionTimestamp = new Date();
			ContentVersion contentVersion = null;
			Content newContent = null;
			DocketVersion docketVersion = null;
			boolean createContentEntry = false;
			try 
			{
				//check if exists in docket table
				DocketEntity docketEntityFound = docketEntityServiceImpl.findDocketByLegacyId(legacyId);
				//Minor performance improvement: if no docket found, then don't query for docket version as none will exist (docket version is very large)
				if (docketEntityFound != null){
					docketVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, sourceDocketMetadata.getCourt(), sourceDocketMetadata.getVendorId(),sourceDocketMetadata.getProduct().getId());
				}

				String contentUuid = null;
				//docket version is null for a new vendor version and also if no docket is present.
				if(null==docketVersion)
				{
					newContent = createNewContent(versionTimestamp, sourceDocketMetadata);
					contentUuid = newContent.getContentUuid();
					createContentEntry=true;
				}

				contentVersion = createContentVersion(docketVersion, sourceDocketMetadata.getDocketContentFile(),contentUuid,versionTimestamp, sourceDocketMetadata);
				//size of the file needed for UI to read from docketVersion
				sourceDocketMetadata.setContenFileSize(contentVersion.getContentSize());
				
				if(null==docketEntityFound){
					docketEntityFound = createNewDocket(sourceDocketMetadata,versionTimestamp);
				}else{					
					docketEntityFound.setDocketNumber(sourceDocketMetadata.getDocketNumber());
					
					if (docketEntityFound.getAcquired() != null && docketEntityFound.getAcquired().equals("N")) {
						docketEntityFound.setAcquired("Y");
						if (docketEntityFound.getPublishFlag().equals("N")){
							docketEntityFound.setPublishFlag("Y");
						}
					}					
					//we missed out populating filing date for existing docket entities. Hence updating.
					if(sourceDocketMetadata.getFilingDate() != null){
						docketEntityFound.setFilingDate(sourceDocketMetadata.getFilingDate());						
					}
					if(sourceDocketMetadata.getCaseTypeId() != null){
						docketEntityFound.setCaseTypeId(sourceDocketMetadata.getCaseTypeId());
					}
				}

				docketVersion = createDocketVersion(docketVersion,sourceDocketMetadata,contentVersion.getPrimaryKey().getContentUuid(),versionTimestamp, Phase.SOURCE);
				DocketHistory docketHistory = createDocketHistory(sourceDocketMetadata, versionTimestamp, sourceDocketMetadata.getPublishingRequest().getStartDate());
				//update last_pp_dropped_reason since PP completed successfully
				docketEntityFound.setLastPpDroppedReason(null);
				persistContentAndDockets(versionTimestamp, contentVersion, newContent,docketVersion, docketHistory,createContentEntry, docketEntityFound);				
			} 
			catch (AcquisitionTimeException ate) 
			{
				droppedReasonId = 101l;
				DroppedDocketHistory droppedDocketHistory = addDroppedDocketHistory(ate, sourceDocketMetadata, legacyId, droppedReasonId);
				droppedDocketHistoryList.add(droppedDocketHistory);	
				log.error("Docket with legacy.id " + droppedDocketHistory.getLegacyId() + " failed for " + droppedDocketHistory.getBatchDroppedDocket().getErrorDescription());
			}
			catch (DocketsPersistenceException dpe)
			{				
				droppedSourceDocketMetadataList.add(sourceDocketMetadata);
				if (retryCount ==  MAX_RETRY){
					droppedReasonId = 102l;
					DroppedDocketHistory droppedDocketHistory = addDroppedDocketHistory(dpe, sourceDocketMetadata, legacyId, droppedReasonId);
					droppedDocketHistoryList.add(droppedDocketHistory);
					log.error("Docket with legacy.id " + droppedDocketHistory.getLegacyId() + " failed for " + droppedDocketHistory.getBatchDroppedDocket().getErrorDescription());
				}				
			} 			
			catch (Exception e) 
			{
				log.error(ExceptionUtils.getFullStackTrace(e));
				System.out.println(ExceptionUtils.getFullStackTrace(e));
				//100l corresponds to 'unknown' preprocessor exception
				droppedReasonId = 100l;
				DroppedDocketHistory droppedDocketHistory = addDroppedDocketHistory(e, sourceDocketMetadata, legacyId, droppedReasonId);
				droppedDocketHistoryList.add(droppedDocketHistory);
				log.error("Docket with legacy.id " + droppedDocketHistory.getLegacyId() + " failed for " + e.getMessage());				
			}
		}
		if (droppedSourceDocketMetadataList.size() == 0 || retryCount ==  MAX_RETRY){
			return droppedDocketHistoryList;
		} else {
			randomDelay();
			String requestName = droppedSourceDocketMetadataList.get(0).getPublishingRequest().getRequestName();
			log.info("Found " + droppedSourceDocketMetadataList.size() + " dockets that we need to retry persistence for from " + requestName);
			return loadDockets (droppedSourceDocketMetadataList, droppedDocketHistoryList, ++retryCount);
		}
		
		
		
	}
	
	private void randomDelay(){
		int random = (int)(45* Math.random() + 5);
		try {
			Thread.sleep(random * 1000);
		} catch (InterruptedException e) {
			log.info("Exception thrown while trying to insert delay in persistence retry step");
		}
	}
	

	/**
	 * @param sourceDocketMetadata
	 * @param versionTimestamp
	 * @param contentVersion
	 * @param newContent
	 * @param docketVersion
	 * @param createContentEntry
	 * @param docketEntityFound
	 * @throws DocketsPersistenceException
	 * @throws IOException
	 */
	
	@Transactional(propagation=Propagation.REQUIRED)
	private void persistContentAndDockets(Date versionTimestamp,ContentVersion contentVersion, Content newContent,
			DocketVersion docketVersion, DocketHistory docketHistory, boolean createContentEntry,DocketEntity docketEntityFound) throws DocketsPersistenceException,	IOException 
	{ 
		//Save Docket
		persistTheDocket(docketEntityFound);
		//save Docket Version
		persistTheDocketVersion(docketVersion);
		//save docket history
		persistTheDocketHistory(docketHistory);
		//save the content entry
		if(createContentEntry)
		{
			persistTheContent(newContent);
		}
		//save the contentVersion
		persistTheContentVersion(contentVersion);
	}
	
	public DroppedDocketHistory addDroppedDocketHistory(Exception e, SourceDocketMetadata sourceDocketMetadata, String legacyId, Long droppedReasonId) 
	{
		BatchDroppedDocket droppedDocket = new BatchDroppedDocket(null, sourceDocketMetadata.getDocketNumber(), e.getMessage(), null, sourceDocketMetadata.getCourt().getCourtNorm(), legacyId);

		DroppedDocketHistory droppedDocketHistory = new DroppedDocketHistory(droppedDocket, legacyId, DroppedProcessTypeEnum.PP, droppedReasonId);
		return droppedDocketHistory;
	}
	
	public void setMaxRetry(int maxRetry){
		this.MAX_RETRY = maxRetry;
	}


}
