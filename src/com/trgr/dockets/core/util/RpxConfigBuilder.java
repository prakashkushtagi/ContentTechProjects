/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import com.trgr.dockets.core.CoreConstants;

public class RpxConfigBuilder 
{

	private XMLEventWriter writer;
	private boolean relloaderProperties = false;
	
	public RpxConfigBuilder(XMLEventWriter writer)
	{
		this.writer = writer;
	}
	
	public void processEvent(XMLEvent event,String releaseFlag) throws XMLStreamException, FactoryConfigurationError, IOException
	{
		if (event.isStartElement())
		{
			//document start
			if (event.asStartElement().getName().getLocalPart().equals(CoreConstants.WORKFLOW_CONFIG_COMPONENT))
			{
				//getting all the attributes
				Attribute name = event.asStartElement().getAttributeByName(new QName(CoreConstants.WORKFLOW_CONFIG_COMPONENT_NAME_ATTR));
				
				if(null!=name && null!=name.getValue() && name.getValue().equals(CoreConstants.WORKFLOW_CONFIG_COMPONENT_REL_LOADER))
				{
					relloaderProperties = true;
				}
			}
			else if(relloaderProperties && event.asStartElement().getName().getLocalPart().equals(CoreConstants.WORKFLOW_CONFIG_PARAMETER))
			{
				//getting all the attributes
				Attribute name = event.asStartElement().getAttributeByName(new QName(CoreConstants.WORKFLOW_CONFIG_COMPONENT_NAME_ATTR));
				if(null!=name && null!=name.getValue() && name.getValue().equals(CoreConstants.WORKFLOW_CONFIG_COMPONENT_RELEASE_FLAG))
				{
					relloaderProperties = false;
					Attribute value = event.asStartElement().getAttributeByName(new QName(CoreConstants.WORKFLOW_CONFIG_COMPONENT_VALUE_ATTR));
					if(null!=value && null!=value.getValue() )
					{
						XMLEventFactory eventFactory = XMLEventFactory.newInstance();
						if(releaseFlag.startsWith("rf"))
						{
							releaseFlag = releaseFlag.replace("rf", "").trim();
						}
				        value = eventFactory.createAttribute(CoreConstants.WORKFLOW_CONFIG_COMPONENT_VALUE_ATTR,releaseFlag);
				        List<?> attributeList = Arrays.asList(name,value);
						event = eventFactory.createStartElement("", null, CoreConstants.WORKFLOW_CONFIG_PARAMETER, attributeList.iterator(), null);
					}
				}
				
			}
			else if(event.isStartDocument())
			{
				
			}
		}
		writer.add(event);
		writer.flush();
	}

	public void writeConfig(XMLEvent event) throws XMLStreamException 
	{
		writer.add(event);
		writer.flush();
		if (event.asEndElement().getName().getLocalPart().equals(CoreConstants.WORKFLOW_CONFIG))
		{
			writer.close();
			writer = null;
		}
	}
}
