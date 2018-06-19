/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

// Stand-in for java.util.function Consumer in Java 8
public interface ConsumerJ8<T> {
	
    void accept(T t);
}
