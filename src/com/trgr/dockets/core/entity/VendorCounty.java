package com.trgr.dockets.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="VENDOR_COUNTY", schema="DOCKETS_PUB")
public class VendorCounty {
	
	private VendorCountyKey primaryKey;
	private County county;
	private char active;
	
	public VendorCounty(){
		super();
	}
	
	public VendorCounty(VendorCountyKey primaryKey){
		setPrimaryKey(primaryKey);
	}
	
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="countyId", column=@Column(name="COUNTY_ID")),
			@AttributeOverride(name="vendorId", column=@Column(name="VENDOR_ID"))
	})
	public VendorCountyKey getPrimaryKey() 
	{
		return primaryKey;
	}
	
	@ManyToOne
	@JoinColumn(name="COUNTY_ID", insertable=false, updatable=false)
	@Basic(fetch = FetchType.EAGER)
	public County getCounty() {
		return county;
	}
	
	@Column(name = "ACTIVE")
	public char getActive(){
		return active;
	}
	
	public void setPrimaryKey(VendorCountyKey primaryKey){
		this.primaryKey = primaryKey;
	}
	
	public void setActive(char active){
		this.active = active;
	}
	
	public void setCounty(County county){
		this.county = county;
	}
}
