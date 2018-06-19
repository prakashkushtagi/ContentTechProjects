/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import com.trgr.dockets.core.util.OffsetIndex;

/**
 * Maps from a line number to the byte position of the beginning of that line.
 * 
 * Line numbering starts at 1, whereas the first index of an ArrayList is 0, so the line index is 1 less than the line
 * number.
 * 
 * This is designed for use with texts that are very long and where the window of interest in the text starts at the
 * beginning and advances toward the end. Therefore, index entries for lines before the window of interest are no longer
 * needed and are removed in order to save space.
 */
public class LinePositionIndex extends OffsetIndex<Long> {
	
	private static final long serialVersionUID = 1L;

	public LinePositionIndex() {
		super(1000,10000);
		add(0L);
	}
	
	/**
	 * Remove index entries that are no longer needed due to the window of interest advancing.
	 */
	public final void deleteToLineNumber(final long lineNumber) {
		deleteToEntryNumber(getLineIndex(lineNumber));
	}
	
	public final long getCharPosition(final DocLocation docLocation) {
		return getEntry(getLineIndex(docLocation.getLineNumber())) + docLocation.getColumnNumber() - 1;
	}
	
	private static long getLineIndex(final long lineNumber) {
		return lineNumber - 1;
	}
}
