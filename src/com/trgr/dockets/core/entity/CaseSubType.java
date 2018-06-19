/**
 * 
 */
package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author C047166
 *
 */
@Entity
@Table(name="CASE_SUB_TYPE", schema="DOCKETS_PUB")
public class CaseSubType
{
	private Long caseSubTypeId;
	private Long caseTypeId;
	private String caseSubType;
	private Product product;
	private Court court;
	
	@Id
	@Column(name="CASE_SUB_TYPE_ID")
	public Long getCaseSubTypeId()
	{
		return caseSubTypeId;
	}
	
	@Column(name="CASE_TYPE_ID")
	public Long getCaseTypeId()
	{
		return caseTypeId;
	}
	
	@Column(name="CASE_SUB_TYPE")
	public String getCaseSubType()
	{
		return caseSubType;
	}
	
	@OneToOne
	@JoinColumn(name="PRODUCT_ID")
	public Product getProduct()
	{
		return product;
	}
	
	@OneToOne
	@JoinColumn(name="COURT_ID")
	public Court getCourt()
	{
		return court;
	}
	
	public void setCaseSubTypeId(Long caseSubTypeId)
	{
		this.caseSubTypeId = caseSubTypeId;
	}
	
	public void setCaseTypeId(Long caseTypeId)
	{
		this.caseTypeId = caseTypeId;
	}

	public void setCaseSubType(String caseSubType)
	{
		this.caseSubType = caseSubType;
	}

	public void setProduct(Product product)
	{
		this.product = product;
	}
	
	public void setCourt(Court court)
	{
		this.court = court;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caseSubType == null) ? 0 : caseSubType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CaseSubType other = (CaseSubType) obj;
		if (caseSubType == null)
		{
			if (other.caseSubType != null) return false;
		}
		else if (!caseSubType.equals(other.caseSubType)) return false;
		return true;
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	
	
}
