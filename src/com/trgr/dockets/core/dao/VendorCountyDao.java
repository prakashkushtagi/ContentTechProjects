package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.VendorCounty;

public interface VendorCountyDao {

	public List<VendorCounty> getActiveVendorCounties(Long vendorId);
}
