/**Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.DictionaryNormalizedDao;
import com.trgr.dockets.core.entity.DictionaryNormalized;
import com.trgr.dockets.core.entity.DictionaryNormalizedKey;

public class DictionaryNormalizedServiceImpl implements DictionaryNormalizedService {

	
	@Autowired
	private DictionaryNormalizedDao dictionaryNormalizedDao;

	public DictionaryNormalizedServiceImpl() {

	}

	@Transactional
	@Override
	public DictionaryNormalized findNormalizedNamesByPrimaryKey(DictionaryNormalizedKey primaryKey) {
		return dictionaryNormalizedDao.findNormalizedNamesByPrimaryKey(primaryKey);
	}

	@Transactional
	public void addDictionaryNormalized(DictionaryNormalized dictNorm) {
		dictionaryNormalizedDao.persist(dictNorm);
	}

	//@Required
	public void setDictionaryNormalizedDao(DictionaryNormalizedDao dictionaryNormalizedDao) {
		this.dictionaryNormalizedDao = dictionaryNormalizedDao;
	}
}
