/* Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.exception.ContentParserException;

public class RpxConfigProcessor 
{
	private static Logger log = Logger.getLogger(RpxConfigProcessor.class);

	public RpxConfigProcessor()
	{
		
	}
	
	public boolean process(File rpxWorkflowConfigFile, File dynamicConfigFile, String releaseFlag) throws ContentParserException
	{
		boolean success = false;
		if (log.isDebugEnabled())
		{
			log.debug("Default Source parse method invoked.");
		}

		InputStream contentInputStream = null;
		
		try
		{
			//throw exception when the Rpx Config File is null
			if (null==rpxWorkflowConfigFile || null==dynamicConfigFile )
			{
				throw new FileNotFoundException("Rpx Config File location value is empty for processing ");
			}
			contentInputStream = new FileInputStream(rpxWorkflowConfigFile);
			success = readAndModifyConfigFile(contentInputStream, dynamicConfigFile,releaseFlag);
		}
		catch (XMLStreamException e)
		{
			log.error( "XML parsing error has occurred in " + getClass().getName(), e);
			throw new ContentParserException("XMLStreamException occurred in RpxConfigProcessor exception has been thrown: ", e);
		}
		catch (FileNotFoundException fnfe)
		{
			log.error("Exception thrown in " + getClass().getName(), fnfe);
			throw new ContentParserException("FileNotFoundException occurred in RpxConfigProcessor exception has been thrown: ", fnfe);

		}
		catch (UnsupportedEncodingException uee)
		{
			log.error("Exception thrown in " + getClass().getName(), uee);
			throw new ContentParserException("UnsupportedEncodingException occurred in RpxConfigProcessor exception has been thrown: " , uee);
		} 
		catch (IOException ioe) 
		{
			log.error("Exception thrown in " + getClass().getName(), ioe);
			throw new ContentParserException("Unable to process file :: ", ioe);
		}
		finally
		{
			if(null!=contentInputStream)
			{
				try 
				{
					contentInputStream.close();
				} 
				catch (IOException e) 
				{

				}
				contentInputStream = null;
			}
		}
		return success;
	}
	
	
	private boolean readAndModifyConfigFile(InputStream contentInputStream,File dynamicConfigFile, String releaseFlag)
			throws XMLStreamException, FactoryConfigurationError, IOException
	{
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEvent event = null;
		RpxConfigBuilder rpxConfigBuilder = null;
		XMLEventReader eventReader=null;
		XMLEventWriter writer = null;
		boolean workflowConfigDocument = false;

		try 
		{
			eventReader = inputFactory.createXMLEventReader(new InputStreamReader(contentInputStream));//parse through the entire load file.
			while (eventReader.hasNext())
			{
				event = eventReader.nextEvent();
				if (event.isStartElement())
				{
					//document start
					if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.WORKFLOW_CONFIG))
					{
						writer = XMLOutputFactory.newInstance().createXMLEventWriter(new FileWriter(dynamicConfigFile));
						rpxConfigBuilder = new RpxConfigBuilder(writer);
						workflowConfigDocument = true;
					}
				}
				else if (event.isEndElement())
				{
					//end document
					if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.DOCKET_HEADER_ELEMENT))
					{
						rpxConfigBuilder.writeConfig(event);
						workflowConfigDocument = false;
						break;
					}
				}
				if(workflowConfigDocument)
				{
					rpxConfigBuilder.processEvent(event, releaseFlag);
				}
			}
		} 
		finally 
		{
			if(null!=eventReader)
			{
				eventReader.close();
				eventReader= null;
			}
			if(null!=writer)
			{
				writer.flush();
				writer.close();
				writer =null;
			}
			inputFactory = null;
		}
		return true;
	}
	
}
