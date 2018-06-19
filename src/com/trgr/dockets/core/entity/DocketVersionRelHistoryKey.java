package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Primary key for the DOCKET_VERSION_REL_HISTORY table.
 */
@Embeddable
public class DocketVersionRelHistoryKey implements Serializable {


	private static final long serialVersionUID = 7510502226960038329L;
	private String relId;
	private String docketHistoryId;
	
	public DocketVersionRelHistoryKey() {
		super();
	}
	public DocketVersionRelHistoryKey(String docketHistoryId, String relId) {
		this.docketHistoryId = docketHistoryId;
		this.relId = relId;
	}
	public String getDocketHistoryId() {
		return this.docketHistoryId; 
	}
	public String getRelId() {
		return this.relId;
	}
	protected void setDocketHistoryId(String docketHistoryId) {
		this.docketHistoryId = docketHistoryId;
	}
	protected void setRelId(String relId) {
		this.relId = relId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((docketHistoryId == null) ? 0 : docketHistoryId.hashCode());
		result = prime * result + ((relId == null) ? 0 : relId.hashCode());
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
		DocketVersionRelHistoryKey other = (DocketVersionRelHistoryKey) obj;
		if (docketHistoryId == null) {
			if (other.docketHistoryId != null)
				return false;
		} else if (!docketHistoryId.equals(other.docketHistoryId))
			return false;
		if (relId == null) {
			if (other.relId != null)
				return false;
		} else if (!relId.equals(other.relId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
