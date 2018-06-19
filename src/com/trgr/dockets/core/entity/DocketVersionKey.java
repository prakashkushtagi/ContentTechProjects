package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.CoreConstants.Phase;

/**
 * The compound primary key for class DocketVersion, as backed by the DOCKET_VERSION table. 
 */
@Embeddable
public class DocketVersionKey implements Serializable {

	private static final long serialVersionUID = -1554635882202408464L;

	private String legacyId;
	private Long vendorKey;
	private Phase phase;
	private Date versionDate;
	
	public DocketVersionKey() {
		super();
	}
	public DocketVersionKey(String legacyId, Long vendorKey, Phase phase,
							Date versionDate) {
		this.legacyId = legacyId;
		this.vendorKey = vendorKey;
		this.phase = phase;
		this.versionDate = versionDate;
	}

	public String getLegacyId() {
		return legacyId;
	}
	public Long getVendorKey() {
		return vendorKey;
	}
	@Transient
	public Phase getPhase() 
	{
		return phase;
	}
	public String getPhaseString() {
		return (phase != null) ? phase.toString() : null;
	}
	public Date getVersionDate() {
		return versionDate;
	}

	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}
	public void setVendorKey(Long vendorKey) {
		this.vendorKey = vendorKey;
	}
	public void setPhase(Phase phase) {
		this.phase = phase;
	}
	/** Not publicly exposed, but needed for Hibernate mapping. */
	@SuppressWarnings("unused")
	private void setPhaseString(String phase) {
		setPhase(Phase.valueOf(phase));
	}
	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((legacyId == null) ? 0 : legacyId.hashCode());
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		result = prime * result
				+ ((vendorKey == null) ? 0 : vendorKey.hashCode());
		result = prime * result
				+ ((versionDate == null) ? 0 : versionDate.hashCode());
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
		DocketVersionKey other = (DocketVersionKey) obj;
		if (legacyId == null) {
			if (other.legacyId != null)
				return false;
		} else if (!legacyId.equals(other.legacyId))
			return false;
		if (phase != other.phase)
			return false;
		if (vendorKey == null) {
			if (other.vendorKey != null)
				return false;
		} else if (!vendorKey.equals(other.vendorKey))
			return false;
		if (versionDate == null) {
			if (other.versionDate != null)
				return false;
		} else if (!versionDate.equals(other.versionDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
