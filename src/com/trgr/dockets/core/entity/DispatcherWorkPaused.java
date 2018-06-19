/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


@Entity
@Table(name="DISPATCHER_WORK_PAUSED", schema="DOCKETS_PREPROCESSOR")
public class DispatcherWorkPaused
{
	private Long jobExecutionId;
	private Long workId;
	private String readyToResume;
	private String serverName;


	
	public DispatcherWorkPaused() {
		super();
	}

	public DispatcherWorkPaused(Long jobExecutionId, Long workId,String readyToResume) {
		super();
		this.jobExecutionId = jobExecutionId;
		this.workId = workId;
		this.readyToResume = readyToResume;
	}

	@Id
	@Column(name="WORK_ID")
	public Long getWorkId()
	{
		return workId;
	}
	
	@Column(name="JOB_EXECUTION_ID")
	public Long getJobExecutionId()
	{
		return jobExecutionId;
	}
	
	@Column(name="READY_TO_RESUME")
	public String getReadyToResume()
	{
		return readyToResume;
	}
	
	
	public void setJobExecutionId(Long jobExecutionId)
	{
		this.jobExecutionId = jobExecutionId;
	}
	
	public void setWorkId(Long workId)
	{
		this.workId = workId;
	}

	public void setReadyToResume(String readyToResume)
	{
		this.readyToResume = readyToResume;
	}
	
	
	@Transient
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((workId == null) ? 0 : workId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DispatcherWorkPaused other = (DispatcherWorkPaused) obj;
		if (workId == null)
		{
			if (other.workId != null) return false;
		}
		else if (!workId.equals(other.workId)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
