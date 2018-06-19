package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Id;

import javax.persistence.*;

/**
 */
public class DictionaryPK implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */
	public DictionaryPK() {
	}

	/**
	 */

	@Column(name = "COUNTY_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public Integer countyId;
	/**
	 */

	@Column(name = "DICTIONARY_TYPE_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public Integer type;
	/**
	 */

	@Column(name = "KEY", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String key;

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
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((countyId == null) ? 0 : countyId.hashCode()));
		result = (int) (prime * result + ((type == null) ? 0 : type.hashCode()));
		result = (int) (prime * result + ((key == null) ? 0 : key.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof DictionaryPK))
			return false;
		DictionaryPK equalCheck = (DictionaryPK) obj;
		if ((countyId == null && equalCheck.countyId != null) || (countyId != null && equalCheck.countyId == null))
			return false;
		if (countyId != null && !countyId.equals(equalCheck.countyId))
			return false;
		if ((type == null && equalCheck.type != null) || (type != null && equalCheck.type == null))
			return false;
		if (type != null && !type.equals(equalCheck.type))
			return false;
		if ((key == null && equalCheck.key != null) || (key != null && equalCheck.key == null))
			return false;
		if (key != null && !key.equals(equalCheck.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("DictionaryPK");
		sb.append(" countyId: ").append(getCountyId());
		sb.append(" type: ").append(getType());
		sb.append(" key: ").append(getKey());
		return sb.toString();
	}
}
