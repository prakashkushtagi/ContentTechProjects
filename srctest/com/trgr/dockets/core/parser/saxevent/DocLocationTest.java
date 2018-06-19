/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.trgr.dockets.core.parser.saxevent.DocLocation;

public class DocLocationTest {

	@Test
	public void testDocLocation() {
		final DocLocation location = new DocLocation(1,2);
		assertEquals(1, location.getLineNumber());
		assertEquals(2, location.getColumnNumber());
		assertEquals("(1,2)", location.toString());
	}
}
