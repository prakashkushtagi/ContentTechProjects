package com.trgr.dockets.core.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.DroppedDocket;
import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.DroppedDockets;
import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Platform;
import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.util.DocketsCoreUtility;

public class LoadUsingWrapperApi
{
	private BankruptcyContentService bankruptcyContentService;

	private HashMap<String, String> courtMap;

	private String environment;
	
	private static Logger log = Logger.getLogger(LoadUsingWrapperApi.class);
	
	public LoadUsingWrapperApi(BankruptcyContentService bankruptcyContentService)
	{
		this.bankruptcyContentService = bankruptcyContentService;
	}
	
	public List<DroppedDocket> loadBulkContent(SourceLoadRequest sourceLoadRequest,String fileLocationForWrapperCall) throws BankruptcyContentServiceException, SourceContentLoaderException
	{
		Date acquisitionTimestamp = null;
		if(null!=sourceLoadRequest.getAcquisitionStart() && !sourceLoadRequest.getAcquisitionStart().isEmpty())
		{
			acquisitionTimestamp = DocketsCoreUtility.getAcquisitionTimeStamp(sourceLoadRequest.getAcquisitionStart(), sourceLoadRequest.getProductCode());
		}
		
		String mergeFileName = sourceLoadRequest.getSourcePath(); 
		if(mergeFileName.lastIndexOf("\\")>-1)
		{
			mergeFileName = mergeFileName.substring(mergeFileName.lastIndexOf("\\")+1);
		}
		else if(mergeFileName.lastIndexOf("/")>-1)
		{
			mergeFileName = mergeFileName.substring(mergeFileName.lastIndexOf("/")+1);
		}
		
		DroppedDockets droppedDockets = bankruptcyContentService.bulkLoadSourceContent(sourceLoadRequest.getProductCode(),acquisitionTimestamp,mergeFileName, 
                																			sourceLoadRequest.getAcquisitionMethod(), sourceLoadRequest.getAcquisitionType(),
                																			fileLocationForWrapperCall,sourceLoadRequest.getPublishingRequestId(),getEnvironment());
		return droppedDockets.getDroppedDockets();
	}

	public void loadContent(Docket sourceDocket, SourceLoadRequest sourceLoadRequest) throws BankruptcyContentServiceException, SourceContentLoaderException
	{
		String wrapperEnvironment = getEnvironment();
		log.debug("Processeding docket Number ="+sourceDocket.getNumber());
		String courtNorm = courtMap.get(sourceDocket.getCourt().toUpperCase());
		String docketNumber = sourceDocket.getNumber();
		 
		docketNumber = docketNumber.toUpperCase();/** Normalize it to upper case ,wrapper API does it anyways just to be in safer side as in DB normalized dockts are saved **/
		String platformTxt = sourceDocket.getPlatform().toUpperCase().replace('-', '_');
		String sourceContentText = sourceDocket.getDocketXmlString();
		Platform platform=Platform.valueOf(platformTxt);
		String productCode = sourceLoadRequest.getProductCode();
		boolean deleteFlag = sourceLoadRequest.isDeleteFlag();
		String acquisitionStart = sourceLoadRequest.getAcquisitionStart();
		Date acquisitionTimeStamp = DocketsCoreUtility.getAcquisitionTimeStamp(acquisitionStart, sourceLoadRequest.getProductCode());
		Date scrapeTimestamp;
		try 
		{
			scrapeTimestamp = DocketsCoreUtility.getFormatedTimeStamp(sourceDocket.getScrapeDate(),sourceLoadRequest.getProductCode());
		} 
		catch (SourceContentLoaderException e) 
		{
			throw new SourceContentLoaderException("Error formating the scrape date based on the product, scrapeDate::Product " + sourceDocket.getScrapeDate()+"::"+sourceLoadRequest.getProductCode() ,e);
		} 
		String sourceFileName = sourceLoadRequest.getSourcePath(); 
		if(sourceFileName.lastIndexOf("\\")>-1)
		{
			sourceFileName = sourceFileName.substring(sourceFileName.lastIndexOf("\\")+1);
		}
		else if(sourceFileName.lastIndexOf("/")>-1)
		{
			sourceFileName = sourceFileName.substring(sourceFileName.lastIndexOf("/")+1);
		}
		String acquisitionMethod = sourceLoadRequest.getAcquisitionMethod();
		String acquisitionType = sourceLoadRequest.getAcquisitionType();

		if( sourceDocket.isDeleteFlag() && sourceDocket.getPlatform().toUpperCase().startsWith("LCI") ) 
		{   /** LCI deletes will not contain location code and ':' just like Oregon inserts **/
			lciDeletesUsingWrapperService(sourceDocket, wrapperEnvironment, sourceLoadRequest,platform,acquisitionTimeStamp,scrapeTimestamp,courtMap,sourceContentText);
		}
		else
		{ 
			/** Regular LCI insert ,Lexis insert and deletes **/
			bankruptcyContentService.putSourceContentByDocketNumber(productCode,courtNorm,docketNumber,platform,acquisitionTimeStamp,scrapeTimestamp,sourceContentText,sourceFileName,acquisitionMethod,acquisitionType,deleteFlag,sourceLoadRequest.getBatchId(),wrapperEnvironment);
		}

		log.debug("Control returned back to SCL from wrapper service.");												  	 
	}

