package com.trgr.dockets.core.parser.saxevent;

import java.util.List;

import org.codehaus.jparsec.error.ParseErrorDetails;
import org.xml.sax.SAXParseException;

public final class SAXParserErrorDetails implements ParseErrorDetails {
	
	public static final String MALFORMED_XML = "Malformed xml or html";
	
	private final ParseErrorDetails base;
	
	private final SAXParseException saxException;
	
	public SAXParserErrorDetails(final ParseErrorDetails base, final SAXParseException saxException) {
		this.base = base;
		this.saxException = saxException;
	}

	@Override
	public final int getIndex() {
		return base.getIndex();
	}

	@Override
	public final String getEncountered() {
		return base.getEncountered();
	}

	@Override
	public final List<String> getExpected() {
		return base.getExpected();
	}

	@Override
	public final String getUnexpected() {
		return base.getUnexpected();
	}

	@Override
	public final String getFailureMessage() {
		return MALFORMED_XML + " at " + saxException.getLineNumber() + ":" + saxException.getColumnNumber() + ".";
	}

}
