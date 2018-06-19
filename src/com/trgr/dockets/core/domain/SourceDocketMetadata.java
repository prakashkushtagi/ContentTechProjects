/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/

package com.trgr.dockets.core.domain;

import java.io.File;
import java.util.Date;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.util.DateUtils;

public class SourceDocketMetadata {
	public static final String CONTENT_PHASE_SOURCE = "SOURCE";

	public static final String DOCKET_HISTORY_TYPE_ACQUISTION = "ACQUISITION_HISTORY";

	private PublishingRequest publishingRequest;
	private String legacyId;
	private String docketNumber;
	private int yearFiled;
	private String locationCode;
	private AcquisitionMethod acquisitionMethod;
	private File sourceFile;
	private Date acquisitionTimestamp;
	private String scrapedTimestamp;
	private int vendorId;
	private String batchId;
	private boolean isDeleteOperation;
	private File docketContentFile;
	private long contentFileSize;
	private String sequenceNumber;
	private String docketTypeCode;
	private Long countyId;
	private String countyName;
	private Long caseTypeId;
	private String publishFlag;
	private Long caseSubTypeId;
	private String caseSubType;
	private String title;
	private String filingDate;
	private DocketHistoryTypeEnum docketHistEnum;
	// This is used when loading source
	private String caseType;
	private String courtName;
	private String sourceFilePath;
	private String sourceCaseId;
	private String status;
	private String miscellaneous;
	private String acquisitionStatus;
	private Court court;
	private Product product;

	/**
	 * @param publishingRequest
	 * @deprecated because it causes performance issues if there are a large number of SDMs floating around with publishing request objects attached to them
	 */
	@Deprecated
	public SourceDocketMetadata(PublishingRequest publishingRequest) {
		this.publishingRequest = publishingRequest;
		court = publishingRequest.getCourt();
	}

	public SourceDocketMetadata(String docketNumber) {
		this.docketNumber = docketNumber;
	}
	
	public SourceDocketMetadata(){}

	/**
	 * @return the legacyId
	 */
	public String getLegacyId() {
		return legacyId;
	}

