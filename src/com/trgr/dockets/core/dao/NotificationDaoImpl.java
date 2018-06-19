package com.trgr.dockets.core.dao;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.NotificationTypeEvent;

public class NotificationDaoImpl implements NotificationDao {
	
	private static final Logger log = Logger.getLogger(NotificationDaoImpl.class);
	
	private SessionFactory sessionFactory;
	private JdbcTemplate jdbcTemplate;

	public NotificationDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public NotificationDaoImpl(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
		this.sessionFactory = sessionFactory;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	@Transactional (readOnly=true)
	public NotificationTypeEvent findNotifEventByTypeAndStatus(String notificationType, String status) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NotificationTypeEvent.class)
																	.createAlias("notificationTypeTable", "notTable");
		criteria.add(Restrictions.eq("notTable.notificationType", notificationType));
		criteria.add(Restrictions.eq("status", status));
		
		return (NotificationTypeEvent) criteria.uniqueResult();
	}
	
	@Override
	@Transactional (readOnly=true)
	public String findNotificationGroupByTypeAndStatus(String notificationType, String status)
	{

		String sql = String.format("select ng.group_email from dockets_pub.notification_group ng, dockets_pub." +
									"notification_subscription ns, dockets_pub.notification_type nt, " +
									"dockets_pub.notification_type_event nte where ns.notification_group_id = ng.notification_group_id " +
									"and nt.notification_type_id = ns.notification_type_id " +
									"and nte.notification_type_id = nt.notification_type_id " +
									"and (nt.notification_type ='%s')" +
									"and (nte.status = '%s')", notificationType, status);
		try {					
			String notificationGroupList = jdbcTemplate.queryForList(sql).toString();
			return notificationGroupList;
		}
		catch (Exception e){
			log.warn("Unknown error while reading notification group ", e);
			return null;
		}
	}

	//Generalized Implementation for Notification Tables
	@Override
	public <T> void delete(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(entity);
	}
	@Override
	public <T> Object save(T entity) {
		Session session = sessionFactory.getCurrentSession();
		return session.save(entity);
	}
	@Override
	public <T> void update(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.update(entity);
	}
}