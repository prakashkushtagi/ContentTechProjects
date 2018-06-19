/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;

import com.trgr.dockets.core.entity.DroppedReason;

public class DroppedReasonDaoImpl implements DroppedReasonDao {
	
	private SessionFactory sessionFactory;
//	private JdbcTemplate jdbcTemplate;

	public DroppedReasonDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public DroppedReasonDaoImpl(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
		this.sessionFactory = sessionFactory;
//		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public DroppedReason findDroppedReasonById(Long droppedReasonId){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DroppedReason.class);
		criteria.add(Restrictions.eq("droppedReasonId", droppedReasonId));
		System.out.println(criteria);
		return (DroppedReason)criteria.uniqueResult();
		
		//Session session = sessionFactory.getCurrentSession();
		//return (DroppedReason) session.get(DroppedReason.class, droppedReasonId);
	}
	
	@Override
	public DroppedReason findDroppedReasonByProcessAndReason(String process, String droppedReason){
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DroppedReason.class);
		criteria.add(Restrictions.eq("process", process));
		criteria.add(Restrictions.eq("droppedReason", droppedReason));
        return (DroppedReason) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DroppedReason> loadDroppedReasons() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(DroppedReason.class)
				.addOrder(Order.asc("droppedReasonId"));
		return (List<DroppedReason>) criteria.list();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
