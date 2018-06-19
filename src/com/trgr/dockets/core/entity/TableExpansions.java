/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 */
@IdClass(com.trgr.dockets.core.entity.TableExpansionsPK.class)
@Entity
@NamedQueries({
	 @NamedQuery(name = "findTableExpansionsByCourtId", query = "select myTableExpansions from TableExpansions myTableExpansions where myTableExpansions.courtId = :courtId"),
	 @NamedQuery(name = "findTableExpansionsByPrimaryKey", query = "select myTableExpansions from TableExpansions myTableExpansions where myTableExpansions.courtId = :courtId and myTableExpansions.recordType = :recordType and myTableExpansions.columnName = :columnName and myTableExpansions.key = :key") })
@Table(schema = "DOCKETS_PREPROCESSOR", name = "TABLE_EXPANSIONS")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "DocketsCore/com/trgr/dockets/core/entity", name = "TableExpansions")
public class TableExpansions implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */

	@Column(name = "COURT_ID", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	Integer courtId;
	/**
	 */

	@Column(name = "RECORD_TYPE", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String recordType;
	/**
	 */

	@Column(name = "COLUMN_NAME", length = 64, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String columnName;
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
	@XmlElement
	String value;
	/**
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LU_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Calendar luDate;

	@Transient
	private String element;
	@Transient
	private String delimiter;
	@Transient
	private int position = 0;
	
	@Transient
	private int offset = 5;
	@Transient
	private String notFoundValue;
	@Transient
	private boolean displayDelimiterFlag = false;
	

	@Transient
	private List<String> recordTypeList;
	@Transient
	private List<String> columnNameList;
	@Transient
	private List<Integer> positionList;
		
	/**
	 */
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public int getPosition() {
		return position;
	}

	
	public boolean isDisplayDelimiterFlag() {
		return displayDelimiterFlag;
	}

	public void setDisplayDelimiterFlag(boolean displayDelimiterFlag) {
		this.displayDelimiterFlag = displayDelimiterFlag;
	}

	public void setPosition(int position) {
		this.position = position;
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
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 */
	public void setLuDate(Calendar luDate) {
		this.luDate = luDate;
	}

	/**
	 */
	public Calendar getLuDate() {
		return this.luDate;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	/**
	 */
	public TableExpansions() {
	}

	/**
	 * Copies the contents of the specified bean into this bean.
	 *
	 */
	public void copy(TableExpansions that) {
		setCourtId(that.getCourtId());
		setRecordType(that.getRecordType());
		setColumnName(that.getColumnName());
		setKey(that.getKey());
		setValue(that.getValue());
		setLuDate(that.getLuDate());
	}

//	/**
//	 * Returns a textual representation of a bean.
//	 *
//	 */
//	public String toString() {
//
//		StringBuilder buffer = new StringBuilder();
//
//		buffer.append("courtId=[").append(courtId).append("] ");
//		buffer.append("recordType=[").append(recordType).append("] ");
//		buffer.append("columnName=[").append(columnName).append("] ");
//		buffer.append("key=[").append(key).append("] ");
//		buffer.append("value=[").append(value).append("] ");
//		buffer.append("luDate=[").append(luDate).append("] ");
//
//		return buffer.toString();
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result
				+ ((columnNameList == null) ? 0 : columnNameList.hashCode());
		result = prime * result + ((courtId == null) ? 0 : courtId.hashCode());
		result = prime * result
				+ ((delimiter == null) ? 0 : delimiter.hashCode());
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((luDate == null) ? 0 : luDate.hashCode());
		result = prime * result
				+ ((notFoundValue == null) ? 0 : notFoundValue.hashCode());
		result = prime * result + offset;
		result = prime * result + position;
		result = prime * result
				+ ((positionList == null) ? 0 : positionList.hashCode());
		result = prime * result
				+ ((recordType == null) ? 0 : recordType.hashCode());
		result = prime * result
				+ ((recordTypeList == null) ? 0 : recordTypeList.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "TableExpansions [courtId=" + courtId + ", recordType="
				+ recordType + ", columnName=" + columnName + ", key=" + key
				+ ", value=" + value + ", luDate=" + luDate + ", element="
				+ element + ", delimiter=" + delimiter + ", position="
				+ position + ", offset=" + offset + ", notFoundValue="
				+ notFoundValue + ", recordTypeList=" + recordTypeList
				+ ", columnNameList=" + columnNameList + ", positionList="
				+ positionList + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableExpansions other = (TableExpansions) obj;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (columnNameList == null) {
			if (other.columnNameList != null)
				return false;
		} else if (!columnNameList.equals(other.columnNameList))
			return false;
		if (courtId == null) {
			if (other.courtId != null)
				return false;
		} else if (!courtId.equals(other.courtId))
			return false;
		if (delimiter == null) {
			if (other.delimiter != null)
				return false;
		} else if (!delimiter.equals(other.delimiter))
			return false;
		if (element == null) {
			if (other.element != null)
				return false;
		} else if (!element.equals(other.element))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (luDate == null) {
			if (other.luDate != null)
				return false;
		} else if (!luDate.equals(other.luDate))
			return false;
		if (notFoundValue == null) {
			if (other.notFoundValue != null)
				return false;
		} else if (!notFoundValue.equals(other.notFoundValue))
			return false;
		if (offset != other.offset)
			return false;
		if (position != other.position)
			return false;
		if (positionList == null) {
			if (other.positionList != null)
				return false;
		} else if (!positionList.equals(other.positionList))
			return false;
		if (recordType == null) {
			if (other.recordType != null)
				return false;
		} else if (!recordType.equals(other.recordType))
			return false;
		if (recordTypeList == null) {
			if (other.recordTypeList != null)
				return false;
		} else if (!recordTypeList.equals(other.recordTypeList))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public List<String> getRecordTypeList() {
		return recordTypeList;
	}

	public void setRecordTypeList(List<String> recordTypeList) {
		this.recordTypeList = recordTypeList;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int i) {
		this.offset = i;
	}

	public String getNotFoundValue() {
		return notFoundValue;
	}
	
	public void setNotFoundValue(String string) {
		this.notFoundValue = string;
	}

	public List<String> getColumnNameList() {
		return columnNameList;
	}

	public void setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
	}

	public List<Integer> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Integer> positionList) {
		this.positionList = positionList;
	}
	
}
