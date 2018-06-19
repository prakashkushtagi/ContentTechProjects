package com.trgr.dockets.core.content.service;

import com.trgr.dockets.core.entity.WiplaNovusContent;

public interface WiplaNovusContentService {
    
	public WiplaNovusContent findContentByLegacyId(String legacyId);
    
    public void saveOrUpdate(WiplaNovusContent wiplaNovusContent);
    
    public void delete(WiplaNovusContent wiplaNovusContent);
}
