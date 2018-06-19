/*
 * Copyright 2016: Thomson Reuters.
 * All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.content.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.AcquisitionStatus;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.ContentVersionPK;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.DocketVersionRelHistoryKey;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.exception.AcquisitionTimeException;
import com.trgr.dockets.core.exception.DocketsPersistenceException;
import com.trgr.dockets.core.service.ContentService;
import com.trgr.dockets.core.service.ContentVersionService;
import com.trgr.dockets.core.service.DocketEntityService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

/**
 * Common code for docket persistence service.
 *  
 * @author C047166
 *
 */
public abstract class AbstractContentService
{
	DocketEntityService docketEntityServiceImpl;
	DocketService docketService;
	DocketHistoryService docketHistoryService;
	ContentService contentService;
	ContentVersionService contentVersionService;
	
	private final static String DATA_INTEGRITY_PREFIX= "Data integrity violation exception ";
	
	@Transactional
	protected void persistTheDocket(DocketEntity docketEntityFound) throws DocketsPersistenceException
	{
		try {
			docketEntityServiceImpl.saveDocketEntity(docketEntityFound);
		} 
		catch (DataIntegrityViolationException dive)
		{
			throw new DocketsPersistenceException(DATA_INTEGRITY_PREFIX + "while trying to persist the docket!", dive);
		}
	}
	
	@Transactional
	protected void persistTheDocketHistory(DocketHistory docketHistory) throws DocketsPersistenceException 
	{
		try
		{
			docketHistory = docketHistoryService.save(docketHistory);
		}
		catch(Exception e)
		{
			throw new DocketsPersistenceException("Unable to create docketHistory "+ e.getMessage(), e);
		}
	
	}
	
	@Transactional
	protected void persistTheDocketVersion(DocketVersion docketVersion) throws DocketsPersistenceException 
	{
		try
		{
			docketService.save(docketVersion);
		}
		catch (DataIntegrityViolationException dive)
		{
			throw new DocketsPersistenceException(DATA_INTEGRITY_PREFIX+"while trying to persist docket version!", dive);
		}
		catch(Exception e)
		{
			throw new DocketsPersistenceException("Unable to create docketVersion "+ e.getMessage(), e);
		}
		
	}
	@Transactional
	protected void persistTheContent(Content newContent) throws DocketsPersistenceException
	{
		try
		{
			newContent = contentService.saveContent(newContent);
		}
		catch(Exception e)
		{
			throw new DocketsPersistenceException("Unable to create content " + e.getMessage(), e);
		}
		
	}
	@Transactional
	protected void persistTheContentVersion(ContentVersion contentVersion) throws DocketsPersistenceException
	{
		try
		{
			contentVersionService.saveContentVersion(contentVersion);
		}
		catch (DataIntegrityViolationException dive)
		{
			throw new DocketsPersistenceException(DATA_INTEGRITY_PREFIX+"while trying to persist content version!", dive);
		}
		catch(Exception e)
		{
			throw new DocketsPersistenceException("Unable to create contentVersion " + e.getMessage(), e);
		}
	}
	
	@Transactional
	protected void persistTheDocketVersionRel(DocketVersionRel docketVersionRel)
	{
		docketService.save(docketVersionRel);
		
	}
	
	@Transactional
	protected void persistTheDocketVersionRelHistory(DocketVersionRelHistory docketVersionRelHistory)
	{
		docketService.save(docketVersionRelHistory);
		
	}
	
	protected DocketEntity createNewDocket(SourceDocketMetadata sourceDocketMetadata, Date versionTimestamp) throws DocketsPersistenceException 
	{
		DocketEntity docketEntity = new DocketEntity();
		docketEntity.setPrimaryKey(sourceDocketMetadata.getLegacyId());  
		docketEntity.setCourt(sourceDocketMetadata.getCourt());
		docketEntity.setSequenceNumber(sourceDocketMetadata.getSequenceNumber());
		docketEntity.setFilingYear((new Long(sourceDocketMetadata.getYearFiled())).longValue());
		docketEntity.setDocketTypeCode(sourceDocketMetadata.getDocketTypeCode());
		docketEntity.setCountyId(sourceDocketMetadata.getCountyId());
		docketEntity.setDocketNumber(sourceDocketMetadata.getDocketNumber());
		if(sourceDocketMetadata.getCaseTypeId() != null){
			docketEntity.setCaseTypeId(sourceDocketMetadata.getCaseTypeId());
		}
		docketEntity.setProduct(sourceDocketMetadata.getProduct());
		docketEntity.setDocLoadedFlag("N");
		docketEntity.setAcquired("Y");
		if(sourceDocketMetadata.getPublishFlag() != null){
			docketEntity.setPublishFlag(sourceDocketMetadata.getPublishFlag());
		}
		else
		{
			docketEntity.setPublishFlag(CoreConstants.PUBLISHABLE_FLAG_YES);
		}
		if(sourceDocketMetadata.getTitle() != null){
			docketEntity.setTitle(sourceDocketMetadata.getTitle());
		}
		if(sourceDocketMetadata.getCaseSubTypeId() != null){
			docketEntity.setCaseSubTypeId(sourceDocketMetadata.getCaseSubTypeId());
		}
		if(sourceDocketMetadata.getFilingDate() != null){
			docketEntity.setFilingDate(sourceDocketMetadata.getFilingDate());
		}
		if(!"0".equals(sourceDocketMetadata.getLocationCode())){
			docketEntity.setLocationCode(sourceDocketMetadata.getLocationCode());
		}
		return docketEntity;
	}

