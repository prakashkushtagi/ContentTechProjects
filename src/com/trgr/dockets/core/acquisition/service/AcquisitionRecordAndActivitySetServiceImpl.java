/**
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.acquisition.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.acquisition.AcquisitionConstants;
import com.trgr.dockets.core.acquisition.AcquisitionDocket;
import com.trgr.dockets.core.acquisition.AcquisitionLogCourtRecord;
import com.trgr.dockets.core.acquisition.AcquisitionLogDocketRecord;
import com.trgr.dockets.core.acquisition.AcquisitionRecord;
import com.trgr.dockets.core.acquisition.AcquisitionResponse;
import com.trgr.dockets.core.acquisition.ActivitySet;
import com.trgr.dockets.core.acquisition.ActivitySetHandler;
import com.trgr.dockets.core.acquisition.FailedAcquisitionDocket;
import com.trgr.dockets.core.acquisition.FailedDocketAcquisitionRecordHandler;
import com.trgr.dockets.core.acquisition.NewAcquisitionRecordHandler;
import com.trgr.dockets.core.acquisition.PreDocketSourceHandler;
import com.trgr.dockets.core.acquisition.WorkflowInformation;
import com.trgr.dockets.core.content.service.FailedAcquisitionDocketContentService;
import com.trgr.dockets.core.content.service.PreDocketContentService;
import com.trgr.dockets.core.domain.DocketsStepLogger;
import com.trgr.dockets.core.domain.PreDocket;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.AcquisitionLookup;
import com.trgr.dockets.core.entity.Batch;
import com.trgr.dockets.core.entity.BatchDocket;
import com.trgr.dockets.core.entity.BatchDocketKey;
import com.trgr.dockets.core.entity.BatchMonitor;
import com.trgr.dockets.core.entity.BatchMonitorKey;
import com.trgr.dockets.core.entity.CaseSubType;
import com.trgr.dockets.core.entity.CodeTableValues.CaseTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.trgr.dockets.core.entity.County;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocGrabberMonitor;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingControlDocGrabber;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.Status;
import com.trgr.dockets.core.exception.DocketsPersistenceException;
import com.trgr.dockets.core.service.CountyService;
import com.trgr.dockets.core.service.DocGrabberMonitorService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.service.NotificationService;
import com.trgr.dockets.core.service.ProcessAdapter;
import com.trgr.dockets.core.util.AcquisitionErrorUtility;
import com.trgr.dockets.core.util.DctDocketNumberUtility;
import com.trgr.dockets.core.util.DocketFileUtils;
import com.trgr.dockets.core.util.DocketServiceUtil;
import com.trgr.dockets.core.util.DocketsCoreUtility;
import com.trgr.dockets.core.util.EmailUtility;
import com.trgr.dockets.core.util.Environment;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;
//import org.apache.log4j.Logger;
public class AcquisitionRecordAndActivitySetServiceImpl implements AcquisitionRecordAndActivitySetService, InitializingBean
{
	//private static final Logger log = Logger.getLogger(AcquisitionRecordAndActivitySetServiceImpl.class);
	//private static final Logger log = Logger.getLogger(DocketConfigFactoryImpl.class);
	private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
	//FOR BKR only the following two are required
	private DocketService docketService;
	private boolean sendEmailFlag;
	
	//For NY pre dockets
	private ProcessAdapter processAdapter;
	private CountyService countyService;
	private PreDocketContentService preDocketContentService;
	private FailedAcquisitionDocketContentService failedAcquisitionDocketContentService;
	private String rootWorkDirectory;
	private String preDocketRootDirectoryState;
	private String preDocketRootDirectoryFederal;
	protected NotificationService notificationService;
	protected DocGrabberMonitorService docGrabberMonitorService;

	
	private SAXParser acquisitionRecordParser;
	
	@Override
	public void afterPropertiesSet() throws Exception
	{
		acquisitionRecordParser = SAXParserFactory.newInstance().newSAXParser();
		
	}
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.acquisition.service.AcquisitionRecordAndActivitySetService#processBKRAcquisitionRecord(java.io.File, com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum, com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum)
	 */
	@Override
	public synchronized AcquisitionResponse processBKRAcquisitionRecord(File acquisitionRecordFile, RequestTypeEnum requestTypeEnum,
			WorkflowTypeEnum workflowTypeEnum, WorkflowInformation workflowInformation, PublishingRequest... publishingRequests) throws Exception
	{
		log.info("inside processBKRAcquisitionRecord" );
		
		InputStream acquisitionRecordFileStream = null;
		NewAcquisitionRecordHandler handler = new NewAcquisitionRecordHandler();
		try{
			acquisitionRecordFileStream = FileUtils.openInputStream(acquisitionRecordFile);
			log.info("acquisition Record file:"+acquisitionRecordFile.toString());
			acquisitionRecordParser.parse(acquisitionRecordFileStream, handler);
			List<AcquisitionRecord> acquisitionRecordList = handler.getAcquisitionRecordList();

			log.info("acquisitionRecordList:"+acquisitionRecordList+ ","+ "requestTypeEnum:"+requestTypeEnum+"," +"workflowInformation:"+workflowInformation +","+"publishingRequests:"+Arrays.toString(publishingRequests));
            return createBKRBatchAndMonitor(acquisitionRecordList, requestTypeEnum, workflowTypeEnum, workflowInformation, publishingRequests);
		}catch(Exception e){
			log.error("Unexpected error occurred while handling the acquisitionRecordFile" + acquisitionRecordFile.getPath(), e);
			//send email
			String msgBody = AcquisitionConstants.ACQUISITION_EMAIL_BODY;
			String subject = AcquisitionConstants.ACQUISITION_EMAIL_SUBJECT + Environment.getInstance().getEnv();
			if(!(e instanceof IOException)){
				//we have access to the file contents. Update the msg body with file contents
				IOUtils.closeQuietly(acquisitionRecordFileStream);
				String fileContents = FileUtils.readFileToString(acquisitionRecordFile);
				msgBody = msgBody + "\n" + fileContents;
			}
			if(sendEmailFlag){
				EmailUtility.sendEmail(AcquisitionConstants.ACQUISITION_EMAIL_HOST_NAME, AcquisitionConstants.ACQUISITION_EMAIL_FROM, StringUtils.splitPreserveAllTokens(AcquisitionConstants.ACQUISITION_EMAIL_TO, ';'), subject, msgBody);
			}
			throw e;
		}
		finally
		{
			IOUtils.closeQuietly(acquisitionRecordFileStream);
		}

	}
	
	@Override
	public AcquisitionResponse processAcquisitionRecord(String acquisitionRecordStr) throws Exception
	{
		InputStream acquisitionRecordStream = null;
		NewAcquisitionRecordHandler handler = new NewAcquisitionRecordHandler();
		
		try{
			acquisitionRecordStream = IOUtils.toInputStream(acquisitionRecordStr);
			acquisitionRecordParser.parse(acquisitionRecordStream, handler);
			AcquisitionRecord acquisitionRecord = handler.getAcquisitionRecord();
			return createBatchAndMonitor(acquisitionRecord);
		}catch(Exception e){
			log.error("Unexpected error occurred while handling the acquisitionRecord" + acquisitionRecordStr, e);
			throw e;
		}finally{
			IOUtils.closeQuietly(acquisitionRecordStream);
		}
	}
	
	

	@Override
	public void processDocGrabberAcquisitionRecord(String acquisitionRecordStr) throws Exception
	{
		InputStream acquisitionRecordStream = null;
		NewAcquisitionRecordHandler handler = new NewAcquisitionRecordHandler();
		
		try{
			acquisitionRecordStream = IOUtils.toInputStream(acquisitionRecordStr);
			acquisitionRecordParser.parse(acquisitionRecordStream, handler);
			AcquisitionRecord acquisitionRecord = handler.getAcquisitionRecord();
			DocGrabberMonitor docGrabberMonitor = docGrabberMonitorService.findDocGrabberMonitorByReceiptId(acquisitionRecord.getAcquisitionReceiptId());
			if(docGrabberMonitor == null){
				docGrabberMonitor = populateDocGrabberMonitor(acquisitionRecord);
			}else{
 				log.error("Received DocGrabberMonitor record previously processed.ReceiptId :"+acquisitionRecord.getAcquisitionReceiptId());
			}
			docGrabberMonitorService.save(docGrabberMonitor);
						
		}catch(Exception e){
			log.error("Unexpected error occurred while handling the DocGrabber acquisitionRecord" + acquisitionRecordStr, e);
			throw e;
		}finally{
			IOUtils.closeQuietly(acquisitionRecordStream);
		}
	}

	private DocGrabberMonitor populateDocGrabberMonitor(AcquisitionRecord acquisitionRecord ){
		
		//Get PUBLISHING_CONTROL_DOC_GRABBER pause flage status
		PublishingControlDocGrabber publishingControlDocGrabber = docGrabberMonitorService.retrieveDocGrabberPublishingControls();
		
		DocGrabberMonitor docGrabberMonitor = new DocGrabberMonitor();
		docGrabberMonitor.setReceiptId(acquisitionRecord.getAcquisitionReceiptId());
		
		List<AcquisitionDocket> acquiredDocketsList = acquisitionRecord.getAcquiredDocketsList();
		if(acquiredDocketsList  != null && acquiredDocketsList.size() > 0 ){
			AcquisitionDocket acquisitionDocket = acquiredDocketsList.get(0);
			docGrabberMonitor.setDocketNumber(acquisitionDocket.getDocketNumber().toUpperCase());
			docGrabberMonitor.setPdfFileName(acquisitionDocket.getPdfFileName());
			docGrabberMonitor.setScrapeDate(acquisitionRecord.getScriptEndDate()); // publish date can be read from activity set confirmation log.
			
		}
		docGrabberMonitor.setMergeFileName(acquisitionRecord.getMergedFileName());
		if (StringUtils.isNotEmpty(acquisitionRecord.getWestlawClusterName())) { 
			Court court = docketService.findCourtByCourtCluster(acquisitionRecord.getWestlawClusterName().toUpperCase());
			docGrabberMonitor.setProduct(court.getProduct());
			docGrabberMonitor.setCourt(court);
		} else {
			Product product = new Product(ProductEnum.STATE);
			docGrabberMonitor.setProduct(product);
		}

		Status status;
		if (publishingControlDocGrabber.getPauseFlag().equals("Y")) {
			status = new Status(7l); // paused status.
		} else {
			status = new Status(2l); // running status.
		}
		docGrabberMonitor.setRequestStatus(status); 
		
	return docGrabberMonitor;			
	}

	@Override
	public void processFailedDocketRecord(String acquisitionRecordStr) throws Exception
	{
		InputStream acquisitionRecordStream = null;
		AcquisitionLogDocketRecord acquisitionLogDocketRecord = null;
		FailedDocketAcquisitionRecordHandler handler = new FailedDocketAcquisitionRecordHandler();
		try{
			acquisitionRecordStream = IOUtils.toInputStream(acquisitionRecordStr);
			acquisitionRecordParser.parse(acquisitionRecordStream, handler);
			acquisitionLogDocketRecord = handler.getAcquisitionRecord();
			createAndPersistFailedDocket(acquisitionLogDocketRecord);
		}catch(Exception e){
			log.error("Unexpected error occurred while handling the acquisitionRecord" + acquisitionRecordStr, e);
			
			UUID publishingRequestKey = UUIDGenerator.createUuid();
			PublishingRequest publishingRequest = buildErrorNotificationPublishingRequest(acquisitionLogDocketRecord,publishingRequestKey);
			String mergeFileName = acquisitionLogDocketRecord.getMergedFileName();
			String errorMessage = StringUtils.abbreviate(e.toString(), 300);
			String receiptId = acquisitionLogDocketRecord.getAcquisitionReceiptId();

			notificationService.createNotificationForAcquisitionError(mergeFileName,publishingRequest, StatusEnum.FAILED,errorMessage,receiptId); 

			throw e;
		}finally{
			IOUtils.closeQuietly(acquisitionRecordStream);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.trgr.dockets.core.acquisition.service.AcquisitionRecordAndActivitySetService#processActivitySetRecord(java.lang.String)
	 */
	public void processActivitySetRecord(String activitySetRecord) throws Exception {
		InputStream activitySetRecordStream = null;
		ActivitySetHandler activitySetHandler = new ActivitySetHandler();
		try{
			activitySetRecordStream = IOUtils.toInputStream(activitySetRecord);
			acquisitionRecordParser.parse(activitySetRecordStream, activitySetHandler);
			ActivitySet activitySet = activitySetHandler.getActivitySet();
			createProcessMonitor(activitySet);
		}catch(Exception e){
			log.error("Unexpected error occurred while handling the activitySetRecord" + activitySetRecord, e);
			throw e;
		}finally{
			IOUtils.closeQuietly(activitySetRecordStream);
		}
	}
	
	public void processDocGrabberActivitySetRecord(String activitySetRecord) throws Exception {

		InputStream activitySetRecordStream = null;
		ActivitySetHandler activitySetHandler = new ActivitySetHandler();
		try{
			activitySetRecordStream = IOUtils.toInputStream(activitySetRecord);
			acquisitionRecordParser.parse(activitySetRecordStream, activitySetHandler);
			ActivitySet activitySet = activitySetHandler.getActivitySet();
			
			DocGrabberMonitor docGrabberMonitor = docGrabberMonitorService.findDocGrabberMonitorByReceiptId(activitySet.getReceiptId());

			if(docGrabberMonitor == null){
				log.error("Received DocGrabberMonitor Acrivity record for docket without prior acquisition message i.e record missing in DB with receipt id: " +activitySet.getReceiptId());
			}else {
				Status status;
				if (activitySet.getStatus().toLowerCase().contains("success")) {
					status = new Status(1l); //Completed Successfully
				} else {
					status = new Status(4l); //Completed with errors
				}
				docGrabberMonitor.setRequestStatus(status); 
				docGrabberMonitor.setPublishDate(activitySet.getStartTime());
				docGrabberMonitorService.update(docGrabberMonitor);
			}

			
		}catch(Exception e){
			log.error("Unexpected error occurred while handling the activitySetRecord" + activitySetRecord, e);
			throw e;
		}finally{
			IOUtils.closeQuietly(activitySetRecordStream);
		}
	
	}

	
	
	/**
	 * Create rows in PUBLISHING_REQUEST and BATCH tables that reflect the batch.
	 * Also creates a batch monitor and process records.
	 * 
	 * 
	 * @param acquisitionRecord
	 */
	private AcquisitionResponse createBKRBatchAndMonitor(List<AcquisitionRecord> acquisitionRecordList, RequestTypeEnum requestTypeEnum, WorkflowTypeEnum workflowTypeEnum, WorkflowInformation workflowInformation, PublishingRequest... publishingRequests)
	{
		log.info("inside createBKRBatchAndMonitor" );
		log.info("acquisitionRecordHeader:"+acquisitionRecordList.get(0));
		AcquisitionRecord acquisitionRecordHeader = acquisitionRecordList.get(0);
		log.info("Cluster Name:"+acquisitionRecordHeader.getWestlawClusterName().toUpperCase());
		Court court = docketService.findCourtByCourtCluster(acquisitionRecordHeader.getWestlawClusterName().toUpperCase());
		PublishingRequest publishingRequest = null;

		//PBUI should create PublishingRequest object and it should be passed to this method for PBUI request.
		if(publishingRequests != null && publishingRequests.length > 0)
		{
			publishingRequest = publishingRequests[0];
		}
		else
		{
			//create publishing requests if it is not passed.
			UUID publishingRequestKey = UUIDGenerator.createUuid();
			String requestName = buildRequestName(acquisitionRecordHeader);
			RequestInitiatorTypeEnum initiatorType = RequestInitiatorTypeEnum.findByCode(acquisitionRecordHeader.getRetrievalType().toUpperCase());
			publishingRequest = new PublishingRequest(
					publishingRequestKey, requestName,
					acquisitionRecordHeader.getAcquisitionSourceName(), false, false, false,
					acquisitionRecordHeader.getScriptStartDate(), null, null,
					null,
					initiatorType, court.getProduct(),
					workflowTypeEnum, requestTypeEnum,
					new Status(StatusEnum.RUNNING), null,null, Long.parseLong(acquisitionRecordHeader.getAcquisitionReceiptId()), acquisitionRecordHeader.getProvider(), 0L, false, null, null, null,null);
			docketService.save(publishingRequest);
		}

		
		//create batch
		Batch requestBatch = new Batch(publishingRequest.getRequestId(), null, publishingRequest);
		docketService.save(requestBatch);
		Batch batch = new Batch(getFormattedBatchId(publishingRequest.getRequestId()), requestBatch, publishingRequest);
		docketService.save(batch);

		Set<String> legacyIdSet = new HashSet<String>();
		
		for(AcquisitionRecord acquisitionRecord : acquisitionRecordList)
		{
			court = docketService.findCourtByCourtCluster(acquisitionRecord.getWestlawClusterName().toUpperCase());
			//Group acquired and delete dockets and persist it. 
			List<AcquisitionDocket> allDockets = new ArrayList<AcquisitionDocket>();
			allDockets.addAll(acquisitionRecord.getAcquiredDocketsList());
			allDockets.addAll(acquisitionRecord.getDeletedDocketsList());
			
			Iterator<AcquisitionDocket> allDocketsIterator = allDockets.iterator();
			while(allDocketsIterator.hasNext())
			{
				AcquisitionDocket acquisitionDocket = allDocketsIterator.next();
				String legacyId = null;
				List<DocketEntity> docketsWithLocationCode = new LinkedList<DocketEntity>();
				if(acquisitionDocket.isDeleteOperation()&&!acquisitionDocket.getDocketNumber().contains(":"))
				{
					//court id, sequence number and filing year
					docketsWithLocationCode = docketService.findDocketsByCourtIdFilingYearSequenceNumber(court.getPrimaryKey(), Long.parseLong(acquisitionDocket.getFilingYear()), acquisitionDocket.getSequenceNumber());
					log.info("docketsWithLocationCode:"+docketsWithLocationCode);
					if(null == docketsWithLocationCode || docketsWithLocationCode.size()==0)
					{
						if(acquisitionDocket.getSequenceNumber().length()==6 && acquisitionDocket.getSequenceNumber().startsWith("0"))
						{
							String sequenceNumber = acquisitionDocket.getSequenceNumber().substring(1);
							docketsWithLocationCode = docketService.findDocketsByCourtIdFilingYearSequenceNumber(court.getPrimaryKey(), Long.parseLong(acquisitionDocket.getFilingYear()),sequenceNumber);
						}
					}
				}
				else
				{
					
					log.info("inside else part");
					log.info("CourtNorm:"+court.getCourtNorm());
					legacyId = DocketsCoreUtility.prepareBkrLegacyID(acquisitionDocket.getDocketNumber(), court.getCourtNorm());
					
					log.info("Legacy Id:"+legacyId);
				}

				if(null!=docketsWithLocationCode && docketsWithLocationCode.size()>0)
				{
					for(DocketEntity docketEntity:docketsWithLocationCode)
					{
						if(!legacyIdSet.contains(docketEntity.getPrimaryKey()))
						{
							BatchDocketKey batchDocketKey = new BatchDocketKey(docketEntity.getPrimaryKey(), publishingRequest.getPrimaryKey());
							BatchDocket batchDocket = new BatchDocket(batchDocketKey, batch, null, null);
							batchDocket.setOperationType("D");
							docketService.save(batchDocket);
						}
					}
				}
				
				//String legacyId = getBKRLegacyId(court.getCourtNorm(), acquisitionDocket.getFilingLocation(), acquisitionDocket.getFilingYear(), acquisitionDocket.getCaseType(), acquisitionDocket.getFileName());
				if(null!=legacyId && !legacyIdSet.contains(legacyId))
				{
					log.info("legacy id not null:"+legacyId);
					legacyIdSet.add(legacyId);
					//save bacth docket
					BatchDocketKey batchDocketKey = new BatchDocketKey(legacyId, publishingRequest.getPrimaryKey());
					BatchDocket batchDocket = new BatchDocket(batchDocketKey, batch, null, null);
					if(acquisitionDocket.isDeleteOperation())
					{
						batchDocket.setOperationType("D");
					}
					docketService.save(batchDocket);
				}
			}
		}
		//save batch monitor 
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(publishingRequest.getPrimaryKey(), batch.getPrimaryKey());
		BatchMonitor batchMonitor = new BatchMonitor(batchMonitorKey);
		batchMonitor.setStatus(StatusEnum.RUNNING);
		batchMonitor.setDocsInCount(Long.valueOf(legacyIdSet.size()));
		batchMonitor.setWorkDirectory(new File(workflowInformation.getWorkFolder()));
		if(null!=workflowInformation.getNovusFolder())
		{
			batchMonitor.setNovusLoadDirectory(new File(workflowInformation.getNovusFolder()));
		}
		batchMonitor.setErrorDirectory(new File(workflowInformation.getErrorFolder()));
		batchMonitor.setProcessDirectory(new File(workflowInformation.getProcessFolder()));
		batchMonitor.setServerHostname(workflowInformation.getAppServerName());
		docketService.save(batchMonitor);
		
		Process dcProcess = processAdapter.startProcess(ProcessTypeEnum.DC, batchMonitorKey, acquisitionRecordHeader.getScriptStartDate());
		processAdapter.endProcess(dcProcess, StatusEnum.COMPLETED_SUCCESSFULLY, null, acquisitionRecordHeader.getScriptEndDate());
		
		return new AcquisitionResponse(publishingRequest.getRequestId().toString(), batch.getPrimaryKey());
	}
	
	
	/**
	 * Create rows in PUBLISHING_REQUEST and BATCH tables that reflect the batch.
	 * Also creates a batch monitor and process records.
	 * 
	 * 
	 * @param acquisitionRecord
	 * @throws IOException 
	 * @throws DocketsPersistenceException 
	 */
	private AcquisitionResponse createBatchAndMonitor(AcquisitionRecord acquisitionRecord) throws Exception
	{
		AcquisitionDocket acquisitionDocket = null;
			
		//create publishing request
		UUID publishingRequestKey = UUIDGenerator.createUuid();
		log.debug("Attempting to process the acquisition message for request id" + publishingRequestKey.toString());
		String requestName = buildRequestName(acquisitionRecord);
		RequestInitiatorTypeEnum initiatorType = RequestInitiatorTypeEnum.findByCode(acquisitionRecord.getRetrievalType().toUpperCase());
		
		Court court = null;
		County county = null;
		
		if (acquisitionRecord.getCourtType().equalsIgnoreCase("DCT"))
		{
			court = docketService.findCourtByCourtCluster(acquisitionRecord.getWestlawClusterName().toUpperCase());
		} else { 
		
			String clusterName = acquisitionRecord.getWestlawClusterName();
			if (clusterName.equalsIgnoreCase("n_dgafultonst") || clusterName.equalsIgnoreCase("n_dgafultonsu"))
			{
				clusterName = "N_DGAFULTON";
			}
			else if(clusterName.equalsIgnoreCase("N_DGACOBBST") || clusterName.equalsIgnoreCase("N_DGACOBBSU"))	{
				clusterName = "N_DGACOBB";
			}
			county = countyService.findCountyByColKey(clusterName.toUpperCase());
			court = docketService.findCourtByPrimaryKey(county.getCourtId().longValue());
		}
		
		PublishingRequest publishingRequest = new PublishingRequest(
				publishingRequestKey, requestName,
				acquisitionRecord.getAcquisitionSourceName(), false, false, false,
				acquisitionRecord.getScriptStartDate(), acquisitionRecord.getScriptEndDate(), null,
				null,
				initiatorType, court.getProduct(),
				getWorkflowTypeEnumByProduct(court.getProduct()), RequestTypeEnum.WCW,
				new Status(StatusEnum.RUNNING), court,null, null, null, getPredocketVendorByProduct(court.getProduct()), false, null, null, null,null);
		docketService.save(publishingRequest);
		
		DocketsStepLogger preDocketLogger = createPreDocketStepLogger(court, getWorkflowTypeEnumByProduct(court.getProduct()));
		
		//create a uber batch with batch id as request id
		Batch parentBatch = new Batch(publishingRequestKey.toString(), null, null);
		docketService.save(parentBatch);
		
		//create batch
		UUID batchUuid = UUIDGenerator.createUuid();
		Batch batch = new Batch(getFormattedBatchId(batchUuid.toString()), parentBatch, publishingRequest);
		docketService.save(batch);
		
		//save batch monitor 
		BatchMonitorKey batchMonitorKey = new BatchMonitorKey(publishingRequest.getPrimaryKey(), batch.getPrimaryKey());
		BatchMonitor batchMonitor = new BatchMonitor(batchMonitorKey);
		batchMonitor.setStatus(StatusEnum.RUNNING);
		batchMonitor.setDocsInCount(Long.valueOf(1)); //assuming 1 always
		docketService.save(batchMonitor);
		
		//start and end process monitor
		Process dcProcess = processAdapter.startProcess(ProcessTypeEnum.DC, batchMonitorKey, acquisitionRecord.getScriptStartDate());
		processAdapter.endProcess(dcProcess, StatusEnum.COMPLETED_SUCCESSFULLY, null, acquisitionRecord.getScriptEndDate());
				
		//save acquisition lookup
		AcquisitionLookup acquisitionLookup = new AcquisitionLookup(publishingRequestKey, acquisitionRecord.getAcquisitionReceiptId());
		docketService.save(acquisitionLookup);
		
		if(acquisitionRecord.getAcquiredDocketsList().size() > 0 || acquisitionRecord.getDeletedDocketsList().size() > 0)
		{
			if(acquisitionRecord.getAcquiredDocketsList().size() > 0)
			{
				acquisitionDocket = acquisitionRecord.getAcquiredDocketsList().get(0);
			}
			else
			{
				acquisitionDocket = acquisitionRecord.getDeletedDocketsList().get(0);
			}


			// if downstate then get county using filename
			if(null != court.getCourtEnum() && court.getCourtCluster().equals("N_DNYDOWNSTATE"))
			{
				acquisitionDocket.hydrateNYAcquisitionDocketFromFileName();
				String countyName = acquisitionDocket.getSubDivision();
				if(countyName.equalsIgnoreCase("NEWYORK"))
				{
					countyName = "NEW YORK";
				}
				county = countyService.findCountyByNameAndCourt(countyName, court.getPrimaryKey().intValue());
			}
			
			File preDocketContentFile = null;
			try {
				if (county != null) {
					preDocketContentFile = getPreDocketContentFile(acquisitionDocket.getFileName(), getColKeyExpanded(county.getName(), acquisitionRecord.getWestlawClusterName()), court);
				} else {
					preDocketContentFile = getPreDocketContentFile(acquisitionDocket.getFileName(), acquisitionRecord.getWestlawClusterName().toUpperCase(), court);					
				}
			}
			catch (Exception e){
				log.warn("Error getting pre-docket content file: " + e.getMessage());
				batchMonitor.setErrorDirectory(preDocketLogger.getErrorDir());
				batchMonitor.setStatus(StatusEnum.FAILED);
				publishingRequest.setPublishingStatus(new Status(StatusEnum.FAILED));
				preDocketLogger.error("Error unzipping or retrieving pre-docket content file");
				
				docketService.update(batchMonitor);
				docketService.update(publishingRequest);			
				throw new Exception ("Error processing or retrieving pre-docket content file");
			}
			
			SourceDocketMetadata sourceDocketMetadata = createSourceDocketMetadata(publishingRequest, acquisitionDocket, preDocketContentFile, acquisitionRecord, court, county);

			preDocketContentService.persistPreDocket(sourceDocketMetadata);
			FileUtils.deleteQuietly(preDocketContentFile);


			
			//save bacth docket
			BatchDocketKey batchDocketKey = new BatchDocketKey(sourceDocketMetadata.getLegacyId(), publishingRequestKey);
			BatchDocket batchDocket = new BatchDocket(batchDocketKey, batch, null, null);
			if(acquisitionDocket.isDeleteOperation())
			{
				batchDocket.setOperationType("D");
			}
			docketService.save(batchDocket);
		}
		
		log.debug("Completed processing the acquisition message for request id" + publishingRequestKey.toString());
		return new AcquisitionResponse(publishingRequestKey.toString(), batch.getPrimaryKey());
	}


	/**
	 * Create rows in skipped dockets dummy record and persist in Docket,docketVersion,history table.
	 * No batch monitor or publishing request records are created. 
	 * 
	 * @param acquisitionLogDocketRecord
	 * @throws IOException 
	 * @throws DocketsPersistenceException 
	 */
	private void createAndPersistFailedDocket(AcquisitionLogDocketRecord acquisitionLogDocketRecord) throws Exception
	{
		
		FailedAcquisitionDocket webErrorFailedAcquisitionDocket = null;
		//create publishing request
		
		log.debug("Attempting to process the skipped acquisition message for receipt id: " + acquisitionLogDocketRecord.getAcquisitionReceiptId());
		for (AcquisitionLogCourtRecord acquisitionLogCourtRecord : acquisitionLogDocketRecord.getAcquisitionLogCourtRecordList())
		{
			Court court = null;
			court = docketService.findCourtByCourtCluster(acquisitionLogCourtRecord.getWestlawClusterName().toUpperCase());
			County county = countyService.findCountyByCourtId(court.getPrimaryKey().intValue());
		
			DocketsStepLogger skippedDocketLogger = createSkippedDocketStepLogger(court, getWorkflowTypeEnumByProduct(court.getProduct()));
			List<FailedAcquisitionDocket> failedDocketList = acquisitionLogCourtRecord.getFailedDocketsList();
			
			for (FailedAcquisitionDocket failedDocket : failedDocketList) 
			{
				
				if(county == null && failedDocket.getSubDivision() != null){
					county = countyService.findCountyByName(failedDocket.getSubDivision());
				}
	
				// if downstate then get county using filename 
				if(null != court.getCourtEnum() && acquisitionLogCourtRecord.getWestlawClusterName().equalsIgnoreCase("N_DNYDOWNSTATE"))
				{
					failedDocket.hydrateNYAcquisitionDocketFromFileName();
					String countyName = StringUtils.splitByCharacterType(failedDocket.getFileName())[0];
					if(countyName.equalsIgnoreCase("NEWYORK"))
					{
						countyName = "NEW YORK";
					}
	
					county = countyService.findCountyByNameAndCourt(countyName, court.getPrimaryKey().intValue());
				}
	
				/**If source meta data doesn`t contain docket number indicating error scenario where state.docket element was not part of the Web.errors.docket. 
					do not persist that docket in docket and docketVersion table but persist email notification record in DB. **/ 
				if(StringUtils.isNotBlank(failedDocket.getDocketNumber()) && !failedDocket.getDocketNumber().equalsIgnoreCase("null"))
				{
					SourceDocketMetadata sourceDocketMetadata = createFailedSourceDocketMetadata(failedDocket, acquisitionLogDocketRecord, court, county);
					sourceDocketMetadata.setPublishFlag("N");
					sourceDocketMetadata.setVendorId(court.getScrapeVendorId().intValue());
					failedAcquisitionDocketContentService.persistFailedDocket(sourceDocketMetadata);
				}else{
					webErrorFailedAcquisitionDocket  = failedDocket;
				}
			
			}
	
			if(webErrorFailedAcquisitionDocket  != null ){
				StatusEnum status  = StatusEnum.FAILED;
				AcquisitionErrorUtility.formatAndSendReport(court, status, webErrorFailedAcquisitionDocket.getCourtErrorMessage());
				
			}
			skippedDocketLogger.log("Completed processing the acquisition message for court " + court.getCourtNorm());
		}
		log.debug("Completed processing the acquisition message for receipt id" + acquisitionLogDocketRecord.getAcquisitionReceiptId());
	}
	
	private PublishingRequest buildErrorNotificationPublishingRequest(AcquisitionLogDocketRecord acquisitionLogDocketRecord,UUID publishingRequestKey){
		PublishingRequest publishingRequest = null; 

		
		Court court = null;
		
		try {
			court = docketService.findCourtByCourtCluster(acquisitionLogDocketRecord.getAcquisitionLogCourtRecordList().get(0).getWestlawClusterName().toUpperCase());
		} catch (Exception e) {
			log.debug("Exception while building court out of acquisitionLogDocketRecord :"+e);

		}
	
		publishingRequest = new PublishingRequest(
				publishingRequestKey, null,
				(acquisitionLogDocketRecord !=null ? acquisitionLogDocketRecord.getAcquisitionSourceName():null),
				false, false, false,
				(acquisitionLogDocketRecord != null ? acquisitionLogDocketRecord.getScriptStartDate():null),
				(acquisitionLogDocketRecord != null ? acquisitionLogDocketRecord.getScriptEndDate():null),
				null,
				null,
				null, (court !=null ? court.getProduct():null),
				null, RequestTypeEnum.ALL,
				new Status(StatusEnum.RUNNING), court,null, null, null, getPredocketVendorByProduct(court.getProduct()), false, null, null, null,null);

		
		return publishingRequest;
	}
	
	private SourceDocketMetadata createFailedSourceDocketMetadata(FailedAcquisitionDocket skippedDocket,  
			AcquisitionLogDocketRecord skippedDocketRecord, Court court, County county) throws Exception
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata();

		if (court.getProduct().getCode().equalsIgnoreCase("DCT"))
		{
			sourceDocketMetadata.setDocketNumber(DctDocketNumberUtility.normalizeDocketNumber(skippedDocket.getDocketNumber()));
		} 
		else 
		{
			sourceDocketMetadata.setDocketNumber(skippedDocket.getDocketNumber());
		}
		sourceDocketMetadata.setCourt(court);
		sourceDocketMetadata.setProduct(court.getProduct());
		if(!StringUtils.isEmpty(skippedDocket.getFilingYear())){
			sourceDocketMetadata.setYearFiled(Integer.valueOf(skippedDocket.getFilingYear()));
		}
		if(skippedDocket.getFilingLocation() != null){
			sourceDocketMetadata.setLocationCode(skippedDocket.getFilingLocation());
		}
		if(skippedDocket.getSequenceNumber() != null){
			sourceDocketMetadata.setSequenceNumber(skippedDocket.getSequenceNumber());
		}
		if(county != null){
		sourceDocketMetadata.setCountyId(Long.valueOf(county.getCountyId()));
		sourceDocketMetadata.setCountyName(county.getName());
		}
		/**
		 * AQ scripts are putting values not existing in our database like CC,CF,CM 
		 */
		sourceDocketMetadata.setCaseTypeId(0l);
		sourceDocketMetadata.setPublishFlag("N");
		
		sourceDocketMetadata.setStatus(skippedDocket.getStatus());

		//sourceDocketMetadata.setDocketContentFile(skippedDocketContentFile); 
		sourceDocketMetadata.setAcquisitionTimestamp(skippedDocketRecord.getScriptStartDate()); 
		
		/**	Just look for retrieve.type and if it’s full_file then it would be Retro, and if one of the others then it would be Prospective? **/
		if(skippedDocketRecord.getRetrievalType().equalsIgnoreCase("full_file")){
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.RETROSPECTIVE);
		}else{
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		}
		//sourceDocketMetadata.setSourceFile(new File(acquisitionRecord.getMergedFileName()));
		sourceDocketMetadata.setDeleteOperation(false);
		
		sourceDocketMetadata.setLegacyId(DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sourceDocketMetadata, court.getCourtCluster()));
		return sourceDocketMetadata;
	}

	
	private SourceDocketMetadata createSourceDocketMetadata(PublishingRequest publishingRequest, AcquisitionDocket acquisitionDocket, File preDocketContentFile, 
			AcquisitionRecord acquisitionRecord, Court court, County county)
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setDocketNumber(acquisitionDocket.getDocketNumber());
		sourceDocketMetadata.setCourt(court);
		if(acquisitionDocket.getFilingYear() != null){
			sourceDocketMetadata.setYearFiled(Integer.valueOf(acquisitionDocket.getFilingYear()));
		}
		if(acquisitionDocket.getFilingLocation() != null){
			sourceDocketMetadata.setLocationCode(acquisitionDocket.getFilingLocation());
		}
		if(acquisitionDocket.getSequenceNumber() != null){
			sourceDocketMetadata.setSequenceNumber(acquisitionDocket.getSequenceNumber());
		}
		if (county != null) {
			sourceDocketMetadata.setCountyId(Long.valueOf(county.getCountyId()));
			sourceDocketMetadata.setCountyName(county.getName());
		}
		if(acquisitionDocket.getCaseType() != null){
			CaseTypeEnum caseTypeEnum = CaseTypeEnum.findByCode(acquisitionDocket.getCaseType());
			if(caseTypeEnum != null){
				sourceDocketMetadata.setCaseTypeId(caseTypeEnum.getKey());
			}else{
				//default to UNKNOWN
				sourceDocketMetadata.setCaseTypeId(CaseTypeEnum.UNKNOWN.getKey());
			}
		}
		if(acquisitionDocket.getSubDivision() != null){
			sourceDocketMetadata.setMiscellaneous(acquisitionDocket.getSubDivision());
		}
		sourceDocketMetadata.setPublishFlag("Y");
		sourceDocketMetadata.setVendorId(getPredocketVendorByProduct(court.getProduct()).intValue());

		hydrateSourceDocketMetadataWithPreDocketSource(preDocketContentFile, sourceDocketMetadata, court.getProduct());
		sourceDocketMetadata.setDocketContentFile(preDocketContentFile); 
		sourceDocketMetadata.setAcquisitionTimestamp(acquisitionRecord.getScriptStartDate()); 
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.SMARTTEMPLATE);
		sourceDocketMetadata.setSourceFile(new File(acquisitionRecord.getMergedFileName()));
		if(acquisitionRecord.getAcquiredDocketsList().size() <= 0)
		{
			sourceDocketMetadata.setDeleteOperation(true);
		}
		sourceDocketMetadata.setLegacyId(DocketServiceUtil.convertDktNumberToLegacyIdByCourt(sourceDocketMetadata, court.getCourtCluster()));

		return sourceDocketMetadata;
	}

	private void createProcessMonitor(ActivitySet activitySet) throws Exception
	{
		//check if a acquisition record exists, otherwise ignore this activity record
		AcquisitionLookup acquisitionLookup;
		acquisitionLookup = docketService.findAcquisitionLookupByReceiptId(activitySet.getReceiptId());
		if(acquisitionLookup == null){
			for(int i =0; i < 10; i++){
				Thread.sleep(3000); //wait 3 seconds; total wait is 30 seconds
				acquisitionLookup = docketService.findAcquisitionLookupByReceiptId(activitySet.getReceiptId());
				if(acquisitionLookup != null){
					break;
				}
			}
		}
		if(acquisitionLookup != null){
			UUID publishingRequestUuid = acquisitionLookup.getPrimaryKey();
			log.debug("Attempting to update the pre docket data for publishing request " + publishingRequestUuid.toString() + " with the activity info");
			List<BatchMonitor> batchMonitorList = docketService.findBatchMonitorByRequestId(publishingRequestUuid);
			if(batchMonitorList.size() > 0){
				BatchMonitor batchMonitor = batchMonitorList.get(0);
				//start and end activity set process monitor
				Process activitySetProcess = processAdapter.startProcess(ProcessTypeEnum.ACTIVITY_SET, batchMonitor.getPrimaryKey(), activitySet.getStartTime());
				processAdapter.endProcess(activitySetProcess, StatusEnum.COMPLETED_SUCCESSFULLY, null, activitySet.getEndTime());
				//get the legacy id from Batch_Docket table using the request id
				
				//why ScrollableResults is not working???
				/*ScrollableResults batchDocketKeysScrollableResults = docketService.findBatchDocketKeys(batchMonitorList.get(0).getPrimaryKey().getBatchId());
				batchDocketKeysScrollableResults.next()
				BatchDocketKey batchDocketKey = (BatchDocketKey) batchDocketKeysScrollableResults.get(0);*/
				
				List<BatchDocketKey> batchDocketKeyList = docketService.findBatchDocketKeysList(batchMonitor.getPrimaryKey().getBatchId());
				BatchDocketKey batchDocketKey = (BatchDocketKey) batchDocketKeyList.get(0);
				String legacyId = batchDocketKey.getLegacyId();
				log.info("legacyId:"+legacyId);
				PublishingRequest publishingRequest = docketService.findPublishingRequestByPrimaryKey(publishingRequestUuid);
				//call the service to update the novus docket version and insert docket history tree
				preDocketContentService.updatePreDocket(legacyId, activitySet, publishingRequest);
				publishingRequest.setPublishingStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
				docketService.update(publishingRequest);
				batchMonitor.setStatus(StatusEnum.COMPLETED_SUCCESSFULLY);
				docketService.update(batchMonitor);
				log.debug("Completed updating the pre docket data for publishing request " + publishingRequestUuid.toString() + " with the activity info");
			}
		}else{
			log.warn("Activity set record for receipt " + activitySet.getReceiptId() + " came before the acquisition recod. Ignoring it.");
		}
	}
	
	/**
	 * @param fileName
	 * @param court
	 * @return
	 * @throws Exception
	 */
	private File getPreDocketContentFile(String fileName, String collectionKey, Court court) throws Exception {
		File unzipPreDocketFile = null;
		 //UAT:  /ct-data/prod/dockets/source/statedocket/pre-dockets_lower_env/UAT/$courtname/datacap_pre_new
		 //TEST:  /ct-data/prod/dockets/source/statedocket/pre-dockets_lower_env/TEST/$courtname/datacap_pre_new 
		String preDocketFileOnNasPath =  determinePredocketPathBasedonEnvironment(fileName, collectionKey.toLowerCase(), court);
		
		File predocketFile = new File(preDocketFileOnNasPath);
		
		
		if(predocketFile.exists()){
			try{
				InputStream predocketFileGzipInputStream = FileUtils.openInputStream(predocketFile);
				String unzipRootWorkDirectory = rootWorkDirectory + court.getProduct().getCode() + "/" + "predocket" + "/";
				File unzipRootWorkDirectoryFile = new File(unzipRootWorkDirectory);
				if(!unzipRootWorkDirectoryFile.exists()){
					unzipRootWorkDirectoryFile.mkdirs();
				}
				String unzipPreDocketFilePath = unzipRootWorkDirectory + fileName;
				unzipPreDocketFile = new File(unzipPreDocketFilePath);
				DocketFileUtils.writeGzipInputStreamToFile(predocketFileGzipInputStream, unzipPreDocketFile);
			}catch(IOException io){
					throw new Exception("Unexpected error occurred while unzipping the pre docket file " + predocketFile.getPath());
			}
		}
		else {
			throw new Exception("Could not find predocket file for this environment!");
		}
				

		
		return unzipPreDocketFile;
	}
	
	private String determinePredocketPathBasedonEnvironment (String fileName, String collectionKey, Court court){
		
		//TODO - Should move this functionality to database
		if (court.getProduct().getCode().equals("DCT")) {
			return preDocketRootDirectoryFederal + collectionKey + "/" + "datacap_pre_new" + "/" + fileName + AcquisitionConstants.GZIP_EXTENSION;
		} else {
			return preDocketRootDirectoryState + collectionKey + "/" + "datacap_pre_new" + "/" + fileName + AcquisitionConstants.GZIP_EXTENSION;			
		}
	}
	
	/**
	 * Populates the source docket metadata object with the information from pre docket source file.
	 * 
	 * @param preDocketContentFile
	 * @param sourceDocketMetadata
	 * @param product
	 */
	private void hydrateSourceDocketMetadataWithPreDocketSource(File preDocketContentFile,
			SourceDocketMetadata sourceDocketMetadata, Product product)
	{
		InputStream preDocketSourceInputStream = null; 
		try{
			preDocketSourceInputStream = FileUtils.openInputStream(preDocketContentFile);
			PreDocketSourceHandler preDocketSourceHandler = new PreDocketSourceHandler();
			acquisitionRecordParser.parse(preDocketSourceInputStream, preDocketSourceHandler);
			PreDocket preDocket = preDocketSourceHandler.getPreDocket();
			if(preDocket != null){
				if(preDocket.getCaseTitle() != null){
					sourceDocketMetadata.setTitle(preDocket.getCaseTitle());
				}
				if(preDocket.getCaseSubType() != null){
					CaseSubType caseSubType = docketService.findCaseSubTypeByCaseSubTypeAndProduct(preDocket.getCaseSubType(), product);
					if(caseSubType != null){
						sourceDocketMetadata.setCaseSubTypeId(caseSubType.getCaseSubTypeId());
					}
				}
				if(preDocket.getFilingDate() != null){
					sourceDocketMetadata.setFilingDate(preDocket.getFilingDate());
				}
				if(preDocket.getCaseType() != null){
					CaseTypeEnum caseTypeEnum = CaseTypeEnum.findByCode(preDocket.getCaseType());
					if(caseTypeEnum != null){
						sourceDocketMetadata.setCaseTypeId(caseTypeEnum.getKey());
					}else{
						//default to UNKNOWN
						sourceDocketMetadata.setCaseTypeId(CaseTypeEnum.UNKNOWN.getKey());
					}
				}
			}
		}catch(Exception e){
			log.error("Unexpected error occurred while handling the pre docket source " + preDocketContentFile.getPath(), e);
		}finally{
			IOUtils.closeQuietly(preDocketSourceInputStream);
		}
		
	}
	
	private String buildRequestName(AcquisitionRecord acquisitionRecord)
	{
		String requestName = acquisitionRecord.getMergedFileName().trim() + "." + getScriptStartDateFormatted(acquisitionRecord.getScriptStartDate());
		return requestName;
	}


	private DocketsStepLogger createPreDocketStepLogger(Court court, WorkflowTypeEnum workflow){
		String dirPrefix = rootWorkDirectory + "/" + court.getProduct().getCode() + "/";
		String dirPostfix = getCurrentDateInMMDDYYYY() + "/" + workflow.getCode() + "/" + getCurrentTimeInHhMmSs();
		String logDir = dirPrefix + "logs" + "/" + dirPostfix;
		String errorDir = dirPrefix + "errors" + "/" + dirPostfix;
		File logDirFile = new File(logDir);
		File errorDirFile = new File(errorDir);
		logDirFile.mkdirs();
		errorDirFile.mkdirs();
		DocketsStepLogger ppLogger = new DocketsStepLogger(logDirFile, errorDirFile, AcquisitionConstants.PREDOCKET_LOG_FILE, AcquisitionConstants.PREDOCKET_ERROR_FILE);
		return ppLogger;	
	}
	
	private DocketsStepLogger createSkippedDocketStepLogger(Court court, WorkflowTypeEnum workflow){
		String dirPrefix = rootWorkDirectory + "/" + court.getProduct().getCode() + "/";
		String dirPostfix = getCurrentDateInMMDDYYYY() + "/" + workflow.getCode() + "/" + getCurrentTimeInHhMmSs();
		String logDir = dirPrefix + "logs" + "/" + dirPostfix;
		String errorDir = dirPrefix + "errors" + "/" + dirPostfix;
		File logDirFile = new File(logDir);
		File errorDirFile = new File(errorDir);
		logDirFile.mkdirs();
		errorDirFile.mkdirs();
		DocketsStepLogger ppLogger = new DocketsStepLogger(logDirFile, errorDirFile, AcquisitionConstants.SKIPPED_DOCKET_LOG_FILE, AcquisitionConstants.SKIPPED_DOCKET_ERROR_FILE);
		return ppLogger;	
	}

	
	private static String getCurrentDateInMMDDYYYY()
	{
		return getCurrentDateInMmDdYyyyHhMmSs().substring(0, 8);

	}
	
	private static String getCurrentTimeInHhMmSs()
	{
		return getCurrentDateInMmDdYyyyHhMmSs().substring(8, 14);
	}	
	
	private static String getCurrentDateInMmDdYyyyHhMmSs()
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmmss");
		return sdf.format(cal.getTime());
	}
	
	/**
	 * @param countyName
	 * @return
	 * @throws Exception
	 */
	private String getColKeyExpanded(String countyName, String clusterName) throws Exception{
		String colKey = null;
		if(countyName.equalsIgnoreCase("KINGS")){
			colKey = "n_dnykings";
		}else if(countyName.equalsIgnoreCase("QUEENS")){
			colKey = "n_dnyqueens";
		}else if(countyName.equalsIgnoreCase("RICHMOND")){
			colKey = "n_dnyrichmond";
		}else if(countyName.equalsIgnoreCase("NEW YORK")){
			colKey = "n_dnymanhattan";
		}else if(countyName.equalsIgnoreCase("BRONX")){
			colKey = "n_dnybronx";
		// else use cluster name
		}else if (null != clusterName){
			colKey = clusterName.toUpperCase();
		}else {
			throw new Exception("Unexpected county name received while finding the expanded col key " + countyName);
		}
		return colKey;
	}
	

	private String getScriptStartDateFormatted(Date scriptStartDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyy.HHmmss");
		//TODO: The following line is included to test the build getting success or failures.
		sdf.setTimeZone(TimeZone.getTimeZone("CST"));
		return sdf.format(scriptStartDate);
	}

	/**
	 * Appends -B0001 to the batch uuid.
	 * 
	 * @param batchId
	 * @return
	 */
	private String getFormattedBatchId(String batchId){
		return batchId + "-B0001";
	}
	
	public void setDocketService(DocketService docketService)
	{
		this.docketService = docketService;
	}

	public void setProcessAdapter(ProcessAdapter processAdapter)
	{
		this.processAdapter = processAdapter;
	}

	public void setCountyService(CountyService countyService)
	{
		this.countyService = countyService;
	}

	public void setSendEmailFlag(boolean sendEmailFlag)
	{
		this.sendEmailFlag = sendEmailFlag;
	}

	public void setPreDocketContentService(PreDocketContentService preDocketContentService)
	{
		this.preDocketContentService = preDocketContentService;
	}

	public void setRootWorkDirectory(String rootWorkDirectory)
	{
		this.rootWorkDirectory = rootWorkDirectory;
	}

	public void setPreDocketRootDirectoryState(String preDocketRootDirectoryState)
	{
		this.preDocketRootDirectoryState = preDocketRootDirectoryState;
	}

	public void setPreDocketRootDirectoryFederal(String preDocketRootDirectoryFederal)
	{
		this.preDocketRootDirectoryFederal = preDocketRootDirectoryFederal;
	}
	
	public void setFailedAcquisitionDocketContentService(FailedAcquisitionDocketContentService failedAcquisitionDocketContentService) {
		this.failedAcquisitionDocketContentService = failedAcquisitionDocketContentService;
	}

	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	private WorkflowTypeEnum getWorkflowTypeEnumByProduct(Product product) {
		if (product.getCode().equals("DCT")) {
			return WorkflowTypeEnum.DCT_PREDOCKET;
		} else {
			return WorkflowTypeEnum.STATEDOCKET_PREDOCKET;
		}
	}

	private Long getPredocketVendorByProduct(Product product) {
		if (product.getCode().equals("DCT")) {
			return 48l;
		} else {
			return 10l;
		}
	}

	public void setDocGrabberMonitorService(DocGrabberMonitorService docGrabberMonitorService) {
		this.docGrabberMonitorService = docGrabberMonitorService;
	}

}
