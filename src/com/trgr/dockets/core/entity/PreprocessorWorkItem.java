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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;


/**
 * Work items picked up by the Preprocessor component from the PREPROCESSOR_WORK table.
 * Items are added to the table by either the Data Capture, or PBUI web app. clients.
 * Also serves as the semaphore to enforce the business rule that no two of the same court may be processing concurrently,
 * i.e. court UPSTATE processing must complete before another request from court UPSTATE begins unless the fifoOverride flag is
 * set to true by the client.
 */
@Entity
@Table(name="PREPROCESSOR_WORK", schema="DOCKETS_PREPROCESSOR")
public class PreprocessorWorkItem {
	
	/** The client from which the request came */
	public enum ClientEnum { DATA_CAPTURE, PBUI, ANY}
	
	private Long primaryKey;
	private ClientEnum client;
	private UUID publishingRequestUuid;
	private Court court;
	private String xml;
	private Date createTime;
	private Date startTime;
	/** True if the user wants to run a file simultaneously for the same court  **/
	private boolean fifoOverride;
	private long publishingPriority;
	private String serverName;
	private Long vendorId;
	
	public PreprocessorWorkItem() {
		super();
	}
	public PreprocessorWorkItem(ClientEnum client, UUID pubReqUuid, Court court, String xml, 
			boolean fifoOverride, long publishingPriority, Long vendorId) {
		setClient(client);
		setPublishingRequestUuid(pubReqUuid);
		setCourt(court);
		setXml(xml);
		setCreateTime(new Date());
		setFifoOverride(fifoOverride);
		setPublishingPriority(publishingPriority);
		setVendorId(vendorId);
	}

	@Id
	@Column(name="WORK_ID", nullable=false)
	@GeneratedValue(generator = "preprocessorWorkSeq")
	@SequenceGenerator(name = "preprocessorWorkSeq", sequenceName = "DOCKETS_PREPROCESSOR.PREPROCESSOR_WORK_SEQ")
	public Long getPrimaryKey() {
		return primaryKey;
	}
	@Column(name="CLIENT", nullable=false)
	public String getClientString() {
		return (client != null) ? client.name() : null;
	}
	@Column(name="REQUEST_ID", nullable=false)
	public String getPublishingRequestId() {
		return (publishingRequestUuid != null) ? publishingRequestUuid.toString() : null;
	}
	@OneToOne
	@JoinColumn(name="COURT_ID", nullable=true)
	public Court getCourt() {
		return court;
	}
	/**
	 * Returns the raw request XML that came from the client system.
	 */
	@Column(name="REQUEST_XML", nullable=false)
	public String getXml() {
		return xml;
	}
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * When was this worked actually started in the Preprocessor.
	 * @return when the Preprocessor started with this request, null if not started
	 */
	@Column(name="START_TIME")
	public Date getStartTime() {
		return startTime;
	}
	
	@Column(name="FIFO_OVERRIDE")
	protected String getFifoOverrideString() {
		return fifoOverride ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
	}
	@Column(name="SERVER_NAME")
	public String getServerName() {
		return serverName;
	}
	@Column(name="VENDOR_ID", nullable=true)
	public Long getVendorId() {
		return vendorId;
	}
	
	
	/**
	 * Returns true if this work item is already in-flight in the processing pipeline.
	 */
	@Transient
	public boolean isAlreadyStarted() {
		return (startTime != null);
	}
	@Transient
	public ClientEnum getClient() {
		return client;
	}
	@Transient UUID getPublishingRequestUuid() {
		return publishingRequestUuid;
	}
	
	@Transient
	/** Returns true if the court processing can begin despite the same court already running in the processing pathway. */	
	public boolean isFifoOverride() {
		return fifoOverride;
	}

	public void setPrimaryKey(Long pk) {
		this.primaryKey = pk;
	}
	protected void setClientString(String client) {
		setClient(ClientEnum.valueOf(client));
	}
	protected void setClient(ClientEnum client) {
		this.client = client;
	}
	protected void setPublishingRequestUuid(UUID uuid) {
		this.publishingRequestUuid = uuid;
	}
	protected void setPublishingRequestId(String id) throws UUIDException {
		setPublishingRequestUuid(new UUID(id));
	}
	protected void setCourt(Court court) {
		this.court = court;
	}
	protected void setFifoOverrideString(String trueFalse) {
		setFifoOverride(Boolean.valueOf(trueFalse));
	}
	public void setFifoOverride(boolean fifoOverride) {
		this.fifoOverride = fifoOverride;
	}
	protected void setXml(String xml) {
		this.xml = xml;
	}
	protected void setCreateTime(Date create) {
		this.createTime = create;
	}
	public void setStartTime(Date start) {
		this.startTime = start;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	protected void setVendorId(Long vendorId){
		this.vendorId = vendorId;
	}
	
	/**
	 * @return the PublishingPriority
	 */
	@Column(name="PRIORITY")
	public long getPublishingPriority() {
		return publishingPriority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPublishingPriority(long publishingPriority) {
		this.publishingPriority = publishingPriority;
	}	
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((court == null) ? 0 : court.hashCode());
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result + (fifoOverride ? 1231 : 1237);
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((xml == null) ? 0 : xml.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PreprocessorWorkItem other = (PreprocessorWorkItem) obj;
		if (client != other.client)
			return false;
		if (court == null) {
			if (other.court != null)
				return false;
		} else if (!court.equals(other.court))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (fifoOverride != other.fifoOverride)
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (xml == null) {
			if (other.xml != null)
				return false;
		} else if (!xml.equals(other.xml))
			return false;
		return true;
	}
}
