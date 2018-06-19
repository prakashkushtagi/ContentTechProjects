package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;

@Entity
@Table(name="STATUS",schema="DOCKETS_PUB")
public class Status implements Serializable {
	private static final long serialVersionUID = 2533744042610642469L;
	private Long id;
	private String status;
	
	public Status() {
		super();
	}
	public Status(Long id) {
		setId(id);
	}
	public Status(Long id,String status) {
		setId(id);
		setStatus(status);
	}
	public Status(StatusEnum status) {
		setId(status.getKey());
		setStatus(status.name());
	}
	
	@Id
	@Column(name= "STATUS_ID")
	public Long getId() {
		return id;
	}
	@Column(name= "STATUS")
	public String getStatus() {
		return status;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Status other = (Status) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
