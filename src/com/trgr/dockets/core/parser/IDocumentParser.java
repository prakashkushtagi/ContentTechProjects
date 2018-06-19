/*
 * Copyright 2013: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import com.trgr.dockets.core.domain.NovusDocumentMetadata;

/**
 * IDocumentParser.
 * Interface for DocumentParser implementations.
 */
public interface IDocumentParser
{
	public List<NovusDocumentMetadata> parse(InputStream contentInputStream) throws XMLStreamException, IOException;
}
