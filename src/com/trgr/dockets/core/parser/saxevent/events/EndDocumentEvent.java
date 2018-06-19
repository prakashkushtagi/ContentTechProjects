/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.events;

import com.trgr.dockets.core.parser.saxevent.DocRange;

public final class EndDocumentEvent extends SaxEvent {
	
	public static final String END_DOCUMENT_SUMMARY = "End of Document";
	
	public EndDocumentEvent(final DocRange docRange) {
		super(docRange);
	}

	@Override public final String getSummary() {return END_DOCUMENT_SUMMARY;}
	
	@Override public final boolean isEndOfDoc() {return true;}
}