	protected Content createNewContent(Date versionTimeStamp, SourceDocketMetadata sourceDocketMetadata) throws DocketsPersistenceException 
	{
		Content newContent = new Content();
		UUID contentUuid = UUIDGenerator.createUuid();
		newContent.setContentUuid(contentUuid.toString());
		newContent.setArtifactDescriptorId(CoreConstants.ARTIFACT_DESCRIPTOR_ID);
		newContent.setArtifactPhaseId(CoreConstants.ARTIFACT_PHASE_SOURCE_ID);
		newContent.setArtifactUuid("");
		newContent.setCreatedBy(CoreConstants.DOCKETS_CONTENT_SERVICE);
		newContent.setCreatedOn(versionTimeStamp);
		newContent.setDbPartition(CoreConstants.LITIGATOR_CONTENT);
		newContent.setFormatTypeId(CoreConstants.ARTIFACT_SOURCE_FORMAT_TYPE);
		newContent.setTerminatedFlag("N");
		newContent.setProductId(sourceDocketMetadata.getProduct().getId());
		newContent.setCourtId(sourceDocketMetadata.getCourt().getPrimaryKey());
		return newContent;
	}

	protected DocketHistory createDocketHistory(SourceDocketMetadata sourceDocketMetadata, Date versionTimestamp, Date startTimestamp) 
	{
		DocketHistoryTypeEnum docketHistoryTypeEnum = DocketHistoryTypeEnum.ACQUISITION_HISTORY;
		if(sourceDocketMetadata.getDocketHistEnum()!=null)
		{
			docketHistoryTypeEnum = sourceDocketMetadata.getDocketHistEnum();
		}
		DocketHistory docketHistory = new DocketHistory(
				UUIDGenerator.createUuid().toString(), sourceDocketMetadata.getLegacyId(),
				docketHistoryTypeEnum, versionTimestamp, 
				sourceDocketMetadata.getRequestName(), null, sourceDocketMetadata.getSourceFile(), startTimestamp);
		return docketHistory;
	}
	
	protected DocketHistory createDocketHistory(SourceDocketMetadata sourceDocketMetadata, Date versionTimestamp, DocketHistoryTypeEnum docketHistoryTypeEnum) {
		DocketHistory docketHistory = new DocketHistory(
				UUIDGenerator.createUuid().toString(), sourceDocketMetadata.getLegacyId(),
				docketHistoryTypeEnum, versionTimestamp, 
				sourceDocketMetadata.getRequestName(), null, sourceDocketMetadata.getSourceFile());
		return docketHistory;
	}
	
	protected DocketHistory createDocketHistory(SourceDocketMetadata sourceDocketMetadata, Date versionTimestamp, DocketHistoryTypeEnum docketHistoryTypeEnum, Date startTimestamp) {
		DocketHistory docketHistory = new DocketHistory(
				UUIDGenerator.createUuid().toString(), sourceDocketMetadata.getLegacyId(),
				docketHistoryTypeEnum, versionTimestamp, 
				sourceDocketMetadata.getRequestName(), null, sourceDocketMetadata.getSourceFile(), startTimestamp);
		return docketHistory;
	}

	protected DocketHistory createDocketHistory(String legacyId, Date date, DocketHistoryTypeEnum docketHistoryTypeEnum, PublishingRequest publishingRequest) {
		DocketHistory docketHistory = new DocketHistory(
				UUIDGenerator.createUuid().toString(), legacyId,
				docketHistoryTypeEnum, date, 
				publishingRequest.getRequestName(), null, null, publishingRequest.getStartDate());
		return docketHistory;
	}
	
