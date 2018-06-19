/** 
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.service;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.ScrollableResults;

import com.trgr.dockets.core.CoreConstants.Phase;
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
import com.westgroup.publishingservices.uuidgenerator.UUID;

/**
 * A Docket content and meta-data service.
 */
public interface DocketService {

	/**
	 * Parse the acquision log metadata file and return a container object
	 * that summarizes the contents of the file.
	 * @param metadataFile the acquisition log file
	 * @return a summary container object for the metadata
	 * @throws Exception on file I/O or parsing error
	 */
	public SourceMetadata createSourceMetadata(File metadataFile) throws Exception;
	/**
	 * Remove the row from the preprocessor_work table that has the specified court name and a non-null start time.
	 * This is used to unlock the semaphore used to enforce the "only one of the same court at a time" business rule.
	 * @param publishingRequest delete the record that has this pub request key
	 */
	public void deletePreprocessorWork(PublishingRequest publishingRequest);
	
	public Batch findBatchByPrimaryKey(String batchId);
	
	public BatchDocket findBatchDocketByPrimaryKey(BatchDocketKey primaryKey);
	public BatchDocket findBatchDocketByContentId(UUID contentUuid);
	public ScrollableResults findBatchDocketKeys(String batchId);
	public ScrollableResults findBatchDockets(String batchId);
	public List<BatchDocketKey> findBatchDocketKeysList(String batchId);
	/**
	 * Checks if this batch is carrying a large docket 
	 * @author <a href="mailto:tyler.studanski@thomsonreuters.com">Tyler Studanski</a> <b>u0162605</b>
	 * @param batchId ID for the batch to check
	 * @return Returns true if only 1 docket exists in the batch and that docket has the LargeDocketFlag set to 'Y'.  Otherwise false.
	 */
	public boolean isLargeDocket(String batchId);
	public BatchDroppedDocket findBatchDroppedDocket(Long primaryKey);
	public BatchMonitor findBatchMonitorByPrimaryKey(BatchMonitorKey primaryKey);
	public List<BatchMonitor> findBatchMonitorByRequestId(UUID publishingRequestUuid);
	public CollectionEntity findCollectionByPrimaryKey(Long pk);
	public CollectionEntity findCollectionByCollectionName(String collectionName);
	public String findPubIdByCollectionName(String metadocCollectionName);
	public List<String> findCourtCollMapTypes(Court court);
	public Court findCourtByCourtCluster(String courtCluster);
	public Court findCourtByCourtNorm(String courtNorm);
	public Court findCourtByPrimaryKey(Long primaryKey);
	public Court findCourtIfCountyCourtUnique(String countyName, Integer... courtIds) throws Exception;
	public List<CountyCourtNorm> getCountyCourtNorms();
	public DispatcherWorkItem findDispatcherWorkItemByBatchId(String batchId);
	public DispatcherWorkItem findDispatcherWorkItemByWorkId(Long workId);
	public DocketEntity findDocketByPrimaryKey(String legacyId);
	public PublishingControl findPublishingControlByPrimaryKey(long pk);
	public List<PublishingControl> findAllPublishingControl();
	public PublishingProcessControl findPublishingProcessControlByPrimaryKey(PublishingProcessControlKey pk);
	public PublishingProcessControl findPublishingProcessControlByPrimaryKeyPb(PublishingControl publishingControl);
	
	public StatusEnum getAggregateBatchMonitorStatus(UUID publishingRequestUuid);
	
	public Map<String, String> getDocketVersionInfoByContentUuid (UUID contentUuuid);

	public DocketVersion findJudicialDocketVersionWithHighestVersion(String legacyId, long vendorId, long productId, Court court);
	public Process findProcessByPrimaryKey(UUID processId);
	
	/**
	 * Get the product from the PUBLISHIHG_REQUEST table for a given batch ID.
	 * @param batchId the batch key
	 * @return the found product, or null if no such batch ID
	 */
	public Product findProductByBatch(String batchId);
	
