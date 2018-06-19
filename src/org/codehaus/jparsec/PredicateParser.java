/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package org.codehaus.jparsec;

import com.trgr.dockets.core.util.PredicateJ8;

public final class PredicateParser<T> extends NamedParser<T> {
	
	private final Parser<T> fBase;
	
	private final PredicateJ8<T> fPredicate;

	public PredicateParser(final String name, final Parser<T> base, final PredicateJ8<T> predicate) {
		super(name);
		fBase = base;
		fPredicate = predicate;}

	@SuppressWarnings("unchecked")
	@Override boolean apply(final ParseContext ctxt) {
		final boolean baseResult = fBase.apply(ctxt);
		if (!baseResult) return false;
		final boolean testResult = fPredicate.test((T)ctxt.result);
		if (!testResult) ctxt.setAt(ctxt.step - 1, ctxt.at - 1);
		return baseResult && testResult;}
	
	public static <T> Parser<T> predicateParser(
			final String name, final Parser<T> base, final PredicateJ8<T> predicate) {
		return new PredicateParser<T>(name,base,predicate);}
}
