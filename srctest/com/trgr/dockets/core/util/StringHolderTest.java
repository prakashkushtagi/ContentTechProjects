/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public final class StringHolderTest {

	@SuppressWarnings("static-method")
	@Test
	public void testStringHolder() {
		final String test = "123";
		final StringHolder holder = new StringHolder();
		holder.accept(test);
		assertSame(test, holder.get());
	}
}
