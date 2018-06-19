/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author U0160964	Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */
@Entity
@Table(name="SOURCE_CONTENT_REQUEST",schema="DOCKETS_PUB")
public class SourceLoadRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long requestId;
	private String batchId;
	private String productCode;
	private String sourcePath;
	private String acquisitionType;
	private String acquisitionMethod;
	private Date timestamp;
	private boolean deleteFlag;
	
	
	public SourceLoadRequest(String batchId,String productCode, String updateType, String sourcePath, String acquisitionType, String acquisitionMethod, Timestamp timestamp, boolean deleteFlag) {
		super();
		this.batchId = batchId;
		this.productCode = productCode;
		this.sourcePath = sourcePath;
		this.acquisitionType = acquisitionType;
		this.acquisitionMethod = acquisitionMethod;
		this.timestamp = timestamp;
		this.deleteFlag = deleteFlag;
		
	}
	/**
	 * 
	 */
	public SourceLoadRequest() {
		// TODO Auto-generated constructor stub
	}
	@Id
	@Column(name= "REQUEST_ID",nullable= false)
	@GeneratedValue(generator="SourceRequestIdSeq")
	@SequenceGenerator(name="SourceRequestIdSeq",sequenceName="DOCKETS_PUB.SOURCE_CONTENT_REQUEST_SEQ")
	public Long getRequestId() {
		return requestId;
	}
	
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	
	@Column(name = "BATCH_ID", nullable = false)
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	@Column(name="PRODUCT_CODE",nullable=false)
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	
	@Column(name ="SOURCE_PATH",nullable = false)
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	
	@Column(name ="ACQUISITION_TYPE",nullable = false)
	public String getAcquisitionType() {
		return acquisitionType;
	}
	public void setAcquisitionType(String acquisitionType) {
		this.acquisitionType = acquisitionType;
	}
	
	@Column(name ="ACQUISITION_METHOD",nullable = false)
	public String getAcquisitionMethod() {
		return acquisitionMethod;
	}
	public void setAcquisitionMethod(String acquisitionMethod) {
		this.acquisitionMethod = acquisitionMethod;
	}

	@Column(name ="TIMESTAMP",nullable = false)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Column(name = "DELETE_FLAG", nullable = false)
	@Type(type = "yes_no")
	public boolean getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	@Override
	public String toString() {
		return "SourceLoadRequest [requestId=" + requestId + ", batchId="
				+ batchId + ", productCode=" + productCode + ", sourcePath="
				+ sourcePath + ", acquisitionType=" + acquisitionType
				+ ", acquisitionMethod=" + acquisitionMethod + ", timestamp="
				+ timestamp + ", deleteFlag=" + deleteFlag + "]";
	}
	
}
