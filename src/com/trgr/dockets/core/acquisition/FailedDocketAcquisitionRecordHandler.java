/*Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.acquisition;


import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import com.west.exception.SAXException;
import com.west.utilities.xml.XmlHandler;


public class FailedDocketAcquisitionRecordHandler extends XmlHandler
{
	private static Logger logger = Logger.getLogger(FailedDocketAcquisitionRecordHandler.class);
	
	private AcquisitionLogDocketRecord acquisitionRecord = null;
	private AcquisitionLogCourtRecord acquisitionCourtRecord = null;
	private StringBuffer cdata = new StringBuffer();
	private FailedAcquisitionDocket failedDocket = null;
	
	private boolean inSkippedDocketsElement = false;
	private boolean inWebsiteErrorsElement = false;
		
	/**
	 * This method handles element start events for the XML parser. 
	 * <p>
	 * This method handles element start events for the XML parser.
	 * It goes through the new acuisition record xml and parses the attribute values
	 * and then populates the model objects accordingly to persist later.
	 *
	 * @param uri The Namespace URI, or the empty string if the
	 *        element has no Namespace URI or if Namespace
	 *        processing is not being performed.
	 * @param localName The local name (without prefix), or the
	 *        empty string if Namespace processing is not being
	 *        performed.
	 * @param qName The qualified name (with prefix), or the
	 *        empty string if qualified names are not available.
	 * @param atts The attributes attached to the element.  If
	 *        there are no attributes, it shall be an empty
	 *        Attributes object.
	 * @return      void
	 * @see         
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		try
		{
			this.cdata.setLength(0);
			
			if (qName.equals(AcquisitionConstants.NEW_ACQUISITION_RECORD_ELEMENT))
			{
				acquisitionRecord = new AcquisitionLogDocketRecord();	
				acquisitionRecord.setAcquisitionReceiptId(attributes.getValue(AcquisitionConstants.DCS_RECEIPT_ATTRIBUTE));
				acquisitionRecord.setProvider(attributes.getValue(AcquisitionConstants.DC_PROVIDER));
				acquisitionRecord.setAcquisitionSourceName(attributes.getValue(AcquisitionConstants.SENDER_ID_ATTRIBUTE));

				String mergedFileName = attributes.getValue(AcquisitionConstants.MERGED_FILE_NAME_ATTRIBUTE);
				if (null != mergedFileName && mergedFileName.length() > 0)
				{
					acquisitionRecord.setMergedFileName(mergedFileName);
				}
				
				String fileSize = attributes.getValue(AcquisitionConstants.MERGED_FILE_SIZE_ATTRIBUTE);
				if (null != fileSize && fileSize.length() > 0)
				{
					acquisitionRecord.setMergedFileSize(Long.parseLong(fileSize));
				}
				
//				SimpleDateFormat simple = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
//				String dateString3 = "Thu Feb 18 23:00:17 CST 2016";
//				String dateString = attributes.getValue(AcquisitionConstants.SCRIPT_DATE_TIME_START_ATTRIBUTE);
//				String dateString2 = simple.format(dateString3);
//				dateString2 = simple.format(dateString);

				acquisitionRecord.setScriptStartDate(attributes.getValue(AcquisitionConstants.SCRIPT_DATE_TIME_START_ATTRIBUTE));
				acquisitionRecord.setScriptEndDate(attributes.getValue(AcquisitionConstants.SCRIPT_DATE_TIME_END_ATTRIBUTE));
				acquisitionRecord.setRetrievalType(attributes.getValue(AcquisitionConstants.RETRIEVE_TYPE_ATTRIBUTE));
			}
			else if (qName.equals(AcquisitionConstants.COURT_ELEMENT))
			{
				acquisitionCourtRecord = new AcquisitionLogCourtRecord();
				acquisitionCourtRecord.setWestlawClusterName(attributes.getValue(AcquisitionConstants.WESTLAW_CLUSTER_NAME_ATTRIBUTE)); 
				acquisitionCourtRecord.setCourtType(attributes.getValue(AcquisitionConstants.COURT_TYPE_ATTRIBUTE));
				acquisitionCourtRecord.setAcquisitionStatus(attributes.getValue(AcquisitionConstants.ACQUISITION_STATUS_ATTRIBUTE));
			}
			else if (qName.equals(AcquisitionConstants.SKIPPED_DOCKETS_ELEMENT))
			{
				inSkippedDocketsElement = true;

			}
			else if(inSkippedDocketsElement && qName.equals(AcquisitionConstants.SKIPPED_DOCKET_STATUS_ELEMENT)) 
					//qName.equals(AcquisitionConstants.STATE_DOCKET_ELEMENT)) 
			{
				failedDocket = new FailedAcquisitionDocket();
				failedDocket.setWestgetStage(attributes.getValue(AcquisitionConstants.WESTGET_STAGE_ATTRIBUTE));
				failedDocket.setStatus(attributes.getValue(AcquisitionConstants.STATUS_ATTRIBUTE));
				failedDocket.setObjectType(AcquisitionConstants.SKIPPED_TYPE_OBJECT);

			}else if(inSkippedDocketsElement && (qName.equals(AcquisitionConstants.STATE_DOCKET_ELEMENT)||
							qName.equals(AcquisitionConstants.BKR_DOCKET_ELEMENT)||
							qName.equals(AcquisitionConstants.CTA_DOCKET_ELEMENT)||
							qName.equals(AcquisitionConstants.SCT_DOCKET_ELEMENT)||
							qName.equals(AcquisitionConstants.OTHER_DOCKET_ELEMENT)||
							qName.equals(AcquisitionConstants.DCT_DOCKET_ELEMENT)
							)){
						
						failedDocket.setDocketNumber(attributes.getValue(AcquisitionConstants.NUMBER_ATTRIBUTE));
						failedDocket.setFileName(attributes.getValue(AcquisitionConstants.FILENAME_ATTRIBUTE));
						failedDocket.setCaseType(attributes.getValue(AcquisitionConstants.CASE_TYPE_ATTRIBUTE));
						failedDocket.setFilingYear(attributes.getValue(AcquisitionConstants.FILING_YEAR_ATTRIBUTE));
						failedDocket.setSequenceNumber(attributes.getValue(AcquisitionConstants.SEQUENCE_NUMBER_ATTRIBUTE));
						failedDocket.setSubfolder(attributes.getValue(AcquisitionConstants.SUBFOLDER_ATTRIBUTE));
						failedDocket.setFilingLocation(attributes.getValue(AcquisitionConstants.FILING_LOCATION_ATTRIBUTE));
						failedDocket.setSubDivision(attributes.getValue(AcquisitionConstants.SUBDIVISION_ATTRIBUTE));
			}else if(qName.equals(AcquisitionConstants.WEBSITE_ERRORS_ELEMENT)){
				inWebsiteErrorsElement = true;
				
			}else if(inWebsiteErrorsElement && (qName.equals(AcquisitionConstants.WEB_ERROR_DOCKET_ELEMENT)||
					qName.equals(AcquisitionConstants.WEB_ERROR_STATUS_ELEMENT))){
				failedDocket = new FailedAcquisitionDocket();
				failedDocket.setReason(attributes.getValue(AcquisitionConstants.REASON_ATTRIBUTE));
				failedDocket.setWestgetStage(attributes.getValue(AcquisitionConstants.WESTGET_STAGE_ATTRIBUTE));
				failedDocket.setStatus(attributes.getValue(AcquisitionConstants.STATUS_ATTRIBUTE));
				failedDocket.setCurlCode(attributes.getValue(AcquisitionConstants.CURL_CODE_ATTRIBUTE));
				failedDocket.setHttpErrorCode(attributes.getValue(AcquisitionConstants.HTTPERROR_CODE_ATTRIBUTE));
				failedDocket.setHttpErrorCode(attributes.getValue(AcquisitionConstants.HTTPERROR_CODE_ATTRIBUTE));
				failedDocket.setCourtErrorMessage(attributes.getValue(AcquisitionConstants.COURT_ERROR_MESSAGE_ATTRIBUTE));
				failedDocket.setObjectType(AcquisitionConstants.WEB_ERRPR_TYPE_OBJECT);

				
			}else if(inWebsiteErrorsElement && (qName.equals(AcquisitionConstants.STATE_DOCKET_ELEMENT))){
				failedDocket.setDocketNumber(attributes.getValue(AcquisitionConstants.NUMBER_ATTRIBUTE));
				failedDocket.setFileName(attributes.getValue(AcquisitionConstants.FILENAME_ATTRIBUTE));
				failedDocket.setCaseType(attributes.getValue(AcquisitionConstants.CASE_TYPE_ATTRIBUTE));
				failedDocket.setFilingYear(attributes.getValue(AcquisitionConstants.FILING_YEAR_ATTRIBUTE));
				failedDocket.setSubfolder(attributes.getValue(AcquisitionConstants.SUBFOLDER_ATTRIBUTE));
				failedDocket.setSubDivision(attributes.getValue(AcquisitionConstants.SUBDIVISION_ATTRIBUTE));
				
			}

		}
		catch (Exception e)
		{
			logger.error(e);
			throw new SAXException(e);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		try
		{
			cdata.append(new String(ch, start, length));
		}
		catch (Exception e)
		{
			throw new SAXException(e);
		}
	}

	/**
	 * This method handles element end events for the XML parser. 
	 * It will handle the notification of the end of an element
	 * <p>
	 * 
	 *
	 * @param uri The Namespace URI, or the empty string if the
	 *        element has no Namespace URI or if Namespace
	 *        processing is not being performed.
	 * @param localName The local name (without prefix), or the
	 *        empty string if Namespace processing is not being
	 *        performed.
	 * @param qName The qualified XML 1.0 name (with prefix), or the
	 *        empty string if qualified names are not available.
	 * * @return      void
	 * @see         
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equals(AcquisitionConstants.SKIPPED_DOCKETS_ELEMENT))
		{
			inSkippedDocketsElement = false;

		} 
		else if (qName.equals(AcquisitionConstants.SKIPPED_DOCKET_STATUS_ELEMENT)) 
		{
			if(inSkippedDocketsElement)
			{
				acquisitionCourtRecord.getFailedDocketsList().add(failedDocket);
			}
			
			failedDocket = null;
		}
		else if(qName.equals(AcquisitionConstants.WEBSITE_ERRORS_ELEMENT))
		{
			inWebsiteErrorsElement = false;
		}
		else if(qName.equals(AcquisitionConstants.WEB_ERROR_DOCKET_ELEMENT) || qName.equals(AcquisitionConstants.WEB_ERROR_STATUS_ELEMENT))
		{
			if(inWebsiteErrorsElement)
			{
				acquisitionCourtRecord.getFailedDocketsList().add(failedDocket);
			}
			failedDocket = null;
		}
		else if (qName.equals(AcquisitionConstants.COURT_ELEMENT))
		{
			acquisitionRecord.getAcquisitionLogCourtRecordList().add(acquisitionCourtRecord);
		}

	}



	public AcquisitionLogDocketRecord getAcquisitionRecord() {
		return acquisitionRecord;
	}

	public void setAcquisitionRecord(AcquisitionLogDocketRecord acquisitionRecord) {
		this.acquisitionRecord = acquisitionRecord;
	}


}



