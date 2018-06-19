/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com) 
 *
 */
@Entity
@Table(name="SOURCE_LOADER_ERROR",schema="DOCKETS_PUB")
public class ErrorLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long errorId;
	private Long requestId;
	private String errorCode;
	private String errorDescription;
	
	
	public ErrorLog(Long requestId, String errorCode, String errorDescription) {
		super();
		this.requestId = requestId;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	
	@Column(name= "REQUEST_ID",nullable= false)
	public Long getRequestId() {
		return requestId;
	}
	
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	
	@Column(name = "ERROR_CODE", nullable = true)
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	@Column(name ="ERROR_DESCRIPTION",nullable = false)
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	@Id
	@Column(name ="ERROR_ID",nullable = false)
	@GeneratedValue(generator ="ErrorIdSeq")
	@SequenceGenerator(name="ErrorIdSeq",sequenceName="DOCKETS_PUB.SOURCE_LOADER_ERROR_SEQ")
	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}
	

}
