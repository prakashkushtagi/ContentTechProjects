package com.trgr.dockets.core.service;

import com.trgr.dockets.core.domain.DocketStaticConfig;

public interface DocketConfigFactory {
	
	public DocketStaticConfig populateDocketStaticConfig();
	
	public DocketService getDocketService();

	public void setDocketService(DocketService docketService);

	public CountyService getCountyService();

	public void setCountyService(CountyService countyService);
}
