/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package com.trgr.dockets.core.util;

import java.io.File;
import java.io.InputStream;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;



public class RpxConfigProcessorTest {
	
	@Rule
	public TemporaryFolder WORK_DIR = new TemporaryFolder();
	
	@Test
	public void testprocess() throws Exception {
		RpxConfigProcessor rpxConfigProcessor = new RpxConfigProcessor();
//		File file1WorkflowConfigFile = WORK_DIR.newFile("File1.xml");
		InputStream inputFileStream = RpxConfigProcessorTest.class.getResourceAsStream("dockets_stadkt_default_config.txt");
		InputStream inputFileStream2 = RpxConfigProcessorTest.class.getResourceAsStream("dockets_stadkt_rf001_config.txt");
		
		File file1WorkflowConfigFile = File.createTempFile("sample-input1", ".txt");
		FileUtils.writeStringToFile(file1WorkflowConfigFile, IOUtils.toString(inputFileStream));
		
		File file2DynamicConfigFile = File.createTempFile("sample-input2", ".txt");
		FileUtils.writeStringToFile(file2DynamicConfigFile, IOUtils.toString(inputFileStream2));

		String releaseOverrideString = "rf001";
		boolean docketNumber = rpxConfigProcessor.process(file1WorkflowConfigFile, file2DynamicConfigFile, releaseOverrideString);
		Assert.assertEquals(true, docketNumber);
	}
	


}
