/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author u0159366
 *
 */
@Entity
@Table(name="COURT_CASETYPE_CONFIG", schema="DOCKETS_PUB")
public class CourtCaseTypeConfig implements Serializable {

	private static final long serialVersionUID = -2377455354620170398L;

	public static String COURT = "court";
	public static String COUNTY = "county";
	public static String COURT_CASE_TYPE = "courtCaseType";
	public static String CASE_TYPE = "caseType";
	
	/** primary key */
	/**  */
	@Id
	@Column(name = "COURT_CASETYPE_ID")
	private Long courtCaseTypeId;
	
	/** Associated court. */
	@ManyToOne(targetEntity = Court.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "COURT_ID", nullable = false, insertable=false ,updatable=false)
	private Court court;

	//** Associated case type. *//*
	@OneToOne(targetEntity = CaseType.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "CASE_TYPE_ID", nullable = false)
	private CaseType caseType;

	
	@Column(name = "ROYALTY_NUMBER")
	private Long royaltyNumber;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(this.courtCaseTypeId);
		hashCodeBuilder.append(this.court);
		hashCodeBuilder.append(this.caseType);
		hashCodeBuilder.append(this.royaltyNumber);
		return hashCodeBuilder.toHashCode();
	}


	public Long getCourtCaseTypeId() {
		return courtCaseTypeId;
	}

	public void setCourtCaseTypeId(Long courtCaseTypeId) {
		this.courtCaseTypeId = courtCaseTypeId;
	}


	public Court getCourt() {
		return court;
	}

	public void setCourt(Court court) {
		this.court = court;
	}

public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}

	public Long getRoyaltyNumber() {
		return royaltyNumber;
	}

	public void setRoyaltyNumber(Long royaltyNumber) {
		this.royaltyNumber = royaltyNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourtCaseTypeConfig other = (CourtCaseTypeConfig) obj;
		
		if (courtCaseTypeId == null) {
			if (other.courtCaseTypeId != null)
				return false;
		} else if (!courtCaseTypeId.equals(other.courtCaseTypeId))
			return false;
		if (caseType == null) {
			if (other.caseType != null)
				return false;
		} else if (!caseType.equals(other.caseType))
			return false;
	
		if (court == null) {
			if (other.court != null)
				return false;
		} else if (!court.equals(other.court))
			return false;
		if (royaltyNumber == null) {
			if (other.royaltyNumber != null)
				return false;
		} else if (!royaltyNumber.equals(other.royaltyNumber))
			return false;
		return true;
	}

	@Override
	public String toString()
	{	StringBuffer buffer = new StringBuffer();
	
		buffer.append("CourtCaseTypeConfig@").append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("courtCaseTypeId='").append(getCourtCaseTypeId()).append("' ");
		buffer.append("court='").append(getCourt()).append("' ");
		buffer.append("caseType='").append(getCaseType()).append("' ");
		buffer.append("royaltyNumber='").append(getRoyaltyNumber()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}
	
	
	
}
