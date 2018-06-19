package com.trgr.dockets.core.parser;

public final class DropObject<T> implements IDropObject {

	private final T fObject;
	
	public DropObject(final T object) {
		fObject = object;
	}
	
	public final T getObject() {
		return fObject;
	}
}
