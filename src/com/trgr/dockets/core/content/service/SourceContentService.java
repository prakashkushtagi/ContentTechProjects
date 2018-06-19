/*
 * Copyright 2016: Thomson Reuters.
 * All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.content.service;

import java.util.List;

import com.trgr.dockets.core.domain.DroppedDocketHistory;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.exception.DocketsPersistenceException;

/**
 * @author c153421
 *
 */
public interface SourceContentService 
{
	
	public List<DroppedDocketHistory> loadDockets(List<SourceDocketMetadata> sourceDocketMetaData) throws DocketsPersistenceException;
	public DroppedDocketHistory addDroppedDocketHistory(Exception e, SourceDocketMetadata sourceDocketMetadata, String legacyId, Long droppedReasonId);

}
