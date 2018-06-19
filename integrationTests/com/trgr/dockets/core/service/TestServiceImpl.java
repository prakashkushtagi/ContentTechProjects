package com.trgr.dockets.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.DocketDao;
import com.trgr.dockets.core.dao.TestDocketDao;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketVersion;

public class TestServiceImpl implements TestService{
	private TestDocketDao docketDao;
	
	public TestServiceImpl(TestDocketDao docketDao){
		this.docketDao = docketDao;
	}
	/*
	 * Only used for tests
	 */
	@Override
	@Transactional
	public void deleteAllDocketVersionsForLegacyId(String legacyId){
		List<DocketVersion> list = docketDao.findAllDocketVersionsForLegacyId(legacyId);
		docketDao.delete(list);
	}
	
	/*
	 * Only used for tests
	 */
	@Override
	@Transactional
	public void deleteAllDocketHistoryForLegacyId(String legacyId){
		List<DocketHistory> list = docketDao.findAllDocketHistoryForLegacyId(legacyId);
		docketDao.delete(list);
	}
	
	@Override
	@Transactional
	public void deleteAllDocketEntitiesForlegacyId(String legacyId){
		List<DocketEntity> list = docketDao.findAllDocketEntitiesForLegacyId(legacyId);
		docketDao.delete(list);
	}
}
