/**
 * 
 */
package com.trgr.dockets.core.content.service;

import com.trgr.dockets.core.domain.NovusDocketMetadata;

/**
 * Service to create the publish history and its childs when dockets are published to NOVUS.
 * 
 * @author c047166
 *
 */
public interface PublishContentService 
{
	public void createPublishHistory(NovusDocketMetadata novusDocketMetadata) throws Exception;
}
