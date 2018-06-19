/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.dao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.ScrollableResults;

import com.trgr.dockets.core.CoreConstants.Phase;
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
import com.trgr.dockets.core.entity.CollectionEntity;
import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.CountyCourtNorm;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.CourtCollectionMapKey;
import com.trgr.dockets.core.entity.DispatcherWorkItem;
import com.trgr.dockets.core.entity.DispatcherWorkPaused;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.PreprocessorWorkItem;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.ProcessType;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingControl;
import com.trgr.dockets.core.entity.PublishingProcessControl;
import com.trgr.dockets.core.entity.PublishingProcessControlKey;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.TransientDocketVersion;
import com.trgr.dockets.core.entity.TransientStgPubRequest;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public interface DocketDao {
	
	/**
	 * Delete the row from the PREPROCESSOR_WORK table that has the specified publishing request ID
	 * @return the number of rows deleted
	 */
	public void deletePreprocessorWork(PublishingRequest publishingRequest);
	
	/**
	 * Fetch docket content as a stream using its legacy id.
	 * @param docketContentUuid a docket content UUID
	 * @return an InputStream for the docket content with the specified key,
	 * or null if no docket with that UUID is found.
	 * <b>It is the client's responsibility to close this stream when finished using it.</b>
	 * @throws RuntimeException if there is an error in accessing the docket data.
	 */
	public InputStream getDocketContentStream(UUID docketContentUuid);
	
	public Batch findBatchByPrimaryKey(String batchId);
	public BatchDocket findBatchDocketByPrimaryKey(BatchDocketKey primaryKey);
	public BatchDocket findBatchDocketByContentId(UUID contentUuid);
	
	
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId,long productId, String phase);
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId, Court court, long productId, String phase);
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId,long productId, String phase, long vendorId);
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId, Court court, long productId, String phase, long vendorId);
	
	public ArrayList<HashMap<String,Object>> findContentUuidListBySingleContentUuid(UUID uuid);
	
	public Map<String, String> getDocketNumberByLegacyId (String legacyId);
	
	public char getLargeDocketFlagByLegacyId (String legacyId);
	
	public String getLegacyIdByContentUuid (UUID contentUuid);
	
	public List<TransientStgPubRequest> findCollectionByPublishingRequestId(String uuid);
	/**
	 * Returns a cursor where the one value within it is the BatchDocketKey.
	 * Done as a scrollable result because the result set will be very large in many cases.
	 * @param batchId the search key
	 * @return a scrollable set of BatchDocketKey
	 */
	public ScrollableResults findBatchDocketKeys(String batchId);
	public ScrollableResults findBatchDockets(String batchId);
	public BatchDroppedDocket findBatchDroppedDocket(Long primaryKey);
	public List<BatchDroppedDocket> findBatchDroppedDocketByBatchId(String batchId);
	public List<Batch> findBatchesByParentBatch(Batch parentBatch);
	public CollectionEntity findCollectionByPrimaryKey(Long primaryKey);
	public CollectionEntity findCollectionByCollectionName(String collectionName);
	public String findPubIdByCollectionName(String metadocCollectionName);
	public List<String> findCourtCollMapTypesByCourt(Court court);
	public BatchMonitor findBatchMonitorByPrimaryKey(BatchMonitorKey primaryKey);
	public List<BatchMonitor> findBatchMonitorByRequestId(UUID requestId);
	public Court findCourtByCourtCluster(String courtCluster);
	public Court findCourtByCourtNorm(String courtNorm);
	public Court findCourtByPrimaryKey(Long primaryKey);
	public Court findCourtIfCountyCourtUnique(String countyName, Integer... courtIds) throws Exception;
	public List<CountyCourtNorm> getCountyCourtNorms();
	public DispatcherWorkItem findDispatcherWorkItemByBatchId(String batchId);
	public DocketEntity findDocketByPrimaryKey(String legacyId);
	public DocketVersion findJudicialDocketVersionWithHighestVersion(String legacyId, long vendorId, long productId, Court court);
	public DocketVersion findJudicialDocketVersionWithLatestTimestamp(String legacyId, long productId, long courtId);
	public Process findProcessByPrimaryKey(UUID processId);
	public PublishingRequest findPublishingRequestByPrimaryKey(UUID primaryKey);
	public AcquisitionLookup findAcquisitionLookupByRequestKey(UUID requestKey);
	public AcquisitionLookup findAcquisitionLookupByReceiptId(String receiptId);
	public PublishingControl findPublishingControlByPrimaryKey(Long primaryKey);
	public List<PublishingControl> findAllPublishingControl();
	public PublishingProcessControl findPublishingProcessControlByPrimaryKey(PublishingProcessControlKey primaryKey);
	
	public List<DispatcherWorkItem> findNewDispatcherWorkItems();
	public List<DispatcherWorkItem> findAllDispatcherWorkItems();
	
	public int updateDispatcherWork(DispatcherWorkItem dwi);
	public int updateDocketReport(String legacyId);
	
	public <T> Object save(T entity);  // generalized save
	public <T> void update(T entity);  // generalized update
	public <T> void merge(T entity);  // generalized merge
	public <T> T mergeOnly(T entity);  // generalized merge
	public <T> void delete(T entity);
	public <T> void delete(Collection<T> entities);
	
	public void deleteBatchDroppedDocketsByBatchId (String batchId);
	
	public void deleteBatchDocketsByPublishingRequestId (String requestId);
	
	public void deleteBatch(String requestId);
	
	public void deleteBatchMonitor(String batchId);
	
	public void deleteProcessByBatchId(String batchId);
	
	public void deleteDispatcherWorkItemByRequestId (String requestId);
	
	public DocketVersion findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId);

	public DocketVersion findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId);

	public DocketVersion findNovusDocketVersionWithHighestVersionForProductAndCourt(String legacyId, Court court, long productId );
	
	public DocketVersion findSourceDocketVersionWithHighestVersionForProductAndCourt(String legacyId, Court court, long productId );
	
	public CaseSubType findCaseSubTypeByCaseSubTypeAndProduct(String caseSubType, Product product);
	
	public CaseSubType findCaseSubTypeByCaseSubTypeIdAndProduct(Long caseSubTypeId, Product product);
	
	public List<CaseType> getCaseTypeTable();
	public List<CaseSubType> getCaseSubTypeTable(Long courtId, Long productId);
	public List<County> getCountyTable();

	public List<BatchDocketKey> findBatchDocketKeysList(String batchId);
	
	public CourtCollectionMapKey findCourtCollectionMapKeyByKeyValues(Long courtId, List<?> mapKeyValues);
	
	public List<CourtCollectionMapKey> findCourtCollectionMapKeysByCourt(Long courtId);
	
	public CollectionEntity findBatchCollection(String batchId);

	public List<DocketEntity> findDocketsByCourtIdFilingYearSequenceNumber(long courtId, long filingYear, String sequenceNumber);

	public boolean isBatchContainDeletes(String BatchId);
	
	public boolean isBatchContainAdds(String batchId);
	
	String findIfFifoPreprocessorWork(PublishingRequest publishingRequest);
	
	public PreprocessorWorkItem findPreprocessorWorkItemByPublishingRequest(PublishingRequest publishingRequest);

	public String findIfCourtIsFifo(Court court);

	public List<Process> findProcessesFailedForProcess(String publishingRequestId, Long processTypeId);

	public List<Process> findProcessesFailed(String publishingRequestId);

	public PublishingControl findPublishingControlByProduct(Product product);

	public PublishingControl findPublishingControlByProductAndCourt(Product product,Court court);

	public PublishingControl findPublishingControlByProductCourtAndVendorId(Product product,Court court, long vendorId);

	/**
	 * Returns the paused dispatcher work items that are ready to run.
	 * 
	 * @return
	 */
	public List<DispatcherWorkPaused> findPausedDispatcherWorkItemsToRun();
	public DispatcherWorkItem findDispatcherWorkItemByWorkId(Long workId);
	public DispatcherWorkPaused findPauseResumeJobByWorkId(Long workId);
	public List<Court> loadCourt();
	public List<PublishingProcessControl> findAllPausedPublishingProcessControl(ProcessType processType);
	public Map<String,String> findRpxConfigByCourtId(Long courtId);
	public Map<String,String> findRpxConfigByProductId(Long productId);
	/**
	 * Get latest docketVersion by highest version for given legacyId,@param court,@param productId,@param phase.
	 * @param legacyId,@param court,@param productId,@param phase
	 * @return
	 */
	public DocketVersion findLatestDocketVersionForProductCourtLegacyIdPhase(String legacyId, Court court, long productId, Phase phase);

	public AcquisitionStatus findAcquisitionStatusForGivenStatus(String status);

	public DocketVersion findHighestJudicialDocketVersionForGivenProductAndLegacyId(String legacyId, long productId);

	public List<DispatcherWorkItem> findDispatcherWorkItemsByRequestId(UUID requestId);

	public DocketVersion findSourceDocketVersionWithHighestVersionForVendorAndProduct(String legacyId, long vendorId, long productId);



}
