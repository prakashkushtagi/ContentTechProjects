/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.service;

import com.trgr.dockets.core.dao.DocGrabberMonitorDao;
import com.trgr.dockets.core.entity.DocGrabberMonitor;
import com.trgr.dockets.core.entity.PublishingControlDocGrabber;

public class DocGrabberMonitorServiceImpl implements DocGrabberMonitorService {

	
	private DocGrabberMonitorDao docGrabberMonitorDao;
	
	public DocGrabberMonitorServiceImpl(DocGrabberMonitorDao docGrabberMonitorDao){
		this.docGrabberMonitorDao = docGrabberMonitorDao;
	}
	
	public DocGrabberMonitorServiceImpl(){
		
	}
	
	@Override
	public DocGrabberMonitor findDocGrabberMonitorByReceiptId(String receiptId){
		
		return docGrabberMonitorDao.findDocGrabberMonitorRecordByReceiptId(receiptId);
	}

	@Override
	public PublishingControlDocGrabber retrieveDocGrabberPublishingControls() {
		return docGrabberMonitorDao.retrieveDocGrabberPublishingControls();
	}
	
	@Override
	public <T> Object save(T entity) {
		
		return docGrabberMonitorDao.save(entity);
	}

	@Override
	public <T> void update(T entity) {
		docGrabberMonitorDao.update(entity);

	}

	@Override
	public <T> void delete(T entity) {
		docGrabberMonitorDao.delete(entity);

	}

}
