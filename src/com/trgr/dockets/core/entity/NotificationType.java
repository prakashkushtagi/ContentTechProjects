package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DOCKETS_PUB.NOTIFICATION_TYPE")
public class NotificationType {

	private long notificationTypeId;
	private String notificationType;
	
	@Id
	@Column(name="NOTIFICATION_TYPE_ID")
	public long getNotificationTypeId() {
		return notificationTypeId;
	}
	public void setNotificationTypeId(long notificationTypeId) {
		this.notificationTypeId = notificationTypeId;
	}
	
	@Column(name="NOTIFICATION_TYPE")
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	@Override
	public String toString() {
		return "NotificationType [notifTypeId=" + notificationTypeId + ", notifType="
				+ notificationType + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((notificationType == null) ? 0 : notificationType.hashCode());
		result = prime * result + (int) (notificationTypeId ^ (notificationTypeId >>> 32));
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
		NotificationType other = (NotificationType) obj;
		if (notificationType == null) {
			if (other.notificationType != null)
				return false;
		} else if (!notificationType.equals(other.notificationType))
			return false;
		if (notificationTypeId != other.notificationTypeId)
			return false;
		return true;
	}
	

}
