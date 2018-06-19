/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.entity;

import static com.trgr.dockets.core.CoreConstants.REQUEST_OWNER_DATACAP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Entity
@Table(name="PUBLISHING_REQUEST", schema="DOCKETS_PUB")
public class PublishingRequest implements Serializable {
	
	private static final long serialVersionUID = -3772492121134137453L;
	private UUID requestId;  	// primary key
	private String requestName;
	private String requestOwner;
	
	private boolean deleteOverride;
	private boolean prismClipDateOverride;
	private boolean publishing;
	private boolean acquisitionTimeOverride;
	private boolean useDefaultBatchSize;
	
	private Date startDate;
	private Date endDate;
	private Date loadRequestDate;
	
	private Long auditKey;
	private RequestInitiatorTypeEnum requestInitiatorType;
	
	private Product product;
	private WorkflowTypeEnum workflowType;
	private RequestTypeEnum requestType;
	private Status publishingStatus;
	private Court court;
	private Long rpxTimeoutOverride;
	//private String rpxReleaseOverride;
	
	private Long receiptId;
	private String dcProvider;
	
	private Long vendorId;
	
	private Long publishingPriority;
	
	private String sharedServicePriority;
	private boolean largeDocketRequest;
	private String rpxConfigOverride;
	private String serverName;
	public PublishingRequest() {
		super();
	}
	
	public PublishingRequest(UUID requestId, String requestName,
			String requestOwner, boolean deleteOverride,
			boolean prismClipDateOverride, boolean publishing,
			Date startDate, Date endDate, Date loadRequestDate, Long auditKey,
			RequestInitiatorTypeEnum initiatorType, Product product,
			WorkflowTypeEnum workflowType, RequestTypeEnum requestType,
			Status status, Court court, Long rpxTimeoutOverride, 
			Long receiptId, String dcProvider, Long vendorId, boolean acquisitionTimeOverride, 
			Long publishingPriority, String sharedServicePriority,String rpxConfigOverride,String serverName) 
	{
		setPrimaryKey(requestId);
		setRequestName(requestName);
		setRequestOwner(requestOwner);
		setDeleteOverride(deleteOverride);
		setPrismClipDateOverride(prismClipDateOverride);
		setPublishing(publishing);
		setStartDate(startDate);
		setEndDate(endDate);
		setLoadRequestDate(loadRequestDate);
		setAuditKey(auditKey);
		setRequestInitiatorType(initiatorType);
		setProduct(product);
		setWorkflowType(workflowType);
		setRequestType(requestType);
		setPublishingStatus(status);
		setCourt(court);
		setRpxTimeoutOverride(rpxTimeoutOverride);
		//setRpxReleaseOverride(rpxReleaseOverride);
		setReceiptId(receiptId);
		setDcProvider(dcProvider);
		setVendorId(vendorId);
		setAcquisitionTimeOverride(acquisitionTimeOverride);
		setPublishingPriority(publishingPriority);
		setSharedServicePriority(sharedServicePriority);
		setRpxConfigOverride(rpxConfigOverride);
		setServerName(serverName);
	}

	public PublishingRequest(UUID requestId) {
		setPrimaryKey(requestId);
	}
	public PublishingRequest(String requestId) throws UUIDException {
		setRequestId(requestId);
	}
	
	@Column(name="ACQUISITION_TIME_OVERRIDE", nullable=false)
	protected String getAcquisitionTimeOverrideString() {
		return (acquisitionTimeOverride ? "T" : "F");
	}
	@Column(name="USE_DEFAULT_BATCH_SIZE", nullable=false)
	protected String getUseDefaultBatchSizeString() {
		return (useDefaultBatchSize ? "T" : "F");
	}
	@Column(name="AUDIT_ID")
	public Long getAuditKey() {
		return auditKey;
	}
	@Column(name="DELETE_OVERRIDE", nullable=false)
	private String getDeleteOverrideString() {
		return (deleteOverride ? "T" : "F");
	}
	@Column(name="END_TIMESTAMP")
	public Date getEndDate() {
		return endDate;
	}
	@Column(name="INITIATOR_TYPE_ID")
	public Long getRequestInitiatorTypeKey() {
		return (requestInitiatorType != null) ? requestInitiatorType.getKey() : null;
	}
	@Column(name="LOAD_REQUEST_DATE")
	public Date getLoadRequestDate() {
		return loadRequestDate;
	}
	@Column(name="PRISM_CLIPDATE_OVERRIDE", nullable=false)
	protected String getPrismClipDateOverrideString() {
		return (prismClipDateOverride ? "T" : "F");
	}

