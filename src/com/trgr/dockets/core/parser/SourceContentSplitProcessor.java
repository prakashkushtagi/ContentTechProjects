/**
 * Copyright 2014: Thomson Reuters Global Resources. All Rights Reserved.
 *
 * Proprietary and Confidential information of TRGR.
 * Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.DroppedDocket;
import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.exception.ContentParserException;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.processor.Docket;
import com.trgr.dockets.core.processor.DocketBuilder;
import com.trgr.dockets.core.processor.LoadUsingWrapperApi;
import com.trgr.dockets.core.processor.SourceLoadRequest;
import com.trgr.dockets.core.service.EventLogService;
import com.trgr.dockets.core.util.DocketsCoreUtility;

public class SourceContentSplitProcessor 
{
	private static Logger log = Logger.getLogger(SourceContentSplitProcessor.class);

	private EventLogService eventLogService; 

	private BankruptcyContentService bankruptcyContentService;
	
	private HashMap<String,String> courtMap;

	private String wrapperEnvironment;
	
	private List<DroppedDocket> failedDockets;
	
	public SourceContentSplitProcessor()
	{
		
	}
	public SourceContentSplitProcessor(EventLogService eventLogService,BankruptcyContentService bankruptcyContentService, HashMap<String,String> courtMap,String wrapperEnvironment) 
	{
		this.eventLogService = eventLogService;
		this.bankruptcyContentService = bankruptcyContentService;
		this.courtMap = courtMap;
		this.wrapperEnvironment = wrapperEnvironment;
	}
	
	public void process(SourceLoadRequest sourceLoadRequest) throws SourceContentLoaderException
	{
		String errorDescription = null;
		List<DroppedDocket> failedDockets = new ArrayList<DroppedDocket>();
		String fileLocationForWrapperCall = null;
		
		//dcr.start event
		if (sourceLoadRequest.getProductCode().equalsIgnoreCase("FBR")) 
		{
			persistDcrStartEvent(sourceLoadRequest);
		}
		try
		{
			//parse and Split
			if (log.isDebugEnabled())
			{
				log.debug("Default Source parse and split method invoked.");
			}
			long timeToSplit = System.currentTimeMillis();
			fileLocationForWrapperCall = processSplit(sourceLoadRequest);
			if (log.isDebugEnabled())
			{
				log.debug("Time to split a document :: " + (System.currentTimeMillis() - timeToSplit));
			}
			//Call wrapper Api
			if (log.isDebugEnabled())
			{
				log.debug(" Before Wrapper API is call invoked.");
			}
			long timeToLoadWrapperApi = System.currentTimeMillis();
			
			failedDockets.addAll(bankruptcyServiceCall(sourceLoadRequest,fileLocationForWrapperCall));

			if (log.isDebugEnabled())
			{
				log.debug("Time to load a merge file :: " + (System.currentTimeMillis() - timeToLoadWrapperApi));
			}
		}
		catch (SourceContentLoaderException scle) 
		{
			errorDescription = scle.getMessage();
			throw scle;
		} 
		catch (ContentParserException cpe) 
		{
			errorDescription = cpe.getMessage();
			throw new SourceContentLoaderException("Error parsing the merge file ",cpe);
		} 
		catch (BankruptcyContentServiceException bcse)
		{
			errorDescription = bcse.getMessage();
			throw new SourceContentLoaderException("Error in calling the wrapper api ",bcse);
		}
		finally
		{
			
			if(sourceLoadRequest.getProductCode().equalsIgnoreCase("FBR"))
			{
				persistDcrEndAndAnyFailures(sourceLoadRequest,failedDockets, errorDescription);
			}
		}
	}
	
	
	/**
	 * @param sourceLoadRequest
	 * @param failedDockets
	 */
	private void persistDcrEndAndAnyFailures(SourceLoadRequest sourceLoadRequest,List<DroppedDocket> failedDockets, String errorDescription)
	{
		InetAddress localHost = null;
		try 
		{
			localHost = InetAddress.getLocalHost();
		} 
		catch (UnknownHostException e1) 
		{

		}
		String machineName = localHost.getHostName();
		
		try 
		{
			String errorCode = null;
			int docsFailedCount = 0;
			String failedDocketsXmlString = null;
			if(null!= failedDockets && failedDockets.size()>0 && errorDescription==null)
			{
				docsFailedCount = failedDockets.size();
				errorCode = "SCL_100";
				errorDescription = "Number of dockets failed to process "+ failedDockets.size() +" dockets.";
				failedDocketsXmlString = DocketsCoreUtility.createDroppedDocketXml(failedDockets);
				setFailedDocketsForJpml(failedDockets);
			}
			else if(errorDescription!=null)
			{
				errorCode = "SCL_99";
			}
			
			eventLogService.logEventMessage("dockets.dcrsourceload.end",sourceLoadRequest.getBatchId().toString(), 
					machineName, docsFailedCount , sourceLoadRequest.getSourcePath(), null, errorCode, errorDescription, failedDocketsXmlString, null);
		}
		catch (Exception e) 
		{
			log.error("SCL could log an event Message \"dockets.dcrsourceload.end\" for batchId " + sourceLoadRequest.getBatchId().toString() + " and merge file name " +
					sourceLoadRequest.getSourcePath(), e);
		}
	}

	/**
	 * 
	 * @param errorDocList
	 * @return
	 */
	private String getFailedDocketsNumbers(List<DroppedDocket> errorDocList)
	{
		String failedDocNumberString = "";
		if (errorDocList != null && errorDocList.size()> 0) 
		{
			for (DroppedDocket docket : errorDocList) 
			{
				if (failedDocNumberString == null) 
				{
					failedDocNumberString = '(' + docket.getLegacyId();
				} 
				else 
				{
					failedDocNumberString += "," + docket.getLegacyId();
				}
			}
			failedDocNumberString += ')';
		}
		return failedDocNumberString;
	}
	
	
	
	/**
	 * @param sourceLoadRequest
	 * @param fileLocationForWrapperCallAndPlatform
	 * @throws BankruptcyContentServiceException 
	 * @throws SourceContentLoaderException
	 */
	private List<DroppedDocket> bankruptcyServiceCall(SourceLoadRequest sourceLoadRequest,String fileLocationForWrapperCall) 
																		throws BankruptcyContentServiceException, SourceContentLoaderException  
	{
		List<DroppedDocket> failedDockets = null;
		LoadUsingWrapperApi loadUsingWrapperApi = new LoadUsingWrapperApi(bankruptcyContentService);
		loadUsingWrapperApi.setCourtMap(courtMap);
		loadUsingWrapperApi.setEnvironment(wrapperEnvironment);
		failedDockets = loadUsingWrapperApi.loadBulkContent(sourceLoadRequest,fileLocationForWrapperCall);
		return failedDockets;
	}

	/**
	 * @param sourceLoadRequest
	 * @param requestId
	 */
	private void persistDcrStartEvent(SourceLoadRequest sourceLoadRequest) {
		InetAddress localHost = null;
		try 
		{
			localHost = InetAddress.getLocalHost();
		} 
		catch (UnknownHostException e1) 
		{

		}
		String machineName = localHost.getHostName();
		String path = sourceLoadRequest.getSourcePath();
		Long requestId = sourceLoadRequest.getRequestId();
		
		if (null != requestId && requestId > 0) 
		{
			
			/** send event log message only for bankruptcy FBR **/
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
	
	public String processSplit(SourceLoadRequest sourceLoadRequest) throws ContentParserException 
	{
		String filePath= null;
		boolean split = true;
		String errorDescription = null;
		
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

			if(contentFileLocation.trim().endsWith(".gz"))
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
				filePath = splitContentFile(sourceLoadRequest, contentInputStream,split);
				try 
				{
					gZipInputStream.close();
					fileOutputStream.flush();
					fileOutputStream.close();
				} 
				catch (IOException e) 
				{
					log.warn("Unable to close the gzipInputStream opened for processing");
				}
			}
			else if(contentFileLocation.trim().endsWith(".nxo"))
			{
				String newFilePath;
				FileOutputStream fileOutputStream;
				ZipInputStream zipInputStream = null;
				String writeFilesToDirectory = null;
				boolean workFolderExists = false;
				if(sourceLoadRequest.getWorkFolder()==null ||sourceLoadRequest.getWorkFolder().isEmpty() )
				{
					writeFilesToDirectory = contentFileLocation.substring(0,contentFileLocation.lastIndexOf(".nxo")) + "/" + "unzip/";
				}
				else
				{
					writeFilesToDirectory = sourceLoadRequest.getWorkFolder() + "unzip/";
					workFolderExists = true;
				}
				File createDirectoryStructure = new File(writeFilesToDirectory);
				createDirectoryStructure.mkdirs();
				ZipEntry zipEntry = null;
				byte[] buffer = new byte[1024];
				zipInputStream = new ZipInputStream(new FileInputStream(contentFileLocation));
				zipEntry = zipInputStream.getNextEntry();
				int n = 0;

				if (zipEntry != null)
				{
					String entryName = zipEntry.getName();
					File newFile = new File(writeFilesToDirectory + entryName);
					fileOutputStream = new FileOutputStream(newFile);
	
					while ((n = zipInputStream.read(buffer, 0, 1024)) > -1)
					{
						fileOutputStream.write(buffer, 0, n);
					}
					try 
					{
						fileOutputStream.close();
						zipInputStream.close();
					} 
					catch (IOException e) 
					{
						log.warn("Unable to close the gzipInputStream opened for processing");
					}
					newFilePath = writeFilesToDirectory + newFile.getName();
//					sourceLoadRequest.setSourcePath(newFilePath);
//					contentInputStream = new FileInputStream(newFilePath);
//					filePath = splitNovusContentFile(sourceLoadRequest,contentInputStream,split);
					SourceContentVtdParser vtdParser = new SourceContentVtdParser();
					String destinationFilePath = null;
					if(!workFolderExists)
					{
						destinationFilePath = newFilePath.substring(0,newFilePath.lastIndexOf(".xml")) + "/";
					}
					else
					{
						destinationFilePath = sourceLoadRequest.getWorkFolder() + "source/";
					}
					File newFilePathStructure = new File(destinationFilePath);
					newFilePathStructure.mkdirs();
					vtdParser.process(newFilePath,destinationFilePath,CoreConstants.NOVUS_N_DOCUMENT_ELEMENT);
					filePath = destinationFilePath;
				}
			}
			else
			{
				contentInputStream = new FileInputStream(contentFileLocation);
				filePath = splitContentFile(sourceLoadRequest,contentInputStream,split);
			}
			
			try
			{
				if(null!=contentInputStream)
				{
					contentInputStream.close();
				}
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
			throw new ContentParserException("XMLStreamException occurred in DocumentParser exception has been thrown: ", e);
			

		}
		catch (FileNotFoundException fnfe)
		{
			log.error("Exception thrown in " + getClass().getName(), fnfe);
			errorDescription = fnfe.getMessage();
			throw new ContentParserException("FileNotFoundException occurred in DocumentParser exception has been thrown: ", fnfe);

		}
		catch (UnsupportedEncodingException uee)
		{
			log.error("Exception thrown in " + getClass().getName(), uee);
			errorDescription = uee.getMessage();
			throw new ContentParserException("UnsupportedEncodingException occurred in DocumentParser exception has been thrown: " , uee);
		} 
		catch (IOException ioe) 
		{
			log.error("Exception thrown in " + getClass().getName(), ioe);
			errorDescription = ioe.getMessage();
			throw new ContentParserException("Unable to process file with gz file extensions :: " + sourceLoadRequest.getSourcePath(), ioe);
		}
		
		return filePath;
	}
	
	public String prepareDocketNumber(String unFormattedDocketNumber) 
	{
		String docketNumber = null;
		if(unFormattedDocketNumber.contains(":"))
		{
			String str[]=unFormattedDocketNumber.split("\\:");
			int year=Integer.parseInt(str[1].substring(0,2));
			if (year>50)
			{
				year= year +1900;
			}
			else
			{
				year =year+2000;
			}		

			docketNumber = str[0].replaceAll("[A-Z]+-[A-Z]+","")+":"+year+str[1].replaceAll("^\\d{2}", "");

	        String strDocketNumberPart[]=docketNumber.split("[aA-zZ]");

	        int index =strDocketNumberPart[0].indexOf(":");
	        int locationCode = Integer.parseInt((strDocketNumberPart[0].substring(0, index)));
			String docketNumberPart2 = strDocketNumberPart[2];
		}
		else{
			String str[]=unFormattedDocketNumber.split("[Bb][Kk][Rr]");
			int year=Integer.parseInt(str[1].substring(0,2));
			if (year>50)
			{
				year= year +1900;
			}
			else
			{
				year =year+2000;
			}
			docketNumber =year+str[1].replaceAll("^\\d{2}", "");
			String strDocketNumberPart[]=docketNumber.split("[aA-zZ]");
			String docketNumberPart2 = strDocketNumberPart[2];
			int locationCode = -100;
		}

		return docketNumber;
	}
	private String splitContentFile(SourceLoadRequest sourceLoadRequest, InputStream contentInputStream, boolean split)
			throws XMLStreamException, FactoryConfigurationError, IOException
	{
		if(failedDockets==null)
		{
			failedDockets = new ArrayList<DroppedDocket>();
		}
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEvent event = null;
		DocketBuilder docketBuilder = null;
		XMLEventReader eventReader;
		eventReader = inputFactory.createXMLEventReader(new InputStreamReader(contentInputStream));
		String contentFileLocation = null;
		boolean docketElementStarted = false;
		int fileSequence = 0;
		//parse through the entire load file.
		while (eventReader.hasNext())
		{
			event = eventReader.nextEvent();
			if (event.isStartElement())
			{
				//document start
				if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.DOCKET_HEADER_ELEMENT))
				{
					if(sourceLoadRequest.getWorkFolder()==null)
					{
						contentFileLocation = sourceLoadRequest.getSourcePath();
						if(contentFileLocation.contains(".xml"))
						{
							contentFileLocation = contentFileLocation.substring(0,contentFileLocation.lastIndexOf(".xml")) + "/";
						}
						else if(contentFileLocation.contains(".gz"))
						{
							contentFileLocation = contentFileLocation.substring(0,contentFileLocation.lastIndexOf(".gz")) + "/";
						}
					}
					else
					{
						contentFileLocation = sourceLoadRequest.getWorkFolder() + "source/";
					}
					File createDirectoryStructure = new File(contentFileLocation);
					createDirectoryStructure.mkdirs();
				}
				if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.DOCKET_ELEMENT))
				{
					docketBuilder = new DocketBuilder();
					docketBuilder.setSplitDocument(split);
					docketBuilder.setCourtMap(courtMap);
					docketBuilder.setEnvironment(wrapperEnvironment);
					docketBuilder.setSourcePath(contentFileLocation);
					docketBuilder.setProductCode(sourceLoadRequest.getProductCode());
					docketBuilder.setFileSequence(++fileSequence);
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
				else if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.DOCKET_ELEMENT))
				{
					if(docketBuilder.getDocket().isDeleteFlag() && (docketBuilder.getDocket().getPlatform().toUpperCase().startsWith("LCI")||docketBuilder.getDocket().isNoLocationCode()))
					{
						try
						{
							fileSequence = docketBuilder.processSplitDeleteDocket(event,sourceLoadRequest,bankruptcyContentService);
						} 
						catch (SourceContentLoaderException e) 
						{
							if(e.getMessage().contains("Delete Dockets Failed"))
							{
								docketBuilder.getDocket().setErrorMessage(e.getMessage());
								failedDockets.add(convertToDroppedDocket(docketBuilder.getDocket()));
							}
							else
							{
								docketBuilder.getDocket().setErrorMessage("Docket failed: Not a valid scrape Date");
								failedDockets.add(convertToDroppedDocket(docketBuilder.getDocket()));
							}
						} 
						catch (BankruptcyContentServiceException e) 
						{
							docketBuilder.getDocket().setErrorMessage("Docket Delete failed: Cannot get dockets by court,year and sequenceNumber");
							failedDockets.add(convertToDroppedDocket(docketBuilder.getDocket()));
						}
						docketElementStarted = false;
					}
					else
					{
						docketBuilder.writeDocket(event);
						docketElementStarted = false;
					}
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
					failedDockets.add(convertToDroppedDocket(docketBuilder.getDocket()));
				}
			}
		}
		
		eventReader= null;
		inputFactory = null;
		return contentFileLocation;
	}
	
	private DroppedDocket convertToDroppedDocket(Docket docket) 
	{
		DroppedDocket droppedDocket = new DroppedDocket(docket.getCourt().toUpperCase(),docket.getNumber(), docket.getNumber(),"",  docket.getErrorMessage());
		return droppedDocket;
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
	public List<DroppedDocket> getFailedDockets() {
		return failedDockets;
	}
	/**
	 * @param failedDocketsForJpml the failedDocketsForJpml to set
	 */
	public void setFailedDocketsForJpml(List<DroppedDocket> failedDockets) {
		this.failedDockets = failedDockets;
	}
	/**
	 * @return the courtMap
	 */
	public HashMap<String, String> getCourtMap() 
	{
		return courtMap;
	}
	/**
	 * @param courtMap the courtMap to set
	 */
	public void setCourtMap(HashMap<String, String> courtMap)
	{
		this.courtMap = courtMap;
	}


}
