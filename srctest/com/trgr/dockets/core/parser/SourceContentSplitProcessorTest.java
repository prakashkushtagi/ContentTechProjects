package com.trgr.dockets.core.parser;

import junit.framework.Assert;

import org.junit.Test;

public class SourceContentSplitProcessorTest {
	@Test
	public void prepareDocketNumberTest() {
		String unFormattedDocketNumber = "3:07BK00671";
		String expectedFormattedDocketNumber = "3:2007BK00671";
		SourceContentSplitProcessor scspContentSplitProcessor = new SourceContentSplitProcessor();
		String formattedDocketNumber = scspContentSplitProcessor.prepareDocketNumber(unFormattedDocketNumber);
		Assert.assertEquals(expectedFormattedDocketNumber, formattedDocketNumber);	
	}
	
}
