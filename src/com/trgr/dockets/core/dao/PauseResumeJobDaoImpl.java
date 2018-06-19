/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.DispatcherWorkPaused;

/**
 * DAO to manage County entities.
 * 
 */

@Transactional
public class PauseResumeJobDaoImpl implements PauseResumeJobDao {

	private SessionFactory sessionFactory;

	/**
	 * Instantiates a new CountyDAOImpl
	 *
	 */
	public PauseResumeJobDaoImpl() {
		super();
	}

	public PauseResumeJobDaoImpl(SessionFactory hibernateSessionFactory) {
		this.sessionFactory = hibernateSessionFactory;
	}


	@Override
	public DispatcherWorkPaused findPauseResumeJobByWorkId(Long workId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DispatcherWorkPaused findPauseResumeJobByJobExecutionId(Long jobExecutionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DispatcherWorkPaused> findPauseResumeJobByReadyToResumeStatus(String status) 
	{
		Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(DispatcherWorkPaused.class);
		criteria.add(Restrictions.eq("readyToResume", status));
		return (List<DispatcherWorkPaused>)criteria.list();
	}

	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}


	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public DispatcherWorkPaused savePauseResumeJob(DispatcherWorkPaused toPersist) {
		sessionFactory.getCurrentSession().save(toPersist);
		return toPersist;
	}

	
	
	/*
	 * (non-Javadoc)
	 */
	@Transactional
	public void deletePauseResumeJob(DispatcherWorkPaused toRemove) {
		toRemove = (DispatcherWorkPaused) sessionFactory.getCurrentSession().merge(toRemove);
		sessionFactory.getCurrentSession().delete(toRemove);
	}

	@Override
	@Transactional
	public void updatePauseResumeJobList(List<DispatcherWorkPaused> listToUnpause) 
	{
		for(DispatcherWorkPaused dispatcherWorkPaused : listToUnpause)
		{
			sessionFactory.getCurrentSession().update(dispatcherWorkPaused);
			flush();
		}
		
	}

	@Override
	@Transactional
	public void updatePauseResumeJob(DispatcherWorkPaused dispatcherWorkPaused) {
		sessionFactory.getCurrentSession().update(dispatcherWorkPaused);
	}

}
