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
@IdClass(com.trgr.dockets.core.entity.DictionaryPK.class)
@Entity
@NamedQueries({
		@NamedQuery(name = "findDictionaryByCountyId", query = "select myDictionary from Dictionary myDictionary where myDictionary.countyId = :countyId"),
		@NamedQuery(name = "findDictionaryByPrimaryKey", query = "select myDictionary from Dictionary myDictionary where myDictionary.countyId = :countyId and myDictionary.type = :type and myDictionary.key = :key") })
@Table(schema = "DOCKETS_PREPROCESSOR", name = "DICTIONARY")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "DocketsCore/com/trgr/dockets/core/entity", name = "Dictionary")
public class Dictionary implements Serializable {
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

	@Column(name = "DICTIONARY_TYPE_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	Integer type;
	/**
	 */

	@Column(name = "KEY", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String key;
	/**
	 */

	@Column(name = "VALUE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Lob
	@XmlElement
	byte[] value;
	/**
	 */

	@Column(name = "SOURCE_FILENAME", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String sourceFilename;
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
	public void setSourceFilename(String sourceFilename) {
		this.sourceFilename = sourceFilename;
	}

	/**
	 */
	public String getSourceFilename() {
		return this.sourceFilename;
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

	/**
	 */
	public Dictionary() {
	}

	/**
	 * Copies the contents of the specified bean into this bean.
	 *
	 */
	public void copy(Dictionary that) {
		setCountyId(that.getCountyId());
		setType(that.getType());
		setKey(that.getKey());
		setValue(that.getValue());
		setSourceFilename(that.getSourceFilename());
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
		buffer.append("key=[").append(key).append("] ");
		buffer.append("value=[").append(value).append("] ");
		buffer.append("sourceFilename=[").append(sourceFilename).append("] ");
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
		result = (int) (prime * result + ((key == null) ? 0 : key.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Dictionary))
			return false;
		Dictionary equalCheck = (Dictionary) obj;
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
}
