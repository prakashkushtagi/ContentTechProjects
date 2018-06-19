package com.trgr.dockets.core.domain;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class LtcAggregateFileInfo {

	private File novusLoadFile;
	private File metadocLoadFile;
	private String collectionName;
	private String collabKeySuffix;
	private List<BatchFileInfo> batchesIncluded;
	
	public LtcAggregateFileInfo(File novusLoadFile, File metadocLoadFile, String collectionName, List<BatchFileInfo> batchesIncluded, String collabKeySuffix){
		this.novusLoadFile = novusLoadFile;
		this.metadocLoadFile = metadocLoadFile;
		this.collectionName = collectionName;
		this.batchesIncluded = batchesIncluded;
		this.collabKeySuffix = collabKeySuffix;
	}
	
	public LtcAggregateFileInfo(File novusLoadFile, File metadocLoadFile, String collectionName, List<BatchFileInfo> batchesIncluded){
		this.novusLoadFile = novusLoadFile;
		this.metadocLoadFile = metadocLoadFile;
		this.batchesIncluded = batchesIncluded;
		this.collectionName = collectionName;
	}
	
	public File getNovusLoadFile() {
		return novusLoadFile;
	}

	public void setNovusLoadFile(File novusLoadFile) {
		this.novusLoadFile = novusLoadFile;
	}

	public File getMetadocLoadFile() {
		return metadocLoadFile;
	}


	public void setMetadocLoadFile(File metadocLoadFile) {
		this.metadocLoadFile = metadocLoadFile;
	}
	
	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	/**
	 * @return empty string if there is no collab key suffix. Prepends a "_" if one doesn't exist (this is due to
	 * how LMS parses the request collaboration key)
	 */
	public String getCollabKeySuffix() {
		if (collabKeySuffix == null){
			return "";
		} else if (!collabKeySuffix.startsWith("_")){
			return "_" + collabKeySuffix;
		}
		return collabKeySuffix;
	}
	
	
	/**
	 * @return Transit file name should always match the novus load file name except for the ending of "xml.transit.zip"
	 */
	public String getTransitZipFileName() {
		if (novusLoadFile != null && StringUtils.isNotBlank(novusLoadFile.getName())){
			String fileName = novusLoadFile.getName();
			fileName = fileName.replaceAll("xml.zip", "xml.transit.zip");
			return fileName;
		} else {
			return "";
		}
	}
	
	public void setCollabKeySuffix(String collabKeySuffix) {
		this.collabKeySuffix = collabKeySuffix;
	}

	public List<BatchFileInfo> getBatchesIncluded() {
		return batchesIncluded;
	}

	public void setBatchesIncluded(List<BatchFileInfo> batchesIncluded) {
		this.batchesIncluded = batchesIncluded;
	}


	
}
