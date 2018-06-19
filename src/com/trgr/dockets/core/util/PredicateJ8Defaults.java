/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

// Stand-in for java.util.function.Predicate default methods in Java 8
public final class PredicateJ8Defaults {

	public static <T> PredicateJ8<T> negate(final PredicateJ8<T> original) {
		return new PredicateJ8<T>() {

			@Override
			public boolean test(final T t) {
				return !original.test(t);
			}
		};
	}
}
