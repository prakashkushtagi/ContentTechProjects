/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class SpringBatchHostTest {
	
	@Test
	public void testClusterControl() {
		final SpringBatchHost host = new SpringBatchHost();
		
		final ClusterControl control = new ClusterControl();
		host.setClusterControl(control);
		assertSame(control,host.getClusterControl());
	}
}
