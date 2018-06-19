/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

public final class DocLocation {

	private final long lineNumber;
	
	private final int columnNumber;
	
	public DocLocation(final long lineNumber, final int columnNumber) {
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}
	
	public final long getLineNumber() {
		return lineNumber;
	}
	
	public final int getColumnNumber() {
		return columnNumber;
	}
	
	@Override
	public final String toString() {
		return "(" + lineNumber + "," + columnNumber + ")";
	}
}
