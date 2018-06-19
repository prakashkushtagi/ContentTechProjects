package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Id;

import javax.persistence.*;

/**
 */
public class AdvocatePK implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */
	public AdvocatePK() {
	}

	/**
	 */

	@Column(name = "COUNTY_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public Integer countyId;
	/**
	 */

	@Column(name = "ADVOCATE_TYPE_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public Integer type;
	/**
	 */

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String id;

	/**
	 */
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	/**
	 */
	public Integer getCountyId() {
		return this.countyId;
	}

	/**
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 */
	public String getId() {
		return this.id;
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((countyId == null) ? 0 : countyId.hashCode()));
		result = (int) (prime * result + ((type == null) ? 0 : type.hashCode()));
		result = (int) (prime * result + ((id == null) ? 0 : id.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AdvocatePK))
			return false;
		AdvocatePK equalCheck = (AdvocatePK) obj;
		if ((countyId == null && equalCheck.countyId != null) || (countyId != null && equalCheck.countyId == null))
			return false;
		if (countyId != null && !countyId.equals(equalCheck.countyId))
			return false;
		if ((type == null && equalCheck.type != null) || (type != null && equalCheck.type == null))
			return false;
		if (type != null && !type.equals(equalCheck.type))
			return false;
		if ((id == null && equalCheck.id != null) || (id != null && equalCheck.id == null))
			return false;
		if (id != null && !id.equals(equalCheck.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("AdvocatePK");
		sb.append(" countyId: ").append(getCountyId());
		sb.append(" type: ").append(getType());
		sb.append(" id: ").append(getId());
		return sb.toString();
	}
}
