/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

// For readable RegEx code
public final class RegExUtil {
	
	private static final String STR_PERIOD = ".";
	
	public static final String RE_ANY_CHARACTER = STR_PERIOD;
	public static final String RE_ESCAPE = "\\";
	public static final String RE_OPTIONAL = "?";
	public static final String RE_OR = "|";
	public static final String RE_WORD_BOUNDARY = "\\b";
	
	public static final String RE_PERIOD = escape(STR_PERIOD);
	public static final String RE_OPTIONAL_PERIOD = optional(RE_PERIOD);
	
	public static String escape(final String x) {
		return RE_ESCAPE + x;
	}
	
	public static String optional(final String x) {
		return x + RE_OPTIONAL;
	}

	public static String or(final String x, final String y) {
		return x + RE_OR + y;
	}

	public static String orG(final String x, final String y) {
		return grouped(or(x,y));
	}
	
	public static String grouped(final String x) {
		return "(" + x + ")";
	}
	
	public static String wordSegment(
			final String segment,
			final boolean mustBeAtBeginningOfWord,
			final boolean mustBeAtEndOfWord,
			final boolean optionallyFollowedByPeriod,
			final boolean eachLetterOptionallyFollowedByPeriod) {
		final StringBuilder b = new StringBuilder();
		if (mustBeAtBeginningOfWord) {
			b.append(RE_WORD_BOUNDARY);
		}
		if (eachLetterOptionallyFollowedByPeriod) {
			for (int i = 0; i < segment.length(); i++) {
				b.append(segment.charAt(i));
				b.append(RE_OPTIONAL_PERIOD);
			}
			
		}
		else {
			b.append(segment);
			if (optionallyFollowedByPeriod) {
				b.append(RE_OPTIONAL_PERIOD);
			}
		}
		if (mustBeAtEndOfWord) {
			b.append(RE_WORD_BOUNDARY);
		}
		return b.toString();
	}
	
	public static void appendWordSegmentAlternative(
			final StringBuilder b,
			final String segment,
			final boolean mustBeAtBeginningOfWord,
			final boolean mustBeAtEndOfWord,
			final boolean optionallyFollowedByPeriod,
			final boolean eachLetterOptionallyFollowedByPeriod) {
		if (!(b.length() == 0)) {
			b.append(RE_OR);
		}
		b.append(wordSegment(segment,mustBeAtBeginningOfWord,mustBeAtEndOfWord,
				optionallyFollowedByPeriod,eachLetterOptionallyFollowedByPeriod));
	}
	
	public static void appendInfixAlt(final StringBuilder b, final String segment) {
		appendWordSegmentAlternative(b,segment,false,false,false,false);
	}
	
	public static void appendInfixAbbrevAlt(final StringBuilder b, final String segment) {
		appendWordSegmentAlternative(b,segment,false,false,true,false);
	}
	
	public static void appendSuffixAbbrevAlt(final StringBuilder b, final String segment) {
		appendWordSegmentAlternative(b,segment,false,true,true,false);
	}
	
	public static void appendWordAlt(final StringBuilder b, final String segment) {
		appendWordSegmentAlternative(b,segment,true,true,false,false);
	}
	
	public static void appendAbbrevAlt(final StringBuilder b, final String segment) {
		appendWordSegmentAlternative(b,segment,true,true,true,false);
	}
	
	public static void appendInitialsAlt(final StringBuilder b, final String segment) {
		appendWordSegmentAlternative(b,segment,true,true,true,true);
	}
	
	public static void appendInfixInitialsAlt(final StringBuilder b, final String segment) {
		appendWordSegmentAlternative(b,segment,false,false,true,true);
	}
}
