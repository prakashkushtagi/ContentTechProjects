package com.trgr.dockets.core.parser.saxevent;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.trgr.dockets.core.parser.Drop;
import com.trgr.dockets.core.parser.saxevent.events.SaxEvent;

public final class SaxEventDrop extends Drop<SaxEvent> implements SaxEventConsumer {
	
	private MutableBoolean completionStatus;
	
	public final boolean isNotCompleted() {
		return completionStatus.isFalse();
	}
	
	public final void setCompletionStatus(final MutableBoolean status) {
		completionStatus = status;
	}

	@Override
	protected boolean isEndOfInput() {
		return getCurrentObject().isEndOfDoc();
	}
}
