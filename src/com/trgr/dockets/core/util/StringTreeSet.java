/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.util;

import java.util.TreeSet;

public final class StringTreeSet extends TreeSet<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StringTreeSet(final String... strings) {
		for(final String string: strings) {
			add(string);
		}
	}
	
	public final void toBuffer(final IndentableStrBuilder b, final String header) {
		b.appendSection(header,new Runnable() {
			@Override
			public void run() {
				for (final String string: StringTreeSet.this) {
					b.appendLines(string);
				}
			}
		});
	}
}
