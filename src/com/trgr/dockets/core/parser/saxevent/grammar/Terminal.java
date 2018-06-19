/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.grammar;

public final class Terminal extends ParserComposition {
	
	public static Terminal TERMINAL = new Terminal();
	
	private Terminal() {}
	
	@Override
	public final String toString() {
		return "<<terminal>>";
	}
}
