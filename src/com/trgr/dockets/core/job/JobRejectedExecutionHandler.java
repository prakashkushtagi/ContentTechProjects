package com.trgr.dockets.core.job;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public final class JobRejectedExecutionHandler implements RejectedExecutionHandler {
	
	@Override
	public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
		throw new RuntimeException(String.format(
			"Start of job was rejected by task executor because the current active thread count is %d, " +
			"and starting a new thread would execeed the configured maximum thread pool size of %d",
			executor.getActiveCount(),
			executor.getMaximumPoolSize()));
	}
}
