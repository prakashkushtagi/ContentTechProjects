/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */

package com.trgr.dockets.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Court;
import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Platform;
import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.dao.LookUpListDAOImpl;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.domain.AllAndBadDockets;
import com.trgr.dockets.core.domain.Docket;
import com.trgr.dockets.core.domain.DocketsList;
import com.trgr.dockets.core.domain.Page;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.domain.SourceLoadRequest;
import com.trgr.dockets.core.entity.ErrorLog;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.util.DocketFileParser;
import com.trgr.dockets.core.util.DocketMarshllerAndUnmarshller;
import com.trgr.dockets.core.util.DocketsCoreUtility;

/**
 * This class parse source dockets and pass on metadata and actual docket block
 * to repository through wrapper API.
 * 
 * @author U0105927 Mahendra Survase (Mahendra.Survase@thomsonreuters.com)
 * 
 */
// @Deprecated
public class SourceContentLoaderImpl implements SourceContentLoader {
	private static final Logger LOG = Logger.getLogger(SourceContentLoaderImpl.class);

	private ErrorLogService errorLogService;
	private EventLogService eventLogService;
	private String wrapperEnvironment;
	private DocketMarshllerAndUnmarshller docketMarshllerAndUnmarshller;
	private SourceLoadRequestService sourceLoadRequestService;

	public SourceContentLoaderImpl(ErrorLogService errorLogService, EventLogService eventLogService, String wrapperEnvironment,
			DocketMarshllerAndUnmarshller docketMarshllerAndUnmarshller, SourceLoadRequestService sourceLoadRequestService) {
		this.errorLogService = errorLogService;
		this.eventLogService = eventLogService;
		this.wrapperEnvironment = wrapperEnvironment;
		this.docketMarshllerAndUnmarshller = docketMarshllerAndUnmarshller;
		this.sourceLoadRequestService = sourceLoadRequestService;
	}

	@Override
	@Transactional
	public AllAndBadDockets loadDockets(SourceLoadRequest sourceLoadRequestDomain) throws Exception {
		InputStream in = null;
		List<Docket> sourceDocketsList = new ArrayList<Docket>();
		List<Docket> errorDocketsList = new ArrayList<Docket>();
		DocketsList docketsList = null;
		Long requestId = null;
		String exceptionMessage = null;
		File path = null;
		InetAddress localHost = InetAddress.getLocalHost();
		String machineName = localHost.getHostName();
		com.trgr.dockets.core.entity.SourceLoadRequest sourceLoadRequestEntity = hydrateSourceRequestEntityObj(sourceLoadRequestDomain);
		long startTime = 0;
		long parseEndTime = 0;
		long loadEndTime = 0;
		try {
			requestId = sourceLoadRequestService.addSourceLoadRequest(sourceLoadRequestEntity);
			path = sourceLoadRequestDomain.getSourceFile();

			if (null != requestId && requestId > 0) {

				logStartEvent(sourceLoadRequestDomain, machineName);
				if (path != null) {
					startTime = System.currentTimeMillis();
					LOG.debug("Process start time " + startTime + " in milliseconds file name =" + path.getPath());

					if (path.getAbsolutePath().endsWith(".gz")) {
						// process gz file.
						in = new GZIPInputStream(new FileInputStream(path));

						docketsList = docketMarshllerAndUnmarshller.docketsUnmarshller(in);

						parseEndTime = logTimeTakenToReadAndParseDocketsMergeFile(docketsList, startTime);

						errorDocketsList = loadDocketsUsingWrapperService(docketsList, sourceLoadRequestDomain);

						loadEndTime = logTimeTakenToLoadDocketsThroughWrapperAPI(docketsList, errorDocketsList, parseEndTime);

					} else if (path.getAbsolutePath().endsWith(".xml")) {
						// process xml files.
						in = new FileInputStream(path);

						docketsList = docketMarshllerAndUnmarshller.docketsUnmarshller(in);

						parseEndTime = logTimeTakenToReadAndParseDocketsMergeFile(docketsList, startTime);

						errorDocketsList = loadDocketsUsingWrapperService(docketsList, sourceLoadRequestDomain);

						loadEndTime = logTimeTakenToLoadDocketsThroughWrapperAPI(docketsList, errorDocketsList, parseEndTime);

					}
					sourceDocketsList = docketsList.getDocketList();
				}
			}
		} catch (FileNotFoundException e) {
			exceptionMessage = "Failed to process merge file FileNotFoundException :" + e.getMessage();
			LOG.debug(exceptionMessage);

		} catch (IOException e) {
			exceptionMessage = "Failed to process merge file SourceContentLoaderException :" + e.getMessage();
			LOG.debug(exceptionMessage);

		} catch (SourceContentLoaderException e) {
			exceptionMessage = "Failed to process merge file SourceContentLoaderException :" + e.getMessage();
			LOG.debug(exceptionMessage);

		} catch (Exception e) {
			exceptionMessage = "Failed to process merge file :" + e.getMessage();
			LOG.debug(exceptionMessage);
		} finally {
			checkForErrorsAndPersist(requestId, sourceDocketsList, errorDocketsList, sourceLoadRequestDomain,
					sourceLoadRequestEntity, exceptionMessage, machineName);

		}

		if (LOG.isDebugEnabled()) {
			printFinalReport(errorDocketsList, docketsList, path, startTime, parseEndTime, loadEndTime);
		}

		return new AllAndBadDockets(sourceDocketsList, errorDocketsList);
	}

