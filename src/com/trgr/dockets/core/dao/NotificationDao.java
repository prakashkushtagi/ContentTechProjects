package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.NotificationTypeEvent;

public interface NotificationDao {

	//Notification_Type_Event Dao
	public NotificationTypeEvent findNotifEventByTypeAndStatus(String notificationType, String status);
	
	//Method to get notification group for a given type and status
	public String findNotificationGroupByTypeAndStatus(String notificationType, String status);
	
	//Generalized Dao
	public <T> Object save(T entity);  // generalized save
	public <T> void update(T entity);  // generalized update
	public <T> void delete(T entity);
}
