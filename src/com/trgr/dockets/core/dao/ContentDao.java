/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
 package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.Content;


/**
 * DAO to manage Content entities.
 * 
 */


public interface ContentDao
{
	public Content persist(Content toPersist);

	public Content findContentByUuid(String contentUuid);
}



