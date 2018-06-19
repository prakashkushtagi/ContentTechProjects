/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.events;

import static com.trgr.dockets.core.parser.saxevent.events.CharacterEvent.MAX_SUMMARY_LENGTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.trgr.dockets.core.parser.saxevent.DocLocation;
import com.trgr.dockets.core.parser.saxevent.DocRange;
import com.trgr.dockets.core.parser.saxevent.events.CharacterEvent;

@SuppressWarnings("static-method")
public final class CharacterEventTest {

	@Test
	public void testCharacterEvent() {
		checkCharacterEvent(0,3);
		checkCharacterEvent(3,6);
		checkCharacterEvent(0,MAX_SUMMARY_LENGTH);
		checkCharacterEvent(0,MAX_SUMMARY_LENGTH - 1);
		checkCharacterEvent(0,MAX_SUMMARY_LENGTH + 1);
	}
	
	private static void checkCharacterEvent(final int start, final int length) {
		final DocRange docRange = new DocRange(new DocLocation(1,2),new DocLocation(3,4));
		final String source = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
				+ "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
		final CharacterEvent event = new CharacterEvent(docRange,source.toCharArray(),start,length);
		final String expectedText = source.substring(start,start + length);
		final int expectedTextLen = expectedText.length();
		final String expectedSummary =
				(expectedTextLen <= MAX_SUMMARY_LENGTH) ? expectedText : expectedText.substring(0, MAX_SUMMARY_LENGTH);
		assertSame(docRange, event.getDocRange());
		assertEquals(expectedText, event.getText());
		assertEquals(expectedSummary, event.getSummary());
	}
}
