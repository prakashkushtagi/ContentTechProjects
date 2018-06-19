package com.trgr.dockets.core.service;

import java.util.Set;

import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.VendorCounty;

public interface VendorCountyService {

	public Set<VendorCounty> getActiveVendorCounties(Long vendorId);
	
	public Set<County> getActiveCounties(Long vendorId);
}
