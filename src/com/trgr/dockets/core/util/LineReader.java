/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import java.util.regex.Pattern;

public final class LineReader {
	
	private final String[] lines;

	public LineReader(final String string) {
		lines = string.split(Pattern.quote("\n"));
	}
	
	public final String getValue(final int index) {
		final String value = lines[index].trim();
		return value.isEmpty() ? null : value;
	}
}
