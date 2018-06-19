/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.domain;

import java.io.File;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.util.DocketsCoreUtility;


@XmlRootElement(name="sourceLoadRequest") 
public class SourceLoadRequest {
	public static final Logger log = Logger.getLogger(SourceLoadRequest.class);

	private long requestId;
	private AcquisitionMethod acquisitionMethod;
	private Date acquisitionStart;
	private RequestInitiatorTypeEnum acquisitionType;
	private String batchId;
	private boolean deleteFlag;
	private ProductEnum product;
	private File sourceFile;
	
	@XmlTransient
	public AcquisitionMethod getAcquisitionMethod() {
		return acquisitionMethod;
	}
	@XmlElement(name="acquisitionMethod")
	private String getAcquisitionMethodString() {
		return (acquisitionMethod != null) ? acquisitionMethod.toString() : null;
	}
	@XmlTransient
	public Date getAcquisitionStart() {

		return acquisitionStart;
	}
	@XmlElement(name="acquisitionStart")
	private String getAcquisitionStartString() {
		return (acquisitionStart != null) ? DocketsCoreUtility.createAcquisitionTimeStamp(acquisitionStart, product.name()) : null;
	}
	@XmlTransient
	public RequestInitiatorTypeEnum getRequestInitiatorType() {
		return acquisitionType;
	}
	@XmlElement(name="acquisitionType")
	private String getAcquisitionTypeString() {
		return (acquisitionType != null) ? acquisitionType.toString() : null;
	}
	@XmlElement(name="batchId")
	public String getBatchId() {
		return batchId;
	}
	@XmlElement(name="deleteFlag")
	private String getDeleteFlag() {
		return String.valueOf(deleteFlag);
	}
	@XmlTransient
	public ProductEnum getProduct() {
		return product;
	}
	@XmlElement(name="productCode")
	private String getProductString() {
		return product.name();
	}
	@XmlTransient
	public File getSourceFile() {
		return sourceFile;
	}
	@XmlElement(name="sourceFile")
	private String getSourceFilename() {
		return sourceFile.getAbsolutePath();
	}
	@XmlTransient
	public boolean isDelete() {
		return deleteFlag;
	}

	public void setAcquisitionMethod(AcquisitionMethod acquisitionMethod) {
		this.acquisitionMethod = acquisitionMethod;
	}
	@SuppressWarnings("unused")
	private void setAcquisitionMethodString(String acquisitionMethod) {
		setAcquisitionMethod(AcquisitionMethod.valueOf(acquisitionMethod));
	}
	public void setAcquisitionStart(Date date) {
		this.acquisitionStart = date;
	}
	@SuppressWarnings("unused")
	private void setAcquisitionStartString(String stringDate) {
		Date formatedDate = null;
		if(product.name().equalsIgnoreCase("FBR")){
				formatedDate = DocketsCoreUtility.getAcquisitionTimeStamp(stringDate,product.name());
		}else if(product.name().equalsIgnoreCase("JPML")){
				formatedDate = DocketsCoreUtility.getAcquisitionTimeStamp(stringDate,product.name());
		}
		setAcquisitionStart(formatedDate);
	}
	public void setRequestInitiatorType(RequestInitiatorTypeEnum requestInitiatorType) {
		this.acquisitionType = requestInitiatorType;
	}
	@SuppressWarnings("unused")
	private void setAcquisitionTypeString(String acquisitionType) {
		setRequestInitiatorType(RequestInitiatorTypeEnum.findByCode(acquisitionType));
	}
	public void setBatchId(String batchId)  {
		this.batchId = batchId;
	}
	public void setDelete(boolean delete) {
		this.deleteFlag = delete;
	}
	@SuppressWarnings("unused")
	private void setDeleteFlag(String delete) {
		setDelete(Boolean.valueOf(delete));
	}

	public void setProduct(ProductEnum product) {
		this.product = product;
	}
	@SuppressWarnings("unused")
	private void setProductString(String productCode) {
		setProduct(ProductEnum.findByCode(productCode));
	}
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	@SuppressWarnings("unused")
	private void setSourceFilename(String sourceFilenameAbsolutePath) {
		setSourceFile(new File(sourceFilenameAbsolutePath));
	}

	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((acquisitionMethod == null) ? 0 : acquisitionMethod
						.hashCode());
		result = prime
				* result
				+ ((acquisitionStart == null) ? 0 : acquisitionStart.hashCode());
		result = prime * result
				+ ((acquisitionType == null) ? 0 : acquisitionType.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + (deleteFlag ? 1231 : 1237);
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + (int) (requestId ^ (requestId >>> 32));
		result = prime * result
				+ ((sourceFile == null) ? 0 : sourceFile.hashCode());
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
		SourceLoadRequest other = (SourceLoadRequest) obj;
		if (acquisitionMethod != other.acquisitionMethod)
			return false;
		if (acquisitionStart == null) {
			if (other.acquisitionStart != null)
				return false;
		} else if (!acquisitionStart.equals(other.acquisitionStart))
			return false;
		if (acquisitionType != other.acquisitionType)
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (deleteFlag != other.deleteFlag)
			return false;
		if (product != other.product)
			return false;
		if (requestId != other.requestId)
			return false;
		if (sourceFile == null) {
			if (other.sourceFile != null)
				return false;
		} else if (!sourceFile.equals(other.sourceFile))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	 
	/**
	 * 
	 * @param deleteFlag
	 */
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}
