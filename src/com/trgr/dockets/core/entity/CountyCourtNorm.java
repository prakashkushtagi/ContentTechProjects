/* Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.entity;

import java.util.HashMap;
import java.util.Map;

public class CountyCourtNorm {
	
	Map<String, Object> queryResult;

	public CountyCourtNorm (Map<String, Object> queryResult){
		this.queryResult = queryResult;
	}
	
	public String getColKey(){
		if (queryResult != null){
			String colKey = (String) queryResult.get("col_key");
			return colKey;
		}
		return "";
	}
	
	public String getCountyCourtNorm(){
		if (queryResult != null){
			String colKey = (String) queryResult.get("court_norm");
			return colKey;
		}
		return "";
	}
	
	public Map<String, String> getCourtNormColKeyMap(){
		Map<String, String> courtNormColKeyMap = new HashMap<String, String>();
		courtNormColKeyMap.put(getCountyCourtNorm(), getColKey());
		return courtNormColKeyMap;
	}
}
