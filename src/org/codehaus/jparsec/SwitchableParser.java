package org.codehaus.jparsec;

import com.trgr.dockets.core.util.SupplierJ8;

public final class SwitchableParser<T> extends Parser<T> {
	
	private final String name;
	
	private final SupplierJ8<Parser<T>> parserSupplier;
	
	public SwitchableParser(final String name, final SupplierJ8<Parser<T>> parserSupplier) {
		this.name = name;
		this.parserSupplier = parserSupplier;
	}

	@Override
	public final boolean apply(final ParseContext ctxt) {
		return parserSupplier.get().apply(ctxt);
	}

	@Override
	public final String toString() {
		return "SwitchableParser: " + name;
	}
}
