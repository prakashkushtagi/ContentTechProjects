package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.entity.DroppedReason;

public interface DroppedReasonService {
		
	public DroppedReason findDroppedReasonById(Long droppedReasonId);
	
	public DroppedReason findDroppedReasonByProcessAndReason(String process, String droppedReason);
	
	public List<DroppedReason> loadDroppedReasons();
	

}
