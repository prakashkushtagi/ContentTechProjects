package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

@Entity
@Table(name="BATCH", schema="DOCKETS_PUB")
public class Batch {

	private String primaryKey;
	private Batch parent;
	private PublishingRequest publishingRequest;
	private Long auditKey;
	
	public Batch() {
		super();
	}
	public Batch(String pk) {
		setPrimaryKey(pk);
	}

	public Batch(String primaryKey, Batch parent,
			PublishingRequest publishingRequest) {
		this.primaryKey = primaryKey;
		this.parent = parent;
		this.publishingRequest = publishingRequest;
		this.auditKey = null;
	}

	@Column(name="AUDIT_ID")
	public Long getAuditKey() {
		return auditKey;
	}
	@OneToOne
	@JoinColumn(name="PARENT_BATCH_ID")
	public Batch getParentBatch() {
		return parent;
	}
	@Id
	@Column(name="BATCH_ID")
	public String getPrimaryKey() {
		return primaryKey;
	}
	@OneToOne
	@JoinColumn(name="REQUEST_ID")
	public PublishingRequest getPublishingRequest() {
		return publishingRequest;
	}
	
	public void setAuditKey(Long key) {
		this.auditKey = key;
	}
	public void setParentBatch(Batch parent) {
		this.parent = parent;
	}
	public void setPrimaryKey(String primaryKey) {
		Assert.notNull(primaryKey);
		this.primaryKey = primaryKey;
	}
	public void setPublishingRequest(PublishingRequest req) {
		this.publishingRequest = req;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((auditKey == null) ? 0 : auditKey.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime
				* result
				+ ((publishingRequest == null) ? 0 : publishingRequest
						.hashCode());
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
		Batch other = (Batch) obj;
		if (auditKey == null) {
			if (other.auditKey != null)
				return false;
		} else if (!auditKey.equals(other.auditKey))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (publishingRequest == null) {
			if (other.publishingRequest != null)
				return false;
		} else if (!publishingRequest.equals(other.publishingRequest))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}

