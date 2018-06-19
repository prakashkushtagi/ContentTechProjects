package com.trgr.dockets.core.service;

import java.util.List;

import com.trgr.dockets.core.domain.AllAndBadDockets;
import com.trgr.dockets.core.domain.Docket;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.domain.SourceLoadRequest;

//@Deprecated
public interface SourceContentLoader {

	/**
	 * Load dockets from a dockets merge file
	 * @param sourceLoadRequest 
	 * @return a container with the collection of all processed dockets and failed (bad) dockets.
	 * @throws Exception on parsing, file I/O or database persistence errors 
	 */
	public AllAndBadDockets loadDockets(SourceLoadRequest sourceLoadRequest) throws Exception;
	
	/**
	 * Acquire the list of dockets, so that the persistence can be completed elsewhere. The SCL class should no longer handle persistence.
	 * @param sourceLoadRequestDomain
	 * @return
	 * @throws Exception 
	 */
	public List<Docket> getDocketList(SourceLoadRequest sourceLoadRequestDomain) throws Exception;
	
	/**
	 * 
	 * @param sourceDocket
	 * @return
	 */
	public String getDocketDump(Docket sourceDocket);
	
	public List<SourceDocketMetadata> getSourceDocketMetadataList(SourceLoadRequest sourceLoadRequestDomain, String filepath) throws Exception;
}
