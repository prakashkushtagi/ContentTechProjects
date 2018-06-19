/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class HostClusterTest {
	
	@Test
	public void testHostCluster() {
		final HostCluster cluster = new HostCluster();
		
		final long id = 123;
		cluster.setClusterId(id);
		assertEquals(id, cluster.getClusterId());
		
		final String name = "name";
		cluster.setClusterName(name);
		assertSame(name,cluster.getClusterName());
	}
}
