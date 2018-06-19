/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package service;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.NovusLoadFile;
import com.trgr.dockets.core.entity.NovusLoadFileKey;
import com.trgr.dockets.core.service.NovusLoadFileService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "LoadMonitorLtcMessageServiceTest-context.xml" })
@Transactional
public class NovusLoadFileServiceTest
{
	@Autowired
	private NovusLoadFileService novusLoadFileServiceImpl;
	
	@Before
	public void setUp() 
	{

	}

	@Test
	public void testFindNovusLoadFileService()
	{
		NovusLoadFile novusLoadFile = new NovusLoadFile();
//		String batchId, String subBatchId, String filename
		NovusLoadFileKey primaryKey = new NovusLoadFileKey("TestBatchId","TestSubBatchId","Myfilename.xml");
		novusLoadFile.setPrimaryKey(primaryKey);
		novusLoadFile.setBatchId("TestBatchId");
		novusLoadFile.setSubBatchId("TestSubBatchId");
		novusLoadFile.setStatus(StatusEnum.PENDING);
		novusLoadFile.setLoadRequestId("testLoadRequestId");
		novusLoadFile.setFilepath("/dockets/test/filepath");
		novusLoadFile.setCollectionId(1l);
		try 
		{
			this.novusLoadFileServiceImpl.saveNovusLoadFile(novusLoadFile);
			NovusLoadFile expectedNovusLoadFile =  this.novusLoadFileServiceImpl.findByBatchIdSubBatchIdFileName("TestBatchId", "TestSubBatchId", "Myfilename.xml");
			Assert.assertTrue("filepath should be /dockets/test/filepath but is " + expectedNovusLoadFile.getFilepath()
					 , "/dockets/test/filepath".equals(expectedNovusLoadFile.getFilepath()));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFindNovusLoadFileServiceByPrimaryKey()
	{
		NovusLoadFile novusLoadFile = new NovusLoadFile();
//		String batchId, String subBatchId, String filename
		NovusLoadFileKey primaryKey = new NovusLoadFileKey("TestBatchId","TestSubBatchId","Myfilename.xml");
		novusLoadFile.setPrimaryKey(primaryKey);
		novusLoadFile.setBatchId("TestBatchId");
		novusLoadFile.setSubBatchId("TestSubBatchId");
		novusLoadFile.setStatus(StatusEnum.PENDING);
		novusLoadFile.setLoadRequestId("testLoadRequestId");
		novusLoadFile.setFilepath("/dockets/test/filepath");
		novusLoadFile.setCollectionId(1l);
		try 
		{
			this.novusLoadFileServiceImpl.saveNovusLoadFile(novusLoadFile);
			NovusLoadFile expectedNovusLoadFile =  this.novusLoadFileServiceImpl.findNovusLoadFileByPrimaryKey(primaryKey);
			Assert.assertTrue("filepath should be /dockets/test/filepath but is " + expectedNovusLoadFile.getFilepath()
					 , "/dockets/test/filepath".equals(expectedNovusLoadFile.getFilepath()));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
	@Test
	public void testFindNovusLoadFilesByBatchIdAndRequestId()
	{
		NovusLoadFile novusLoadFile = new NovusLoadFile();

		NovusLoadFileKey primaryKey = new NovusLoadFileKey("TestBatchId","TestSubBatchId","Myfilename.xml");
		novusLoadFile.setPrimaryKey(primaryKey);
		novusLoadFile.setBatchId("TestBatchId");
		novusLoadFile.setSubBatchId("TestSubBatchId");
		novusLoadFile.setStatus(StatusEnum.PENDING);
		novusLoadFile.setLoadRequestId("testLoadRequestId");
		novusLoadFile.setFilepath("/dockets/test/filepath");
		novusLoadFile.setCollectionId(1l);
		
		NovusLoadFile novusLoadFile_2 = new NovusLoadFile();
		NovusLoadFileKey primaryKey_2 = new NovusLoadFileKey("TestBatchId_2","TestSubBatchId_2","Myfilename_2.xml");
		novusLoadFile_2.setPrimaryKey(primaryKey_2);
		novusLoadFile_2.setBatchId("TestBatchId_2");
		novusLoadFile_2.setSubBatchId("TestSubBatchId_2");
		novusLoadFile_2.setStatus(StatusEnum.PENDING);
		novusLoadFile_2.setLoadRequestId("testLoadRequestId");
		novusLoadFile_2.setFilepath("/dockets/test/filepath_2");
		novusLoadFile_2.setCollectionId(1l);
		
		
		try 
		{
			this.novusLoadFileServiceImpl.saveNovusLoadFile(novusLoadFile);
			this.novusLoadFileServiceImpl.saveNovusLoadFile(novusLoadFile_2);
			List <NovusLoadFile> expectedNovusLoadFiles =  this.novusLoadFileServiceImpl.findNovusLoadFilesByBatchIdAndRequestId("TestBatchId", "testLoadRequestId");
			Assert.assertTrue(expectedNovusLoadFiles.size() == 1);
			Assert.assertTrue(expectedNovusLoadFiles.get(0).getBatchId().equalsIgnoreCase("TestBatchId"));
			Assert.assertTrue(expectedNovusLoadFiles.get(0).getLoadRequestId().equalsIgnoreCase("testLoadRequestId"));

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Test
	public void testFindWhenMultipleNovusLoadFileService()
	{
		NovusLoadFile novusLoadFile = new NovusLoadFile();
//		String batchId, String subBatchId, String filename
		NovusLoadFileKey primaryKey = new NovusLoadFileKey("TestBatchId","TestSubBatchId1","Myfilename.xml");
		novusLoadFile.setPrimaryKey(primaryKey);
		novusLoadFile.setBatchId("TestBatchId");
		novusLoadFile.setSubBatchId("TestSubBatchId");
		novusLoadFile.setStatus(StatusEnum.PENDING);
		novusLoadFile.setLoadRequestId("testLoadRequestId");
		novusLoadFile.setFilepath("/dockets/test/filepath");
		novusLoadFile.setCollectionId(1l);
		
		NovusLoadFile novusLoadFile2 = new NovusLoadFile();
//		String batchId, String subBatchId, String filename
		NovusLoadFileKey primaryKey2 = new NovusLoadFileKey("TestBatchId","TestSubBatchId2","Myfilename2.xml");
		novusLoadFile2.setPrimaryKey(primaryKey2);
		novusLoadFile2.setBatchId("TestBatchId");
		novusLoadFile2.setSubBatchId("TestSubBatchId2");
		novusLoadFile2.setStatus(StatusEnum.PENDING);
		novusLoadFile2.setLoadRequestId("testLoadRequestId");
		novusLoadFile2.setFilepath("/dockets2/test2/filepath2");
		novusLoadFile2.setCollectionId(2l);
		
		NovusLoadFile novusLoadFile3 = new NovusLoadFile();
//		String batchId, String subBatchId, String filename
		NovusLoadFileKey primaryKey3 = new NovusLoadFileKey("TestBatchId","TestSubBatchId3","Myfilename2.xml");
		novusLoadFile3.setPrimaryKey(primaryKey3);
		novusLoadFile3.setBatchId("TestBatchId");
		novusLoadFile3.setSubBatchId("TestSubBatchId3");
		novusLoadFile3.setStatus(StatusEnum.PENDING);
		novusLoadFile3.setLoadRequestId("testLoadRequestId");
		novusLoadFile3.setFilepath("/dockets2/test2/filepath2");
		novusLoadFile3.setCollectionId(2l);
		try 
		{
			this.novusLoadFileServiceImpl.saveNovusLoadFile(novusLoadFile);
			this.novusLoadFileServiceImpl.saveNovusLoadFile(novusLoadFile2);
			this.novusLoadFileServiceImpl.saveNovusLoadFile(novusLoadFile3);
			List<NovusLoadFile> expectedNovusLoadFile =  this.novusLoadFileServiceImpl.findByBatchIdCollectionIdFileName("TestBatchId", 2l, "Myfilename2.xml");
			Assert.assertTrue("filepath should be TestSubBatchId2 but is " + expectedNovusLoadFile.get(0).getSubBatchId()
					 , "TestSubBatchId2".equals(expectedNovusLoadFile.get(0).getSubBatchId()));
			Assert.assertTrue("subBatch should be /dockets2/test2/filepath2 but is " + expectedNovusLoadFile.get(0).getFilepath()
					 , "/dockets2/test2/filepath2".equals(expectedNovusLoadFile.get(0).getFilepath()));
			Assert.assertTrue("filepath should be /dockets2/test2/filepath2 but is " + expectedNovusLoadFile.get(1).getFilepath()
					 , "/dockets2/test2/filepath2".equals(expectedNovusLoadFile.get(1).getFilepath()));
			Assert.assertTrue("filepath should be TestSubBatchId3 but is " + expectedNovusLoadFile.get(1).getSubBatchId()
					 , "TestSubBatchId3".equals(expectedNovusLoadFile.get(1).getSubBatchId()));
			expectedNovusLoadFile =  this.novusLoadFileServiceImpl.findByBatchIdCollectionIdFileName("TestBatchId", 1l, "Myfilename.xml");
			Assert.assertTrue("filepath should be /dockets/test/filepath but is " + expectedNovusLoadFile.get(0).getFilepath()
					 , "/dockets/test/filepath".equals(expectedNovusLoadFile.get(0).getFilepath()));
		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}
