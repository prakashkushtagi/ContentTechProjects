package com.trgr.dockets.core.domain;

import java.util.List;
import java.util.Map;

import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.util.CourtCountyConfig;

public class DocketStaticConfig {
	private List<Court> courtList;
	private List<County> countyList;
	private Map<String, Court> courtMap;
	private Map<String, County> countyMap;
	private Map<String, CourtCountyConfig> courtCountyConfigMap;
	
	public List<Court> getCourtList() {
		return courtList;
	}
	public void setCourtList(List<Court> courtList) {
		this.courtList = courtList;
	}
	public List<County> getCountyList() {
		return countyList;
	}
	public void setCountyList(List<County> countyList) {
		this.countyList = countyList;
	}
	public Map<String, Court> getCourtMap() {
		return courtMap;
	}
	public void setCourtMap(Map<String, Court> courtMap) {
		this.courtMap = courtMap;
	}
	public Map<String, County> getCountyMap() {
		return countyMap;
	}
	public void setCountyMap(Map<String, County> countyMap) {
		this.countyMap = countyMap;
	}
	public Map<String, CourtCountyConfig> getCourtCountyConfigMap() {
		return courtCountyConfigMap;
	}
	public void setCourtCountyConfigMap(
			Map<String, CourtCountyConfig> courtCountyConfigMap) {
		this.courtCountyConfigMap = courtCountyConfigMap;
	}
}
