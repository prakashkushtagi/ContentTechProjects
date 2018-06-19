/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.dao.DroppedReasonDao;
import com.trgr.dockets.core.entity.DroppedReason;

public class DroppedReasonServiceImpl implements DroppedReasonService {
	
	private DroppedReasonDao droppedReasonDao;
	
	/**
	 * Instantiates a new CountyServiceImpl.
	 *
	 */
	public DroppedReasonServiceImpl(DroppedReasonDao droppedReasonDao) {
		this.droppedReasonDao = droppedReasonDao;
	}
	
	public DroppedReasonServiceImpl() {
		
	}

	@Override
	public DroppedReason findDroppedReasonById(Long droppedReasonId){
		return droppedReasonDao.findDroppedReasonById(droppedReasonId);
	}

	@Override
	public DroppedReason findDroppedReasonByProcessAndReason(String process, String droppedReason){
		return droppedReasonDao.findDroppedReasonByProcessAndReason(process, droppedReason);
	}

	@Override
	public List<DroppedReason> loadDroppedReasons() {
		return droppedReasonDao.loadDroppedReasons();
	}

}