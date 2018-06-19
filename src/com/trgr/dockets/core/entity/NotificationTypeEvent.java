package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="DOCKETS_PUB.NOTIFICATION_TYPE_EVENT")
public class NotificationTypeEvent {
	
	public static final String NOTIFICATION_TYPE_ID = "notificationTypeId";
	
	private long notificationTypeEventId;
	private NotificationType notificationType;
	private String status;
	private String subject;
	private String body;
	
	@Id
	@Column(name="NOTIFICATION_TYPE_EVENT_ID")
	public long getNotificationTypeEventId() {
		return notificationTypeEventId;
	}
	public void setNotificationTypeEventId(long notificationTypeEventId) {
		this.notificationTypeEventId = notificationTypeEventId;
	}
	
	@ManyToOne
	@JoinColumn(name="NOTIFICATION_TYPE_ID")
	public NotificationType getNotificationTypeTable() {
		return notificationType;
	}
	public void setNotificationTypeTable(NotificationType notificationType) {
		this.notificationType = notificationType;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="SUBJECT")
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Column(name="BODY")
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "NotificationTypeEvent [notificationTypeEventId="
				+ notificationTypeEventId + ", notificationTypeTable="
				+ notificationType + ", status=" + status + ", subject="
				+ subject + ", body=" + body + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result
				+ ((status == null) ? 0 : status.hashCode());
		result = prime
				* result
				+ ((notificationType == null) ? 0 : notificationType.hashCode());
		result = prime
				* result
				+ (int) (notificationTypeEventId ^ (notificationTypeEventId >>> 32));
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
		NotificationTypeEvent other = (NotificationTypeEvent) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (notificationType == null) {
			if (other.notificationType != null)
				return false;
		} else if (!notificationType.equals(other.notificationType))
			return false;
		if (notificationTypeEventId != other.notificationTypeEventId)
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}
	
	
}
