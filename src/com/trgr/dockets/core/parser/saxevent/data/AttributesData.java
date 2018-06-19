/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.data;

import java.util.TreeMap;

import org.xml.sax.Attributes;

import com.trgr.dockets.core.parser.saxevent.events.StartElementEvent;
import com.trgr.dockets.core.util.IndentableStrBuilder;
import com.trgr.dockets.core.util.StringBufferable;

public final class AttributesData extends TreeMap<String,String> implements StringBufferable {
	
	private static final long serialVersionUID = 1L;
	public static AttributesData EMPTY_ATTRIBUTES_DATA = new AttributesData();

	private AttributesData() {}

	public AttributesData(final StartElementEvent startEvent) {
		final Attributes attributes = startEvent.getAtts();
		final int size = attributes.getLength();
		for (int index = 0; index < size; index++) {
			put(attributes.getQName(index),attributes.getValue(index));
		}
	}
	
	@Override
	public final void toBuffer(final IndentableStrBuilder b) {
		if (isEmpty()) {
			return;
		}
		b.appendSection("Attributes",new Runnable() {
			@Override
			public void run() {
				for (final String key: keySet()) {
					b.appendLabeledValue(key, get(key));
				}
			}
		}); 
	}
}
