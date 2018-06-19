package com.trgr.dockets.core.util;

import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.Court;

public class CourtCountyConfig {
	private County county;
	private Court court;
	private long pubid;
	private String jrecsContext;
	private String colkey;
	private long countyId;
	private long courtId;
	
	public String getJrecsContext() {
		return jrecsContext;
	}
	public void setJrecsContext(String jrecsContext) {
		this.jrecsContext = jrecsContext;
	}
	public County getCounty() {
		return county;
	}
	public void setCounty(County county) {
		this.county = county;
	}
	public Court getCourt() {
		return court;
	}
	public void setCourt(Court court) {
		this.court = court;
	}
	public Long getPubid() {
		return pubid;
	}
	public void setPubid(Long pubid) {
		this.pubid = pubid;
	}
	public long getCountyId() {
		return countyId;
	}
	public void setCountyId(long countyId) {
		this.countyId = countyId;
	}
	public long getCourtId() {
		return courtId;
	}
	public void setCourtId(long courtId) {
		this.courtId = courtId;
	}
	public String getColkey() {
		return colkey;
	}
	public void setColkey(String colkey) {
		this.colkey = colkey;
	}
	
	
}
