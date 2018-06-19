package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;

@Entity
@Table(name="PROCESS_TYPE",schema="DOCKETS_PUB")
public class ProcessType {
	
	private Long primaryKey;
	private String name;
	private String description;
	
	public ProcessType() {
		super();
	}
	public ProcessType(Long key) {
		setPrimaryKey(key);
	}
	public ProcessType(ProcessTypeEnum type) {
		setPrimaryKey(type.getKey());
	}

	@Column(name="PROCESS_DESC", nullable=false)
	public String getDescription() {
		return description;
	}
	@Id
	@Column(name= "PROCESS_TYPE_ID")
	public Long getPrimaryKey() {
		return primaryKey;
	}
	@Column(name="PROCESS_TYPE", nullable=false)
	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrimaryKey(Long key) {
		Assert.notNull(key);
		this.primaryKey = key;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ProcessType other = (ProcessType) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}
}
