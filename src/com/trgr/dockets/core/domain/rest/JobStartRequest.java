package com.trgr.dockets.core.domain.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Identifies a specific collection and docket sub-batch for processing.
 * The type of processing applied to the sub-batch depends upon what the collection is. 
 */
@XmlRootElement(name="jobStartRequest") 
public class JobStartRequest {
	
	private String batchId;
	private Long workId;
	
	public JobStartRequest() {
		super();
	}
	public JobStartRequest(String batchId,Long workId) {
		setBatchId(batchId);
		setWorkId(workId);
	}
	
	@XmlElement(name="batchId")
	public String getBatchId() {
		return batchId;
	}

	@XmlElement(name="workId")
	public Long getWorkId() {
		return workId;
	}
	public void setBatchId(String id) {
		this.batchId = id;
	}
	public void setWorkId(Long workId) {
		this.workId = workId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((workId == null) ? 0 : workId.hashCode());
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
		JobStartRequest other = (JobStartRequest) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (workId == null) {
			if (other.workId != null)
				return false;
		} else if (!workId.equals(other.workId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
