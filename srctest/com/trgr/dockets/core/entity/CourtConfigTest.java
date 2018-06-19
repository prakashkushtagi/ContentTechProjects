/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.entity;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class CourtConfigTest {
	
	@SuppressWarnings("static-method")
	@Test
	public void testMaxBatchDocketSize() {
		final Long maxSize = 1234L;
		final CourtConfig config1 = new CourtConfig();
		final CourtConfig config2 = new CourtConfig();
		assertEquals(config1,config2);
		config2.setMaxBatchDocketSize(maxSize);
		assertEquals(maxSize, config2.getMaxBatchDocketSize());
		assertFalse(config1.equals(config2));
		assertTrue(config1.hashCode() != config2.hashCode());
		assertTrue(config2.toString().contains(maxSize.toString()));
	}
}
