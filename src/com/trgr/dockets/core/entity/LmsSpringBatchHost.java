package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The hosts that are running the LoadMonitor web application and are available to 
 * handle message processing.
 */
@Entity
@Table(name="LMS_HOST_SPRING_BATCH", schema="DOCKETS_PREPROCESSOR")
public class LmsSpringBatchHost {

	private String hostName;
	private Long maxJobs;
	private String active;
	private LmsClusterControl lmsClusterControl;
	
	public LmsSpringBatchHost() {
		super();
	}
	public LmsSpringBatchHost(String name) {
		setHostName(name);
	}
	
	public LmsSpringBatchHost(String name, Long maxJobs) {
		setHostName(name);
		this.maxJobs = maxJobs;
	}
	
	/**
	 * Full constructor
	 * 
	 * @param name
	 * @param maxJobs
	 * @param active
	 */
	public LmsSpringBatchHost(String name, Long maxJobs, String active) {
		setHostName(name);
		this.maxJobs = maxJobs;
		this.active = active;
	}
	
	@Id
	@Column(name="HOST_NAME",length=256)
	public String getHostName() {
		return hostName;
	}
	
	@Column(name="MAX_JOBS")
	public Long getMaxJobs() {
		return maxJobs;
	}
	
	@Column(name="ACTIVE")
	public String getActive() {
		return active;
	}
	
	@ManyToOne
	@JoinColumn(name="CLUSTER_ID")
	public LmsClusterControl getLmsClusterControl() {
		return lmsClusterControl;
	}

	public void setHostName(String name) {
		this.hostName = name;
	}
	
	public void setMaxJobs(Long maxJobs) {
		this.maxJobs = maxJobs;
	}
	
	public void setActive(String active) {
		this.active = active;
	}
	
	public void setLmsClusterControl(final LmsClusterControl control) {
		this.lmsClusterControl = control;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
