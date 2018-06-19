/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

public interface ExceptionConsumer extends ConsumerJ8<Exception> {

	public static final ExceptionConsumer NULL_EXCEPTION_CONSUMER = null;
}