	/**
	 * Get the batchDroppedDockets from the batch_dropped_dockets table for a given batch ID.
	 * @param batchId the batch key
	 * @return the found list of BatchDroppedDockets, or null if no such batch ID
	 */
	public List<BatchDroppedDocket> findBatchDroppedDocketByBatchId(String batchId);
	 
	
	/**
	 * Get the sub-batches from the batch table for a given batch ID.
	 * @param parent batch the  batchId is parent_batch_id
	 * @return the found list of Batches, or null if no such batch ID
	 */
	public List<Batch> findBatchesByParentBatch(Batch parentBatch) ;

		
	/**
	 * Update both the BATCH_DROPPED_DOCKET and DOCKET_HISTORY tables with bad docket info.
	 * @return
	 */
	public DocketHistory saveNegativeHistory(DroppedDocketHistory droppedDocketHistory, 
				PublishingRequest publishingRequest, DocketHistoryTypeEnum type);
	
	/**
	 * Update Docket table with approproate dropped reason based on reason Id and process.
	 */
	public void updateDocketDroppedReasonForDocket(String legacyId, DroppedProcessTypeEnum processName, Long droppedReasonId, Long requestTypeId);
	
	/**
	 * Update both the BATCH_DROPPED_DOCKET and DOCKET_HISTORY tables with bad docket info.
	 * @return
	 */
	public DocketHistory saveNegativeHistory(DroppedDocketHistory droppedDocketHistory, 
				PublishingRequest publishingRequest, DocketHistoryTypeEnum type, File fileName);

	
	/**
	 * Update PROCESS and potentially both the BATCH_DROPPED_DOCKET and DOCKET_HISTORY tables with bad docket info.
	 * @param process update this object with end time and error codes/descriptions as necessary
	 * @param droppedDocketHistoryList if there are errors then batch_dropped_dockets otherwise null
	 * @param publishingRequest only needs the request_id
	 * @param DocketHistoryTypeEnum SCL: ACQUISITION_HISTORY IC_SUBBATCH/DCRLOAD: CONVERSION_HISTORY 
	 * 								RPX/METADOC/JRECS/DPB_SUBBATCH : PUBLISH_HISTORY
	 * @param fileName merge.filename previously found in dc.end event payload for SCL otherwise null
	 */
	public void endProcessAndNegativeHistory(Process process, List<DroppedDocketHistory> droppedDocketHistoryList,
			 PublishingRequest publishingRequest, DocketHistoryTypeEnum type, File fileName); 

		
	/**
	 * Update PROCESS, then insert BATCH_MONITOR and PUBLISHING_REQUEST
	 *  and potentially both the BATCH_DROPPED_DOCKET and DOCKET_HISTORY tables with bad docket info.
	 * @param process update this object with end time and error codes/descriptions as necessary
	 * @param batchMonitor for insert 
	 * @param droppedDocketHistoryList if there are errors then batch_dropped_dockets otherwise null
	 * @param publishingRequest for insert
	 * @param DocketHistoryTypeEnum SCL: ACQUISITION_HISTORY IC_SUBBATCH/DCRLOAD: CONVERSION_HISTORY 
	 * 								RPX/METADOC/JRECS/DPB_SUBBATCH : PUBLISH_HISTORY
	 * @param fileName merge.filename previously found in dc.end event payload for SCL otherwise null
	 */
	public void endProcessBatchRequestAndNegativeHistory(Process process, BatchMonitor batchMonitor, List<DroppedDocketHistory> droppedDocketHistoryList,
			 PublishingRequest publishingRequest, DocketHistoryTypeEnum type, File fileName); 

	
	public PublishingRequest findPublishingRequestByPrimaryKey(UUID pk);
	
	
	public List<TransientStgPubRequest> findCollectionByPublishingRequestId(String requestKey);
	/**
	 * Fetch docket content as a stream using its content uuid.
	 * @param contentUuid a docket content uuid
	 * @return an InputStream for the docket content with the specified key,
	 * or null if no docket with that key as found.
	 * Note: It is the client's responsibility to close this stream when finished using it.
	 * @throws SQLException if there is an error in accessing the docket data.
	 */
	public InputStream getDocketContentStream(UUID contentUuid);
	public ArrayList<HashMap<String,Object>> findContentUuidListBySingleContentUuid(UUID contentUuid);
	/**
	 * Returns true if all the Dispatcher_Work rows have had a Spring Batch job started for them.
	 * This is determined by checking for an assigned job execution id.
	 * @param publishingRequestUuid
	 * @return
	 */
	public boolean isAllDispatcherWorkStarted(UUID publishingRequestUuid);
	
