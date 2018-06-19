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
public interface FailedAcquisitionDocketContentService
{
	public void persistFailedDocket(SourceDocketMetadata sourceDocketMetadata) throws Exception;
	
}
