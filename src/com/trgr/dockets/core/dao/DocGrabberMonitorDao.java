/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.DocGrabberMonitor;
import com.trgr.dockets.core.entity.PublishingControlDocGrabber;

public interface DocGrabberMonitorDao {

	public DocGrabberMonitor findDocGrabberMonitorRecordByReceiptId(String receiptId);
	public PublishingControlDocGrabber retrieveDocGrabberPublishingControls();
	//Generalized Dao
	public <T> Object save(T entity);  // generalized save
	public <T> void update(T entity);  // generalized update
	public <T> void delete(T entity);
}
