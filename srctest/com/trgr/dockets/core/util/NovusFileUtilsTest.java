package com.trgr.dockets.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class NovusFileUtilsTest {
	private static final String DOCKET_NUM = "MDL No. 1500";
	private static final String LEGACY_ID = "JPML1500";	
	
	@Rule
	public TemporaryFolder WORK_DIR = new TemporaryFolder();
	
	private static final String XML = 
"<n-document control='ADD' guid='I56b357832ead11e28a21ccb9036b2470'>" +
"  <n-metadata>" +
"	<metadata.block>" +
"	<md.identifiers>" +
"	<md.uuid>I56b357832ead11e28a21ccb9036b2470</md.uuid>" +
"	<md.legacy.id>"+LEGACY_ID+"</md.legacy.id>" +
"   <md.westlawids>" +
"   	<md.docketnum>"+DOCKET_NUM+"</md.docketnum>" +
"   </md.westlawids>" +
"   </md.identifiers>" +
"	</metadata.block>" +
"  </n-metadata>" +
"</n-document>";
	

	
	@Test
	public void testGetDocketNumber() throws Exception {
		String docketNumber = NovusFileUtils.getDocketNumber(XML);
		Assert.assertEquals(DOCKET_NUM, docketNumber);
	}
	
	@Test
	public void testGetLegacyId() throws Exception {
		String legacyId = NovusFileUtils.getLegacyId(XML);
		Assert.assertEquals(LEGACY_ID, legacyId);
		
	}
	
	@Test
	public void testOnlyAddExists() throws Exception{
		String expectedAddFileContents = "<n-load><n-document control=\"ADD\"><test>test1</test></n-document><n-document control=\"ADD\"><test>test2</test></n-document><n-document control=\"ADD\"><test>test3</test></n-document><n-document control=\"ADD\"><test>test4</test></n-document></n-load>";
		URL url = NovusFileUtils.class.getResource("novusFileWithAdds.xml");
		File sourceFile = new File(url.getFile());
		File addTargetFile = WORK_DIR.newFile("add.xml");
		File deleteTargetFile = WORK_DIR.newFile("delete.xml");
		NovusFileUtils.splitFileBasedOnControl(sourceFile, addTargetFile, deleteTargetFile);
		Assert.assertEquals(0, FileUtils.sizeOf(deleteTargetFile));
		Assert.assertEquals(expectedAddFileContents, FileUtils.readFileToString(addTargetFile));
	}
	
	@Test
	public void testOnlyDeletesExists() throws Exception{
		String expectedDeleteFileContents = "<n-load><n-document control=\"DEL\"/><n-document control=\"DEL\"/><n-document control=\"DEL\"/><n-document control=\"DEL\"/></n-load>";
		URL url = NovusFileUtils.class.getResource("novusFileWithDeletes.xml");
		File sourceFile = new File(url.getFile());
		File addTargetFile = WORK_DIR.newFile("add.xml");
		File deleteTargetFile = WORK_DIR.newFile("delete.xml");
		NovusFileUtils.splitFileBasedOnControl(sourceFile, addTargetFile, deleteTargetFile);
		Assert.assertEquals(0, FileUtils.sizeOf(addTargetFile));
		Assert.assertEquals(expectedDeleteFileContents, FileUtils.readFileToString(deleteTargetFile));
	}
	
	@Test
	public void testAddsAndDeletesExists() throws Exception{
		String expectedAddFileContents = "<n-load><n-document control=\"ADD\"><test>test1</test></n-document><n-document control=\"ADD\"><test>test2</test></n-document></n-load>";
		String expectedDeleteFileContents = "<n-load><n-document control=\"DEL\"/><n-document control=\"DEL\"/></n-load>";
		URL url = NovusFileUtils.class.getResource("novusFileWithAddsAndDeletes.xml");
		File sourceFile = new File(url.getFile());
		File addTargetFile = WORK_DIR.newFile("add.xml");
		File deleteTargetFile = WORK_DIR.newFile("delete.xml");
		NovusFileUtils.splitFileBasedOnControl(sourceFile, addTargetFile, deleteTargetFile);
		Assert.assertEquals(expectedAddFileContents, FileUtils.readFileToString(addTargetFile));
		Assert.assertEquals(expectedDeleteFileContents, FileUtils.readFileToString(deleteTargetFile));
	}
	
	@Test
	public void testMergeNovusFiles() throws IOException{
		File outputFile = WORK_DIR.newFile("output.xml");
		File in1 = WORK_DIR.newFile("in1.xml");
		File in2 = WORK_DIR.newFile("in2.xml");
		
		FileUtils.writeStringToFile(in1, "<n-load><n-document>test1</n-document></n-load>");
		FileUtils.writeStringToFile(in2, "<n-load><n-document>test2</n-document></n-load>");
		NovusFileUtils.mergeNovusFiles(outputFile, in1, in2);
		String expectedOutputFileContent = "<n-load><n-document>test1</n-document></n-load>";
		Assert.assertEquals(expectedOutputFileContent, FileUtils.readFileToString(outputFile));
	}

}
