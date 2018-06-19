/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The configuration used to control the way in which the dockets are 
 * handled thru the processing pipeline.
 */
@Entity
@Table(name="LARGE_DOCKET_CRITERIA", schema="DOCKETS_PREPROCESSOR")
public class LargeDocketCriteria {
	
	private int productId;
	private int docketParties;
	private int docketEntries;
	
	public LargeDocketCriteria() {
		super();
	}
	
	public LargeDocketCriteria(int productId, int docketParties, int docketEntries) {
		this.productId = productId;
		this.docketParties = docketParties;
		this.docketEntries = docketEntries;
	}
	
	@Column(name="PARTIES_COUNT", nullable = true, columnDefinition ="int 0")
	public int getDocketParties() {
		return docketParties;
	}
	
	@Column(name="DOCKET_ENTRIES_COUNT", nullable = true,  columnDefinition = "int 0")
	public int getDocketEntries() {
		return docketEntries;
	}
	
	public void setDocketParties(int docketParties) {
		this.docketParties = docketParties;
	}
	
	public void setDocketEntries(int docketEntries) {
		this.docketEntries = docketEntries;
	}
	
	@Id
	@Column(name="PRODUCT_ID", nullable = false, columnDefinition = "int 0")
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
