/* Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/

package com.trgr.dockets.core.util;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.trgr.dockets.core.domain.SourceDocketMetadata;

public class DocketFileParser {
	
	String DOCKET_TAG = "docket";
	String DOCKETS_TAG = "dockets";
	
	private static final Logger LOG = Logger.getLogger(DocketFileParser.class);
	

	public List<SourceDocketMetadata> buildSourceDocketMetadata(InputStream in, String inputName, String filepath) throws Exception {
		List<SourceDocketMetadata> sourceDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader xsr = xif.createXMLStreamReader(in);
		int eventType = xsr.nextTag();
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

		File file = null;
		SimpleDateFormat tempFileDateNameFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		boolean hadDocketsTag = false;
		while (eventType == XMLStreamConstants.START_ELEMENT){	
			if (xsr.getLocalName().equals(DOCKET_TAG)){
				Date tempFileDate = new Date();
				
				String dateFileName = tempFileDateNameFormat.format(tempFileDate);
				
				file = new File(filepath + dateFileName + xsr.getAttributeValue(null, "county") + xsr.getAttributeValue(null, "number")+ ".xml");
				
				FileUtils.forceMkdir(file.getParentFile());
				boolean newFile = file.createNewFile();
				//In the even that the file already exists, create a new timestamp and try to run again
				while (!newFile){
					file = new File(filepath + tempFileDateNameFormat.format(new Date()) + xsr.getAttributeValue(null, "county") + xsr.getAttributeValue(null, "number")+ ".xml");
					newFile = file.createNewFile();
				}
				
				
				SourceDocketMetadata sdm = buildSDMFromAttrs(xsr, file);
				//If we can't generate source docket metadata, don't build a file for it
				if (sdm != null){
					sourceDocketMetadataList.add(sdm);
					t.transform(new StAXSource(xsr), new StreamResult(file));
					eventType = xsr.getEventType();					
					if (eventType != XMLStreamConstants.START_ELEMENT && eventType != XMLStreamConstants.END_ELEMENT){
						 eventType = xsr.nextTag();
					}
				} else {
					LOG.error("Failed to create source docket metadata because we couldn't find docket number in header for file" + inputName);
					throw new Exception ("Failed to create source docket metadata because we were missing docket number on the header! ");
					
				}

			}
			else {
				if (eventType == XMLStreamConstants.START_ELEMENT && xsr.getLocalName().equals(DOCKETS_TAG)) {
					if (hadDocketsTag) {
						throw new Exception("Illformed docket with nested dockets tags.");
					} else {
						hadDocketsTag = true;
					}
				}
				eventType = xsr.nextTag();	
	        }			
		}
		
		return sourceDocketMetadataList;
	}
		
	public SourceDocketMetadata buildSDMFromAttrs (XMLStreamReader xsr, File file) {
		SourceDocketMetadata sourceDocketMetadata = null;
		
		String docketNumber = xsr.getAttributeValue(null, "number");
		String caseType = xsr.getAttributeValue(null, "casetype");
		String scrapedDate = xsr.getAttributeValue(null, "scrape.date");
		String county = xsr.getAttributeValue(null, "county");
		String courtName = xsr.getAttributeValue(null, "court");
		String filingDate = xsr.getAttributeValue(null, "filingdate");
		String caseSubType = xsr.getAttributeValue(null, "casesubtype");
		String miscellaneous = xsr.getAttributeValue(null, "miscellaneous");
		String acquisitionStatus = xsr.getAttributeValue(null, "acquisition.status");
		
		if (StringUtils.isNotBlank(docketNumber))
			sourceDocketMetadata = new SourceDocketMetadata(docketNumber);
		if (StringUtils.isNotBlank(caseType) && sourceDocketMetadata != null)
			sourceDocketMetadata.setCaseType(caseType);
		if (file != null)
			sourceDocketMetadata.setDocketContentFile(file);
		if (StringUtils.isNotBlank(scrapedDate) && sourceDocketMetadata != null)
			sourceDocketMetadata.setScrapedTimestamp(scrapedDate);
		if (StringUtils.isNotBlank(county) && sourceDocketMetadata != null)
			sourceDocketMetadata.setCountyName(county);
		if (StringUtils.isNotBlank(courtName) && sourceDocketMetadata !=null)
			sourceDocketMetadata.setCourtName(courtName);
		if (StringUtils.isNotBlank(filingDate) && sourceDocketMetadata !=null)
			sourceDocketMetadata.setFilingDate(filingDate);
		if (StringUtils.isNotBlank(caseSubType) && sourceDocketMetadata != null)
			sourceDocketMetadata.setCaseSubType(caseSubType);	
		if (StringUtils.isNotBlank(miscellaneous) && sourceDocketMetadata != null)
			sourceDocketMetadata.setMiscellaneous(miscellaneous);
		if (StringUtils.isNotBlank(acquisitionStatus) && sourceDocketMetadata != null)
			sourceDocketMetadata.setAcquisitionStatus(acquisitionStatus);
		return sourceDocketMetadata;
	}

	

}
