package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Id;

import javax.persistence.*;

/**
 */
public class DctTablePK implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */
	public DctTablePK() {
	}

	/**
	 */

	@Column(name = "COUNTY_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String countyCode;
	/**
	 */

	@Column(name = "DCT_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String dctCode;

	/**
	 */
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	/**
	 */
	public String getCountyCode() {
		return this.countyCode;
	}

	/**
	 */
	public void setDctCode(String dctCode) {
		this.dctCode = dctCode;
	}

	/**
	 */
	public String getDctCode() {
		return this.dctCode;
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((countyCode == null) ? 0 : countyCode.hashCode()));
		result = (int) (prime * result + ((dctCode == null) ? 0 : dctCode.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof DctTablePK))
			return false;
		DctTablePK equalCheck = (DctTablePK) obj;
		if ((countyCode == null && equalCheck.countyCode != null) || (countyCode != null && equalCheck.countyCode == null))
			return false;
		if (countyCode != null && !countyCode.equals(equalCheck.countyCode))
			return false;
		if ((dctCode == null && equalCheck.dctCode != null) || (dctCode != null && equalCheck.dctCode == null))
			return false;
		if (dctCode != null && !dctCode.equals(equalCheck.dctCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("DctTablePK");
		sb.append(" countyCode: ").append(getCountyCode());
		sb.append(" dctCode: ").append(getDctCode());
		return sb.toString();
	}
}
