package com.trgr.dockets.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="DOCKET_VERSION_REL_HISTORY", schema="DOCKETS_PUB")
public class DocketVersionRelHistory {
	
	private DocketVersionRelHistoryKey primaryKey;
	private String legacyId;
	
	public DocketVersionRelHistory() {
		super();
	}
	public DocketVersionRelHistory(DocketVersionRelHistoryKey primaryKey, String legacyId) {
		setPrimaryKey(primaryKey);
		this.legacyId = legacyId;
	}
	
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="relId", column=@Column(name="REL_ID")),
			@AttributeOverride(name="docketHistoryId", column=@Column(name="DOCKET_HISTORY_ID"))
	})
	public DocketVersionRelHistoryKey getPrimaryKey() {
		return primaryKey;
	}
	@Column(name="LEGACY_ID", length=30, nullable = true)
	public String getLegacyId() {
		return this.legacyId; 
	}
	protected void setPrimaryKey(DocketVersionRelHistoryKey pk) {
		this.primaryKey = pk;
	}
	protected void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
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
				+ ((legacyId == null) ? 0 : legacyId.hashCode());
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
		DocketVersionRelHistory other = (DocketVersionRelHistory) obj;
		if (legacyId == null) {
			if (other.legacyId != null)
				return false;
		} else if (!legacyId.equals(other.legacyId))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}
}
