/*Copyright 2016 Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

/**
 */
@IdClass(com.trgr.dockets.core.entity.ScrapedDocketDataPK.class)
@Entity
@NamedQueries({
		@NamedQuery(name = "getScrapedDocketDataCountByLastSourceFilename", query = "select count(*) from ScrapedDocketData myScrapedDocketData where myScrapedDocketData.lastSourceFilename = :lastSourceFilename"),
		@NamedQuery(name = "findScrapedDocketDataByLastSourceFilename", query = "select myScrapedDocketData from ScrapedDocketData myScrapedDocketData where myScrapedDocketData.lastSourceFilename = :lastSourceFilename"),		
		@NamedQuery(name = "findScrapedDocketDataByPrimaryKey", query = "select myScrapedDocketData from ScrapedDocketData myScrapedDocketData where myScrapedDocketData.westlawClusterName = :westlawClusterName " +
							"and myScrapedDocketData.docketNumber = :docketNumber " +
							"and myScrapedDocketData.subdivisionName = :subdivisionName "+
							"and myScrapedDocketData.vendor = :vendor ")})
@Table(schema = "DOCKETS_PREPROCESSOR", name = "SCRAPED_DOCKET_DATA")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "DocketsCore/com/trgr/dockets/core/entity", name = "ScrapedDocketData")
@XmlRootElement(namespace = "DocketsCore/com/trgr/dockets/core/entity")
public class ScrapedDocketData implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 */

	@Column(name = "WESTLAW_CLUSTER_NAME", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String westlawClusterName;
	/**
	 */

	@Column(name = "DOCKET_NUMBER", length = 20, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	String docketNumber;
	/**
	 */
	
	@Column(name="VENDOR", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@Id
	@XmlElement
	Long vendor;

	@Column(name = "SCRAPE_TYPE", length = 10, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String scrapeType;
	/**
	 */

	@Column(name = "CASE_TYPE", length = 10)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String caseType;
	/**
	 */

	@Column(name = "SUBDIVISION_NAME", length = 100)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String subdivisionName;
	/**
	 */

	@Column(name = "DOCKET_DATA_FIELDS")
	@Basic(fetch = FetchType.EAGER)
	@Lob
	@XmlElement
	String docketDataFields;
	/**
	 */
//	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_SCRAPE_DATE", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	Timestamp lastScrapeDate;
	/**
	 */

	@Column(name = "LAST_SOURCE_FILENAME", length = 40, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String lastSourceFilename;

	/**
	 */
	
	@Column(name = "YEAR", length = 4)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String year;
	
	
	@Column(name = "DELETE_FLAG", length = 1)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String deleteFlag;
	
	@Column(name = "DOCKET_COMPLETE_FLAG", length = 1)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	private String docketCompleteFlag = "N";
	
	@Column(name = "LEGACY_ID", length = 30)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String legacyId;
	
	@Column(name = "INDEX_NUMBER", length = 10)
	@Basic(fetch = FetchType.EAGER)
	@XmlElement
	String indexNumber;
	

	
	@Transient
	String filingDate;
	

	public String getDocketCompleteFlag() {
		return docketCompleteFlag;
	}

	public void setDocketCompleteFlag(String docketCompleteFlag) {
		this.docketCompleteFlag = docketCompleteFlag;
	}

	/**
	 */
	public void setWestlawClusterName(String westlawClusterName) {
		this.westlawClusterName = westlawClusterName;
	}

	/**
	 */
	public String getWestlawClusterName() {
		return this.westlawClusterName;
	}

	/**
	 */
	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	/**
	 */
	public String getDocketNumber() {
		return this.docketNumber;
	}
	
	public Long getVendor(){
		return vendor;
	}
	
	public void setVendor(Long vendorId){
		this.vendor = vendorId;
	}

	/**
	 */
	public void setScrapeType(String scrapeType) {
		this.scrapeType = scrapeType;
	}

	/**
	 */
	public String getScrapeType() {
		return this.scrapeType;
	}

	/**
	 */
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	/**
	 */
	public String getCaseType() {
		return this.caseType;
	}

	/**
	 */
	public void setSubdivisionName(String subdivisionName) {
		this.subdivisionName = subdivisionName;
	}

	/**
	 */
	public String getSubdivisionName() {
		return this.subdivisionName;
	}

	/**
	 */
	public void setDocketDataFields(String docketDataFields) {
		this.docketDataFields = docketDataFields;
	}

	/**
	 */
	public String getDocketDataFields() {
		return this.docketDataFields;
	}

	/**
	 */
	public void setLastScrapeDate(Timestamp lastScrapeDate) {
		this.lastScrapeDate = lastScrapeDate;
	}

	/**
	 */
	public Timestamp getLastScrapeDate() {
		return this.lastScrapeDate;
	}

	/**
	 */
	public void setLastSourceFilename(String lastSourceFilename) {
		this.lastSourceFilename = lastSourceFilename;
	}

	/**
	 */
	public String getLastSourceFilename() {
		return this.lastSourceFilename;
	}

	/**
	 */
	public ScrapedDocketData() {
	}
	
	public ScrapedDocketData(String scrapedDocketData) {		
		//Unserialize the data from pseudo-xml format...
		String westlawClusterName = capture("<westlawClusterName>(.*?)</westlawClusterName>", scrapedDocketData);
		if(StringUtils.isNotBlank(westlawClusterName))
			this.westlawClusterName = westlawClusterName;
		String docketNumber = capture("<docketNumber>(.*?)</docketNumber>", scrapedDocketData);
		if(StringUtils.isNotBlank(docketNumber))
			this.docketNumber = docketNumber;
		String scrapeType = capture("<scrapeType>(.*?)</scrapeType>", scrapedDocketData);
		if(StringUtils.isNotBlank(scrapeType))
			this.scrapeType = scrapeType;
		String caseType = capture("<caseType>(.*?)</caseType>", scrapedDocketData);
		if(StringUtils.isNotBlank(caseType))
			this.caseType = caseType;
		String subdivisionName = capture("<subdivisionName>(.*?)</subdivisionName>", scrapedDocketData);
		if(StringUtils.isNotBlank(subdivisionName))
			this.subdivisionName = subdivisionName;
		String docketDataFields = capture("<docketDataFields>(.*?)</docketDataFields>", scrapedDocketData);
		if(StringUtils.isNotBlank(docketDataFields))
			this.docketDataFields = docketDataFields;
		String lastSourceFilename = capture("<lastSourceFilename>(.*?)</lastSourceFilename>", scrapedDocketData);
		if(StringUtils.isNotBlank(lastSourceFilename))
			this.lastSourceFilename = lastSourceFilename;
		String year = capture("<year>(.*?)</year>", scrapedDocketData);
		if(StringUtils.isNotBlank(year))
			this.year = year;
		String deleteFlag = capture("<deleteFlag>(.*?)</deleteFlag>", scrapedDocketData);
		if(StringUtils.isNotBlank(deleteFlag))
			this.deleteFlag = deleteFlag;		
		String docketCompleteFlag = capture("<docketCompleteFlag>(.*?)</docketCompleteFlag>", scrapedDocketData);
		if(StringUtils.isNotBlank(docketCompleteFlag))
			this.docketCompleteFlag = docketCompleteFlag;
		String legacyId = capture("<legacyId>(.*?)</legacyId>", scrapedDocketData);
		if(StringUtils.isNotBlank(legacyId))
			this.legacyId = legacyId;
		String indexNumber = capture("<indexNumber>(.*?)</indexNumber>", scrapedDocketData);
		if(StringUtils.isNotBlank(indexNumber)){
			if(indexNumber.length()>10){
				this.indexNumber = indexNumber.substring(0, 10);
			} else {
				this.indexNumber = indexNumber;
			}
		}
		String lastScrapeDate = capture("<lastScrapeDate>(.*?)</lastScrapeDate>", scrapedDocketData);
		if(StringUtils.isNotBlank(capture("<lastScrapeDate>(.*?)</lastScrapeDate>", scrapedDocketData)))
			this.lastScrapeDate = java.sql.Timestamp.valueOf(lastScrapeDate);
	}

	/**
	 * Copies the contents of the specified bean into this bean.
	 *
	 */
	public void copy(ScrapedDocketData that) {
		setWestlawClusterName(that.getWestlawClusterName());
		setDocketNumber(that.getDocketNumber());
		setScrapeType(that.getScrapeType());
		setCaseType(that.getCaseType());
		setSubdivisionName(that.getSubdivisionName());
		setDocketDataFields(that.getDocketDataFields());
		setLastScrapeDate(that.getLastScrapeDate());
		setLastSourceFilename(that.getLastSourceFilename());
//		setScrapedDocketHistories(new java.util.LinkedHashSet<com.trgr.dockets.core.entity.ScrapedDocketHistory>(that.getScrapedDocketHistories()));
	}

	/**
	 * Returns a textual representation of a bean.
	 *
	 */
	public String toString() {

		StringBuilder buffer = new StringBuilder();

		//Serialize the data into pseudo-xml to prevent problems with certain characters...
		buffer.append("<westlawClusterName>").append(westlawClusterName).append("</westlawClusterName>");
		buffer.append("<docketNumber>").append(docketNumber).append("</docketNumber>");
		buffer.append("<scrapeType>").append(scrapeType).append("</scrapeType>");
		buffer.append("<caseType>").append(caseType).append("</caseType>");
		buffer.append("<subdivisionName>").append(subdivisionName).append("</subdivisionName>");
		buffer.append("<docketDataFields>").append(docketDataFields).append("</docketDataFields>");
		buffer.append("<lastScrapeDate>").append(lastScrapeDate).append("</lastScrapeDate>");
		buffer.append("<lastSourceFilename>").append(lastSourceFilename).append("</lastSourceFilename>");
		buffer.append("<year>").append(year).append("</year>");
		buffer.append("<deleteFlag>").append(deleteFlag).append("</deleteFlag>");		
		buffer.append("<docketCompleteFlag>").append(docketCompleteFlag).append("</docketCompleteFlag>");	
		buffer.append("<legacyId>").append(legacyId).append("</legacyId>");
		buffer.append("<indexNumber>").append(indexNumber).append("</indexNumber>");	
		
		return buffer.toString();
	}

	/**
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + ((westlawClusterName == null) ? 0 : westlawClusterName.hashCode()));
		result = (int) (prime * result + ((docketNumber == null) ? 0 : docketNumber.hashCode()));
		return result;
	}

	/**
	 */
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof ScrapedDocketData))
			return false;
		ScrapedDocketData equalCheck = (ScrapedDocketData) obj;
		if ((westlawClusterName == null && equalCheck.westlawClusterName != null) || (westlawClusterName != null && equalCheck.westlawClusterName == null))
			return false;
		if (westlawClusterName != null && !westlawClusterName.equals(equalCheck.westlawClusterName))
			return false;
		if ((docketNumber == null && equalCheck.docketNumber != null) || (docketNumber != null && equalCheck.docketNumber == null))
			return false;
		if (docketNumber != null && !docketNumber.equals(equalCheck.docketNumber))
			return false;
		return true;
	}

	/**
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 */
	public String getYear() {
		return this.year;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getLegacyId() {
		return legacyId;
	}

	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getFilingDate() {
		return filingDate;
	}

	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}
	
	public String capture(String regex, String input){
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(input);
		if(m.find()){
			return m.group(1);
		}
		return "";
	}
	
}