	@Transactional
	public List<Docket> getDocketList(SourceLoadRequest sourceLoadRequestDomain) throws Exception {
		InputStream in = null;
		List<Docket> sourceDocketsList = new ArrayList<Docket>();
		try {
			File path = sourceLoadRequestDomain.getSourceFile();
			if (path != null) {
				if (path.getAbsolutePath().endsWith(".gz")) {
					// process gz file.
					in = new GZIPInputStream(new FileInputStream(path));

				} else if (path.getAbsolutePath().endsWith(".xml")) {
					// process xml files.
					in = new FileInputStream(path);
				} else {
					throw new NotImplementedException("File has neither a gzip nor a xml extension. Please check inputs");
				}
				DocketsList docketsList = docketMarshllerAndUnmarshller.docketsUnmarshller(in);
				sourceDocketsList = docketsList.getDocketList();
				return sourceDocketsList;
			}
			throw new Exception("Path from sourceLoadRequestDomain was null. It was not expected to be.");

		} catch (Exception e) {
			LOG.error("Failed to process merge file :" + e.getMessage());
			throw new Exception("Failed to process merge file: " + e.getMessage());
		}
	}

	public List<SourceDocketMetadata> getSourceDocketMetadataList (SourceLoadRequest sourceLoadRequestDomain, String filepath) throws Exception {
		InputStream in = null;
		List<SourceDocketMetadata> sdmList = new ArrayList<SourceDocketMetadata>();
		try {
			File path = sourceLoadRequestDomain.getSourceFile();
			if (path != null) {
				if (path.getAbsolutePath().endsWith(".gz")) {
					// process gz file.
					in = new GZIPInputStream(new FileInputStream(path));

				} else if (path.getAbsolutePath().endsWith(".xml")) {
					// process xml files.
					in = new FileInputStream(path);
				} else {
					throw new NotImplementedException("File has neither a gzip nor a xml extension. Please check inputs");
				}
				sdmList = new DocketFileParser().buildSourceDocketMetadata(in, path.toString(), filepath);
				
			}

		} catch (Exception e) {
			LOG.error("Failed to process merge file :" + e.getMessage());
			throw new Exception("Failed to process merge file: " + e.getMessage());
		}
		finally {
			if (in != null){
				in.close();
			}
		}
		return sdmList;
	}
	/**
	 * persists dockets in DB using wrapper api.
	 * 
	 * @param docketList
	 * @param sourceLoadRequest
	 * @return List<Docket> docketsFailed
	 * @throws SourceContentLoaderException
	 */
	private List<Docket> loadDocketsUsingWrapperService(DocketsList docketList, SourceLoadRequest sourceLoadRequest)
			throws SourceContentLoaderException {

		BankruptcyContentService bankruptcyContentService = new BankruptcyContentService(true);

		LookUpListDAOImpl lookUpListDAO = new LookUpListDAOImpl(wrapperEnvironment);
		List<Court> courtList = lookUpListDAO.getCourts();
		HashMap<String, String> courtMap = DocketsCoreUtility.getCourtClusterNormMap(courtList);

		String productCode = sourceLoadRequest.getProduct().name().toUpperCase();
		boolean deleteFlag = sourceLoadRequest.isDelete();
		List<Docket> dockets = docketList.getDocketList();
		List<Docket> docketsFailed = new ArrayList<Docket>();

		String acquisitionMethod = sourceLoadRequest.getAcquisitionMethod().name().toUpperCase();
		String acquisitionType = sourceLoadRequest.getRequestInitiatorType().getCode();
		Date acquisitionTimeStamp = sourceLoadRequest.getAcquisitionStart();
		String sourceFileName = DocketsCoreUtility.getFileName(sourceLoadRequest.getSourceFile().getAbsolutePath());

		for (Docket sourceDocket : dockets) {

			LOG.debug("Processeding docket Number =" + sourceDocket.getNumber());
			String courtNorm = courtMap.get(sourceDocket.getCourt().toUpperCase());
			String docketNumber = sourceDocket.getNumber();

			docketNumber = docketNumber.toUpperCase();
			/**
			 * Normalize it to upper case ,wrapper API does it anyways just to
			 * be in safer side as in DB normalized dockts are saved
			 **/
			String platformTxt = sourceDocket.getPlatform().toUpperCase().replace('-', '_');

			Platform platform = Platform.valueOf(platformTxt);
			Date scrapeTimestamp = DocketsCoreUtility.getFormatedTimeStamp(sourceDocket.getScrapeDate(), productCode);
			String sourceContentText = docketMarshllerAndUnmarshller.docketDumpMarshaller(sourceDocket);

			try {

				if (deleteFlag & !docketNumber.contains(":")) {
					/**
					 * LCI deletes will not contain location code and ':' just
					 * like Oragone inserts
					 **/
					lciDeletesUsingWrapperService(bankruptcyContentService, sourceDocket, wrapperEnvironment, sourceLoadRequest,
							platform, acquisitionTimeStamp, scrapeTimestamp, courtMap, sourceContentText);

				} else {

					printValuesBeingPassedToWrapper(productCode, courtNorm, docketNumber, platform, acquisitionTimeStamp,
							scrapeTimestamp, sourceContentText, sourceFileName, acquisitionMethod, acquisitionType, deleteFlag,
							wrapperEnvironment);
					/** Regular LCI insert ,Lexis insert and deletes **/
					bankruptcyContentService.putSourceContentByDocketNumber(productCode, courtNorm, docketNumber, platform,
							acquisitionTimeStamp, scrapeTimestamp, sourceContentText, sourceFileName, acquisitionMethod,
							acquisitionType, deleteFlag, sourceLoadRequest.getBatchId(), wrapperEnvironment);
				}
				sourceDocket.setPageList(null);
				LOG.debug("Control returned back to SCL from wrapper service.");

			} catch (BankruptcyContentServiceException e) {
				LOG.debug("BankruptcyContentServiceException : " + e.getMessage());
				/**
				 * Dockets can raise business exception related version
				 * timestamp ,scrape timestamp log those dockets and keep on
				 * processing merge file. append error message to "End event"
				 * indicating that not all dockets were loaded. pointing them to
				 * log.
				 **/
				Docket faileDocket = sourceDocket;
				faileDocket.setPageList(null);
				faileDocket.setErrorMessage(e.getMessage());
				docketsFailed.add(faileDocket);
			}
		}

		return docketsFailed;
	}

