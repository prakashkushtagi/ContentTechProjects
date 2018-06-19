/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited. 
 */

package com.trgr.dockets.core.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class XMLDoctorTest {
	
	@Test
	public void testAttributeCorrector() {
		String input = "<r>"
				+ "<e a1=\"g\" a2=\"b \" a\" a3=\"b & a\">txt</e>"
				+ "</r>";
		
		String actual = XMLDoctor.correctAttributes(input);
		
		String expected = 
			"<r>"
				+ "<e a1=\"g\" a2=\"b &quot; a\" a3=\"b &amp; a\">txt</e>"
			+ "</r>";
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testAttributeCorrector2() {
		String input = 	
			"<r>"
				+ "<e a1=\"g\" a4=\"b<a\" a5=\"b>a\" a6=\"b&#15;a\">txt</e>"
			+ "</r>";
		
		String actual = XMLDoctor.correctAttributes(input);
		
		String expected = 
			"<r>"
				+ "<e a1=\"g\" a4=\"b&lt;a\" a5=\"b&gt;a\" a6=\"b&#15;a\">txt</e>"
			+ "</r>";
		
		assertEquals(expected, actual);
	}

	@Test
	public void testAttributeCorrectSingleElement() {
		String input = 
				"<r>"
					+ "<e a1=\"a\"b\" a2=\"a\'b\"/>"
				+ "</r>";
			
			String actual = XMLDoctor.correctAttributes(input);
			
			String expected = 
				"<r>"
					+ "<e a1=\"a&quot;b\" a2=\"a&apos;b\"/>"
				+ "</r>";
			
			assertEquals(expected, actual);
	}
	
	@Test
	public void testAttributeEqualsSignInAttribute() {
		String input = 
				"<r>"
					+ "<e a1=\"a=b\" a2=\"a=b\"b=c\"/>"
				+ "</r>";
			
			String actual = XMLDoctor.correctAttributes(input);
			
			String expected = 
				"<r>"
					+ "<e a1=\"a=b\" a2=\"a=b&quot;b=c\"/>"
				+ "</r>";
			
			assertEquals(expected, actual);
	}
}