	protected DocketVersion createDocketVersion(DocketVersion docketVersion, SourceDocketMetadata sourceDocketMetadata, String contentUuid, Date versionTimestamp, Phase phase)
			throws DocketsPersistenceException, AcquisitionTimeException
	{
		DocketVersion newDocketVersion = new DocketVersion();
		DocketVersionKey docketVersionKey = null;
		if(null!=docketVersion)
		{
			docketVersionKey = new DocketVersionKey(docketVersion.getPrimaryKey().getLegacyId(), docketVersion.getPrimaryKey().getVendorKey(), docketVersion.getPrimaryKey().getPhase(),versionTimestamp);
			if(docketVersion.getAcquisitionTimestamp().compareTo(sourceDocketMetadata.getAcquisitionTimestamp())==0)
			{
				newDocketVersion.setOperationType("R");
			}
			else if(docketVersion.getAcquisitionTimestamp().compareTo(sourceDocketMetadata.getAcquisitionTimestamp())<1)
			{
				newDocketVersion.setOperationType("U");
			}
			else if (sourceDocketMetadata.getPublishingRequest().isAcquisitionTimeOverride())
			{
				newDocketVersion.setOperationType("R");
			}
			else {
				throw new AcquisitionTimeException("Newer version of docket already processed. Acquisition timestamp of docket in DCR: "+ docketVersion.getAcquisitionTimestamp() +". Acquisition timestamp of docket in current load: " +
						sourceDocketMetadata.getAcquisitionTimestamp() +".");
			}
			if(sourceDocketMetadata.isDeleteOperation())
			{
				newDocketVersion.setOperationType("D");
			}
			newDocketVersion.setVersion(docketVersion.getVersion() + 1L);
			newDocketVersion.setContentUuid(docketVersion.getContentUuid());
		}
		else
		{
			docketVersionKey = new DocketVersionKey(sourceDocketMetadata.getLegacyId(), new Long(sourceDocketMetadata.getVendorId()), phase, versionTimestamp);
			if(sourceDocketMetadata.isDeleteOperation())
			{
				newDocketVersion.setOperationType("D");
			}
			else
			{
				newDocketVersion.setOperationType("I");
			}		
			newDocketVersion.setVersion(1L);
			try 
			{
				newDocketVersion.setContentId(contentUuid);
			} 
			catch (UUIDException e) 
			{
				//TODO::Need to do something about this
			}
		}
		newDocketVersion.setPrimaryKey(docketVersionKey);
		newDocketVersion.setAcquisitionMethod(sourceDocketMetadata.getAcquisitionMethod());
		AcquisitionStatus acquisitionStatus = docketService.findAcquisitionStatusForGivenStatus(sourceDocketMetadata.getAcquisitionStatus());
		if (acquisitionStatus != null) {
			newDocketVersion.setAcquisitionStatus(acquisitionStatus.getStatusId());
		}
		newDocketVersion.setAcquisitionTimestamp(sourceDocketMetadata.getAcquisitionTimestamp());
		newDocketVersion.setSourceFile(sourceDocketMetadata.getSourceFile());
		newDocketVersion.setContentSize(sourceDocketMetadata.getContentFileSize());
		newDocketVersion.setProductId(sourceDocketMetadata.getProduct().getId());		
		newDocketVersion.setCourt(sourceDocketMetadata.getCourt());
		
		return newDocketVersion;
	}
	
	protected ContentVersion createContentVersion(DocketVersion docketVersion, File contentFile, String contentUuid, Date versionTimeStamp, SourceDocketMetadata sourceDocketMetadata) throws IOException 
	{
		ContentVersionPK contentVersionPK;
		if(null!=docketVersion)
		{
			contentVersionPK = new ContentVersionPK(docketVersion.getContentId(), docketVersion.getVersion()+1L);
		}
		else
		{
			contentVersionPK = new ContentVersionPK(contentUuid, 1l);
		}
		ContentVersion contentVersion = new ContentVersion();
		contentVersion.setPrimaryKey(contentVersionPK);
		contentVersion.setCreatedOn(versionTimeStamp);
		contentVersion.setCreatedBy(CoreConstants.DOCKETS_CONTENT_SERVICE);
		contentVersion.setProductId(sourceDocketMetadata.getProduct().getId());
		contentVersion.setCourtId(sourceDocketMetadata.getCourt().getPrimaryKey());
		if(contentFile != null){
			contentVersion.setContentSize(contentFile.length());
			contentVersion.setFileimage(FileUtils.readFileToByteArray(contentFile));
		}
   		return contentVersion;
	}
	
	protected DocketVersionRel createDocketVersionRel(DocketVersion sourceDocketVersion, DocketVersion targetDocketVersion, Date versionTimeDate, String legacyId, DocketVersionRelTypeEnum docketVersionRelTypeEnum){
		String relationId = UUIDGenerator.createUuid().toString();
		DocketVersionRel docketVersionRel = new DocketVersionRel(relationId,
				sourceDocketVersion.getContentUuid().toString(), sourceDocketVersion.getVersion(),
				targetDocketVersion.getContentUuid().toString(), targetDocketVersion.getVersion(),
				docketVersionRelTypeEnum, versionTimeDate, legacyId);
		return docketVersionRel;
	}
	
	protected DocketVersionRelHistory createDocketVersionRelHistory(DocketHistory docketHistory, DocketVersionRel docketVersionRel, String legacyId){
		DocketVersionRelHistoryKey joinKey = new DocketVersionRelHistoryKey(docketHistory.getDocketHistoryId(), docketVersionRel.getRelId());
		return new DocketVersionRelHistory(joinKey, legacyId);
	}

	public void setDocketEntityServiceImpl(DocketEntityService docketEntityServiceImpl)
	{
		this.docketEntityServiceImpl = docketEntityServiceImpl;
	}

	public void setDocketService(DocketService docketService)
	{
		this.docketService = docketService;
	}

	public void setDocketHistoryService(DocketHistoryService docketHistoryService)
	{
		this.docketHistoryService = docketHistoryService;
	}

	public void setContentService(ContentService contentService)
	{
		this.contentService = contentService;
	}

	public void setContentVersionService(ContentVersionService contentVersionService)
	{
		this.contentVersionService = contentVersionService;
	}

}
