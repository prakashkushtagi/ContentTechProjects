/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.domain;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingRequest;

public class JudicialDocketMetadata {
	public static final String DOCKET_HISTORY_TYPE_ACQUISTION = "CONVERSION_HISTORY";
	
	private PublishingRequest publishingRequest;
	private DocketVersion docketVersion;
	private File jaxmlFile;  	// the generated JAXML content file
	private String batchId;		// the processing set of dockets from which this docket came.
	private Integer yearFiled;
	private String locationCode;
	private Date scrapeTimeStamp;
	private boolean isDeleteOperation;
	private LargeDocketInfo largeDocketInfo;
	private String sequenceNumber;
	/**
	 * Partial constructor that handles the data we currently have before us.
	 */
	public JudicialDocketMetadata (PublishingRequest pubReq, DocketVersion docketVersion, File jaxmlOutputFile, String batchId) {
		this.publishingRequest = pubReq;
		this.docketVersion = docketVersion;
		this.jaxmlFile = jaxmlOutputFile;
		this.batchId = batchId;
		this.locationCode = null;
		this.yearFiled = null;
		this.sequenceNumber = null;
	}
	
	public AcquisitionMethod getAcquisitionMethod() {
		return docketVersion.getAcquisitionMethod();
	}
	public boolean isLargeDocket(){
		char largeDocketFlag = docketVersion.getDocket().getLargeDocketFlag();
		if (largeDocketFlag == 'Y'){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * @return The ID for the batch representing the set of dockets the current processing job is handling.
	 */
	public String getBatchId() {
		return batchId;
	}
	public Court getCourt() {
		return publishingRequest.getCourt();
	}
	public String getDocketHistoryType() {
		return DOCKET_HISTORY_TYPE_ACQUISTION;
	}
	public String getDocketNumber() {
		return docketVersion.getDocket().getDocketNumber();
	}
	public DocketVersion getDocketVersion() {
		return docketVersion;
	}
	/**
	 * Returns the size in bytes of the JAXML content file.
	 * @return the size of the file in bytes, or 0 if it does not exist, or -1 if the file property is null.
	 */
	public long getJaxmlContentSize() {
		return (jaxmlFile != null) ? jaxmlFile.length() : -1;
	}
	/**
	 * @return The file that (will) contain the JA-XML content.
	 */
	public File getJaxmlFile() {
		return jaxmlFile;
	}
	public String getLegacyId() {
		return docketVersion.getDocket().getPrimaryKey();
	}
	public String getLocationCode() {
		return locationCode;
	}
	public String getSequenceNumber(){
		return sequenceNumber;
	}

	public Phase getPhase() {
		return Phase.JUDICIAL;
	}
	public Product getProduct() {
		return docketVersion.getDocket().getProduct();
	}
	public PublishingRequest getPublishingRequest() {
		return publishingRequest;
	}
	public RequestInitiatorTypeEnum getRequestInitiatorType() {
		return docketVersion.getRequestInitiatorType();
	}
	public String getRequestName() {
		return publishingRequest.getRequestName();
	}
	public Date getScrapeTime() {
		return docketVersion.getScrapeTimestamp();
	}
	/**
	 * Returns the file containing the source XML that was converted to JA-XML.
	 * @return The docket source file.
	 */
	public File getSourceFile() {
		return docketVersion.getSourceFile();
	}
	/**
	 * Determine who the vendor is as a function of the product and/or the court.
	 */
	public long getVendor() {
		if (publishingRequest.getVendorId() != 0){
			return publishingRequest.getVendorId();
		}		
		DocketEntity docket = docketVersion.getDocket();
		Court court = docket.getCourt();
		
		//TODO: This bandaid is for protecting New York and JPML from changes because of business concerns.
		long courtId = court.getPrimaryKey();
		if ( courtId == 95L || courtId == 96L || courtId == 97L ){
			return 9L;
		}
		else if ( courtId == 94L ){
			return 7L;
		}
		else if ( courtId == 98L ){
			return 11L;
		}
		///////////////////////////////////////////////////////////////////////////////

		throw new NotImplementedException("Programming error/gap: Could not determine the vendor for: " + docket); 
	}
	public Integer getYearFiled() {
		return yearFiled;
	}
	public boolean isDeleteOperation() {
		return isDeleteOperation;
	}
	public void setDeleteOperation(boolean val) {
		isDeleteOperation = val;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * @return the scrapeTimeStamp
	 */
	public Date getScrapeTimeStamp() {
		return scrapeTimeStamp;
	}
	
	public LargeDocketInfo getLargeDocketInfo() {
		if(null == largeDocketInfo){
			largeDocketInfo = new LargeDocketInfo();		
		}
		return largeDocketInfo;
	}
	
	/**
	 * @param scrapeTimeStamp the scrapeTimeStamp to set
	 */
	public void setScrapeTimeStamp(Date scrapeTimeStamp) {
		this.scrapeTimeStamp = scrapeTimeStamp;
	}
	
	public void setJaxmlFile(File jaxmlFile){
		this.jaxmlFile = jaxmlFile;
	}

	public void setLargeDocketInfo(LargeDocketInfo largeDocketInfo) {
		this.largeDocketInfo = largeDocketInfo;
	}
	public void setSequenceNumber(String sequenceNumber){
		this.sequenceNumber = sequenceNumber;
	}
	public void setLocationCode(String locationCode){
		this.locationCode = locationCode;
	}
}
