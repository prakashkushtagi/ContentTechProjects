/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import java.lang.StringBuilder;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.xml.bind.annotation.*;

import javax.persistence.*;

/**
 */

@Entity
@NamedQueries({
		@NamedQuery(name="findCountyByCourtId",query = "select myCounty from County myCounty where myCounty.courtId = :courtId"),
		@NamedQuery(name = "findCountyByCourtIdAndCountyCode", query = "select myCounty from County myCounty where myCounty.courtId = :courtId and myCounty.countyCode = :countyCode"),
		@NamedQuery(name = "findCountyByPrimaryKey", query = "select myCounty from County myCounty where myCounty.countyId = :countyId") })
@Table(schema = "DOCKETS_PUB", name = "COUNTY")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "DocketsCore/com/trgr/dockets/core/entity", name = "County")
public class County implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */

	@Column(name = "COUNTY_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	Integer countyId;
	/**
	 */

	@Column(name = "COUNTY_CODE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Integer countyCode;
	/**
	 */

	@Column(name = "COURT_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Integer courtId;
	/**
	 */

	@Column(name = "DISTRICT_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Integer districtId;
	/**
	 */

	@Column(name = "NAME", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String name;
	/**
	 */

	@Column(name = "ABBREVIATION", length = 4)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String abbreviation;
	/**
	 */

	@Column(name = "COL_KEY", length = 64)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String colKey;
	/**
	 */

	@Column(name = "COURT_NORM", length = 64)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String courtNorm;
	/**
	 */

	@Column(name = "LU_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Timestamp luDate;

	@Column(name = "LOWER_JURISNUM", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Integer lowerJurisNum;
	
	@Column(name = "PUBLICATION_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Long publicationId;
	
	@Column(name = "IS_PREDOCKET")
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String isPredocket;
	
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
	public void setCountyCode(Integer countyCode) {
		this.countyCode = countyCode;
	}

	/**
	 */
	public Integer getCountyCode() {
		return this.countyCode;
	}

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
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	/**
	 */
	public Integer getDistrictId() {
		return this.districtId;
	}

	/**
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 */
	public String getName() {
		return this.name;
	}

	/**
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	/**
	 */
	public String getAbbreviation() {
		return this.abbreviation;
	}

	/**
	 */
	public void setColKey(String colKey) {
		this.colKey = colKey;
	}

	/**
	 */
	public String getColKey() {
		return this.colKey;
	}

	/**
	 */
	public void setCourtNorm(String courtNorm) {
		this.courtNorm = courtNorm;
	}

	/**
	 */
	public String getCourtNorm() {
		return this.courtNorm;
	}

	/**
	 */
	public void setLuDate(Timestamp luDate) {
		this.luDate = luDate;
	}

	/**
	 */
	public Timestamp getLuDate() {
		return this.luDate;
	}

	public Integer getLowerJurisNum() {
		return lowerJurisNum;
	}

	public void setLowerJurisNum(Integer lowerJurisNum) {
		this.lowerJurisNum = lowerJurisNum;
	}

	public Long getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
	}
	
	public String getIsPredocket() {
		return isPredocket;
	}
	
	public void setIsPredocket(String isPredocket) {
		this.isPredocket = isPredocket;
	}

	/**
	 */
	public County() {
	}

	/**
	 * Copies the contents of the specified bean into this bean.
	 *
	 */
	public void copy(County that) {
		setCountyId(that.getCountyId());
		setCountyCode(that.getCountyCode());
		setCourtId(that.getCourtId());
		setDistrictId(that.getDistrictId());
		setName(that.getName());
		setAbbreviation(that.getAbbreviation());
		setColKey(that.getColKey());
		setCourtNorm(that.getCourtNorm());
		setLuDate(that.getLuDate());
		setLowerJurisNum(that.getLowerJurisNum());
		setPublicationId(that.getPublicationId());
		setIsPredocket(that.getIsPredocket());
	}

	/**
	 * Returns a textual representation of a bean.
	 *
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("countyId=[").append(countyId).append("] ");
		buffer.append("countyCode=[").append(countyCode).append("] ");
		buffer.append("courtId=[").append(courtId).append("] ");
		buffer.append("districtId=[").append(districtId).append("] ");
		buffer.append("name=[").append(name).append("] ");
		buffer.append("abbreviation=[").append(abbreviation).append("] ");
		buffer.append("colKey=[").append(colKey).append("] ");
		buffer.append("courtNorm=[").append(courtNorm).append("] ");
		buffer.append("luDate=[").append(luDate).append("] ");
		buffer.append("lowerJurisNum=[").append(lowerJurisNum).append("] ");
		buffer.append("publicationId=[").append(publicationId).append("] ");
		buffer.append("isPredocket=[").append(isPredocket).append("]");

		return buffer.toString();
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((countyId == null) ? 0 : countyId.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof County))
			return false;
		County equalCheck = (County) obj;
		if ((countyId == null && equalCheck.countyId != null) || (countyId != null && equalCheck.countyId == null))
			return false;
		if (countyId != null && !countyId.equals(equalCheck.countyId))
			return false;
		return true;
	}
}