	@OneToOne
	@JoinColumn(name="PRODUCT_ID")
	public Product getProduct() {
		return product;
	}
	@Column(name="PUBLISHING_IND")
	protected String getPublishingString() {
		return publishing ? "Y" : "N";
	}
	@Id
	@Column(name="REQUEST_ID")
	public String getRequestId() {
		return (requestId != null) ? requestId.toString() : null;
	}
	@Column(name="REQUEST_NAME", nullable=false)
	public String getRequestName() {
		return requestName;
	}
	@Column(name="REQUEST_OWNER", nullable=false)
	public String getRequestOwner() {
		return requestOwner;
	}
	@Column(name="REQUEST_TYPE_ID")
	public Long getRequestTypeKey() {
		return (requestType != null) ? requestType.getKey() : null;
	}
	@Column(name="START_TIMESTAMP")
	public Date getStartDate() {
		return startDate;
	}
	@OneToOne
	@JoinColumn(name="STATUS_ID")
	public Status getPublishingStatus() {
		return publishingStatus;
	}
	@Column(name="WORKFLOW_TYPE", nullable=false)
	public String getWorkflowTypeCode() {
		return workflowType.getCode();
	}
	
	@OneToOne
	@JoinColumn(name="COURT_ID")
	public Court getCourt() {
		return court;
	}

	@Column(name="RPX_TIMEOUT_OVERRIDE")
	public Long getRpxTimeoutOverride() {
		return rpxTimeoutOverride ;
	}
	@Column(name="RECEIPT_ID")
	public Long getReceiptId() {
		return receiptId;
	}
	@Column(name="DATA_PROVIDER")
	public String getDcProvider() {
		return dcProvider;
	}
	
	@Column(name="VENDOR_ID")
	public Long getVendorId() {
		return vendorId;
	}
	
	@Column(name= "DISPATCHER_SERVER")
	public String getServerName(){
		return serverName;
	}
	
	//Always highest if null
	@Column(name="PRIORITY", nullable = false)
	public Long getPublishingPriority() {
		return (publishingPriority != null) ? publishingPriority : 11l;
	}
	
	//Always highest if null
	@Column(name="SHARED_SERVICE_PRIORITY")
	public String getSharedServicePriority() {
		return (sharedServicePriority != null) ? sharedServicePriority : "HIGH";
	}
	@Column(name="RPX_CONFIG_OVERRIDE")
	public String getRpxConfigOverride() {
		return rpxConfigOverride;
	}

	@Transient
	public UUID getPrimaryKey() {
		return requestId;
	}
	@Transient
	public RequestTypeEnum getRequestType() {
		return requestType;
	}
	@Transient
	public RequestInitiatorTypeEnum getRequestInitiatorType() {
		return requestInitiatorType;
	}
	@Transient
	public WorkflowTypeEnum getWorkflowType() {
		return workflowType;
	}

	@Transient
	public boolean isDeleteOverride() {
		return deleteOverride;
	}
	@Transient
	public boolean isAcquisitionTimeOverride() {
		return acquisitionTimeOverride;
	}
	@Transient
	public boolean isPrismClipDateOverride() {
		return prismClipDateOverride;
	}
	@Transient
	public boolean isPublishing() {
		return publishing;
	}
	@Transient
	public boolean isUseDefaultBatchSize() {
		return useDefaultBatchSize;
	}


	public void setAuditKey(Long key) {
		this.auditKey = key;
	}
	public void setCourt(Court court) {
		this.court = court;
	}
	public void setDeleteOverride(boolean deleteOverride) {
		this.deleteOverride = deleteOverride;
	}
	protected void setDeleteOverrideString(String deleteOverride) {
		this.deleteOverride = "T".equalsIgnoreCase(deleteOverride);
	}

	public void setAcquisitionTimeOverride(boolean acquisitionTimeOverride) {
		this.acquisitionTimeOverride = acquisitionTimeOverride;
	}
	protected void setAcquisitionTimeOverrideString(String acquisitionTimeOverride) {
		this.acquisitionTimeOverride = "T".equalsIgnoreCase(acquisitionTimeOverride);
	}
	
