/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.parser;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.entity.LoadMonitorLtcMessage;
import com.trgr.dockets.core.exception.MessageParseException;

/**
 * LTCMessageParser.
 * This class parsers LTC jms Message into POJO message.	
 */
public class LtcLsvResponseParser 
{
	private static Logger logger = Logger.getLogger(LtcLsvResponseParser.class);
	
		
	/**
	 * @param inputStream
	 * @param ltcMessage
	 * @param ltcEvent
	 * @return
	 * @throws MessageParseException
	 * @throws ParseException 
	 */
	public LoadMonitorLtcMessage parse(InputStream inputStream) throws MessageParseException
	{
	     LoadMonitorLtcMessage ltcMessageStatus = null;
		/** iterate thru the inputStream using STAX to map appropriate elements to LTCMessage **/
         // First create a new XMLInputFactory
         XMLInputFactory inputFactory = XMLInputFactory.newInstance();
         // Setup a new eventReader
         try {
			XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
			 // Read the XML document
			 while (eventReader.hasNext()) 
			 {  
			    XMLEvent event = eventReader.nextEvent();  
			    
			    if (event.isStartElement()) 
			    {
			        if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LSV_LOAD_REQEUST)) 
			        {
			        	ltcMessageStatus = new LoadMonitorLtcMessage();
			        	Attribute id =event.asStartElement().getAttributeByName(new QName("id"));
			        	if(id!=null)
			        	{
			        		ltcMessageStatus.setLoadDatasetRequestId(id.getValue());
			        	}
			        	
			        }
			        if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LSV_DATASET)) 
			        {
			        	
			        	Attribute id =event.asStartElement().getAttributeByName(new QName("id"));
			        	if(id!=null)
			        	{
			        		ltcMessageStatus.setLoadDatasetId(id.getValue());
			        	}
			        	
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LSV_DATASET_NAME)) 
			        {
			        	if (eventReader.peek().isCharacters())
				           {
				        	   ltcMessageStatus.setLtcFilename(eventReader.nextEvent().asCharacters().getData());
				              logger.debug("LSV  Message LTC datasetname "+ ltcMessageStatus.getLtcFilename());
				           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LSV_DATASET_NAME)) 
			        {
			        	if (eventReader.peek().isCharacters())
			        	{
			        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			        		String requestDate = eventReader.nextEvent().asCharacters().getData();
			        		requestDate = requestDate.substring(0,requestDate.indexOf("."));
			        		try 
			        		{
								ltcMessageStatus.setLtcEventTimestamp(sdf.parse(requestDate));
							} 
			        		catch (ParseException e) 
			        		{
								
							}
			        		logger.debug("LSV  Message LTC datasetname "+ ltcMessageStatus.getLtcFilename());
			        	}
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LSV_LTC_STEP)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcMessageStatus.setLoadElement(eventReader.nextEvent().asCharacters().getData());
			        	   logger.debug("LSV  Message load element/ltc step "+ ltcMessageStatus.getLoadElement());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LSV_LTC_STEP_STATUS)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcMessageStatus.setLoadStatus(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LSV Message LTC load step status "+ ltcMessageStatus.getLoadStatus());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LSV_LTC_SYSTEM_MESSAGE)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcMessageStatus.setErrorDescription(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC load system message "+ ltcMessageStatus.getErrorDescription());
			           }
			        }
			    }
			    else if(event.isEndElement())
			    {
			    	if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.LSV_LOAD_REQEUST)) 
			        {
			           break;
			        }
			    }
			 }
		} catch (XMLStreamException xse) {
			throw new MessageParseException("Error when parsing the fields out of jmsMessage from LTC", xse);
		}

      return ltcMessageStatus;
	}
}
