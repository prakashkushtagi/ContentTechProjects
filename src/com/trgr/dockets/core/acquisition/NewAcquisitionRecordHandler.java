package com.trgr.dockets.core.acquisition;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

import com.trgr.dockets.core.util.DctDocketNumberUtility;
import com.west.exception.SAXException;
import com.west.utilities.xml.XmlHandler;


public class NewAcquisitionRecordHandler extends XmlHandler
{
	private static Logger logger = Logger.getLogger(NewAcquisitionRecordHandler.class);
	
	private AcquisitionRecord acquisitionRecord = null;
	private AcquisitionRecord commonHeaders = null;
	private List<AcquisitionRecord> acquisitionRecordList = null;
	private StringBuffer cdata = new StringBuffer();
	private AcquisitionDocket acquisitionDocket = null;
	
	private boolean inAcquiredDocketsElement = false;
	private boolean inDeletedDocketsElement = false;
		
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
				acquisitionRecordList = new ArrayList<AcquisitionRecord>();
				commonHeaders = new AcquisitionRecord();	
				commonHeaders.setAcquisitionReceiptId(attributes.getValue(AcquisitionConstants.DCS_RECEIPT_ATTRIBUTE));
				commonHeaders.setProvider(attributes.getValue(AcquisitionConstants.DC_PROVIDER));
				commonHeaders.setAcquisitionSourceName(attributes.getValue(AcquisitionConstants.SENDER_ID_ATTRIBUTE));

				String mergedFileName = attributes.getValue(AcquisitionConstants.MERGED_FILE_NAME_ATTRIBUTE);
				if (null != mergedFileName && mergedFileName.length() > 0)
				{
					commonHeaders.setMergedFileName(mergedFileName);
				}
				
				String fileSize = attributes.getValue(AcquisitionConstants.MERGED_FILE_SIZE_ATTRIBUTE);
				if (null != fileSize && fileSize.length() > 0)
				{
					commonHeaders.setMergedFileSize(Long.parseLong(fileSize));
				}
				
