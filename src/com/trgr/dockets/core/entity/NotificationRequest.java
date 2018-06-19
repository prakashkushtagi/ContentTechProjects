package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DOCKETS_PUB.NOTIFICATION_REQUEST")
public class NotificationRequest {
	
	private long notificationRequestId;
	private String notificationGroup;
	private String subject;
	private String body;
	private Date createdTime;
	private Date sentTime;
	
	@Id
	@Column(name ="NOTIFICATION_REQUEST_ID",nullable = false)
	@GeneratedValue(generator ="NotificReqIdSeq")
	@SequenceGenerator(name="NotificReqIdSeq",sequenceName="DOCKETS_PUB.NOTIFIC_REQ_ID")
	public long getNotificationRequestId() {
		return notificationRequestId;
	}
	public void setNotificationRequestId(long notificationRequestId) {
		this.notificationRequestId = notificationRequestId;
	}
	
	@Column(name= "NOTIFICATION_GROUP")
	public String getNotificationGroup() {
		return notificationGroup;
	}
	public void setNotificationGroup(String notificationGroup) {
		this.notificationGroup = notificationGroup;
	}
	
	@Column(name= "SUBJECT")
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Column(name= "BODY")
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	@Column(name= "CREATED_TIMESTAMP")
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
	@Column(name= "SENT_TIMESTAMP")
	public Date getSentTime() {
		return sentTime;
	}
	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}
	@Override
	public String toString() {
		return "NotificationRequest [notificationRequestId="
				+ notificationRequestId + ", notificationGroup="
				+ notificationGroup + ", subject=" + subject + ", body=" + body
				+ ", createdTime=" + createdTime + ", sentTime=" + sentTime
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result
				+ ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result
				+ ((notificationGroup == null) ? 0 : notificationGroup.hashCode());
		result = prime * result
				+ (int) (notificationRequestId ^ (notificationRequestId >>> 32));
		result = prime * result
				+ ((sentTime == null) ? 0 : sentTime.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
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
		NotificationRequest other = (NotificationRequest) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (createdTime == null) {
			if (other.createdTime != null)
				return false;
		} else if (!createdTime.equals(other.createdTime))
			return false;
		if (notificationGroup == null) {
			if (other.notificationGroup != null)
				return false;
		} else if (!notificationGroup.equals(other.notificationGroup))
			return false;
		if (notificationRequestId != other.notificationRequestId)
			return false;
		if (sentTime == null) {
			if (other.sentTime != null)
				return false;
		} else if (!sentTime.equals(other.sentTime))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

	

}
