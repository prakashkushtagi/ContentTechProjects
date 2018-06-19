/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="COLLECTION", schema="DOCKETS_PUB")
public class CollectionEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long primaryKey;
	private String name;
	private String ltcDatatype;
	private String metadocCollectionName;
	private String collectionNumber; // varchar2 for 0 or 00 values.
	
	public CollectionEntity() {
		super();
	}
	public CollectionEntity(Long pk, String name) {
		setPrimaryKey(pk);
		setName(name);
	}
	
	@Id
	@Column(name="COLLECTION_ID")
	public Long getPrimaryKey() {
		return primaryKey;
	}
	@Column(name="COLLECTION_NAME")
	public String getName() {
		return name;
	}
	@Column(name="LTC_DATA_TYPE")
	public String getLtcDatatype() {
		return ltcDatatype;
	}
	@Column(name="METADOC_COLLECTION_NAME")
	public String getMetadocCollectionName() {
		return metadocCollectionName;
	}
	@Column(name="COLLECTION_NUMBER")
	public String getCollectionNumber() {
		return collectionNumber;
	}
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMetadocCollectionName(String metadocCollectionName) {
		this.metadocCollectionName = metadocCollectionName;
	}
	public void setCollectionNumber(String collectionNumber) {
		this.collectionNumber = collectionNumber;
	}
	/**
	 * @param ltcDatatype the ltcDatatype to set
	 */
	public void setLtcDatatype(String ltcDatatype) {
		this.ltcDatatype = ltcDatatype;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result
				+ ((ltcDatatype == null) ? 0 : ltcDatatype.hashCode());
		result = prime * result
				+ ((metadocCollectionName == null) ? 0 : metadocCollectionName.hashCode());
		result = prime * result
				+ ((collectionNumber == null) ? 0 : collectionNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollectionEntity other = (CollectionEntity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (metadocCollectionName == null) {
			if (other.metadocCollectionName != null)
				return false;
		} else if (!metadocCollectionName.equals(other.metadocCollectionName))
			return false;
		if (collectionNumber == null) {
			if (other.collectionNumber != null)
				return false;
		} else if (!collectionNumber.equals(other.collectionNumber))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (ltcDatatype == null) {
			if (other.ltcDatatype != null)
				return false;
		} else if (!ltcDatatype.equals(other.ltcDatatype))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}

