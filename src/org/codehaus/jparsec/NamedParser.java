/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package org.codehaus.jparsec;

public abstract class NamedParser<T> extends Parser<T> {

	/**
	 * For use when running the debugger, since there are typically many parsers in the call stack and they can be
	 * difficult to tell apart.
	 */
	private final String name;
	
	protected NamedParser(final String name) {
		this.name = name;
	}
	
	public final String getName() {
		return name;
	}
}
