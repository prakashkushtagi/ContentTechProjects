/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.ContentVersion;


/**
 * DAO to manage ContentVersion entities.
 * 
 */


public interface ContentVersionDao
{
	public ContentVersion persist(ContentVersion toPersist);

	public List<ContentVersion> findContentVersionByUuid(String contentUuid);
	
	public ContentVersion findContentVersionByUuidAndVersion(String contentUuid, Long version);
}



