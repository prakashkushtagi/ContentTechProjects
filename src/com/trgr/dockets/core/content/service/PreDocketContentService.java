/**
 * 
 */
package com.trgr.dockets.core.content.service;

import java.util.Date;

import com.trgr.dockets.core.acquisition.ActivitySet;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.PublishingRequest;

/**
 * @author C047166
 *
 */
public interface PreDocketContentService
{
	public void persistPreDocket(SourceDocketMetadata sourceDocketMetadata) throws Exception;
	
	public void updatePreDocket(String legacyId, ActivitySet activitySet, PublishingRequest publishingRequest) throws Exception;
}
