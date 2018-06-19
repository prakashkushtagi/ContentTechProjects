/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.content.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.domain.JudicialDocketMetadata;
import com.trgr.dockets.core.domain.LargeDocketInfo;
import com.trgr.dockets.core.entity.BatchDocket;
import com.trgr.dockets.core.entity.BatchDocketKey;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.ContentVersionPK;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.exception.DocketsPersistenceException;
import com.trgr.dockets.core.exception.ScrapeTimeException;
import com.trgr.dockets.core.service.ContentService;
import com.trgr.dockets.core.service.ContentVersionService;
import com.trgr.dockets.core.service.DocketEntityService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

public class JudicialContentServiceImpl implements JudicialContentService {
	private static final Logger log = Logger.getLogger(JudicialContentServiceImpl.class);
	
	private DocketService docketService;
	private ContentService contentServiceImpl;
	private ContentVersionService contentVersionServiceImpl;
	private DocketEntityService docketEntityServiceImpl;
	private DocketHistoryService docketHistoryService;
	
	public JudicialContentServiceImpl(
			DocketService docketService,
			ContentService contentServiceImpl,
			ContentVersionService contentVersionService,
			DocketEntityService docketEntityService,
			DocketHistoryService docketHistoryService) {
		this.docketService = docketService;
		this.contentServiceImpl = contentServiceImpl;
		this.contentVersionServiceImpl = contentVersionService;
		this.docketEntityServiceImpl = docketEntityService;
		this.docketHistoryService = docketHistoryService;
	}
	
		
	@Override
	public DocketVersion persistDocket(JudicialDocketMetadata judicialDocketMetadata, boolean acquisitionOverride) throws Exception {
		
		String legacyId = judicialDocketMetadata.getLegacyId();
		Date versionTimestamp = new Date();
		ContentVersion contentVersion = null;
		Content newContent = null;
		DocketVersion docketJudicialVersion = null;
		DocketVersion docketSourceVersion = null;
		boolean createNewContent = false;
		
		//check if exists in docket table
		DocketEntity docketEntity = judicialDocketMetadata.getDocketVersion().getDocket();
		
		if(docketEntity == null)
		{
			throw new DocketsPersistenceException("No Docket found for legacyId:" + legacyId);
		}

		//Use to create history relationship between source and judicial phases
		docketSourceVersion = docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, docketEntity.getCourt(), judicialDocketMetadata.getVendor(), judicialDocketMetadata.getProduct().getId());
		
		docketJudicialVersion = docketService.findJudicialDocketVersionWithHighestVersion(legacyId, judicialDocketMetadata.getVendor(), judicialDocketMetadata.getProduct().getId(), docketEntity.getCourt());
			
		String contentUuid = null;
		//docket version is null for a new vendor version
		if (docketJudicialVersion == null) 
		{
			//create new content
			newContent = createNewContent(versionTimestamp, judicialDocketMetadata);
			contentUuid = newContent.getContentUuid();
			createNewContent = true;
		}
		//create a new version in the ContentVersion
		contentVersion = createContentVersion(docketJudicialVersion, judicialDocketMetadata.getJaxmlFile(),contentUuid,versionTimestamp, judicialDocketMetadata);
		
