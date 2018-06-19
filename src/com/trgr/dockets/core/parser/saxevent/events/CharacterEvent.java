/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.events;

import com.trgr.dockets.core.parser.saxevent.DocRange;

public final class CharacterEvent extends SaxEvent {
	
	public static final int MAX_SUMMARY_LENGTH = 10;
	
	private final String fText;

	public CharacterEvent(final DocRange docRange, final char[] ch, final int start, final int length) {
		super(docRange);
		fText = new String(ch,start,length);
	}
	
	@Override public final boolean isCharacter() {
		return true;
	}
	
	public final String getText() {
		return fText;
	}

	@Override
	public final String getSummary() {
		final String trimmedText = fText.trim();
		final String raw = (trimmedText.length() <= MAX_SUMMARY_LENGTH)
				? trimmedText : trimmedText.substring(0, MAX_SUMMARY_LENGTH);
		return raw.replaceAll("\n","\\\\n");
	}
}
