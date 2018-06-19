/**Copyright 2015: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.westgroup.publishingservices.uuidgenerator.UUID;


/**
 * Represents docket processing work to be carried out by a DocketsBatch Spring Batch web app instance.
 * Properties batchId, courtId, batchSize, and createTime are populated by the splitter component when
 * creating rows in this table.  The instanceId and executionId are populated once the corresponding
 * Spring Batch job is launched for this work item.
 */
@Entity
@Table(name="DISPATCHER_WORK", schema="DOCKETS_PREPROCESSOR")
public class DispatcherWorkItem {
	
	private Long primaryKey;
	/** Used to correlate all the sub-batch jobs together. */
	private UUID publishingRequestUuid;
	/** The sub-batch of the Uber-Batch, created by the splitter component. */
	private String batchId;
	/** Number of dockets in the batch. */
	private long batchSize;	
	private Long jobInstanceId;		// from Spring Batch job start
	private Long jobExecutionId;	// from Spring Batch job start
	private Date createTime;		// Time row added to table
	private long publishingPriority;// Higher the number, the more important
	private Date startTime;		// 
	private String serverName;
	private String client;
	
	public DispatcherWorkItem() {
		super();
	}
	public DispatcherWorkItem(UUID pubReqUuid, String batchId, long batchSize, long publishingPriority) {
		setPublishingRequestUuid(pubReqUuid);
		setBatchId(batchId);
		setBatchSize(batchSize);
		setCreateTime(new Date());
		setPublishingPriority(publishingPriority);
	}

	@Id
	@Column(name="WORK_ID", nullable=false)
	@GeneratedValue(generator = "dispatcherWorkSeq")
	@SequenceGenerator(name = "dispatcherWorkSeq", sequenceName = "DOCKETS_PREPROCESSOR.DISPATCHER_WORK_SEQ")
	public Long getPrimaryKey() {
		return primaryKey;
	}

	@Column(name="REQUEST_ID", nullable=false)
	public String getPublishingRequestId() {
		return (publishingRequestUuid != null) ? publishingRequestUuid.toString() : null;
	}
	@Column(name="BATCH_ID", nullable=false)
	public String getBatchId() {
		return batchId;
	}
	@Column(name="BATCH_SIZE", nullable=false)
	public long getBatchSize() {
		return batchSize;
	}
	@Column(name="JOB_INSTANCE_ID")
	public Long getJobInstanceId() {
		return jobInstanceId;
	}
	@Column(name="JOB_EXECUTION_ID")
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
	@Column(name="CREATE_TIME", nullable=false)
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * Returns the item priority, higher means the jobs will be started before lower priority items.
	 */
	@Column(name="PRIORITY", nullable=false)
	public long getPublishingPriority() {
		return publishingPriority;
	}
	
	@Column(name="START_TIME")
	public Date getStartTime() {
		return startTime;
	}
	@Column(name="SERVER_NAME")
	public String getServerName() {
		return serverName;
	}
	
	@Transient
	public UUID getPublishingRequestUuid() {
		return publishingRequestUuid;
	}
	@Transient
	public boolean isJobStarted() {
		return (startTime != null);
	}
	@Transient
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public void setPublishingRequestId(String pubReqId) throws Exception {
		setPublishingRequestUuid(new UUID(pubReqId));
	}
	protected void setPublishingRequestUuid(UUID pubReqUuid) {
		this.publishingRequestUuid = pubReqUuid;
	}
	public void setBatchId(String id) {
		this.batchId = id;
	}
	public void setBatchSize(long size) {
		this.batchSize = size;
	}
	protected void setCreateTime(Date create) {
		this.createTime = create;
	}
	public void setJobInstanceId(Long id) {
		this.jobInstanceId = id;
	}
	public void setJobExecutionId(Long id) {
		this.jobExecutionId = id;
	}
	public void setPrimaryKey(Long pk) {
		this.primaryKey = pk;
	}
	protected void setPublishingPriority(long publishingPriority) {
		this.publishingPriority = publishingPriority;
	}
	public void setStartTime(Date start) {
		this.startTime = start;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