		if(contentVersionServiceImpl.findContentVersionByUuidAndVersion(contentVersion.getPrimaryKey().getContentUuid(), contentVersion.getPrimaryKey().getVersionId()) != null){
			log.error("Cannot have a content version already " + contentVersion.getPrimaryKey().getContentUuid() + " version is " + contentVersion.getPrimaryKey().getVersionId());
		}
		//update docket table with scrapeTimestamp if already found
		docketEntity.setLastScrapeDate(judicialDocketMetadata.getScrapeTimeStamp());
		log.debug(judicialDocketMetadata);
		log.debug(docketSourceVersion);
		log.debug(docketJudicialVersion);
		docketEntity.setDocketTypeCode(judicialDocketMetadata.getDocketVersion().getDocket().getDocketTypeCode());
		docketEntity.setTitle(judicialDocketMetadata.getDocketVersion().getDocket().getTitle());
		docketEntity.setSequenceNumber(judicialDocketMetadata.getDocketVersion().getDocket().getSequenceNumber());
		docketEntity.setFilingDate(judicialDocketMetadata.getDocketVersion().getDocket().getFilingDate());
		docketEntity.setCountyId(judicialDocketMetadata.getDocketVersion().getDocket().getCountyId());
		docketEntity.setLastCaseStatus(judicialDocketMetadata.getDocketVersion().getDocket().getLastCaseStatus());
		docketEntity.setCaseTypeId(judicialDocketMetadata.getDocketVersion().getDocket().getCaseTypeId());
		docketEntity.setCaseSubTypeId(judicialDocketMetadata.getDocketVersion().getDocket().getCaseSubTypeId());
		if (judicialDocketMetadata.getDocketVersion() != null ){
			DocketVersion docketVersion = judicialDocketMetadata.getDocketVersion();
			if (docketVersion.getConvertedTimestamp() == null || docketEntity.getLastConvertedDate() == null ){
				docketEntity.setLastConvertedDate(versionTimestamp);
			}
		}
		//create a new version in the DocketVersion
		docketJudicialVersion = createDocketVersion(docketJudicialVersion,judicialDocketMetadata,contentVersion.getPrimaryKey().getContentUuid(),versionTimestamp, acquisitionOverride);
		//update last_ic_dropped_reason to null since IC completed successfully.
		docketEntity.setLastIcDroppedReason(null);
		LargeDocketInfo largeDocketInfo = judicialDocketMetadata.getLargeDocketInfo();
		if(null != largeDocketInfo){
			docketEntity.setLargeDocketFlag(largeDocketInfo.getLargeDocket());
		}else{
			docketEntity.setLargeDocketFlag('N');
		}
		docketJudicialVersion.setDocket(docketEntity);
		persistContentAndDockets(judicialDocketMetadata.getJaxmlFile(), docketEntity, docketJudicialVersion,
								 newContent, createNewContent, contentVersion);

		// Create Docket History related table entries
		docketHistoryService.saveDocketHistoryRelation(judicialDocketMetadata.getPublishingRequest(),
										docketSourceVersion, docketJudicialVersion, versionTimestamp,
										DocketHistoryTypeEnum.CONVERSION_HISTORY, null, judicialDocketMetadata.getSourceFile(),
										DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
		
		return docketJudicialVersion;
	}

	@Transactional(propagation=Propagation.REQUIRED)
	private void persistContentAndDockets(File jaxmlFile,DocketEntity docketEntity, DocketVersion docketVersion, Content newContent,
			  boolean createContentEntry, ContentVersion contentVersion) throws DocketsPersistenceException,	IOException 
	{ 
		docketService.merge(docketEntity);
		//Save Docket
		//persistTheDocket(docketEntity);
		//save Docket Version
		persistTheDocketVersion(docketVersion);
		//save the content entry
		if(createContentEntry)
		{
			persistTheContent(newContent);
		}
		//save the contentVersion
		persistTheContentVersion(contentVersion,jaxmlFile);
	}
	
	public void updateBatchDocketLargeDocketFlag(DocketEntity docketEntity, PublishingRequest pubRequest) throws Exception {
		BatchDocketKey primaryKey = new BatchDocketKey(docketEntity.getPrimaryKey(), new UUID(pubRequest.getRequestId()));
		BatchDocket batchDocket = docketService.findBatchDocketByPrimaryKey(primaryKey);
		batchDocket.setLargeDocketFlag(docketEntity.getLargeDocketFlag());
		docketService.update(batchDocket);	
	}
	
