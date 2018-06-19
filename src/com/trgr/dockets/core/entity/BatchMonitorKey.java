package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

/**
 * The compound primary key for class BatchMonitor, as backed by the BATCH_MONITOR table. 
 */
@Embeddable
public class BatchMonitorKey implements Serializable {
	
	private static final long serialVersionUID = -9179311013172998673L;
	
	private UUID publishingRequestUuid;	// foreign key to PublishingRequest
	private String batchId;
	
	public BatchMonitorKey() {
		super();
	}

	public BatchMonitorKey(UUID pubRequestId, String batchId) {
		setPublishingRequestUuid(pubRequestId);
		setBatchId(batchId);
	}
	
	public String getBatchId() {
		return batchId;
	}
	public String getPublishingRequestId() {
		return (publishingRequestUuid != null) ? publishingRequestUuid.toString() : null;
	}
	@Transient
	public UUID getPublishingRequestUuid() {
		return publishingRequestUuid;
	}

	public void setBatchId(String id) {
		this.batchId = id;
	}
	public void setPublishingRequestId(String pubRequestId) throws UUIDException {
		setPublishingRequestUuid(new UUID(pubRequestId));
	}
	public void setPublishingRequestUuid(UUID pubRequestId) {
		this.publishingRequestUuid = pubRequestId;
	}	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime
				* result
				+ ((publishingRequestUuid == null) ? 0 : publishingRequestUuid
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
		BatchMonitorKey other = (BatchMonitorKey) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (publishingRequestUuid == null) {
			if (other.publishingRequestUuid != null)
				return false;
		} else if (!publishingRequestUuid.equals(other.publishingRequestUuid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
