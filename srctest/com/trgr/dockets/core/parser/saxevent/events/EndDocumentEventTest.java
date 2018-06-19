/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.events;

import static com.trgr.dockets.core.parser.saxevent.events.EndDocumentEvent.END_DOCUMENT_SUMMARY;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.trgr.dockets.core.parser.saxevent.DocLocation;
import com.trgr.dockets.core.parser.saxevent.DocRange;
import com.trgr.dockets.core.parser.saxevent.events.EndDocumentEvent;

@SuppressWarnings("static-method")
public final class EndDocumentEventTest {

	@Test
	public void testEndDocumentEvent() {
		final DocRange docRange = new DocRange(new DocLocation(1,2),new DocLocation(3,4));
		final EndDocumentEvent event = new EndDocumentEvent(docRange);
		assertSame(docRange, event.getDocRange());
		assertSame(END_DOCUMENT_SUMMARY, event.getSummary());
		assertTrue(event.isEndOfDoc());
	}
}
