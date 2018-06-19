package com.trgr.dockets.core.entity;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

public class TransientDocketVersion {
	private static final Logger log = Logger.getLogger(TransientDocketVersion.class);
	Map<String, Object> queryResult;

	public TransientDocketVersion (Map<String, Object> queryResult){
		this.queryResult = queryResult;
	}
		
	public long getContentSize (){
		if (queryResult != null){
			BigDecimal contentSizeTemp = (BigDecimal) queryResult.get("content_size");
			return contentSizeTemp.longValue();
		}
		return 0;
	}
	
	public String getOperationType(){
		if (queryResult != null){
			String operationType = (String) queryResult.get("operation_type");
			return operationType;
		}
		return "";
	}
	
	public char getLargeDocketFlag(){
		if (queryResult != null){
			String largeDocketFlag = (String) queryResult.get("large_docket_flag");		
			return largeDocketFlag.charAt(0);		
		}
		return 'N';
	}
	
	public Long getCountyId(){
		if (queryResult != null && queryResult.get("county_id") != null){
			//Cannot do an explicit cast because query result might have number stored as a BigDecimal (dependent on DB and DB driveR)
			//The below seems like the safest way to proect against any driver changes as opposed to assuming it will always be a BigDecimal
			Long countyId =  Long.valueOf(queryResult.get("county_id").toString());
			return countyId;
		}
		return 0L;
	}
	
	public Long getVendorId(){
		if (queryResult != null && queryResult.get("vendor_id") != null){
			//Cannot do an explicit cast because query result might have number stored as a BigDecimal (dependent on DB and DB driveR)
			//The below seems like the safest way to proect against any driver changes as opposed to assuming it will always be a BigDecimal
			Long vendorId =  Long.valueOf(queryResult.get("vendor_id").toString());
			return vendorId;
		}
		return 0L;
	}
	
	public UUID getContentUuid(){
		if (queryResult != null){
			String uuidTemp = (String) queryResult.get("content_uuid");
			UUID contentUuid = null;
			try {
				contentUuid = new UUID(uuidTemp);
			} catch (NumberFormatException e) {
				log.error("Error due to Number format exception in creating a UUID based off string from query result" + e.getMessage());
				e.printStackTrace();
			} catch (UUIDException e) {
				log.error("Error due to UUID exception in creating a UUID based off string from query result" + e.getMessage());
				e.printStackTrace();
			}
			return contentUuid;
		}
		return null;
	}
	
}
