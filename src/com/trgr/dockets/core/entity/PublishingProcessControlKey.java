package com.trgr.dockets.core.entity;

import static com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum.getDPBKey;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Primary key for the PublishingProcessControl entity.
 */
@Embeddable
public class PublishingProcessControlKey implements Serializable {

	private static final long serialVersionUID = 5674851337871628765L;
	private long publishingControlId;
	private long processTypeId;

	public PublishingProcessControlKey() {
		super();
	}
	public PublishingProcessControlKey(long publishingControlId, long processId) {
		this.publishingControlId = publishingControlId;
		this.processTypeId = processId;
	}
	
	public static PublishingProcessControlKey newPublishingProcessControlKeyPb(long publishingControlId) {
		return new PublishingProcessControlKey(publishingControlId, getDPBKey());
	}
	
	public long getPublishingControlId() {
		return publishingControlId;
	}
	public long getProcessTypeId() {
		return processTypeId;
	}

	public void setPublishingControlId(long publishingControlId) {
		this.publishingControlId = publishingControlId;
	}
	public void setProcessTypeId(long processTypeId) {
		this.processTypeId = processTypeId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (processTypeId ^ (processTypeId >>> 32));
		result = prime * result
				+ (int) (publishingControlId ^ (publishingControlId >>> 32));
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
		PublishingProcessControlKey other = (PublishingProcessControlKey) obj;
		if (processTypeId != other.processTypeId)
			return false;
		if (publishingControlId != other.publishingControlId)
			return false;
		return true;
	}
}
