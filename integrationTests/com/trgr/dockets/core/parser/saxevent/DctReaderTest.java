/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 *  Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.parser.saxevent;

import static com.trgr.dockets.core.parser.saxevent.DctReader.readXml;

import java.io.File;

import org.junit.Test;

public class DctReaderTest {

	@Test
	public void testDctReader() throws Exception {
		final File inputFile =
			new File("integrationTests\\resources\\TX-EDCT6_07CV00111.xml");
		readXml(inputFile);
	}
}
