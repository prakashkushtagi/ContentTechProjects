package com.trgr.dockets.core.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.processor.Docket;
import com.trgr.dockets.core.processor.DocketBuilder;
import com.trgr.dockets.core.processor.SourceLoadRequest;
import com.trgr.dockets.core.service.ErrorLogService;
import com.trgr.dockets.core.service.EventLogService;
import com.trgr.dockets.core.service.SourceLoadRequestService;
import com.trgr.dockets.core.util.DocketsCoreUtility;

public class SourceContentProcessor 
{
	private static Logger log = Logger.getLogger(SourceContentProcessor.class);

	private ErrorLogService errorLogService;

	private EventLogService eventLogService; 

	private SourceLoadRequestService sourceLoadRequestService;

	private BankruptcyContentService bankruptcyContentService;
	
	private HashMap<String,String> courtMap;

	private String wrapperEnvironment;
	
	private List<Docket> failedDocketsForJpml;
	
	public SourceContentProcessor()
	{
		
	}
	public SourceContentProcessor(ErrorLogService errorLogService,EventLogService eventLogService,SourceLoadRequestService sourceLoadRequestService,BankruptcyContentService bankruptcyContentService, HashMap<String,String> courtMap,String wrapperEnvironment) 
	{
		this.errorLogService = errorLogService;
		this.eventLogService = eventLogService;
		this.sourceLoadRequestService = sourceLoadRequestService;
		this.bankruptcyContentService = bankruptcyContentService;
		this.courtMap = courtMap;
		this.wrapperEnvironment = wrapperEnvironment;
	}
	/**
	 * 
	 * @param sourceLoadRequest
	 * @throws SourceContentLoaderException
	 */
	public void process(SourceLoadRequest sourceLoadRequest) throws SourceContentLoaderException 
	{
		List<Docket> failedDocketList = null;
		InetAddress localHost = null;
		String errorDescription = null;
		try 
		{
			localHost = InetAddress.getLocalHost();
		} 
		catch (UnknownHostException e1) 
		{

		}
		String machineName = localHost.getHostName();
		com.trgr.dockets.core.entity.SourceLoadRequest sourceLoadRequestDbEntity = hydrateSourceRequestEntityObj(sourceLoadRequest) ;
		Long requestId =0L;
		try 
		{
			requestId = sourceLoadRequestService.addSourceLoadRequest(sourceLoadRequestDbEntity);
		} 
		catch (SourceContentLoaderException e1) 
		{
			log.error("Unable to save the incoming SCL RequestMessage " + sourceLoadRequestDbEntity.toString());
		}
		String path = sourceLoadRequest.getSourcePath();

			if (null != requestId && requestId > 0) 
			{
				
				/** send event log message only for bankruptcy FBR **/
				if (sourceLoadRequest.getProductCode().equalsIgnoreCase("FBR")) 
				{
					try 
					{
						eventLogService.logEventMessage("dockets.dcrsourceload.start",
															sourceLoadRequest.getBatchId().toString(),
															machineName, 0, 
															path, null, null, null, null, null);
					} 
					catch (Exception e) 
					{
						log.error("SCL could log an event Message \"dockets.dcrsourceload.start\" for batchId " + sourceLoadRequest.getBatchId().toString() + " and merge file name " +
									sourceLoadRequest.getSourcePath(), e);
					}

				}
			}
		
		if (log.isDebugEnabled())
		{
			log.debug("Default Source parse method invoked.");
		}

		InputStream contentInputStream = null;
		
		try
		{
			String contentFileLocation = sourceLoadRequest.getSourcePath();
			//throw exception when the contentFileLocation is null
			if (StringUtils.isEmpty(contentFileLocation))
			{
				throw new FileNotFoundException("Content Input File location value is empty for processing ");
			}
			if(!contentFileLocation.trim().endsWith(".gz"))
			{
				contentInputStream = new FileInputStream(contentFileLocation);
				failedDocketList = processContentFile(sourceLoadRequest, contentInputStream);
			}
			else
			{
					GZIPInputStream gZipInputStream;
					ByteArrayOutputStream fileOutputStream;
					gZipInputStream = new GZIPInputStream(new FileInputStream(contentFileLocation));
					fileOutputStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int n = 0;
					while ((n = gZipInputStream.read(buffer, 0, 1024)) > -1) 
					{
						fileOutputStream.write(buffer, 0, n);
					}
					contentInputStream = new ByteArrayInputStream(fileOutputStream.toByteArray());
					failedDocketList = processContentFile(sourceLoadRequest, contentInputStream);
					try 
					{
						gZipInputStream.close();
						fileOutputStream.close();
					} 
					catch (IOException e) 
					{
						log.warn("Unable to close the gzipInputStream opened for processing");
					}
			}
			
			try
			{
				contentInputStream.close();
			}
			catch (IOException e) 
			{
				log.warn("Unable to close the contentInputStream opened for processing");
			}

		}
		catch (XMLStreamException e)
		{
			log.error( "XML parsing error has occurred in " + getClass().getName(), e);
			errorDescription = e.getMessage();
			throw new SourceContentLoaderException("XMLStreamException occurred in DocumentParser exception has been thrown: "
			        + e.getMessage(), e);
			

		}
		catch (FileNotFoundException fnfe)
		{
			log.error("Exception thrown in " + getClass().getName(), fnfe);
			errorDescription = fnfe.getMessage();
			throw new SourceContentLoaderException("FileNotFoundException occurred in DocumentParser exception has been thrown: " + fnfe.getMessage(), fnfe);

		}
		catch (UnsupportedEncodingException uee)
		{
			log.error("Exception thrown in " + getClass().getName(), uee);
			errorDescription = uee.getMessage();
			throw new SourceContentLoaderException("UnsupportedEncodingException occurred in DocumentParser exception has been thrown: " + uee.getMessage(), uee);
		} 
		catch (IOException ioe) 
		{
			log.error("Exception thrown in " + getClass().getName(), ioe);
			errorDescription = ioe.getMessage();
			throw new SourceContentLoaderException("Unable to process file with gz file extensions :: " + sourceLoadRequest.getSourcePath(), ioe);
		}
		
		finally 
		{
			if(sourceLoadRequest.getProductCode().equalsIgnoreCase("FBR"))
			{
				try 
				{
					String errorCode = null;
					int docsFailedCount = 0;
					
					if(null!= failedDocketList && failedDocketList.size()>0)
					{
						docsFailedCount = failedDocketList.size();
						errorCode = "SCL_100";
						errorDescription = "Number of dockets failed to process "+ failedDocketList.size() +" dockets."+getFailedDocketsNumbers(failedDocketList);
						setFailedDocketsForJpml(failedDocketList);
					}
					else if(null == failedDocketList && errorDescription!=null)
					{
						errorCode = "SCL_99";
					}
					
					eventLogService.logEventMessage("dockets.dcrsourceload.end", 
							sourceLoadRequest.getBatchId().toString(), 
							machineName, docsFailedCount , sourceLoadRequest.getSourcePath(), null, errorCode, errorDescription, null, null);
				}
				catch (Exception e) 
				{
					log.error("SCL could log an event Message \"dockets.dcrsourceload.end\" for batchId " + sourceLoadRequest.getBatchId().toString() + " and merge file name " +
							sourceLoadRequest.getSourcePath(), e);
				}
			}
		} 
		return;

	}
	
