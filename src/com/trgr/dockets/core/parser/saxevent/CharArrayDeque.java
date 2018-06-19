// Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
package com.trgr.dockets.core.parser.saxevent;

import static java.lang.System.arraycopy;

import java.util.ArrayDeque;

/**
 * Provides the logical functionality of a char deque, but for the sake of time and memory performance, increases the
 * granularity through the use of char array segments of a specified size.
 * 
 * A logical char sequence is represented that can span over multiple char array segments.
 * 
 * The logical char sequence represents a window into a larger char sequence that is being traversed from beginning to
 * end.
 *
 */
public class CharArrayDeque extends ArrayDeque<char[]> {
	
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_CHARSET_NAME = "UTF-8";
	
	/**
	 * The size of each char array.
	 */
	private final int segmentSize;
	
	/**
	 * The position in the first segment of the start of the logical char sequence. 
	 */
	private int firstSegmentCharPosition;
	
	/**
	 * The position in the last segment following the end of the logical char sequence.
	 */
	private int lastSegmentCharPosition;
	
	/**
	 * The number of segments that the window of interest has passed.
	 */
	private long deletedSegmentCount = 0;
	
	public CharArrayDeque(final int segmentSize) {
		this.segmentSize = segmentSize;
		resetFirstSegmentCharPosition();
		setLastSegmentCharPosition(segmentSize);
	}
	
	private void setFirstSegmentCharPosition(final int position) {
		firstSegmentCharPosition = position;
	}
	
	private void resetFirstSegmentCharPosition() {
		setFirstSegmentCharPosition(0);
	}
	
	private void setLastSegmentCharPosition(final int position) {
		lastSegmentCharPosition = position;
	}
	
	public final void appendCharArray(final char[] charArray, final int start, final int length) {
		final int end = start + length;
		int charArrayPosition = start;
		while (true) {
			final int lastSegmentSpaceRemaining = segmentSize - lastSegmentCharPosition;
			if (lastSegmentSpaceRemaining == 0) {
				add(new char[segmentSize]);
				setLastSegmentCharPosition(0);
				continue;
			}
			final int charArrayLengthRemaining = end - charArrayPosition;
			if (charArrayLengthRemaining > lastSegmentSpaceRemaining) {
				appendCharArrayInOneSegment(charArray,charArrayPosition,lastSegmentSpaceRemaining);
				charArrayPosition += lastSegmentSpaceRemaining;
				continue;
			}
			appendCharArrayInOneSegment(charArray,charArrayPosition,charArrayLengthRemaining);
			break;
		}
	}
	
	private void appendCharArrayInOneSegment(final char[] charArray, final int start, final int length) {
		arraycopy(charArray,start,getLast(),lastSegmentCharPosition,length);
		setLastSegmentCharPosition(lastSegmentCharPosition + length);
	}
	
	public final synchronized void deleteToCharPosition(final long charPosition) {
		readAndDeleteToCharPosition(charPosition,false);
	}
	
	public final synchronized String readAndDeleteToCharPosition(final long charPosition) {
		return readAndDeleteToCharPosition(charPosition,true);
	}

	/**
	 * Advance the window of interest, deleting the segments that are being passed by.
	 * 
	 * If recording is on (doRead == true), return as a String the bytes that are being passed by.
	 */
	private String readAndDeleteToCharPosition(final long charPosition, final boolean doRead) {
		final StringBuilder b = doRead ? new StringBuilder() : null;
		while (true) {
			final long deletedSegmentByteSize = deletedSegmentCount * segmentSize;
			final long firstSegmentEndPosition = deletedSegmentByteSize + segmentSize;
			if (firstSegmentEndPosition <= charPosition) {
				read(b,segmentSize - firstSegmentCharPosition);
				remove();
				deletedSegmentCount++;
				resetFirstSegmentCharPosition();
				continue;
			}
			final int newFirstSegmentBytePosition = (int)(charPosition - deletedSegmentByteSize);
			read(b,newFirstSegmentBytePosition - firstSegmentCharPosition);
			setFirstSegmentCharPosition(newFirstSegmentBytePosition);
			break;
		}
		return doRead ? b.toString() : null;
	}
	
	/**
	 * If recording is on, b will be non-null and will be given the next series of bytes of the specified length. 
	 */
	private void read(final StringBuilder b, final int length) {
		if ((b != null) && !isEmpty()) {
			b.append(new String(getFirst(),firstSegmentCharPosition,length));
		}
	}
	
	public final synchronized long getNextCharPosition() {
		return ((deletedSegmentCount + size() - 1) * segmentSize) + lastSegmentCharPosition;
	}
}
