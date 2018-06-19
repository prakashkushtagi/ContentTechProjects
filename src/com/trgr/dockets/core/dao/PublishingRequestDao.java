package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.PublishingRequest;

public interface PublishingRequestDao 
{
	public PublishingRequest findPublishingRequestByRequestId(String requestId);

	void savePublishingRequest(PublishingRequest publishingRequest);
	
	void deletePublishingRequest(PublishingRequest publishingRequest);
}
