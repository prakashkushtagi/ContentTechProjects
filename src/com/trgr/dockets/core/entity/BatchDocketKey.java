package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

/**
 * The compound primary key for class BatchDocket, as backed by the BATCH_DOCKET table. 
 */
@Embeddable
public class BatchDocketKey implements Serializable {
	
	private static final long serialVersionUID = 6407236770619363056L;
	
	private String legacyId;
	private UUID publishingRequestUuid;	// foreign key to PublishingRequest
	
	public BatchDocketKey() {
		super();
	}
	
	public BatchDocketKey(String legacyId, UUID pubRequestId) {
		setLegacyId(legacyId);
		setPublishingRequestUuid(pubRequestId);
	}
	
	public String getLegacyId() {
		return legacyId;
	}
	public String getPublishingRequestId() {
		return (publishingRequestUuid != null) ? publishingRequestUuid.toString() : null;
	}
	@Transient
	public UUID getPublishingRequestUuid() {
		return publishingRequestUuid;
	}

	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}
	public void setPublishingRequestId(String requestId) throws UUIDException {
		setPublishingRequestUuid((requestId != null) ? new UUID(requestId) : null);
	}	
	public void setPublishingRequestUuid(UUID requestId) {
		this.publishingRequestUuid = requestId;
	}	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((legacyId == null) ? 0 : legacyId.hashCode());
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
		BatchDocketKey other = (BatchDocketKey) obj;
		if (legacyId == null) {
			if (other.legacyId != null)
				return false;
		} else if (!legacyId.equals(other.legacyId))
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
