/** 
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.service;

import static com.trgr.dockets.core.entity.PublishingProcessControlKey.newPublishingProcessControlKeyPb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.ScrollableResults;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.dao.DocketDao;
import com.trgr.dockets.core.dao.ProcessDao;
import com.trgr.dockets.core.domain.DroppedDocketHistory;
import com.trgr.dockets.core.domain.SourceMetadata;
import com.trgr.dockets.core.entity.AcquisitionLookup;
import com.trgr.dockets.core.entity.AcquisitionStatus;
import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.BatchDocket;
import com.trgr.dockets.core.entity.BatchDocketKey;
import com.trgr.dockets.core.entity.BatchDroppedDocket;
import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CaseSubType;
import com.trgr.dockets.core.entity.CaseType;
import com.trgr.dockets.core.entity.CodeTableValues.DroppedProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.CollectionEntity;
import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.CountyCourtNorm;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.CourtCollectionMapKey;
import com.trgr.dockets.core.entity.DispatcherWorkItem;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.PreprocessorWorkItem;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.ProcessType;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingControl;
import com.trgr.dockets.core.entity.PublishingProcessControl;
import com.trgr.dockets.core.entity.PublishingProcessControlKey;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.TransientStgPubRequest;
import com.trgr.dockets.core.util.DocketServiceUtil;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public class DocketServiceImpl implements DocketService {

	private DocketDao dao;
	private ProcessDao processDao;
	
	/** Used to map log file type values to the Java enumerations, like "daily" -> AcquisitionType.DAILY */
	private Map<String, RequestInitiatorTypeEnum> acquisitionTypeValueMap;
	
	public DocketServiceImpl(DocketDao dao) {
		this.dao = dao;
	}
	
	public DocketServiceImpl(DocketDao dao, Map<String, RequestInitiatorTypeEnum> downloadTypeMap) {
		this.dao = dao;
		this.acquisitionTypeValueMap = downloadTypeMap;
	}
	
	public DocketServiceImpl(DocketDao dao, Map<String, RequestInitiatorTypeEnum> downloadTypeMap, ProcessDao processDao) {
		this.dao = dao;
		this.acquisitionTypeValueMap = downloadTypeMap;
		this.processDao = processDao;
	}

	@Override
	@Transactional(readOnly=true)
	public SourceMetadata createSourceMetadata(File metadataFile) throws Exception {
		FileReader fr = new FileReader(metadataFile);
		BufferedReader bufferedReader = new BufferedReader(fr); 
		String line;
		try {
			SourceMetadata metadata = new SourceMetadata();
			// Read the lines of the acquisition log file to fetch dynamic values
			while (((line = bufferedReader.readLine()) != null)) {
				// Just in case if the Aq file is all in one line. Bug 144892
				String[] splited = line.split("><");
				for (String eachlin : splited) {
					DocketServiceUtil.processMetadataFileLine(eachlin, metadata, acquisitionTypeValueMap, this);
				}
			}
			if (metadata.getDocketNumbers().isEmpty()) {
				throw new RuntimeException("No docket numbers in source metadata");
			}
			return metadata;
		} finally {
			fr.close();
		}
	}
	
	@Override
	@Transactional
	public void deletePreprocessorWork(PublishingRequest publishingRequest) {
		dao.deletePreprocessorWork(publishingRequest);
	}
	@Override
	@Transactional(readOnly=true)
	public Batch findBatchByPrimaryKey(String batchId) {
		return dao.findBatchByPrimaryKey(batchId);
	}
	@Override
	@Transactional(readOnly=true)
	public List<Batch> findBatchesByParentBatch(Batch parentBatch) {
		return dao.findBatchesByParentBatch(parentBatch);
	}
	
	@Override
	@Transactional(readOnly=true)
	public BatchDocket findBatchDocketByPrimaryKey(BatchDocketKey primaryKey) {
		return dao.findBatchDocketByPrimaryKey(primaryKey);
	}
	@Override
	@Transactional(readOnly=true)
	public BatchDocket findBatchDocketByContentId(UUID contentUuid) {
		return dao.findBatchDocketByContentId(contentUuid);
	}
	@Override
	@Transactional(readOnly=true)
	public ScrollableResults findBatchDocketKeys(String batchId) {
		return dao.findBatchDocketKeys(batchId);
	}
	@Override
	@Transactional(readOnly=true)
	public ScrollableResults findBatchDockets(String batchId){
		return dao.findBatchDockets(batchId);
	}
	@Override
	@Transactional(readOnly=true)
	public List<BatchDocketKey> findBatchDocketKeysList(String batchId) {
		return dao.findBatchDocketKeysList(batchId);
	}
	@Override
	@Transactional(readOnly=true)
	public List<DocketEntity> findDocketsByCourtIdFilingYearSequenceNumber(long courtId, long filingYear, String sequenceNumber) {
		return dao.findDocketsByCourtIdFilingYearSequenceNumber(courtId,filingYear,sequenceNumber);
	}
	@Override
	@Transactional(readOnly=true)
	public BatchDroppedDocket findBatchDroppedDocket(Long primaryKey) {
		return dao.findBatchDroppedDocket(primaryKey);
	}
	@Override
	@Transactional(readOnly=true)
	public BatchMonitor findBatchMonitorByPrimaryKey(BatchMonitorKey primaryKey) {
		return dao.findBatchMonitorByPrimaryKey(primaryKey);
	}
	@Override
	@Transactional(readOnly=true)
	public List<BatchMonitor> findBatchMonitorByRequestId(UUID requestId) 
	{
		return dao.findBatchMonitorByRequestId(requestId);
	}
	@Override
	@Transactional(readOnly=true)
	public CollectionEntity findCollectionByPrimaryKey(Long pk) {
		return dao.findCollectionByPrimaryKey(pk);
	}
	@Override
	@Transactional(readOnly=true)
	public CollectionEntity findCollectionByCollectionName(String collectionName)
	{
		return dao.findCollectionByCollectionName(collectionName);
	}
	
	@Override
	@Transactional(readOnly=true)
	public String findPubIdByCollectionName(String metadocCollectionName) {
		return dao.findPubIdByCollectionName(metadocCollectionName);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List<String> findCourtCollMapTypes(Court court){
		return dao.findCourtCollMapTypesByCourt(court);
	}
	@Override
	@Transactional(readOnly=true)
	public Court findCourtByCourtCluster(String courtCluster) {
		return dao.findCourtByCourtCluster(courtCluster);
	}
	@Override
	@Transactional(readOnly=true)
	public List<Court> loadCourt() {
		return dao.loadCourt();
	}
	@Override
	@Transactional(readOnly=true)
	public Court findCourtByCourtNorm(String courtNorm) {
		return dao.findCourtByCourtNorm(courtNorm);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Court findCourtIfCountyCourtUnique(String countyName, Integer... courtIds) throws Exception{
		return dao.findCourtIfCountyCourtUnique(countyName, courtIds);
	}

	
	@Override
	public List<CountyCourtNorm> getCountyCourtNorms(){
		return dao.getCountyCourtNorms();
	}
	@Override
	@Transactional(readOnly=true)
	public DispatcherWorkItem findDispatcherWorkItemByBatchId(String batchId) {
		return dao.findDispatcherWorkItemByBatchId(batchId);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findDispatcherWorkItemByWorkId(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public DispatcherWorkItem findDispatcherWorkItemByWorkId(Long workId){
		return dao.findDispatcherWorkItemByWorkId(workId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Court findCourtByPrimaryKey(Long primaryKey) {
		return dao.findCourtByPrimaryKey(primaryKey);
	}
	@Override
	@Transactional(readOnly=true)
	public DocketEntity findDocketByPrimaryKey(String legacyId) {
		return dao.findDocketByPrimaryKey(legacyId);
	}
	
	@Override
	public Map<String, String> getDocketVersionInfoByContentUuid (UUID contentUuid){
		String legacyId = dao.getLegacyIdByContentUuid(contentUuid);
		
		return dao.getDocketNumberByLegacyId(legacyId);
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public DocketVersion findJudicialDocketVersionWithHighestVersion(String legacyId, long vendorId, long productId, Court court) {
		return dao.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court);
	}
	
	@Override
	@Transactional(readOnly=true)
	public DocketVersion findJudicialDocketVersionWithLatestTimestamp(String legacyId, long productId, long courtId) {
		return dao.findJudicialDocketVersionWithLatestTimestamp(legacyId, productId, courtId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public DocketVersion findHighestJudicialDocketVersionForGivenProductAndLegacyId(String legacyId, long productId) {
		return dao.findHighestJudicialDocketVersionForGivenProductAndLegacyId(legacyId,productId);
	}

	@Override
	@Transactional(readOnly=true)
	public Process findProcessByPrimaryKey(UUID processId) {
		return dao.findProcessByPrimaryKey(processId);
	}

	@Override
	@Transactional(readOnly=true)
	public Product findProductByBatch(String batchId) {
		Batch batch = findBatchByPrimaryKey(batchId);
		if (batch != null) {
			return batch.getPublishingRequest().getProduct();
		}
		return null;
	}
	@Override
	@Transactional(readOnly=true)
	public PublishingRequest findPublishingRequestByPrimaryKey(UUID primaryKey) {
		return dao.findPublishingRequestByPrimaryKey(primaryKey);
	}
	@Override
	@Transactional(readOnly=true)
	public DocketVersion findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId) {
			return dao.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId,productId);
	}
	 @Override
	 @Transactional(readOnly=true)
	 public DocketVersion findSourceDocketVersionWithHighestVersionForVendorAndProduct(String legacyId, long vendorId, long productId) {
		 return dao.findSourceDocketVersionWithHighestVersionForVendorAndProduct(legacyId,vendorId,productId);
	 }
	@Override
	@Transactional(readOnly=true)
	public DocketVersion findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId){
		return dao.findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public DocketVersion findLatestDocketVersionForProductCourtLegacyIdPhase(String legacyId, Court court, long productId,Phase phase){
		return dao.findLatestDocketVersionForProductCourtLegacyIdPhase(legacyId, court, productId, phase);
	}	
	
	@Override
	@Transactional(readOnly=true)
	public DocketVersion findNovusDocketVersionWithHighestVersionForProductAndCourt(String legacyId, Court court, long productId){
		return dao.findNovusDocketVersionWithHighestVersionForProductAndCourt(legacyId, court, productId);
	}
	
	@Override
	public List<TransientStgPubRequest> findCollectionByPublishingRequestId(String requestKey){
		return dao.findCollectionByPublishingRequestId(requestKey);
	}
	
	@Override
	@Transactional(readOnly=true)
	public StatusEnum getAggregateBatchMonitorStatus(UUID publishingRequestUuid) {
		StatusEnum requestStatusEnum = StatusEnum.FAILED;
		int failedBatchCount = 0;
		List<BatchMonitor> batchMonitorList = findBatchMonitorByRequestId(publishingRequestUuid);
		int batchMonitorListSize = batchMonitorList.size();
		for (BatchMonitor batchMonitor : batchMonitorList) {
			Batch batch = dao.findBatchByPrimaryKey(batchMonitor.getBatchId());
			if(batch.getParentBatch() != null){
    			requestStatusEnum = getRequestLevelStatusFromBatchMonitorStatus(requestStatusEnum, StatusEnum.findByKey(batchMonitor.getStatus().getId()));
    			if(requestStatusEnum == StatusEnum.FAILED){
    				failedBatchCount++;
    				requestStatusEnum = StatusEnum.COMPLETED_WITH_ERRORS;
    			}
			}else{
				batchMonitorListSize = batchMonitorListSize -1;
			}
		}
		if(failedBatchCount == batchMonitorListSize){
			requestStatusEnum = StatusEnum.FAILED;
		}
		return requestStatusEnum;
	}
	
	/**
	 * Retrieves all the batches for given request uuid and returns true of any has failed.
	 * @param publishingRequestUuid
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public boolean checkIfAnyBatchFailedForGivenRequestId(UUID publishingRequestUuid)
	{
		boolean batchFailedFlag = false ;
		
		List<BatchMonitor> batchMonitorList = findBatchMonitorByRequestId(publishingRequestUuid);
		for (BatchMonitor batchMonitor : batchMonitorList)
		{
			
			if(batchMonitor.getStatus().getStatus().equalsIgnoreCase("FAILED"))
			{
				batchFailedFlag = true ;
				break;
			}
		}
		
		return batchFailedFlag;
		
	}
	
	@Override
	public StatusEnum getRequestLevelStatusFromBatchMonitorStatus(StatusEnum status1, StatusEnum status2){
		if (status1 == StatusEnum.PENDING || status2 == StatusEnum.PENDING || status1 == StatusEnum.RUNNING || status2 == StatusEnum.RUNNING) {
			return StatusEnum.RUNNING;
		}
		if(status1 == StatusEnum.PAUSED || status2 == StatusEnum.PAUSED)
		{
			return StatusEnum.PAUSED;
		}
		if (status2 == StatusEnum.FAILED) {
			return StatusEnum.FAILED;
		}
		if (status1 == StatusEnum.COMPLETED_WITH_ERRORS || status2 == StatusEnum.COMPLETED_WITH_ERRORS) {
			return StatusEnum.COMPLETED_WITH_ERRORS;
		}
		if (status1 == StatusEnum.COMPLETED_SUCCESSFULLY || status2 == StatusEnum.COMPLETED_SUCCESSFULLY) {
			return StatusEnum.COMPLETED_SUCCESSFULLY;
		}
		return StatusEnum.FAILED;
	}

	/**
	 * @see com.trgr.dockets.core.service.DocketService#getDocketContentStream
	 */
	@Override
	@Transactional(readOnly=true)
	public InputStream getDocketContentStream(UUID contentUuid) {
		return dao.getDocketContentStream(contentUuid);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ArrayList<HashMap<String,Object>> findContentUuidListBySingleContentUuid(UUID contentUuid){
		return dao.findContentUuidListBySingleContentUuid(contentUuid);
	}
	
	@Override
	@Transactional(readOnly=true)
	public boolean isAllDispatcherWorkStarted(UUID publishingRequestUuid) {
		List<DispatcherWorkItem> allWorkItems = dao.findDispatcherWorkItemsByRequestId(publishingRequestUuid);
		for (DispatcherWorkItem item : allWorkItems) {
			if (!item.isJobStarted()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<PublishingControl> findAllPublishingControl() {
		return dao.findAllPublishingControl();
	}
	@Override
	@Transactional(readOnly=true)
	public List<PublishingProcessControl> findAllPausedPublishingProcessControl(ProcessType processType) {
		return dao.findAllPausedPublishingProcessControl(processType);
	}
	@Override
	@Transactional(readOnly=true)
	public PublishingControl findPublishingControlByPrimaryKey(long pk) {
		return dao.findPublishingControlByPrimaryKey(pk);
	}
	
	@Override
	@Transactional(readOnly=true)
	public PublishingProcessControl findPublishingProcessControlByPrimaryKey(PublishingProcessControlKey pk) {
		return dao.findPublishingProcessControlByPrimaryKey(pk);
	}

	@Override
	@Transactional(readOnly=true)
	public PublishingProcessControl findPublishingProcessControlByPrimaryKeyPb(PublishingControl publishingControl) {
		return (publishingControl == null) ? null :
			findPublishingProcessControlByPrimaryKey(
					newPublishingProcessControlKeyPb(publishingControl.getPrimaryKey()));
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public PublishingControl findPublishingControlByProductAndCourt(Product product, Court court)
//	{
//		return dao.findPublishingControlByProductAndCourt(product,court);
//	}
	
	@Override
	@Transactional(readOnly=true)
	public PublishingControl findPublishingControlByProductCourtAndVendorId(Product product,Court court,long vendorId)
	{
		return dao.findPublishingControlByProductCourtAndVendorId(product,court,vendorId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public PublishingControl findPublishingControlByProduct(Product product)
	{
		return dao.findPublishingControlByProduct(product);
	}
	@Override
	@Transactional(readOnly=true)
	public List<BatchDroppedDocket> findBatchDroppedDocketByBatchId(String batchId) {
		return dao.findBatchDroppedDocketByBatchId(batchId);
	}
	@Override
	@Transactional
	public void endProcessBatchRequestAndNegativeHistory(Process process, BatchMonitor batchMonitor, List<DroppedDocketHistory> droppedDocketHistoryList,
			 PublishingRequest publishingRequest, DocketHistoryTypeEnum docketHistoryTypeEnum, File mergeFilname) {					 	
		update(publishingRequest);
		update(batchMonitor);
		endProcessAndNegativeHistory( process,  droppedDocketHistoryList, publishingRequest,  docketHistoryTypeEnum,  mergeFilname);
		}
	
	@Override
	@Transactional
	public void endProcessAndNegativeHistory(Process process, List<DroppedDocketHistory> droppedDocketHistoryList,
											 PublishingRequest publishingRequest, DocketHistoryTypeEnum docketHistoryTypeEnum, File mergeFilname) {
		update(process);

		for (DroppedDocketHistory droppedDocketHistory : droppedDocketHistoryList)
		{
		if(droppedDocketHistory != null && droppedDocketHistory.getBatchDroppedDocket() != null)
		{
			if(mergeFilname == null )
			{
				saveNegativeHistory(droppedDocketHistory, publishingRequest, docketHistoryTypeEnum);
			}
			else 
			{
				saveNegativeHistory(droppedDocketHistory, publishingRequest, docketHistoryTypeEnum, mergeFilname);
			}
		}
		}
				
	}
	
	@Override
	@Transactional
	public DocketHistory saveNegativeHistory(DroppedDocketHistory droppedDocketHistory,PublishingRequest publishingRequest, DocketHistoryTypeEnum type) 
	{
		
		DocketHistory docketHistory = createDocketHistory(droppedDocketHistory,publishingRequest, type);

		dao.save(droppedDocketHistory.getBatchDroppedDocket());  // BATCH_DROPPED_DOCKET insert
		dao.save(docketHistory);	// DOCKET_HISTORY insert
		
		saveAppropriateLastTimeOnDocketBasedOnType(docketHistory.getLegacyId(),type);
		
		//Update Dropped Reason for Docket.
		DroppedProcessTypeEnum processName = droppedDocketHistory.getProcessName();
		Long droppedReasonId = droppedDocketHistory.getDroppedReasonId();
		if(processName != null)
		{
			updateDocketDroppedReasonForDocket(docketHistory.getLegacyId(), processName, droppedReasonId, publishingRequest.getRequestTypeKey());
		}
		
		return docketHistory;
	}
	
	@Override
	@Transactional
	public DocketHistory saveNegativeHistory(DroppedDocketHistory droppedDocketHistory,PublishingRequest publishingRequest, DocketHistoryTypeEnum type, File fileName) 
	{
		
		DocketHistory docketHistory = createDocketHistory(droppedDocketHistory,publishingRequest, type);
		docketHistory.setFile(fileName);

		dao.save(droppedDocketHistory.getBatchDroppedDocket());  // BATCH_DROPPED_DOCKET insert
		dao.save(docketHistory);	// DOCKET_HISTORY insert
		//based on type
		//Conversion - last convertedDate
		//DPB - last publishedDate
		saveAppropriateLastTimeOnDocketBasedOnType(docketHistory.getLegacyId(),type);
		
		//Update Dropped Reason for Docket.
		DroppedProcessTypeEnum processName = droppedDocketHistory.getProcessName();
		Long droppedReasonId = droppedDocketHistory.getDroppedReasonId();
		if(processName != null)
		{
			updateDocketDroppedReasonForDocket(docketHistory.getLegacyId(), processName, droppedReasonId, publishingRequest.getRequestTypeKey());
		}
		
		return docketHistory;
	}
	
	@Override
	@Transactional
	public void updateDocketDroppedReasonForDocket(String legacyId,
			DroppedProcessTypeEnum processName, Long droppedReasonId, Long requestTypeId) {
		
		DocketEntity docketEntity = dao.findDocketByPrimaryKey(legacyId);
		if(null != docketEntity) {
			if(processName.equals(DroppedProcessTypeEnum.PP)){
				if(droppedReasonId == null) droppedReasonId = 100l;
					docketEntity.setLastPpDroppedReason(droppedReasonId);
			}
			else if(processName.equals(DroppedProcessTypeEnum.IC)){
				if(droppedReasonId == null) droppedReasonId = 200l;
				docketEntity.setLastIcDroppedReason(droppedReasonId);
				if (requestTypeId == 6 || requestTypeId == 7 || requestTypeId == 8)
				{
					docketEntity.setLastPpDroppedReason(null);
				}
			}
			else if(processName.equals(DroppedProcessTypeEnum.PB)){
				if(droppedReasonId == null) droppedReasonId = 300l;
				docketEntity.setLastPbDroppedReason(droppedReasonId);
				if (requestTypeId == 7 || requestTypeId == 8)
				{
					docketEntity.setLastPpDroppedReason(null);
				}
				if (requestTypeId == 7 || requestTypeId == 8 || requestTypeId == 10 || requestTypeId == 2)
				{
					docketEntity.setLastIcDroppedReason(null);
				}
			}
			else if(processName.equals(DroppedProcessTypeEnum.NL)){
				if(droppedReasonId == null) droppedReasonId = 400l;
				docketEntity.setLastNlDroppedReason(droppedReasonId);
				if (requestTypeId == 8)
				{
					docketEntity.setLastPpDroppedReason(null);
				}
				if (requestTypeId == 8 || requestTypeId == 2)
				{
					docketEntity.setLastIcDroppedReason(null);
				}
				if (requestTypeId == 8 || requestTypeId == 2 || requestTypeId == 4)
				{
					docketEntity.setLastPbDroppedReason(null);
				}
			}
			dao.save(docketEntity);		
		}
	}
	
	private void saveAppropriateLastTimeOnDocketBasedOnType(String legacyId, DocketHistoryTypeEnum type)
	{
		DocketEntity docketEntity =  dao.findDocketByPrimaryKey(legacyId);
		
		if(null!=docketEntity)
		{
			if(type.equals(DocketHistoryTypeEnum.CONVERSION_HISTORY))
			{
				docketEntity.setLastConvertedDate(new Date());
				dao.update(docketEntity);
			}
			else if(type.equals(DocketHistoryTypeEnum.PUBLISH_HISTORY))
			{
				docketEntity.setLastPublishDate(new Date());
				dao.update(docketEntity);
			}
		}
	}
	
	private static DocketHistory createDocketHistory(DroppedDocketHistory droppedDocketHistory,
			 PublishingRequest publishingRequest,DocketHistoryTypeEnum type )
	{
		DocketHistory docketHistory = new DocketHistory();
		docketHistory.setDocketHistoryId(UUIDGenerator.createUuid().toString());
		docketHistory.setLegacyId(droppedDocketHistory.getLegacyId());
		docketHistory.setDroppedDocketReason(droppedDocketHistory.getBatchDroppedDocket().getErrorDescription());
		docketHistory.setRequestName(publishingRequest.getRequestName());
		docketHistory.setRequestStartTimestamp(publishingRequest.getStartDate());
		docketHistory.setTimestamp(new Date());
		docketHistory.setType(type);
		return docketHistory;
	}
	
	@Override
	@Transactional
	public Object save(Object entity) {
		return dao.save(entity);
	}
	@Override
	@Transactional
	public void update(Object entity) {
		dao.update(entity);
	}
	
	@Override
	@Transactional
	public void merge(Object entity){
		dao.merge(entity);
	}
	
	@Override
	@Transactional
	public void mergeAndDelete(final PreprocessorWorkItem entity) {
		final PreprocessorWorkItem newEntity = dao.mergeOnly(entity);
		dao.delete(newEntity);
	}	
	
	/* 
	 * (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findAcquisitionLookupByRequestKey(com.westgroup.publishingservices.uuidgenerator.UUID)
	 */
	@Override
	public AcquisitionLookup findAcquisitionLookupByRequestKey(UUID requestKey){
		return dao.findAcquisitionLookupByRequestKey(requestKey);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findAcquisitionLookupByReceiptId(java.lang.String)
	 */
	@Override
	@Transactional
	public AcquisitionLookup findAcquisitionLookupByReceiptId(String receiptId){
		return dao.findAcquisitionLookupByReceiptId(receiptId);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findCaseSubTypeByCaseSubTypeAndProduct(java.lang.String, com.trgr.dockets.core.entity.Product)
	 */
	@Override
	@Transactional
	public CaseSubType findCaseSubTypeByCaseSubTypeAndProduct(String caseSubType, Product product){
		return dao.findCaseSubTypeByCaseSubTypeAndProduct(caseSubType, product);
	}
	
	@Transactional 
	@Override
	public AcquisitionStatus findAcquisitionStatusForGivenStatus(String status){
		return  dao.findAcquisitionStatusForGivenStatus(status);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findCaseSubTypeByCaseSubTypeIdAndProduct(java.lang.Long, com.trgr.dockets.core.entity.Product)
	 */
	@Override
	@Transactional
	public CaseSubType findCaseSubTypeByCaseSubTypeIdAndProduct(Long caseSubTypeId, Product product){
		return dao.findCaseSubTypeByCaseSubTypeIdAndProduct(caseSubTypeId, product);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findCourtCollectionMapKeyByMapKey(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public CourtCollectionMapKey findCourtCollectionMapKeyByKeyValues(Long courtId, List<?> mapKeyValues){
		return dao.findCourtCollectionMapKeyByKeyValues(courtId, mapKeyValues);
	}
	
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findCourtCollectionMapKeyByMapKey(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public List<CourtCollectionMapKey> findCourtCollectionMapKeysByCourt(Long courtId){
		return dao.findCourtCollectionMapKeysByCourt(courtId);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#findBatchCollection(java.lang.String)
	 */
	@Override
	@Transactional
	public CollectionEntity findBatchCollection(String batchId){
		return dao.findBatchCollection(batchId);
	}

	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#isBatchContainDeletes(java.lang.String)
	 */
	@Override
	@Transactional
	public boolean isBatchContainDeletes(String batchId) {
		return dao.isBatchContainDeletes(batchId);
	}
	/**
	 * 
	 * @param batchId
	 * @return
	 */
	@Override
	@Transactional
	public boolean isBatchContainAdds(String batchId){
		return dao.isBatchContainAdds(batchId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public String findIfFifoPreprocessorWork(PublishingRequest publishingRequest) {
		return dao.findIfFifoPreprocessorWork(publishingRequest);
	}

	@Override
	@Transactional(readOnly=true)
	public PreprocessorWorkItem findPreprocessorWorkItemByPublishingRequest(PublishingRequest publishingRequest) {
		return dao.findPreprocessorWorkItemByPublishingRequest(publishingRequest);
	}
	
	@Override
	public String findIfCourtIsFifo(Court court) {
		return dao.findIfCourtIsFifo(court);
	}
	@Override
	public List<Process> findProcessesFailedForProcess(String publishingRequestId,Long processTypeId) {
		return dao.findProcessesFailedForProcess(publishingRequestId,processTypeId);
	}

	@Override
	@Transactional
	public List<Process> findProcessesFailed(String publishingRequestId) {
		return dao.findProcessesFailed(publishingRequestId);
	}
	
	@Override
	public List<CaseType> getCaseTypeTable(){
		return dao.getCaseTypeTable();
	}
	
	@Override
	public List<CaseSubType> getCaseSubTypeTable(Long courtId, Long productId){
		return dao.getCaseSubTypeTable(courtId,productId);
	}
	
	@Override
	public List<County> getCountyTable(){
		return dao.getCountyTable();
	}

	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.DocketService#getPublishingRequestStatus(com.trgr.dockets.core.entity.CodeTableValues.StatusEnum, java.lang.String)
	 */
	@Override
	@Transactional
	public StatusEnum getPublishingRequestStatus(
			StatusEnum aggregateBatchStatus, String uberBatchId) {
		boolean considerUberBatchProcesses = false;
		StatusEnum publishingRequestStatusEnum = aggregateBatchStatus;
		StatusEnum uberBatchStatusEnum = StatusEnum.COMPLETED_SUCCESSFULLY;
		List<Process> uberBatchProcessList = processDao.findProcessesByBatchId(uberBatchId);
		if(uberBatchProcessList.size() > 0){
			for(Process process : uberBatchProcessList){
				uberBatchStatusEnum = StatusEnum.max(uberBatchStatusEnum, process.getStatusEnum());
				if(process.getProcessType().getPrimaryKey().equals(ProcessTypeEnum.SCL.getKey())){
					considerUberBatchProcesses = true;
				}
			}
			if(considerUberBatchProcesses){
				publishingRequestStatusEnum = StatusEnum.bestCaseStatus(aggregateBatchStatus, uberBatchStatusEnum);
			}
		}
		return publishingRequestStatusEnum;
	}
	
	@Override
	@Transactional
	public StatusEnum getPreprocessorProcessStatus(String uberBatchId) {
		StatusEnum ppStatusEnum = null;
		List<Process> uberBatchProcessList = processDao.findProcessesByBatchTypeId(uberBatchId, 10l);
		// Process Type 10 is SCL court (DCRSourceContentLoad ) and Process Type 20 is PreProcessor court
		if(uberBatchProcessList.size() == 0){
			uberBatchProcessList = processDao.findProcessesByBatchTypeId(uberBatchId, 20l);
		}
		
		if(uberBatchProcessList.size() > 0){
			for(Process process : uberBatchProcessList){
				ppStatusEnum = StatusEnum.findByKey(process.getStatus().getId());
			}
		}
		return ppStatusEnum;
	}
	
	@Override
	@Transactional
	public List<Process> getProcessListByBatchId(String uberBatchId){
		return processDao.findProcessesByBatchId(uberBatchId);
	}

	@Override
	public Map<String, String> findRpxConfigByCourtId(Long courtId) {
		return dao.findRpxConfigByCourtId(courtId);
	}
	
	@Override
	public Map<String, String> findRpxConfigByProductId(Long productId) {
		return dao.findRpxConfigByProductId(productId);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public int updateDocketReport(String legacyId) {
		return dao.updateDocketReport(legacyId);
		
	}
	
	@Override
	@Transactional
	public char getLargeDocketFlag(String legacyId){
		return dao.getLargeDocketFlagByLegacyId(legacyId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public boolean isLargeDocket(String batchId) {
		List<BatchDocketKey> batchKeys = findBatchDocketKeysList(batchId);
		if(batchKeys.size() == 1) {
			BatchDocket batchDocket = findBatchDocketByPrimaryKey(batchKeys.get(0));
			if (batchDocket.getLargeDocketFlag() == 'Y') {
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly=true)
	public List<DispatcherWorkItem> findDispatcherWorkItemsByRequestId(UUID publishingRequestUuid) {
		List<DispatcherWorkItem> allWorkItems = dao.findDispatcherWorkItemsByRequestId(publishingRequestUuid);
		return allWorkItems;
	}
}

