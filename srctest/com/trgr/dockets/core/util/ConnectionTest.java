/**
 * Copyright 2017: Thomson Reuters. All Rights Reserved. Proprietary and 
 * Confidential information of Thomson Reuters. Disclosure, Use or Reproduction 
 * without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.util;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
/*Copyright 2015: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
import org.junit.runner.RunWith;

import com.trgr.dockets.core.util.ConnectionUtility;
@RunWith(JUnitParamsRunner.class)
public class ConnectionTest {

	// used for junit parameterized test
	public static Object[] $(Object... params) {
		return params;
	}
	
	public Object[] parametersHappyPath() {
		return $(
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=10040"),
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=973"),
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=786"),
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=20303"),
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=789"),
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=042"),
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=1362"),
				$("http://trdocs.int.westgroup.com/GetPDFDetail?court=N_DDECHANCERY&docketNo=2402")
				);
	};
	
	@Test
	@Parameters(method = "parametersHappyPath")
	public void testFilterConnectionUtility(String url) throws Exception{
		final int maxTries = 3;
		int retry = 0;
		String response = null;
		while (response == null && retry < maxTries) {
			try {
				response = ConnectionUtility.doGetRequestWithReadTimeout(new URL(url));
			} catch (Exception e) {
				e.printStackTrace();
				retry++;
				if (retry == maxTries) {
					throw e;
				}
			}
		}
		assertNotNull(response);
	}
}
