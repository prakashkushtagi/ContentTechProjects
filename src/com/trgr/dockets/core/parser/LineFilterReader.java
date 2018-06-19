/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.parser;

import java.io.BufferedReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.xml.sax.InputSource;

import com.trgr.dockets.core.util.StringToStringFunction;

public final class LineFilterReader extends FilterReader {
	
	private final BufferedReader containedReader;
	
	private final StringToStringFunction lineFilter;
	
	private String line;
	
	private int linePos;

	public LineFilterReader(
			final BufferedReader containedReader,
			final StringToStringFunction lineFilter,
			final String initialLine) {
		super(containedReader);
		this.containedReader = containedReader;
		this.lineFilter = lineFilter;
		line = initialLine;
	}

	public LineFilterReader(
			final Reader containedReader,
			final StringToStringFunction lineFilter,
			final String initialLine) {
		this(getBufferedReader(containedReader),lineFilter,initialLine);
	}
	
	public LineFilterReader(
			final InputStream containedStream,
			final String charsetName,
			final StringToStringFunction lineFilter,
			final String initialLine) {
		this(new InputStreamReader(containedStream,Charset.forName(charsetName)),lineFilter,initialLine);
	}
	
	private static BufferedReader getBufferedReader(final Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
	}
	
	public static InputSource getFilteredSource(final InputSource source, final StringToStringFunction lineFilter) {
		final Reader reader = source.getCharacterStream();
		final LineFilterReader filteredReader = (reader != null)
				? new LineFilterReader(reader,lineFilter,null)
				: new LineFilterReader(source.getByteStream(),"UTF-8",lineFilter,null);
		return new InputSource(filteredReader);
	}
	
	@Override
	public final int read() throws IOException {
		if (line == null) {
			final String rawLine = containedReader.readLine();
			if (rawLine == null) {
				return -1;
			}
			line = lineFilter.apply(rawLine);
			if (line == null) {
				return -1;
			}
			linePos = 0;
		}
		if (linePos < line.length()) {
			final char result = line.charAt(linePos);
			linePos++;
			return result;
		}
		line = null;
		return '\n';
	}
	
	@Override
	public final int read(final char[] cbuf, final int off, final int len) throws IOException {
		int numberRead = 0;
		while (numberRead < len) {
			final int nextChar = read();
			if (nextChar == -1) {
				return (numberRead == 0) ? -1 : numberRead;
			}
			cbuf[off + numberRead] = (char)nextChar;
			numberRead++;
		}
		return numberRead;
	}
	
	@Override
	public final int read(final char[] cbuf) throws IOException {
		return read(cbuf,0,cbuf.length);
	}
	
	@Override
	public final int read(final CharBuffer target) throws IOException {
		final int readResult = read();
		if (readResult < 0) {
			return -1;
		}
		target.put((char)readResult);
		return 1;
	}
}
