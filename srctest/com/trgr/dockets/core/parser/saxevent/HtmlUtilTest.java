/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.BODY;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.HTML;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.TABLE;
import static com.trgr.dockets.core.parser.saxevent.HtmlUtil.getTag;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.trgr.dockets.core.parser.saxevent.HtmlUtil;

@SuppressWarnings("static-method")
public final class HtmlUtilTest {

	@Test
	public void testHtmlUtil() {
		assertEquals("", new HtmlUtil().getString());
	}

	@Test
	public void testGetTag() {
		assertEquals("<html>", getTag(HTML, true));
		assertEquals("</body>", getTag(BODY, false));
	}
	
	@Test
	public void testAppendLines() {
		checkAppendLines("line","line");
		checkAppendLines("line1line2","line1","line2");
	}
	
	private void checkAppendLines(final String expected, final String... lines) {
		final HtmlUtil html = new HtmlUtil();
		html.appendLines(lines);
		assertEquals(expected,html.getString());
	}
	
	@Test
	public void testAppendTag() {
		checkAppendTag(HTML,true,"<html>");
		checkAppendTag(BODY,false,"</body>");
	}
	
	private static void checkAppendTag(final String name, final boolean start, final String expected) {
		final HtmlUtil html = new HtmlUtil();
		html.appendTag(name, start);
		assertEquals(expected,html.getString());
	}

	@Test
	public void testAppendElement() {
		final HtmlUtil html = new HtmlUtil();
		html.appendElement(TABLE,new Runnable() {

			@Override
			public void run() {
				html.appendLines("content");
			}
		});
		assertEquals("<table>content</table>", html.getString());
	}
	
	@Test
	public void testAppendDetail() {
		final HtmlUtil html = new HtmlUtil();
		html.appendDetail("body");
		assertEquals("<td>body</td>",html.getString());
	}
	
	@Test
	public void testAppendRow() {
		final HtmlUtil html = new HtmlUtil();
		html.appendRow("detail1", "detail2");
		assertEquals("<tr><td>detail1</td><td>detail2</td></tr>",html.getString());
	}
	
	@Test
	public void testAppendTable() {
		final HtmlUtil html = new HtmlUtil();
		html.appendTable("detail1a","detail1b","detail2a","detail2b");
		assertEquals(
				"<table><tr><td>detail1a</td><td>detail1b</td></tr><tr><td>detail2a</td><td>detail2b</td></tr></table>",
				html.getString());
	}
}
