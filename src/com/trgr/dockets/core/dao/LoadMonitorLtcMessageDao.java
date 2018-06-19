/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.LoadMonitorLtcMessage;


public class LoadMonitorLtcMessageDao
{

    private SessionFactory sessionFactory;
  
    public void saveOrUpdatePublishingControl(LoadMonitorLtcMessage loadMonitorLtcMessage)
    {
		sessionFactory.getCurrentSession().saveOrUpdate(loadMonitorLtcMessage);
    }
    
    public void deleteLtcMessagesByFilename(String filename)
    {
    	String hql = "delete from LoadMonitorLtcMessage where ltcFilename = :filename";
		sessionFactory.getCurrentSession().createQuery(hql).setString("filename", filename).executeUpdate();
    }
    
	public LoadMonitorLtcMessage getLtcMessageByMessageId(String messageId)
	{
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
		criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_MESSAGE_ID,messageId));
        return (LoadMonitorLtcMessage) criteria.uniqueResult();
	}


	@SuppressWarnings("unchecked")
	public List<LoadMonitorLtcMessage> getLtcMessageByEventId(long eventId) 
	{
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
                criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_EVENT_ID, eventId));
		return (List<LoadMonitorLtcMessage>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<LoadMonitorLtcMessage> getLtcMessageByCollaborationKey(String collaborationKey) 
	{
        final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
                criteria.add(Restrictions.like(LoadMonitorLtcMessage.LTC_COLLABORATION_KEY, collaborationKey,MatchMode.ANYWHERE));
                criteria.add(Restrictions.isNull(LoadMonitorLtcMessage.ERROR_DESCRIPTION));
                criteria.addOrder(Order.desc(LoadMonitorLtcMessage.LMS_TIMESTAMP));
		return (List<LoadMonitorLtcMessage>)criteria.list();
	}
	
	public LoadMonitorLtcMessage findByCollaborationKeyFilenameStep(String collaborationKey, String ltcFilename, String loadElement) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
		criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_COLLABORATION_KEY, collaborationKey));
		criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_FILENAME, ltcFilename.trim()));
		criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_LOAD_ELEMENT, loadElement));
//		criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_LOAD_STATUS, loadStatus));
		return (LoadMonitorLtcMessage) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<LoadMonitorLtcMessage> findLtcMessagesByFilename(String ltcFilename) {
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
		criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_FILENAME, ltcFilename.trim()));
		return (List<LoadMonitorLtcMessage>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<LoadMonitorLtcMessage> getLtcMessageByStatusId() 
	{
		List<String> statusesNeedProcessing = new ArrayList<String>(); 
		statusesNeedProcessing.add("301");
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
		criteria.add(Restrictions.in("statusId", statusesNeedProcessing));
        return (List<LoadMonitorLtcMessage>)criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<LoadMonitorLtcMessage> getLtcMessageByStatusIdFor100(List<String> statusesNeedProcessing, int maxRecords) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
		criteria.add(Restrictions.in("statusId", statusesNeedProcessing));
		criteria.addOrder(Order.asc(LoadMonitorLtcMessage.LMS_TIMESTAMP));
		criteria.setMaxResults(maxRecords);
        return (List<LoadMonitorLtcMessage>)criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<LoadMonitorLtcMessage> getLtcMessageByStatusIdForMaxRecords(String statusId, int maxRecords)
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
		criteria.add(Restrictions.eq("statusId", statusId));
		criteria.addOrder(Order.asc(LoadMonitorLtcMessage.LMS_TIMESTAMP));
		criteria.setMaxResults(maxRecords);
        return (List<LoadMonitorLtcMessage>)criteria.list();
	}
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public List<LoadMonitorLtcMessage> getLtcMessageByStatusIdFor100() 
	{
		List<String> statusesNeedProcessing = new ArrayList<String>(); 
		statusesNeedProcessing.add("301");
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
		criteria.add(Restrictions.in("statusId", statusesNeedProcessing));
		criteria.addOrder(Order.asc(LoadMonitorLtcMessage.LMS_TIMESTAMP));
		criteria.setMaxResults(100);
        return (List<LoadMonitorLtcMessage>)criteria.list();
	}

	public LoadMonitorLtcMessage getLtcMessageByCollaborationKeyMessageType(String collaborationKey, String messageType) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LoadMonitorLtcMessage.class);
        criteria.add(Restrictions.like(LoadMonitorLtcMessage.LTC_COLLABORATION_KEY, collaborationKey,MatchMode.ANYWHERE));
        criteria.add(Restrictions.eq(LoadMonitorLtcMessage.LTC_LOAD_ELEMENT, messageType));
        return (LoadMonitorLtcMessage)criteria.uniqueResult();
	}


}