package com.trgr.dockets.core.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 * 
 */

@XmlRootElement(name ="dockets.publishing.event")
public class EventXml {
	
	private String eventId;
	private String eventName;
	private String batchId;
	private String subBatchId;
	private String appServer;
	private String eventTimestamp;
	private String fileName;
	private String numDocs;
	private String fileSize;
	private String logFolder;
	private String errorFolder;
	private String workFolder;
	private String novusLoadFolder;
	private String droppedDockets;
	private EventError eventError;
	private String payload;

	public EventXml(){
		super();
	}

	public String getEventId(){
		return eventId;
	}
	

	@XmlAttribute(name="id", required = true )
	public void setEventId(String eventId){
		this.eventId = eventId;
	}
	

	public String getEventName(){
		return eventName;
	}
	
	@XmlAttribute (name="name", required = true)
	public void setEventName(String eventName){
		this.eventName = eventName;
	}
	

	public String getBatchId(){
		return batchId;
	}
	
	@XmlAttribute(name="batch.id", required = true)
	public void setBatchId(String batchId){
		this.batchId = batchId;
	}
	

	public String getSubBatchId(){
		return subBatchId;
	}
	
	@XmlAttribute(name="sub.batch.id", required = true)
	public void setSubBatchId(String subBatchId){
		this.subBatchId = subBatchId;
	}
	

	public String getAppServer(){
		return appServer;
	}
	
	@XmlAttribute(name="app.server", required=  false)
	public void setAppServer(String appServer){
		this.appServer = appServer;
	}
	

	public String getEventTimestamp(){
		return eventTimestamp;
	}
	
	@XmlAttribute (name="timestamp", required = false)
	public void setEventTimestamp(String eventTimestamp){
		this.eventTimestamp = eventTimestamp;
	}
	

	public String getFileName(){
		return fileName;
	}
	
	@XmlAttribute(name="file.name", required = false)
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	

	public String getNumDocs(){
		return numDocs;
	}
	
	@XmlAttribute(name="num_docs", required = false)
	public void setNumDocs(String numDocs){
		this.numDocs = numDocs;
	}
	


	public String getFileSize(){
		return fileSize;
	}
	
	@XmlAttribute(name="size", required = false)
	public void setFileSize(String fileSize){
		this.fileSize = fileSize;
	}
	

	public String getLogFolder(){
		return logFolder;
	}
	
	@XmlAttribute(name="process.log.folder", required = false)
	public void setLogFolder(String logFolder){
		this.logFolder = logFolder;
	}
	

	public String getErrorFolder(){
		return errorFolder;
	}
	
	@XmlAttribute(name="error.log.folder", required = false)
	public void setErrorFolder(String errorFolder){
		this.errorFolder = errorFolder;
	}
	

	public String getWorkFolder(){
		return workFolder;
	}
	
	@XmlAttribute(name="work_folder", required = false)
	public void setWorkFolder(String workFolder){
		this.workFolder = workFolder;
	}
	

	public String getNovusLoadFolder(){
		return novusLoadFolder;
	}
	
	@XmlAttribute(name="novus_load_folder", required = false)
	public void setNovusLoadFolder(String novusLoadFolder){
		this.novusLoadFolder = novusLoadFolder;
	}
	

	public String getDroppedDockets(){
		return droppedDockets;
	}
	
	@XmlElement(name="dropped.dockets")
	public void setDroppedDockets(String droppedDockets){
		this.droppedDockets = droppedDockets;
	}
	

	public EventError getEventError(){
		return eventError;
	}
	
	@XmlElement(name="error")
	public void setEventError(EventError eventError){
		this.eventError = eventError;
	}

	public String getPayload(){
		return payload;
	}
	
	public void setPayload(String payload){
		this.payload = payload;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appServer == null) ? 0 : appServer.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result
				+ ((droppedDockets == null) ? 0 : droppedDockets.hashCode());
		result = prime * result
				+ ((errorFolder == null) ? 0 : errorFolder.hashCode());
		result = prime * result
				+ ((eventError == null) ? 0 : eventError.hashCode());
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		result = prime * result
				+ ((eventName == null) ? 0 : eventName.hashCode());
		result = prime * result
				+ ((eventTimestamp == null) ? 0 : eventTimestamp.hashCode());
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result
				+ ((fileSize == null) ? 0 : fileSize.hashCode());
		result = prime * result
				+ ((logFolder == null) ? 0 : logFolder.hashCode());
		result = prime * result
				+ ((novusLoadFolder == null) ? 0 : novusLoadFolder.hashCode());
		result = prime * result + ((numDocs == null) ? 0 : numDocs.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result
				+ ((subBatchId == null) ? 0 : subBatchId.hashCode());
		result = prime * result
				+ ((workFolder == null) ? 0 : workFolder.hashCode());
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
		EventXml other = (EventXml) obj;
		if (appServer == null) {
			if (other.appServer != null)
				return false;
		} else if (!appServer.equals(other.appServer))
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (droppedDockets == null) {
			if (other.droppedDockets != null)
				return false;
		} else if (!droppedDockets.equals(other.droppedDockets))
			return false;
		if (errorFolder == null) {
			if (other.errorFolder != null)
				return false;
		} else if (!errorFolder.equals(other.errorFolder))
			return false;
		if (eventError == null) {
			if (other.eventError != null)
				return false;
		} else if (!eventError.equals(other.eventError))
			return false;
		if (eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!eventId.equals(other.eventId))
			return false;
		if (eventName == null) {
			if (other.eventName != null)
				return false;
		} else if (!eventName.equals(other.eventName))
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
		if (fileSize == null) {
			if (other.fileSize != null)
				return false;
		} else if (!fileSize.equals(other.fileSize))
			return false;
		if (logFolder == null) {
			if (other.logFolder != null)
				return false;
		} else if (!logFolder.equals(other.logFolder))
			return false;
		if (novusLoadFolder == null) {
			if (other.novusLoadFolder != null)
				return false;
		} else if (!novusLoadFolder.equals(other.novusLoadFolder))
			return false;
		if (numDocs == null) {
			if (other.numDocs != null)
				return false;
		} else if (!numDocs.equals(other.numDocs))
			return false;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (subBatchId == null) {
			if (other.subBatchId != null)
				return false;
		} else if (!subBatchId.equals(other.subBatchId))
			return false;
		if (workFolder == null) {
			if (other.workFolder != null)
				return false;
		} else if (!workFolder.equals(other.workFolder))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "EventXml [eventId=" + eventId + ", eventName=" + eventName
				+ ", batchId=" + batchId + ", subBatchId=" + subBatchId
				+ ", appServer=" + appServer + ", eventTimestamp="
				+ eventTimestamp + ", fileName=" + fileName + ", numDocs="
				+ numDocs + ", fileSize=" + fileSize + ", logFolder="
				+ logFolder + ", errorFolder=" + errorFolder + ", workFolder="
				+ workFolder + ", novusLoadFolder=" + novusLoadFolder
				+ ", droppedDockets=" + droppedDockets + ", eventError="
				+ eventError + ", payload=" + payload + "]";
	}


}



