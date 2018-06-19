/**
 * Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 */
@Entity
@Table(schema = "CONTENT", name = "CONTENT")
public class Content 
{
	
	public static final String CONTENT_UUID = "contentUuid";
	
	@Id
	@Column(name = "CONTENT_UUID", length = 33, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	String contentUuid;
	/**
	 */

	@Column(name = "FORMAT_TYPE_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	Long formatTypeId;
	/**
	 */

	@Column(name = "CREATED_ON", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	Date createdOn;
	/**
	 */

	@Column(name = "CREATED_BY", length = 200, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	String createdBy;

	@Column(name = "ARTIFACT_PHASE_ID", nullable = true)
	@Basic(fetch=FetchType.EAGER)
	Long artifactPhaseId;
	
	@Column(name = "ARTIFACT_DESCRIPTOR_ID", nullable = false)
	@Basic(fetch=FetchType.EAGER)
	Long artifactDescriptorId;
	
	@Column(name = "ARTIFACT_UUID", length=33, nullable = true)
	@Basic(fetch=FetchType.EAGER)
	String artifactUuid;
	
	@Column(name = "DB_PARTITION", length=50, nullable = false)
	@Basic(fetch=FetchType.EAGER)
	String dbPartition;
	
	@Column(name = "TERMINATED_FLAG", length=1, nullable = true)
	@Basic(fetch=FetchType.EAGER)
	String terminatedFlag;

	@Column(name = "PRODUCT_ID", nullable = true)
	@Basic(fetch=FetchType.EAGER)
	Long productId;
	
	@Column(name = "COURT_ID", nullable = true)
	@Basic(fetch=FetchType.EAGER)
	Long courtId;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentUuid == null) ? 0 : contentUuid.hashCode());
		result = prime * result + ((formatTypeId == null) ? 0 : formatTypeId.hashCode());
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
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
		Content other = (Content) obj;
		if (contentUuid == null) {
			if (other.contentUuid != null)
				return false;
		} else if (!contentUuid.equals(other.contentUuid))
			return false;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (formatTypeId == null) {
			if (other.formatTypeId != null)
				return false;
		} else if (!formatTypeId.equals(other.formatTypeId))
			return false;
		return true;
	}
	/**
	 * @return the contentUuid
	 */
	public String getContentUuid() 
	{
		return contentUuid;
	}
	/**
	 * @param contentUuid the contentUuid to set
	 */
	public void setContentUuid(String contentUuid) 
	{
		this.contentUuid = contentUuid;
	}
	/**
	 * @return the formatTypeId
	 */
	public Long getFormatTypeId() 
	{
		return formatTypeId;
	}
	/**
	 * @param formatTypeId the formatTypeId to set
	 */
	public void setFormatTypeId(Long formatTypeId) 
	{
		this.formatTypeId = formatTypeId;
	}
	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() 
	{
		return createdOn;
	}
	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) 
	{
		this.createdOn = createdOn;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() 
	{
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) 
	{
		this.createdBy = createdBy;
	}
	/**
	 * @return the artifactPhaseId
	 */
	public Long getArtifactPhaseId() 
	{
		return artifactPhaseId;
	}
	/**
	 * @param artifactPhaseId the artifactPhaseId to set
	 */
	public void setArtifactPhaseId(Long artifactPhaseId) 
	{
		this.artifactPhaseId = artifactPhaseId;
	}
	/**
	 * @return the artifactDescriptorId
	 */
	public Long getArtifactDescriptorId() 
	{
		return artifactDescriptorId;
	}
	/**
	 * @param artifactDescriptorId the artifactDescriptorId to set
	 */
	public void setArtifactDescriptorId(Long artifactDescriptorId)
	{
		this.artifactDescriptorId = artifactDescriptorId;
	}
	/**
	 * @return the artifactUuid
	 */
	public String getArtifactUuid() 
	{
		return artifactUuid;
	}
	/**
	 * @param artifactUuid the artifactUuid to set
	 */
	public void setArtifactUuid(String artifactUuid) {
		this.artifactUuid = artifactUuid;
	}
	/**
	 * @return the dbPartition
	 */
	public String getDbPartition() {
		return dbPartition;
	}
	/**
	 * @param dbPartition the dbPartition to set
	 */
	public void setDbPartition(String dbPartition) {
		this.dbPartition = dbPartition;
	}
	/**
	 * @return the terminatedFlag
	 */
	public String getTerminatedFlag() {
		return terminatedFlag;
	}
	/**
	 * @param terminatedFlag the terminatedFlag to set
	 */
	public void setTerminatedFlag(String terminatedFlag) {
		this.terminatedFlag = terminatedFlag;
	}
	/**
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * @return the courtId
	 */
	public Long getCourtId() {
		return courtId;
	}
	/**
	 * @param courtId the courtId to set
	 */
	public void setCourtId(Long courtId) {
		this.courtId = courtId;
	}
	
}
