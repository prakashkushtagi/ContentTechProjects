/*
Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.trgr.dockets.core.entity.SourceLoadRequest;
import com.trgr.dockets.core.exception.SourceContentLoaderException;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */
public class SourceLoadRequestDaoImpl implements SourceLoadRequestDao{
	
	private SessionFactory sessionFactory;
	
	public SourceLoadRequestDaoImpl(SessionFactory sessionFactory){
		super();
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Long addSourceLoadRequest(SourceLoadRequest sourceLoadRequest) throws SourceContentLoaderException{
		Long requesId = null ;
		
			
			Session session = sessionFactory.getCurrentSession();
			requesId =(Long) session.save(sourceLoadRequest);
		return requesId	;
	}
	
	@Override
	public void deleteSourceLoadRequest(long requestId){
		Session session = sessionFactory.getCurrentSession();
		session.delete(requestId);
		session.flush();
	}
	
	@Override
	public void updateSourceLoadRequest(SourceLoadRequest sourceLoadRequest){
		
		Session session =sessionFactory.getCurrentSession();
		session.update(sourceLoadRequest);
	}
	
	@Override
	public SourceLoadRequest findSourceLoadRequestById(long requestId){

		SourceLoadRequest sourceLoadRequest = null;
		@SuppressWarnings("unchecked")
		List<SourceLoadRequest> loadRequestList = (List<SourceLoadRequest>) sessionFactory.getCurrentSession().createCriteria(SourceLoadRequest.class).add(Restrictions.eq("request.requestId", requestId)).list();
		
		if(loadRequestList  != null && loadRequestList.size()> 0 ){
			 sourceLoadRequest = (SourceLoadRequest) loadRequestList.get(0);
		}
		
		return sourceLoadRequest;
	}
	


}