	public List<DispatcherWorkItem> findDispatcherWorkItemsByRequestId(UUID publishingRequestUuid);
	/**
	 * Insert (only) the entity, cannot be an update
	 * @param entity object to be saved
	 */
	public Object save(Object entity);
	/**
	 * Update the state of the entity.
	 * @param entity object to be updated, PK (ID) is required to be set
	 */
	public void update(Object entity);
	public void merge(Object entity);  // generalized merge
	public void mergeAndDelete (PreprocessorWorkItem Entity);
	
	public DocketVersion findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId);
	public AcquisitionLookup findAcquisitionLookupByRequestKey(UUID requestKey);
	public AcquisitionLookup findAcquisitionLookupByReceiptId(String receiptId);
	public DocketVersion findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court,long vendorId, long productId);
	public DocketVersion findNovusDocketVersionWithHighestVersionForProductAndCourt(String legacyId, Court court, long productId);
	public CaseSubType findCaseSubTypeByCaseSubTypeAndProduct(String caseSubType, Product product);
	public CaseSubType findCaseSubTypeByCaseSubTypeIdAndProduct(Long caseSubTypeId, Product product);
	public CourtCollectionMapKey findCourtCollectionMapKeyByKeyValues(Long courtId, List<?> mapKeyValues);
	public List<CourtCollectionMapKey> findCourtCollectionMapKeysByCourt(Long courtId);
	public CollectionEntity findBatchCollection(String batchId);
	List<DocketEntity> findDocketsByCourtIdFilingYearSequenceNumber(long courtId, long filingYear, String sequenceNumber);
	public boolean isBatchContainDeletes(String batchId);
	public String findIfFifoPreprocessorWork(PublishingRequest publishingRequest);
	public PreprocessorWorkItem findPreprocessorWorkItemByPublishingRequest(PublishingRequest publishingRequest);

	String findIfCourtIsFifo(Court court);
	public List<Process> findProcessesFailedForProcess(String publishingRequestId,Long processTypeId);
	public List<Process> findProcessesFailed(String publishingRequestId);
	public PublishingControl findPublishingControlByProduct(Product product);
	public StatusEnum getPublishingRequestStatus(StatusEnum aggregateBatchStatus, String uberBatchId);
	public StatusEnum getPreprocessorProcessStatus(String uberBatchId);
	public StatusEnum getRequestLevelStatusFromBatchMonitorStatus(StatusEnum status1, StatusEnum status2);
	
	public List<Process> getProcessListByBatchId(String uberBatchId);
	//PublishingControl findPublishingControlByProductAndCourt(Product product,Court court);
	public PublishingControl findPublishingControlByProductCourtAndVendorId(Product product,Court court, long vendorId);

	List<PublishingProcessControl> findAllPausedPublishingProcessControl(ProcessType processType);
	List<Court> loadCourt();
	
	List<CaseType> getCaseTypeTable();
	List<CaseSubType> getCaseSubTypeTable(Long courtId, Long productId);
	List<County> getCountyTable();
	/**
	 * Retrieves all the batches for given request id and returns true of any has failed.
	 * @param publishingRequestUuid
	 * @return
	 */
	public boolean checkIfAnyBatchFailedForGivenRequestId(UUID publishingRequestUuid);
	public boolean isBatchContainAdds(String batchId);
	public Map<String,String> findRpxConfigByCourtId(Long courtId);
	public Map<String,String> findRpxConfigByProductId(Long productId);
	public int updateDocketReport(String legacyId);
	public DocketVersion findJudicialDocketVersionWithLatestTimestamp(String legacyId, long productId, long courtId);
	public DocketVersion findLatestDocketVersionForProductCourtLegacyIdPhase(String legacyId, Court court, long productId, Phase phase);
	public DocketVersion findHighestJudicialDocketVersionForGivenProductAndLegacyId(String legacyId, long productId);
	public AcquisitionStatus findAcquisitionStatusForGivenStatus(String status);
	public char getLargeDocketFlag(String legacyId);
	public DocketVersion findSourceDocketVersionWithHighestVersionForVendorAndProduct(String legacyId, long vendorId, long productId);
	
	

}
