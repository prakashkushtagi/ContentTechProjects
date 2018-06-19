/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.job.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.batch.core.BatchStatus;

/**
 * The filter criteria used when searching for jobs to display in the Job Summary table.
 * A null or blank property value indicates that it is to be ignored and not included as part of the search criteria.
 */
public class JobFilter {
	
	// Job Execution properties
	private Date from;	// start date on and after this calendar date (inclusive)
	private Date to;	// start date on and before this calendar date (inclusive)
	private BatchStatus[] batchStatus;	// Multi-selectable job batch status

	public JobFilter() {
		super();
	}
	public JobFilter(Date from, Date to, BatchStatus[] batchStatus) {
		this.from = from;
		this.to = to;
		this.batchStatus = batchStatus;
	}
	
	/** Include executions with a start time from the start of (00:00:00) of this calendar date and after. */
	public Date getFrom() {
		return from;
	}
	/** Filter to date entered by user, normalized to midnight (00:00:00) of the entered day. */
	public Date getTo() {
		return to;
	}
	public BatchStatus[] getBatchStatus() {
		return batchStatus;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
