package com.trgr.dockets.core.service;

public interface TestService {

	public void deleteAllDocketVersionsForLegacyId(String legacyId);
	void deleteAllDocketHistoryForLegacyId(String legacyId);
	void deleteAllDocketEntitiesForlegacyId(String legacyId);
	
}
