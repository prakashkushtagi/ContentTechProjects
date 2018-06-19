/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.trgr.dockets.core.parser.saxevent.DocLocation;

public final class LinePositionIndexTest {

	@SuppressWarnings("static-method")
	@Test
	public void testConstructor() {
		final int lineNumber = 1;
		final int columnNumber = 1;
		final DocLocation docLocation = new DocLocation(lineNumber,columnNumber);
		final LinePositionIndex index = new LinePositionIndex();
		final long bytePosition = index.getCharPosition(docLocation);
		assertEquals(0, bytePosition);
	}

	@SuppressWarnings("static-method")
	@Test
	public void testDeleteToLineNumber() {
		final LinePositionIndex index = new LinePositionIndex();
		for (int lineNumber = 2; lineNumber <= 10; lineNumber++) {
			index.add((lineNumber - 1) * 100L);
		}
		final int windowStartLineNumber = 5;
		final int columnNumber = 1;
		index.deleteToLineNumber(windowStartLineNumber);
		final DocLocation docLocation = new DocLocation(windowStartLineNumber,columnNumber);
		final long bytePosition = index.getCharPosition(docLocation);
		assertEquals(400, bytePosition);
	}
}
