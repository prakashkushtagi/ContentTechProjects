/**
 * 
 */
package com.trgr.dockets.core.content.service;

import java.util.Date;
import java.util.List;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.NovusDocketMetadata;
import com.trgr.dockets.core.entity.PublishingRequest;


/**
 * Service to create the publish history and its childs when dockets are published to NOVUS.
 * 
 * @author c047166
 *
 */
public interface LoadContentService 
{
	public List<NovusDocketMetadata> createLoadHistory(List<NovusDocketMetadata> novusDocketMetadataList, PublishingRequest publishingRequest, Date loadTimestamp, String loadFileName, String droppedDocketReason);
}
