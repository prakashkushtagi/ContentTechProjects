package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.batch.core.BatchStatus;

@Entity
@Table(name="BATCH_JOB_EXECUTION", schema="DOCKETS_SPRING_BATCH")
public class JobExecutionEntity {
	
	private Long jobInstanceId;
	private Long jobExecutionId;
	private String status;
	
	@Id
	@Column(name="JOB_EXECUTION_ID")
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
	@Column(name="JOB_INSTANCE_ID")
	public Long getJobInstanceId() {
		return jobInstanceId;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	@Transient
	public BatchStatus getBatchStatus() {
		return (status != null) ? BatchStatus.valueOf(status) : null;
	}
	
	public void setJobInstanceId(Long id) {
		this.jobInstanceId = id;
	}
	public void setJobExecutionId(Long id) {
		this.jobExecutionId = id;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
