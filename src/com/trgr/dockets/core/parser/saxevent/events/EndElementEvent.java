package com.trgr.dockets.core.parser.saxevent.events;

import com.trgr.dockets.core.parser.saxevent.DocRange;

public final class EndElementEvent extends ElementEvent {

	public EndElementEvent(final DocRange docRange, final String uri, final String localName, final String qName) {
		super(docRange,uri,localName,qName);
	}

	@Override public final boolean isStart() {return false;}
}