	/**
	 * @param legacyId
	 *            the legacyId to set
	 */
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}

	/**
	 * @return the docketNumber
	 */
	public String getDocketNumber() {
		return docketNumber;
	}

	/**
	 * @param docketNumber
	 *            the docketNumber to set
	 */
	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	/**
	 * @return the court
	 */
	public Court getCourt() {
		if (court == null && publishingRequest != null && publishingRequest.getCourt() != null){
			return publishingRequest.getCourt();
		}
		return court;
	}

	public void setCourt(Court court) {
		this.court = court;
	}

	/**
	 * @return the yearFiled
	 */
	public int getYearFiled() {
		return this.yearFiled;
	}

	/**
	 * @param yearFiled
	 *            the yearFiled to set
	 */
	public void setYearFiled(int yearFiled) {
		this.yearFiled = yearFiled;
	}

	/**
	 * @return the productId
	 */
	public Product getProduct() {
		product = (product != null) ? product : publishingRequest.getProduct();
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	/**
	 * @return the acquisitionMethod
	 */
	public AcquisitionMethod getAcquisitionMethod() {
		return acquisitionMethod;
	}

	/**
	 * @param acquisitionMethod
	 *            the acquisitionMethod to set
	 */
	public void setAcquisitionMethod(AcquisitionMethod acquisitionMethod) {
		this.acquisitionMethod = acquisitionMethod;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	/**
	 * @param sourceFileName
	 *            the sourceFileName to set
	 */
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * @return the acquisitionTimestamp
	 */
	public Date getAcquisitionTimestamp() {
		return acquisitionTimestamp;
	}

	/**
	 * @param acquisitionTimestamp
	 *            the acquisitionTimestamp to set
	 */
	public void setAcquisitionTimestamp(Date acquisitionTimestamp) {
		this.acquisitionTimestamp = acquisitionTimestamp;
	}

	/**
	 * @return the vendorId
	 */
	public int getVendorId() {
		return vendorId;
	}

	/**
	 * @param vendorId
	 *            the vendorId to set
	 */
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @return the docketContentFileLocation
	 */
	public File getDocketContentFile() {
		return docketContentFile;
	}

	/**
	 * @param docketContentFileLocation
	 *            the docketContentFileLocation to set
	 */
	public void setDocketContentFile(File docketContentFile) {
		this.docketContentFile = docketContentFile;
	}

	/**
	 * @return the locationCode
	 */
	public String getLocationCode() {
		return locationCode;
	}

	/**
	 * @param locationCode
	 *            the locationCode to set
	 */
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getPhase() {
		return CONTENT_PHASE_SOURCE;
	}

	public String getDocketHistoryType() {
		return DOCKET_HISTORY_TYPE_ACQUISTION;
	}

	// ScrapedDocketData
	/**
	 * @return the requestName
	 */
	public String getRequestName() {
		return publishingRequest.getRequestName();
	}

	/**
	 * @return the isDeleteOperation
	 */
	public boolean isDeleteOperation() {
		return isDeleteOperation;
	}

	/**
	 * @param isDeleteOperation
	 *            the isDeleteOperation to set
	 */
	public void setDeleteOperation(boolean isDeleteOperation) {
		this.isDeleteOperation = isDeleteOperation;
	}

	/**
	 * @return the batchId
	 */
	public String getBatchId() {
		return batchId;
	}

	/**
	 * @param batchId
	 *            the batchId to set
	 */
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	/**
	 * 
	 * @param contentFileSize
	 */
	public void setContenFileSize(long contentFileSize) {
		this.contentFileSize = contentFileSize;
	}

	public long getContentFileSize() {
		return this.contentFileSize;
	}

	/**
	 * @return the sequenceNumber
	 */
	public String getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the docketTypeCode
	 */
	public String getDocketTypeCode() {
		return docketTypeCode;
	}

	/**
	 * @param docketTypeCode
	 *            the docketTypeCode to set
	 */
	public void setDocketTypeCode(String docketTypeCode) {
		this.docketTypeCode = docketTypeCode;
	}

	/**
	 * @return the countyId
	 */
	public Long getCountyId() {
		return countyId;
	}

	/**
	 * @param countyId
	 *            the countyId to set
	 */
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public Long getCaseTypeId() {
		return caseTypeId;
	}

	public void setCaseTypeId(Long caseTypeId) {
		this.caseTypeId = caseTypeId;
	}

	public String getPublishFlag() {
		return publishFlag;
	}

	public void setPublishFlag(String publishFlag) {
		this.publishFlag = publishFlag;
	}

	public Long getCaseSubTypeId() {
		return caseSubTypeId;
	}

	public void setCaseSubTypeId(Long caseSubTypeId) {
		this.caseSubTypeId = caseSubTypeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getFilingDate() {
		if (getCourt().getCourtCluster() != null && getCourt().getCourtCluster().equals("N_DNYCNTYCLERK")) {
			return DateUtils.getFormattedDate(filingDate, "MMddyyyy");
		} else {
			return DateUtils.getFormattedDate(filingDate, "yyyyMMdd");
		}
	}

	public String getFilingDateAsString() {
		return filingDate;
	}

	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}

	public PublishingRequest getPublishingRequest() {
		return publishingRequest;
	}
	/**
	 * @deprecated: should set individual values not entire request (for memory)
	 */
	@Deprecated 
	public void setPublishingRequest(PublishingRequest pubreq) {
		publishingRequest = pubreq;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCaseType() {
		return ((caseType != null) ? caseType : "");
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getScrapedTimestamp() {
		return scrapedTimestamp;
	}

	public void setScrapedTimestamp(String scrapedTimestamp) {
		this.scrapedTimestamp = scrapedTimestamp;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public String getCaseSubType() {
		return caseSubType;
	}

	public void setCaseSubType(String caseSubType) {
		this.caseSubType = caseSubType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMiscellaneous() {
		return miscellaneous;
	}

	public void setMiscellaneous(String miscellaneous) {
		this.miscellaneous = miscellaneous;
	}

	public String getSourceCaseId() {
		return sourceCaseId;
	}

	public void setSourceCaseId(String sourceCaseId) {
		this.sourceCaseId = sourceCaseId;
	}

	public String getAcquisitionStatus() {
		return acquisitionStatus;
	}

	public void setAcquisitionStatus(String acquisitionStatus) {
		this.acquisitionStatus = acquisitionStatus;
	}
	

	public DocketHistoryTypeEnum getDocketHistEnum() {
		return docketHistEnum;
	}

	public void setDocketHistEnum(DocketHistoryTypeEnum docketHistEnum) {
		this.docketHistEnum = docketHistEnum;
	}
}
