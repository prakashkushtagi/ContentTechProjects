package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@MappedSuperclass
public class AbstractClusterControl {

	private long clusterId;
	private HostCluster cluster;
	private boolean active;
	private String userId;
	
	@Id
	@Column(name="CLUSTER_ID", length=256)
	public long getClusterId() {
		return clusterId;
	}
	
	@OneToOne
	@JoinColumn(name="CLUSTER_ID", insertable=false, updatable = false)
	public HostCluster getCluster() {
		return cluster;
	}
	
	@Column(name="ACTIVE")
	public String getActiveString() {
		return (active) ? "Y" : "N";
	}
	
	@Transient
	public boolean isActive() {
		return active;
	}
	
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	
	public void setClusterId(final long id) {
		this.clusterId = id;
	}
	
	public void setCluster(final HostCluster cluster) {
		this.cluster = cluster;
	}

	protected void setActiveString(final String active) {
		setActive("Y".equals(active));
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setUserId(final String id) {
		this.userId = id;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
