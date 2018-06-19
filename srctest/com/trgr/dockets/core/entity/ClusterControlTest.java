/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClusterControlTest {
	
	@Test
	public void testClusterControl() {
		final ClusterControl control = new ClusterControl();
		
		final long id = 123;
		control.setClusterId(id);
		assertEquals(id, control.getClusterId());
		
		final HostCluster cluster = new HostCluster();
		control.setCluster(cluster);
		assertSame(cluster, control.getCluster());
		
		control.setActive(true);
		assertTrue(control.isActive());
		
		final String userId = "userId";
		control.setUserId(userId);
		assertSame(userId,control.getUserId());
	}

}
