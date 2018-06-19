package com.trgr.dockets.core.job.domain;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.Period;
import org.springframework.batch.core.BatchStatus;

/**
 * A subset of job execution data used to present the Job Summary view.
 */
public class JobSummary {
	
	private String jobName;
	private Long jobInstanceId;
	private Long jobExecutionId;
	private BatchStatus batchStatus;
	private String batchId;
	private Date startTime;
	private Date endTime;

	public JobSummary(String jobName, Long jobInstanceId, Long jobExecutionId,
					  BatchStatus batchStatus, String batchId, Date startTime, Date endTime) {
		this.jobName = jobName;
		this.jobInstanceId = jobInstanceId;
		this.jobExecutionId = jobExecutionId;
		this.batchStatus = batchStatus;
		this.batchId = batchId;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Long getJobInstanceId() {
		return jobInstanceId;
	}
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
	public String getJobName() {
		return jobName;
	}
	public BatchStatus getBatchStatus() {
		return batchStatus;
	}
	public String getBatchId() {
		return batchId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public String getDuration() {
		return JobSummary.getExecutionDuration(getExecutionDuration());
	}
	public void setBatchId(String id) {
		this.batchId = id;
	}

	private Long getExecutionDuration() {
		return getExecutionDuration(startTime, endTime);
	}
	
	public static long getExecutionDuration(Date start, Date end) {		
		long duration = 0;
		if (start != null) {
			if (end == null) {
				end = new Date();
			}
			duration = end.getTime() - start.getTime();
		}
		return duration;
	}
	
	public static String getExecutionDuration(Long durationMilliseconds) {
		if (durationMilliseconds == null) {
			return null;
		}
		long durationMs = durationMilliseconds.longValue();
		if (durationMs < 1000) {
			return String.format("%d ms", durationMs);
		}
		// Round to nearest second
		long roundedMs = Math.round(durationMs / 1000.0) * 1000l;
		StringBuffer periodString = new StringBuffer();
		Period period = new Period(roundedMs);
		periodString.append((period.getHours() < 10) ? "0" : "");
		periodString.append(period.getHours());
		periodString.append(":");
		periodString.append((period.getMinutes() < 10) ? "0" : "");
		periodString.append(period.getMinutes());
		periodString.append(":");
		periodString.append((period.getSeconds() < 10) ? "0" : "");
		periodString.append(period.getSeconds());
		return periodString.toString();
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