				commonHeaders.setScriptStartDate(attributes.getValue(AcquisitionConstants.SCRIPT_DATE_TIME_START_ATTRIBUTE));
				commonHeaders.setScriptEndDate(attributes.getValue(AcquisitionConstants.SCRIPT_DATE_TIME_END_ATTRIBUTE));
				commonHeaders.setRetrievalType(attributes.getValue(AcquisitionConstants.RETRIEVE_TYPE_ATTRIBUTE));
			}
			else if (qName.equals(AcquisitionConstants.COURT_ELEMENT))
			{
				
				acquisitionRecord = new AcquisitionRecord();
				acquisitionRecord.setAcquisitionReceiptId(commonHeaders.getAcquisitionReceiptId());
				acquisitionRecord.setProvider(commonHeaders.getProvider());
				acquisitionRecord.setAcquisitionSourceName(commonHeaders.getAcquisitionSourceName());
				
				String mergedFileName = commonHeaders.getMergedFileName();
				if (null != mergedFileName && mergedFileName.length() > 0)
				{
					acquisitionRecord.setMergedFileName(mergedFileName);
				}

				SimpleDateFormat simple = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
				acquisitionRecord.setMergedFileSize(commonHeaders.getMergedFileSize());
				acquisitionRecord.setScriptStartDate(simple.format(commonHeaders.getScriptStartDate()));
				acquisitionRecord.setScriptEndDate(simple.format(commonHeaders.getScriptEndDate()));
				acquisitionRecord.setRetrievalType(commonHeaders.getRetrievalType());
				
				acquisitionRecord.setWestlawClusterName(attributes.getValue(AcquisitionConstants.WESTLAW_CLUSTER_NAME_ATTRIBUTE)); 
				acquisitionRecord.setCourtType(attributes.getValue(AcquisitionConstants.COURT_TYPE_ATTRIBUTE));
				acquisitionRecord.setAcquisitionStatus(attributes.getValue(AcquisitionConstants.ACQUISITION_STATUS_ATTRIBUTE));
			}
			else if (qName.equals(AcquisitionConstants.DELETED_DOCKETS_ELEMENT))
			{
				inDeletedDocketsElement = true;
			}
			else if (qName.equals(AcquisitionConstants.ACQUIRED_DOCKETS_ELEMENT))
			{
				inAcquiredDocketsElement = true;
			}
			else if (qName.equals(AcquisitionConstants.DCT_DOCKET_ELEMENT) || 
					qName.equals(AcquisitionConstants.STATE_DOCKET_ELEMENT) ||
					qName.equals(AcquisitionConstants.BKR_DOCKET_ELEMENT) ||
					qName.equals(AcquisitionConstants.CTA_DOCKET_ELEMENT) ||
					qName.equals(AcquisitionConstants.SCT_DOCKET_ELEMENT) ||
					qName.equals(AcquisitionConstants.OTHER_DOCKET_ELEMENT)) 
			{
				
				if (inAcquiredDocketsElement || inDeletedDocketsElement)
				{
					acquisitionDocket = new AcquisitionDocket();
					if (qName.equals(AcquisitionConstants.DCT_DOCKET_ELEMENT)) {
						acquisitionDocket.setDocketNumber(DctDocketNumberUtility.normalizeDocketNumber(attributes.getValue(AcquisitionConstants.NUMBER_ATTRIBUTE)));
					} else {
						acquisitionDocket.setDocketNumber(attributes.getValue(AcquisitionConstants.NUMBER_ATTRIBUTE));
					}
					acquisitionDocket.setFileName(attributes.getValue(AcquisitionConstants.FILENAME_ATTRIBUTE));
					acquisitionDocket.setCaseType(attributes.getValue(AcquisitionConstants.CASE_TYPE_ATTRIBUTE));
					acquisitionDocket.setFilingYear(attributes.getValue(AcquisitionConstants.FILING_YEAR_ATTRIBUTE));
					acquisitionDocket.setSequenceNumber(attributes.getValue(AcquisitionConstants.SEQUENCE_NUMBER_ATTRIBUTE));
					acquisitionDocket.setSubfolder(attributes.getValue(AcquisitionConstants.SUBFOLDER_ATTRIBUTE));
					acquisitionDocket.setFilingLocation(attributes.getValue(AcquisitionConstants.FILING_LOCATION_ATTRIBUTE));
					acquisitionDocket.setSubDivision(attributes.getValue(AcquisitionConstants.SUBDIVISION_ATTRIBUTE));
				}
			}
			else if(qName.equals(AcquisitionConstants.PDF_FILE_ELEMENT))
			{
				acquisitionDocket.setPdfFileName(attributes.getValue(AcquisitionConstants.FILENAME_ATTRIBUTE));
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
		if (qName.equals(AcquisitionConstants.ACQUIRED_DOCKETS_ELEMENT))
		{
			inAcquiredDocketsElement = false;
		} 
		else if (qName.equals(AcquisitionConstants.DELETED_DOCKETS_ELEMENT))
		{
			inDeletedDocketsElement = false;
		} 
		else if (qName.equals(AcquisitionConstants.SCT_DOCKET_ELEMENT) ||
				qName.equals(AcquisitionConstants.OTHER_DOCKET_ELEMENT)) 
		{
			acquisitionDocket = null;
		}
		else if(qName.equals(AcquisitionConstants.CTA_DOCKET_ELEMENT) ||
				qName.equals(AcquisitionConstants.BKR_DOCKET_ELEMENT) || 
				qName.equals(AcquisitionConstants.STATE_DOCKET_ELEMENT) || 
				qName.equals(AcquisitionConstants.DCT_DOCKET_ELEMENT))
		{
			if(inAcquiredDocketsElement)
			{
				acquisitionDocket.setDeleteOperation(false);
				acquisitionRecord.getAcquiredDocketsList().add(acquisitionDocket);
			}
			else if(inDeletedDocketsElement)
			{
				acquisitionDocket.setDeleteOperation(true);
				acquisitionRecord.getDeletedDocketsList().add(acquisitionDocket);
			}
			acquisitionDocket = null;
		}
		else if(qName.equals(AcquisitionConstants.COURT_ELEMENT))
		{
			acquisitionRecordList.add(acquisitionRecord);
		}
	}

	public AcquisitionRecord getAcquisitionRecord()
	{
		return acquisitionRecord;
	}

	/**
	 * @return the acquisitionRecordList
	 */
	public List<AcquisitionRecord> getAcquisitionRecordList() {
		return acquisitionRecordList;
	}

}



