/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.util;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import com.trgr.dockets.core.domain.EventXml;
import com.trgr.dockets.core.domain.ltc.EventCollection;
import com.trgr.dockets.core.exception.LoadMonitoringException;

/**
 *
 */
public class LtcEventMarshallerAndUnmarshaller {
	private static final Logger LOG = Logger.getLogger(LtcEventMarshallerAndUnmarshaller.class);

	public LtcEventMarshallerAndUnmarshaller(){
		super();
	}

	/**
	 * Converts xml message to java objects(unmarshall)
	 * @throws LoadMonitoringException
	 */
	public EventCollection ltcEventUnmarshall(InputSource is) throws LoadMonitoringException{
		EventCollection eventCollection = null;
			try{
				
				JAXBContext jaxbContext = JAXBContext.newInstance(EventCollection.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				eventCollection = (EventCollection) jaxbUnmarshaller.unmarshal(is);
				LOG.debug(eventCollection);
			} catch (JAXBException e){
				throw new LoadMonitoringException("Failed to parse message "+e);
			}
		return eventCollection;
	}
	
	/*Converts java objects to Xml message
	 * @throws LoadMonitoringException
	 */
	
	public String ltcEventMarshal(EventXml eventXml) throws Exception {

		Writer writer = new StringWriter();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(EventXml.class);
			Marshaller marshller = jaxbContext.createMarshaller();
			
			//escape character avoidance
/*			marshller.setProperty("com.sun.xml.internal.bind.characterEscapeHandler", new CharacterEscapeHandler(){
				@Override
				public void escape(char[] ac, int i , int j, boolean flag, Writer writer) throws IOException{
					writer.write(ac, i, j);
				}
			});*/
			//marshller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			//remove xml header from the message
			marshller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			marshller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshller.marshal(eventXml, writer);
			return writer.toString();
			
		} catch (JAXBException e){
			e.printStackTrace();
			throw new LoadMonitoringException("Failed to create Event message "+e);
			
		} finally {
			writer.close();
		}
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}


