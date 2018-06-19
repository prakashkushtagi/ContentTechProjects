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
 * The hosts that are running the DocketsBatch web application and are available to 
 * handle docket processing.
 */
@Entity
@Table(name="HOST_SPRING_BATCH", schema="DOCKETS_PREPROCESSOR")
public class SpringBatchHost {
	
	private String hostName;
	private Long maxJobs;
	private String client;
	private String active;
	private ClusterControl clusterControl;
	private Long maxDispatcherThreads;
	private String hasDispatcher;
	
	public SpringBatchHost() {
		super();
	}
	public SpringBatchHost(String name) {
		setHostName(name);
	}
	
	public SpringBatchHost(String name, Long maxJobs, String client) {
		setHostName(name);
		this.maxJobs = maxJobs;
		this.client = client;
	}
	
	public SpringBatchHost(
			final String name,
			final Long maxJobs,
			final String client,
			final long maxDispatcherThreads,
			final boolean hasDispatcher) {
		this(name,maxJobs,client);
		this.maxDispatcherThreads = maxDispatcherThreads;
		this.hasDispatcher = hasDispatcher ? "Y" : "N";
	}
	
	/**
	 * Full constructor
	 * 
	 * @param name
	 * @param maxJobs
	 * @param client
	 * @param active
	 */
	public SpringBatchHost(String name, Long maxJobs, String client, String active) {
		setHostName(name);
		this.maxJobs = maxJobs;
		this.client = client;
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
	
	@Column(name="CLIENT")
	public String getClient() {
		return client;
	}
	
	@Column(name="ACTIVE")
	public String getActive() {
		return active;
	}
	
	@ManyToOne
	@JoinColumn(name="CLUSTER_ID")
	public ClusterControl getClusterControl() {
		return clusterControl;
	}
	
	@Column(name="MAX_DISPATCHER_THREADS")
	public Long getMaxDispatcherThreads() {
		return maxDispatcherThreads;
	}
	
	@Column(name="HAS_DISPATCHER")
	public String getHasDispatcher() {
		return hasDispatcher;
	}
	
	public Long getMaxThreadsByApplication(final boolean forDispatcher) {
		return forDispatcher ? maxDispatcherThreads : maxJobs;
	}
	
	public boolean hasDispatcher() {
		return "Y".equals(hasDispatcher);
	}

	public void setHostName(String name) {
		this.hostName = name;
	}
	
	public void setMaxJobs(Long maxJobs) {
		this.maxJobs = maxJobs;
	}

	public void setClient(String client) {
		this.client = client;
	}
	
	public void setActive(String active) {
		this.active = active;
	}
	
	public void setClusterControl(final ClusterControl control) {
		this.clusterControl = control;
	}
	
	public void setMaxDispatcherThreads(final Long maxThreads) {
		this.maxDispatcherThreads = maxThreads;
	}
	
	public void setHasDispatcher(final String hasDispatcher) {
		this.hasDispatcher = hasDispatcher;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
