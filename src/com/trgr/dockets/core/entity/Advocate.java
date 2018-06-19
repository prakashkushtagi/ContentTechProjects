/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import java.lang.StringBuilder;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import javax.xml.bind.annotation.*;

import javax.persistence.*;

/**
 */
@IdClass(com.trgr.dockets.core.entity.AdvocatePK.class)
@Entity
@NamedQueries({
		@NamedQuery(name = "findAdvocateByCountyId", query = "select myAdvocate from Advocate myAdvocate where myAdvocate.countyId = :countyId"),
		@NamedQuery(name = "findAdvocateByPrimaryKey", query = "select myAdvocate from Advocate myAdvocate where myAdvocate.countyId = :countyId and myAdvocate.type = :type and myAdvocate.id = :id") })
@Table(schema = "DOCKETS_PREPROCESSOR", name = "ADVOCATE")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "DocketsCore/com/trgr/dockets/core/entity", name = "Advocate")
public class Advocate implements Serializable {
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

	@Column(name = "ADVOCATE_TYPE_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	Integer type;
	/**
	 */

	@Column(name = "ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String id;
	/**
	 */

	@Column(name = "VALUE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Lob
	@XmlElement
	byte[] value;
	/**
	 */

	@Column(name = "FILENAME", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String filename;
	/**
	 */

	@Column(name = "LU_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Timestamp luDate;

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
	public void setValue(byte[] value) {
		this.value = value;
	}

	/**
	 */
	public byte[] getValue() {
		return this.value;
	}

	/**
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 */
	public String getFilename() {
		return this.filename;
	}
	

	public Timestamp getLuDate() {
		return luDate;
	}

	public void setLuDate(Timestamp luDate) {
		this.luDate = luDate;
	}

	/**
	 */
	public Advocate() {
	}

	/**
	 * Copies the contents of the specified bean into this bean.
	 *
	 */
	public void copy(Advocate that) {
		setCountyId(that.getCountyId());
		setType(that.getType());
		setId(that.getId());
		setValue(that.getValue());
		setFilename(that.getFilename());
		setLuDate(that.getLuDate());
	}

	/**
	 * Returns a textual representation of a bean.
	 *
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		buffer.append("courtId=[").append(countyId).append("] ");
		buffer.append("type=[").append(type).append("] ");
		buffer.append("id=[").append(id).append("] ");
		buffer.append("value=[").append(value).append("] ");
		buffer.append("filename=[").append(filename).append("] ");
		buffer.append("luDate=[").append(luDate).append("] ");

		return buffer.toString();
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
		if (!(obj instanceof Advocate))
			return false;
		Advocate equalCheck = (Advocate) obj;
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
}
