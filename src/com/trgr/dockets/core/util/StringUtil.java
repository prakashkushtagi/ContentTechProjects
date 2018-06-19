/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

public final class StringUtil {

	public static String concat(final String separator, final boolean skipNull, final Object... objects) {
		final StringBuilder b = new StringBuilder();
		String sep = "";
		for (final Object object: objects) {
			if (skipNull && (object == null)) {
				continue;
			}
			b.append(sep);
			sep = separator;
			b.append((object == null) ? "" : object);
		}
		return b.toString();
	}

	public static String concat(final String separator, final Object... objects) {
		return concat(separator,true,objects);
	}
	
	public static String spaceConcat(final Object... objects) {
		return concat(" ",objects);
	}
	
	public static String colonSpaceConcat(final Object... objects) {
		return concat(": ",objects);
	}

	public static String commaConcat(final Object... objects) {
		return concat(",",objects);
	}

	public static String commaSpaceConcat(final Object... objects) {
		return concat(", ",objects);
	}
	
	public static String linefeedConcat(final Object... objects) {
		return concat("\n",false,objects);
	}
	
	public static String semicolonSpaceConcat(final Object... objects) {
		return concat("; ",objects);
	}
	
	public static String normalizeApostropheAndEscapeXml(final String s) {
		return StringEscapeUtils.escapeXml(s.replaceAll(Pattern.quote("\u2019"),"'"));
	}
}
