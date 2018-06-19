/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */

package com.trgr.dockets.core.job;

import static com.trgr.dockets.core.job.StoppedJobExceptionTest.MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

@SuppressWarnings("static-method")
public final class StopRequestMonitorTest {

	@Test
	public void testInitialValue() {
		assertFalse(new StopRequestMonitor().isRequestedToStop());
	}
	
	@Test
	public void testSettingToStop() {
		final StopRequestMonitor monitor = new StopRequestMonitor();
		monitor.setRequestedToStop();
		assertTrue(monitor.isRequestedToStop());
	}
	
	@Test
	public void testReset() {
		final StopRequestMonitor monitor = new StopRequestMonitor();
		monitor.setRequestedToStop();
		monitor.reset();
		assertFalse(monitor.isRequestedToStop());
	}
	
	@Test
	public void testStopIfRequested() {
		final StopRequestMonitor monitor = new StopRequestMonitor();
		monitor.setRequestedToStop();
		try {
			monitor.stopJobIfRequested(MESSAGE);
			fail();
		} catch (final StoppedJobException e) {
			assertEquals(MESSAGE, e.getMessage());
		}
	}
	
	@Test
	public void testStopIfRequestedWithNoMessage() {
		final StopRequestMonitor monitor = new StopRequestMonitor();
		monitor.setRequestedToStop();
		try {
			monitor.stopJobIfRequested();
			fail();
		} catch (final StoppedJobException e) {
			assertNull(e.getMessage());
		}
	}
	
	@Test
	public void testDoNotStopIfNotRequested() {
		final StopRequestMonitor monitor = new StopRequestMonitor();
		try {
			monitor.stopJobIfRequested(MESSAGE);
		} catch (final StoppedJobException e) {
			fail();
		}
	}
}
