package com.trgr.dockets.core.domain.rest;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Response object to the restart job REST service operation
 * as carried out by the DocketsBatch web application internal REST service provider.
 */
public class BaseStartRestartStopResponse {

	private boolean success;
	private Long executionId;
	private String message;
	
	public BaseStartRestartStopResponse() {
		super();
	}

	public BaseStartRestartStopResponse(boolean success, Long executionId,
						   String message) {
		setSuccess(success);
		setExecutionId(executionId);
		setMessage(message);
	}
	
	/** Returns the ID of the job that was stopped. */
	@XmlElement(name="executionId")
	public Long getExecutionId() {
		return executionId;
	}
	/** Returns the general description message describing the success or failure of the operation. */
	@XmlElement(name="message")
	public String getMessage() {
		return message;
	}
	/**
	 * Returns true if the operation was successful.
	 * Will be false (failure) if the job could not be stopped because is was not running.
	 */
	@XmlElement(name="success")
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
