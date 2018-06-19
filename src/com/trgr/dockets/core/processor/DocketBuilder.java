package com.trgr.dockets.core.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.util.DocketsCoreUtility;

public class DocketBuilder 
{

	private String environment;
	private static Logger log = Logger.getLogger(DocketBuilder.class);
	private HashMap<String,String> courtMap;
	private boolean splitDocument;
	private String sourcePath;
	private XMLEventWriter writer;
	private StringWriter docketXmlStringWriter;
	private Docket docket;
	private long timeStat;
	private String docketLocation;
	private String productCode;
	private int fileSequence;
	
	public DocketBuilder()
	{
	}
	
	public void processEvent(XMLEvent event) throws XMLStreamException, FactoryConfigurationError, IOException, SourceContentLoaderException
	{
		if (event.isStartElement())
		{
			//document start
			if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.DOCKET_ELEMENT))
			{
				timeStat = System.currentTimeMillis();
				docket = new Docket();

				//getting all the attributes
				Attribute number = event.asStartElement().getAttributeByName(new QName(CoreConstants.DOCKET_NUMBER_ATTR));
				boolean noLocationCode = false;
				if(null!=number)
				{
					docket.setNumber(number.getValue());
					if(!docket.getNumber().contains(":"))
					{
						noLocationCode = true;
						docket.setNoLocationCode(true);
					}
					
				}
				Attribute scrapeDate = event.asStartElement().getAttributeByName(new QName(CoreConstants.DOCKET_SCRAPE_DATE_ATTR));
				if(null!=scrapeDate)
				{
					docket.setScrapeDate(scrapeDate.getValue());
				}
				else
				{
					docket.setScrapeDate("");
				}
				Attribute court = event.asStartElement().getAttributeByName(new QName(CoreConstants.DOCKET_COURT_ATTR));
				if(null!=court)
				{
					docket.setCourt(court.getValue());
				}
				Attribute platform = event.asStartElement().getAttributeByName(new QName(CoreConstants.DOCKET_PLATFORM_ATTR));
				if(null!=platform)
				{
					docket.setPlatform(platform.getValue());
				}
				Attribute status = event.asStartElement().getAttributeByName(new QName(CoreConstants.DOCKET_STATUS_ATTR));
				String updateOrDelete = "update";
				boolean deleteFlag = false;
				if(null!=status)
				{
					if(status.getValue().equalsIgnoreCase("deleted"))
					{
						updateOrDelete = "delete";
						deleteFlag = true;
					}
					docket.setDeleteFlag(deleteFlag);
				}
				
				if(splitDocument)
				{
					if(deleteFlag && (docket.getPlatform().toUpperCase().startsWith("LCI")||noLocationCode))
					{
						docketXmlStringWriter = new StringWriter();
						writer = XMLOutputFactory.newInstance().createXMLEventWriter(docketXmlStringWriter);
					}
					else
					{
						StringBuffer docketOutputFileLocation = new StringBuffer();
						docketOutputFileLocation.append(getSourcePath());
						String courtNorm = courtMap.get(docket.getCourt().toUpperCase());
						String scrapeTimestampString = "";

						Date scrapeTimestamp = DocketsCoreUtility.getFormatedTimeStamp(docket.getScrapeDate(),getProductCode());
						DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
						scrapeTimestampString = df.format(scrapeTimestamp);

						docketOutputFileLocation.append(""+this.fileSequence+"_"+courtNorm+"_" +docket.getNumber() + "_" + scrapeTimestampString + "_" + docket.getPlatform()+ "_"+updateOrDelete +".scl.split.xml");
						String docketOutputFilePath = docketOutputFileLocation.toString();
	
						if(System.getProperty("file.separator").endsWith("\\") && docket.getNumber().contains(":"))
						{
							docketOutputFilePath = docketOutputFilePath.replace(":", "%3A");
						}
						File contentFile = new File(docketOutputFilePath);
						writer = XMLOutputFactory.newInstance().createXMLEventWriter(new FileWriter(contentFile));
					}
				}
				else
				{
					docketXmlStringWriter = new StringWriter();
					writer = XMLOutputFactory.newInstance().createXMLEventWriter(docketXmlStringWriter);
				}
			}
		}
		writer.add(event);
	}

	
	public int processSplitDeleteDocket(XMLEvent event, SourceLoadRequest sourceLoadRequest, BankruptcyContentService bankruptcyContentService ) throws XMLStreamException, BankruptcyContentServiceException, SourceContentLoaderException
	{
		writer.add(event);
		writer.flush();
		String contentXml = docketXmlStringWriter.toString();
		
		try 
		{
			docketXmlStringWriter.close();
		}
		catch (IOException e) 
		{
			if(null!=docketXmlStringWriter)
			{
				//then do nothing
			}

		}
		writer.close();
		
		StringBuffer docketOutputFileLocation = new StringBuffer();
		docketOutputFileLocation.append(getSourcePath());
		String courtNorm = courtMap.get(docket.getCourt().toUpperCase());
		String fileNamePlaceHolder = "filelocationfordeleteddocketNumber";
		String fileSequenceHolder = "filesequenceholder";
		String scrapeTimestampString = "";

		Date scrapeTimestamp = DocketsCoreUtility.getFormatedTimeStamp(docket.getScrapeDate(),getProductCode());
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		scrapeTimestampString = df.format(scrapeTimestamp);

		docketOutputFileLocation.append(fileSequenceHolder+"_"+courtNorm+"_" +fileNamePlaceHolder + "_" + scrapeTimestampString + "_" + docket.getPlatform()+ "_"+"delete" +".scl.split.xml");
		String docketOutputFilePath = docketOutputFileLocation.toString();

		
		Map<String,String> docNumberMap = DocketsCoreUtility.docketNumberParsing(docket.getNumber(), sourceLoadRequest.getProductCode().toUpperCase());

		int year = Integer.parseInt(docNumberMap.get(DocketsCoreUtility.KeysEnum.year.toString()));
		String sequenceNumber =  docNumberMap.get(DocketsCoreUtility.KeysEnum.sequenceNo.toString());
		
		List<com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Docket>
		docktsList = bankruptcyContentService.getDocketsByCourtYearSequenceNumber(courtNorm,year,sequenceNumber,environment);
		StringBuffer errorMessageForDeleteDockets = new StringBuffer();

		if(null == docktsList || docktsList.size()==0)
		{
			if(sequenceNumber.length()==6 && sequenceNumber.startsWith("0"))
			{
				sequenceNumber = sequenceNumber.substring(1);
				docktsList = bankruptcyContentService.getDocketsByCourtYearSequenceNumber(courtNorm,year,sequenceNumber,environment);
			}

		}
		
		if (docktsList != null && docktsList.size()>0) 
		{
			for (com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Docket docketOtherVersions : docktsList)
			{
				String docketDeleteOutputFilePath = docketOutputFilePath.replace(fileNamePlaceHolder, docketOtherVersions.getDocketNumber());
				docketDeleteOutputFilePath = docketDeleteOutputFilePath.replace(fileSequenceHolder,""+this.fileSequence);
				if(System.getProperty("file.separator").endsWith("\\") && docketOtherVersions.getDocketNumber().contains(":"))
				{
					docketDeleteOutputFilePath = docketDeleteOutputFilePath.replace(":", "%3A");
				}
				FileWriter fw = null;
				try 
				{
					File contentFile = new File(docketDeleteOutputFilePath);
					fw = new FileWriter(contentFile);
					fw.write(contentXml);
					fw.flush();
				} 
				catch (IOException e) 
				{
					errorMessageForDeleteDockets.append(docketOtherVersions.getDocketNumber() +":");
				}

				try 
				{
					fw.close();
				}
				catch (IOException e) 
				{
					errorMessageForDeleteDockets.append(docketOtherVersions.getDocketNumber() +":");
				}
				this.fileSequence++;
			}
		}
		else
		{
			errorMessageForDeleteDockets.append( "No docket to delete for " +" : " + docket.getNumber().toUpperCase());
		}
		
		if(!errorMessageForDeleteDockets.toString().isEmpty())
		{
			throw new SourceContentLoaderException("Delete Dockets Failed for " + docket.getNumber().toUpperCase() + errorMessageForDeleteDockets);
		}
		return --this.fileSequence;
	}

	public void processDocket(XMLEvent event, SourceLoadRequest sourceLoadRequest, BankruptcyContentService bankruptcyContentService ) throws XMLStreamException, BankruptcyContentServiceException, SourceContentLoaderException 
	{
		writer.add(event);
		if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.DOCKET_ELEMENT))
		{
			writer.flush();
			docket.setDocketXmlString(docketXmlStringWriter.toString());
			timeStat = System.currentTimeMillis() - timeStat;
			log.debug("Time Elapsed in parsing a docket call :: " + timeStat);
			//call to wrapper API
			try 
			{
				long startTime = System.currentTimeMillis();
				LoadUsingWrapperApi loadUsingWrapperApi = new LoadUsingWrapperApi(bankruptcyContentService);
				loadUsingWrapperApi.setCourtMap(courtMap);
				loadUsingWrapperApi.setEnvironment(environment);
				loadUsingWrapperApi.loadContent(docket, sourceLoadRequest);
				long endTime = System.currentTimeMillis();
				log.debug("Time Elapsed in WAPI call :: " + (endTime - startTime));
			}
			catch (SourceContentLoaderException sce) 
			{
				log.error("Error processing the docket in the SourceControlLoader for Docket :: " + docket.getNumber(),sce);
				docket.setErrorMessage(sce.getMessage());
				throw sce;
			} catch (BankruptcyContentServiceException bce) 
			{
				log.error("Error processing the docket in the Wrapper API for Docket :: " + docket.getNumber(),bce);
				docket.setErrorMessage(bce.getMessage());
				throw bce;
			}
			finally
			{
				try 
				{
					docketXmlStringWriter.close();
				}
				catch (IOException e) 
				{
					if(null!=docketXmlStringWriter)
					{
						//then do nothing
					}

				}
				writer.close();
				if(docket.getErrorMessage()!=null && !docket.getErrorMessage().isEmpty())
				{
					docket.setDocketXmlString(null);
				}
				else
				{
					docket = null;
				}
			}
		}

	}


	public void writeDocket(XMLEvent event) throws XMLStreamException 
	{
		writer.add(event);

		if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.DOCKET_ELEMENT))
		{
			writer.flush();
			writer.close();
			writer = null;
			docket = null;
		}
		
	}
	
	
	/**
	 * @return the environment
	 */
	public String getEnvironment() 
	{
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) 
	{
		this.environment = environment;
	}

	/**
	 * @return the docket
	 */
	public Docket getDocket() 
	{
		return docket;
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

	/**
	 * @return the splitDocument
	 */
	public boolean isSplitDocument() {
		return splitDocument;
	}

	/**
	 * @param splitDocument the splitDocument to set
	 */
	public void setSplitDocument(boolean splitDocument) {
		this.splitDocument = splitDocument;
	}


	/**
	 * @return the sourcePath
	 */
	public String getSourcePath() {
		return sourcePath;
	}


	/**
	 * @param sourcePath the sourcePath to set
	 */
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}


	/**
	 * @return the docketLocation
	 */
	public String getDocketLocation() {
		return docketLocation;
	}


	/**
	 * @param docketLocation the docketLocation to set
	 */
	public void setDocketLocation(String docketLocation) {
		this.docketLocation = docketLocation;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode the productCode to set
	 */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the fileSequence
	 */
	public int getFileSequence() {
		return fileSequence;
	}

	/**
	 * @param fileSequence the fileSequence to set
	 */
	public void setFileSequence(int fileSequence) {
		this.fileSequence = fileSequence;
	}
}
