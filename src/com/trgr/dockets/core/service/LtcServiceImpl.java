/*Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
/**
 * 
 */
package com.trgr.dockets.core.service;

import java.io.File;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.trgr.dockets.core.domain.LtcAggregateFileInfo;
import com.trgr.dockets.core.domain.LtcRequestInfo;
import com.trgr.dockets.core.entity.CollectionEntity;
import com.trgr.dockets.core.entity.Court;

/**
 * @author C047166
 *
 */
public class LtcServiceImpl implements LtcService
{
	private static final Logger log = Logger.getLogger(LtcServiceImpl.class);	
	
	protected LtcRequestInfo ltcRequestInfo;
	
	
	/**
	 * @param ltcRequestInfo
	 */
	public LtcServiceImpl(LtcRequestInfo ltcRequestInfo)
	{
		super();
		this.ltcRequestInfo = ltcRequestInfo;
	}

	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.service.LtcService#sendRequestToLTCURL(java.lang.String)
	 */
	public boolean sendRequestToLTCURL(String ltcRequestMessage) throws Exception {
		HttpURLConnection uc = null;
		try {			
			URL url = new URL(ltcRequestInfo.getRequestUrl());
			log.debug("Message to LTC " + ltcRequestMessage );
			uc = (HttpURLConnection)url.openConnection();
			uc.setDoInput(true);
			uc.setDoOutput(true);
			uc.setAllowUserInteraction(false);
			uc.setUseCaches(false);
			uc.setRequestMethod("POST");
			
			PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.write("XML=");
			pw.write(ltcRequestMessage);
			pw.flush();
			pw.close();
			
			String response = uc.getResponseMessage();
			if (response.equalsIgnoreCase("OK")) 
			{
				if(response.indexOf("<response.text>failed") != -1)
				{ 
					throw new Exception("Error in LTC response message." + response);
				}
			} 
			else 
			{
				log.error("LTC response error: " + response);
				throw new Exception("Error in LTC response message." + response);
			}
		}
		finally
		{
			uc.disconnect();
		}
		return true;
	}

	/* (non-Javadoc)
	 * This appears to only be used by test code
	 * @see com.trgr.dockets.core.service.LtcService#generateLtcRequestMessage(java.util.List, java.util.List, com.trgr.dockets.core.entity.Court, java.lang.String)
	 */
	@Override
	public String generateLtcRequestMessage(List<File> novusFileList, List<File> metadocFileList, Court court, String requestCollaborationKey)
	{
		boolean isNovusAndMetadocLoad = false;
		if(metadocFileList.size() > 0){
			isNovusAndMetadocLoad = true;
		}
		
		//construct the request
		StringBuffer request = new StringBuffer();
		
		request.append("<?xml version=\"1.0\"?>");
		request.append("<ltc.autorequest>");
		if (isNovusAndMetadocLoad) {
			request.append("<transaction>");
		}

		request.append("<requests>");

		Iterator<File> novusFileIter = novusFileList.iterator();
		while (novusFileIter.hasNext()) {
			File novusFile = novusFileIter.next();
			prepareRequest(request, novusFile, LtcRequestInfo.LTC_NOVUS_LOAD_VARIANT, requestCollaborationKey, court,null);
		}
		
		if(isNovusAndMetadocLoad){
			Iterator<File> metadocFileIter = metadocFileList.iterator();
			while (metadocFileIter.hasNext()) {
				File metadocFile = metadocFileIter.next();
				prepareRequest(request, metadocFile, LtcRequestInfo.LTC_METADOC_LOAD_VARIANT, requestCollaborationKey, court,null);
			}
		}

		request.append("</requests>");
		if (isNovusAndMetadocLoad) {
			request.append("</transaction>");
		}

		// Subscribe to a queue for Load complete events
		prepareQueueSubscriptionRequest(request);
		request.append("</ltc.autorequest>");
		log.debug("[ "+requestCollaborationKey+ " ] Request to LTC " + request.toString());
		return request.toString();
	}
	
