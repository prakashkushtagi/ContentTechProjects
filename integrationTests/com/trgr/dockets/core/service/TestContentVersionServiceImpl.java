package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.dao.TestContentVersionDao;
import com.trgr.dockets.core.entity.ContentVersion;

public class TestContentVersionServiceImpl implements TestContentVersionService{
	ContentVersionService cvs;
	TestContentVersionDao contentVersionDao;
	
	public TestContentVersionServiceImpl(ContentVersionService cvs, TestContentVersionDao contentVersionDao) {
		this.cvs = cvs;
		this.contentVersionDao = contentVersionDao;
	}
	@Override
	public void deleteAllContentVersionsByUuid(String contentUuid){
		List<ContentVersion> list =	cvs.findContentVersionByUuid(contentUuid);
		contentVersionDao.delete(list);
	}
}
