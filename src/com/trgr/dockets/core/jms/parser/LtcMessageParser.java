/*
Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.jms.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.domain.ltc.LtcCollaborationKey;
import com.trgr.dockets.core.domain.ltc.LtcEvent;
import com.trgr.dockets.core.domain.ltc.LtcMessage;
import com.trgr.dockets.core.exception.MessageParseException;

/**
 * LTCMessageParser.
 * This class parsers LTC jms Message into POJO message.	
 */
public class LtcMessageParser 
{
	private static Logger logger = Logger.getLogger(LtcMessageParser.class);
	
	/**
	 * 
	 * @param jmsMessage
	 * @return
	 * @throws MessageParseException
	 */
	public LtcMessage parseMessage(javax.jms.Message jmsMessage) throws MessageParseException
	{
      String message = null;
      InputStream inputStream = null;
     /** create inputStream from jmsMessage **/
     if (jmsMessage instanceof TextMessage)
     {
        try 
        {
			message = ((TextMessage) jmsMessage).getText();
		}
        catch (JMSException jmse) 
        {
			throw new MessageParseException("Error when getting the text out of jmsMessage from LTC", jmse);
		}
        inputStream = new ByteArrayInputStream(message.getBytes());
     }
     else
     {
    	 throw new MessageParseException("We are getting someother type of message as opposed to text jmsMessage");
     }
     
     return parse(inputStream);
   }
	
	/**
	 * 
	 * @param textMessage
	 * @return
	 * @throws MessageParseException
	 */
	public LtcMessage parseMessage(String textMessage) throws MessageParseException
	{
	      InputStream inputStream = null;
	     /** create inputStream from jmsMessage **/
	     if (null!=textMessage)
	     {
	        inputStream = new ByteArrayInputStream(textMessage.getBytes());
	     }    
	     return parse(inputStream);
    }
	
	/**
	 * @param inputStream
	 * @param ltcMessage
	 * @param ltcEvent
	 * @return
	 * @throws MessageParseException
	 */
	private LtcMessage parse(InputStream inputStream) throws MessageParseException 
	{
	      LtcMessage ltcMessage = null;
	      LtcEvent ltcEvent = null;
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
			        if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_EVENT_COLLECTION)) 
			        {
			        	ltcMessage = new LtcMessage();
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_EVENT_DATA)) 
			        {
			        	ltcEvent = new LtcEvent();
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_EVENT_ID)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	  ltcEvent.setEventId(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC event id "+ ltcEvent.getEventId());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_EVENT_DATETIME)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcEvent.setEventTimestamp(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC event datetime element "+ ltcEvent.getEventTimestamp());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_LOAD_COLLECTION)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcEvent.setCollection(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC load collection element "+ ltcEvent.getCollection());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_LOAD_STATUS)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcEvent.setEventLoadStatus(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC load status element "+ ltcEvent.getEventLoadStatus());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_LOAD_ELEMENT)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcEvent.setEventLoadElementStep(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC load element step"+ ltcEvent.getEventLoadElementStep());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_SYSTEM_MESSAGE)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcEvent.setSystemMessage(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC System Message"+ ltcEvent.getSystemMessage());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_LOAD_DATASET)) 
			        {
			            String loadDatasetId = "";
			            String fileName = "";
			            String loadRequestId = "";
			            String requestCollaborationKey = "";
			            
			            Attribute attributeId =  event.asStartElement().getAttributeByName(new QName(CoreConstants.LTC_LOAD_DATASET_ID));
			            if (attributeId != null)
			            {
			            	loadDatasetId = attributeId.getValue();
			            	logger.debug("LTC Message LTC loadDatasetId "+ loadDatasetId);
			            }
			            attributeId =  event.asStartElement().getAttributeByName(new QName(CoreConstants.LTC_LOAD_DATASET_REQUEST_ID));
			            if (attributeId != null)
			            {
			            	loadRequestId = attributeId.getValue();
			            	logger.debug("LTC Message LTC loadRequestId "+ loadRequestId);
			            }
			            attributeId =  event.asStartElement().getAttributeByName(new QName(CoreConstants.LTC_LOAD_DATASET_REQUEST_COLLAB_KEY));
			            if (attributeId != null)
			            {
			            	requestCollaborationKey = attributeId.getValue();
			            	logger.debug("LTC Message LTC loadRequestId "+ requestCollaborationKey);
			            }
			            if (eventReader.peek().isCharacters())
			            {
			            	fileName = eventReader.nextEvent().asCharacters().getData();
			            	logger.debug("LTC Message LTC input file location "+ fileName);
			            }
			            
						LtcCollaborationKey ltcCollaborationKey = new LtcCollaborationKey(requestCollaborationKey);
			            ltcEvent.addLoadDataset(loadDatasetId, loadRequestId, ltcCollaborationKey,ltcCollaborationKey.getRequestId(), ltcCollaborationKey.getUberBatchId(), ltcCollaborationKey.getBatchId(), ltcCollaborationKey.getSubBatchId(),fileName);
			            ltcMessage.setProductCode(ltcCollaborationKey.getProductCode());
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.LTC_EVENT_DATETIME)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   ltcEvent.setEventTimestamp(eventReader.nextEvent().asCharacters().getData());
			              logger.debug("LTC Message LTC System Message"+ ltcEvent.getCollection());
			           }
			        }
			    }
			    else if(event.isEndElement())
			    {
			    	if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.LTC_EVENT_DATA)) 
			        {
			           ltcMessage.addLTCEvent(ltcEvent);
			        }
			    }
			 }
		} catch (XMLStreamException xse) {
			throw new MessageParseException("Error when parsing the fields out of jmsMessage from LTC", xse);
		}

      return ltcMessage;
	}
}
