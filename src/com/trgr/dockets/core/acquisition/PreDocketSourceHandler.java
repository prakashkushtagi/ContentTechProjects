package com.trgr.dockets.core.acquisition;


import org.xml.sax.Attributes;

import com.trgr.dockets.core.domain.PreDocket;
import com.west.exception.SAXException;
import com.west.utilities.xml.XmlHandler;


public class PreDocketSourceHandler extends XmlHandler
{
	private PreDocket preDocket = null;
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
	 * @see         
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		this.cdata.setLength(0);
		if (qName.equals(AcquisitionConstants.PRE_DOCKET_ELEMENT))
		{
			preDocket = new PreDocket();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		cdata.append(new String(ch, start, length));
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
		if (qName.equals(AcquisitionConstants.CASE_TITLE_ELEMENT)){
			preDocket.setCaseTitle(cdata.toString());
		} else	if (qName.equals(AcquisitionConstants.FILING_DATE_ELEMENT)){
			preDocket.setFilingDate(cdata.toString());
		} else	if (qName.equals(AcquisitionConstants.CASE_SUB_TYPE_ELEMENT)){
			preDocket.setCaseSubType(cdata.toString());
		} else	if (qName.equals(AcquisitionConstants.PRE_DOCKET_CASE_TYPE)){
			preDocket.setCaseType(cdata.toString());
		}
	}

	public PreDocket getPreDocket()
	{
		return preDocket;
	}
	
}



