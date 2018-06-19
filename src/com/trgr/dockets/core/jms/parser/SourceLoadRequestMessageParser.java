/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.jms.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.trgr.dockets.core.CoreConstants;
import com.trgr.dockets.core.exception.MessageParseException;
import com.trgr.dockets.core.processor.SourceLoadRequest;

/**
 * LTCMessageParser.
 * This class parsers LTC jms Message into POJO message.	
 */
public class SourceLoadRequestMessageParser 
{
	private static Logger logger = Logger.getLogger(SourceLoadRequestMessageParser.class);
	
	/**
	 * 
	 * @param jmsMessage
	 * @return
	 * @throws MessageParseException
	 */
	public SourceLoadRequest parseMessage(javax.jms.Message jmsMessage) throws MessageParseException
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
	public SourceLoadRequest parseMessage(String textMessage) throws MessageParseException
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
	private SourceLoadRequest parse(InputStream inputStream) throws MessageParseException 
	{
		SourceLoadRequest sourceLoadRequest = null;

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
			        if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.SOURCE_LOAD_REQUEST)) 
			        {
			        	sourceLoadRequest = new SourceLoadRequest();
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.ACQUISITION_METHOD)) 
			        {
			        	if (eventReader.peek().isCharacters())
				           {
			        		sourceLoadRequest.setAcquisitionMethod(eventReader.nextEvent().asCharacters().getData());
				              logger.debug("Source Load Message acquisition Method "+ sourceLoadRequest.getAcquisitionMethod());
				           }

			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.ACQUISITION_TYPE)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   sourceLoadRequest.setAcquisitionType(eventReader.nextEvent().asCharacters().getData());
			        	   logger.debug("Source Load Message acquisition type "+ sourceLoadRequest.getAcquisitionType());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.ACQUISITION_START)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   sourceLoadRequest.setAcquisitionStart(eventReader.nextEvent().asCharacters().getData());
			        	   logger.debug("Source Load Message acquisition start "+ sourceLoadRequest.getAcquisitionStart());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.BATCHID)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   sourceLoadRequest.setBatchId(eventReader.nextEvent().asCharacters().getData());
			        	   logger.debug("Source Load Message batch Id "+ sourceLoadRequest.getBatchId());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.DELETE_FLAG)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   boolean deleteFlag = false;

			        		   deleteFlag = Boolean.parseBoolean(eventReader.nextEvent().asCharacters().getData());

			        	   sourceLoadRequest.setDeleteFlag(deleteFlag);
			        	   logger.debug("Source Load Message delete Flag "+ sourceLoadRequest.isDeleteFlag());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.PRODUCT_CODE)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   sourceLoadRequest.setProductCode(eventReader.nextEvent().asCharacters().getData());
			        	   logger.debug("Source Load Message product Code "+ sourceLoadRequest.getProductCode());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.SOURCE_FILE)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   String sourcePath = eventReader.nextEvent().asCharacters().getData();
			        	   logger.debug("Source Load Message source file path "+ sourcePath);
			        	   if(sourcePath!=null && !sourcePath.isEmpty())
			        	   {
			        		   sourcePath = sourcePath.replaceAll("\\\\", "/");
			        	   }
			        	   sourceLoadRequest.setSourcePath(sourcePath);
			        	   logger.debug("Source Load Message converted source file path "+ sourceLoadRequest.getSourcePath());
			           }
			        }
			        else if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.WORK_FOLDER)) 
			        {
			           if (eventReader.peek().isCharacters())
			           {
			        	   String workFolder = eventReader.nextEvent().asCharacters().getData();
			        	   logger.debug("Source Load Message source file path "+ workFolder);
			        	   if(workFolder!=null && !workFolder.isEmpty())
			        	   {
			        		   workFolder = workFolder.replaceAll("\\\\", "/");
			        		   if (!workFolder.endsWith("/"))
								{
									workFolder = workFolder + "/";
								}
			        	   }
							
			        	   sourceLoadRequest.setWorkFolder(workFolder);
			        	   logger.debug("Source Load Message converted work folder "+ sourceLoadRequest.getWorkFolder());
			           }
			        }
			       
			    }
			    else if(event.isEndElement())
			    {
			    	if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.SOURCE_LOAD_REQUEST)) 
			        {
			           break;
			        }
			    }
			 }
		} catch (XMLStreamException xse) {
			throw new MessageParseException("Error when parsing the fields out of Source Load request jms from LTC", xse);
		}

      return sourceLoadRequest;
	}
}
