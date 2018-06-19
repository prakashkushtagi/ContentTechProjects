/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.util;

import java.util.ArrayList;

/**
 * Index which can be very large but for which only the entries toward the end need to be kept.  
 */
public class OffsetIndex<T> extends ArrayList<T> {
	
	private static final long serialVersionUID = 1L;

	private final int minToRetain;
	
	private final int maxToRetain;
	
	public OffsetIndex(final int minToRetain, final int maxToRetain) {
		this.minToRetain = minToRetain;
		this.maxToRetain = maxToRetain;
	}

	/**
	 * The number of index entries that have been removed.
	 */
	private long previousEntryCount = 0;
	
	public final long getPreviousEntryCount() {
		return previousEntryCount;
	}
	
	public final void addAndAdjust(final T entry) {
		add(entry);
		final int size = size();
		if (size <= maxToRetain) return;
		deleteToEntryNumber(size + previousEntryCount - minToRetain);
	}
	
	/**
	 * Remove index entries that are no longer needed due to the window of interest advancing.
	 */
	public final void deleteToEntryNumber(final long entryNumber) {
		removeRange(0,getOffsetEntryNumber(entryNumber));
		previousEntryCount = entryNumber;
	}
	
	public final T getEntry(final long entryNumber) {
		return get(getOffsetEntryNumber(entryNumber));
	}
	
	private int getOffsetEntryNumber(final long entryNumber) {
		return (int)(entryNumber - previousEntryCount);
	}

	public final boolean entryNumberIsInRange(final long entryNumber) {
		return getOffsetEntryNumber(entryNumber) >= 0;
	}
}
