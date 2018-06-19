/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.events;

import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.CLASS;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.HREF;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import com.trgr.dockets.core.parser.saxevent.DocRange;

public final class StartElementEvent extends ElementEvent {
	
	private final Attributes fAtts;

	public StartElementEvent(
			final DocRange docRange,
			final String uri,
			final String localName,
			final String qName,
			final Attributes atts) {
		super(docRange,uri,localName,qName);
		
		/*
		 * It is important to make a copy of atts instead of just storing it directly, since what it refers to via the
		 * proxy can change when the next start element is read.
		 * 
		 * The AttributesImpl constructor used here makes a copy of the data.
		 */
		fAtts = new AttributesImpl(atts);}

	@Override public final boolean isStart() {return true;}
	
	public final Attributes getAtts() {return fAtts;}
	
	public final boolean hasAttribute(final String qName) {return fAtts.getIndex(qName) > -1;}
	
	public final String getAttributeValue(final String qName) {return fAtts.getValue(qName);}
	
	public final String getClassAttr() {return getAttributeValue(CLASS);}
	
	public final String getHRef() {return getAttributeValue(HREF);}
}
