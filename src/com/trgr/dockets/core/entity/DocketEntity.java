/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/

package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="DOCKET", schema="DOCKETS_PUB")
@org.hibernate.annotations.Entity(
		dynamicInsert = true
)
public class DocketEntity 
{

	public static final String LEGACY_ID = "primaryKey";
	private String primaryKey;  // legacy ID
	private Court court;
//	DOCKET_NUMBER
	private String docketNumber;
//	PREV_LEGACY_ID
	private String previousLegacyId;
//	PRODUCT_ID
	private Product product;
//	CASE_TYPE_ID
	private Long caseTypeId;
//	CASE_SUB_TYPE_ID
	private Long caseSubTypeId;
//	DOCKET_TYPE_CODE
	private String docketTypeCode;
//	LAST_CASE_STATUS
	private String lastCaseStatus;
//	TITLE
	private String title;
//	SEQUENCE_NUMBER
	private String sequenceNumber;
//	FILING_YEAR
	private Long filingYear;
//	FILING_DATE
	private Date filingDate;
//	LOCATION_CODE
	private String locationCode;
//	LAST_SCRAPE_DATE
	private Date lastScrapeDate;
//	LAST_CONVERTED_DATE
	private Date lastConvertedDate;
//	LAST_PUBLISH_DATE
	private Date lastPublishDate;
//	LAST_LOAD_DATE
	private Date lastLoadDate;
//	PUBLISH_FLAG
	private String publishFlag;
//	AUDIT_ID
	private Long auditId;
//	DOC_LOADED_FLAG
	private String docLoadedFlag;
//	UUID
	private String uuid;
//	COUNTY_ID
	private Long countyId;
//	LAST_DROPPED_REASONS
	private Long lastPpDroppedReason;
	private Long lastIcDroppedReason;
	private Long lastPbDroppedReason;
	private Long lastNlDroppedReason;
//	LAST REPORT DROPPED REASON
	private String knosErrorExist;
	private String summaryServiceErrorExist;
	private String pdfDetailErrorExist;
	private String jnfErrorExist;
//	ACQUIRED
	private String acquired;
	private char largeDocketFlag;
	
	public DocketEntity() 
	{
		super();
	}
	public DocketEntity(String pk, String docketNumber, Court court) 
	{
		setPrimaryKey(pk);
		setDocketNumber(docketNumber);
		setCourt(court);
		setLargeDocketFlag('N');
	}

	@OneToOne
	@JoinColumn(name="COURT_ID")
	public Court getCourt() 
	{
		return court;
	}
	@Id
	@Column(name="LEGACY_ID")
	public String getPrimaryKey() 
	{
		return primaryKey;
	}