	/**
	 * we found better way to rebuild dockets block using JAXB marshler. keeping
	 * this method in case if we ever decide to get away from JAXB.
	 */
	@Override
	public String getDocketDump(Docket sourceDocket) {
		String docketDump = null;
		String docketNo = sourceDocket.getNumber();
		String encoding = sourceDocket.getEncoding();
		String scrapeDate = sourceDocket.getScrapeDate();
		String court = sourceDocket.getScrapeDate();
		String platform = sourceDocket.getPlatform();
		String state = sourceDocket.getStatePostal();
		List<Page> pageList = sourceDocket.getPageList();
		docketDump = "<docket number=" + docketNo + " encoding=" + encoding + " scrape.date=" + scrapeDate + " court=" + court
				+ " platform=" + platform + " state.postal=" + state + ">";
		for (Page page : pageList) {
			docketDump += "<page name=" + page.getName() + ">";
			docketDump += page.getDocketContent();
			docketDump += "</page>";
		}
		docketDump += "</docket>";
		return docketDump;
	}

	/**
	 * LCI delete retrieve dockets marked for deletion from repository and
	 * update their contents with the one from source dockets.
	 * 
	 * @param bankruptcyContentService
	 * @param docketList
	 * @param sourceLoadRequest
	 * @throws SourceContentLoaderException
	 * @throws BankruptcyContentServiceException
	 */
	private void lciDeletesUsingWrapperService(BankruptcyContentService bankruptcyContentService, Docket sourceDocket, String env,
			SourceLoadRequest sourceLoadRequest, Platform platform, Date acquisitionTimeStamp, Date scrapeTimestamp,
			HashMap<String, String> courtMap, String docketsClobBlock) throws SourceContentLoaderException,
			BankruptcyContentServiceException {

		try {
			Map<String, String> docNumbMap = DocketsCoreUtility.docketNumberParsing(sourceDocket.getNumber(), sourceLoadRequest
					.getProduct().name().toUpperCase());

			String courtNorm = courtMap.get(sourceDocket.getCourt());
			int year = Integer.parseInt(docNumbMap.get(DocketsCoreUtility.KeysEnum.year.toString()));
			String sequenceNumber = docNumbMap.get(DocketsCoreUtility.KeysEnum.sequenceNo.toString());
			List<com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Docket> docktsList = bankruptcyContentService
					.getDocketsByCourtYearSequenceNumber(courtNorm, year, sequenceNumber, env);

			if (docktsList != null && docktsList.size() > 0) {

				for (com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Docket docket : docktsList) {
					/**
					 * This docketsList is object we get from
					 * bankruptcyContentService which we insert back with new
					 * content form source
					 * 
					 */
					String courtNormSource = courtMap.get(sourceDocket.getCourt().toUpperCase());
					String fileName = DocketsCoreUtility.getFileName(sourceLoadRequest.getSourceFile().getAbsolutePath());

					// TODO: remove this method its just for innitial debug in
					// dev,test environment.
					printValuesBeingPassedToWrapper(sourceLoadRequest.getProduct().name(), courtNormSource, sourceDocket
							.getNumber().toUpperCase(), platform, acquisitionTimeStamp, scrapeTimestamp, docketsClobBlock,
							fileName, sourceLoadRequest.getAcquisitionMethod().name().toUpperCase(), sourceLoadRequest
									.getRequestInitiatorType().getCode(), sourceLoadRequest.isDelete(), env);

					bankruptcyContentService.putSourceContentByDocketNumber(sourceLoadRequest.getProduct().name().toUpperCase(),
							courtNormSource, sourceDocket.getNumber().toUpperCase(), platform, acquisitionTimeStamp,
							scrapeTimestamp, docketsClobBlock,// new Date()
							fileName, sourceLoadRequest.getAcquisitionMethod().name().toUpperCase(), sourceLoadRequest
									.getRequestInitiatorType().getCode(), sourceLoadRequest.isDelete(), sourceLoadRequest
									.getBatchId(), env);
				}
			}

		} catch (BankruptcyContentServiceException e) {
			// e.printStackTrace();
			LOG.debug("Failed to persist 'Delete' docket with docketNumber :" + sourceDocket.getNumber() + " : " + e);
			throw e;
			/***
			 * Re-throwing exception so that calling method could catch and note
			 * this docket as failed docket
			 ***/
		}

	}

