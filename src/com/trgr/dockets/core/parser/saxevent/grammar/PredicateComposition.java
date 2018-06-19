/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.grammar;

public final class PredicateComposition extends ParserComposition {
	
	private final String name;
	
	private final ParserComposition base;
	
	public PredicateComposition(final String name, final ParserComposition base) {
		this.name = name;
		this.base = base;
	}

	@Override
	public final String toString() {
		return base + "[" + name + "]";
	}

}
