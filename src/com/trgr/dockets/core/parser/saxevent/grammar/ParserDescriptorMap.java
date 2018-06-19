/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent.grammar;

import java.util.TreeMap;

import com.trgr.dockets.core.util.IndentableStrBuilder;

public final class ParserDescriptorMap extends TreeMap<String,ParserDescriptor<?>> {
	
	private static final long serialVersionUID = 1L;
	private final ParserDescriptors orderedList = new ParserDescriptors();
	
	public ParserDescriptorMap(final ParserDescriptor<?>... descriptors) {
		putNewDescriptors(descriptors);
	}

	public final void putNewDescriptor(final ParserDescriptor<?> descriptor) {
		final String name = descriptor.getName();
		if ((name == null) || containsKey(name)) {
			throw new RuntimeException();
		}
		put(name,descriptor);
		orderedList.add(descriptor);
	}
	
	public final void putNewDescriptors(final ParserDescriptor<?>... descriptors) {
		for (final ParserDescriptor<?> descriptor: descriptors) {
			putNewDescriptor(descriptor);
		}
	}
	
	@Override
	public final String toString() {
		final IndentableStrBuilder b = new IndentableStrBuilder();
		b.appendSection("Grammar", new Runnable() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				for (final ParserDescriptor<?> descriptor: orderedList) {
					b.appendLines(descriptor.getGrammarEntryString());
				}
			}
		}); 
		return b.toString();
	}
}
