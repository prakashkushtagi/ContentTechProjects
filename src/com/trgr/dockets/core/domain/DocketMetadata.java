/*
* Copyright 2011: Thomson Reuters Global Resources. All Rights Reserved.
* Proprietary and Confidential information of TRGR. Disclosure, Use or
* Reproduction without the written authorization of TRGR is prohibited
*/
package com.trgr.dockets.core.domain;

import com.thomson.judicial.dockets.bankruptcycontentservice.factory.ContentOperationFactory.OperationType;

public class DocketMetadata 
{
	private String legacyId;
	private String courtCluster;
	private String docketNumber;
	private String docketNumberAlt;
	private String title;
	private String caseType;
	private String caseSubType;
	private String dateFiled;
	private int yearFiled;
	private String scrapeDate;
	private String convertedDate;
	private String publishedDate;
	private String loadedDate;
	private String productDesc;
	private String productCode;
	private String acquisitionType;
	private String acquisitionMethod;
	private String caseStatus;
	private String sourceFileName;
	private String uuid;
	private String control;
	
	
	public void setControl(String control) 
	{
		this.control = control;
	}
	public String getControl() {
		return control;
	}	
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUuid() 
	{
		return uuid;
	}	
	
	public void setSourceFileName(String sourceFileName) 
	{
		this.sourceFileName = sourceFileName;
	}
	
	public String getSourceFileName() 
	{
		return sourceFileName;
	}	
	
	public void setLegacyId(String legacyId) 
	{
		this.legacyId = legacyId;
	}
	
	public String getLegacyId()
	{
		return legacyId;
	}
	
	public void setCourtCluster(String courtCluster)
	{
		this.courtCluster = courtCluster;
	}
	
	public String getCourtCluster() 
	{
		return courtCluster;
	}	
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return title;
	}
	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	public String getCaseType() {
		return caseType;
	}
	
	public void setCaseSubType(String caseSubType) 
	{
		this.caseSubType = caseSubType;
	}
	
	public String getCaseSubType() {
		return caseSubType;
	}
	public void setDateFiled(String dateFiled) {
		this.dateFiled = dateFiled;
	}
	public String getDateFiled() {
		return dateFiled;
	}
	public void setYearFiled(int yearFiled) {
		this.yearFiled = yearFiled;
	}
	public int getYearFiled() {
		return yearFiled;
	}	
	public void setScrapeDate(String scrapeDate) {
		this.scrapeDate = scrapeDate;
	}
	public String getScrapeDate() {
		return scrapeDate;
	}
	public void setConvertedDate(String convertedDate) {
		this.convertedDate = convertedDate;
	}
	public String getConvertedDate() {
		return convertedDate;
	}
	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getPublishedDate() {
		return publishedDate;
	}
	public void setLoadedDate(String loadedDate) {
		this.loadedDate = loadedDate;
	}
	public String getLoadedDate() {
		return loadedDate;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductCode() {
		return productCode;
	}	
	
	public void setAcquisitionType(String acquisitionType) {
		this.acquisitionType = acquisitionType;
	}
	public String getAcquisitionType() {
		return acquisitionType;
	}
	
	public void setAcquisitionMethod(String acquisitionMethod) {
		this.acquisitionMethod = acquisitionMethod;
	}
	public String getAcquisitionMethod() {
		return acquisitionMethod;
	}
	
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	public String getCaseStatus() {
		return caseStatus;
	}
	
	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}
	public String getDocketNumber() {
		return docketNumber;
	}	
	
	public void setDocketNumberAlt(String docketNumberAlt) {
		this.docketNumberAlt = docketNumberAlt;
	}
	public String getDocketNumberAlt() {
		return docketNumberAlt;
	}
	
	public OperationType getOperationType()
	{
		if (control.equals("ADD")) 
		{
			return OperationType.ADD;
		} 
		else if (control.equals("UPDATE"))
		{
			return OperationType.UPDATE;
		}
		else if (control.equalsIgnoreCase("DEL") || control.equalsIgnoreCase("DELETE"))
		{
			return OperationType.REMOVE;
		}
		else
		{
			return OperationType.ADD;
		}
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer(100);
		buf.append("\nLegacy ID: ");
		buf.append(legacyId);
		buf.append("\nControl: ");
		buf.append(control);
		buf.append("\nCourt Cluster: ");
		buf.append(courtCluster);
		buf.append("\nDocket Number: ");
		buf.append(docketNumber);
		buf.append("\nDocket Number Alt: ");
		buf.append(docketNumberAlt);
		buf.append("\nTitle: ");
		buf.append(title);
		buf.append("\nCase Type: ");
		buf.append(caseType);
		buf.append("\nCase SubType: ");
		buf.append(caseSubType);
		buf.append("\nDate Filed: ");
		buf.append(dateFiled);
		buf.append("\nScrape Date: ");
		buf.append(scrapeDate);
		buf.append("\nConverted Date: ");
		buf.append(convertedDate);
		buf.append("\nProduct Desc: ");
		buf.append(productDesc);
		buf.append("\nProduct Code: ");
		buf.append(productCode);
		buf.append("\nAcquisition Type: ");
		buf.append(acquisitionType);
		buf.append("\nAcquisition Method: ");
		buf.append(acquisitionMethod);
		buf.append("\nCase Status: ");
		buf.append(caseStatus);
		return buf.toString();
	}
}