	/**
	 * @param requestId
	 * @param errorDocList
	 * @param sourceLoadRequestDomain
	 * @param sourceLoadRequestEntity
	 * @param exceptionMessage
	 * @return TODO
	 * @throws Exception
	 */
	private String checkForErrorsAndPersist(Long requestId, List<Docket> sourceDocList, List<Docket> errorDocList,
			com.trgr.dockets.core.domain.SourceLoadRequest sourceLoadRequestDomain,
			com.trgr.dockets.core.entity.SourceLoadRequest sourceLoadRequestEntity, String exceptionMessage, String machineName)
			throws Exception {

		String returnMessage = null;

		/*********** Log error in error table ************/
		if (null != requestId) {
			String errorMessage = ((exceptionMessage != null && !exceptionMessage.equalsIgnoreCase("")) ? exceptionMessage : "");
			ErrorLog errorLog = null;

			if (errorDocList != null && errorDocList.size() > 0) {

				returnMessage = "Failed to process " + errorDocList.size() + " " + getFailedDocketsNumbers(errorDocList)
						+ " dockets out of " + sourceDocList.size() + " dockets ";
				errorLog = new ErrorLog(requestId, sourceLoadRequestDomain.getProduct().name() + "_" + errorDocList.size(),
						returnMessage);
				errorLogService.addErrorLog(errorLog);

			} else {
				if (!errorMessage.equalsIgnoreCase("")) {
					returnMessage = "Failed to process merge file :" + errorMessage;
					errorLog = new ErrorLog(requestId, sourceLoadRequestDomain.getProduct().name() + "_001", returnMessage);
					errorLogService.addErrorLog(errorLog);

				}
			}

			logEndEvent(sourceLoadRequestDomain, sourceDocList, errorDocList, exceptionMessage, machineName);

		} else if (null == requestId) {

			/**
			 * Can not add entry to error table as requistId is FK which is
			 * null.
			 **/
			LOG.debug("Failed to persist SourceLoadRequest. " + sourceLoadRequestEntity.toString());
			returnMessage = "Failed to persist SourceLoadRequest. " + sourceLoadRequestEntity.toString();
		}

		return returnMessage;
	}

