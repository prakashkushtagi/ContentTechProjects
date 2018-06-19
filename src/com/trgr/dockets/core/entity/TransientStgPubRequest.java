/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;

public class TransientStgPubRequest {

	private static final Logger log = Logger.getLogger(TransientDocketVersion.class);
	Map<String, Object> queryResult;
	
	public TransientStgPubRequest (Map<String, Object> queryResult){
		this.queryResult = queryResult;
	}
	
	public CollectionEntity getCollection(){
		CollectionEntity collectionEntity = new CollectionEntity();	
		if(queryResult != null && queryResult.get("collection_id") != null){
			try {
				BigDecimal collId = (BigDecimal) queryResult.get("collection_id");
				collectionEntity.setPrimaryKey((Long) collId.longValueExact());
				collectionEntity.setName((String) queryResult.get("collection_name"));
				collectionEntity.setLtcDatatype((String) queryResult.get("ltc_data_type"));	
				return collectionEntity;
			}
			catch (NullPointerException e){
				log.error("Query to retrieve collection info for request failed for one or more fields");
				throw e;
			}
		}			
		return null;
	}
	
	public String getLegacyId(){
		if(queryResult != null && queryResult.get("legacy_id") != null){
			return (String) queryResult.get("legacy_id");
		}			
		return "";
	}
}
