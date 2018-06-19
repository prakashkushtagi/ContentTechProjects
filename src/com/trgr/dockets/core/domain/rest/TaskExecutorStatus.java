package com.trgr.dockets.core.domain.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/**
 * The marshalled XML response to a REST request for the state of the 
 * ThreadPoolTaskExecutor in a Spring Batch engine.
 * See the property defintions for a TaskExecution to understand what the properties (same names here) are.
 */
@XmlRootElement(name="taskExecutorStatus") 
public class TaskExecutorStatus {
	
	private int activeCount;
	private int corePoolSize;
	private int poolSize;
	
	public TaskExecutorStatus() {
		super();
	}

	public TaskExecutorStatus(final ThreadPoolTaskExecutor taskExecutor) {
		setActiveCount(taskExecutor.getActiveCount());
		setCorePoolSize(taskExecutor.getCorePoolSize());
		setPoolSize(taskExecutor.getPoolSize());
	}
	@XmlElement(name="activeCount")
	public int getActiveCount() {
		return activeCount;
	}
	@XmlElement(name="corePoolSize")
	public int getCorePoolSize() {
		return corePoolSize;
	}
	@XmlElement(name="poolSize")
	public int getPoolSize() {
		return poolSize;
	}
	public void setActiveCount(int activeCount) {
		this.activeCount = activeCount;
	}
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
