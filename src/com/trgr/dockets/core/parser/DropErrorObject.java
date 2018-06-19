package com.trgr.dockets.core.parser;

public final class DropErrorObject implements IDropObject {
	
	private final Exception fException;

	public DropErrorObject(final Exception exception) {
		fException = exception;
	}
	
	public final Exception getException() {
		return fException;
	}
}
