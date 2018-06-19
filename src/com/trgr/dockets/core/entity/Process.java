/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Entity
@NamedQueries({	@NamedQuery(name = "findNumberProcessByBatchTypeId", query = "select count(*) from Process myProcess where myProcess.batchId = :batchId and myProcess.processType.primaryKey =  :processTypeId")})
@Table(name="PROCESS", schema="DOCKETS_PUB")
public class Process implements Serializable {
	private static final long serialVersionUID = -6965067864546625556L;

	public static final int MAX_ERR_MESG_LENGTH = 256;
	
	private UUID primaryKey;  // process_id
	private Long auditKey;
	
	private String batchId;
	private String subBatchId;
	private Date startDate;
	private Date endDate;
	private String errorCode;
	private String errorDescription;
	
	private Status status;
	private ProcessType processType;
	private String publishingRequestId;
	
    public static String BATCH_ID_ALIAS = "batch_id_alias";

    public Process() {
		super();
	}
	public Process(UUID processId) {
		setPrimaryKey(processId);
	}
	
	public static Process create(ProcessTypeEnum processTypeEnum, BatchMonitorKey batchMonitorKey, Date startTime) {
		UUID processKey = UUIDGenerator.createUuid();
		Process process = new Process(processKey);
		process.setAuditKey(null);
		process.setBatchId(batchMonitorKey.getBatchId());
		process.setEndDate(null);
		process.setErrorCode(null);
		process.setErrorDescription(null);
		process.setProcessType(processTypeEnum);
		process.setPublishingRequestId(batchMonitorKey.getPublishingRequestId());
		process.setStartDate(startTime);
		process.setStatus(StatusEnum.RUNNING);
		process.setSubBatchId(null);
		return process;
	}
	
	@Column(name="AUDIT_ID")
	public Long getAuditKey() {
		return auditKey;
	}
	@Column(name="BATCH_ID", nullable=false)
	public String getBatchId() {
		return batchId;
	}
	@Column(name="END_TIME")
	public Date getEndDate() {
		return endDate;
	}
	@Column(name="ERROR_CODE")
	public String getErrorCode() {
		return errorCode;
	}
	@Column(name="ERROR_DESCRIPTION", length=MAX_ERR_MESG_LENGTH)
	public String getErrorDescription() {
		return errorDescription;
	}
	@Id
	@Column(name="PROCESS_ID")
	public String getProcessId() {
		return (primaryKey != null) ? primaryKey.toString() : null;
	}
	@OneToOne
	@JoinColumn(name="PROCESS_TYPE_ID", nullable=false)
	public ProcessType getProcessType() {
		return processType; 
	}
	@Column(name="REQUEST_ID", nullable=false)
	public String getPublishingRequestId() {
		return publishingRequestId;
	}
	@Column(name="START_TIME")
	public Date getStartDate() {
		return startDate;
	}
	@OneToOne
	@JoinColumn(name="PROCESS_STATUS_ID", nullable=false)
	public Status getStatus() {
		return status;
	}
	@Column(name="SUB_BATCH_ID", nullable=true)
	public String getSubBatchId() {
		return subBatchId;
	}
	@Transient
	public UUID getPrimaryKey() {
		return primaryKey;
	}
	@Transient
	public StatusEnum getStatusEnum() {
		return ((status != null) ? StatusEnum.findByKey(status.getId()) : null);
	}

	public void setAuditKey(Long auditKey) {
		this.auditKey = auditKey;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public void setErrorDescription(String desc) {
		if(desc != null && desc.length() > MAX_ERR_MESG_LENGTH)
			this.errorDescription = desc.substring(0, MAX_ERR_MESG_LENGTH);
		else 
			this.errorDescription = desc;
	}
	public void setPrimaryKey(UUID id) {
		this.primaryKey = id;
	}
	protected void setProcessId(String id) throws UUIDException {
		setPrimaryKey(new UUID(id));
	}
	public void setProcessType(ProcessType processType) {
		this.processType = processType;
	}
	public void setProcessType(ProcessTypeEnum processTypeEnum) {
		setProcessType(new ProcessType(processTypeEnum));
	}
	public void setPublishingRequestId(String publishingRequestId) {
		this.publishingRequestId = publishingRequestId;
	}
	public void setPublishingRequestId(UUID publishingRequestId) {
		setPublishingRequestId(publishingRequestId.toString());
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setStatus(StatusEnum statusEnum) {
		setStatus(new Status(statusEnum));
	}
	public void setSubBatchId(String subBatchId) {
		this.subBatchId = subBatchId;
	}
		
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((auditKey == null) ? 0 : auditKey.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime
				* result
				+ ((errorDescription == null) ? 0 : errorDescription.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result
				+ ((processType == null) ? 0 : processType.hashCode());
		result = prime
				* result
				+ ((publishingRequestId == null) ? 0 : publishingRequestId
						.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((subBatchId == null) ? 0 : subBatchId.hashCode());
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
		Process other = (Process) obj;
		if (auditKey == null) {
			if (other.auditKey != null)
				return false;
		} else if (!auditKey.equals(other.auditKey))
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (errorDescription == null) {
			if (other.errorDescription != null)
				return false;
		} else if (!errorDescription.equals(other.errorDescription))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (processType != other.processType)
			return false;
		if (publishingRequestId == null) {
			if (other.publishingRequestId != null)
				return false;
		} else if (!publishingRequestId.equals(other.publishingRequestId))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (subBatchId == null) {
			if (other.subBatchId != null)
				return false;
		} else if (!subBatchId.equals(other.subBatchId))
			return false;
		return true;
	}

}
