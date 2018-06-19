package com.trgr.dockets.core.dao;

import java.util.List;

import com.trgr.dockets.core.entity.DroppedReason;

public interface DroppedReasonDao {
	
	public DroppedReason findDroppedReasonById(Long droppedReasonId);
	
	public DroppedReason findDroppedReasonByProcessAndReason(String process, String droppedReason);
	
	public List<DroppedReason> loadDroppedReasons();
	
}