	private Content createNewContent(Date versionTimeStamp, JudicialDocketMetadata judicialDocketMetadata) throws DocketsPersistenceException 
	{
		Content newContent = new Content();
		newContent.setArtifactDescriptorId(CoreConstants.ARTIFACT_DESCRIPTOR_ID);
		newContent.setArtifactPhaseId(CoreConstants.ARTIFACT_PHASE_JUDICIAL_ID);
		newContent.setArtifactUuid("");
		newContent.setCreatedBy(CoreConstants.DOCKETS_CONTENT_SERVICE);
		newContent.setCreatedOn(versionTimeStamp);
		newContent.setDbPartition(CoreConstants.LITIGATOR_CONTENT);
		newContent.setFormatTypeId(CoreConstants.ARTIFACT_JUDICIAL_FORMAT_TYPE);
		newContent.setTerminatedFlag("N");
		UUID contentUuid = UUIDGenerator.createUuid();
		newContent.setContentUuid(contentUuid.toString());
		newContent.setProductId(judicialDocketMetadata.getProduct().getId());
		if(judicialDocketMetadata.getCourt() != null){
			newContent.setCourtId(judicialDocketMetadata.getCourt().getPrimaryKey());
		}else{
			
			if((judicialDocketMetadata.getDocketVersion() != null) 
					&& (judicialDocketMetadata.getDocketVersion().getCourt()!= null)){
			
				newContent.setCourtId(judicialDocketMetadata.getDocketVersion().getCourt().getPrimaryKey());	
			}
			
		}
		return newContent;
	}

	private ContentVersion createContentVersion(DocketVersion docketVersion, File contentFile, String contentUuid, Date versionTimeStamp, JudicialDocketMetadata judicialDocketMetadata) throws  IOException 
	{
		ContentVersionPK contentVersionPK = new ContentVersionPK();
		if(null!=docketVersion)
		{
			contentVersionPK.setContentUuid(docketVersion.getContentId());
			contentVersionPK.setVersionId(docketVersion.getVersion()+1L);
		}
		else
		{
			contentVersionPK.setContentUuid(contentUuid);
			contentVersionPK.setVersionId(1L);
		}
		ContentVersion contentVersion = new ContentVersion();
		contentVersion.setPrimaryKey(contentVersionPK);
		contentVersion.setCreatedOn(versionTimeStamp);
		contentVersion.setCreatedBy(CoreConstants.DOCKETS_CONTENT_SERVICE);
		contentVersion.setContentSize(contentFile.length());
		contentVersion.setProductId(judicialDocketMetadata.getProduct().getId());

		if(judicialDocketMetadata.getCourt() != null){
			
			contentVersion.setCourtId(judicialDocketMetadata.getCourt().getPrimaryKey());
			
		}else if((judicialDocketMetadata.getDocketVersion() != null) && (judicialDocketMetadata.getDocketVersion().getCourt() != null)){
			
			contentVersion.setCourtId(judicialDocketMetadata.getDocketVersion().getCourt().getPrimaryKey());	
				
		}
   		return contentVersion;
	}
	