	/**
	 * @param sourceLoadRequestDomain
	 * @throws Exception
	 */
	private void logStartEvent(com.trgr.dockets.core.domain.SourceLoadRequest sourceLoadRequestDomain, String machineName)
			throws Exception {
		String product = sourceLoadRequestDomain.getProduct().name();

		/** send event log message only for bankruptcy FBR **/
		if (product.equalsIgnoreCase("FBR")) {
			eventLogService.logEventMessage("dockets.dcrsourceload.start", sourceLoadRequestDomain.getBatchId().toString(),
					machineName, 0, sourceLoadRequestDomain.getSourceFile().getAbsolutePath(), null, null, null, null, null);
		}

	}

	/**
	 * 
	 * @param sourceLoadRequestDomain
	 * @throws Exception
	 */
	private void logEndEvent(com.trgr.dockets.core.domain.SourceLoadRequest sourceLoadRequestDomain, List<Docket> sourceDocList,
			List<Docket> errorDocList, String exceptionMessage, String machineName) throws Exception {

		int docCount = 0;
		int docCountFailed = 0;
		boolean exceptionFlag = false;
		String errorCode = null;
		String errorDescription = null;
		String product = sourceLoadRequestDomain.getProduct().name();

		/** failed to access or parse source file **/
		if (null != sourceDocList && !sourceDocList.isEmpty()) {
			docCount = sourceDocList.size();
		}
		/** able to parse source file with failed dockets **/
		if (null != errorDocList && !errorDocList.isEmpty()) {
			docCountFailed = errorDocList.size();
		}
		/** exception while accessing file or processing file **/
		if (null != exceptionMessage && !exceptionMessage.equalsIgnoreCase("")) {
			exceptionFlag = true;
		}

		if (docCount == 0 | docCountFailed > 0 | exceptionFlag) {
			errorCode = "SCL_100";
			if (exceptionFlag) {
				errorDescription = "Out off " + docCount + " dockets failed to process " + docCountFailed + " dockets "
						+ getFailedDocketsNumbers(errorDocList) + ":" + exceptionMessage;
			} else {
				errorDescription = "Out off " + docCount + " dockets failed to process " + docCountFailed + " dockets."
						+ getFailedDocketsNumbers(errorDocList);
			}
		}

		/** send event log message only for bankruptcy FBR **/
		if (product.equalsIgnoreCase("FBR")) {

			eventLogService.logEventMessage("dockets.dcrsourceload.end", sourceLoadRequestDomain.getBatchId().toString(),
					machineName, docCount, sourceLoadRequestDomain.getSourceFile().getAbsolutePath(), null, errorCode,
					errorDescription, null, null);
		}
		// (eventName, batchId, serverName, docCount, fileName, fileSize,
		// errorCode, errorDescription, droppedDockets)
	}

