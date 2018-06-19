package com.trgr.dockets.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.trgr.dockets.core.dao.VendorCountyDao;
import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.VendorCounty;

public class VendorCountyServiceImpl implements VendorCountyService {
	
	@Autowired
	private VendorCountyDao vendorCountyDao;	

	@Override
	public Set<VendorCounty> getActiveVendorCounties(Long vendorId) {
		List<VendorCounty> vendorCounties = vendorCountyDao.getActiveVendorCounties(vendorId);
		if (vendorCounties != null){
			return new HashSet<VendorCounty>(vendorCounties);
		}
		return new HashSet<VendorCounty>();
	}

	@Override
	public Set<County> getActiveCounties(Long vendorId) {
		List<VendorCounty> vendorCounties = vendorCountyDao.getActiveVendorCounties(vendorId);
		Set<County> activeCounties = new HashSet<County>();
		if (vendorCounties != null){
			for (VendorCounty vc : vendorCounties){
				activeCounties.add(vc.getCounty());
			}			
		}
		return activeCounties;
	}
	
	public void setVendorCountyDao(VendorCountyDao vendorCountyDao) {
		this.vendorCountyDao = vendorCountyDao;
	}

}