	@Column(name = "DOCKET_NUMBER", length = 30, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getDocketNumber() 
	{
		return docketNumber;
	}
	
	@Column(name = "PREV_LEGACY_ID", length = 30, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public String getPreviousLegacyId() 
	{
		return previousLegacyId;
	}
	
	@OneToOne
	@JoinColumn(name="PRODUCT_ID")
	public Product getProduct() 
	{
		return product;
	}
	@Column(name = "CASE_TYPE_ID", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getCaseTypeId() 
	{
		return caseTypeId;
	}
	@Column(name = "CASE_SUB_TYPE_ID", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getCaseSubTypeId() 
	{
		return caseSubTypeId;
	}
	@Column(name = "DOCKET_TYPE_CODE", length=2, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public String getDocketTypeCode()
	{
		return docketTypeCode;
	}
	@Column(name = "LAST_CASE_STATUS", length=100, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public String getLastCaseStatus() 
	{
		return lastCaseStatus;
	}
	@Column(name = "TITLE", length=100, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public String getTitle() 
	{
		return title;
	}
	@Column(name = "SEQUENCE_NUMBER", length=30, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public String getSequenceNumber()
	{
		return sequenceNumber;
	}
	@Column(name = "FILING_YEAR", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getFilingYear() 
	{
		return filingYear;
	}
	@Column(name = "FILING_DATE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Date getFilingDate() 
	{
		return filingDate;
	}
	@Column(name = "LOCATION_CODE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public String getLocationCode()
	{
		return locationCode;
	}
	@Column(name = "LAST_SCRAPE_DATE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Date getLastScrapeDate()
	{
		return lastScrapeDate;
	}
	@Column(name = "LAST_CONVERTED_DATE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Date getLastConvertedDate()
	{
		return lastConvertedDate;
	}
	@Column(name = "LAST_PUBLISH_DATE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Date getLastPublishDate()
	{
		return lastPublishDate;
	}
	@Column(name = "LAST_LOAD_DATE", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Date getLastLoadDate() 
	{
		return lastLoadDate;
	}
	@Column(name = "PUBLISH_FLAG", columnDefinition = "CHAR(1) DEFAULT 'Y'")
	@Basic(fetch = FetchType.EAGER)
	public String getPublishFlag() 
	{
		return publishFlag;
	}
	@Column(name = "AUDIT_ID", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getAuditId() 
	{
		return auditId;
	}
	@Column(name = "DOC_LOADED_FLAG", columnDefinition = "CHAR(1) DEFAULT 'N'", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getDocLoadedFlag() 
	{
		return docLoadedFlag;
	}
	@Column(name = "UUID", length=33, nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public String getUuid() 
	{
		return uuid;
	}
	@Column(name = "COUNTY_ID", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getCountyId()
	{
		return countyId;
	}
	@Column(name = "LAST_PP_DROPPED_REASON", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getLastPpDroppedReason() {
		return lastPpDroppedReason;
	}
	@Column(name = "LAST_IC_DROPPED_REASON", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getLastIcDroppedReason() {
		return lastIcDroppedReason;
	}
	@Column(name = "LAST_PB_DROPPED_REASON", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getLastPbDroppedReason() {
		return lastPbDroppedReason;
	}
	@Column(name = "LAST_NL_DROPPED_REASON", nullable = true)
	@Basic(fetch = FetchType.EAGER)
	public Long getLastNlDroppedReason() {
		return lastNlDroppedReason;
	}
	
	@Column(name = "KNOS_ERROR_EXIST", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getKnosErrorExist() {
		return knosErrorExist;
	}

	@Column(name = "SUMMARY_SERVICE_ERROR_EXIST", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getSummaryServiceErrorExist() {
		return summaryServiceErrorExist;
	}
	
	@Column(name = "PDF_DETAIL_ERROR_EXIST", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getPdfDetailErrorExist() {
		return pdfDetailErrorExist;
	}
	
	@Column(name = "JNF_ERROR_EXIST", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getJnfErrorExist() {
		return jnfErrorExist;
	}
	@Column(name = "ACQUIRED", columnDefinition = "CHAR(1) DEFAULT 'Y'")
	@Basic(fetch = FetchType.EAGER)
	public String getAcquired() 
	{
		return acquired;
	}
	
	@Column(name="LARGE_DOCKET_FLAG")
	public char getLargeDocketFlag() {
		return largeDocketFlag;
	}
	
	public void setPrimaryKey(String primaryKey) 
	{
		this.primaryKey = primaryKey;
	}
	public void setCourt(Court court) 
	{
		this.court = court;;
	}
	/**
	 * @param docketNumber the docketNumber to set
	 */
	public void setDocketNumber(String docketNumber)
	{
		this.docketNumber = docketNumber;
	}
	/**
	 * @param previousLegacyId the previousLegacyId to set
	 */
	public void setPreviousLegacyId(String previousLegacyId) 
	{
		this.previousLegacyId = previousLegacyId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProduct(Product product)
	{
		this.product = product;
	}
	/**
	 * @param caseTypeId the caseTypeId to set
	 */
	public void setCaseTypeId(Long caseTypeId) 
	{
		this.caseTypeId = caseTypeId;
	}

	/**
	 * @param caseSubTypeId the caseSubTypeId to set
	 */
	public void setCaseSubTypeId(Long caseSubTypeId) 
	{
		this.caseSubTypeId = caseSubTypeId;
	}

	/**
	 * @param docketTypeCode the docketTypeCode to set
	 */
	public void setDocketTypeCode(String docketTypeCode) 
	{
		this.docketTypeCode = docketTypeCode;
	}

	/**
	 * @param lastCaseStatus the lastCaseStatus to set
	 */
	public void setLastCaseStatus(String lastCaseStatus) 
	{
		this.lastCaseStatus = lastCaseStatus;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) 
	{
		this.title = title;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @param filingYear the filingYear to set
	 */
	public void setFilingYear(Long filingYear) 
	{
		this.filingYear = filingYear;
	}

	/**
	 * @param filingDate the filingDate to set
	 */
	public void setFilingDate(Date filingDate) 
	{
		this.filingDate = filingDate;
	}

	/**
	 * @param locationCode the locationCode to set
	 */
	public void setLocationCode(String locationCode) 
	{
		this.locationCode = locationCode;
	}

	/**
	 * @param lastScrapeDate the lastScrapeDate to set
	 */
	public void setLastScrapeDate(Date lastScrapeDate)
	{
		this.lastScrapeDate = lastScrapeDate;
	}

	/**
	 * @param lastConvertedDate the lastConvertedDate to set
	 */
	public void setLastConvertedDate(Date lastConvertedDate) 
	{
		this.lastConvertedDate = lastConvertedDate;
	}

	/**
	 * @param lastPublishDate the lastPublishDate to set
	 */
	public void setLastPublishDate(Date lastPublishDate) 
	{
		this.lastPublishDate = lastPublishDate;
	}

	/**
	 * @param lastLoadDate the lastLoadDate to set
	 */
	public void setLastLoadDate(Date lastLoadDate) 
	{
		this.lastLoadDate = lastLoadDate;
	}

	/**
	 * @param publishFlag the publishFlag to set
	 */
	public void setPublishFlag(String publishFlag)
	{
		this.publishFlag = publishFlag;
	}
	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Long auditId) 
	{
		this.auditId = auditId;
	}

	/**
	 * @param docLoadedFlag the docLoadedFlag to set
	 */
	public void setDocLoadedFlag(String docLoadedFlag) 
	{
		this.docLoadedFlag = docLoadedFlag;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) 
	{
		this.uuid = uuid;
	}
	/**
	 * @param countyId the countyId to set
	 */
	public void setCountyId(Long countyId)
	{
		this.countyId = countyId;
	}
	
	public void setLastPpDroppedReason(Long lastPpDroppedReason) {
		this.lastPpDroppedReason = lastPpDroppedReason;
	}
	public void setLastIcDroppedReason(Long lastIcDroppedReason) {
		this.lastIcDroppedReason = lastIcDroppedReason;
	}
	public void setLastPbDroppedReason(Long lastPbDroppedReason) {
		this.lastPbDroppedReason = lastPbDroppedReason;
	}
	
	public void setLastNlDroppedReason(Long lastNlDroppedReason) {
		this.lastNlDroppedReason = lastNlDroppedReason;
	}
		
	public void setKnosErrorExist(String knosErrorExist) {
		this.knosErrorExist = knosErrorExist;
	}

	public void setSummaryServiceErrorExist(String summaryServiceErrorExist) {
		this.summaryServiceErrorExist = summaryServiceErrorExist;
	}

	public void setPdfDetailErrorExist(String pdfDetailErrorExist) {
		this.pdfDetailErrorExist = pdfDetailErrorExist;
	}

	public void setJnfErrorExist(String jnfErrorExist) {
		this.jnfErrorExist = jnfErrorExist;
	}
	/**
	 * @param acquired the acquired to set
	 */
	public void setAcquired(String acquired)
	{
		this.acquired = acquired;
	}
	
	public void setLargeDocketFlag(char largeDocketFlag) {
		this.largeDocketFlag = largeDocketFlag;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((auditId == null) ? 0 : auditId.hashCode());
		result = prime * result
				+ ((caseSubTypeId == null) ? 0 : caseSubTypeId.hashCode());
		result = prime * result
				+ ((caseTypeId == null) ? 0 : caseTypeId.hashCode());
		result = prime * result
				+ ((countyId == null) ? 0 : countyId.hashCode());
		result = prime * result + ((court == null) ? 0 : court.hashCode());
		result = prime * result
				+ ((docLoadedFlag == null) ? 0 : docLoadedFlag.hashCode());
		result = prime * result
				+ ((docketNumber == null) ? 0 : docketNumber.hashCode());
		result = prime * result
				+ ((docketTypeCode == null) ? 0 : docketTypeCode.hashCode());
		result = prime * result
				+ ((filingDate == null) ? 0 : filingDate.hashCode());
		result = prime * result
				+ ((filingYear == null) ? 0 : filingYear.hashCode());
		result = prime * result
				+ ((lastCaseStatus == null) ? 0 : lastCaseStatus.hashCode());
		result = prime
				* result
				+ ((lastConvertedDate == null) ? 0 : lastConvertedDate
						.hashCode());
		result = prime
				* result
				+ ((lastIcDroppedReason == null) ? 0 : lastIcDroppedReason
						.hashCode());
		result = prime * result
				+ ((lastLoadDate == null) ? 0 : lastLoadDate.hashCode());
		result = prime
				* result
				+ ((lastNlDroppedReason == null) ? 0 : lastNlDroppedReason
						.hashCode());
		result = prime
				* result
				+ ((lastPbDroppedReason == null) ? 0 : lastPbDroppedReason
						.hashCode());
		result = prime
				* result
				+ ((lastPpDroppedReason == null) ? 0 : lastPpDroppedReason
						.hashCode());
		result = prime * result
				+ ((lastPublishDate == null) ? 0 : lastPublishDate.hashCode());
		result = prime * result
				+ ((lastScrapeDate == null) ? 0 : lastScrapeDate.hashCode());
		result = prime * result
				+ ((locationCode == null) ? 0 : locationCode.hashCode());
		result = prime
				* result
				+ ((previousLegacyId == null) ? 0 : previousLegacyId.hashCode());
		//Start
		result = prime
				* result
				+ ((knosErrorExist == null) ? 0 : knosErrorExist.hashCode());
		result = prime
				* result
				+ ((summaryServiceErrorExist == null) ? 0 : summaryServiceErrorExist.hashCode());
		result = prime
				* result
				+ ((pdfDetailErrorExist == null) ? 0 : pdfDetailErrorExist.hashCode());
		result = prime
				* result
				+ ((jnfErrorExist == null) ? 0 : jnfErrorExist.hashCode());
		//End
		result = prime * result
				+ ((acquired == null) ? 0 : acquired.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result
				+ ((publishFlag == null) ? 0 : publishFlag.hashCode());
		result = prime * result
				+ ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		DocketEntity other = (DocketEntity) obj;
		if (auditId == null) {
			if (other.auditId != null)
				return false;
		} else if (!auditId.equals(other.auditId))
			return false;
		if (caseSubTypeId == null) {
			if (other.caseSubTypeId != null)
				return false;
		} else if (!caseSubTypeId.equals(other.caseSubTypeId))
			return false;
		if (caseTypeId == null) {
			if (other.caseTypeId != null)
				return false;
		} else if (!caseTypeId.equals(other.caseTypeId))
			return false;
		if (countyId == null) {
			if (other.countyId != null)
				return false;
		} else if (!countyId.equals(other.countyId))
			return false;
		if (court == null) {
			if (other.court != null)
				return false;
		} else if (!court.equals(other.court))
			return false;
		if (docLoadedFlag == null) {
			if (other.docLoadedFlag != null)
				return false;
		} else if (!docLoadedFlag.equals(other.docLoadedFlag))
			return false;
		if (docketNumber == null) {
			if (other.docketNumber != null)
				return false;
		} else if (!docketNumber.equals(other.docketNumber))
			return false;
		if (docketTypeCode == null) {
			if (other.docketTypeCode != null)
				return false;
		} else if (!docketTypeCode.equals(other.docketTypeCode))
			return false;
		if (filingDate == null) {
			if (other.filingDate != null)
				return false;
		} else if (!filingDate.equals(other.filingDate))
			return false;
		if (filingYear == null) {
			if (other.filingYear != null)
				return false;
		} else if (!filingYear.equals(other.filingYear))
			return false;
		if (lastCaseStatus == null) {
			if (other.lastCaseStatus != null)
				return false;
		} else if (!lastCaseStatus.equals(other.lastCaseStatus))
			return false;
		if (lastConvertedDate == null) {
			if (other.lastConvertedDate != null)
				return false;
		} else if (!lastConvertedDate.equals(other.lastConvertedDate))
			return false;
		if (lastIcDroppedReason == null) {
			if (other.lastIcDroppedReason != null)
				return false;
		} else if (!lastIcDroppedReason.equals(other.lastIcDroppedReason))
			return false;
		if (lastLoadDate == null) {
			if (other.lastLoadDate != null)
				return false;
		} else if (!lastLoadDate.equals(other.lastLoadDate))
			return false;
		if (lastNlDroppedReason == null) {
			if (other.lastNlDroppedReason != null)
				return false;
		} else if (!lastNlDroppedReason.equals(other.lastNlDroppedReason))
			return false;
		if (lastPbDroppedReason == null) {
			if (other.lastPbDroppedReason != null)
				return false;
		} else if (!lastPbDroppedReason.equals(other.lastPbDroppedReason))
			return false;
		if (lastPpDroppedReason == null) {
			if (other.lastPpDroppedReason != null)
				return false;
		} else if (!lastPpDroppedReason.equals(other.lastPpDroppedReason))
			return false;
		if (lastPublishDate == null) {
			if (other.lastPublishDate != null)
				return false;
		} else if (!lastPublishDate.equals(other.lastPublishDate))
			return false;
		if (lastScrapeDate == null) {
			if (other.lastScrapeDate != null)
				return false;
		} else if (!lastScrapeDate.equals(other.lastScrapeDate))
			return false;
		if (locationCode == null) {
			if (other.locationCode != null)
				return false;
		} else if (!locationCode.equals(other.locationCode))
			return false;
		if (previousLegacyId == null) {
			if (other.previousLegacyId != null)
				return false;
		} else if (!previousLegacyId.equals(other.previousLegacyId))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (publishFlag == null) {
			if (other.publishFlag != null)
				return false;
		} else if (!publishFlag.equals(other.publishFlag))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		if (knosErrorExist == null) {
			if (other.knosErrorExist != null)
				return false;
		} else if (!knosErrorExist.equals(other.knosErrorExist))
			return false;
		if (summaryServiceErrorExist == null) {
			if (other.summaryServiceErrorExist != null)
				return false;
		} else if (!summaryServiceErrorExist.equals(other.summaryServiceErrorExist))
			return false;
		if (pdfDetailErrorExist == null) {
			if (other.pdfDetailErrorExist != null)
				return false;
		} else if (!pdfDetailErrorExist.equals(other.pdfDetailErrorExist))
			return false;
		if (jnfErrorExist == null) {
			if (other.jnfErrorExist != null)
				return false;
		} else if (!jnfErrorExist.equals(other.jnfErrorExist))
			return false;
		if (acquired == null) {
			if (other.acquired != null)
				return false;
		} else if (!acquired.equals(other.acquired))
			return false;
		return true;
	}
	
	@Override
	public String toString() 
	{
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}


