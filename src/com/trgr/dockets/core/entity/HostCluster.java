/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="HOST_CLUSTER", schema="DOCKETS_PREPROCESSOR")
public class HostCluster {
	
	private long clusterId;
	private String clusterName;
	
	@Id
	@Column(name="CLUSTER_ID")
	public long getClusterId() {
		return clusterId;
	}
	
	@Column(name="CLUSTER_NAME",length=256)
	public String getClusterName() {
		return clusterName;
	}
	
	public void setClusterId(final long id) {
		this.clusterId = id;
	}
	
	public void setClusterName(final String name) {
		this.clusterName = name;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
