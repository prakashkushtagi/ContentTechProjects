package com.trgr.dockets.core.parser;

import java.util.concurrent.SynchronousQueue;

import com.trgr.dockets.core.util.ConsumerJ8;

public abstract class Drop<T> implements ConsumerJ8<T> {
	
	private SynchronousQueue<IDropObject> fQueue = new SynchronousQueue<IDropObject>();
	
	private IDropObject fObject;
	
	private boolean fEndOfInputReached = false;
	
	private boolean fTerminated = false;
	
	private Exception fTerminatingException;
	
	@SuppressWarnings("unchecked")
	public final T getCurrentObject() {
		return ((DropObject<T>)fObject).getObject();
	}
	
	public final T getNextObject() {
		if (isFinished()) {
			return null;
		}
		try {
			fObject = fQueue.take();
			if (fObject instanceof DropErrorObject) {
				return terminate(((DropErrorObject)fObject).getException());
			}
			fEndOfInputReached = isEndOfInput();
			return getCurrentObject();
		} catch (final InterruptedException e) {
			return terminate(e);
		}
	}

	@Override
	public final void accept(final T object) {
		if (isFinished()) {
			return;			
		}
		try {
			fQueue.put(new DropObject<T>(object));
		} catch (final InterruptedException e) {
			terminate(e);
		}
	}
	
	public final T terminate(final Exception exception) {
		fTerminated = true;
		
		// Make sure no threads get hung waiting for an event to be read
		fQueue.poll();
		
		// Make sure no threads get hung waiting for the next event
		fQueue.offer(new DropErrorObject(exception));
		
		fTerminatingException = exception;
		return null;
	}
	
	public final boolean isTerminated() {return fTerminated;}
	
	private boolean isFinished() {
		return fTerminated || fEndOfInputReached;
	}
	
	public final Exception getTerminatingException() {return fTerminatingException;}
	
	protected abstract boolean isEndOfInput();
}
