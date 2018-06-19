/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */

package com.trgr.dockets.core.util;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import com.trgr.dockets.core.domain.Docket;
import com.trgr.dockets.core.domain.DocketsList;
import com.trgr.dockets.core.domain.SourceLoadRequest;
import com.trgr.dockets.core.exception.SourceContentLoaderException;

/**
 * @author <a href="mailto:mahendra.survase@thomsonreuters.com">Mahendra Survase</a> u0105927
 * 
 */
public class DocketMarshllerAndUnmarshller {
//	private static final Logger LOG = Logger.getLogger(DocketMarshllerAndUnmarshller.class);
	
	
	
	public DocketMarshllerAndUnmarshller() {
		super();
	}

	/**
	 * Converts xml contents in java objects(unmarshall).
	 * @throws SourceContentLoaderException 
	 */
	public DocketsList docketsUnmarshller(InputStream contentInputStream) throws SourceContentLoaderException{
		DocketsList docketsList = null;
		try {
			
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DocketsList.class);
			Unmarshaller jaxbUnmarshller = jaxbContext.createUnmarshaller();
//			jaxbUnmarshller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File("DocketMergefileXsd.xsd")));
			docketsList = (DocketsList) jaxbUnmarshller.unmarshal(contentInputStream);
			
		} catch (JAXBException e) {
			
			e.printStackTrace();
			throw new SourceContentLoaderException("Failed to parse file "+ e);
		} 		
		return docketsList;
	}
	
	/**
	 * parse source content load request(xml) and returns java object populated with values from message. 
	 * 
	 * @param inputSource
	 * @return
	 * @throws SourceContentLoaderException
	 */
	public SourceLoadRequest requestMessageUnmarshal(InputSource inputSource) throws SourceContentLoaderException{
		SourceLoadRequest sourceLoadRequest = null;
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(SourceLoadRequest.class);
				Unmarshaller jaxbUnmarshller = jaxbContext.createUnmarshaller();
//				jaxbUnmarshller.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File("src/com/trgr/dockets/core/util/SourceLoadRequestXsd.xsd")));
				sourceLoadRequest =(SourceLoadRequest) jaxbUnmarshller.unmarshal(inputSource);
			} catch (JAXBException e) {
				 
				e.printStackTrace();
				throw new SourceContentLoaderException("Failed to parse file "+ e);
			}	

		return sourceLoadRequest;
		
	}
	/**
	 * Creates xml file using domain objects.(marshalL)  
	 * @param docketsList
	 * @throws SourceContentLoaderException 
	 */
	public void docketsMarshaller(DocketsList docketsList) throws SourceContentLoaderException {
		try {

			File file = new File("RecreatedContent.xml");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DocketsList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(docketsList, file);
			jaxbMarshaller.marshal(docketsList, System.out);

		} catch (JAXBException e) {

			e.printStackTrace();
			throw new SourceContentLoaderException("Failed to ceate xml file using passed in objects : "+ docketsList);
		}

	}
	
	/**
	 * Creates xml file using domain objects.(marshal)  
	 * @param docketsList
	 * @throws SourceContentLoaderException 
	 */
	public String docketDumpMarshaller(Docket docket) throws SourceContentLoaderException {
		String returnString = null; 
		try {

//			File file = new File("RecreatedContent.xml");
			Writer writer = new StringWriter();

			JAXBContext jaxbContext = JAXBContext.newInstance(DocketsList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			// remove xml namespace.
			jaxbMarshaller.setProperty("jaxb.encoding", "Unicode");
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			jaxbMarshaller.marshal(docket, writer);
			returnString = writer.toString();

		} catch (JAXBException e) {

			e.printStackTrace();
			throw new SourceContentLoaderException("Failed to xml docket dump for given docket object : "+ docket);
		}
		return  returnString;
	}


	

}
