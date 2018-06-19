/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import static junit.framework.Assert.assertSame;

import org.junit.Test;

public final class ConsumerJ8Test {

	@SuppressWarnings("static-method")
	@Test
	public void testConsumerJ8() {
		final String[] stringPointer = {null};
		final String testString = "1234";
		final ConsumerJ8<String> consumer = new ConsumerJ8<String>() {

			@Override
			public void accept(final String t) {
				stringPointer[0] = t;
			}
		};
		consumer.accept(testString);
		assertSame(testString, stringPointer[0]);
	}
}
