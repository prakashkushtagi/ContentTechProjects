/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.domain;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 * 
 */

@XmlRootElement(name = "event.collection")
public class LtcEvent {
	
	
	public LtcEvent(){
		super();
	}
	
/*	public String getNumber() {
		return number;
	}
	@XmlAttribute
	public void setNumber(String number) {
		this.number = number;
	}
*/
	
	private String fileName;
	private String loadStatus;
	private String eventTimestamp;
	private String collaborationKey;

	
	public String getFileName(){
		return fileName;
	}
	
	@XmlElement(name= "load.dataset")
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getEventTimestamp() {
		return eventTimestamp;
	}

	@XmlElement(name = "event.datetime")
	public void setEventTimestamp(String eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}


	public String getCollaborationKey() {
		return collaborationKey;
	}

	@XmlElement(name = "subscription.collaboration.key")
	public void setCollaborationKey(String collaborationKey) {
		this.collaborationKey = collaborationKey;
	}
	
	public String getLoadStatus(){
		return loadStatus;
	}
	
	@XmlElement(name="load.status")
	public void setLoadStatus(String loadStatus){
		this.loadStatus = loadStatus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((collaborationKey == null) ? 0 : collaborationKey.hashCode());
		result = prime * result
				+ ((eventTimestamp == null) ? 0 : eventTimestamp.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((loadStatus == null) ? 0 : loadStatus.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LtcEvent other = (LtcEvent) obj;
		if (collaborationKey == null) {
			if (other.collaborationKey != null)
				return false;
		} else if (!collaborationKey.equals(other.collaborationKey))
			return false;
		if (eventTimestamp == null) {
			if (other.eventTimestamp != null)
				return false;
		} else if (!eventTimestamp.equals(other.eventTimestamp))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (loadStatus == null) {
			if (other.loadStatus != null)
				return false;
		} else if (!loadStatus.equals(other.loadStatus))
			return false;
		return true;
	}


	
}
