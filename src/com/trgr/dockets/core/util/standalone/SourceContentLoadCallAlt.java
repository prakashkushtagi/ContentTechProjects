package com.trgr.dockets.core.util.standalone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Court;
import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.DroppedDocket;
import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.dao.LookUpListDAOImpl;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.exception.ContentParserException;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.parser.SourceContentSplitProcessor;
import com.trgr.dockets.core.processor.LoadUsingWrapperApi;
import com.trgr.dockets.core.processor.SourceLoadRequest;
import com.trgr.dockets.core.util.DocketsCoreUtility;

public class SourceContentLoadCallAlt
{
	private static final Logger log = Logger.getLogger(SourceContentLoadCallAlt.class);

	public static void main(String[] args)
	{
		List<DroppedDocket> failedDockets = new ArrayList<DroppedDocket>();
		String fileLocationForWrapperCall = null;
		String wrapperEnvironment =  "dev";
		LookUpListDAOImpl lookUpListDAO = new LookUpListDAOImpl(wrapperEnvironment);
		List<Court> courtList = lookUpListDAO.getCourts();
		
		HashMap<String,String>courtMap = DocketsCoreUtility.getCourtClusterNormMap(courtList);
		try
		{ 
			//TODO:drop messages to event queue for source load start

			SourceContentSplitProcessor sourceContentSplitProcessor = new SourceContentSplitProcessor();
			SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
			sourceLoadRequest.setAcquisitionMethod("PROSPECTIVE");
			sourceLoadRequest.setAcquisitionStart(null);
			sourceLoadRequest.setAcquisitionType("DAILY");
			sourceLoadRequest.setBatchId("I54d845c0ec8911e1a40800219b689001");
			sourceLoadRequest.setPublishingRequestId("I54d845c0ec8911e1a40800219b689001");
			sourceLoadRequest.setDeleteFlag(false);
			sourceLoadRequest.setProductCode("FBR");
			sourceLoadRequest.setRequestId(0L);
			sourceLoadRequest.setSourcePath("/dockets/icnullerrortest/sourceForicNulltest/WEST.BKR341.preprocout.071813.002740.xml.gz");
			sourceLoadRequest.setWorkFolder("/dockets/icnullerrortest/sourceForicNulltest/");
			
			BankruptcyContentService bankruptcyContentService = new BankruptcyContentService(true);
			sourceContentSplitProcessor.setCourtMap(courtMap);
			sourceContentSplitProcessor.setBankruptcyContentService(bankruptcyContentService);
			sourceContentSplitProcessor.setWrapperEnvironment(wrapperEnvironment);
			fileLocationForWrapperCall = sourceContentSplitProcessor.processSplit(sourceLoadRequest);
			failedDockets.addAll(sourceContentSplitProcessor.getFailedDockets());
//			fileLocationForWrapperCall="/dockets/icnullerror/";
			LoadUsingWrapperApi loadUsingWrapperApi = new LoadUsingWrapperApi(bankruptcyContentService);
			loadUsingWrapperApi.setCourtMap(courtMap);
			loadUsingWrapperApi.setEnvironment(wrapperEnvironment);
			failedDockets.addAll(loadUsingWrapperApi.loadBulkContent(sourceLoadRequest,fileLocationForWrapperCall));
			String message = "No Failed Dockets";
			if(failedDockets.size()>0)
			{
				message = DocketsCoreUtility.createDroppedDocketXml(failedDockets);
			}
			log.debug("Run Done " + message);
		}
		catch (SourceContentLoaderException e) 
		{
			log.debug(e.getMessage());
		} 
		catch (ContentParserException e) 
		{
			log.debug(e.getMessage());
		} 
		catch (BankruptcyContentServiceException e)
		{
			log.debug(e.getMessage());
		}
		
		//TODO:drop messages to event queue for source load end with any failure informaton including dropped dockets or any other exception.
		//continue to log dropped dockets as you've done in the past.
	}

}
