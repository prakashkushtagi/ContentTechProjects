/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.events;

import com.trgr.dockets.core.parser.saxevent.DocRange;

public abstract class SaxEvent {
	
	public static final SaxEvent NULL_SAX_EVENT = null;
	
	private final DocRange docRange;
	
	protected SaxEvent(final DocRange docRange) {
		this.docRange = docRange;
	}
	
	public final DocRange getDocRange() {
		return docRange;
	}

	public abstract String getSummary();
	
	@Override
	public final String toString() {
		return getSummary() + ": " + docRange;
	}
	
	public boolean isEndOfDoc() {
		return false;
	}
	
	public boolean isNotEndOfDoc() {
		return !isEndOfDoc();
	}
	
	public boolean isElement() {
		return false;
	}
	
	public boolean isCharacter() {
		return false;
	}
	
	public static SaxEvent toSaxEvent(final Object object) {
		return (SaxEvent)object;
	}
}
