package com.trgr.dockets.core.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;

public class DocumentParserFactoryTest {

	@Test
	public void testBKR(){
		DocumentParserFactory testDocumentParser = DocumentParserFactory.getInstance();
		IDocumentParser iDocParse = testDocumentParser.createDocumentParser(ProductEnum.FBR.name(), "testfilename", 0L);
		
		assertTrue(iDocParse instanceof BKRNovusDocumentParser);
	}
	
	@Test
	public void testState(){
		DocumentParserFactory testDocumentParser = DocumentParserFactory.getInstance();
		IDocumentParser iDocParse = testDocumentParser.createDocumentParser(ProductEnum.STATE.name(), "testfilename", 11L);
		
		assertTrue(iDocParse instanceof StateNovusDocketHandler);
	}
	
	@Test
	public void testJpml(){
		DocumentParserFactory testDocumentParser = DocumentParserFactory.getInstance();
		IDocumentParser iDocParse = testDocumentParser.createDocumentParser(ProductEnum.JPML.name(), "testfilename", 7L);
		
		assertTrue(iDocParse instanceof FederalNovusDocketHandler);
	}
}
