/**
 * Copyright 2017: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and 
 * Confidential information of TRGR. Disclosure, Use or Reproduction without the 
 * written authorization of TRGR is prohibited.
 *
 */

package com.trgr.dockets.core.entity;

import java.io.File;
import java.util.Date;

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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Entity
@Table(name="DOCKET_VERSION", schema="DOCKETS_PUB")
public class DocketVersion 
{
	private DocketVersionKey primaryKey;
	private DocketEntity docket;
	private UUID contentUuid;
	private File sourceFile;
	private Long version;
	private Long contentSize;
//	SCRAPE_TIMESTAMP
	private Date scrapeTimestamp;
//	CONVERTED_TIMESTAMP
	private Date convertedTimestamp;
//	ACQUISITION_METHOD
	private AcquisitionMethod acquisitionMethod;
//	CASE_STATUS
	private String caseStatus;
//	OPERATION_TYPE
	private String operationType;
//	AUDIT_ID
	private Long auditId;
//	ACQUISITION_TYPE
	private RequestInitiatorTypeEnum requestInitiatorType;  // aka acquisitionType
//	ACQUISITION_TIMESTAMP
	private Date acquisitionTimestamp;
	private Long productId;
	private Court court;
	private Long acquisitionStatus;
	
	

	public DocketVersion() 
	{
		super();
	}
	public DocketVersion(DocketVersionKey key) 
	{
		setPrimaryKey(key);
	}
	
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="legacyId", column=@Column(name="LEGACY_ID")),
			@AttributeOverride(name="vendorKey", column=@Column(name="VENDOR_ID")),
			@AttributeOverride(name="phaseString", column=@Column(name="PHASE")),
			@AttributeOverride(name="versionDate", column=@Column(name="VERSION_TIMESTAMP"))
	})
	public DocketVersionKey getPrimaryKey() 
	{
		return primaryKey;
	}
	@Column(name="CONTENT_UUID", length=33, nullable = true)
	public String getContentId() 
	{
		return (contentUuid != null) ? contentUuid.toString() : null;
	}

	@Column(name="CONTENT_SIZE")
	public Long getContentSize() {
		return contentSize; 
	}
	@ManyToOne
	@JoinColumn(name="LEGACY_ID", insertable=false, updatable=false)
	@Basic(fetch = FetchType.LAZY)
	public DocketEntity getDocket() {
		return docket;
	}
	@Column(name="SOURCE_FILENAME", length=512, nullable = true)
	protected String getSourceFilename() {
		return (sourceFile != null) ? sourceFile.getName() : null; 
	}
	@Column(name="VERSION")
	public Long getVersion() {
		return version; 
	}
	@Transient
	public UUID getContentUuid() {
		return contentUuid;
	}
	@Transient
	public File getSourceFile() {
		return sourceFile;
	}
	
	@Column(name="SCRAPE_TIMESTAMP")
	public Date getScrapeTimestamp() 
	{
		return scrapeTimestamp;
	}
	@Column(name="CONVERTED_TIMESTAMP")
	public Date getConvertedTimestamp() 
	{
		return convertedTimestamp;
	}
	@Column(name="ACQUISITION_METHOD", length=20)
	protected String getAcquisitionMethodString() 
	{
		return (acquisitionMethod != null) ? acquisitionMethod.name() : null;
	}
	@Column(name="CASE_STATUS", length=100)
	public String getCaseStatus() 
	{
		return caseStatus;
	}
	@Column(name="OPERATION_TYPE", length=1)
	public String getOperationType() 
	{
		return operationType;
	}
	@Column(name="AUDIT_ID")
	public Long getAuditId() 
	{
		return auditId;
	}
	@Column(name="ACQUISITION_TYPE", length=20)
	protected String getAcquisitionType() {
		return (requestInitiatorType != null) ? requestInitiatorType.name() : null;
	}
	@Column(name="ACQUISITION_TIMESTAMP")
	public Date getAcquisitionTimestamp() 
	{
		return acquisitionTimestamp;
	}
	@Column(name="PRODUCT_ID")
	public Long getProductId(){
		return productId;
	}
	
	@ManyToOne
	@JoinColumn(name="COURT_ID")
	public Court getCourt()
	{
		return court;
	}

	
	public void setCourt(Court court)
	{
		this.court = court;
	}
	@Transient
	public AcquisitionMethod getAcquisitionMethod() {
		return acquisitionMethod;
	}
	
	@Transient
	public RequestInitiatorTypeEnum getRequestInitiatorType() {
		return requestInitiatorType;
	}

	@Column(name="ACQUISITION_STATUS")
	public Long getAcquisitionStatus() {
		return acquisitionStatus;
	}

	
	public void setContentId(String uuid) throws UUIDException 
	{
		setContentUuid(new UUID(uuid));
	}
	public void setContentSize(Long size) 
	{
		this.contentSize = size;
	}
	public void setContentUuid(UUID id) 
	{
		this.contentUuid = id;
	}
	public void setDocket(DocketEntity docket) 
	{
		this.docket = docket;
	}
	public void setPrimaryKey(DocketVersionKey primaryKey) 
	{
		Assert.notNull(primaryKey);
		this.primaryKey = primaryKey;
	}
	public void setSourceFile(File file) {
		this.sourceFile = file;
	}
	protected void setSourceFilename(String filename) {
		this.sourceFile = (filename != null) ? new File(filename) : null;
	}
	public void setVersion(Long ver) 
	{
		this.version = ver;
	}
	
	/**
	 * @param scrapeTimestamp the scrapeTimestamp to set
	 */
	public void setScrapeTimestamp(Date scrapeTimestamp) 
	{
		this.scrapeTimestamp = scrapeTimestamp;
	}
	/**
	 * @param convertedTimestamp the convertedTimestamp to set
	 */
	public void setConvertedTimestamp(Date convertedTimestamp) 
	{
		this.convertedTimestamp = convertedTimestamp;
	}
	public void setAcquisitionMethod(AcquisitionMethod method) {
		this.acquisitionMethod = method;
	}
	/**
	 * @param acquisitionMethod the acquisitionMethod to set
	 */
	protected void setAcquisitionMethodString(String acquisitionMethod) 
	{
		setAcquisitionMethod(AcquisitionMethod.valueOf(acquisitionMethod));
	}
	/**
	 * @param caseStatus the caseStatus to set
	 */
	public void setCaseStatus(String caseStatus) 
	{
		this.caseStatus = caseStatus;
	}
	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(String operationType) 
	{
		this.operationType = operationType;
	}
	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Long auditId) 
	{
		this.auditId = auditId;
	}
	/**
	 * @param acquisitionType the acquisitionType to set
	 */
	protected void setAcquisitionType(String acquisitionType) {
		setRequestInitiatorType((acquisitionType != null) ? RequestInitiatorTypeEnum.valueOf(acquisitionType) : null);
	}
	public void setRequestInitiatorType(RequestInitiatorTypeEnum acqType) {
		this.requestInitiatorType = acqType;
	}
	/**
	 * @param acquisitionTimestamp the acquisitionTimestamp to set
	 */
	public void setAcquisitionTimestamp(Date acquisitionTimestamp)
	{
		this.acquisitionTimestamp = acquisitionTimestamp;
	}
	
	public void setAcquisitionStatus(Long acquisitionStatus) {
		this.acquisitionStatus = acquisitionStatus;
	}

	@Override
	public String toString() 
	{
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) 
	{
		this.productId = productId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acquisitionMethod == null) ? 0 : acquisitionMethod.hashCode());
		result = prime * result + ((acquisitionStatus == null) ? 0 : acquisitionStatus.hashCode());
		result = prime * result + ((acquisitionTimestamp == null) ? 0 : acquisitionTimestamp.hashCode());
		result = prime * result + ((auditId == null) ? 0 : auditId.hashCode());
		result = prime * result + ((caseStatus == null) ? 0 : caseStatus.hashCode());
		result = prime * result + ((contentSize == null) ? 0 : contentSize.hashCode());
		result = prime * result + ((contentUuid == null) ? 0 : contentUuid.hashCode());
		result = prime * result + ((convertedTimestamp == null) ? 0 : convertedTimestamp.hashCode());
		result = prime * result + ((court == null) ? 0 : court.hashCode());
		result = prime * result + ((docket == null) ? 0 : docket.hashCode());
		result = prime * result + ((operationType == null) ? 0 : operationType.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((requestInitiatorType == null) ? 0 : requestInitiatorType.hashCode());
		result = prime * result + ((scrapeTimestamp == null) ? 0 : scrapeTimestamp.hashCode());
		result = prime * result + ((sourceFile == null) ? 0 : sourceFile.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		DocketVersion other = (DocketVersion) obj;
		if (acquisitionMethod != other.acquisitionMethod)
			return false;
		if (acquisitionStatus == null) {
			if (other.acquisitionStatus != null)
				return false;
		} else if (!acquisitionStatus.equals(other.acquisitionStatus))
			return false;
		if (acquisitionTimestamp == null) {
			if (other.acquisitionTimestamp != null)
				return false;
		} else if (!acquisitionTimestamp.equals(other.acquisitionTimestamp))
			return false;
		if (auditId == null) {
			if (other.auditId != null)
				return false;
		} else if (!auditId.equals(other.auditId))
			return false;
		if (caseStatus == null) {
			if (other.caseStatus != null)
				return false;
		} else if (!caseStatus.equals(other.caseStatus))
			return false;
		if (contentSize == null) {
			if (other.contentSize != null)
				return false;
		} else if (!contentSize.equals(other.contentSize))
			return false;
		if (contentUuid == null) {
			if (other.contentUuid != null)
				return false;
		} else if (!contentUuid.equals(other.contentUuid))
			return false;
		if (convertedTimestamp == null) {
			if (other.convertedTimestamp != null)
				return false;
		} else if (!convertedTimestamp.equals(other.convertedTimestamp))
			return false;
		if (court == null) {
			if (other.court != null)
				return false;
		} else if (!court.equals(other.court))
			return false;
		if (docket == null) {
			if (other.docket != null)
				return false;
		} else if (!docket.equals(other.docket))
			return false;
		if (operationType == null) {
			if (other.operationType != null)
				return false;
		} else if (!operationType.equals(other.operationType))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (requestInitiatorType != other.requestInitiatorType)
			return false;
		if (scrapeTimestamp == null) {
			if (other.scrapeTimestamp != null)
				return false;
		} else if (!scrapeTimestamp.equals(other.scrapeTimestamp))
			return false;
		if (sourceFile == null) {
			if (other.sourceFile != null)
				return false;
		} else if (!sourceFile.equals(other.sourceFile))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	
	
}
