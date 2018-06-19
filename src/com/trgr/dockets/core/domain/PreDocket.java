/**
 * 
 */
package com.trgr.dockets.core.domain;

/**
 * A domain object holds the required information from the source file to persist a pre docket.
 * 
 * @author C047166
 *
 */
public class PreDocket
{
	private String caseSubType;
	private String caseTitle;
	private String filingDate; //yyyyMMdd
	private String caseType;
	
	public String getCaseSubType()
	{
		return caseSubType;
	}
	public void setCaseSubType(String caseSubType)
	{
		this.caseSubType = caseSubType;
	}
	public String getCaseTitle()
	{
		return caseTitle;
	}
	public void setCaseTitle(String caseTitle)
	{
		this.caseTitle = caseTitle;
	}
	public String getFilingDate()
	{
		return filingDate;
	}
	public void setFilingDate(String filingDate)
	{
		this.filingDate = filingDate;
	}
	public String getCaseType() {
		return caseType;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	
}
