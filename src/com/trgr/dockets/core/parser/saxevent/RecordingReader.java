/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static com.trgr.dockets.core.parser.saxevent.CharArrayDeque.DEFAULT_CHARSET_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public final class RecordingReader extends Reader {
	
	private static final char LINEFEED = '\n';
	
	private static final char CARRIAGE_RETURN = '\r';
	
	private final Reader base;
	
	private final CharArrayDeque charArrayDeque;
	
	private final LinePositionIndex linePositionIndex = new LinePositionIndex();
	
	private final char[] singleCharBuffer = new char[1];
	
	private boolean linefeedJustRead = false;
	
	private boolean carriageReturnJustRead = false;
	
	public RecordingReader(final InputStream inputStream, final String charsetName) {
		base = new InputStreamReader(inputStream,Charset.forName(charsetName));
		charArrayDeque = new CharArrayDeque(8192);
	}
	
	public RecordingReader(final Reader reader) {
		base = reader;
		charArrayDeque = new CharArrayDeque(8192);
	}
	
	public RecordingReader(final InputStream base) {
		this(base,DEFAULT_CHARSET_NAME);
	}
	
	@Override
	public final synchronized void close() throws IOException {
		base.close();
	}
	
	@Override
	public final synchronized void mark(final int readlimit) throws IOException {
		base.mark(readlimit);
	}
	
	@Override
	public final synchronized boolean markSupported() {
		return base.markSupported();
	}
	
	private long getCharPosition(final DocLocation docLocation) {
		return linePositionIndex.getCharPosition(docLocation);
	}
	
	public final synchronized String getText(final DocLocation start, final DocLocation end) {
		final long startCharPosition = getCharPosition(start);
		final long endCharPosition = getCharPosition(end);
		linePositionIndex.deleteToLineNumber(end.getLineNumber());
		charArrayDeque.deleteToCharPosition(startCharPosition);
		return charArrayDeque.readAndDeleteToCharPosition(endCharPosition);
	}
	
	private void monitorCharsRead(final char[] charArray, final int off, final int len) {
		final long baseCharPosition = charArrayDeque.getNextCharPosition() - off;
		charArrayDeque.appendCharArray(charArray,off,len);
		final int end = off + len;
		int i = off;
		while (i < end) {
			final char c = charArray[i];
			if (c == CARRIAGE_RETURN) {
				setCrlfJustRead(true,baseCharPosition,i,true);
			}
			else if (c == LINEFEED) {
				setCrlfJustRead(false,baseCharPosition,i,false);
			}
			else {
				setCrlfJustRead(true,baseCharPosition,i,false);
			}
			i++;
		}
	}
	
	private void setCrlfJustRead(
			final boolean checkCarriageReturn,
			final long baseCharPosition,
			final int i,
			final boolean newCarriageReturnJustRead) {
		if ((checkCarriageReturn && carriageReturnJustRead) || linefeedJustRead) {
			linePositionIndex.add(baseCharPosition + i);
		}
		carriageReturnJustRead = newCarriageReturnJustRead;
		linefeedJustRead = !checkCarriageReturn;
	}
	
	private void monitorCharsRead(final char[] charArray, final int len) {
		monitorCharsRead(charArray,0,len);
	}
	
	@Override
	public final synchronized int read() throws IOException {
		final int c = base.read();
		if (c > -1) {
			singleCharBuffer[0] = (char)c;
			monitorCharsRead(singleCharBuffer,1);
		}
		return c;
	}
	
	@Override
	public final synchronized int read(final char[] charArray) throws IOException {
		final int charsRead = base.read(charArray);
		monitorCharsRead(charArray,charsRead);
		return charsRead;
	}
	
	@Override
	public final synchronized int read(final char[] charArray, final int off, final int len) throws IOException {
		final int charsRead = base.read(charArray, off, len);
		if (charsRead > 0) monitorCharsRead(charArray,off,charsRead);
		return charsRead;
	}
	
	@Override
	public final synchronized void reset() throws IOException {
		throw new UnsupportedOperationException();
	}
}
