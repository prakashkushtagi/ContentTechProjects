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

import com.thomson.judicial.dockets.bankruptcycontentservice.BankruptcyContentServiceConstants;
import com.trgr.dockets.core.domain.NovusDocumentMetadata;

/**
* Docket SAX Event Handler that parses out relevant Docket metadata that needs to be persisted
* in the Bankruptcy Content Wrapper database.
*/
public class BKRNovusDocumentParser implements IDocumentParser
{

	private NovusDocumentMetadata docketMetadata;
	private List <NovusDocumentMetadata> docketMetadataList = new ArrayList <NovusDocumentMetadata> ();
	private boolean isLegacyId = false;	
	private boolean isCreatedDatetime = false;
	private boolean isDocketNumber = false;
	private boolean isProductCode = false;
	private boolean isScrapeDate = false;
	private boolean isConvertDate = false;
	private boolean isVendorCode = false;
	private boolean isCaseStatus = false;
	private boolean isCourtNorm = false;
	private boolean isCaseType = false;
	private boolean isCaseSubType = false;
	private boolean isFiledDate = false;
	private boolean isTitle = false;	
	private String fileName;
	private StringBuffer charBuf = new StringBuffer();
	
	
	public BKRNovusDocumentParser(String fileName)
	{
		this.fileName = fileName;
	}
	
	public List<NovusDocumentMetadata> parse(InputStream contentInputStream) throws XMLStreamException, IOException
	{
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
				String elementName = event.asStartElement().getName().getLocalPart();
				if (elementName.equals(BankruptcyContentServiceConstants.N_DOCUMENT))
				{
					docketMetadata = new NovusDocumentMetadata();
					docketMetadata.setFileName(fileName);
					Attribute IdAttr = event.asStartElement().getAttributeByName(new QName(BankruptcyContentServiceConstants.N_DOCUMENT_GUID));
					if (IdAttr != null)
					{
						docketMetadata.setUuid(IdAttr.getValue());
					}
					IdAttr = event.asStartElement().getAttributeByName(new QName(BankruptcyContentServiceConstants.N_DOCUMENT_CONTROL));
					if (IdAttr != null)
					{
						docketMetadata.setControl(IdAttr.getValue());
					}
					else
					{
						docketMetadata.setControl("ADD");
					}
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.DOCKET_NUMBER))
				{
					Attribute IdAttr = event.asStartElement().getAttributeByName(new QName(BankruptcyContentServiceConstants.DOCKET_FILED_YEAR));
					if (IdAttr != null)
					{
						docketMetadata.setFiledYear(IdAttr.getValue());
					}
					IdAttr = event.asStartElement().getAttributeByName(new QName(BankruptcyContentServiceConstants.DOCKET_SEQUENCE));
					if (IdAttr != null)
					{
						docketMetadata.setSequence(IdAttr.getValue());
					}
					IdAttr = event.asStartElement().getAttributeByName(new QName(BankruptcyContentServiceConstants.DOCKET_TYPE));
					if (IdAttr != null)
					{
						docketMetadata.setType(IdAttr.getValue());
					}
					IdAttr = event.asStartElement().getAttributeByName(new QName(BankruptcyContentServiceConstants.DOCKET_OFFICE_LOCATION));
					if (IdAttr != null)
					{
						docketMetadata.setLocation(IdAttr.getValue());
					}
					isDocketNumber = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.CASE_TYPE))
				{
					isCaseType = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.CHAPTER))
				{
					isCaseSubType = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.MD_FILEDATE))
				{
					isFiledDate = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.PRIMARY_TITLE))
				{
					isTitle = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.LEGACY_ID))
				{
					isLegacyId = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.MD_CREATE_DATE_TIME))
				{
					isCreatedDatetime = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.PC))
				{
					isProductCode = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.SCRAPE_DATE))
				{
					isScrapeDate = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.DATA_SOURCE_TYPE))
				{
					isVendorCode = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.CONVERT_DATE))
				{
					isConvertDate = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.STATUS_DESCRIPTION))
				{
					isCaseStatus = true;
					charBuf = new StringBuffer();
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.COURT_NORM))
				{
					isCourtNorm = true;
					charBuf = new StringBuffer();
				}
			}
			else if(event.isCharacters() && (isDocketNumber || isLegacyId 
					|| isCreatedDatetime || isProductCode || isConvertDate 
					|| isScrapeDate || isVendorCode || isCaseStatus 
					|| isCourtNorm || isCaseType || isCaseSubType 
					|| isFiledDate || isTitle
					))
			{
				charBuf.append(event.asCharacters().getData());
			}
			else if (event.isEndElement())
			{
				//document start
				String elementName = event.asEndElement().getName().getLocalPart();
				if (elementName.equals(BankruptcyContentServiceConstants.N_DOCUMENT))
				{
					docketMetadataList.add(docketMetadata);
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.DOCKET_NUMBER))
				{
					isDocketNumber = false;
					docketMetadata.setDocketNumber(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.CASE_TYPE))
				{
					isCaseType = false;
					docketMetadata.setCaseType(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.CHAPTER))
				{
					isCaseSubType = false;
					docketMetadata.setCaseSubType(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.MD_FILEDATE))
				{
					isFiledDate = false;
					String date = charBuf.toString().trim();
					if(date.length()>4)
					{
						date = date.substring(0, 4);
					}
					docketMetadata.setFiledDate(date);
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.PRIMARY_TITLE))
				{
					isTitle = false;
					docketMetadata.setTitle(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.LEGACY_ID))
				{
					isLegacyId = false;
					docketMetadata.setLegacyId(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.MD_CREATE_DATE_TIME))
				{
					isCreatedDatetime = false;
					docketMetadata.setPublishDate(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.PC))
				{
					isProductCode = false;
					docketMetadata.setProductCode(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.SCRAPE_DATE))
				{
					isScrapeDate = false;
					docketMetadata.setScrapeDate(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.DATA_SOURCE_TYPE))
				{
					isVendorCode = false;
					docketMetadata.setVendorCode(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.CONVERT_DATE))
				{
					isConvertDate = false;
					docketMetadata.setConvertDate(charBuf.toString().trim());
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.STATUS_DESCRIPTION))
				{
					isCaseStatus = false;
					String caseStatus = charBuf.toString().trim();
					if(caseStatus.length()>100)
					{
						caseStatus = caseStatus.substring(0, 100);
					}
					docketMetadata.setCaseStatus(caseStatus);
					charBuf = null;
				}
				else if (elementName.equals(BankruptcyContentServiceConstants.COURT_NORM))
				{
					isCourtNorm = false;
					docketMetadata.setCourtNorm(charBuf.toString().trim());
					charBuf = null;
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
		eventReader= null;
		inputFactory = null;
		
		return docketMetadataList;
	}
	
	public List<NovusDocumentMetadata> getDocketMetadata()
	{
		return docketMetadataList;
	}
}