	/**
	 * 
	 * @param errorDocList
	 * @return
	 */
	private String getFailedDocketsNumbers(List<Docket> errorDocList){
		String failedDocNumberString = "";
		if (errorDocList != null && errorDocList.size()> 0) {
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

	/**
	 * @param sourceLoadRequest
	 * @param docketElementStarted
	 * @param inputFactory
	 * @param contentInputStream
	 * @param docketBuilder
	 * @throws XMLStreamException
	 * @throws IOException 
	 * @throws FactoryConfigurationError 
	 */
	private List<Docket> processContentFile(SourceLoadRequest sourceLoadRequest, InputStream contentInputStream)
			throws XMLStreamException, FactoryConfigurationError, IOException 
	{
		
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEvent event = null;
		DocketBuilder docketBuilder = null;
		XMLEventReader eventReader;
		eventReader = inputFactory.createXMLEventReader(new InputStreamReader(contentInputStream, "UTF-8"));
		List<Docket> failedDocketsList = new ArrayList<Docket>();
		 boolean docketElementStarted = false;
		//parse through the entire load file.
		while (eventReader.hasNext())
		{
			event = eventReader.nextEvent();


			if (event.isStartElement())
			{
				//document start
				if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.DOCKET_ELEMENT))
				{
					docketBuilder = new DocketBuilder();
					docketBuilder.setSplitDocument(false);
					docketBuilder.setCourtMap(courtMap);
					docketBuilder.setEnvironment(wrapperEnvironment);
					docketElementStarted = true;
				}

			}
			if (event.isEndElement())
			{
				//end document
				if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.DOCKET_HEADER_ELEMENT))
				{
					break;
				}
				else if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.DOCKET_ELEMENT) && docketElementStarted)
				{
					try 
					{
						docketBuilder.processDocket(event,sourceLoadRequest,getBankruptcyContentService());
					} 
					catch (BankruptcyContentServiceException e) 
					{
						failedDocketsList.add(docketBuilder.getDocket());
					} 
					catch (SourceContentLoaderException e) 
					{
						failedDocketsList.add(docketBuilder.getDocket());
					}
					docketElementStarted = false;
				}
			}
			if(docketElementStarted)
			{
				try 
				{
					docketBuilder.processEvent(event);
				} 
				catch (SourceContentLoaderException e) 
				{
					docketElementStarted = false;
					docketBuilder.getDocket().setErrorMessage("Docket failed: Not a valide scrape Date");
					failedDocketsList.add(docketBuilder.getDocket());
				}
			}
		}
		
		eventReader= null;
		inputFactory = null;
		return failedDocketsList;
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
	 * @return the wrapperEnvironment
	 */
	public String getWrapperEnvironment() {
		return wrapperEnvironment;
	}
	/**
	 * @param wrapperEnvironment the wrapperEnvironment to set
	 */
	public void setWrapperEnvironment(String wrapperEnvironment) {
		this.wrapperEnvironment = wrapperEnvironment;
	}
	/**
	 * @return the failedDocketsForJpml
	 */
	public List<Docket> getFailedDocketsForJpml() {
		return failedDocketsForJpml;
	}
	/**
	 * @param failedDocketsForJpml the failedDocketsForJpml to set
	 */
	public void setFailedDocketsForJpml(List<Docket> failedDocketsForJpml) {
		this.failedDocketsForJpml = failedDocketsForJpml;
	}
}
