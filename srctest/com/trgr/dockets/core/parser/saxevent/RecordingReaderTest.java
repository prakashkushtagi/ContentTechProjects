/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.easymock.EasyMock;
import org.junit.Test;

import com.lowagie.text.pdf.codec.Base64.InputStream;

@SuppressWarnings({ "static-method", "resource" })
public final class RecordingReaderTest {

	@Test
	public void testClose() {
		final InputStream mockInputStream = EasyMock.createMock(InputStream.class);
		try {
			
			mockInputStream.close();
			EasyMock.expectLastCall();
			EasyMock.replay(mockInputStream);
			
			final RecordingReader stream = new RecordingReader(mockInputStream);
			stream.close();
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCloseException() {
		final InputStream mockInputStream = EasyMock.createMock(InputStream.class);
		final IOException expected = new IOException();
		try {
			
			mockInputStream.close();
			EasyMock.expectLastCall().andThrow(expected);
			EasyMock.replay(mockInputStream);
			
			final RecordingReader stream = new RecordingReader(mockInputStream);
			stream.close();
			
			fail("Exception should have been thrown");
			
		} catch (final IOException actual) {
			assertSame(expected, actual);
		}
	}

	@Test
	public void testMarkSupported() {
		final InputStream mockInputStream = EasyMock.createMock(InputStream.class);
		final boolean expected = false;
		
		EasyMock.expect(mockInputStream.markSupported()).andReturn(expected);
		EasyMock.replay(mockInputStream);
		
		final RecordingReader stream = new RecordingReader(mockInputStream);
		final boolean actual = stream.markSupported();
		
		assertEquals(expected, actual);
	}

	@Test
	public void testReadResults() {
		final String expected = "abcdefghijklmno";
		final ByteArrayInputStream bais = new ByteArrayInputStream(expected.getBytes());
		final RecordingReader stream = new RecordingReader(bais);
		final int length = expected.length();
		try {
			for (int i = 0; i < length; i++) {
				stream.read();
			}
			final String actual = stream.getText(new DocLocation(1,1), new DocLocation(1,length + 1));
			assertEquals(expected, actual);
		} catch (final IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testReadArrayResults() {
		final String expected = "abcdefghijklmno";
		final ByteArrayInputStream bais = new ByteArrayInputStream(expected.getBytes());
		final RecordingReader stream = new RecordingReader(bais);
		final int length = expected.length();
		final char[] charArray = new char[length];
		try {
			stream.read(charArray);
			final String actual = stream.getText(new DocLocation(1,1), new DocLocation(1,length + 1));
			assertEquals(expected, actual);
			assertEquals(expected, new String(charArray));
		} catch (final IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testReadArraySegmentResults() {
		final String expected = "abcdefghijklmno";
		final ByteArrayInputStream bais = new ByteArrayInputStream(expected.getBytes());
		final RecordingReader stream = new RecordingReader(bais);
		final int length = expected.length();
		final char[] byteArray = new char[100];
		final int offset = 10;
		try {
			stream.read(byteArray,offset,length);
			final String actual = stream.getText(new DocLocation(1,1), new DocLocation(1,length + 1));
			assertEquals(expected, actual);
			assertEquals(expected, new String(byteArray,offset,length));
		} catch (final IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testReadMultiline() {
		final String source = "abcde\nfghij\r\nklmno\npqrst\nuvwxyz";
		final ByteArrayInputStream bais = new ByteArrayInputStream(source.getBytes());
		final RecordingReader stream = new RecordingReader(bais);
		final int length = source.length();
		final char[] byteArray = new char[length];
		try {
			stream.read(byteArray);
			final String actual1 = stream.getText(new DocLocation(1,3), new DocLocation(2,3));
			final String actual2 = stream.getText(new DocLocation(2,3), new DocLocation(3,3));
			final String actual3 = stream.getText(new DocLocation(4,3), new DocLocation(5,3));
			assertEquals(source.substring(2,8), actual1);
			assertEquals(source.substring(8,15), actual2);
			assertEquals(source.substring(21,27), actual3);
		} catch (final IOException e) {
			fail(e.getMessage());
		}
	}

}
