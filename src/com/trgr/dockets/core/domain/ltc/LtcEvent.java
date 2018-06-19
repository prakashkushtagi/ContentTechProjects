/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.domain.ltc;

import java.util.ArrayList;
import java.util.List;



public class LtcEvent {

	private String eventId;
	private String eventTimestamp;
	private String collection;
	private String eventLoadStatus;
	private String eventLoadElementStep;
	private String systemMessage;
	private List<LoadDataset> loadDatasetList;
	
	/**
	 * @return the eventTimestamp
	 */
	public String getEventTimestamp() {
		return eventTimestamp;
	}
	/**
	 * @param eventTimestamp the eventTimestamp to set
	 */
	public void setEventTimestamp(String eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}
	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return the collection
	 */
	public String getCollection() {
		return collection;
	}
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}
	/**
	 * @return the eventLoadStatus
	 */
	public String getEventLoadStatus() {
		return eventLoadStatus;
	}
	/**
	 * @param eventLoadStatus the eventLoadStatus to set
	 */
	public void setEventLoadStatus(String eventLoadStatus) {
		this.eventLoadStatus = eventLoadStatus;
	}
	/**
	 * @return the eventLoadElementStep
	 */
	public String getEventLoadElementStep() {
		return eventLoadElementStep;
	}
	/**
	 * @param eventLoadElementStep the eventLoadElementStep to set
	 */
	public void setEventLoadElementStep(String eventLoadElementStep) {
		this.eventLoadElementStep = eventLoadElementStep;
	}
	/**
	 * @return the systemMessage
	 */
	public String getSystemMessage() {
		return systemMessage;
	}
	/**
	 * @param systemMessage the systemMessage to set
	 */
	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}
	/**
	 * @return the loadDatasetList
	 */
	public List<LoadDataset> getLoadDatasetList() {
		return loadDatasetList;
	}
	/**
	 * @param loadDatasetList the loadDatasetList to set
	 */
	public void setLoadDatasetList(List<LoadDataset> loadDatasetList) {
		this.loadDatasetList = loadDatasetList;
	}
	public void addLoadDataset(String loadDatasetId,String loadRequestId,LtcCollaborationKey requestCollaborationKey, String requestId, String uberBatchId,String batchId, String subBatchId,String fileName)
	{
		LoadDataset loadDataset = new LoadDataset(loadDatasetId, loadRequestId, requestCollaborationKey, requestId, uberBatchId,batchId, subBatchId, fileName);
		if(getLoadDatasetList() == null)
		{
			this.loadDatasetList = new ArrayList<LoadDataset>();
		}
		getLoadDatasetList().add(loadDataset);
	}
	
}
