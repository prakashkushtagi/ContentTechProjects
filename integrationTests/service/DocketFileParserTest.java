/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package service;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.trgr.dockets.core.util.DocketFileParser;

public class DocketFileParserTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testWellFormedDocument () throws Exception {
		InputStream is = getClass().getResourceAsStream("test_dockets.xml");
		
	 new DocketFileParser().buildSourceDocketMetadata(is, "test-file1", folder.toString()+ File.separator);
		
	}
	
	@Test
	public void testWellFormedSingleLine() throws Exception {
		InputStream is = getClass().getResourceAsStream("test_dockets1.xml");
		 new DocketFileParser().buildSourceDocketMetadata(is, "test-file2",folder.toString()+ File.separator);
		
	}
	
	@Test
	public void testIllFormedXMLDocketSingleLine() throws Exception {
		InputStream is = getClass().getResourceAsStream("test_dockets3.txt");		
		try {
		 new DocketFileParser().buildSourceDocketMetadata(is, "test-file",folder.toString());
			fail();
		}
		catch (XMLStreamException e){
		}
		
	}
	
	@Test
	public void testIllFormedXMLDocket2() throws Exception {
		InputStream is = getClass().getResourceAsStream("test_dockets4.txt");		
		try {
		 new DocketFileParser().buildSourceDocketMetadata(is, "test-file",folder.toString()+ File.separator);
			fail();
		} 
		catch (Exception e){
			if (!e.getMessage().equals("Illformed docket with nested dockets tags.")) {
				throw new Exception (e);
			}
		}
	}
}
