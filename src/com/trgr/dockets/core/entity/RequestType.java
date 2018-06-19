package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;

@Entity
@Table(name="REQUEST_TYPE",schema="DOCKETS_PUB")
public class RequestType {
	
	private RequestTypeEnum primaryKey;
	
	public RequestType() {
		super();
	}
	public RequestType(RequestTypeEnum key) {
		setPrimaryKey(key);
	}

	@Id
	@Column(name= "REQUEST_TYPE_ID")
	public Long getId() {
		return primaryKey.getKey();
	}
	@Transient
	public RequestTypeEnum getPrimaryKey() {
		return primaryKey;
	}
	
	public void setId(Long id) {
		setPrimaryKey(RequestTypeEnum.findByKey(id));
	}
	public void setPrimaryKey(RequestTypeEnum key) {
		Assert.notNull(key);
		this.primaryKey = key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
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
		RequestType other = (RequestType) obj;
		if (primaryKey != other.primaryKey)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
