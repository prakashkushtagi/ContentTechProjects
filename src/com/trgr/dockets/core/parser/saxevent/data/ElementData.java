/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.data;

import static com.trgr.dockets.core.parser.saxevent.data.AttributesData.EMPTY_ATTRIBUTES_DATA;

import com.trgr.dockets.core.parser.saxevent.events.EndElementEvent;
import com.trgr.dockets.core.util.IndentableStrBuilder;

public final class ElementData extends XmlData {
	
	private final String tag;
	
	private final AttributesData attributes;

	private final XmlDataItems dataItems;
	
	public ElementData(
			final AttributesData attributes, final XmlDataItems dataItems, final EndElementEvent endElement) {
		this.tag = endElement.getQName();
		this.attributes = attributes.isEmpty() ? EMPTY_ATTRIBUTES_DATA : attributes;
		this.dataItems = dataItems;
	}
	
	public final String getTag() {
		return tag;
	}
	
	public final AttributesData getAttributes() {
		return attributes;
	}
	
	public final XmlDataItems getDataItems() {
		return dataItems;
	}
	
	public final ElementData getSubelement(final int index) {
		return dataItems.getElement(index);
	}
	
	public final String getSubstring(final int index) {
		return dataItems.getString(index);
	}
	
	@Override
	public final String toString() {
		final IndentableStrBuilder b = new IndentableStrBuilder();
		toBuffer(b);
		return b.toString();
	}
	
	@Override
	public final void toBuffer(final IndentableStrBuilder b) {
		b.appendSection(tag,new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				attributes.toBuffer(b);
				dataItems.toBuffer(b);
			}
		}); 
	}
}
