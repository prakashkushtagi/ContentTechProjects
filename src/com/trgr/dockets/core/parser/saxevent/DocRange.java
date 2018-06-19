/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

public class DocRange {

	private final DocLocation start;
	
	private final DocLocation end;
	
	public DocRange(final DocLocation start, final DocLocation end) {
		this.start = start;
		this.end = end;
	}
	
	public final DocLocation getStart() {
		return start;
	}
	
	public final DocLocation getEnd() {
		return end;
	}
	
	@Override
	public final String toString() {
		return start + "-" + end;
	}
}