	public void lciDeletesUsingWrapperService(Docket sourceDocket, String env,SourceLoadRequest sourceLoadRequest,Platform platform,Date acquisitionTimeStamp,Date scrapeTimestamp,HashMap<String,String> courtMap,String docketsClobBlock ) throws  BankruptcyContentServiceException 
	{

		Map<String,String> docNumberMap = DocketsCoreUtility.docketNumberParsing(sourceDocket.getNumber(), sourceLoadRequest.getProductCode().toUpperCase());
		
		String courtNorm = courtMap.get(sourceDocket.getCourt());
		int year = Integer.parseInt(docNumberMap.get(DocketsCoreUtility.KeysEnum.year.toString()));
		String sequenceNumber =  docNumberMap.get(DocketsCoreUtility.KeysEnum.sequenceNo.toString());
		List<com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Docket>
		docktsList = bankruptcyContentService.getDocketsByCourtYearSequenceNumber(courtNorm,year,sequenceNumber,env);

		if (docktsList != null && docktsList.size()>0) {
			
			for (com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Docket docket : docktsList) {
				/**
				 * This docketsList is object we get from bankruptcyContentService which we insert back with new content form source 
				 * 
				 */
				String courtNormSource = courtMap.get(sourceDocket.getCourt().toUpperCase());
				String fileName = sourceLoadRequest.getSourcePath();
				
				bankruptcyContentService.putSourceContentByDocketNumber(sourceLoadRequest.getProductCode().toUpperCase(),
						courtNormSource, docket.getDocketNumber().toUpperCase(),
						platform,acquisitionTimeStamp, scrapeTimestamp, docketsClobBlock ,//new Date()  
						fileName,
						sourceLoadRequest.getAcquisitionMethod().toUpperCase(),
						sourceLoadRequest.getAcquisitionType().toUpperCase(),
						sourceLoadRequest.isDeleteFlag(),sourceLoadRequest.getBatchId(), env);
			}
		}

	}

	/**
	 * @return the bankruptcyContentService
	 */
	public BankruptcyContentService getBankruptcyContentService() {
		return bankruptcyContentService;
	}

	/**
	 * @param bankruptcyContentService the bankruptcyContentService to set
	 */
	public void setBankruptcyContentService(
			BankruptcyContentService bankruptcyContentService) {
		this.bankruptcyContentService = bankruptcyContentService;
	}

	/**
	 * @return the courtMap
	 */
	public HashMap<String, String> getCourtMap() {
		return courtMap;
	}

	/**
	 * @param courtMap the courtMap to set
	 */
	public void setCourtMap(HashMap<String, String> courtMap) {
		this.courtMap = courtMap;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

}
