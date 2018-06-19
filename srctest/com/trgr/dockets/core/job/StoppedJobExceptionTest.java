/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */

package com.trgr.dockets.core.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

@SuppressWarnings("static-method")
public final class StoppedJobExceptionTest {
	
	public static final String MESSAGE = "message";

	@Test
	public void testMessage() {
		assertEquals(MESSAGE, new StoppedJobException(MESSAGE).getMessage());
	}
	
	@Test
	public void testNoMessage() {
		assertNull(new StoppedJobException().getMessage());
	}
	
	@Test
	public void testException() {
		try {
			throw new StoppedJobException();
		}
		catch (final StoppedJobException e) {
			
		}
		catch (final Throwable e) {
			fail();
		}
	}
}
