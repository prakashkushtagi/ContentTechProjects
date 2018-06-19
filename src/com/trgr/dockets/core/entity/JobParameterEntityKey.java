package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Embeddable
public class JobParameterEntityKey implements Serializable {
	private static final long serialVersionUID = 3348414368151844191L;

	private Long jobExecutionId;
	private String keyName;
	
	public JobParameterEntityKey() {
		super();
	}
	public JobParameterEntityKey(Long executionId, String keyName) {
		this.jobExecutionId = executionId;
		this.keyName = keyName;
	}
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setJobExecutionId(Long instanceId) {
		this.jobExecutionId = instanceId;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
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
				+ ((jobExecutionId == null) ? 0 : jobExecutionId.hashCode());
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
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
		JobParameterEntityKey other = (JobParameterEntityKey) obj;
		if (jobExecutionId == null) {
			if (other.jobExecutionId != null)
				return false;
		} else if (!jobExecutionId.equals(other.jobExecutionId))
			return false;
		if (keyName == null) {
			if (other.keyName != null)
				return false;
		} else if (!keyName.equals(other.keyName))
			return false;
		return true;
	}
}
