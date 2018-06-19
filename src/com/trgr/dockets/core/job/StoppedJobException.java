package com.trgr.dockets.core.job;

public final class StoppedJobException extends Exception {

	public StoppedJobException() {
		super();
	}

	public StoppedJobException(final String message) {
		super(message);
	}
}
