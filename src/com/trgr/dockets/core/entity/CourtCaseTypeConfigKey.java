/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/

package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Composite primary key of CourtCaseTypeConfig.
 *
 * @author u0159366
 */

public class CourtCaseTypeConfigKey implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6139390527389545842L;
	public static String COURT_ID = "courtId";
	public static String CASE_TYPE = "caseType";

	private CaseType caseType;
	
//private Court court;
	
	//private Long caseTypeId;
	
	private Long courtId;

	public CourtCaseTypeConfigKey() {
		super();
	}

	public CourtCaseTypeConfigKey(CaseType caseType, Long courtId) {
		this.caseType = caseType;
		this.courtId = courtId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((caseType == null) ? 0 : caseType.hashCode());
		result = prime * result + ((courtId == null) ? 0 : courtId.hashCode());
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
		CourtCaseTypeConfigKey other = (CourtCaseTypeConfigKey) obj;
		if (courtId == null) {
			if (other.courtId != null)
			return false;
		} else if (!courtId.equals(other.courtId))
			return false;
		if (caseType == null) {
			if (other.caseType != null)
			return false;
		} else if (!caseType.equals(other.caseType))
			return false;
		return true;
	}

	/** Associated case type. */
	@ManyToOne(targetEntity = CaseType.class, fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "CASE_TYPE_ID", nullable = false)
	public CaseType getCaseType() {
		return caseType;
	}

	public void setCaseType(CaseType caseType) {
		this.caseType = caseType;
	}
	
	/** Associated court. */
/*	@ManyToOne(targetEntity = Court.class )
			//fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "COURT_ID", nullable = false, insertable = false, updatable  = false)
	public Court getCourt() {
		return court;
	}

	public void setCourt(Court court) {
		this.court = court;
	}*/
/*	@Id
	@Column(name="CASE_TYPE_ID", nullable = false)
	public Long getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(Long caseTypeId) {
		this.caseTypeId = caseTypeId;
	}
	*/

	@Id
	@Column(name="COURT_ID", nullable = false)
	public Long getCourtId() {
		return courtId;
	}

	public void setCourtId(Long courtId) {
		this.courtId = courtId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append("CourtCaseTypeConfigKey").append("@").append(Integer.toHexString(hashCode()))
			  .append(" [");
		buffer.append("court='").append(getCourtId()).append("' ");
		buffer.append("caseType='").append(getCaseType()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}
}
