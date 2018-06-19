package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

@Entity
@Table(name="BATCH_DROPPED_DOCKETS", schema="DOCKETS_PUB")
public class BatchDroppedDocket {
	private static final int MAX_ERROR_DESC_LEN = 500;
	
	private Long primaryKey;
	private String docketNumber;
	private String errorDescription;
	private Process process;
	private String courtNorm;
	private String legacyId;

	public BatchDroppedDocket() {
		super();
	}
	public BatchDroppedDocket(Long pk) {
		setPrimaryKey(pk);
	}
	public BatchDroppedDocket(Long primaryKey, String docketNumber,
			String errorDescription, Process process, String courtNorm, String legacyId) {
		this.primaryKey = primaryKey;
		this.docketNumber = docketNumber;
		this.errorDescription = StringUtils.substring(errorDescription, 0, MAX_ERROR_DESC_LEN);
		this.process = process;
		this.courtNorm = courtNorm;
		this.legacyId = legacyId;
	}
	
	@Column(name="COURT_NORM")
	public String getCourtNorm() {
		return courtNorm;
	}
	@Column(name="DOCKET_NUMBER")
	public String getDocketNumber() {
		return docketNumber;
	}
	@Column(name="ERROR_DESCRIPTION", length=MAX_ERROR_DESC_LEN)
	public String getErrorDescription() {
		return errorDescription;
	}
	
	@Column(name="LEGACY_ID", nullable = true)
	public String getLegacyId() {
		return legacyId;
	}
	
	@Id
	@Column(name="DROPPED_DOCKET_ID")
	@GeneratedValue(generator = "batchDroppedDocketsSeq")
	@SequenceGenerator(name = "batchDroppedDocketsSeq", sequenceName = "DOCKETS_PUB.BATCH_DROPPED_DOCKETS_SEQ")
	public Long getPrimaryKey() {
		return primaryKey;
	}
	
	@OneToOne
	@JoinColumn(name="PROCESS_ID")
	public Process getProcess() {
		return process;
	}
	
	public void setCourtNorm(String courtNorm) {
		this.courtNorm = courtNorm;
	}
	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = StringUtils.substring(errorDescription, 0, MAX_ERROR_DESC_LEN);
	}
	public void setPrimaryKey(Long primaryKey) {
		Assert.notNull(primaryKey);
		this.primaryKey = primaryKey;
	}
	public void setProcess(Process process) {
		this.process = process;
	}
	
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courtNorm == null) ? 0 : courtNorm.hashCode());
		result = prime * result + ((docketNumber == null) ? 0 : docketNumber.hashCode());
		result = prime * result + ((errorDescription == null) ? 0 : errorDescription.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((process == null) ? 0 : process.hashCode());
		result = prime * result + ((legacyId == null) ? 0 : legacyId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BatchDroppedDocket other = (BatchDroppedDocket) obj;
		if (courtNorm == null)
		{
			if (other.courtNorm != null) return false;
		}
		else if (!courtNorm.equals(other.courtNorm)) return false;
		if (docketNumber == null)
		{
			if (other.docketNumber != null) return false;
		}
		else if (!docketNumber.equals(other.docketNumber)) return false;
		if (errorDescription == null)
		{
			if (other.errorDescription != null) return false;
		}
		else if (!errorDescription.equals(other.errorDescription)) return false;
		if (primaryKey == null)
		{
			if (other.primaryKey != null) return false;
		}
		else if (!primaryKey.equals(other.primaryKey)) return false;
		if (process == null)
		{
			if (other.process != null) return false;
		}
		else if (!process.equals(other.process)) return false;
		if (legacyId == null)
		{
			if (other.legacyId != null) return false;
		}
		else if (!legacyId.equals(other.legacyId)) return false;
		return true;
	}
	
	

}
