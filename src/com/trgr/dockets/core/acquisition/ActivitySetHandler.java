package com.trgr.dockets.core.acquisition;


import org.xml.sax.Attributes;

import com.west.exception.SAXException;
import com.west.utilities.xml.XmlHandler;


public class ActivitySetHandler extends XmlHandler
{
	private ActivitySet activitySet = null;
	private StringBuffer cdata = new StringBuffer();
	
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
	 *         
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		try
		{
			this.cdata.setLength(0);
					
			if (qName.equals(AcquisitionConstants.ACTIVITY_SET_ELEMENT))
			{
				activitySet = new ActivitySet();
				activitySet.setReceiptId(attributes.getValue(AcquisitionConstants.ACTIVITY_RECEIPT_ATTRIBUTE));
			}
			else if (qName.equals(AcquisitionConstants.ACTIVITY_RUN_ELEMENT))
			{
				activitySet.setStartTime(attributes.getValue(AcquisitionConstants.ACTIVITY_DATE_TIME_START_ATTRIBUTE));
				activitySet.setEndTime(attributes.getValue(AcquisitionConstants.ACTIVITY_DATE_TIME_END_ATTRIBUTE));
				activitySet.setSourceFile(attributes.getValue(AcquisitionConstants.SOURCE_FILE_ATTRIBUTE));
				activitySet.setStatus(attributes.getValue(AcquisitionConstants.STATUS_ATTRIBUTE));
			}
			else if(qName.equals(AcquisitionConstants.ACTIVITY_ELEMENT)){
				if(attributes.getValue(AcquisitionConstants.ACTIVITY_NAME_ATTRIBUTE).equals(AcquisitionConstants.DOCKET_CONVERSION_ACTIVITY_NAME)){
					activitySet.setConversionStartTime(attributes.getValue(AcquisitionConstants.ACTIVITY_DATE_TIME_START_ATTRIBUTE));
				}else if(attributes.getValue(AcquisitionConstants.ACTIVITY_NAME_ATTRIBUTE).equals(AcquisitionConstants.SABER_ACTIVITY_NAME)){
					activitySet.setSaberStartTime(attributes.getValue(AcquisitionConstants.ACTIVITY_DATE_TIME_START_ATTRIBUTE));
				}else if(attributes.getValue(AcquisitionConstants.ACTIVITY_NAME_ATTRIBUTE).equals(AcquisitionConstants.NOVUS_LOAD_ACTIVITY_NAME)){
					activitySet.setNovusLoadStartTime(attributes.getValue(AcquisitionConstants.ACTIVITY_DATE_TIME_START_ATTRIBUTE));
				}else if(attributes.getValue(AcquisitionConstants.ACTIVITY_NAME_ATTRIBUTE).equals(AcquisitionConstants.EHA_NOVUS_LOAD_ACTIVITY_NAME)){
					activitySet.setNovusLoadStartTime(attributes.getValue(AcquisitionConstants.ACTIVITY_DATE_TIME_START_ATTRIBUTE));
				}
			}
		}
		catch (Exception e)
		{
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

	public ActivitySet getActivitySet()
	{
		return activitySet;
	}
}



