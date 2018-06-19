/* Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.dao;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

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
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
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
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

public class DocketDaoImpl implements DocketDao {
	private static final Logger log = Logger.getLogger(DocketDaoImpl.class);
	private static final DispatcherWorkRowMapper DISPATCHER_WORK_ROW_MAPPER = new DispatcherWorkRowMapper();
	private static final DispatcherWorkPausedRowMapper DISPATCHER_WORK_PAUSED_ROW_MAPPER = new DispatcherWorkPausedRowMapper();
	
	private SessionFactory sessionFactory;
	private JdbcTemplate jdbcTemplate;

	public DocketDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public DocketDaoImpl(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
		this.sessionFactory = sessionFactory;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void deletePreprocessorWork(PublishingRequest publishingRequest) {
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("delete from PreprocessorWorkItem where (publishingRequestId = '%s')",
									publishingRequest.getPrimaryKey());
		session.createQuery(hql).executeUpdate();
	}
	@Override
	public String findIfFifoPreprocessorWork(PublishingRequest publishingRequest) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PreprocessorWorkItem.class);
		criteria.add(Restrictions.eq("publishingRequestId", publishingRequest.getPrimaryKey().toString()));
		PreprocessorWorkItem content = (PreprocessorWorkItem)criteria.uniqueResult();
//		String content = jdbcTemplate.queryForObject(sql, String.class);
		if (content == null || content.isFifoOverride() != true)
		 {
			if (content == null )
				{
				log.error("findIfFifoPreprocessorWork content is null setting to false for " + publishingRequest.getRequestName());
				}
			return (new Boolean(false)).toString();
		 }
		return (new Boolean(content.isFifoOverride())).toString();
	}
	
	@Override
	public PreprocessorWorkItem findPreprocessorWorkItemByPublishingRequest(PublishingRequest publishingRequest) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PreprocessorWorkItem.class);
		criteria.add(Restrictions.eq("publishingRequestId", publishingRequest.getPrimaryKey().toString()));
		PreprocessorWorkItem content = (PreprocessorWorkItem)criteria.uniqueResult();
		if (content == null || content.isFifoOverride() != true)
		 {
			if (content == null )
				{
				log.error("findIfFifoPreprocessorWork content is null setting to false for " + publishingRequest.getRequestName());
				}
			
		 }
		return content;
	}
	
	@Override
	public String findIfCourtIsFifo(Court court)
	{
		String sql = String.format("Select FIFO from DOCKETS_PUB.COURT_CONFIG where (COURT_ID = %s)", court.getPrimaryKey());
		String content = jdbcTemplate.queryForObject(sql, String.class);
		return content;
	}
	
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId, long productId, String phase){
		//Need to order by version timestamp, not version, as version is linked to a particular vendor and we want to ignore vendor
		String sql =String.format("Select content_uuid, operation_type, content_size, version, large_docket_flag, county_id, vendor_id  from dockets_pub.docket_version dv join dockets_pub.docket d on dv.legacy_id=d.legacy_id where " +
				"d.legacy_id='" +legacyId+"' and phase ='" + phase + "' and d.product_id ='"+productId+"' ORDER by version_timestamp desc ");
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);

		if (tempObjects.size() > 0){
			TransientDocketVersion transientDocketVersion= new TransientDocketVersion(tempObjects.get(0));		
			return transientDocketVersion;
		}
		return null;
		
	}
	
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId, Court court, long productId, String phase ){
		//Need to order by version timestamp, not version, as version is linked to a particular vendor and we want to ignore vendor
		String sql =String.format("Select content_uuid, operation_type, content_size, version, large_docket_flag, county_id, vendor_id from dockets_pub.docket_version dv join dockets_pub.docket d on dv.legacy_id=d.legacy_id where " +
				"d.legacy_id='" +legacyId+"' and phase ='"+ phase + "' and " +
				"d.court_id ='" +court.getPrimaryKey()+"'  and d.product_id ='"+productId+"' ORDER by version_timestamp desc");
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);

		if (tempObjects.size() > 0){
			TransientDocketVersion transientDocketVersion= new TransientDocketVersion(tempObjects.get(0));		
			return transientDocketVersion;
		}
		return null;
		
	}
	
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId, long productId, String phase, long vendorId){
		String sql =String.format("Select content_uuid, operation_type, content_size, version, large_docket_flag, county_id, vendor_id  from dockets_pub.docket_version dv join dockets_pub.docket d on dv.legacy_id=d.legacy_id where " +
				"d.legacy_id='" +legacyId+"' and phase ='" + phase + "' and d.product_id ='"+productId+"'  and dv.vendor_id='" + vendorId + "' ORDER by version desc");
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);

		if (tempObjects.size() > 0){
			TransientDocketVersion transientDocketVersion= new TransientDocketVersion(tempObjects.get(0));		
			return transientDocketVersion;
		}
		return null;
		
	}
	
	public TransientDocketVersion findLatestDocketVersionInfo(String legacyId, Court court, long productId, String phase, long vendorId ){		
		String sql =String.format("Select content_uuid, operation_type, content_size, version, large_docket_flag, county_id, vendor_id from dockets_pub.docket_version dv join dockets_pub.docket d on dv.legacy_id=d.legacy_id where " +
				"d.legacy_id='" +legacyId+"' and phase ='"+ phase + "' and " +
				"d.court_id ='" +court.getPrimaryKey()+"'  and d.product_id ='"+productId+"' and dv.vendor_id='" + vendorId + "' ORDER by version desc");
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);

		if (tempObjects.size() > 0){
			TransientDocketVersion transientDocketVersion= new TransientDocketVersion(tempObjects.get(0));		
			return transientDocketVersion;
		}
		return null;
		
	}
	
	
	public ArrayList<HashMap<String, Object>> findContentUuidListBySingleContentUuid(UUID uuid){
	    sessionFactory.getCurrentSession();
		ArrayList<HashMap<String, Object>> contentUuidObjectList = new ArrayList<HashMap<String, Object>>();
		
		String sql = String.format(
				"WITH DVJUD AS (" +
					"SELECT UNIQUE LEGACY_ID, CONTENT_UUID, VENDOR_ID, MAX(CONVERTED_TIMESTAMP) AS CONVERTED_TIMESTAMP, MAX(VERSION) AS VERSION " +
					"FROM DOCKETS_PUB.DOCKET_VERSION " +
					"WHERE PHASE = 'JUDICIAL' " +
					"AND LEGACY_ID = " +
						"(SELECT UNIQUE LEGACY_ID " +
						"FROM DOCKETS_PUB.DOCKET_VERSION " +
						"WHERE CONTENT_UUID = '" + uuid.toString() + "') " +
					"GROUP BY LEGACY_ID, CONTENT_UUID, VENDOR_ID), " +
				"DVSRC AS (" +
					"SELECT UNIQUE LEGACY_ID, CONTENT_UUID, VENDOR_ID, MAX(ACQUISITION_TIMESTAMP) AS ACQUISITION_TIMESTAMP, MAX(VERSION) AS VERSION " +
					"FROM DOCKETS_PUB.DOCKET_VERSION " +
					"WHERE PHASE = 'SOURCE' " +
					"AND LEGACY_ID = " +
						"(SELECT UNIQUE LEGACY_ID " +
						"FROM DOCKETS_PUB.DOCKET_VERSION " +
						"WHERE CONTENT_UUID = '" + uuid.toString() + "') " +
					"GROUP BY LEGACY_ID, CONTENT_UUID, VENDOR_ID) " +
				"SELECT DVJUD.LEGACY_ID, DVJUD.CONTENT_UUID, DVJUD.VENDOR_ID, DVSRC.ACQUISITION_TIMESTAMP " +
				"FROM DVJUD, DVSRC " +
				"WHERE DVJUD.LEGACY_ID = DVSRC.LEGACY_ID " +
				"AND DVJUD.VENDOR_ID = DVSRC.VENDOR_ID"
				);
		
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);
		
		if( tempObjects.size() > 0){
			for(Map<String, Object> tempMap : tempObjects){
				String uuidDB = (String)tempMap.get("CONTENT_UUID");
				String vendorId = (String)tempMap.get("VENDOR_ID").toString();
				Timestamp acquisitionTimestamp = (Timestamp)tempMap.get("ACQUISITION_TIMESTAMP");
				try{
					HashMap<String, Object> tempHash = new HashMap<String,Object>();
					tempHash.put("CONTENT_UUID", new UUID(uuidDB));
					tempHash.put("VENDOR_ID", vendorId);
					tempHash.put("ACQUISITION_TIMESTAMP", acquisitionTimestamp);
					contentUuidObjectList.add(tempHash);
				}catch(UUIDException e){
					log.error("Error creating UUID for " + tempMap.get("CONTENT_UUID").toString());
				}
			}
		}
		return contentUuidObjectList;
	}
	
	@Override
	public String getLegacyIdByContentUuid (UUID contentUuid) {
		String sql =String.format("Select legacy_id from dockets_pub.docket_version where " +
				"content_uuid='" +contentUuid.toString()+"'");
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);
		String legacyId = (String) tempObjects.get(0).get("legacy_id");
		if (StringUtils.isNotBlank(legacyId)){
			return legacyId;
		}
		return "";
		
	}
	
	@Override
	public Map<String, String> getDocketNumberByLegacyId (String legacyId){

		
		Map<String, String> legacyDocket = new HashMap<String, String>();
		
		String sql =String.format("Select docket_number from dockets_pub.docket where " +
				"legacy_id='" +legacyId+"'");
		String docketNumber = jdbcTemplate.queryForObject(sql, String.class);
		if (StringUtils.isNotBlank(docketNumber)){
			legacyDocket.put("DocketNumber", docketNumber);
		}
		else {
			legacyDocket.put("DocketNumber", "");
		}
		legacyDocket.put("LegacyId", legacyId);
		
		return legacyDocket;
	}
	
	public char getLargeDocketFlagByLegacyId (String legacyId){
		String sql =String.format("Select large_docket_flag from dockets_pub.docket where " +
				"legacy_id='" +legacyId+"'");
		String largeDocketFlag = jdbcTemplate.queryForObject(sql, String.class);
		return largeDocketFlag.charAt(0);
	}
	
	public List<TransientStgPubRequest> findCollectionByPublishingRequestId (String uuid){
		String sql = String.format("select C.COLLECTION_ID, C.COLLECTION_NAME, C.LTC_DATA_TYPE, S.legacy_id, S.LARGE_DOCKET_FLAG from dockets_pub.stg_pub_ui_request s, " +
				"dockets_pub.collection c "+
				"where (C.COLLECTION_NAME = S.COLLECTION_NAME and s.request_id='"+uuid+"')");
		
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);
		List<TransientStgPubRequest> transientStgRequests= new ArrayList<TransientStgPubRequest>();
		for ( Map<String,Object> tempObject : tempObjects){
			transientStgRequests.add(new TransientStgPubRequest(tempObject));
		}
		return transientStgRequests;
	}

	public String findLogLevelForCourt(Court court) 
	{
		String loglevel = null;
		String sql = String.format("Select LOGGING_LEVEL from DOCKETS_PUB.COURT_CONFIG where (COURT_ID = %s)",
						court.getPrimaryKey());
		try
		{	
			loglevel = jdbcTemplate.queryForObject(sql, String.class);
		}
		catch(Exception e)
		{
			
		}
		return loglevel;
	}
	
	@Override
	public InputStream getDocketContentStream(UUID contentUuid) {
		String sql = String.format(
		"select content from litigator_content.content_version where content_uuid = '%s'" +
		"and version_id = (select max(version_id) from litigator_content.content_version where content_uuid = '%s')",
			contentUuid.toString(), contentUuid.toString());
		try {
			Blob content = jdbcTemplate.queryForObject(sql, Blob.class);
			return content.getBinaryStream();
		} catch (Exception e) {
			log.warn("Unknown error while reading the jaxml stream from the blob content ", e);
			return null;
		}
	}

	@Override
	public Batch findBatchByPrimaryKey(String batchId) {
		Session session = sessionFactory.getCurrentSession();
		return (Batch) session.get(Batch.class, batchId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Batch> findBatchesByParentBatch(Batch parentBatch) {
		
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Batch.class);
		criteria.add(Restrictions.eq("parentBatch", parentBatch));
		List<Batch> batches = (List<Batch>) criteria.list();
		return  batches;
	}
	
	@Override
	public BatchDocket findBatchDocketByPrimaryKey(BatchDocketKey primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (BatchDocket) session.get(BatchDocket.class, primaryKey);
	}
	@Override
	public BatchDocket findBatchDocketByContentId(UUID contentUuid) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BatchDocket.class);
		criteria.add(Restrictions.eq("contentId", contentUuid.toString()));
		BatchDocket docket = (BatchDocket) criteria.uniqueResult();
		return docket;
	}
	@Override
	public ScrollableResults findBatchDocketKeys(String batchId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("select bd.primaryKey from BatchDocket bd where bd.batch.primaryKey = '%s'", batchId);
		Query query = session.createQuery(hql);
		return query.scroll();
	}

	
	public ScrollableResults findBatchDockets(String batchId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("from BatchDocket bd where bd.batch.primaryKey = '%s' order by bd.collection", batchId);
		Query query = session.createQuery(hql);
		return query.scroll();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BatchDocketKey> findBatchDocketKeysList(String batchId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("select bd.primaryKey from BatchDocket bd where bd.batch.primaryKey = '%s'", batchId);
		Query query = session.createQuery(hql);
		return query.list();
	}

	@Override
	public BatchDroppedDocket findBatchDroppedDocket(Long primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (BatchDroppedDocket) session.get(BatchDroppedDocket.class,
				primaryKey);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<BatchDroppedDocket> findBatchDroppedDocketByBatchId(String batchId) {            
        
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BatchDroppedDocket.class)
                .createAlias("process", Process.BATCH_ID_ALIAS);

          criteria.add(Restrictions.eq("batch_id_alias.batchId", batchId));
          List<BatchDroppedDocket> bdd = (List<BatchDroppedDocket>) criteria.list();


		return (bdd);

	}

	@Override
	public BatchMonitor findBatchMonitorByPrimaryKey(BatchMonitorKey primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (BatchMonitor) session.get(BatchMonitor.class, primaryKey);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<BatchMonitor> findBatchMonitorByRequestId(UUID requestUuid) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BatchMonitor.class);
		criteria.add(Restrictions.eq("primaryKey.publishingRequestId", requestUuid.toString()));
		return (List<BatchMonitor>)criteria.list();
	}
	
	@Override
	public CollectionEntity findCollectionByPrimaryKey(Long primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (CollectionEntity) session.get(CollectionEntity.class, primaryKey);
	}
	
	@Override
	public String findPubIdByCollectionName(String metadocCollectionName){
		String sql = String.format("select publication_id from docketS_pub.court c join docketS_pub.court_coll_map_key k on c.court_id=k.courT_id where "
				+ "k.collection_id in "
					+ "(select collection_id from dockets_pub.collection where metadoc_collection_name='"+metadocCollectionName+"')" 
				+ " and rownum<2");
		String pubId = Long.toString(jdbcTemplate.queryForObject(sql,Long.class));
		return pubId;
	}
	@Override
	public CollectionEntity findCollectionByCollectionName(String collectionName) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CollectionEntity.class);
		criteria.add(Restrictions.eq("name", collectionName));
		return (CollectionEntity) criteria.uniqueResult();
	}
	@Override
	public List<String> findCourtCollMapTypesByCourt(Court court){
		sessionFactory.getCurrentSession();
		
		String sql = String.format("select coll_map_type_code from dockets_pub.court_coll_map_type ccmt join dockets_pub.coll_map_type cmt on ccmt.coll_map_type_id=cmt.coll_map_type_id where court_id='" + court.getPrimaryKey()+"'");
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);
		List<String> courtCollMapTypes = new ArrayList<String>();
		for (Map<String, Object> mapTypes : tempObjects){
			courtCollMapTypes.add((String) mapTypes.get("coll_map_type_code"));
		}
		return courtCollMapTypes;

	}
	@Override
	public PublishingControl findPublishingControlByPrimaryKey(Long primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (PublishingControl) session.get(PublishingControl.class, primaryKey);
	}
	@Override
	public PublishingControl findPublishingControlByProductAndCourt(Product product, Court court) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PublishingControl.class);
		criteria.add(Restrictions.eq("product.id",product.getPrimaryKey().getKey()));
		criteria.add(Restrictions.eq("court.primaryKey",court.getPrimaryKey()));
		return (PublishingControl) criteria.uniqueResult();
	}
	@Override
	public PublishingControl findPublishingControlByProductCourtAndVendorId(Product product,Court court, long vendorId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PublishingControl.class);
		criteria.add(Restrictions.eq("product.id",product.getPrimaryKey().getKey()));
		criteria.add(Restrictions.eq("court.primaryKey",court.getPrimaryKey()));
		if (vendorId == 0) {
			criteria.add(Restrictions.isNull("vendorId"));
		}else {
			criteria.add(Restrictions.eq("vendorId",vendorId));
		}
		
		return (PublishingControl) criteria.uniqueResult();
	}
	@Override
	public PublishingControl findPublishingControlByProduct(Product product) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PublishingControl.class);
		criteria.add(Restrictions.eq("product.id",product.getPrimaryKey().getKey()));
		criteria.add(Restrictions.isNull("court"));
		return (PublishingControl) criteria.uniqueResult();
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<PublishingControl> findAllPublishingControl() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(PublishingControl.class);
		return criteria.list();
	}
	@Override
	public PublishingProcessControl findPublishingProcessControlByPrimaryKey(PublishingProcessControlKey primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (PublishingProcessControl) session.get(PublishingProcessControl.class, primaryKey);
	}

	@Override
	public Court findCourtByCourtCluster(String courtCluster) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Court.class);
		criteria.add(Restrictions.eq("courtCluster", courtCluster));
		Court court = (Court) criteria.uniqueResult();
		return court;
	}
	
	@Override
	public Court findCourtByCourtNorm(String courtNorm) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Court.class);
		criteria.add(Restrictions.eq("courtNorm", courtNorm));
		Court court = (Court) criteria.uniqueResult();
		return court;
	}
	
	@Override
	public Court findCourtIfCountyCourtUnique(String countyName, Integer... courtIds) throws Exception{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(County.class, "cnty");
		criteria.add(Restrictions.eq("name", countyName).ignoreCase());
		criteria.add(Restrictions.in("courtId", courtIds));
		List<County> counties = criteria.list();
		if (counties.size() > 1){
			throw new Exception("County is mapped to multiple courts!");
		}
		return (Court) session.get(Court.class, counties.get(0).getCourtId().longValue());
	}
	
	@Override
	public Court findCourtByPrimaryKey(Long primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (Court) session.get(Court.class, primaryKey);
	}
	
	@Override
	public List<CountyCourtNorm> getCountyCourtNorms() {		
		String sql =String.format("select c.court_norm, c.col_key from dockets_pub.county c join dockets_pub.court u on c.court_id=u.court_id where u.court_norm != c.court_norm");
		List<Map<String, Object>> tempObjects = jdbcTemplate.queryForList(sql);

		List<CountyCourtNorm> countyCourtNormList = new ArrayList<CountyCourtNorm>();
		for (Map<String, Object> tempObject : tempObjects){
			countyCourtNormList.add(new CountyCourtNorm(tempObject));
		}
		return countyCourtNormList;
	}
	@Override
	public DispatcherWorkItem findDispatcherWorkItemByBatchId(String batchId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DispatcherWorkItem.class);
		criteria.add(Restrictions.eq("batchId", batchId));
		DispatcherWorkItem entity = (DispatcherWorkItem) criteria.uniqueResult();
		return entity;
	}
	@Override
	public DocketEntity findDocketByPrimaryKey(String legacyId) {
		Session session = sessionFactory.getCurrentSession();
		return (DocketEntity) session.get(DocketEntity.class, legacyId);
	}
	@Override
	public List<DocketEntity> findDocketsByCourtIdFilingYearSequenceNumber(long courtId, long filingYear, String sequenceNumber) 
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DocketEntity.class);
		criteria.add(Restrictions.eq("court.primaryKey",courtId));
		criteria.add(Restrictions.eq("filingYear",filingYear));
		criteria.add(Restrictions.eq("sequenceNumber",sequenceNumber));
		return (List<DocketEntity>)criteria.list();
	}
	@Override
	public DocketVersion findJudicialDocketVersionWithHighestVersion(String legacyId, long vendorId, long productId, Court court) {
		return findLatestDocketVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId, Phase.JUDICIAL);
	}
	
	
	public DocketVersion findJudicialDocketVersionWithLatestTimestamp(String legacyId, long productId, long courtId) {
		return findLatestTimestampDocketVersion(legacyId, Phase.JUDICIAL, productId, courtId);
	}
	
	@Override
	public DocketVersion findHighestJudicialDocketVersionForGivenProductAndLegacyId(String legacyId, long productId)
	{
		return findHighestJudicialDocketVersionForGivenProductAndLegacyId(legacyId, productId, Phase.JUDICIAL);
	}

	@Override
	public DocketVersion findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId)
	{
		return findLatestDocketVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId, Phase.SOURCE);
	}
	@Override
	public DocketVersion findSourceDocketVersionWithHighestVersionForVendorAndProduct(String legacyId, long vendorId, long productId)
	{
	    return findLatestDocketVersionForVendorAndProduct(legacyId, vendorId, productId, Phase.SOURCE);
	}
	public DocketVersion findSourceDocketVersionWithHighestVersionForProductAndCourt(String legacyId, Court court, long productId){
		return findLatestDocketVersionForCourtAndProduct(legacyId, court, productId, Phase.SOURCE );
	}
	
	@Override
	public DocketVersion findNovusDocketVersionWithHighestVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId){
		return findLatestDocketVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId, Phase.NOVUS);
	}
	
	public DocketVersion findNovusDocketVersionWithHighestVersionForProductAndCourt(String legacyId, Court court, long productId){
		return findLatestDocketVersionForCourtAndProduct(legacyId, court, productId, Phase.NOVUS );
	}
	
	private DocketVersion findLatestDocketVersionForCourtVendorAndProduct(String legacyId, Court court, long vendorId, long productId, Phase phase) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersion.class);
		criteria.add(Restrictions.eq("productId", productId));
		criteria.add(Restrictions.eq("court", court));
		criteria.add(Restrictions.eq("primaryKey.legacyId", legacyId));
		criteria.add(Restrictions.eq("primaryKey.phaseString", phase.name()));
		criteria.add(Restrictions.eq("primaryKey.vendorKey",vendorId));
		criteria.addOrder(Order.desc("version"));
		criteria.setMaxResults(1);
		return (DocketVersion) criteria.uniqueResult();
	}

	private DocketVersion findLatestDocketVersionForVendorAndProduct(String legacyId, long vendorId, long productId, Phase phase) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersion.class);
		criteria.add(Restrictions.eq("productId", productId));
		criteria.add(Restrictions.eq("primaryKey.legacyId", legacyId));
		criteria.add(Restrictions.eq("primaryKey.phaseString", phase.name()));
		criteria.add(Restrictions.eq("primaryKey.vendorKey",vendorId));
		criteria.addOrder(Order.desc("version"));
		criteria.setMaxResults(1);
		return (DocketVersion) criteria.uniqueResult();
	}

	private DocketVersion findHighestJudicialDocketVersionForGivenProductAndLegacyId(String legacyId, long productId, Phase phase) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersion.class);
		criteria.add(Restrictions.eq("productId", productId));
 		criteria.add(Restrictions.eq("primaryKey.legacyId", legacyId));
		criteria.add(Restrictions.eq("primaryKey.phaseString", phase.name()));
		criteria.addOrder(Order.desc("version"));
		criteria.setMaxResults(1);
        return (DocketVersion) criteria.uniqueResult();
	}

	
	@Override
	public DocketVersion findLatestDocketVersionForProductCourtLegacyIdPhase(String legacyId, Court court, long productId, Phase phase) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersion.class);
		criteria.add(Restrictions.eq("productId", productId));
		criteria.add(Restrictions.eq("court", court));
		criteria.add(Restrictions.eq("primaryKey.legacyId", legacyId));
		criteria.add(Restrictions.eq("primaryKey.phaseString", phase.name()));
		criteria.add(Restrictions.ne("primaryKey.vendorKey", 10L));
		criteria.addOrder(Order.desc("version"));
		criteria.setMaxResults(1);
        return (DocketVersion) criteria.uniqueResult();
	}
	
	private DocketVersion findLatestDocketVersionForCourtAndProduct(String legacyId, Court court, long productId, Phase phase) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersion.class);
		criteria.add(Restrictions.eq("productId", productId));
		criteria.add(Restrictions.eq("court", court));
		criteria.add(Restrictions.eq("primaryKey.legacyId", legacyId));
		criteria.add(Restrictions.eq("primaryKey.phaseString", phase.name()));
		criteria.add(Restrictions.ne("primaryKey.vendorKey", 10L));
		criteria.addOrder(Order.desc("version"));
		List<DocketVersion> list = (List<DocketVersion>) criteria.list();
		criteria.setMaxResults(1);
        return (DocketVersion) criteria.uniqueResult();
	}
	
	private DocketVersion findLatestTimestampDocketVersion(String legacyId, Phase phase, long productId, long courtId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocketVersion.class);
		criteria.add(Restrictions.eq("productId", productId));
		criteria.add(Restrictions.eq("court.primaryKey",courtId));
		criteria.add(Restrictions.eq("primaryKey.legacyId", legacyId));
		criteria.add(Restrictions.eq("primaryKey.phaseString", phase.name()));
		criteria.addOrder(Order.desc("primaryKey.versionDate"));
		criteria.setMaxResults(1);
        return (DocketVersion) criteria.uniqueResult();
	}

	@Override
	public Process findProcessByPrimaryKey(UUID processId) {
		Session session = sessionFactory.getCurrentSession();
		return (Process) session.get(Process.class, processId.toString());
	}
	
	@Override
	public List<Process> findProcessesFailedForProcess(String publishingRequestId, Long processTypeId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Process.class);
		criteria.add(Restrictions.eq("publishingRequestId", publishingRequestId));
		criteria.add(Restrictions.eq("processType.primaryKey", processTypeId));
		criteria.add(Restrictions.eq("status.id", StatusEnum.FAILED.getKey()));
		return (List<Process>) criteria.list();
	}
	@Override
	public List<Process> findProcessesFailed(String publishingRequestId) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Process.class);
		criteria.add(Restrictions.eq("publishingRequestId", publishingRequestId));
		criteria.add(Restrictions.eq("status.id", StatusEnum.FAILED.getKey()));
		return (List<Process>) criteria.list();
	}
	@Override
	public PublishingRequest findPublishingRequestByPrimaryKey(UUID requestId) {
		Session session = sessionFactory.getCurrentSession();
		return (PublishingRequest) session.get(PublishingRequest.class, requestId.toString());
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#findAcquisitionLookupByRequestKey(com.westgroup.publishingservices.uuidgenerator.UUID)
	 */
	public AcquisitionLookup findAcquisitionLookupByRequestKey(UUID requestKey){
		Session session = sessionFactory.getCurrentSession();
		return (AcquisitionLookup)session.get(AcquisitionLookup.class, requestKey.toString());
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#findAcquisitionLookupByReceiptId(java.lang.String)
	 */
	public AcquisitionLookup findAcquisitionLookupByReceiptId(String receiptId){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AcquisitionLookup.class);
		criteria.add(Restrictions.eq("receiptId", receiptId));
		return (AcquisitionLookup) criteria.uniqueResult();
	}
	
	/**
	 * Returns the complete list of DISPATCHER_WORK rows ordered as they should be started as jobs.
	 * Priority is the first ordering criteria, followed by the creation time of the record (FIFO).
	 */
	/*@Override
	@SuppressWarnings("unchecked")
	public List<DispatcherWorkItem> findNewDispatcherWorkItems() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DispatcherWorkItem.class)
				.addOrder(Order.desc("priority"))  
				.addOrder(Order.asc("createTime"));
		criteria.add(Restrictions.isNull("startTime"));
		criteria.add(Restrictions.isNull("serverName"));
		return criteria.list();
	}*/
	
	//TODO: CHANGE ROW NUMBER
	@Override
	//@Transactional(isolation=Isolation.READ_COMMITTED)
	public List<DispatcherWorkItem> findNewDispatcherWorkItems() {
		Table dispatcherWorkTable = DispatcherWorkItem.class.getAnnotation(Table.class);
		String dispatcherWorkTableName = dispatcherWorkTable.name();
		Table preprocessorWorkTable = PreprocessorWorkItem.class.getAnnotation(Table.class);
		String preprocessorWorkTableName = preprocessorWorkTable.name();
		String sql = "select dispatcherWork.*, preprocessorWork.CLIENT REQ_CLIENT from DOCKETS_PREPROCESSOR."
				+ dispatcherWorkTableName
				+ " dispatcherWork,  DOCKETS_PREPROCESSOR."
				+ preprocessorWorkTableName
				+ " preprocessorWork where dispatcherWork.START_TIME is null and dispatcherWork.job_instance_id is null and dispatcherWork.REQUEST_ID = preprocessorWork.REQUEST_ID order by dispatcherWork.PRIORITY desc, dispatcherWork.CREATE_TIME asc, dispatcherWork.BATCH_ID asc for update of dispatcherWork.WORK_ID skip locked";
		List<DispatcherWorkItem> rows = jdbcTemplate.query(sql.toString(), DISPATCHER_WORK_ROW_MAPPER);
		return rows;
	}
	
	/**
	 * Returns the complete list of DISPATCHER_WORK rows ordered as they should be started as jobs.
	 * Priority is the first ordering criteria, followed by the creation time of the record (FIFO).
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DispatcherWorkItem> findAllDispatcherWorkItems() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DispatcherWorkItem.class)
				.addOrder(Order.desc("publishingPriority"))  
				.addOrder(Order.asc("createTime"));
		return criteria.list();
	}
	
	/**
	 * Returns the complete list of Court rows ordered as they should be started as jobs.
	 * Priority is the first ordering criteria, followed by the creation time of the record (FIFO).
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Court> loadCourt() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Court.class)
				.addOrder(Order.desc("courtCluster"));
		return criteria.list();
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#findCaseSubTypeByCaseSubTypeAndProduct(java.lang.String, com.trgr.dockets.core.entity.Product)
	 */
	public CaseSubType findCaseSubTypeByCaseSubTypeAndProduct(String caseSubType, Product product){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CaseSubType.class);
		criteria.add(Restrictions.eq("caseSubType", caseSubType));
		criteria.add(Restrictions.eq("product", product));
		return (CaseSubType) criteria.uniqueResult();
	}
	
	public CaseSubType findCaseSubTypeByCaseSubTypeIdAndProduct(Long caseSubTypeId, Product product){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CaseSubType.class);
		criteria.add(Restrictions.eq("caseSubTypeId", caseSubTypeId));
		criteria.add(Restrictions.eq("product", product));
		return (CaseSubType) criteria.uniqueResult();
	}
	
	public List<CaseType> getCaseTypeTable(){
		String sql = "select * from dockets_pub.case_type";
		@SuppressWarnings("unchecked")
		List<CaseType> rows = jdbcTemplate.query(sql.toString(),
				new Object[]{},
				new RowMapper() {
					public Object mapRow (ResultSet rs, int rowNum) throws SQLException {
						CaseType ct= new CaseType();
						ct.setCaseType(rs.getString("CASE_TYPE"));
						ct.setCaseTypeId(rs.getLong("CASE_TYPE_ID"));
						return ct;
					}
		});
		
		return rows;
	}
	
	public List<CaseSubType> getCaseSubTypeTable(Long courtId, Long productId){
		String sql = "select * from dockets_pub.case_sub_type where court_id ="+courtId+" and product_id="+productId;
		@SuppressWarnings("unchecked")
		List<CaseSubType> rows = jdbcTemplate.query(sql.toString(),
				new Object[]{},
				new RowMapper() {
					public Object mapRow (ResultSet rs, int rowNum) throws SQLException {
						CaseSubType ct= new CaseSubType();
						ct.setCaseSubType(rs.getString("CASE_SUB_TYPE"));
						ct.setCaseSubTypeId(rs.getLong("CASE_SUB_TYPE_ID"));
						return ct;
					}
		});
		
		return rows;
	}
	
	
	@Override
	public AcquisitionStatus findAcquisitionStatusForGivenStatus(String status){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(AcquisitionStatus.class);
		criteria.add(Restrictions.eq("status",status));
		return(AcquisitionStatus) criteria.uniqueResult();
	}
	
	public List<County> getCountyTable(){
		String sql = "select * from dockets_pub.county";
		@SuppressWarnings("unchecked")
		List<County> rows = jdbcTemplate.query(sql.toString(),
				new Object[]{},
				new RowMapper() {
					public Object mapRow (ResultSet rs, int rowNum) throws SQLException {
						County cty= new County();
						cty.setName(rs.getString("NAME"));
						cty.setCourtId(rs.getInt("COURT_ID"));
						cty.setCountyId(rs.getInt("COUNTY_ID"));
						cty.setAbbreviation(rs.getString("ABBREVIATION"));
						return cty;
					}
		});
		
		return rows;
	}
	
	public CourtCollectionMapKey findCourtCollectionMapKeyByKeyValues(Long courtId, List mapKeyValues){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CourtCollectionMapKey.class);
		criteria.add(Restrictions.eq("primaryKey.courtId", courtId));
		criteria.add(Restrictions.in("primaryKey.mapKey", mapKeyValues));
		return (CourtCollectionMapKey) criteria.uniqueResult();
	}
	
	/**
	 * @param courtId
	 * @return list of all map keys for that court ID
	 */
	@SuppressWarnings("unchecked")
	public List<CourtCollectionMapKey> findCourtCollectionMapKeysByCourt(Long courtId){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(CourtCollectionMapKey.class);
		criteria.add(Restrictions.eq("primaryKey.courtId", courtId));
		return criteria.list();
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#findBatchCollection(java.lang.String)
	 */
	public CollectionEntity findBatchCollection(String batchId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("select bd.collection from BatchDocket bd where bd.batch.primaryKey = '%s' and rownum = 1", batchId);
		Query query = session.createQuery(hql);
		return (CollectionEntity) query.uniqueResult();
	}
	
	@Override
	public void deleteBatchDroppedDocketsByBatchId (String batchId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("delete from BatchDroppedDocket bdd where bdd.process in (select p from Process p, Batch b where p.batchId = b.primaryKey and (b.primaryKey='%s' or b.parentBatch='%s'))", batchId, batchId);
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	@Override
	public void deleteBatchDocketsByPublishingRequestId (String requestId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("delete from BatchDocket where REQUEST_ID='%s'", requestId);
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	@Override
	public void deleteBatch (String requestId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("delete from Batch where primaryKey='%s' or parentBatch='%s'", requestId, requestId);
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	
	@Override
	public void deleteBatchMonitor (String batchId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("delete from BatchMonitor where BATCH_ID in (select primaryKey from Batch where primaryKey='%s' or parentBatch='%s')", batchId, batchId);
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	
	@Override
	public void deleteProcessByBatchId (String batchId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("delete from Process where BATCH_ID in (select primaryKey from Batch where primaryKey='%s' or parentBatch='%s')", batchId, batchId);
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}
	
	@Override
	public void deleteDispatcherWorkItemByRequestId (String requestId){
		Session session = sessionFactory.getCurrentSession();
		String hql = String.format("delete from DispatcherWorkItem where REQUEST_ID='%s'", requestId);
		Query query = session.createQuery(hql);
		query.executeUpdate();
	}

	@Override
	public int updateDispatcherWork(DispatcherWorkItem dwi) {
		Session session = sessionFactory.getCurrentSession();
		String hql ="update DispatcherWorkItem set jobExecutionId = :jEI, jobInstanceId = :jII, serverName =:serverName, " +
				"startTime = :startTime where primaryKey =:workId and serverName is NULL";
		Query query = session.createQuery(hql);
		query.setLong("jEI", dwi.getJobExecutionId());
		query.setLong ("jII", dwi.getJobInstanceId());
		query.setString("serverName", dwi.getServerName());
		query.setTimestamp("startTime", dwi.getStartTime());
		query.setLong("workId", dwi.getPrimaryKey());
        return query.executeUpdate();        	
	}
	
	public int updateDocketReport(String legacyId){
		Session session = sessionFactory.getCurrentSession();
		String hql ="update dockets_pub.docket set KNOS_ERROR_EXIST = :knosReport, SUMMARY_SERVICE_ERROR_EXIST = :summaryReport, PDF_DETAIL_ERROR_EXIST = :pdfReport, JNF_ERROR_EXIST = :jnfReport where LEGACY_ID = :legacyId";
		//Query query = session.createQuery(hql);
		SQLQuery query = session.createSQLQuery(hql);
		query.setString("knosReport", "N");
		query.setString("summaryReport", "N");
		query.setString("pdfReport", "N");
		query.setString("jnfReport", "N");
		query.setString("legacyId", legacyId);
		/*int updates = query.executeUpdate();
        session.flush();
        session.clear();*/
        return  query.executeUpdate();       	
	}
	
	
	@Override
	public <T> void delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}
	@Override
	public <T> void delete(Collection<T> entities) {
		for (T entity : entities) {
			delete(entity);
		}
	}
	@Override
	public <T> Object save(T entity) {
		Session session = sessionFactory.getCurrentSession();
		return session.save(entity);
	}
	@Override
	public <T> void update(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#merge(java.lang.Object)
	 */
	@Override
	public <T> void merge(T entity) {
		Session session = sessionFactory.getCurrentSession();
		update(session.merge(entity));
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#merge(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T mergeOnly(T entity) {
		Session session = sessionFactory.getCurrentSession();
		return (T)session.merge(entity);
	}

	
	public boolean isBatchContainDeletes(String batchId){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BatchDocket.class);
		criteria.add(Restrictions.eq("batch.primaryKey", batchId));
		criteria.add(Restrictions.eq("operationType", "D"));
		return criteria.list().size() > 0;
	}
	
	@Override
	public boolean isBatchContainAdds(String batchId){
		
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BatchDocket.class);
		criteria.add(Restrictions.eq("batch.primaryKey", batchId));
		criteria.add(Restrictions.or((Restrictions.not(Restrictions.eq("operationType", "D"))), Restrictions.isNull("operationType")));
		return criteria.list().size() > 0;
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#findPausedDispatcherWorkItemsToRun()
	 */
	public List<DispatcherWorkPaused> findPausedDispatcherWorkItemsToRun(){
		Table dispatcherWorkTable = DispatcherWorkItem.class.getAnnotation(Table.class);
		Table pausedDispatcherWorkTable = DispatcherWorkPaused.class.getAnnotation(Table.class);
		
		String dispatcherWorkTableName = dispatcherWorkTable.name();
		String pausedDispatcherWorkTableName = pausedDispatcherWorkTable.name();
		String sql = "select pausedDispatcherWork.*, dispatcherWork.SERVER_NAME from DOCKETS_PREPROCESSOR." + dispatcherWorkTableName  + " dispatcherWork, DOCKETS_PREPROCESSOR." + pausedDispatcherWorkTableName + " pausedDispatcherWork " + 
					 "where dispatcherWork.WORK_ID = pausedDispatcherWork.WORK_ID and pausedDispatcherWork.READY_TO_RESUME = 'Y' " + 
					 "order by dispatcherWork.CREATE_TIME for update of pausedDispatcherWork.WORK_ID skip locked";
		List<DispatcherWorkPaused> rows = jdbcTemplate.query(sql.toString(), DISPATCHER_WORK_PAUSED_ROW_MAPPER);
		return rows;
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#findDispatcherWorkItemByWorkId(java.lang.Long)
	 */
	public DispatcherWorkItem findDispatcherWorkItemByWorkId(Long workId){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DispatcherWorkItem.class);
		criteria.add(Restrictions.eq("primaryKey", workId));
		return (DispatcherWorkItem) criteria.uniqueResult();
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.dao.DocketDao#findPauseResumeJobByWorkId(java.lang.Long)
	 */
	public DispatcherWorkPaused findPauseResumeJobByWorkId(Long workId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DispatcherWorkPaused.class);
		criteria.add(Restrictions.eq("workId", workId));
		return (DispatcherWorkPaused) criteria.uniqueResult();
	}

	@Override
	public List<PublishingProcessControl> findAllPausedPublishingProcessControl(ProcessType processType) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PublishingProcessControl.class);
		criteria.add(Restrictions.eq("active", false));
		criteria.add(Restrictions.eq("primaryKey.processTypeId", processType.getPrimaryKey()));
		return (List<PublishingProcessControl>)criteria.list();
	}

	@Override
	public Map<String, String> findRpxConfigByCourtId(Long courtId) {
		Map<String, String> rpxConfig = new HashMap<String, String>();
		
		String sql =String.format("select courtconfig.RPX_COMPONENT_NAME, rpxworkflow.NAME from dockets_pub.court_config courtconfig, dockets_config.rpx_workflow_type rpxworkflow"+
              " where courtconfig.RPX_WORKFLOW_TYPE_ID= rpxworkflow.RPX_WORKFLOW_TYPE_ID"+
              " AND courtconfig.court_id='"+courtId+"'");
		Map<String,Object> queryResult = jdbcTemplate.queryForMap(sql);
		if (queryResult!=null){
			rpxConfig.put("rpx.component", (String)queryResult.get("RPX_COMPONENT_NAME"));
			rpxConfig.put("rpx.bermuda.data.manager.config", (String)queryResult.get("NAME"));			
		}
		else {
			rpxConfig.put("rpx.component", "");
			rpxConfig.put("rpx.bermuda.data.manager.config", "");
		}		
		return rpxConfig;
	}
	
	@Override
	public Map<String, String> findRpxConfigByProductId(Long productId) {
		Map<String, String> rpxConfig = new HashMap<String, String>();
		
		String sql =String.format("select courtconfig.RPX_COMPONENT_NAME, rpxworkflow.NAME from dockets_pub.court_config courtconfig, dockets_config.rpx_workflow_type rpxworkflow"+
              " where courtconfig.RPX_WORKFLOW_TYPE_ID= rpxworkflow.RPX_WORKFLOW_TYPE_ID"+
              " AND courtconfig.court_id= (select court_id from dockets_pub.court where product_id='" + productId + "' and ROWNUM <2)");
		Map<String,Object> queryResult = jdbcTemplate.queryForMap(sql);
		if (queryResult!=null){
			rpxConfig.put("rpx.component", (String)queryResult.get("RPX_COMPONENT_NAME"));
			rpxConfig.put("rpx.bermuda.data.manager.config", (String)queryResult.get("NAME"));			
		}
		else {
			rpxConfig.put("rpx.component", "");
			rpxConfig.put("rpx.bermuda.data.manager.config", "");
		}		
		return rpxConfig;
	}

	@Override
	public List<DispatcherWorkItem> findDispatcherWorkItemsByRequestId(UUID requestId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DispatcherWorkItem.class);
		criteria.add(Restrictions.eq("publishingRequestId", requestId.toString()));
		return (List<DispatcherWorkItem>) criteria.list();
	}

}

class DispatcherWorkRowMapper implements RowMapper<DispatcherWorkItem> {
	public DispatcherWorkItem mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		DispatcherWorkItem dispatcherWorkItem = new DispatcherWorkItem();
		dispatcherWorkItem.setPrimaryKey(Long.valueOf(resultSet.getString("WORK_ID")));
		dispatcherWorkItem.setBatchId(resultSet.getString("BATCH_ID"));
		try {
			dispatcherWorkItem.setPublishingRequestId(resultSet.getString("REQUEST_ID"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dispatcherWorkItem.setClient(resultSet.getString("REQ_CLIENT"));
		return dispatcherWorkItem;
	}
}
	
class DispatcherWorkPausedRowMapper implements RowMapper<DispatcherWorkPaused> {
		public DispatcherWorkPaused mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			DispatcherWorkPaused dispatcherWorkPaused = new DispatcherWorkPaused(Long.valueOf(resultSet.getString("JOB_EXECUTION_ID")), Long.valueOf(resultSet.getString("WORK_ID")), resultSet.getString("READY_TO_RESUME"));
			dispatcherWorkPaused.setServerName(resultSet.getString("SERVER_NAME"));
			return dispatcherWorkPaused;
		}
}

