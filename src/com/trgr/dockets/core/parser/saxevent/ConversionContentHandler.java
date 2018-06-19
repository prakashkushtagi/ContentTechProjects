/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.trgr.dockets.core.parser.saxevent.events.CharacterEvent;
import com.trgr.dockets.core.parser.saxevent.events.EndDocumentEvent;
import com.trgr.dockets.core.parser.saxevent.events.EndElementEvent;
import com.trgr.dockets.core.parser.saxevent.events.SaxEvent;
import com.trgr.dockets.core.parser.saxevent.events.StartElementEvent;

public final class ConversionContentHandler implements ContentHandler {
	
	private final SaxEventConsumer fEventConsumer;
	private void fireEvent(final SaxEvent event) {fEventConsumer.accept(event);}
	
	private Locator fLocator;
	
	private DocRange fDocRange = new DocRange(null,new DocLocation(0,0));
	
	public ConversionContentHandler(final SaxEventConsumer eventConsumer) {fEventConsumer = eventConsumer;}

	@Override
	public final void setDocumentLocator(final Locator locator) {
		fLocator = locator;
	}
	
	private void updateLocation() {
		fDocRange = new DocRange(
				fDocRange.getEnd(),new DocLocation(fLocator.getLineNumber(),fLocator.getColumnNumber()));
	}

	@Override
	public final void startDocument() throws SAXException {
		updateLocation();
	}

	@Override
	public final void endDocument() throws SAXException {
		updateLocation();
		fireEvent(new EndDocumentEvent(fDocRange));
	}

	@Override
	public final void startPrefixMapping(final String prefix, final String uri) throws SAXException {
		updateLocation();
	}

	@Override
	public final void endPrefixMapping(final String prefix) throws SAXException {
		updateLocation();
	}

	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes atts)
			throws SAXException {
		updateLocation();
		fireEvent(new StartElementEvent(fDocRange,uri,localName,qName,atts));
	}

	@Override
	public final void endElement(final String uri, final String localName, final String qName) throws SAXException {
		updateLocation();
		fireEvent(new EndElementEvent(fDocRange,uri,localName,qName));
	}

	@Override
	public final void characters(final char[] ch, final int start, final int length) throws SAXException {
		updateLocation();
		fireEvent(new CharacterEvent(fDocRange,ch,start,length));
	}

	@Override
	public final void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
		updateLocation();
	}

	@Override
	public final void processingInstruction(final String target, final String data) throws SAXException {
		updateLocation();
	}

	@Override
	public final void skippedEntity(final String name) throws SAXException {
		updateLocation();
	}
}
