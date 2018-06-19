package com.trgr.dockets.core.domain.rest;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Response object to the restart job REST service operation
 * as carried out by the DocketsBatch web application internal REST service provider.
 */
@XmlRootElement(name="jobStopResponse") 
public class JobStopResponse extends BaseStartRestartStopResponse {

	public JobStopResponse() {
		super();
	}

	public JobStopResponse(boolean success, Long executionId,
						   String message) {
		super(success, executionId, message);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
