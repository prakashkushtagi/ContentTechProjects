package com.trgr.dockets.core.content.service;

import com.trgr.dockets.core.domain.JudicialDocketMetadata;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.exception.DocketsPersistenceException;

public interface JudicialContentService {
	
	/**
	 * Load a single Judicial Artifact XML (JAXML) docket file
	 * @param judicialDocketMetadata metadata about the content including the file in which the content resides
	 * @return a DocketVersion object - a judicial docket version instance.
	 * @throws DocketsPersistenceException
	 */
	public DocketVersion persistDocket(JudicialDocketMetadata judicialDocketMetadataList, boolean acquisitionOverride) throws Exception;
	
	public void updateBatchDocketLargeDocketFlag(DocketEntity docketEntity, PublishingRequest pubRequest) throws Exception;

}
