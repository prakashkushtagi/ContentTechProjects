/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.processor;





public class SourceLoadRequest{

	private Long requestId;
	private String batchId;
	private String publishingRequestId;
	private String productCode;
	private String sourcePath;
	private String workFolder;
	private String acquisitionType;
	private String acquisitionMethod;
	private String acquisitionStart;
	private boolean deleteFlag;
	
	
	public SourceLoadRequest(String batchId,String productCode, String updateType, String sourcePath, String workFolder, String acquisitionType, String acquisitionMethod, String acquisitionStart, boolean deleteFlag, String publishingRequestId) 
	{
		super();
		this.batchId = batchId;
		this.productCode = productCode;
		this.sourcePath = sourcePath;
		this.workFolder = workFolder;
		this.acquisitionType = acquisitionType;
		this.acquisitionMethod = acquisitionMethod;
		this.acquisitionStart = acquisitionStart;
		this.deleteFlag = deleteFlag;
		this.publishingRequestId = publishingRequestId;
	}
	/**
	 * 
	 */
	public SourceLoadRequest() {
		
	}
	
	@Override
	public String toString() {
		return "SourceLoadRequest [requestId=" + requestId + ", publishingRequestId=" + publishingRequestId + ", batchId="
				+ batchId + ", productCode=" + productCode + ", sourcePath="
				+ sourcePath + ", workFolder="
						+ workFolder + ", acquisitionType=" + acquisitionType
				+ ", acquisitionMethod=" + acquisitionMethod + ", timestamp="
				+ acquisitionStart + ", deleteFlag=" + deleteFlag + "]";
	}
	/**
	 * @return the requestId
	 */
	public Long getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the batchId
	 */
	public String getBatchId() {
		return batchId;
	}
	/**
	 * @param batchId the batchId to set
	 */
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * @return the sourcePath
	 */
	public String getSourcePath() 
	{
		if(sourcePath!=null && !sourcePath.isEmpty())
  	   {
  		   sourcePath = sourcePath.replaceAll("\\\\", "/");
  	   }
		return sourcePath;
	}
	/**
	 * @param sourcePath the sourcePath to set
	 */
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	/**
	 * @return the acquisitionType
	 */
	public String getAcquisitionType() {
		return acquisitionType;
	}
	/**
	 * @param acquisitionType the acquisitionType to set
	 */
	public void setAcquisitionType(String acquisitionType) {
		this.acquisitionType = acquisitionType;
	}
	/**
	 * @return the acquisitionMethod
	 */
	public String getAcquisitionMethod() {
		return acquisitionMethod;
	}
	/**
	 * @param acquisitionMethod the acquisitionMethod to set
	 */
	public void setAcquisitionMethod(String acquisitionMethod) {
		this.acquisitionMethod = acquisitionMethod;
	}

	/**
	 * @return the deleteFlag
	 */
	public boolean isDeleteFlag() {
		return deleteFlag;
	}
	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	/**
	 * @return the acquisitionStart
	 */
	public String getAcquisitionStart() {
		return acquisitionStart;
	}
	/**
	 * @param acquisitionStart the acquisitionStart to set
	 */
	public void setAcquisitionStart(String acquisitionStart) {
		this.acquisitionStart = acquisitionStart;
	}
	/**
	 * @return the workFolder
	 */
	public String getWorkFolder() 
	{
		if(null!=workFolder&& !workFolder.isEmpty())
		{
			workFolder = workFolder.replaceAll("\\\\", "/");
			if (!workFolder.endsWith("/"))
			{
				workFolder = workFolder + "/";
			}
		}
		return workFolder;
	}
	/**
	 * @param workFolder the workFolder to set
	 */
	public void setWorkFolder(String workFolder) 
	{
		this.workFolder = workFolder;
	}
	/**
	 * @return the publishingRequestId
	 */
	public String getPublishingRequestId() 
	{
		return publishingRequestId;
	}
	/**
	 * @param publishingRequestId the publishingRequestId to set
	 */
	public void setPublishingRequestId(String publishingRequestId) 
	{
		this.publishingRequestId = publishingRequestId;
	}
	

}
