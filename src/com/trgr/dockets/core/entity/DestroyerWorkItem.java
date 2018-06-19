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

@Entity
@Table(name="DESTROYER_WORK", schema="DOCKETS_PREPROCESSOR")
public class DestroyerWorkItem {
	
	private Long primaryKey;
	/** Used to correlate all the sub-batch jobs together. */
	private UUID deleteRequestUuid;
	/** The sub-batch of the Uber-Batch, created by the splitter component. */
	private String batchId;
	/** Number of dockets in the batch. */
	private long batchSize;	
	private Long jobInstanceId;		// from Spring Batch job start
	private Long jobExecutionId;	// from Spring Batch job start
	private Date createTime;		// Time row added to table
	private long priority;			// Higher the number, the more important
	private Date startTime;
	private String serverName;
	private String client;
	
	public DestroyerWorkItem() {
		super();
	}
	public DestroyerWorkItem(UUID pubReqUuid, String batchId, long batchSize, long priority) {
		setDeleteRequestUuid(pubReqUuid);
		setBatchId(batchId);
		setBatchSize(batchSize);
		setCreateTime(new Date());
		setPriority(priority);
	}

	@Id
	@Column(name="WORK_ID", nullable=false)
	@GeneratedValue(generator = "dispatcherWorkSeq")
	@SequenceGenerator(name = "dispatcherWorkSeq", sequenceName = "DOCKETS_PREPROCESSOR.DISPATCHER_WORK_SEQ")
	public Long getPrimaryKey() {
		return primaryKey;
	}

	@Column(name="REQUEST_ID", nullable=false)
	public String getDeleteRequestId() {
		return (deleteRequestUuid != null) ? deleteRequestUuid.toString() : null;
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
	public long getPriority() {
		return priority;
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
	public UUID getDeleteRequestUuid() {
		return deleteRequestUuid;
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
	public void setDeleteRequestId(String delReqId) throws Exception {
		setDeleteRequestUuid(new UUID(delReqId));
	}
	protected void setDeleteRequestUuid(UUID pubReqUuid) {
		this.deleteRequestUuid = pubReqUuid;
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
	protected void setPriority(long priority) {
		this.priority = priority;
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
