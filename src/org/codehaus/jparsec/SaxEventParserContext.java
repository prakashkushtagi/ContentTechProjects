/** Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package org.codehaus.jparsec;

import static java.lang.Integer.MAX_VALUE;

import java.util.List;

import org.codehaus.jparsec.error.Location;
import org.codehaus.jparsec.error.ParseErrorDetails;
import org.codehaus.jparsec.error.ParserException;
import org.xml.sax.SAXParseException;

import com.trgr.dockets.core.parser.saxevent.DocLocation;
import com.trgr.dockets.core.parser.saxevent.DocRange;
import com.trgr.dockets.core.parser.saxevent.SAXParserErrorDetails;
import com.trgr.dockets.core.parser.saxevent.SaxEventDrop;
import com.trgr.dockets.core.parser.saxevent.events.SaxEvent;
import com.trgr.dockets.core.util.OffsetIndex;
import com.trgr.dockets.core.util.Strings;

public final class SaxEventParserContext extends ParseContext {
	  
	private static final String USED_ON_SAX_EVENT_INPUT = "Cannot scan characters on SAX events.";
	
	private static final int MIN_POSITION_SUMMARIES = 100;
	private static final int MAX_POSITION_SUMMARIES = 1000; 
	private static final int MIN_EVENTS_TO_RETAIN = 10000;
	private static final int MAX_EVENTS_TO_RETAIN = 66000;

	private final SaxEventDrop fDrop;
	
	private final OffsetIndex<String> fPositionSummaries =
			new OffsetIndex<String>(MIN_POSITION_SUMMARIES,MAX_POSITION_SUMMARIES);
	
	private SaxEvent fCurrentEvent;
	
	private int fCurrentIndex = -1;
	
	private int fCurrentIndexInSource = fCurrentIndex;
	
	private Token fCurrentToken;
	
	private int fInputLength = MAX_VALUE;
	
	private boolean fTerminationReported = false;
	
	private final OffsetIndex<SaxEvent> saxEventBuffer =
			new OffsetIndex<SaxEvent>(MIN_EVENTS_TO_RETAIN,MAX_EVENTS_TO_RETAIN);

	public SaxEventParserContext(final SaxEventDrop drop) {
		super("",0,"Docket HTML", null);
		fDrop = drop;
	}
	
	public final void advanceEvent() {
		fCurrentIndex++;
		if (fCurrentIndex > fCurrentIndexInSource) {
			if (fTerminationReported) {
				fCurrentToken = null;
				return;
			}
			fCurrentEvent = fDrop.getNextObject();
			if (isTerminated()) {
				reportTermination();
				return;
			}
			fCurrentIndexInSource++;
			saxEventBuffer.addAndAdjust(fCurrentEvent);
		}
		else {
			updateCurrentEventFromBuffer();
		}
		if (fCurrentEvent == null) {
			if (fInputLength == MAX_VALUE) fInputLength = fCurrentIndex;
			reportTermination();
			return;
		}
		updatePositionInfo();
	}
	
	private boolean isTerminated() {
		return fDrop.isTerminated();
	}
	
	private void reportTermination() {
		fTerminationReported = true;
		fCurrentToken = null;
	}
	
	private void updatePositionInfo() {
		fCurrentToken = new Token(0,1,fCurrentEvent);
		final String positionSummary = fCurrentIndex + ": " + fCurrentEvent.getSummary();
		fPositionSummaries.addAndAdjust(positionSummary);
	}
	
	private void advanceToAt() {
		while (fCurrentIndex < at) advanceEvent();
	}

	@Override final String getInputName(final int pos) {
		return fPositionSummaries.entryNumberIsInRange(pos)
				? pastEof(pos) ? EOF : fPositionSummaries.getEntry(pos)
				: "Info for position " + pos + " no longer available";
	}

	@Override final boolean isEof() {
		syncEvent();
		return pastEof(at) || isTerminated();
	}
	
	private boolean pastEof(final int pos) {
		return pos >= fInputLength;
	}

	@Override final Token getToken() {
		syncEvent();
		return fCurrentToken;
	}
	
	private void syncEvent() {
		if (at < fCurrentIndex) {
			if (at < (int)saxEventBuffer.getPreviousEntryCount()) {
				throw new IllegalStateException("Cannot backtrack from " + fCurrentIndex + " to " + at);
			}
			fCurrentIndex = at;
			updateCurrentEventFromBuffer();
			updatePositionInfo();
		}
		else {
			advanceToAt();
		}
	}
	
	private void updateCurrentEventFromBuffer() {
		fCurrentEvent = saxEventBuffer.getEntry(fCurrentIndex);
	}

	@Override final char peekChar() {
		throw new IllegalStateException(USED_ON_SAX_EVENT_INPUT);
	}

	@Override final int toIndex(final int pos) {return pastEof(pos) ? fInputLength : pos;}

	@Override final CharSequence characters() {
		throw new IllegalStateException(USED_ON_SAX_EVENT_INPUT);
	}

	@SuppressWarnings("deprecation")
	public final <T> T runEventParser(final Parser<T> parser) {
		try {
			if (!applyWithExceptionWrapped(parser.followedBy(Parsers.EOF))) {
				ParseErrorDetails errorDetails = renderError();
				if (isTerminated()) {
					final Exception terminatingException = fDrop.getTerminatingException();
					if (terminatingException instanceof SAXParseException) {
						errorDetails = new SAXParserErrorDetails(errorDetails,(SAXParseException)terminatingException);
					}
					throw new ParserException(terminatingException,errorDetails, module, null);
				}
				final SaxEvent currentEvent = fDrop.getCurrentObject();
				final DocRange docRange = currentEvent.getDocRange();
				final DocLocation start = docRange.getStart();
				final Location location = new Location((int)start.getLineNumber(),start.getColumnNumber());
				final ParserException parserException = new ParserException(errorDetails, module, location);
				fDrop.terminate(parserException);
				throw parserException;
			}
			
			// Probably not necessary, but guarantee no threads are left blocked
			fDrop.terminate(null);
			
			return parser.getReturn(this);
		}
		catch (final RuntimeException e) {
			fDrop.terminate(e);
			throw e;
		}
	}

	private boolean applyWithExceptionWrapped(final Parser<?> parser) {
		try {
			return parser.apply(this);
		}
		catch (final RuntimeException e) {
			if (e instanceof ParserException) throw (ParserException) e;
			@SuppressWarnings("synthetic-access")
			final ParseErrorDetails details = new ParseErrorDetails() {

				@Override
				public int getIndex() {
					return fCurrentIndex;
				}

				@Override
				public String getEncountered() {
					return fCurrentEvent.toString();
				}

				@Override
				public List<String> getExpected() {
					return new Strings("Correct code");
				}

				@Override
				public String getUnexpected() {
					return "Incorrect code";
				}

				@Override
				public String getFailureMessage() {
					return fCurrentEvent.toString();
				}};
			@SuppressWarnings("deprecation")
			final ParserException wrapper = new ParserException(e, details, module,new Location(fCurrentIndex,1));
			throw wrapper;
		}
	}
}
