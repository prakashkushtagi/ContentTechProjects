package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DOCKETS_PUB.NOTIFICATION_GROUP")
public class NotificationGroup {
	
	private long notificationGroupId;
	private String emailGroup;
	
	@Id
	@Column(name="NOTIFICATION_GROUP_ID")
	public long getNotifGroupId() {
		return notificationGroupId;
	}
	public void setNotifGroupId(long notifGroupId) {
		this.notificationGroupId = notifGroupId;
	}
	
	@Column(name="GROUP_EMAIL")
	public String getEmailGroup() {
		return emailGroup;
	}
	public void setEmailGroup(String emailGroup) {
		this.emailGroup = emailGroup;
	}
	@Override
	public String toString() {
		return "NotificationGroup [notifGroupId=" + notificationGroupId + ", notificationEmailGroupName="
				+ emailGroup + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailGroup == null) ? 0 : emailGroup.hashCode());
		result = prime * result + (int) (notificationGroupId ^ (notificationGroupId >>> 32));
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
		NotificationGroup other = (NotificationGroup) obj;
		if (emailGroup == null) {
			if (other.emailGroup != null)
				return false;
		} else if (!emailGroup.equals(other.emailGroup))
			return false;
		if (notificationGroupId != other.notificationGroupId)
			return false;
		return true;
	}
	
	
}
