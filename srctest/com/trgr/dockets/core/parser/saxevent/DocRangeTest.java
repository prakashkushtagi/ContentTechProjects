/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.trgr.dockets.core.parser.saxevent.DocLocation;
import com.trgr.dockets.core.parser.saxevent.DocRange;

public final class DocRangeTest {

	@SuppressWarnings("static-method")
	@Test
	public void testDocRange() {
		final DocLocation startLocation = new DocLocation(1,2);
		final DocLocation endLocation = new DocLocation(3,4);
		final DocRange range = new DocRange(startLocation,endLocation);
		assertSame(startLocation, range.getStart());
		assertSame(endLocation,range.getEnd());
		assertEquals("(1,2)-(3,4)", range.toString());
	}

}
