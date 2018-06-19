package com.trgr.dockets.core.entity;

import java.io.Serializable;

import java.lang.StringBuilder;

import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.xml.bind.annotation.*;

import javax.persistence.*;

/**
 */
@IdClass(com.trgr.dockets.core.entity.FrmTablePK.class)
@Entity
@NamedQueries({
		@NamedQuery(name = "findAllFrmTables", query = "select myFrmTable from FrmTable myFrmTable"),
		@NamedQuery(name = "findFrmTableByAttorneyCode", query = "select myFrmTable from FrmTable myFrmTable where myFrmTable.attorneyCode = :attorneyCode"),
		@NamedQuery(name = "findFrmTableByCountyCode", query = "select myFrmTable from FrmTable myFrmTable where myFrmTable.countyCode = :countyCode"),
		@NamedQuery(name = "findFrmTableByFrmTableText", query = "select myFrmTable from FrmTable myFrmTable where myFrmTable.frmTableText = :frmTableText"),
		@NamedQuery(name = "findFrmTableByPrimaryKey", query = "select myFrmTable from FrmTable myFrmTable where myFrmTable.countyCode = :countyCode and myFrmTable.attorneyCode = :attorneyCode"),
		@NamedQuery(name = "findFrmTableByWestlawClusterName", query = "select myFrmTable from FrmTable myFrmTable where myFrmTable.westlawClusterName = :westlawClusterName") })
@Table(schema = "DOCKETS_PREPROCESSOR", name = "FRM_TABLE")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "StateDocketPreProcessor/com/trgr/dockets/core/entity", name = "FrmTable")
public class FrmTable implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */

	@Column(name = "COUNTY_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String countyCode;
	/**
	 */

	@Column(name = "ATTORNEY_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String attorneyCode;
	/**
	 */

	@Column(name = "WESTLAW_CLUSTER_NAME", length = 30, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String westlawClusterName;
	/**
	 */

	@Column(name = "FRM_TABLE_TEXT", length = 4000, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String frmTableText;

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
	public void setWestlawClusterName(String westlawClusterName) {
		this.westlawClusterName = westlawClusterName;
	}

	/**
	 */
	public String getWestlawClusterName() {
		return this.westlawClusterName;
	}

	/**
	 */
	public void setFrmTableText(String frmTableText) {
		this.frmTableText = frmTableText;
	}

	/**
	 */
	public String getFrmTableText() {
		return this.frmTableText;
	}

	/**
	 */
	public FrmTable() {
	}

	/**
	 * Copies the contents of the specified bean into this bean.
	 *
	 */
	public void copy(FrmTable that) {
		setCountyCode(that.getCountyCode());
		setAttorneyCode(that.getAttorneyCode());
		setWestlawClusterName(that.getWestlawClusterName());
		setFrmTableText(that.getFrmTableText());
	}

	/**
	 * Returns a textual representation of a bean.
	 *
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("countyCode=[").append(countyCode).append("] ");
		buffer.append("attorneyCode=[").append(attorneyCode).append("] ");
		buffer.append("westlawClusterName=[").append(westlawClusterName).append("] ");
		buffer.append("frmTableText=[").append(frmTableText).append("] ");

		return buffer.toString();
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
		if (!(obj instanceof FrmTable))
			return false;
		FrmTable equalCheck = (FrmTable) obj;
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
}
