/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public final class CharArrayDequeTest {

	@SuppressWarnings("static-method")
	@Test
	public void testConstructor() {
		final int segmentSize = 10;
		final CharArrayDeque deque = new CharArrayDeque(segmentSize);
		final long firstBytePosition = deque.getNextCharPosition();
		assertEquals(0,firstBytePosition);
		final String contents = deque.readAndDeleteToCharPosition(firstBytePosition);
		assertEquals("",contents);
	}

	// Also tests getNextBytePosition
	@SuppressWarnings("static-method")
	@Test
	public void testAppendByteArray() {
		checkAppendByteArray("abc");
		checkAppendByteArray("abcdefghij");
		checkAppendByteArray("abcdefghijk");
		checkAppendByteArray("abc","def");
		checkAppendByteArray("abc","defghijk");
		checkAppendByteArray(1,2,"abcd","defghijk");
	}
	
	private static void checkAppendByteArray(final String... inputs) {
		checkAppendByteArray(0,0,inputs);
	}
	
	private static void checkAppendByteArray(final int start, final int endTruncateLength, final String... inputs) {
		final int segmentSize = 10;
		final CharArrayDeque deque = new CharArrayDeque(segmentSize);
		final StringBuilder b = new StringBuilder();
		for (final String input: inputs) {
			final String partialInput = input.substring(start,input.length() - endTruncateLength);
			final char[] inputBytes = input.toCharArray();
			deque.appendCharArray(inputBytes, start, partialInput.length());
			b.append(partialInput);
		}
		final String expectedString = b.toString();
		final String stringRead = deque.readAndDeleteToCharPosition(expectedString.length());
		assertEquals(expectedString,stringRead);
		assertEquals(expectedString.length(),deque.getNextCharPosition());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testReadAndDeleteToBytePosition() {
		final int segmentSize = 10;
		final CharArrayDeque deque = new CharArrayDeque(segmentSize);
		final String input = "abcdefghijklmno";
		deque.appendCharArray(input.toCharArray(), 0, input.length());
		final String abcActual = deque.readAndDeleteToCharPosition(3);
		final String defActual = deque.readAndDeleteToCharPosition(6);
		final String ghijklActual = deque.readAndDeleteToCharPosition(12);
		assertEquals("abc",abcActual);
		assertEquals("def",defActual);
		assertEquals("ghijkl",ghijklActual);
	}

	@SuppressWarnings("static-method")
	@Test
	public void testDeleteToBytePosition() {
		final int segmentSize = 10;
		final CharArrayDeque deque = new CharArrayDeque(segmentSize);
		final String input = "abcdefghijklmno";
		deque.appendCharArray(input.toCharArray(), 0, input.length());
		deque.deleteToCharPosition(3);
		final String defActual = deque.readAndDeleteToCharPosition(6);
		deque.deleteToCharPosition(12);
		final String mnoActual = deque.readAndDeleteToCharPosition(15);
		assertEquals("def",defActual);
		assertEquals("mno",mnoActual);
	}
}
