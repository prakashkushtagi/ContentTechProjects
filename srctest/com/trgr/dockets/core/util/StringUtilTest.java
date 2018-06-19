/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import static com.trgr.dockets.core.util.StringUtil.colonSpaceConcat;
import static com.trgr.dockets.core.util.StringUtil.commaSpaceConcat;
import static com.trgr.dockets.core.util.StringUtil.concat;
import static com.trgr.dockets.core.util.StringUtil.linefeedConcat;
import static com.trgr.dockets.core.util.StringUtil.semicolonSpaceConcat;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public final class StringUtilTest {

	@SuppressWarnings("static-method")
	@Test
	public void testConcat() {
		checkConcat(null,true,"");
		checkConcat(null,false,"");
		checkConcat(".",true,"");
		checkConcat(".",false,"");
		checkConcat(".",true,"",(String)null);
		checkConcat(".",false,"",(String)null);
		checkConcat(".",true,"a","a");
		checkConcat(".",false,"a","a");
		checkConcat(".",true,"",null,null);
		checkConcat(".",false,".",null,null);
		checkConcat(".",true,"b",null,"b");
		checkConcat(".",false,".b",null,"b");
		checkConcat(".",true,"a","a",null);
		checkConcat(".",false,"a.","a",null);
		checkConcat(".",true,"a.b","a","b");
		checkConcat(".",false,"a.b","a","b");
	}
	
	private static void checkConcat(
			final String separator, final boolean skipNull, final String expectedResult, final Object... objects) {
		assertEquals(expectedResult, concat(separator, skipNull, objects));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testConcatWithSkipNullsDefault() {
		checkConcatWithSkipNullsDefault(null,"");
		checkConcatWithSkipNullsDefault(".","");
		checkConcatWithSkipNullsDefault(".","",(String)null);
		checkConcatWithSkipNullsDefault(".","a","a");
		checkConcatWithSkipNullsDefault(".","",null,null);
		checkConcatWithSkipNullsDefault(".","b",null,"b");
		checkConcatWithSkipNullsDefault(".","a","a",null);
		checkConcatWithSkipNullsDefault(".","a.b","a","b");
	}
	
	private static void checkConcatWithSkipNullsDefault(
			final String separator, final String expectedResult, final Object... objects) {
		assertEquals(expectedResult, concat(separator, objects));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testColonSpaceConcat() {
		checkColonSpaceConcat("");
		checkColonSpaceConcat("",(String)null);
		checkColonSpaceConcat("a","a");
		checkColonSpaceConcat("",null,null);
		checkColonSpaceConcat("b",null,"b");
		checkColonSpaceConcat("a","a",null);
		checkColonSpaceConcat("a: b","a","b");
	}
	
	private static void checkColonSpaceConcat(final String expectedResult, final Object... objects) {
		assertEquals(expectedResult, colonSpaceConcat(objects));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testCommaSpaceConcat() {
		checkCommaSpaceConcat("");
		checkCommaSpaceConcat("",(String)null);
		checkCommaSpaceConcat("a","a");
		checkCommaSpaceConcat("",null,null);
		checkCommaSpaceConcat("b",null,"b");
		checkCommaSpaceConcat("a","a",null);
		checkCommaSpaceConcat("a, b","a","b");
	}
	
	private static void checkCommaSpaceConcat(final String expectedResult, final Object... objects) {
		assertEquals(expectedResult, commaSpaceConcat(objects));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testLinefeedConcat() {
		checkLinefeedConcat("");
		checkLinefeedConcat("",(String)null);
		checkLinefeedConcat("a","a");
		checkLinefeedConcat("\n",null,null);
		checkLinefeedConcat("\nb",null,"b");
		checkLinefeedConcat("a\n","a",null);
		checkLinefeedConcat("a\nb","a","b");
	}
	
	private static void checkLinefeedConcat(final String expectedResult, final Object... objects) {
		assertEquals(expectedResult, linefeedConcat(objects));
	}

	@SuppressWarnings("static-method")
	@Test
	public void testSemicolonSpaceConcat() {
		checkSemicolonSpaceConcat("");
		checkSemicolonSpaceConcat("",(String)null);
		checkSemicolonSpaceConcat("a","a");
		checkSemicolonSpaceConcat("",null,null);
		checkSemicolonSpaceConcat("b",null,"b");
		checkSemicolonSpaceConcat("a","a",null);
		checkSemicolonSpaceConcat("a; b","a","b");
	}
	
	private static void checkSemicolonSpaceConcat(final String expectedResult, final Object... objects) {
		assertEquals(expectedResult, semicolonSpaceConcat(objects));
	}

}
