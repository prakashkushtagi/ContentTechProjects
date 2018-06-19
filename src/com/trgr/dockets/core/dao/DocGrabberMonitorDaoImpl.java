/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.DocGrabberMonitor;
import com.trgr.dockets.core.entity.PublishingControlDocGrabber;
@Transactional 
public class DocGrabberMonitorDaoImpl	implements DocGrabberMonitorDao {
	
	private SessionFactory sessionFactory;
	public DocGrabberMonitorDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public DocGrabberMonitorDaoImpl(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
		this.sessionFactory = sessionFactory;
	}


	@Override
	public DocGrabberMonitor findDocGrabberMonitorRecordByReceiptId(String receiptId) {		
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DocGrabberMonitor.class);
		criteria.add(Restrictions.eq("receiptId", receiptId));
		DocGrabberMonitor docGrabberMonitor  = (DocGrabberMonitor)  criteria.uniqueResult();
		return docGrabberMonitor;
	}
	
	@Override
	public PublishingControlDocGrabber retrieveDocGrabberPublishingControls(){
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(PublishingControlDocGrabber.class);
		criteria.add(Restrictions.eq(PublishingControlDocGrabber.ARTIFACT, "DocGrabber"));
		return (PublishingControlDocGrabber) criteria.uniqueResult();
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


	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

		
}

