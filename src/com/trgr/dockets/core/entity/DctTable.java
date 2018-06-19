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
@IdClass(com.trgr.dockets.core.entity.DctTablePK.class)
@Entity
@NamedQueries({
		@NamedQuery(name = "findAllDctTables", query = "select myDctTable from DctTable myDctTable"),
		@NamedQuery(name = "findDctTableByCountyCode", query = "select myDctTable from DctTable myDctTable where myDctTable.countyCode = :countyCode"),
		@NamedQuery(name = "findDctTableByDctCode", query = "select myDctTable from DctTable myDctTable where myDctTable.dctCode = :dctCode"),
		@NamedQuery(name = "findDctTableByPrimaryKey", query = "select myDctTable from DctTable myDctTable where myDctTable.countyCode = :countyCode and myDctTable.dctCode = :dctCode"),
		@NamedQuery(name = "findDctTableByWestlawClusterName", query = "select myDctTable from DctTable myDctTable where myDctTable.westlawClusterName = :westlawClusterName") })
@Table(schema = "DOCKETS_PREPROCESSOR", name = "DCT_TABLE")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "DocketsCore/com/trgr/dockets/core/entity", name = "DctTable")
public class DctTable implements Serializable {
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

	@Column(name = "DCT_CODE", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String dctCode;
	/**
	 */

	@Column(name = "WESTLAW_CLUSTER_NAME", length = 30, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String westlawClusterName;
	/**
	 */

	@Column(name = "DCT_TABLE_TEXT", length = 4000)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String dctTableText;

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
	public void setDctTableText(String dctTableText) {
		this.dctTableText = dctTableText;
	}

	/**
	 */
	public String getDctTableText() {
		return this.dctTableText;
	}

	/**
	 */
	public DctTable() {
	}

	/**
	 * Copies the contents of the specified bean into this bean.
	 *
	 */
	public void copy(DctTable that) {
		setCountyCode(that.getCountyCode());
		setDctCode(that.getDctCode());
		setWestlawClusterName(that.getWestlawClusterName());
		setDctTableText(that.getDctTableText());
	}

	/**
	 * Returns a textual representation of a bean.
	 *
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("countyCode=[").append(countyCode).append("] ");
		buffer.append("dctCode=[").append(dctCode).append("] ");
		buffer.append("westlawClusterName=[").append(westlawClusterName).append("] ");
		buffer.append("dctTableText=[").append(dctTableText).append("] ");

		return buffer.toString();
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
		if (!(obj instanceof DctTable))
			return false;
		DctTable equalCheck = (DctTable) obj;
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
}
