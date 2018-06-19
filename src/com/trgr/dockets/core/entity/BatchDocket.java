/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Entity
@Table(name="BATCH_DOCKET", schema="DOCKETS_PUB")
public class BatchDocket {
	
	private BatchDocketKey primaryKey;
	private Batch batch;
	private String subBatchId;
	private Long auditKey;
	private UUID contentUuid;
	private String operationType;
	private CollectionEntity collection;
	private char largeDocketFlag;
	
	public BatchDocket() {
		super();
	}
	public BatchDocket(BatchDocketKey key, Batch batch, UUID contentUuid, String operationType) {
		setPrimaryKey(key);
		setBatch(batch);
		setContentUuid(contentUuid);
		setOperationType(operationType);
		setLargeDocketFlag('N');
	}
	
	public BatchDocket(BatchDocketKey key, Batch batch, UUID contentUuid,String operationType, CollectionEntity collection) {
		this(key, batch, contentUuid, operationType);
		setCollection(collection);
		setOperationType(operationType);
	}
	
	@Column(name="AUDIT_ID")
	public Long getAuditKey() {
		return auditKey;
	}
	
	@ManyToOne
	@JoinColumn(name="BATCH_ID")
	public Batch getBatch() {
		return batch;
	}
	
	@Column(name="OPERATION_TYPE", length=1)
	public String getOperationType() 
	{
		return operationType;
	}
	
	@Column(name="CONTENT_UUID")
	protected String getContentId() {
		return (contentUuid != null) ? contentUuid.toString() : null;
	}
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="legacyId", column=@Column(name="LEGACY_ID")),
			@AttributeOverride(name="publishingRequestId", column=@Column(name="REQUEST_ID"))
	})
	public BatchDocketKey getPrimaryKey() {
		return primaryKey;
	}
	@Transient
	public UUID getContentUuid() {
		return contentUuid;
	}
	
	@Column(name="SUB_BATCH_ID")
	public String getSubBatchId() {
		return subBatchId;
	}

	@OneToOne
	@JoinColumn(name="COLLECTION_ID")
	public CollectionEntity getCollection()
	{
		return collection;
	}
	
	@Column(name="LARGE_DOCKET_FLAG")
	public char getLargeDocketFlag() {
		return largeDocketFlag;
	}
	
	public void setCollection(CollectionEntity collection)
	{
		this.collection = collection;
	}
	public void setAuditKey(Long auditKey) {
		this.auditKey = auditKey;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	protected void setContentId(String uuid) throws UUIDException {
		setContentUuid((uuid != null) ? new UUID(uuid) : null);
	}
	public void setContentUuid(UUID uuid) {
		this.contentUuid = uuid;
	}
	public void setPrimaryKey(BatchDocketKey primaryKey) {
		Assert.notNull(primaryKey);
		this.primaryKey = primaryKey;
	}
	public void setSubBatchId(String subBatchId) {
		this.subBatchId = subBatchId;
	}
	public void setLargeDocketFlag(char largeDocketFlag) {
		this.largeDocketFlag = largeDocketFlag;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((auditKey == null) ? 0 : auditKey.hashCode());
		result = prime * result + ((batch == null) ? 0 : batch.hashCode());
		result = prime * result
				+ ((contentUuid == null) ? 0 : contentUuid.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
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
		BatchDocket other = (BatchDocket) obj;
		if (auditKey == null) {
			if (other.auditKey != null)
				return false;
		} else if (!auditKey.equals(other.auditKey))
			return false;
		if (batch == null) {
			if (other.batch != null)
				return false;
		} else if (!batch.equals(other.batch))
			return false;
		if (contentUuid == null) {
			if (other.contentUuid != null)
				return false;
		} else if (!contentUuid.equals(other.contentUuid))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}
	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
}
