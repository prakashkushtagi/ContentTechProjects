/*
Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Court;
import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.DroppedDocket;
import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.dao.LookUpListDAOImpl;
import com.trgr.dockets.core.domain.AllAndBadDockets;
import com.trgr.dockets.core.domain.Docket;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.exception.MessageParseException;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.jms.parser.SourceLoadRequestMessageParser;
import com.trgr.dockets.core.parser.SourceContentProcessor;
import com.trgr.dockets.core.parser.SourceContentSplitProcessor;
import com.trgr.dockets.core.service.ErrorLogService;
import com.trgr.dockets.core.service.EventLogService;
import com.trgr.dockets.core.service.SourceContentLoader;
import com.trgr.dockets.core.service.SourceLoadRequestService;
import com.trgr.dockets.core.util.DocketsCoreUtility;
import com.trgr.dockets.core.util.Environment;

@Service("sourceContentLoaderProcessor")
public class SourceContentLoaderProcessor implements SourceContentLoader
{
	private static Logger log = Logger.getLogger(SourceContentLoaderProcessor.class);

	@Autowired
	private String wrapperEnvironment;
	@Autowired
	private ErrorLogService errorLogService;
	@Autowired
	private EventLogService eventLogService; 
	@Autowired
	private SourceLoadRequestService sourceLoadRequestService;
	@Autowired
	private BankruptcyContentService bankruptcyContentService;
	private HashMap<String, String>courtMap;
	
	public SourceContentLoaderProcessor()
	{
		this.wrapperEnvironment = Environment.getInstance().getEnv();
		LookUpListDAOImpl lookUpListDAO = new LookUpListDAOImpl(wrapperEnvironment.toLowerCase());
		List<Court> courtList = lookUpListDAO.getCourts();
		courtMap = DocketsCoreUtility.getCourtClusterNormMap(courtList);
	}
	
	public SourceContentLoaderProcessor(String environment)
	{
		this.wrapperEnvironment = environment;
		LookUpListDAOImpl lookUpListDAO = new LookUpListDAOImpl(wrapperEnvironment.toLowerCase());
		List<Court> courtList = lookUpListDAO.getCourts();
		courtMap = DocketsCoreUtility.getCourtClusterNormMap(courtList);
	}
	
	private com.trgr.dockets.core.entity.SourceLoadRequest hydrateSourceRequestEntityObj(SourceLoadRequest sourceLoadRequest) {
		
		com.trgr.dockets.core.entity.SourceLoadRequest sourceLoadRequestEntity = new com.trgr.dockets.core.entity.SourceLoadRequest();
		sourceLoadRequestEntity.setBatchId(sourceLoadRequest.getBatchId().toString());
		sourceLoadRequestEntity.setProductCode(sourceLoadRequest.getProductCode());
		sourceLoadRequestEntity.setSourcePath(sourceLoadRequest.getSourcePath());
		sourceLoadRequestEntity.setAcquisitionMethod(sourceLoadRequest.getAcquisitionMethod());
		sourceLoadRequestEntity.setAcquisitionType(sourceLoadRequest.getAcquisitionType());
		sourceLoadRequestEntity.setTimestamp(DocketsCoreUtility.getAcquisitionTimeStamp(sourceLoadRequest.getAcquisitionStart(), sourceLoadRequest.getProductCode()));
		sourceLoadRequestEntity.setDeleteFlag(sourceLoadRequest.isDeleteFlag());
		return sourceLoadRequestEntity;
	}
	
	public void process(TextMessage textMessage) 
	{

		String sourceLoadRequestMessage = null;
		SourceLoadRequest sourceLoadRequest = null;
		try 
		{
			//parse the message
			sourceLoadRequestMessage = textMessage.getText();
			SourceLoadRequestMessageParser loadRequestMessageParser = new SourceLoadRequestMessageParser();
			sourceLoadRequest =  loadRequestMessageParser.parseMessage(sourceLoadRequestMessage);

			//Persist the message.
			Long requestId =0L;
			requestId = saveSourceLoadRequestToDb(sourceLoadRequest);
			sourceLoadRequest.setRequestId(requestId);

			processRequest(sourceLoadRequest);
		} 
		catch(JMSException jmse)
		{
			log.error("Parsing the message from ltc in the LoadMonitoringService ", jmse);
		}
		catch (MessageParseException mpe) 
		{
			log.error("Parsing the message from ltc in the LoadMonitoringService ", mpe);
		} catch (SourceContentLoaderException e) 
		{
		}
	}

	public List<DroppedDocket> processRequest(SourceLoadRequest sourceLoadRequest) throws SourceContentLoaderException 
	{
//		SourceContentProcessor sourceContentParser = new SourceContentProcessor(errorLogService,eventLogService,sourceLoadRequestService,bankruptcyContentService,courtMap,wrapperEnvironment);
		SourceContentSplitProcessor sourceContentParser = new SourceContentSplitProcessor(eventLogService,bankruptcyContentService,courtMap,wrapperEnvironment);

		sourceContentParser.process(sourceLoadRequest);

		return sourceContentParser.getFailedDockets();
	}

	private Long  saveSourceLoadRequestToDb(SourceLoadRequest sourceLoadRequest) 
	{
		Long requestId =0L;
		com.trgr.dockets.core.entity.SourceLoadRequest sourceLoadRequestDbEntity = hydrateSourceRequestEntityObj(sourceLoadRequest) ;
		try 
		{
			requestId = sourceLoadRequestService.addSourceLoadRequest(sourceLoadRequestDbEntity);
		} 
		catch (SourceContentLoaderException e1) 
		{
			log.error("Unable to save the incoming SCL RequestMessage " + sourceLoadRequestDbEntity.toString());
		}

		return requestId;
	}

	@Override
	public AllAndBadDockets loadDockets(com.trgr.dockets.core.domain.SourceLoadRequest sourceLoadRequest)
			throws Exception 
	{
		SourceContentProcessor sourceContentParser = new SourceContentProcessor(errorLogService,eventLogService,sourceLoadRequestService,bankruptcyContentService,courtMap,wrapperEnvironment);
		
		sourceContentParser.process(convertFromDomainSourceLoadRequest(sourceLoadRequest));
		List<Docket> failedDockets = convertToListOfDomainDockets(sourceContentParser.getFailedDocketsForJpml());
		
		AllAndBadDockets badDockets = new AllAndBadDockets(new ArrayList<Docket>(), failedDockets);
		return badDockets;
	}
	
	private List<Docket> convertToListOfDomainDockets(List<com.trgr.dockets.core.processor.Docket> failedDocketsForJpml) 
	{
		List<Docket> domainDockets = new ArrayList<Docket>();
		
		for(com.trgr.dockets.core.processor.Docket docketSource : failedDocketsForJpml)
		{
			Docket docketDomain = new Docket();
			docketDomain.setCourt(docketSource.getCourt());
			docketDomain.setEncoding(docketSource.getEncoding());
			docketDomain.setErrorMessage(docketSource.getErrorMessage());
			docketDomain.setNumber(docketSource.getNumber());
			docketDomain.setPlatform(docketSource.getPlatform());
			docketDomain.setScrapeDate(docketSource.getScrapeDate());
			docketDomain.setStatePostal(docketSource.getStatePostal());
			domainDockets.add(docketDomain);
		}
		
		return domainDockets;
	}
	private SourceLoadRequest convertFromDomainSourceLoadRequest(com.trgr.dockets.core.domain.SourceLoadRequest sourceLoadRequestDomain) 
	{
		SourceLoadRequest sourceLoadRequest = new SourceLoadRequest();
		sourceLoadRequest.setAcquisitionMethod(sourceLoadRequestDomain.getAcquisitionMethod().toString());
		Date timeStamp = sourceLoadRequestDomain.getAcquisitionStart();
		sourceLoadRequest.setAcquisitionStart(DocketsCoreUtility.createAcquisitionTimeStamp(timeStamp, sourceLoadRequestDomain.getProduct().getCode()));
		sourceLoadRequest.setAcquisitionType(sourceLoadRequestDomain.getRequestInitiatorType().name());
		sourceLoadRequest.setBatchId(sourceLoadRequestDomain.getBatchId());
		sourceLoadRequest.setDeleteFlag(sourceLoadRequestDomain.isDelete());
		sourceLoadRequest.setProductCode(sourceLoadRequestDomain.getProduct().getCode());
		sourceLoadRequest.setRequestId(sourceLoadRequestDomain.getRequestId());
		sourceLoadRequest.setSourcePath(sourceLoadRequestDomain.getSourceFile().getAbsolutePath());
		return sourceLoadRequest;
	}
	
	
	@Override
	public String getDocketDump(Docket sourceDocket) 
	{
		return null;
	}
	
	/**
	 * @return the environment
	 */
	public String getWrapperEnvironment() {
		return wrapperEnvironment;
	}
	/**
	 * @param environment the environment to set
	 */
	public void setWrapperEnvironment(String environment) {
		this.wrapperEnvironment = environment;
	}
	/**
	 * @return the errorLogService
	 */
	public ErrorLogService getErrorLogService() {
		return errorLogService;
	}
	/**
	 * @param errorLogService the errorLogService to set
	 */
	public void setErrorLogService(ErrorLogService errorLogService) {
		this.errorLogService = errorLogService;
	}
	/**
	 * @return the eventLogService
	 */
	public EventLogService getEventLogService() {
		return eventLogService;
	}
	/**
	 * @param eventLogService the eventLogService to set
	 */
	public void setEventLogService(EventLogService eventLogService) {
		this.eventLogService = eventLogService;
	}
	/**
	 * @return the sourceLoadRequestService
	 */
	public SourceLoadRequestService getSourceLoadRequestService() {
		return sourceLoadRequestService;
	}
	/**
	 * @param sourceLoadRequestService the sourceLoadRequestService to set
	 */
	public void setSourceLoadRequestService(
			SourceLoadRequestService sourceLoadRequestService) {
		this.sourceLoadRequestService = sourceLoadRequestService;
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

	@Override
	public List<Docket> getDocketList(com.trgr.dockets.core.domain.SourceLoadRequest sourceLoadRequestDomain) {
		return null;
	}

	@Override
	public List<SourceDocketMetadata> getSourceDocketMetadataList(
			com.trgr.dockets.core.domain.SourceLoadRequest sourceLoadRequestDomain, String filepath) throws Exception {
		return null;
	}

}