	/**
	 * 
	 * @param errorDocList
	 * @return
	 */
	private String getFailedDocketsNumbers(List<Docket> errorDocList) {
		String failedDocNumberString = "";
		if (errorDocList != null && errorDocList.size() > 0) {
			for (Docket docket : errorDocList) {
				if (failedDocNumberString == null) {
					failedDocNumberString = '(' + docket.getNumber();
				} else {
					failedDocNumberString += "," + docket.getNumber();
				}
			}
			failedDocNumberString += ')';
		}
		return failedDocNumberString;
	}

	private com.trgr.dockets.core.entity.SourceLoadRequest hydrateSourceRequestEntityObj(SourceLoadRequest sourceLoadRequestDomain) {

		com.trgr.dockets.core.entity.SourceLoadRequest sourceLoadRequestEntity = new com.trgr.dockets.core.entity.SourceLoadRequest();
		sourceLoadRequestEntity.setBatchId(sourceLoadRequestDomain.getBatchId().toString());
		sourceLoadRequestEntity.setProductCode(sourceLoadRequestDomain.getProduct().name());
		sourceLoadRequestEntity.setSourcePath(sourceLoadRequestDomain.getSourceFile().getAbsolutePath());
		sourceLoadRequestEntity.setAcquisitionMethod(sourceLoadRequestDomain.getAcquisitionMethod().name());
		sourceLoadRequestEntity.setAcquisitionType(sourceLoadRequestDomain.getRequestInitiatorType().getCode());
		sourceLoadRequestEntity.setTimestamp(sourceLoadRequestDomain.getAcquisitionStart());
		sourceLoadRequestEntity.setDeleteFlag(sourceLoadRequestDomain.isDelete());
		return sourceLoadRequestEntity;
	}

