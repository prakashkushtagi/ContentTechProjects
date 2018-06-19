/*
 Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */

package com.trgr.dockets.core.util;

import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.trgr.dockets.core.domain.EventXml;

public class EventMarshllerAndUnmarshller {
	public EventMarshllerAndUnmarshller() {
		super();
	}

	/**
	 * 
	 * @param eventXml
	 * @return
	 * @throws Exception
	 */
	public String eventMarshal(EventXml eventXml) throws Exception {

		Writer writer = new StringWriter();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(EventXml.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			// remove xml namespace.
			jaxbMarshaller.setProperty("jaxb.encoding", "Unicode");
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(eventXml, writer);
			return writer.toString();
			
		} catch (JAXBException e){
			e.printStackTrace();
			throw new JAXBException("Failed to create Event message ",e);
			
		} finally {
			writer.close();
		}
	}


}
