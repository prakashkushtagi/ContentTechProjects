/* Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.domain.DocketStaticConfig;
import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.util.CourtCountyConfig;

/**
 * A factory bean used to create a Docket Static config bean.
 * Used in the spring bean configuration XML files.
 */
public class DocketConfigFactoryImpl implements DocketConfigFactory {
	
	private static final Logger log = Logger.getLogger(DocketConfigFactoryImpl.class);
	
	private DocketService docketService;
	private CountyService countyService;
	
	public DocketConfigFactoryImpl(DocketService docketService, CountyService countyService) {
		this.docketService = docketService;
		this.countyService = countyService;
	}
	

	@Transactional(readOnly=true)
	public DocketStaticConfig populateDocketStaticConfig() {
		DocketStaticConfig docketStaticConfig = new DocketStaticConfig();

		//put values in docketStaticConfig using setters
		List<Court> courtList = docketService.loadCourt();
		docketStaticConfig.setCourtList(courtList);
		List<County> countyList = countyService.loadCounty();
		docketStaticConfig.setCountyList(countyList);
		log.info("Courts and counties have been loaded to docketStaticConfig");
        
		//generate courtMap
		Map<String, County> countyMap = new HashMap<String, County>();
		Map<String, Court> courtMap = new HashMap<String, Court>();
		Map<String, CourtCountyConfig> courtCountyConfigMap = new HashMap<String, CourtCountyConfig>();
		
		for (Court court : courtList){
			courtMap.put(court.getCourtCluster().toLowerCase(), court);
		}
		docketStaticConfig.setCourtMap(courtMap);
		
		for (County county : docketStaticConfig.getCountyList() )
		{
			countyMap.put(county.getName().toLowerCase()+county.getCourtId().toString(), county);
		}
		docketStaticConfig.setCountyMap(countyMap);
		
		for (County county : countyList){
			
			CourtCountyConfig courtCountyConfig = new CourtCountyConfig();
			
			String courtCountyKey = county.getCourtId() + "_" + county.getCountyId();
			
			courtCountyConfig.setCounty(county);
			courtCountyConfig.setCountyId(county.getCountyId());
			
			Integer courtId = county.getCourtId();
			Court court = null;
			for(Court indCourt : courtList){
				if(indCourt.getPrimaryKey().intValue() == courtId){
					court = indCourt;
					break;
				}
			}
			
			courtCountyConfig.setCourt(court);
			courtCountyConfig.setCourtId(courtId);

			courtCountyConfig.setColkey(county.getColKey());
			
			if(null == county.getPublicationId()){
				courtCountyConfig.setPubid(court.getPublicationId());
			}else{
				courtCountyConfig.setPubid(county.getPublicationId());
			}
			
			String jrecsContext = "Root/PublicRecords/StateDocket/" + county.getColKey();
			courtCountyConfig.setJrecsContext(jrecsContext);
			
			courtCountyConfigMap.put(courtCountyKey, courtCountyConfig);
		}
		
		for (Court court : courtList){
			
			CourtCountyConfig courtCountyConfig = new CourtCountyConfig();
			
			String courtCountyKey = court.getPrimaryKey().toString();
			
			courtCountyConfig.setCourt(court);
			courtCountyConfig.setCourtId(court.getPrimaryKey().intValue());
			courtCountyConfig.setColkey(court.getCourtCluster());
			courtCountyConfig.setPubid(court.getPublicationId());
			
			String jrecsContext = null;
			if (court.getProduct().getId() == 1L) { // BKR
				jrecsContext = "Root/PublicRecords/stuff";
			} else if (court.getProduct().getId() == 2L) { // JPML dockets
				jrecsContext = "Root/PublicRecords/FedJPMLDockets";
			} else if (court.getProduct().getId() == 3L) { // STATE
				jrecsContext = "Root/PublicRecords/StateDocket/" +  court.getCourtCluster();
			} else if (court.getProduct().getId() == 4L) { // PTAB -- so far
				jrecsContext = "Root/PublicRecords/" + court.getCourtCluster();
			} else if (court.getProduct().getId() == 5L){ //DCT dockets
				jrecsContext = "Root/PublicRecords/FedDCTDocket";
			} else {
				log.error("JRECS context cannot be assigned to a court for product:" + court.getProduct().getDisplayName() + "!");
			}
			if(null != jrecsContext){
				courtCountyConfig.setJrecsContext(jrecsContext);
			}
			
			courtCountyConfigMap.put(courtCountyKey, courtCountyConfig);
		}
		
		//generate countyMap	
		//docketStaticConfig.setJrecsCourtMap(jrecsCourtMap);
		//docketStaticConfig.setJrecsCourtCountyMap(jrecsCourtCountyMap);
		docketStaticConfig.setCourtCountyConfigMap(courtCountyConfigMap);
		return docketStaticConfig;
	}


	public DocketService getDocketService() {
		return docketService;
	}


	public void setDocketService(DocketService docketService) {
		this.docketService = docketService;
	}


	public CountyService getCountyService() {
		return countyService;
	}


	public void setCountyService(CountyService countyService) {
		this.countyService = countyService;
	}
}
