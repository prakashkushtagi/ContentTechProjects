/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name="LOAD_MONITOR_LTC_MESSAGE",schema="DOCKETS_PUB")
public class LoadMonitorLtcMessage implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	public static final String LTC_MESSAGE_ID = "messageId";
	public static final String LTC_EVENT_ID = "eventId";
	public static final String LTC_COLLECTION = "collection";
	public static final String LTC_LOAD_STATUS = "loadStatus";
	public static final String LTC_LOAD_ELEMENT = "loadElement";
	public static final String LTC_LOAD_DATASET_ID = "loadDatasetId";
	public static final String LTC_LOAD_REQUEST_ID = "loadDatasetRequestId";
	public static final String LTC_COLLABORATION_KEY = "collaborationKey";
	public static final String LTC_FILENAME = "ltcFilename";
	public static final String LTC_SPLIT_EVENTS = "splitEvents";
	public static final String LTC_ORIGINAL_MESSAGE_TXT = "originalMessageTxt";
	public static final String STATUS_ID = "statusId";
	public static final String ERROR_DESCRIPTION = "errorDescription";
	public static final String LMS_TIMESTAMP = "lmsTimestamp";
	public static final String LTC_EVENT_TIMESTAMP = "ltcEventTimestamp";
	 /**  */
    @Id
    @Column(name = "MESSAGE_ID", unique=true, nullable=false)
    private String messageId;

    @Column(name = "EVENT_ID")
    private long eventId;

    @Column(name = "COLLECTION")
    private String collection;
    
    /**  */
    @Column(name = "LOAD_STATUS")
    private String loadStatus;

    /**  */
    @Column(name = "LOAD_ELEMENT")
    private String loadElement;

    /**  */
    @Column(name = "LOAD_DATASET_ID")
    private String loadDatasetId;


    @Column(name = "LOAD_REQUEST_ID")
    private String loadDatasetRequestId;

    @Column(name = "COLLABORATION_KEY")
    private String collaborationKey;

    @Column(name = "FILENAME")
    private String ltcFilename;	

    @Lob
    @Column(name = "SPLIT_EVENTS")
    private String splitEvents;	
    
    @Lob
    @Column(name = "ORIGINAL_MESSAGE_TXT")
    private String originalMessageTxt;
    
    @Column(name = "STATUS_ID")
    private String statusId;
    
    @Column(name = "ERROR_DESCRIPTION")
    private String errorDescription;
    
    @Column(name = "LMS_TIMESTAMP")
    private Date lmsTimestamp;
    
    @Column(name = "LTC_EVENT_TIMESTAMP")
    private Date ltcEventTimestamp;
    
    @Column(name = "PRODUCT_ID")
    private Long productId;
    
    @Lob
    @Column(name = "LEGACY_IDS_IN_FILE")
    private String legacyIdsInFile;
    
	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if ((this == other))
        {
            return true;
        }

        if ((other == null))
        {
            return false;
        }

        if (!(other instanceof LoadMonitorLtcMessage))
        {
            return false;
        }

        LoadMonitorLtcMessage otherMessage = (LoadMonitorLtcMessage) other;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(this.messageId, otherMessage.getMessageId());
        equalsBuilder.append(this.eventId, (otherMessage.getEventId()).longValue());

        return equalsBuilder.isEquals();
    }

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.messageId);
        hashCodeBuilder.append(this.eventId);

        return hashCodeBuilder.toHashCode();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("LoadMonitorLtcMessage@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("messageId='").append(getMessageId()).append("' ");
        buffer.append("eventId='").append(getEventId()).append("' ");
        buffer.append("load element ='").append(getLoadElement()).append("' ");
        buffer.append("load status ='").append(getLoadStatus()).append("' ");
        buffer.append("collaborationKey='").append(getCollaborationKey()).append("' ");
        buffer.append("collection='").append(getCollection()).append("' ");
        buffer.append("loaddatasetid='").append(getLoadDatasetId()).append("' ");
        buffer.append("filename='").append(getLtcFilename()).append("' ");
        buffer.append("error Description ='").append(getErrorDescription()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the eventId
	 */
	public Long getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(long eventId) {
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
	 * @return the loadStatus
	 */
	public String getLoadStatus() {
		return loadStatus;
	}

	/**
	 * @param loadStatus the loadStatus to set
	 */
	public void setLoadStatus(String loadStatus) {
		this.loadStatus = loadStatus;
	}

	/**
	 * @return the loadElement
	 */
	public String getLoadElement() {
		return loadElement;
	}

	/**
	 * @param loadElement the loadElement to set
	 */
	public void setLoadElement(String loadElement) {
		this.loadElement = loadElement;
	}

	/**
	 * @return the loadDatasetId
	 */
	public String getLoadDatasetId() {
		return loadDatasetId;
	}

	/**
	 * @param loadDatasetId the loadDatasetId to set
	 */
	public void setLoadDatasetId(String loadDatasetId) {
		this.loadDatasetId = loadDatasetId;
	}

	/**
	 * @return the loadDatasetRequestId
	 */
	public String getLoadDatasetRequestId() {
		return loadDatasetRequestId;
	}

	/**
	 * @param loadDatasetRequestId the loadDatasetRequestId to set
	 */
	public void setLoadDatasetRequestId(String loadDatasetRequestId) {
		this.loadDatasetRequestId = loadDatasetRequestId;
	}

	/**
	 * @return the collaborationKey
	 */
	public String getCollaborationKey() {
		return collaborationKey;
	}

	/**
	 * @param collaborationKey the collaborationKey to set
	 */
	public void setCollaborationKey(String collaborationKey) {
		this.collaborationKey = collaborationKey;
	}

	/**
	 * @return the ltcFilename
	 */
	public String getLtcFilename() {
		return ltcFilename;
	}

	/**
	 * @param ltcFilename the ltcFilename to set
	 */
	public void setLtcFilename(String ltcFilename) {
		this.ltcFilename = ltcFilename;
	}

	/**
	 * @return the splitEvents
	 */
	public String getSplitEvents() {
		return splitEvents;
	}

	/**
	 * @param splitEvents the splitEvents to set
	 */
	public void setSplitEvents(String splitEvents) {
		this.splitEvents = splitEvents;
	}

	/**
	 * @return the originalMessageTxt
	 */
	public String getOriginalMessageTxt() {
		return originalMessageTxt;
	}

	/**
	 * @param originalMessageTxt the originalMessageTxt to set
	 */
	public void setOriginalMessageTxt(String originalMessageTxt) {
		this.originalMessageTxt = originalMessageTxt;
	}

	/**
	 * @return the messageProcessStatus
	 */
	public String getStatusId() {
		return statusId;
	}

	/**
	 * @param messageProcessStatus the messageProcessStatus to set
	 */
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * @return the lmsTimestamp
	 */
	public Date getLmsTimestamp() {
		return lmsTimestamp;
	}

	/**
	 * @param lmsTimestamp the lmsTimestamp to set
	 */
	public void setLmsTimestamp(Date lmsTimestamp) {
		this.lmsTimestamp = lmsTimestamp;
	}

	/**
	 * @return the ltcEventTimestamp
	 */
	public Date getLtcEventTimestamp() {
		return ltcEventTimestamp;
	}

	/**
	 * @param ltcEventTimestamp the ltcEventTimestamp to set
	 */
	public void setLtcEventTimestamp(Date ltcEventTimestamp) {
		this.ltcEventTimestamp = ltcEventTimestamp;
	}

	/**
	 * @return the productId
	 */
	public long getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}

	/**
	 * @return the legacyIdsInFile
	 */
	public String getLegacyIdsInFile() {
		return legacyIdsInFile;
	}

	/**
	 * @param legacyIdsInFile the legacyIdsInFile to set
	 */
	public void setLegacyIdsInFile(String legacyIdsInFile) {
		this.legacyIdsInFile = legacyIdsInFile;
	}

}
