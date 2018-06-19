/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.transaction.annotation.Transactional;

/**
 * An ItemReader implementation which accepts a novus file and return one nDocument for
 * every read call.
 * 
 * @author C047166
 *
 */
public class NDocumentItemReader implements ItemReader<String>
{

	private Logger logger = Logger.getLogger(NDocumentItemReader.class);
	
	private File novusFile;
	private XMLStreamReader xsr;
	private InputStream novusFileInputStream = null;
	private Transformer transformer;
	
	/**
	 * @param novusFile
	 */
	public NDocumentItemReader(File novusFile)
	{
		super();
		this.novusFile = novusFile;
		init(novusFile);
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemReader#read()
	 */
	@Override
	@Transactional
	public String read()
	{
		String nDocumentString = null;
		try
		{
			// if element is n-document
			if(xsr.isStartElement()){
				StringWriter writer = new StringWriter();
    			transformer.transform(new StAXSource(xsr), new StreamResult(writer));
    			nDocumentString = writer.toString();
    			if(xsr.isWhiteSpace()){
					xsr.nextTag();
				}
			}
		}
		catch(Exception e){
			logger.error("Unexpected error occurred while reading n-document elements from the file " + novusFile.getPath());
		}
		return nDocumentString;
	}
	
	private void init(File novusFile){
		try
		{
			novusFileInputStream = new FileInputStream(novusFile);
			XMLInputFactory xif = XMLInputFactory.newInstance();
			xsr = xif.createXMLStreamReader(novusFileInputStream);
			xsr.nextTag(); // Advance to n-load element
			xsr.nextTag(); // Advance to first n-document element
			TransformerFactory tf = TransformerFactory.newInstance();
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		}
		catch (Exception e)
		{
			logger.error("Unexpected error occurred while initializing the xml sream reader for file " + novusFile.getPath());
		}
	}
}