	/**
	 * Temporary method debug values being passed on to wrapper server
	 * 
	 * @param productCode
	 * @param courtNorm
	 * @param docketNumber
	 * @param platform
	 * @param acquisitionTimeStamp
	 * @param scrapeTimestamp
	 * @param sourceContentText
	 * @param sourceFileName
	 * @param acquisitionMethod
	 * @param acquisitionType
	 * @param deleteFlag
	 * @param wrapperEnvironment2
	 */
	private void printValuesBeingPassedToWrapper(String productCode, String courtNorm, String docketNumber, Platform platform,
			Date acquisitionTimeStamp, Date scrapeTimestamp, String sourceContentText, String sourceFileName,
			String acquisitionMethod, String acquisitionType, boolean deleteFlag, String wrapperEnvironment2) {
		LOG.debug("Values being passed on to wrapper API.");
		LOG.debug("productCode :" + productCode + "  courtNorm :" + courtNorm + "  docketNumber :" + docketNumber + " platform :"
				+ platform.name() + " acquisitionTimeStamp :" + acquisitionTimeStamp + " scrapeTimestamp :" + scrapeTimestamp
				+ " sourceContentText :" + "<DocketBlock>...</DocketBlock>" + " sourceFileName :" + sourceFileName
				+ " acquisitionMethod :" + acquisitionMethod + " acquisitionType :" + acquisitionType + " deleteFlag :"
				+ deleteFlag + " wrapperEnvironment2 :" + wrapperEnvironment2);

	}

	/**
	 * Method Summarize parse and load process, assist in monitoring. .
	 * 
	 * @param errorDocketsList
	 * @param docketsList
	 * @param path
	 * @param startTime
	 * @param parseEndTime
	 * @param loadEndTime
	 */
	private void printFinalReport(List<Docket> errorDocketsList, DocketsList docketsList, File path, long startTime,
			long parseEndTime, long loadEndTime) {
		LOG.debug("SCL done processsing file: " + path.getPath());
		LOG.debug(String.format("SCL parsed %d dockets in %d milliseconds.", docketsList.getDocketList().size(),
				(parseEndTime - startTime)));
		int errorDocSize = (errorDocketsList != null) ? errorDocketsList.size() : 0;
		long timeToLoad = (loadEndTime - parseEndTime);
		LOG.debug(String.format("SCL loaded %d out of %d dockets in %d milliseconds.",
				(docketsList.getDocketList().size() - errorDocSize), docketsList.getDocketList().size(), timeToLoad));
	}

	/**
	 * Log statements to monitor dockets parse performance.
	 * 
	 * @param docketsListSize
	 * @param startTime
	 */
	private long logTimeTakenToReadAndParseDocketsMergeFile(DocketsList docketsList, long startTime) {

		long parseEndTime = System.currentTimeMillis();
		int docketsListSize = 0;
		if (docketsList != null && docketsList.getDocketList() != null && docketsList.getDocketList().size() > 0) {
			docketsListSize = docketsList.getDocketList().size();
		}
		LOG.debug("Parsed " + docketsListSize + " dockets in " + (parseEndTime - startTime) + " milliseconds");
		return parseEndTime;
	}

	/**
	 * Log statement to monitor dockets load performance.
	 * 
	 * @param docketsListSize
	 * @param startTime
	 */
	private long logTimeTakenToLoadDocketsThroughWrapperAPI(DocketsList docketsList, List<Docket> badDocketsList, long parseEndTime) {
		long loadEndTime = System.currentTimeMillis();
		int docketsListSize = 0;
		int badDockListSize = 0;
		if (docketsList != null && docketsList.getDocketList() != null && docketsList.getDocketList().size() > 0) {
			docketsListSize = docketsList.getDocketList().size();
		}

		if (badDocketsList != null && badDocketsList.size() > 0) {
			badDockListSize = badDocketsList.size();
		}
		LOG.debug("Loaded " + (docketsListSize - badDockListSize) + " dockets in " + (loadEndTime - parseEndTime) + " milliseconds");
		return loadEndTime;
	}
}
