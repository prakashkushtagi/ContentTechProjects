package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Embeddable
public class StgPubUiRequestKey  implements Serializable {

	private static final long serialVersionUID = -2586006073749549205L;
	private UUID publishingRequestUuid;
	private String legacyId;
	
	public StgPubUiRequestKey() {
		super();
	}
	public StgPubUiRequestKey(UUID pubRequestUuid, String legacyId) {
		setPublishingRequestUuid(pubRequestUuid);
		setLegacyId(legacyId);
	}
	public String getPublishingRequestId() {
		return (publishingRequestUuid != null) ? publishingRequestUuid.toString() : null;
	}
	@Transient
	public UUID getPublishingRequestUuid() {
		return publishingRequestUuid;
	}
	public String getLegacyId() {
		return legacyId;
	}

	public void setPublishingRequestId(String pubRequestId) throws UUIDException {
		setPublishingRequestUuid((pubRequestId != null) ? new UUID(pubRequestId) : null);
	}
	public void setPublishingRequestUuid(UUID pubRequestId) {
		this.publishingRequestUuid = pubRequestId;
	}
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
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
		StgPubUiRequestKey other = (StgPubUiRequestKey) obj;
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

