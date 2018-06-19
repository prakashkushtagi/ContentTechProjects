/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.Platform;
import com.thomson.judicial.dockets.bankruptcycontentservice.controller.BankruptcyContentService;
import com.thomson.judicial.dockets.bankruptcycontentservice.exception.BankruptcyContentServiceException;
import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.exception.SourceContentLoaderException;
import com.trgr.dockets.core.processor.Docket;
import com.trgr.dockets.core.processor.DocketBuilder;
import com.trgr.dockets.core.processor.LoadUsingWrapperApi;
import com.trgr.dockets.core.processor.SourceLoadRequest;
import com.trgr.dockets.core.util.DocketsCoreUtility;
import com.trgr.dockets.core.util.UUIDGenerator;

public class NovusDocketBuilder 
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
	private String tempDocketLocation;
	private String productCode;
	
	public NovusDocketBuilder()
	{
	}
	
	public void processEvent(XMLEvent event) throws XMLStreamException, FactoryConfigurationError, IOException
	{
		if (event.isStartElement())
		{
			//document start
			if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.NOVUS_N_DOCUMENT_ELEMENT))
			{
				Attribute guid = event.asStartElement().getAttributeByName(new QName(CoreConstants.NOVUS_DOCUMENT_GUID));
				String fileName = null;
				if(null!=guid)
				{
					fileName = guid.getValue();
				}
				else
				{
					fileName = UUIDGenerator.createUuid().toString();
				}
				timeStat = System.currentTimeMillis();
				docket = new Docket();
				if(splitDocument)
				{
					String tempFileUUID = getSourcePath() +fileName + ".tmp.xml";
					setTempDocketLocation(tempFileUUID);
					File contentFile = new File(tempFileUUID);
					writer = XMLOutputFactory.newInstance().createXMLEventWriter(new FileWriter(contentFile));
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


	public void processDocket(XMLEvent event, SourceLoadRequest sourceLoadRequest, BankruptcyContentService bankruptcyContentService ) throws XMLStreamException, BankruptcyContentServiceException, SourceContentLoaderException 
	{
		writer.add(event);
		if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.NOVUS_N_DOCUMENT_ELEMENT))
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

		if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.NOVUS_N_DOCUMENT_ELEMENT))
		{
			writer.flush();
			writer.close();
			renameTempNovusFile(getTempDocketLocation(),docket);
			writer = null;
		}
		
	}
	
	
	private void renameTempNovusFile(String tempDocketLocation, Docket docket) 
	{
		StringBuffer docketOutputFileLocation = new StringBuffer();
		String scrapeTimestampString = null;
		try 
		{
			Date scrapeTimestamp = DocketsCoreUtility.getFormatedTimeStamp(docket.getScrapeDate(),getProductCode());
			DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			scrapeTimestampString = df.format(scrapeTimestamp);
			
		} 
		catch (SourceContentLoaderException e) 
		{
			e.printStackTrace();
		}
		
		docketOutputFileLocation.append(getSourcePath() + docket.getCourt()+"_" +docket.getNumber() + "_" + scrapeTimestampString + "_" + Platform.NOVUSXML.toString()+".scl.split.xml");
	
		File tmpfile = new File(tempDocketLocation);
		File file = new File(docketOutputFileLocation.toString());
		// Rename file 
		boolean success = tmpfile.renameTo(file);
		if (!success) 
		{
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
	 * @return the tempDocketLocation
	 */
	private String getTempDocketLocation() {
		return tempDocketLocation;
	}

	/**
	 * @param tempDocketLocation the tempDocketLocation to set
	 */
	private void setTempDocketLocation(String tempDocketLocation) {
		this.tempDocketLocation = tempDocketLocation;
	}

}
