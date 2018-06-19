package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class VendorCountyKey implements Serializable {

	private static final long serialVersionUID = 230301433424115059L;
	
	private Long vendorId;
	private Long countyId;

	public VendorCountyKey(){
		super();
	}

	public VendorCountyKey(Long vendorId, Long countyId){
		this.setVendorId(vendorId);
		this.setCountyId(countyId);
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countyId == null) ? 0 : countyId.hashCode());
		result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VendorCountyKey other = (VendorCountyKey) obj;
		if (countyId == null) {
			if (other.countyId != null)
				return false;
		} else if (!countyId.equals(other.countyId))
			return false;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		} else if (!vendorId.equals(other.vendorId))
			return false;
		return true;
	}
}
