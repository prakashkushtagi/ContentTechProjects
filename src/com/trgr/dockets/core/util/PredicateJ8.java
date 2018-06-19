/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

// Stand-in for java.util.function.Predicate in Java 8
public interface PredicateJ8<T> {
    boolean test(T t);
}
