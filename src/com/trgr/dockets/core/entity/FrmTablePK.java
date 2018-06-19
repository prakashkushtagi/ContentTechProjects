package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Id;

import javax.persistence.*;

/**
 */
public class FrmTablePK implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */
	public FrmTablePK() {
	}

	/**
	 */

	@Column(name = "COUNTY_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String countyCode;
	/**
	 */

	@Column(name = "ATTORNEY_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String attorneyCode;

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
	public void setAttorneyCode(String attorneyCode) {
		this.attorneyCode = attorneyCode;
	}

	/**
	 */
	public String getAttorneyCode() {
		return this.attorneyCode;
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((countyCode == null) ? 0 : countyCode.hashCode()));
		result = (int) (prime * result + ((attorneyCode == null) ? 0 : attorneyCode.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof FrmTablePK))
			return false;
		FrmTablePK equalCheck = (FrmTablePK) obj;
		if ((countyCode == null && equalCheck.countyCode != null) || (countyCode != null && equalCheck.countyCode == null))
			return false;
		if (countyCode != null && !countyCode.equals(equalCheck.countyCode))
			return false;
		if ((attorneyCode == null && equalCheck.attorneyCode != null) || (attorneyCode != null && equalCheck.attorneyCode == null))
			return false;
		if (attorneyCode != null && !attorneyCode.equals(equalCheck.attorneyCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("FrmTablePK");
		sb.append(" countyCode: ").append(getCountyCode());
		sb.append(" attorneyCode: ").append(getAttorneyCode());
		return sb.toString();
	}
}
