/*
 * Copyright 2016: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.domain;

import java.util.List;

import com.trgr.dockets.core.entity.NotificationTypeEvent;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.DataCaptureRequest;

/**
 * @author U0076257 Kirsten Gunn (Kirsten.Gunn@thomsonreuters.com)
 */
public class NotificationInfo {

	NotificationTypeEvent notificationTypeEvent;
	String notificationGroupString;
	DataCaptureRequest dcReq;
	PublishingRequest pubReq;
	String batchId;
	String errorCode;
	String errorDesc;
	String env;
	String FTPFile;
	String statusName;
	List<String> batchWithProcessErr;
	String collectionName;
	String metaDocInputFile;
	String pubId;
	String mergeFileName;
	String receiptId;

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getMetaDocInputFile() {
		return metaDocInputFile;
	}

	public void setMetaDocInputFile(String metaDocInputFile) {
		this.metaDocInputFile = metaDocInputFile;
	}

	public String getPubId() {
		return pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public NotificationTypeEvent getNotificationTypeEvent() {
		return notificationTypeEvent;
	}

	public String getNotificationGroupString() {
		return notificationGroupString;
	}

	public PublishingRequest getPubReq() {
		return pubReq;
	}

	public DataCaptureRequest getDcReq() {
		return dcReq;
	}

	public String getBatchId() {
		return batchId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public String getEnv() {
		return env;
	}

	public String getFTPFile() {
		return FTPFile;
	}

	public String getStatusName() {
		return statusName;
	}

	public List<String> getBatchWithProcessErr() {
		return batchWithProcessErr;
	}

	public void setBatchWithProcessErr(List<String> batchWithProcessErr) {
		this.batchWithProcessErr = batchWithProcessErr;
	}

	public void setNotificationTypeEvent(NotificationTypeEvent notificationTypeEvent) {
		this.notificationTypeEvent = notificationTypeEvent;
	}

	public void setNotificationGroupString(String notificationGroupString) {
		this.notificationGroupString = notificationGroupString;
	}

	public void setPubReq(PublishingRequest pubReq) {
		this.pubReq = pubReq;
	}

	public void setDcReq(DataCaptureRequest dcReq) {
		this.dcReq = dcReq;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public void setFTPFile(String fTPFile) {
		FTPFile = fTPFile;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getMergeFileName() {
		return mergeFileName;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setMergeFileName(String mergeFileName) {
		this.mergeFileName = mergeFileName;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FTPFile == null) ? 0 : FTPFile.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((batchWithProcessErr == null) ? 0 : batchWithProcessErr.hashCode());
		result = prime * result + ((collectionName == null) ? 0 : collectionName.hashCode());
		result = prime * result + ((dcReq == null) ? 0 : dcReq.hashCode());
		result = prime * result + ((env == null) ? 0 : env.hashCode());
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((errorDesc == null) ? 0 : errorDesc.hashCode());
		result = prime * result + ((mergeFileName == null) ? 0 : mergeFileName.hashCode());
		result = prime * result + ((metaDocInputFile == null) ? 0 : metaDocInputFile.hashCode());
		result = prime * result + ((notificationGroupString == null) ? 0 : notificationGroupString.hashCode());
		result = prime * result + ((notificationTypeEvent == null) ? 0 : notificationTypeEvent.hashCode());
		result = prime * result + ((pubId == null) ? 0 : pubId.hashCode());
		result = prime * result + ((pubReq == null) ? 0 : pubReq.hashCode());
		result = prime * result + ((receiptId == null) ? 0 : receiptId.hashCode());
		result = prime * result + ((statusName == null) ? 0 : statusName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationInfo other = (NotificationInfo) obj;
		if (FTPFile == null) {
			if (other.FTPFile != null)
				return false;
		} else if (!FTPFile.equals(other.FTPFile))
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (batchWithProcessErr == null) {
			if (other.batchWithProcessErr != null)
				return false;
		} else if (!batchWithProcessErr.equals(other.batchWithProcessErr))
			return false;
		if (collectionName == null) {
			if (other.collectionName != null)
				return false;
		} else if (!collectionName.equals(other.collectionName))
			return false;
		if (dcReq == null) {
			if (other.dcReq != null)
				return false;
		} else if (!dcReq.equals(other.dcReq))
			return false;
		if (env == null) {
			if (other.env != null)
				return false;
		} else if (!env.equals(other.env))
			return false;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (errorDesc == null) {
			if (other.errorDesc != null)
				return false;
		} else if (!errorDesc.equals(other.errorDesc))
			return false;
		if (mergeFileName == null) {
			if (other.mergeFileName != null)
				return false;
		} else if (!mergeFileName.equals(other.mergeFileName))
			return false;
		if (metaDocInputFile == null) {
			if (other.metaDocInputFile != null)
				return false;
		} else if (!metaDocInputFile.equals(other.metaDocInputFile))
			return false;
		if (notificationGroupString == null) {
			if (other.notificationGroupString != null)
				return false;
		} else if (!notificationGroupString.equals(other.notificationGroupString))
			return false;
		if (notificationTypeEvent == null) {
			if (other.notificationTypeEvent != null)
				return false;
		} else if (!notificationTypeEvent.equals(other.notificationTypeEvent))
			return false;
		if (pubId == null) {
			if (other.pubId != null)
				return false;
		} else if (!pubId.equals(other.pubId))
			return false;
		if (pubReq == null) {
			if (other.pubReq != null)
				return false;
		} else if (!pubReq.equals(other.pubReq))
			return false;
		if (receiptId == null) {
			if (other.receiptId != null)
				return false;
		} else if (!receiptId.equals(other.receiptId))
			return false;
		if (statusName == null) {
			if (other.statusName != null)
				return false;
		} else if (!statusName.equals(other.statusName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotificationInfo [notificationTypeEvent=" + notificationTypeEvent + ", notificationGroupString=" + notificationGroupString + ", dcReq=" + dcReq + ", pubReq=" + pubReq + ", batchId=" + batchId + ", errorCode=" + errorCode + ", errorDesc="
				+ errorDesc + ", env=" + env + ", FTPFile=" + FTPFile + ", statusName=" + statusName + ", batchWithProcessErr=" + batchWithProcessErr + ", collectionName=" + collectionName + ", metaDocInputFile=" + metaDocInputFile + ", pubId=" + pubId
				+ ", mergeFileName=" + mergeFileName + ", receiptId=" + receiptId + "]";
	}




}
