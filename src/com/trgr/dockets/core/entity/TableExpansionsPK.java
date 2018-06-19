package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Id;

import javax.persistence.*;

/**
 */
public class TableExpansionsPK implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */
	public TableExpansionsPK() {
	}

	/**
	 */

	@Column(name = "COURT_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public Integer courtId;
	/**
	 */

	@Column(name = "RECORD_TYPE", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String recordType;
	/**
	 */

	@Column(name = "COLUMN_NAME", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String columnName;
	/**
	 */

	@Column(name = "KEY", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	public String key;

	/**
	 */
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	/**
	 */
	public Integer getCourtId() {
		return this.courtId;
	}

	/**
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	/**
	 */
	public String getRecordType() {
		return this.recordType;
	}

	/**
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 */
	public String getColumnName() {
		return this.columnName;
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
		result = (int) (prime * result + ((courtId == null) ? 0 : courtId.hashCode()));
		result = (int) (prime * result + ((recordType == null) ? 0 : recordType.hashCode()));
		result = (int) (prime * result + ((columnName == null) ? 0 : columnName.hashCode()));
		result = (int) (prime * result + ((key == null) ? 0 : key.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof TableExpansionsPK))
			return false;
		TableExpansionsPK equalCheck = (TableExpansionsPK) obj;
		if ((courtId == null && equalCheck.courtId != null) || (courtId != null && equalCheck.courtId == null))
			return false;
		if (courtId != null && !courtId.equals(equalCheck.courtId))
			return false;
		if ((recordType == null && equalCheck.recordType != null) || (recordType != null && equalCheck.recordType == null))
			return false;
		if (recordType != null && !recordType.equals(equalCheck.recordType))
			return false;
		if ((columnName == null && equalCheck.columnName != null) || (columnName != null && equalCheck.columnName == null))
			return false;
		if (columnName != null && !columnName.equals(equalCheck.columnName))
			return false;
		if ((key == null && equalCheck.key != null) || (key != null && equalCheck.key == null))
			return false;
		if (key != null && !key.equals(equalCheck.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TableExpansionsPK");
		sb.append(" courtId: ").append(getCourtId());
		sb.append(" recordType: ").append(getRecordType());
		sb.append(" columnName: ").append(getColumnName());
		sb.append(" key: ").append(getKey());
		return sb.toString();
	}
}
