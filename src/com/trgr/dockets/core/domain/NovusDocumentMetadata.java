/*
* Copyright 2011: Thomson Reuters Global Resources. All Rights Reserved.
* Proprietary and Confidential information of TRGR. Disclosure, Use or
* Reproduction without the written authorization of TRGR is prohibited
*/
package com.trgr.dockets.core.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO that represents the parsed out metadata for a Docket document.
 *
 * @author <a href="mailto:Selvedin.Alic@thomsonreuters.com">Selvedin Alic</a> u0095869
 */
public class NovusDocumentMetadata 
{
	private String legacyId;
	private String uuid;
	private String control;	
	private String publishDate;
	private String docketNumber;
	private String productCode;
	private String vendorCode;
	private String scrapeDate;
	private String convertDate;
	private boolean isValid;
	private StringBuffer invalidReason;
	private String fileName;
	private String caseStatus = "";
	private String courtNorm;
	private String sequence;
	private String type;
	private String location;
	private String caseType;
	private String caseSubType;
	private String filedDate;
	private String filedYear;
	private String title;
	private List <String> legacyIdList = new ArrayList <String> ();
	
	
	public NovusDocumentMetadata()
	{

	}
	
	public NovusDocumentMetadata(String fileName)
	{
		this.fileName = fileName;
	}
	
	public String getFileName()
	{
		return this.fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getCaseType() {
		return caseType;
	}		
	
	public void setCaseSubType(String caseSubType) {
		this.caseSubType = caseSubType;
	}
	public String getCaseSubType() {
		return caseSubType;
	}		
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}		
	
	public void setFiledDate(String filedDate) {
		this.filedDate = filedDate;
	}
	public String getFiledDate() {
		return filedDate;
	}		

	public void setFiledYear(String filedYear) {
		this.filedYear = filedYear;
	}
	public String getFiledYear() {
		return filedYear;
	}		
	
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getSequence() {
		return sequence;
	}	
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}	
	
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocation() {
		return location;
	}	
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUuid() {
		return uuid;
	}		
	
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
		this.legacyIdList.add(legacyId);
	}
	public String getLegacyId() {
		return legacyId;
	}
	
	public void setControl(String control) {
		this.control = control;
	}
	public String getControl() {
		return control;
	}	
	
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getPublishDate() {
		return publishDate;
	}
	
	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}
	public String getDocketNumber() {
		return docketNumber;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductCode() {
		return productCode;
	}
	
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	
	public void setScrapeDate(String scrapeDate) {
		this.scrapeDate = scrapeDate;
	}
	public String getScrapeDate() {
		return scrapeDate;
	}
	
	public void setConvertDate(String convertDate) {
		this.convertDate = convertDate;
	}
	public String getConvertDate() {
		return convertDate;
	}
	
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	public String getCaseStatus() {
		return caseStatus;
	}		
	
	public void setCourtNorm(String courtNorm) {
		this.courtNorm = courtNorm;
	}
	public String getCourtNorm() {
		return courtNorm;
	}			
	public String getInvalidReason()
	{
		if (invalidReason != null) 
		{
			return invalidReason.toString();
		}
		else
		{
			return null;
		}
	}
	
	public List<String> getLegacyIds()
	{
		return legacyIdList;
	}

	
	public boolean isValid()
	{
		//validity check for required fields
		isValid = true;
		invalidReason = new StringBuffer();
		if (legacyId == null) 
		{
			invalidReason.append("LegacyId cannot be null\n");
			isValid = false;
		} 
		if (uuid == null) 
		{
			invalidReason.append("uuid cannot be null\n");
			isValid = false;
		}
		if (publishDate == null) 
		{
			invalidReason.append("publishDate cannot be null\n");
			isValid = false;
		}
		if (docketNumber == null) 
		{
			invalidReason.append("docketNumber cannot be null\n");
			isValid = false;
		}
		if (productCode == null) 
		{
			invalidReason.append("productCode cannot be null\n");
			isValid = false;
		}
		/*if (vendorCode == null) 
		{
			invalidReason.append("vendorCode cannot be null\n");
			isValid = false;
		}*/
		return isValid;
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer(100);
		buf.append("Legacy ID: ");
		if (legacyId != null)
			buf.append(legacyId);
		buf.append("\nDocketNumber: ");
		if (docketNumber != null)
			buf.append(docketNumber);		
		buf.append("\nUuid: ");
		if (uuid != null)
			buf.append(uuid);
		buf.append("\nControl: ");
		if (control != null)
			buf.append(control);	
		buf.append("\nPublishDate: ");
		if (publishDate != null)
			buf.append(publishDate);
		buf.append("\nProductCode: ");
		if (productCode != null)
			buf.append(productCode);
		buf.append("\nVendorCode: ");
		if (vendorCode != null)
			buf.append(vendorCode);
		buf.append("\nScrapeDate: ");
		if (scrapeDate != null)
			buf.append(scrapeDate);
		buf.append("\nConvertDate: ");
		if (convertDate != null)
			buf.append(convertDate);
		buf.append("\nFileName: ");
		if (fileName != null)
			buf.append(fileName);
		buf.append("\nCaseStatus: ");
		if (caseStatus != null)
			buf.append(caseStatus);
		buf.append("\nCourtNorm: ");
		if (courtNorm != null)
			buf.append(courtNorm);				
		buf.append("\nSequence: ");
		if (sequence != null)
			buf.append(sequence);
		buf.append("\nType: ");
		if (type != null)
			buf.append(type);
		buf.append("\nLocation: ");
		if (location != null)
			buf.append(location);				
		buf.append("\nCaseType: ");
		if (caseType != null)
			buf.append(caseType);
		buf.append("\nCaseSubType: ");
		if (caseSubType != null)
			buf.append(caseSubType);
		buf.append("\nFiledDate: ");
		if (filedDate != null)
			buf.append(filedDate);
		buf.append("\nFiledYear: ");
		if (filedYear != null)
			buf.append(filedYear);
		buf.append("\nTitle: ");
		if (title != null)
			buf.append(title);
		
		return buf.toString().trim();
	}

}
