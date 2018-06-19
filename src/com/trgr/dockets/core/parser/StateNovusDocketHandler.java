/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import com.trgr.dockets.core.domain.NovusDocumentMetadata;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;

public class StateNovusDocketHandler implements IDocumentParser
{
	public static final String N_DOCUMENT = "n-document";
	public static final String LEGACY_ID = "md.legacy.id";
	public static final String N_DOCUMENT_GUID = "guid";
	public static final String N_DOCUMENT_CONTROL = "control";
	public static final String SCRAPE_DATE = "scrape.date";
	public static final String PUBLISH_DATE = "publish.date";
	public static final String CONVERT_DATE = "convert.date";
	public static final String DOCKET_NUMBER = "md.docketnum";
	public static final String COURT_NORM = "md.jurisabbrev";
	
	private boolean isBuffered = false;
	private String fileName;
	private StringBuffer charBuf = null;
	
	private Long vendorId;
	
	public StateNovusDocketHandler(String fileName, Long vendorId)
	{
		this.fileName = fileName;
		this.vendorId = vendorId;
	}
	
	public List<NovusDocumentMetadata> parse(InputStream contentInputStream) throws XMLStreamException, IOException
	{
		NovusDocumentMetadata docketMetadata = null;
		List <NovusDocumentMetadata> docketMetadataList = new ArrayList <NovusDocumentMetadata>();
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		XMLEvent event = null;
		XMLEventReader eventReader = null;
		InputStreamReader inputStreamReader = new InputStreamReader(contentInputStream,"UTF-8");
		eventReader = inputFactory.createXMLEventReader(inputStreamReader);

		//parse through the entire load file.
		while (eventReader.hasNext())
		{
			event = eventReader.nextEvent();
			if (event.isStartElement())
			{
				//document start
				if (event.asStartElement().getName().getLocalPart().equals(N_DOCUMENT))
				{
					docketMetadata = new NovusDocumentMetadata(fileName);
					docketMetadata.setProductCode(ProductEnum.STATE.getCode());
					docketMetadata.setVendorCode(vendorId.toString());
					Attribute IdAttr = event.asStartElement().getAttributeByName(new QName(N_DOCUMENT_GUID));
					if (IdAttr != null)
					{
						docketMetadata.setUuid(IdAttr.getValue());
					}
					IdAttr  = event.asStartElement().getAttributeByName(new QName(N_DOCUMENT_CONTROL));
					if (IdAttr != null)
					{
						docketMetadata.setControl(IdAttr.getValue());
					}
					else
					{
						docketMetadata.setControl("ADD");
					}
				}
				else if (event.asStartElement().getName().getLocalPart().equals(LEGACY_ID))
				{
					isBuffered = true;
				}
				else if (event.asStartElement().getName().getLocalPart().equals(DOCKET_NUMBER))
				{
					isBuffered = true;
				}
				else if (event.asStartElement().getName().getLocalPart().equals(SCRAPE_DATE))
				{
					isBuffered = true;
				}
				else if (event.asStartElement().getName().getLocalPart().equals(CONVERT_DATE))
				{
					isBuffered = true;
				}
				else if (event.asStartElement().getName().getLocalPart().equals(PUBLISH_DATE))
				{
					isBuffered = true;
				}
				else if(event.asStartElement().getName().getLocalPart().equals(COURT_NORM))
				{
					isBuffered = true;
				}
			}
			else if(event.isCharacters())
			{
				if(isBuffered)
				{
					charBuf = new StringBuffer();
					charBuf.append(event.asCharacters().getData());
				}
			}
			else if (event.isEndElement())
			{
				//document start
				if (event.asEndElement().getName().getLocalPart().equals(N_DOCUMENT))
				{
					docketMetadataList.add(docketMetadata);
				}
				else if (event.asEndElement().getName().getLocalPart().equals(LEGACY_ID))
				{
					docketMetadata.setLegacyId(charBuf.toString().trim());
					isBuffered = false;
				}
				else if (event.asEndElement().getName().getLocalPart().equals(DOCKET_NUMBER))
				{
					docketMetadata.setDocketNumber(charBuf.toString().trim());
					isBuffered = false;
				}
				else if (event.asEndElement().getName().getLocalPart().equals(SCRAPE_DATE))
				{
					docketMetadata.setScrapeDate(charBuf.toString().trim());
					isBuffered = false;
				}
				else if (event.asEndElement().getName().getLocalPart().equals(CONVERT_DATE))
				{
					docketMetadata.setConvertDate(charBuf.toString().trim());
					isBuffered = false;
				}
				else if (event.asEndElement().getName().getLocalPart().equals(PUBLISH_DATE))
				{
					docketMetadata.setPublishDate(charBuf.toString().trim());
					isBuffered = false;
				}
				else if(event.asEndElement().getName().getLocalPart().equals(COURT_NORM))
				{
					docketMetadata.setCourtNorm(charBuf.toString().trim());
					isBuffered = false;
				}
			}
		}
		if(eventReader!=null)
		{
			eventReader.close();
		}
		if(inputStreamReader!=null)
		{
			inputStreamReader.close();
		}
		return docketMetadataList;
	}
}
