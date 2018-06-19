/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author u0160964 Sagun Khanal(Sagun.Khanal@thomsonreuters.com) 
 *
 */

@Entity
@Table(name="EVENT", schema="DOCKETS_PUB")
public class EventLog implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String eventId;
	private String eventName;
	private String eventTimestamp;
	private String batchId;
	private String subBatchId;
	private String appServer;
	private String numDocs;
	private String fileName;
	private String fileSize;
	private String errorCode;
	private String errorDescription;
	private String logFolder;
	private String errorFolder;
	private String droppedDockets;
	private String processedFlag;
	private String payload;
	private String workFolder;
	private String novusLoadFolder;
	
	public EventLog (String eventName, String eventTimestamp, String batchId,
			String subBatchId, String appServer, String numDocs, String fileName,
			String fileSize, String errorCode, String errorDescription, String logFolder,
			String errorFolder, String droppedDockets, String processedFlag, String payload, String workFolder, String novusLoadFolder){
		super();
		this.eventName = eventName;
		this.eventTimestamp = eventTimestamp;
		this.batchId = batchId;
		this.subBatchId = subBatchId;
		this.appServer = appServer;
		this.numDocs = numDocs;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		this.logFolder = logFolder;
		this.errorFolder = errorFolder;
		this.droppedDockets = droppedDockets;
		this.processedFlag = processedFlag;
		this.payload = payload;
		this.workFolder = workFolder;
		this.novusLoadFolder = novusLoadFolder;
	
	}
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name="EVENT_ID", unique = true)
	public String getEventId(){
		return eventId;
	}
	
	public void setEventId(String eventId){
		this.eventId = eventId;
	}
	
	@Column (name="EVENT_NAME", nullable = false)
	public String geteventName(){
		return eventName;
	}
	
	public void setEventName(String eventName){
		this.eventName = eventName;
	}
	
	@Column (name="EVENT_TIMESTAMP", nullable = false)
	public String geteventTimestamp(){
		return eventTimestamp;
	}
	
	public void setEventTimestamp(String eventTimestamp){
		this.eventTimestamp = eventTimestamp;
	}
	
	@Column(name="BATCH_ID", nullable = false)
	public String getBatchId(){
		return batchId;
	}
	
	public void setBatchId(String batchId){
		this.batchId = batchId;
	}
	
	@Column(name="SUB_BATCH_ID", nullable = true)
	public String getsubBatchId(){
		return subBatchId;
	}
	
	public void setSubBatchId(String subBatchId){
		this.subBatchId = subBatchId;
	}
	
	@Column(name="APP_SERVER", nullable = true)
	public String getAppServer(){
		return appServer;
	}
	
	public void setAppServer(String appServer){
		this.appServer = appServer;
	}
	
	@Column(name="NUM_DOCS", nullable = true)
	public String getNumDocs(){
		return numDocs;
	}
	
	public void setNumDocs(String numDocs){
		this.numDocs = numDocs;
	}
	
	@Column(name="FILE_NAME", nullable = true)
	public String getFileName(){
		return fileName;
	}
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	@Column(name="FILE_SIZE", nullable = true)
	public String getFileSize(){
		return fileSize;
	}
	
	public void setFileSize(String fileSize){
		this.fileSize = fileSize;
	}
	
	@Column(name="ERROR_CODE", nullable = true)
	public String getErrorCode(){
		return errorCode;
	}
	
	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}
	
	@Column(name="ERROR_DESCRIPTION", nullable = true)
	public String getErrorDescription(){
		return errorDescription;
	}
	
	public void setErrorDescription(String errorDescription){
		this.errorDescription = errorDescription;
	}
	
	@Column(name="LOG_FOLDER", nullable = true)
	public String getLogFolder(){
		return logFolder;
	}
	
	public void setLogFolder(String logFolder){
		this.logFolder = logFolder;
	}
	
	@Column(name="ERROR_FOLDER", nullable = true)
	public String getErrorFolder(){
		return errorFolder;
	}
	
	public void setErrorFolder(String errorFolder){
		this.errorFolder = errorFolder;
	}
	
	@Column(name="DROPPED_DOCKETS", nullable = true)
	public String getDroppedDockets(){
		return droppedDockets;
	}
	
	public void setDroppedDockets(String droppedDockets){
		this.droppedDockets = droppedDockets;
	}
	
	@Column(name="PROCESSED_FLAG", nullable = false)
	public String getProcessedFlag(){
		return processedFlag;
	}
	
	public void setProcessedFlag(String processedFlag){
		this.processedFlag = processedFlag;
	}
	
	@Column(name="PAYLOAD", nullable = true)
	public String getPayload(){
		return payload;
	}
	
	public void setPayload(String payload){
		this.payload = payload;
	}
	
	@Column(name="WORK_FOLDER", nullable = true)
	public String getWorkFolder(){
		return workFolder;
	}
	
	public void setWorkFolder(String workFolder){
		this.workFolder = workFolder;
	}
	
	@Column(name="NOVUS_LOAD_FOLDER", nullable = true)
	public String getNovusLoadFolder(){
		return novusLoadFolder;
	}
	
	public void setNovusLoadFolder(String novusLoadFolder){
		this.novusLoadFolder = novusLoadFolder;
	}

}
