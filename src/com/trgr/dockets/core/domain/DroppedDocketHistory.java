package com.trgr.dockets.core.domain;

import com.trgr.dockets.core.entity.BatchDroppedDocket;
import com.trgr.dockets.core.entity.CodeTableValues.DroppedProcessTypeEnum;

/**
 * Encapsulates Data needed to insert a row into both the BATCH_DROPPED_DOCKET and DOCKET_HISTORY tables. 
 */
public class DroppedDocketHistory {
	
	private BatchDroppedDocket droppedDocket;
	private String legacyId;

	//new fields for dropped docket reason changes.
	private Long droppedReasonId;
	private DroppedProcessTypeEnum processName;
	
	//TODO this constructor should be removed once all code changes are in place.
	public DroppedDocketHistory(BatchDroppedDocket badDocket, String legacyId) {
		this.droppedDocket = badDocket;
		this.legacyId = legacyId;
	}
	
	public DroppedDocketHistory(BatchDroppedDocket badDocket, String legacyId, DroppedProcessTypeEnum processName, Long droppedReasonId) {
		this.droppedDocket = badDocket;
		this.legacyId = legacyId;
		this.processName = processName;
		this.droppedReasonId = droppedReasonId;
	}
	
	public BatchDroppedDocket getBatchDroppedDocket() {
		return droppedDocket;
	}

	public String getLegacyId() {
		return legacyId;
	}

	public Long getDroppedReasonId() {
		return droppedReasonId;
	}

	public DroppedProcessTypeEnum getProcessName() {
		return processName;
	}
}
