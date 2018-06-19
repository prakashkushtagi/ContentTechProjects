/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.data;

import java.util.ArrayList;
import java.util.List;

import com.trgr.dockets.core.util.IndentableStrBuilder;
import com.trgr.dockets.core.util.StringBufferable;

public final class XmlDataItems extends ArrayList<XmlData> implements StringBufferable {
	
	private static final long serialVersionUID = 1L;

	public XmlDataItems(final List<XmlData> source) {
		for (final XmlData data: source) {
			if (data.hasContent()) {
				add(data);
			}
		}
		trimToSize();
	}
	
	@Override
	public final void toBuffer(final IndentableStrBuilder b) {
		for (final XmlData item: this) {
			item.toBuffer(b);
		}
	}
	
	public final ElementData getElement(final int index) {
		int count = 0;
		final int size = size();
		for (int i = 0; i <= size; i++) {
			final XmlData data = get(i);
			if (!(data instanceof ElementData)) {
				continue;
			}
			if (count == index) {
				return (ElementData)data;
			}
			count++;
		}
		return null;
	}
	
	public final String getString(final int index) {
		int count = 0;
		final int size = size();
		StringBuilder b = null;
		for (int i = 0; i <= size; i++) {
			final XmlData data = get(i);
			if (!(data instanceof CharData)) {
				if (count == index) {
					return b.toString();
				}
				count++;
				continue;
			}
			if (count == index) {
				if (b == null) {
					b = new StringBuilder();
				}
				else {
					b.append(" ");
				}
				b.append(((CharData)data).getString());
			}
		}
		return b == null ? null : b.toString();
	}
	
	public final ElementData trimToFirstElement() {
		while (!isEmpty()) {
			final XmlData data = get(0);
			if (data instanceof ElementData) {
				return (ElementData)data;
			}
			removeFirst();
		}
		return null;
	}
	
	public final ElementData trimToAndRemoveFirstElement() {
		final ElementData element = trimToFirstElement();
		if (element == null) {
			return null;
		}
		removeFirst();
		return element;
	}
	
	public final void removeFirst() {
		remove(0);
	}
}
