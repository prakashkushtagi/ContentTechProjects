/**
 * Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 */
@Entity
@Table(schema = "LITIGATOR_CONTENT", name = "CONTENT_VERSION")
public class ContentVersion 
{
	
    @Id
    @AttributeOverrides({
    	@AttributeOverride(name = "contentUuid", column =@Column(name = "CONTENT_UUID")),
    	@AttributeOverride(name = "versionId", column =@Column(name = "VERSION_ID"))
    })
    private ContentVersionPK primaryKey;
    
    
    @Column(name = "VERSION_ID", insertable = false, updatable = false)
    private Long versionId;
    
    @Column(name = "CONTENT_UUID", insertable = false, updatable = false)
    private String contentUuid;
    
	@Column( name = "CONTENT" )
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private byte[] fileimage;
	
	/**
	 */

	@Column(name = "CREATED_ON", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	Date createdOn;
	/**
	 */

	@Column(name = "CREATED_BY", length = 200, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	String createdBy;
	
	@Column(name = "PRODUCT_ID", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	Long productId;
	
	@Column(name = "COURT_ID", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	Long courtId;

	@Transient
	private long contentSize = 0;
	
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}
	
  @Override
    public boolean equals(Object other)
    {
        if ((this == other))
        {
            return true;
        }

        if ((other == null))
        {
            return false;
        }

        if (!(other instanceof ContentVersion))
        {
            return false;
        }

        ContentVersion otherContentVersion = (ContentVersion) other;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(this.primaryKey, otherContentVersion.getPrimaryKey());
        return equalsBuilder.isEquals();
    }

/**
 * @return the primaryKey
 */
public ContentVersionPK getPrimaryKey() {
	return primaryKey;
}

/**
 * @param primaryKey the primaryKey to set
 */
public void setPrimaryKey(ContentVersionPK primaryKey) {
	this.primaryKey = primaryKey;
}

/**
 * @return the fileimage
 */
public byte[] getFileimage() {
	return fileimage;
}

/**
 * @param fileimage the fileimage to set
 */
public void setFileimage(byte[] fileimage) {
	this.fileimage = fileimage;
}

/**
 * @return the createdOn
 */
public Date getCreatedOn() {
	return createdOn;
}

/**
 * @param createdOn the createdOn to set
 */
public void setCreatedOn(Date createdOn) {
	this.createdOn = createdOn;
}

/**
 * @return the createdBy
 */
public String getCreatedBy() {
	return createdBy;
}

/**
 * @param createdBy the createdBy to set
 */
public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}

/**
 * @return the contentSize
 */
@Transient
public long getContentSize() {
	return contentSize;
}

/**
 * @param contentSize the contentSize to set
 */
public void setContentSize(long contentSize) {
	this.contentSize = contentSize;
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
