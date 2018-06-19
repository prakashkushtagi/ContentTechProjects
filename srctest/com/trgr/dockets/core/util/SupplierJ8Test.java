/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import static junit.framework.Assert.assertSame;

import org.junit.Test;

public final class SupplierJ8Test {

	@SuppressWarnings("static-method")
	@Test
	public void testSupplierJ8() {
		final String testString = "1234";
		final SupplierJ8<String> supplier = new SupplierJ8<String>() {

			@Override
			public String get() {
				return testString;
			}
		};
		assertSame(testString, supplier.get());
	}
}
