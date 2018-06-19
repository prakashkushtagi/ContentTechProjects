/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.grammar;

import static com.trgr.dockets.core.parser.saxevent.DocketParsers.EVENT_PARSER;
import static com.trgr.dockets.core.parser.saxevent.grammar.Terminal.TERMINAL;

import com.trgr.dockets.core.parser.saxevent.events.CharacterEvent;
import com.trgr.dockets.core.parser.saxevent.events.SaxEvent;

public final class Grammar {
	
	private ParserDescriptor<SaxEvent> saxEventPD =
			new ParserDescriptor<SaxEvent>(this,"SaxEvent",SaxEvent.class,EVENT_PARSER,TERMINAL);
	
	private final ParserDescriptorMap descriptorMap = new ParserDescriptorMap(saxEventPD);
	
	private ParserDescriptor<CharacterEvent> charEventPD = saxEventSubclassPD(CharacterEvent.class);
	
	public Grammar() {
		putNewDescriptor(charEventPD.many());
	}
	
	public final void putNewDescriptor(final ParserDescriptor<?> descriptor) {
		descriptorMap.putNewDescriptor(descriptor);
	}
	
	private <T extends SaxEvent> ParserDescriptor<T> saxEventSubclassPD(final Class<T> cls) {
		return saxEventPD.instanceOfFilter(cls);
	}
	
	@Override
	public final String toString() {
		return descriptorMap.toString();
	}
}
