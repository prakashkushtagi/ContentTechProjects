/**
 * 
 */
package com.trgr.dockets.core.domain;

import java.io.File;


/**
 * Domain objects holds the necessary info to build a LTC request in aggregator.
 * 
 *
 */
public class BatchFileInfo
{

	private String batchId;
	private File novusFile;
	private File metadocFile;
	private File transitFile;
	private String collectionName;
	private String collectionNumber;
	private boolean novusAndMetadocLoad;
	private boolean novusOnly;
	private boolean metadocOnly;
	
	public BatchFileInfo()
	{
		
	}
	public BatchFileInfo(String batchId, File novusFile, File metadocFile,File transitFile, String collectionName, String collectionNumber) 
	{
		this.batchId = batchId;
		this.novusFile = novusFile;
		this.metadocFile = metadocFile;
		this.collectionName = collectionName;
		this.transitFile = transitFile;
		this.collectionNumber = collectionNumber;
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
	 * @return the novusFile
	 */
	public File getNovusFile() {
		return novusFile;
	}
	/**
	 * @param novusFile the novusFile to set
	 */
	public void setNovusFile(File novusFile) {
		this.novusFile = novusFile;
	}
	/**
	 * @return the metadocFile
	 */
	public File getMetadocFile() {
		return metadocFile;
	}
	/**
	 * @param metadocFile the metadocFile to set
	 */
	public void setMetadocFile(File metadocFile) {
		this.metadocFile = metadocFile;
	}
	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}
	/**
	 * @param collectionName the collectionName to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	/**
	 * @return the collectionName
	 */
	public String getCollectionNumber() {
		return collectionNumber;
	}
	/**
	 * @param collectionNumber the collectionNumber to set
	 */
	public void setCollectionId(String collectionNumber) {
		this.collectionNumber = collectionNumber;
	}
	/**
	 * @return the novusAndMetadocLoad
	 */
	public boolean isNovusAndMetadocLoad() {
		return novusAndMetadocLoad;
	}
	/**
	 * @param novusAndMetadocLoad the novusAndMetadocLoad to set
	 */
	public void setNovusAndMetadocLoad(boolean novusAndMetadocLoad) {
		this.novusAndMetadocLoad = novusAndMetadocLoad;
	}
	/**
	 * @return the novusOnly
	 */
	public boolean isNovusOnly() {
		return novusOnly;
	}
	/**
	 * @param novusOnly the novusOnly to set
	 */
	public void setNovusOnly(boolean novusOnly) {
		this.novusOnly = novusOnly;
	}
	/**
	 * @return the metadocOnly
	 */
	public boolean isMetadocOnly() {
		return metadocOnly;
	}
	/**
	 * @param metadocOnly the metadocOnly to set
	 */
	public void setMetadocOnly(boolean metadocOnly) {
		this.metadocOnly = metadocOnly;
	}
	/**
	 * @return the transitFile
	 */
	public File getTransitFile() {
		return transitFile;
	}
	/**
	 * @param transitFile the transitFile to set
	 */
	public void setTransitFile(File transitFile) {
		this.transitFile = transitFile;
	}
	
	
}