	public void setUseDefaultBatchSize(boolean useDefaultBatchSize) {
		this.useDefaultBatchSize = useDefaultBatchSize;
	}
	protected void setUseDefaultBatchSizeString(String useDefaultBatchSize) {
		this.useDefaultBatchSize = "T".equalsIgnoreCase(useDefaultBatchSize);
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setRequestId(String requestId) throws UUIDException {
		setPrimaryKey(new UUID(requestId));
	}
	public void setPrimaryKey(UUID requestId) {
		this.requestId = requestId;
	}
	public void setRequestInitiatorType(RequestInitiatorTypeEnum type) {
		this.requestInitiatorType = type;
	}
	protected void setRequestInitiatorTypeKey(Long key) {
		setRequestInitiatorType(RequestInitiatorTypeEnum.findByKey(key));
	}
	public void setLoadRequestDate(Date loadRequestDate) {
		this.loadRequestDate = loadRequestDate;
	}
	public void setPrismClipDateOverride(boolean prismClipDateOverride) {
		this.prismClipDateOverride = prismClipDateOverride;
	}
	protected void setPrismClipDateOverrideString(String prismClipDateOverride) {
		this.prismClipDateOverride = "T".equalsIgnoreCase(prismClipDateOverride);
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public void setPublishing(boolean publishing) {
		this.publishing = publishing;
	}
	protected void setPublishingString(String publishing) {
		this.publishing = "Y".equalsIgnoreCase(publishing);
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public void setRequestOwner(String requestOwner) {
		this.requestOwner = requestOwner;
	}
	
	public final void makeIntoDataCapRequest() {
		setRequestOwner(REQUEST_OWNER_DATACAP);
	}
	
	protected void setRequestTypeKey(Long key) {
		setRequestType(RequestTypeEnum.findByKey(key));
	}
	public void setRequestType(RequestTypeEnum type) {
		requestType = type;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setPublishingStatus(Status status) {
		this.publishingStatus = status;
	}
	public void setPublishingStatus(StatusEnum statusEnum) {
		setPublishingStatus(new Status(statusEnum));
	}
	public void setWorkflowType(WorkflowTypeEnum type) {
		this.workflowType = type;
	}
	public void setReceiptId(Long receiptId) {
		this.receiptId = receiptId;
	}
	public void setDcProvider(String dcProvider) {
		this.dcProvider = dcProvider;
	}
	
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
		
	}
	
	protected void setWorkflowTypeCode(String typeCode) {
		setWorkflowType(WorkflowTypeEnum.findByCode(typeCode));
	}
	/**
	 * @param rpxTimeoutOverride the rpxTimeoutOverride to set
	 */
	public void setRpxTimeoutOverride(Long rpxTimeoutOverride) {
		this.rpxTimeoutOverride = rpxTimeoutOverride;
	}
	
	public void setPublishingPriority(Long publishingPriority) {
		this.publishingPriority = (publishingPriority != null) ? publishingPriority : 11l;
	}
	
	public void setSharedServicePriority(String sharedServicePriority) {
		this.sharedServicePriority = (sharedServicePriority != null) ? sharedServicePriority : "HIGH";
	}
	
	public void setLargeDocketRequest(boolean largeDocketRequest) {
		this.largeDocketRequest = largeDocketRequest;
	}
	
	public void setRpxConfigOverride(String rpxConfigOverride) {
		this.rpxConfigOverride = rpxConfigOverride;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	
	@Override
	public String toString() {
		return "PublishingRequest [requestId=" + requestId + ", requestName="
				+ requestName + ", requestOwner=" + requestOwner
				+ ", deleteOverride=" + deleteOverride
				+ ", prismClipDateOverride=" + prismClipDateOverride
				+ ", publishing=" + publishing + ", acquisitionTimeOverride="
				+ acquisitionTimeOverride + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", loadRequestDate="
				+ loadRequestDate + ", auditKey=" + auditKey
				+ ", requestInitiatorType=" + requestInitiatorType
				+ ", product=" + product + ", workflowType=" + workflowType
				+ ", requestType=" + requestType + ", publishingStatus="
				+ publishingStatus + ", court=" + court
				+ ", rpxTimeoutOverride=" + rpxTimeoutOverride
				+ ", rpxConfigOverride=" + rpxConfigOverride + ", "
				+ "receiptId="
				+ receiptId + ", dcProvider=" + dcProvider + ", vendorId="
				+ vendorId + ", publishingPriority=" + publishingPriority
				+ serverName + ", serverName=" + serverName
				+ ", sharedServicePriority=" + sharedServicePriority + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (acquisitionTimeOverride ? 1231 : 1237);
		result = prime * result + ((auditKey == null) ? 0 : auditKey.hashCode());
		result = prime * result + ((court == null) ? 0 : court.hashCode());
		result = prime * result + ((dcProvider == null) ? 0 : dcProvider.hashCode());
		result = prime * result + (deleteOverride ? 1231 : 1237);
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((loadRequestDate == null) ? 0 : loadRequestDate.hashCode());
		result = prime * result + (prismClipDateOverride ? 1231 : 1237);
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + (publishing ? 1231 : 1237);
		result = prime * result + ((publishingPriority == null) ? 0 : publishingPriority.hashCode());
		result = prime * result + ((publishingStatus == null) ? 0 : publishingStatus.hashCode());
		result = prime * result + ((receiptId == null) ? 0 : receiptId.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((requestInitiatorType == null) ? 0 : requestInitiatorType.hashCode());
		result = prime * result + ((requestName == null) ? 0 : requestName.hashCode());
		result = prime * result + ((requestOwner == null) ? 0 : requestOwner.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + ((rpxConfigOverride == null) ? 0 : rpxConfigOverride.hashCode());
		result = prime * result + ((rpxTimeoutOverride == null) ? 0 : rpxTimeoutOverride.hashCode());
		result = prime * result + ((serverName == null) ? 0 : serverName.hashCode());
		result = prime * result + ((sharedServicePriority == null) ? 0 : sharedServicePriority.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
		result = prime * result + ((workflowType == null) ? 0 : workflowType.hashCode());
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
		PublishingRequest other = (PublishingRequest) obj;
		if (acquisitionTimeOverride != other.acquisitionTimeOverride)
			return false;
		if (auditKey == null) {
			if (other.auditKey != null)
				return false;
		} else if (!auditKey.equals(other.auditKey))
			return false;
		if (court == null) {
			if (other.court != null)
				return false;
		} else if (!court.equals(other.court))
			return false;
		if (dcProvider == null) {
			if (other.dcProvider != null)
				return false;
		} else if (!dcProvider.equals(other.dcProvider))
			return false;
		if (deleteOverride != other.deleteOverride)
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (loadRequestDate == null) {
			if (other.loadRequestDate != null)
				return false;
		} else if (!loadRequestDate.equals(other.loadRequestDate))
			return false;
		if (prismClipDateOverride != other.prismClipDateOverride)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (publishing != other.publishing)
			return false;
		if (publishingPriority == null) {
			if (other.publishingPriority != null)
				return false;
		} else if (!publishingPriority.equals(other.publishingPriority))
			return false;
		if (publishingStatus == null) {
			if (other.publishingStatus != null)
				return false;
		} else if (!publishingStatus.equals(other.publishingStatus))
			return false;
		if (receiptId == null) {
			if (other.receiptId != null)
				return false;
		} else if (!receiptId.equals(other.receiptId))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (requestInitiatorType != other.requestInitiatorType)
			return false;
		if (requestName == null) {
			if (other.requestName != null)
				return false;
		} else if (!requestName.equals(other.requestName))
			return false;
		if (requestOwner == null) {
			if (other.requestOwner != null)
				return false;
		} else if (!requestOwner.equals(other.requestOwner))
			return false;
		if (requestType != other.requestType)
			return false;
		if (rpxConfigOverride == null) {
			if (other.rpxConfigOverride != null)
				return false;
		} else if (!rpxConfigOverride.equals(other.rpxConfigOverride))
			return false;
		if (rpxTimeoutOverride == null) {
			if (other.rpxTimeoutOverride != null)
				return false;
		} else if (!rpxTimeoutOverride.equals(other.rpxTimeoutOverride))
			return false;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;
		if (sharedServicePriority == null) {
			if (other.sharedServicePriority != null)
				return false;
		} else if (!sharedServicePriority.equals(other.sharedServicePriority))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		} else if (!vendorId.equals(other.vendorId))
			return false;
		if (workflowType != other.workflowType)
			return false;
		return true;
	}
	
	@Transient
	public final boolean isDataCapRequest() {
		return REQUEST_OWNER_DATACAP.equalsIgnoreCase(requestOwner);
	}
	
	@Transient
	public final boolean isLargeDocketRequest() {
		return this.largeDocketRequest;
	}
}
