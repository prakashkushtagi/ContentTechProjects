package com.trgr.dockets.core.job;

import static org.apache.log4j.Logger.getLogger;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.log4j.Logger;

public final class StopRequestMonitor {

	private static final Logger log = getLogger(StopRequestMonitor.class);
	
	private final MutableBoolean requestedToStop = new MutableBoolean(false);
	
	public final void reset() {
		requestedToStop.setFalse();
	}
	
	public final boolean isRequestedToStop() {
		return requestedToStop.booleanValue();
	}
	
	public final void setRequestedToStop() {
		requestedToStop.setTrue();
		log.warn("Received request to stop job.");
	}
	
	public final void stopJobIfRequested(final String message) throws StoppedJobException {
		if (isRequestedToStop()) {
			log.warn("Stopping job...");
			throw new StoppedJobException(message);
		}
	}
	
	public final void stopJobIfRequested() throws StoppedJobException {
		stopJobIfRequested(null);
	}
}
