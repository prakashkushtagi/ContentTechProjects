package com.trgr.dockets.core.service;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.PublishingRequestDao;
import com.trgr.dockets.core.entity.PublishingRequest;

public class PublishingRequestServiceImpl implements PublishingRequestService
{

	private PublishingRequestDao publishingRequestDaoImpl;
	
	
	public PublishingRequestServiceImpl() 
	{

	}

	@Override
	@Transactional
	public void savePublishingRequest(PublishingRequest publishingRequest) 
	{
		publishingRequestDaoImpl.savePublishingRequest(publishingRequest);
	}
	
	@Override
	@Transactional
	public void deletePublishingRequest(PublishingRequest publishingRequest)
	{
		publishingRequestDaoImpl.deletePublishingRequest(publishingRequest);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PublishingRequest findPublishingRequestByRequestId(String requestId) 
	{
		return publishingRequestDaoImpl.findPublishingRequestByRequestId(requestId);
	}

	public PublishingRequestDao getPublishingRequestDaoImpl() {
		return publishingRequestDaoImpl;
	}

	public void setPublishingRequestDaoImpl(PublishingRequestDao publishingRequestDaoImpl) {
		this.publishingRequestDaoImpl = publishingRequestDaoImpl;
	}

}
