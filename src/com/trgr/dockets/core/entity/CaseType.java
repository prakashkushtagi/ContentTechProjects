/**
 * Copyright 2013: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and Confidential information of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
 *
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * 
 * @author u0159366
 */
@Entity
@Table(name = "DOCKETS_PUB.CASE_TYPE")
public class CaseType implements Serializable
{
	
	private static final long serialVersionUID = -5643799445998778851L;
	public static String CASE_TYPE_ID = "caseTypeId";
	public static String CASE_TYPE_CODE = "caseType";

	/**  */
	@Id
	@Column(name = "CASE_TYPE_ID")
	private Long caseTypeId;

	/**  */
	@Column(name = "CASE_TYPE")
	private String caseType;

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		if ((this == other))
		{
			return true;
		}

		if ((other == null))
		{
			return false;
		}

		if (!(other instanceof Court))
		{
			return false;
		}

		CaseType castOther = (CaseType) other;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(this.caseType, castOther.getCaseType());

		return equalsBuilder.isEquals();
	}

	public String getCaseType()
	{
		return caseType;
	}

	public Long getCaseTypeId()
	{
		return caseTypeId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(this.caseType);

		return hashCodeBuilder.toHashCode();
	}

	public void setCaseType(String caseType)
	{
		this.caseType = caseType;
	}

	public void setCaseTypeId(Long caseTypeId)
	{
		this.caseTypeId = caseTypeId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();

		buffer.append("CaseType@").append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("caseType='").append(this.caseType).append("' ");
		buffer.append("]");

		return buffer.toString();
	}
}
