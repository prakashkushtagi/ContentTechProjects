/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

public class Holder<T> implements ConsumerJ8<T>, SupplierJ8<T> {

	private T value;
	
	@Override
	public final void accept(final T val) {
		value = val;
	}
	
	@Override
	public final T get() {
		return value;
	} 

}