	@Override
	public String generateLtcRequestMessage(List<LtcAggregateFileInfo> loadFiles, String collaborationKey, Court court, Map<String,CollectionEntity> collectionDataTypeMap)
	{
		String requestCollaborationKey = null;
		
		//construct the request
		StringBuffer request = new StringBuffer();
		
		request.append("<?xml version=\"1.0\"?>");
		request.append("<ltc.autorequest>");
		request.append("<transaction>");

		request.append("<requests>");
				
		for (LtcAggregateFileInfo loadFile : loadFiles){
			String collectionName = loadFile.getCollectionName();
			requestCollaborationKey = collaborationKey + collectionName + loadFile.getCollabKeySuffix();
			
			log.debug("Generating request for " + requestCollaborationKey + " datatype " + collectionDataTypeMap.get(collectionName).getLtcDatatype());
			
			if (null != loadFile.getNovusLoadFile()){
				prepareRequest(request, loadFile.getNovusLoadFile(), LtcRequestInfo.LTC_NOVUS_LOAD_VARIANT, requestCollaborationKey, court,collectionDataTypeMap.get(collectionName).getLtcDatatype());
			}
			if (null != loadFile.getMetadocLoadFile() && isValidFile(loadFile.getMetadocLoadFile())){
				prepareRequest(request,  loadFile.getMetadocLoadFile(), LtcRequestInfo.LTC_METADOC_LOAD_VARIANT, requestCollaborationKey, court,collectionDataTypeMap.get(collectionName).getLtcDatatype());
			}else{
				log.debug("Skipping metadoc files as they are empty: "+loadFile.getMetadocLoadFile());
			}
		}

		request.append("</requests>");
		request.append("</transaction>");

		// Subscribe to a queue for Load complete events
		prepareQueueSubscriptionRequest(request);
		request.append("</ltc.autorequest>");
		return request.toString();
	}
	private boolean isValidFile(File file){
		try {
			if(FileUtils.sizeOf(file) >0 ){
				return true;
			}

		} catch (Exception e) {
			log.debug("IOException while validating file.");
		}
		return false;
	}
	//This appears to only be used by test code
	public String generateLtcRequestMessage(File novusFile, File metadocFile, Court court, String requestCollaborationKey){

		boolean isNovusAndMetadocLoad = false;
		if(metadocFile != null){
			isNovusAndMetadocLoad = true;
		}
		
		//construct the request
		StringBuffer request = new StringBuffer();
		
		request.append("<?xml version=\"1.0\"?>");
		request.append("<ltc.autorequest>");
		if (isNovusAndMetadocLoad) {
			request.append("<transaction>");
		}

		request.append("<requests>");

		prepareRequest(request, novusFile, LtcRequestInfo.LTC_NOVUS_LOAD_VARIANT, requestCollaborationKey, court,null);
		
		if(isNovusAndMetadocLoad){
			prepareRequest(request, metadocFile, LtcRequestInfo.LTC_METADOC_LOAD_VARIANT, requestCollaborationKey, court,null);
		}
		
		request.append("</requests>");
		if (isNovusAndMetadocLoad) {
			request.append("</transaction>");
		}

		// Subscribe to a queue for Load complete events
		prepareQueueSubscriptionRequest(request);

		request.append("</ltc.autorequest>");

		return request.toString();
	

	
	}
	
	/**
	 * Generates a <request>...</request> for one load file.
	 * 
	 * @param requestBuffer
	 * @param loadFile
	 * @param dataTypeName
	 */
	private void prepareRequest(StringBuffer requestBuffer, File loadFile, String variant, String requestCollaborationKey, Court court, String dataType){
		requestBuffer.append("<request>");
		requestBuffer.append("<requesttype>").append(LtcRequestInfo.REQUEST_TYPE).append("</requesttype>");
		requestBuffer.append("<source>").append(LtcRequestInfo.REQUEST_SOURCE).append("</source>");
		requestBuffer.append("<requestdata>");
		//request.collaboration.key
		requestBuffer.append("<request.collaboration.key>");
		requestBuffer.append(requestCollaborationKey);
		requestBuffer.append("</request.collaboration.key>");
		// datatype
		requestBuffer.append("<datatype>");

		requestBuffer.append(appendDataType(court,dataType));
		
		requestBuffer.append("<variant>");
		requestBuffer.append(variant);
		requestBuffer.append("</variant>");
		requestBuffer.append("</datatype>");
		// data sets
		requestBuffer.append("<datasets>");
		requestBuffer.append("<dataset>");
		requestBuffer.append("<datasetname>");
		requestBuffer.append(loadFile.getName());
		requestBuffer.append("</datasetname>");
		// Novus load file path
		requestBuffer.append("<datasetpath>");
		requestBuffer.append(FilenameUtils.getFullPath(loadFile.getPath()));
		requestBuffer.append("</datasetpath>");
		// ftp server
		requestBuffer.append("<resource>");
		requestBuffer.append("<datasetresource.name>");
		requestBuffer.append(ltcRequestInfo.getDataResourceName());
		requestBuffer.append("</datasetresource.name>");
		requestBuffer.append("</resource>");
		requestBuffer.append("</dataset>");
		requestBuffer.append("</datasets>");
		requestBuffer.append("</requestdata>");
		requestBuffer.append("</request>");
	}
	
