package com.trgr.dockets.core.processor;


public class Docket 
{
	private String number;
	private String court;
	private String platform;
	private String scrapeDate;
	private String statePostal;
	private String encoding;
	private String docketXmlString;
	private String errorMessage;
	private boolean deleteFlag;
	private boolean noLocationCode;
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the court
	 */
	public String getCourt() {
		return court;
	}
	/**
	 * @param court the court to set
	 */
	public void setCourt(String court) {
		this.court = court;
	}
	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}
	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	/**
	 * @return the scrapeDate
	 */
	public String getScrapeDate() {
		return scrapeDate;
	}
	/**
	 * @param scrapeDate the scrapeDate to set
	 */
	public void setScrapeDate(String scrapeDate) {
		this.scrapeDate = scrapeDate;
	}
	/**
	 * @return the statePostal
	 */
	public String getStatePostal() {
		return statePostal;
	}
	/**
	 * @param statePostal the statePostal to set
	 */
	public void setStatePostal(String statePostal) {
		this.statePostal = statePostal;
	}
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/**
	 * @return the docketXmlString
	 */
	public String getDocketXmlString() {
		return docketXmlString;
	}
	/**
	 * @param docketXmlString the docketXmlString to set
	 */
	public void setDocketXmlString(String docketXmlString) {
		this.docketXmlString = docketXmlString;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String toString() {
		return "Docket [number=" + number + ", court=" + court + ", platform="
				+ platform + ", scrapeDate=" + scrapeDate + ", statePostal="
				+ statePostal + ", encoding=" + encoding + ", errorMessage=" + errorMessage + "]";
	}
	public void setDeleteFlag(boolean deleteFlag) 
	{
		this.deleteFlag = deleteFlag;
	}
	/**
	 * @return the deleteFlag
	 */
	public boolean isDeleteFlag() 
	{
		return deleteFlag;
	}
	/**
	 * @return the noLocationCode
	 */
	public boolean isNoLocationCode() {
		return noLocationCode;
	}
	/**
	 * @param noLocationCode the noLocationCode to set
	 */
	public void setNoLocationCode(boolean noLocationCode) {
		this.noLocationCode = noLocationCode;
	}
	
	
}
