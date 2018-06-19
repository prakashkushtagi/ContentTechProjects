package com.trgr.dockets.core.service;

import com.trgr.dockets.core.entity.PublishingRequest;

/**
 * A PublishRequest service.
 */
public interface PublishingRequestService 
{
	public PublishingRequest findPublishingRequestByRequestId(String requestId);

	void savePublishingRequest(PublishingRequest publishingRequest);
	
	void deletePublishingRequest(PublishingRequest publishingRequest);
}