	/**
	 * Default implementation doesnt not take court in to consideration. Ex: JPML
	 * 
	 * @param court
	 * @return
	 */
	protected String appendDataType(Court court, String dataType){
		StringBuffer requestBuffer = new StringBuffer();
		requestBuffer.append("<datatypename>");
		if(null!=dataType && !dataType.isEmpty())
		{
			requestBuffer.append(dataType);
		}
		else
		{
			requestBuffer.append(ltcRequestInfo.getDataTypeName());
		}
		requestBuffer.append("</datatypename>");
		return requestBuffer.toString();
	}
	
	private void prepareQueueSubscriptionRequest(StringBuffer request)
	{
		request.append("<subscription.collection>");
		request.append("<subscription.data>");
		// owner
		request.append("<subscription.owner>");
		request.append(LtcRequestInfo.REQUEST_SOURCE);
		request.append("</subscription.owner>");

		// event filter
		request.append("<event.filter>");
		// event publisher
		request.append("<event.publisher>");
		request.append(LtcRequestInfo.EVENT_PUBLISHER);
		request.append("</event.publisher>");
		// event type
		request.append("<event.type>");
		request.append(LtcRequestInfo.EVENT_TYPE);
		request.append("</event.type>");
		// event owner
		request.append("<event.owner>");
		request.append(LtcRequestInfo.EVENT_OWNER);
		request.append("</event.owner>");
		
		// event extended attributes
		request.append("<event.extended.attributes>");
		// Load Complete, Failed to Initiate, Failed in Process, Suspended, Terminated
		StringTokenizer subscriptionEventsTokens = new StringTokenizer(ltcRequestInfo.getSubscriptionEvents(), ";");
		while(subscriptionEventsTokens.hasMoreTokens()){
			request.append("<status>");
			request.append(subscriptionEventsTokens.nextToken());
			request.append("</status>");
		}
		request.append("</event.extended.attributes>");
		
		request.append("</event.filter>");

		prepareQueueNotificationRequest(request);
		
		request.append("</subscription.data>");
		request.append("</subscription.collection>");
	
		
	}

	private void prepareQueueNotificationRequest(StringBuffer request)
	{
		// notification criteria
		request.append("<notification.criteria>");
		// Queue notification
		request.append("<notification>");
		request.append("<notification.type>");
		request.append("queue");
		request.append("</notification.type>");
		request.append("<notification.data>");
		request.append("<queues>");
		request.append("<queue>");
		// Queue Host
		request.append("<host.name>");
		request.append(ltcRequestInfo.getQueueHost()); //??
		request.append("</host.name>");
		// Queue Manager
		request.append("<queue.manager/>");
		// Queue Name
		request.append("<queue.name>");
		request.append(ltcRequestInfo.getQueueName()); //??
		request.append("</queue.name>");
		// Queue Channel
		request.append("<queue.channel>");
		request.append(ltcRequestInfo.getQueueChannel()); //??
		request.append("</queue.channel>");
		// Message Format
		request.append("<message.format>");
		request.append("String"); //??
		request.append("</message.format>");
		request.append("</queue>");
		request.append("</queues>");
		request.append("</notification.data>");
		request.append("</notification>");
		request.append("</notification.criteria>");
	}

	

	public void setLtcRequestInfo(LtcRequestInfo ltcRequestInfo)
	{
		this.ltcRequestInfo = ltcRequestInfo;
	}
}
