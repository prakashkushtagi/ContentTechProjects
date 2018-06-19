/**Copyright 2015: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;

import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.westgroup.publishingservices.uuidgenerator.UUID;

@XmlRootElement(name="preprocessor_request") 
public class DataCaptureRequest extends PreProcessorRequest {
	
	private String publishingRequestId;
	private Date startTime;	// Expressed as "yyyyMMdd.HHmmss"
	private Date endTime;	// Expressed as "yyyyMMdd.HHmmss"
	private File metadataFile;
	private String requestName;
	private Long publishingPriority;  // 0="on hold", Higher the priority, the sooner the request will be started
	private String sharedServicePriority;
	private String requestType;
	private Long receiptId;
	private String dcProvider;
	private String aqTimeOverride;
	private long vendorId;
	private Long rpxTimeoutOverride;
		
	@XmlElement(name="startTime")
	public String getStartTimeString(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		return (startTime != null) ? sdf.format(startTime) : null;
	}
	@XmlElement(name="endTime")
	public String getEndTimeString(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		return (endTime != null) ? sdf.format(endTime) : null;
	}
	@XmlElement(name="metadataFile")
	public String getMetadataFilename() {
		return (metadataFile != null) ? metadataFile.getPath() : null;
	}
	@XmlElement(name="requestId")
	public String getPublishingRequestId() {
		return publishingRequestId;
	}
	@XmlElement(name="requestName")
	public String getRequestName() {
		return requestName;
	}
	@XmlElement(name="priority")
	public Long getPublishingPriority() {
		return (publishingPriority != null) ? publishingPriority : 11l;  // default is 11 for DC request always.
	}
	@XmlElement(name="sharedServicePriority")
	public String getSharedServicePriority() {
		return (sharedServicePriority != null) ? sharedServicePriority : "HIGH";  // default is HIGH for DC request always.
	}
	@XmlElement(name="requestType")
	public String getRequestType() {
		return requestType;
	}
	@XmlElement(name="receiptId")
	public Long getReceiptId() {
		return receiptId;
	}
	@XmlElement(name="dcProvider")
	public String getDcProvider() {
		return dcProvider;
	}
	@Override
	@XmlElement(name="vendorId")
	public long getVendorId() {
		return vendorId;
	}
	
	@XmlElement(name="aqTimeOverride")
	public String getAqTimeOverride() {
		return (aqTimeOverride != null) ? aqTimeOverride : "N";
	}
	@XmlElement(name="rpxTimeoutOverride")
	public Long getRpxTimeoutOverride() {
		return rpxTimeoutOverride;
	}
	
	@XmlTransient
	public RequestTypeEnum getRequestTypeEnum() {
		return RequestTypeEnum.findByCode(requestType);
	}
	
	@Override
	@XmlTransient
	public Date getStartTime() {
		return startTime;
	}
	@Override
	@XmlTransient
	public Date getEndTime() {
		return endTime;
	}
	@XmlTransient
	public File getMetadataFile() {
		return metadataFile;
	}
	
	public void setRequestType(String requestType){
		this.requestType = requestType;
	}
	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}
	public void setDcProvider(String dcProvider) {
		this.dcProvider = dcProvider;
	}
	public void setPublishingPriority(Long publishingPriority) {
		this.publishingPriority = publishingPriority;
	}
	
	public void setSharedServicePriority(String sharedServicePriority) {
		this.sharedServicePriority = sharedServicePriority;
	}
	
	public void setPublishingRequestUuid(UUID pubReqUuid) {
		setPublishingRequestId(pubReqUuid.toString());
	}
	public void setPublishingRequestId(String requestId) {
		this.publishingRequestId = requestId;
	}
	public void setStartTime(Date time) {
		this.startTime = time;
	}
	public void setEndTime(Date time) {
		this.endTime = time;
	}
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	
	public void setAqTimeOverride(String aqTimeOverride){
		this.aqTimeOverride = aqTimeOverride;
	}
	
	public void setRpxTimeoutOverride(Long rpxTimeoutOverride){
		this.rpxTimeoutOverride = rpxTimeoutOverride;
	}	
	
	protected void setStartTimeString(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		setStartTime(StringUtils.isNotBlank(time) ? sdf.parse(time) : null);
	} 
	protected void setEndTimeString(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		setEndTime(StringUtils.isNotBlank(time) ? sdf.parse(time) : null);
	}
	public void setMetadataFile(File metadataFile) {
		this.metadataFile = metadataFile;
	}
	public void setMetadataFilename(String metadataFilename) {
		setMetadataFile(new File(metadataFilename));
	}
	

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dcProvider == null) ? 0 : dcProvider.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((metadataFile == null) ? 0 : metadataFile.hashCode());
		result = prime * result
				+ ((publishingPriority == null) ? 0 : publishingPriority.hashCode());
		result = prime * result
				+ ((sharedServicePriority == null) ? 0 : sharedServicePriority.hashCode());		
		result = prime * result
				+ ((publishingRequestId == null) ? 0 : publishingRequestId.hashCode());
		result = prime * result
				+ ((receiptId == null) ? 0 : receiptId.hashCode());
		result = prime * result
				+ ((requestName == null) ? 0 : requestName.hashCode());
		result = prime * result
				+ ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataCaptureRequest other = (DataCaptureRequest) obj;
		if (dcProvider == null) {
			if (other.dcProvider != null)
				return false;
		} else if (!dcProvider.equals(other.dcProvider))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (metadataFile == null) {
			if (other.metadataFile != null)
				return false;
		} else if (!metadataFile.equals(other.metadataFile))
			return false;
		if (publishingPriority == null) {
			if (other.publishingPriority != null)
				return false;
		} else if (!publishingPriority.equals(other.publishingPriority))
			return false;
		if (sharedServicePriority == null) {
			if (other.sharedServicePriority != null)
				return false;
		} else if (!sharedServicePriority.equals(other.sharedServicePriority))
			return false;
		if (publishingRequestId == null) {
			if (other.publishingRequestId != null)
				return false;
		} else if (!publishingRequestId.equals(other.publishingRequestId))
			return false;
		if (receiptId == null) {
			if (other.receiptId != null)
				return false;
		} else if (!receiptId.equals(other.receiptId))
			return false;
		if (requestName == null) {
			if (other.requestName != null)
				return false;
		} else if (!requestName.equals(other.requestName))
			return false;
		if (requestType == null) {
			if (other.requestType != null)
				return false;
		} else if (!requestType.equals(other.requestType))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataCaptureRequest [publishingRequestId=" + publishingRequestId
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", metadataFile=" + metadataFile + ", requestName="
				+ requestName + ", publishingPriority=" + publishingPriority 
				+", sharedServicesPriority=" + sharedServicePriority + ", requestType="
				+ requestType + ", receiptId=" + receiptId + ", dcProvider="
				+ dcProvider + "]";
	}
}
