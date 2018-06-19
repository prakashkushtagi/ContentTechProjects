/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public final class Strings extends ArrayList<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Strings(final String... strings) {
		for(final String string: strings) {
			if (string != null) {
				add(string);
			}
		}
	}
	
	public Strings(final List<String> strings) {
		super(strings);
	}
	
	public final void addAllNonBlank(final Collection<String> sources) {
		for (final String source: sources)
			if (StringUtils.isNotBlank(source))
				add(source);
	}
	
	public String join(String separator) {
		return StringUtils.join(this, separator);
	}
	
	public String join() {
		return join("");
	}
	
	public final void toBuffer(final IndentableStrBuilder b, final String header) {
		b.appendSection(header,new Runnable() {
			@Override
			public void run() {
				for (final String string: Strings.this) {
					b.appendLines(string);
				}
			}
		});
	}
	
	public final String[] toStringArray() {
		return toArray(new String[size()]);
	}

	public final String getSpaceSeparatedContent() {
		final StringBuilder b = new StringBuilder();
		for (final String seg: this) {
			if (!(b.length() == 0)) b.append(" ");
			b.append(seg);
		}
		return b.toString();
	}
	
	public static String getSpaceSeparatedContent(final String... strings) {
		return new Strings(strings).getSpaceSeparatedContent();
	}
}
