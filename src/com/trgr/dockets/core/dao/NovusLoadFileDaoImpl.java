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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.NovusLoadFile;
import com.trgr.dockets.core.entity.NovusLoadFileKey;

public class NovusLoadFileDaoImpl implements NovusLoadFileDao 
{
	private SessionFactory sessionFactory;

	public NovusLoadFileDaoImpl() 
	{
	}

	/*
	@Override
	public NovusLoadFile findByLoadRequestIdFileName(String loadRequestId, String filename) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NovusLoadFile.class);
//		criteria.add(Restrictions.eq(NovusLoadFile.LOAD_REQUEST_ID, loadRequestId));
//		criteria.add(Restrictions.eq(NovusLoadFile.FILE_NAME, filename));
		NovusLoadFileKey primarKey = new NovusLoadFileKey(loadRequestId,filename);
        criteria.add(Restrictions.eq("primaryKey", primarKey));
        return (NovusLoadFile) criteria.uniqueResult();
	}*/

	@Override
	@Transactional(readOnly = true)
	public NovusLoadFile findByRequestIdSubBatchId(String requestId,String subBatchId) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NovusLoadFile.class);
		criteria.add(Restrictions.eq(NovusLoadFile.LOAD_REQUEST_ID, requestId));
		criteria.add(Restrictions.eq(NovusLoadFile.SUB_BATCH_ID, subBatchId));
		criteria.add(Restrictions.not(Restrictions.like(NovusLoadFile.FILE_NAME, "MetadocRenderedFile",MatchMode.START)));
		return (NovusLoadFile) criteria.uniqueResult();
	}
	
	@Override
	@Transactional(readOnly = true)
	public NovusLoadFile findByBatchIdSubBatchId(String batchId, String subBatchId) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NovusLoadFile.class);
		criteria.add(Restrictions.eq(NovusLoadFile.BATCH_ID, batchId));
		criteria.add(Restrictions.eq(NovusLoadFile.SUB_BATCH_ID, subBatchId));
		criteria.add(Restrictions.not(Restrictions.like(NovusLoadFile.FILE_NAME, "MetadocRenderedFile",MatchMode.START)));
		return (NovusLoadFile) criteria.uniqueResult();
	}
	
	@Override
	public NovusLoadFile findNovusLoadFileByPrimaryKey(NovusLoadFileKey primaryKey) {
		Session session = sessionFactory.getCurrentSession();
		return (NovusLoadFile) session.get(NovusLoadFile.class, primaryKey);
	}
	
	@Override
	public NovusLoadFile findByBatchIdSubBatchIdFileName(String batchId, String subBatchId, String filename) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NovusLoadFile.class);
		criteria.add(Restrictions.eq(NovusLoadFile.BATCH_ID, batchId));
		criteria.add(Restrictions.eq(NovusLoadFile.SUB_BATCH_ID, subBatchId));
		criteria.add(Restrictions.eq(NovusLoadFile.FILE_NAME, filename));
        return (NovusLoadFile) criteria.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<NovusLoadFile> findByBatchIdCollectionIdFileName(String batchId,Long collectionId, String filename) 
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NovusLoadFile.class);
		criteria.add(Restrictions.eq(NovusLoadFile.BATCH_ID, batchId));
		criteria.add(Restrictions.eq(NovusLoadFile.COLLECTION_ID, collectionId));
		criteria.add(Restrictions.eq(NovusLoadFile.FILE_NAME, filename));
        return (List<NovusLoadFile>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<NovusLoadFile> findNovusLoadFilesByBatchIdAndRequestId(String batchId,String requestId)
	{
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NovusLoadFile.class);
		criteria.add(Restrictions.eq(NovusLoadFile.BATCH_ID, batchId));
		criteria.add(Restrictions.eq(NovusLoadFile.LOAD_REQUEST_ID, requestId));
		return (List<NovusLoadFile>) criteria.list();
		
	}
	@Override
	@Transactional
	public void saveNovusLoadFile(NovusLoadFile novusLoadFile) 
	{
		save(novusLoadFile);
	}
	
	@Transactional
	private <T> void save(T entity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(entity);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
