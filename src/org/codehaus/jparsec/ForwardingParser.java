/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package org.codehaus.jparsec;

import com.trgr.dockets.core.util.ConsumerJ8;

public final class ForwardingParser<T> extends NamedParser<T> {
	
	private final Parser<T> baseParser;
	
	private final ConsumerJ8<T> consumer;
	
	public ForwardingParser(final String name, final Parser<T> baseParser, final ConsumerJ8<T> consumer) {
		super(name);
		this.baseParser = baseParser;
		this.consumer = consumer;
	}
	
	public static <T> ForwardingParser<T> forward(
			final String name, final Parser<T> baseParser, final ConsumerJ8<T> consumer) {
		return new ForwardingParser<T>(name,baseParser,consumer);
	}
	
	public static <T> ForwardingParser<T> debug(final String label, final Parser<T> baseParser) {
		return forward(label,baseParser,new ConsumerJ8<T>() {
			@Override
			public void accept(final T x) {
				System.out.println(((label == null) ? "" : label + ": ") + x);
			}
		});
	}
	
	public static <T> ForwardingParser<T> debug(final Parser<T> baseParser) {
		return debug(null,baseParser);
	}

	@SuppressWarnings("unchecked")
	@Override
	final boolean apply(final ParseContext ctxt) {
		final boolean successful = baseParser.apply(ctxt);
		if (!successful) {
			return false;
		}
		consumer.accept((T)ctxt.result);
		return true;
	}
}
