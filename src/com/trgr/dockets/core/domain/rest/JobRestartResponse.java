package com.trgr.dockets.core.domain.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Response object to the restart job REST service operation
 * as carried out by the DocketsBatch web application internal REST service provider.
 */
@XmlRootElement(name="jobRestartResponse") 
public class JobRestartResponse extends BaseStartRestartStopResponse {

	private Long restartedExecutionId;
	
	public JobRestartResponse() {
		super();
	}

	public JobRestartResponse(boolean success, Long executionId, Long restartedExecutionId,
						   	  String message) {
		super(success, executionId, message);
		setRestartedExecutionId(restartedExecutionId);
	}

	/** Returns the execution ID of the restarted job. Will be null if the job failed to restart. */
	@XmlElement(name="restartedExecutionId")
	public Long getRestartedExecutionId() {
		return restartedExecutionId;
	}
	
	public void setRestartedExecutionId(Long restartedExecutionId) {
		this.restartedExecutionId = restartedExecutionId;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
