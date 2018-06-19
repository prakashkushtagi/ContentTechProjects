/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.util;

/**
 *
 * Stand in for Java 8 java.util.funtion.Function
 */
public interface FunctionJ8<T,R> {

	R apply(T t);
}