	private DocketVersion createDocketVersion(DocketVersion docketVersion, JudicialDocketMetadata judicialDocketMetadata, String contentUuid, Date versionTimestamp, boolean acquisitionOverride) 
			throws DocketsPersistenceException, ScrapeTimeException
	{
		DocketVersion newDocketVersion = new DocketVersion();
		DocketVersionKey docketVersionKey = null;
		if(null!=docketVersion)
		{
			docketVersionKey = new DocketVersionKey(docketVersion.getPrimaryKey().getLegacyId(), docketVersion.getPrimaryKey().getVendorKey(), Phase.JUDICIAL,versionTimestamp);
			if(null != judicialDocketMetadata.getScrapeTimeStamp() && null != docketVersion.getScrapeTimestamp()) {
				if(docketVersion.getScrapeTimestamp().compareTo(judicialDocketMetadata.getScrapeTimeStamp())==0)
				{
					newDocketVersion.setOperationType("R");
				}
				else if(docketVersion.getScrapeTimestamp().compareTo(judicialDocketMetadata.getScrapeTimeStamp())<1)
				{
					newDocketVersion.setOperationType("U");
				}
				else if (acquisitionOverride){
					newDocketVersion.setOperationType("R");
				}
				else
				{
					throw new ScrapeTimeException("Newer version of docket already processed" +
							" Scrape timestamp of docket in DCR: " +docketVersion.getScrapeTimestamp() + 
							" Scrape timestamp of docket in current load : " +judicialDocketMetadata.getScrapeTimeStamp()); 
				}
			//If there is not scrape information populated, always do as an add
			} else {
				newDocketVersion.setOperationType("I");
			}
			newDocketVersion.setVersion(docketVersion.getVersion() + 1L);
			newDocketVersion.setContentUuid(docketVersion.getContentUuid());

				
		}
		else
		{
			docketVersionKey = new DocketVersionKey(judicialDocketMetadata.getLegacyId(), judicialDocketMetadata.getVendor(),Phase.JUDICIAL ,versionTimestamp);
			newDocketVersion.setVersion(1L);
			newDocketVersion.setOperationType("I");
			try 
			{
				newDocketVersion.setContentId(contentUuid);
			} 
			catch (UUIDException e) 
			{
				//TODO::Need to do something about this
			}
		}
		
		// it does not matter if this is new or existing.  If the delete operation is D then that needs to be persisted
		if (judicialDocketMetadata.isDeleteOperation()||judicialDocketMetadata.getPublishingRequest().isDeleteOverride())
		{
			newDocketVersion.setOperationType("D");
		}

		newDocketVersion.setPrimaryKey(docketVersionKey);
		newDocketVersion.setAcquisitionMethod(judicialDocketMetadata.getAcquisitionMethod());
		
		newDocketVersion.setScrapeTimestamp(judicialDocketMetadata.getScrapeTimeStamp());
		newDocketVersion.setConvertedTimestamp(judicialDocketMetadata.getDocketVersion().getDocket().getLastConvertedDate());

		newDocketVersion.setRequestInitiatorType(judicialDocketMetadata.getRequestInitiatorType());
		newDocketVersion.setSourceFile(judicialDocketMetadata.getSourceFile());
		newDocketVersion.setContentSize(judicialDocketMetadata.getJaxmlContentSize());
		newDocketVersion.setProductId(judicialDocketMetadata.getProduct().getId());
		
		if(judicialDocketMetadata.getCourt() != null){
			
			newDocketVersion.setCourt(judicialDocketMetadata.getCourt());
			
		}else if((judicialDocketMetadata.getDocketVersion() != null) && (judicialDocketMetadata.getDocketVersion().getCourt() != null)){
			
			newDocketVersion.setCourt(judicialDocketMetadata.getDocketVersion().getCourt());	
				
		}
		
		newDocketVersion.setCaseStatus(judicialDocketMetadata.getDocketVersion().getDocket().getLastCaseStatus());
		return newDocketVersion;
	}

	@Transactional
	private void persistTheDocket(DocketEntity docketEntityFound) 
	{
		docketEntityServiceImpl.saveDocketEntity(docketEntityFound);
	}
	@Transactional
	private void persistTheDocketVersion(DocketVersion docketVersion) throws DocketsPersistenceException 
	{
		try
		{
			docketService.save(docketVersion);
		}
		catch(Exception e)
		{
			throw new DocketsPersistenceException("Unable to create docketVersion "+ e.getMessage(), e);
		}
		
	}
	@Transactional
	private void persistTheContent(Content newContent) throws DocketsPersistenceException
	{
		try
		{
			newContent = contentServiceImpl.saveContent(newContent);
		}
		catch(Exception e)
		{
			throw new DocketsPersistenceException("Unable to create content in content table " + e.getMessage(), e);
		}
		
	}
	@Transactional
	private void persistTheContentVersion(ContentVersion contentVersion, File docketContentFile) throws IOException
	{
		contentVersion.setFileimage(FileUtils.readFileToByteArray(docketContentFile));
   		contentVersion = contentVersionServiceImpl.saveContentVersion(contentVersion);
	}
}
