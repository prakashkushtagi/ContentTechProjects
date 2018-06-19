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
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Entity
@Table(name="LMS_WORK", schema="DOCKETS_PREPROCESSOR")
public class LmsWorkItem {
	
	private Long primaryKey;
	private Date createTime;			// Time row was added to table	
	private UUID publishingRequestUuid; // Used to correlate all the ltc messages together. //
	private String messageId;			// The message ID of load monitor ltc message
	private String productCode;			// The product code for this work item
	private Date startTime;				// Time job started its execution 
	private String serverName;			// The server which picked up the job
	private Long jobInstanceId;			// from Spring Batch job start
	private Long jobExecutionId;		// from Spring Batch job start
	
	public LmsWorkItem() {
		super();
	}
	
	public LmsWorkItem(UUID pubReqUuid, String messageId, String productCode) {
		setPublishingRequestUuid(pubReqUuid);
		setMessageId(messageId);
		setCreateTime(new Date());
		setProductCode(productCode);
	}

	@Id
	@Column(name="WORK_ID", nullable=false)
	@GeneratedValue(generator = "lmsWorkSeq")
	@SequenceGenerator(name = "lmsWorkSeq", sequenceName = "DOCKETS_PREPROCESSOR.LMS_WORK_SEQ")
	public Long getPrimaryKey() {
		return primaryKey;
	}

	@Column(name="CREATE_TIME", nullable=false)
	public Date getCreateTime() {
		return createTime;
	}
	
	@Column(name="REQUEST_ID", nullable=false)
	public String getPublishingRequestId() {
		return (publishingRequestUuid != null) ? publishingRequestUuid.toString() : null;
	}
	
	@Column(name="MESSAGE_ID", nullable=false)
	public String getMessageId() {
		return messageId;
	}
	
	@Column(name="PRODUCT_CODE", nullable=false)
	public String getProductCode() {
		return productCode;
	}

	@Column(name="START_TIME")
	public Date getStartTime() {
		return startTime;
	}
	
	@Column(name="SERVER_NAME")
	public String getServerName() {
		return serverName;
	}
	
	@Column(name="JOB_INSTANCE_ID")
	public Long getJobInstanceId() {
		return jobInstanceId;
	}

	@Column(name="JOB_EXECUTION_ID")
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
	
	@Transient
	public UUID getPublishingRequestUuid() {
		return publishingRequestUuid;
	}
	
	@Transient
	public boolean isJobStarted() {
		return (startTime != null);
	}
	
	public void setPrimaryKey(Long pk) {
		this.primaryKey = pk;
	}
	
	protected void setCreateTime(Date create) {
		this.createTime = create;
	}
	
	public void setPublishingRequestId(String pubReqId) throws NumberFormatException, UUIDException {
		setPublishingRequestUuid(new UUID(pubReqId));
	}
	
	protected void setPublishingRequestUuid(UUID pubReqUuid) {
		this.publishingRequestUuid = pubReqUuid;
	}
	
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public void setStartTime(Date start) {
		this.startTime = start;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public void setJobInstanceId(Long id) {
		this.jobInstanceId = id;
	}
	
	public void setJobExecutionId(Long id) {
		this.jobExecutionId = id;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}