/**
 * 
 */
package com.trgr.dockets.core.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.trgr.dockets.core.domain.LtcAggregateFileInfo;
import com.trgr.dockets.core.entity.CollectionEntity;
import com.trgr.dockets.core.entity.Court;

/**
 * @author C047166
 *
 */
public interface LtcService
{
	/**
	 * @param novusFileList
	 * @param metadocFileList
	 * @param court
	 * @param requestCollaborationKey
	 */
	public String generateLtcRequestMessage(List<File> novusFileList, List<File> metadocFileList, Court court, String requestCollaborationKey);
	
	/**
	 * @param novusFileList
	 * @param metadocFileList
	 * @param court
	 * @param requestCollaborationKey
	 */
	public String generateLtcRequestMessage(File novusFile, File metadocFile, Court court, String requestCollaborationKey);
	
	/**
	 * @param ltcRequestMessage
	 * @return
	 * @throws Exception
	 */
	public boolean sendRequestToLTCURL(String ltcRequestMessage) throws Exception;

	/**
	 * 
	 * @param loadFilesPerCollection - expects collectionName as the key and the list with 2 file entries 1. novus load file 2. metadoc load file in that order
	 * @param court
	 * @return
	 * 
	 */
	public String generateLtcRequestMessage( List<LtcAggregateFileInfo> loadFiles, String collaborationKey, Court court, Map<String,CollectionEntity>collectionDatatypeMap);
}
