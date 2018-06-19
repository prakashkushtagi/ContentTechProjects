/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public final class IndentableStrBuilderTest {

	@SuppressWarnings("static-method")
	@Test
	public void testAppendLabeledValue() {
		final IndentableStrBuilder b = new IndentableStrBuilder();
		b.appendLabeledValue("label", "value");
		assertEquals("label: value\n", b.toString());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testAppendLines() {
		final IndentableStrBuilder b = new IndentableStrBuilder();
		b.appendLines("123");
		assertEquals("123\n", b.toString());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testAppendSection() {
		final IndentableStrBuilder b = new IndentableStrBuilder();
		b.appendSection("header",new Runnable() {

			@Override
			public void run() {
				b.appendLines("123");
			}
		});
		assertEquals("header\n    123\n", b.toString());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testAppendSectionWithNoHeader() {
		final IndentableStrBuilder b = new IndentableStrBuilder();
		b.appendSection(new Runnable() {

			@Override
			public void run() {
				b.appendLines("123");
			}
		});
		assertEquals("    123\n", b.toString());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testToString() {
		final IndentableStrBuilder b = new IndentableStrBuilder();
		b.appendLines("123");
		assertEquals("123\n", b.toString());
	}

}
