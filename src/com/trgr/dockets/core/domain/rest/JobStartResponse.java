package com.trgr.dockets.core.domain.rest;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Response object to the start job REST service operation
 * as carried out by the DocketsBatch web application internal REST service provider.
 */
@XmlRootElement(name="jobStartResponse") 
public class JobStartResponse extends BaseStartRestartStopResponse {

	private String jobName;
	private Long instanceId;
	private String batchId;
	private Date jobStartTime;
	
	public JobStartResponse() {
		super();
	}

	public JobStartResponse(String jobName, boolean success, Long instanceId, Long executionId, String batchId,
						    String message) {
		super(success, executionId, message);
		setJobName(jobName);
		setInstanceId(instanceId);
		setBatchId(batchId);
	}
	@XmlElement(name="jobName")
	public String getJobName() {
		return jobName;
	}
	/** Returns the Instance ID of the job that was started. */
	@XmlElement(name="instanceId")
	public Long getInstanceId() {
		return instanceId;
	}
	@XmlElement(name="batchId")
	public String getBatchId() {
		return batchId;
	}
	@XmlElement(name="jobStartTime")
	public Date getJobStartTime(){
		return jobStartTime;
	}

	public void setJobStartTime(Date jobStartTime){
		this.jobStartTime = jobStartTime;
	}

	public void setJobName(String name) {
		this.jobName = name;
	}
	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
