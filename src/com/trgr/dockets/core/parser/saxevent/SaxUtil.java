/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static java.lang.System.out;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.log4j.Logger;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.SaxEventParserContext;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.trgr.dockets.core.parser.saxevent.events.SaxEvent;
import com.trgr.dockets.core.util.ExceptionConsumer;

public final class SaxUtil {

	private static final Logger log = Logger.getLogger(SaxUtil.class);
	
	public static InputSource inputSource(final String input) {
		return new InputSource(new ByteArrayInputStream(input.getBytes()));}

	public static MutableBoolean parseHtml(
			final InputSource inputSource,
			final boolean useSeparateThread,
			final EntityResolver entityResolver,
			final SaxEventConsumer eventConsumer,
			final ExceptionConsumer exceptionHandler) throws Exception {
		final SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    final SAXParser saxParser = spf.newSAXParser();
	    final XMLReader xmlReader = saxParser.getXMLReader();
	    xmlReader.setContentHandler(new ConversionContentHandler(eventConsumer));
	    if (entityResolver != null) {
		    xmlReader.setEntityResolver(entityResolver);
	    }
	    final MutableBoolean completionStatus = new MutableBoolean();
	    if (useSeparateThread)
	    	new Thread(new Runnable() {
				@SuppressWarnings("synthetic-access")
				@Override
				public void run() {
					parseHtml(xmlReader,inputSource,exceptionHandler,completionStatus);
					
				}
			},"SAX Parser Thread").start();
	    else parseHtml(xmlReader,inputSource,exceptionHandler,completionStatus);
	    return completionStatus;}
	
	private static void parseHtml(
			final XMLReader xmlReader,
			final InputSource inputSource,
			final ExceptionConsumer exceptionHandler,
			final MutableBoolean completionStatus) {
		completionStatus.setFalse();
		try {
			xmlReader.parse(inputSource);
		}
		catch (final Exception e) {
			if (exceptionHandler != null) exceptionHandler.accept(e);
		}
		finally {
			completionStatus.setTrue();
		}
	}
	
	public static void reportEvent(final String testLabel, final SaxEvent event) {
		out.println(testLabel + ": " + event.getSummary());
	}
	
	public static SaxEventDrop startParser(final InputSource input, final EntityResolver entityResolver)
			throws Exception {
		final SaxEventDrop drop = new SaxEventDrop();
		try {
			final ExceptionConsumer exceptionConsumer = new ExceptionConsumer() {
				@Override
				public void accept(Exception t) {
					drop.terminate(t);
				}
			};
			drop.setCompletionStatus(parseHtml(input,true,entityResolver,drop,exceptionConsumer));
		} catch (final Exception exception) {
			drop.terminate(exception);
			throw exception;
		}
		return drop;}
	
	public static <T> T parseWithEventParser(
			final InputSource input, final EntityResolver entityResolver, final Parser<T> parser) throws Exception {
		SaxEventDrop drop = null;
		try {
			drop = startParser(input,entityResolver);
			final T result = new SaxEventParserContext(drop).runEventParser(parser);
			return result;
		}
		finally {
			Thread.sleep(10);
			if ((drop != null) && drop.isNotCompleted()) {
				Thread.sleep(1000);
				if (drop.isNotCompleted()) {
					log.error("SAX parsing thread may be blocked");
				}
			}
		}
	}
	
	public static void printParsedSummaries(final String parserName, final List<String> summaries) {
		for (final String summary: summaries) {
			out.println(parserName + " parser: " + summary);
		}
	}
	
	public static void parseWithEventParser(
			final InputSource input,
			final EntityResolver entityResolver,
			final String parserName,
			final Parser<List<String>> parser) throws Exception {
		final List<String> summaries = parseWithEventParser(input,entityResolver,parser);
		printParsedSummaries(parserName,summaries);}
}
