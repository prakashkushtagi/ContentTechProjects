package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="DOCKETS_PUB.NOTIFICATION_SUBSCRIPTION")
public class NotificationSubscription {
	
	private long subscriptionId;
	private NotificationType notificationType;
	private NotificationGroup notificationGroup;
	
	@Id
	@Column(name="SUBSCRIPTION_ID")
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
	@ManyToOne
	@JoinColumn(name="NOTIFICATION_TYPE_ID")
	public NotificationType getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}
	
	@ManyToOne
	@JoinColumn(name="NOTIFICATION_GROUP_ID")
	public NotificationGroup getNotificationGroup() {
		return notificationGroup;
	}
	public void setNotificationGroup(NotificationGroup notificationGroup) {
		this.notificationGroup = notificationGroup;
	}
	@Override
	public String toString() {
		return "NotificationSubscription [subscriptionId=" + subscriptionId
				+ ", notificationType=" + notificationType
				+ ", notificationGroup=" + notificationGroup + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((notificationGroup == null) ? 0 : notificationGroup
						.hashCode());
		result = prime
				* result
				+ ((notificationType == null) ? 0 : notificationType.hashCode());
		result = prime * result
				+ (int) (subscriptionId ^ (subscriptionId >>> 32));
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
		NotificationSubscription other = (NotificationSubscription) obj;
		if (notificationGroup == null) {
			if (other.notificationGroup != null)
				return false;
		} else if (!notificationGroup.equals(other.notificationGroup))
			return false;
		if (notificationType == null) {
			if (other.notificationType != null)
				return false;
		} else if (!notificationType.equals(other.notificationType))
			return false;
		if (subscriptionId != other.subscriptionId)
			return false;
		return true;
	}
}
