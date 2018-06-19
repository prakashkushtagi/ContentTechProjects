/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.events;

import static com.trgr.dockets.core.parser.saxevent.events.SaxEvent.toSaxEvent;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.trgr.dockets.core.parser.saxevent.DocLocation;
import com.trgr.dockets.core.parser.saxevent.DocRange;
import com.trgr.dockets.core.parser.saxevent.events.SaxEvent;

@SuppressWarnings("static-method")
public final class SaxEventTest {

	@Test
	public void testSaxEvent() {
		final DocRange docRange = new DocRange(new DocLocation(1,2),new DocLocation(3,4));
		final String expectedSummary = "summary";
		final SaxEvent event = new SaxEvent(docRange) {

			@Override
			public String getSummary() {
				return expectedSummary;
			}
		};
		assertSame(docRange, event.getDocRange());
		assertSame(expectedSummary, event.getSummary());
		assertFalse(event.isEndOfDoc());
		assertTrue(event.isNotEndOfDoc());
		assertFalse(event.isCharacter());
		assertFalse(event.isElement());
		assertSame(event,toSaxEvent(event));
		try {
			toSaxEvent("");
			fail();
		} catch (final Throwable e) {
			assertTrue(e instanceof ClassCastException);
		}
	}
}
