/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The status of clusters of servers.
 */
@Entity
@Table(name="CLUSTER_CONTROL", schema="DOCKETS_PREPROCESSOR")
public class ClusterControl extends AbstractClusterControl{

	public ClusterControl(){
		super();
	}
}
