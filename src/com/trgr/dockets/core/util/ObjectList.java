/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.util.ArrayList;

public final class ObjectList extends ArrayList<Object> implements StringBufferable {
	
	private static final long serialVersionUID = 1L;

	public ObjectList(final Object... objects) {
		for (final Object object: objects) {
			add(object);
		}
	}
	
	@Override
	public final void toBuffer(final IndentableStrBuilder b) {
		b.appendSection("Object List", new Runnable() {
			@Override
			public void run() {
				for (final Object object: ObjectList.this) {
					if (object instanceof StringBufferable) {
						((StringBufferable)object).toBuffer(b);
					}
					else {
						b.appendLines(object);
					}
				}
			}
		});
	}
}
