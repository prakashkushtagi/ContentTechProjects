/**Copyright 2015: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.domain.DroppedDocketHistory;
import com.trgr.dockets.core.entity.BatchDroppedDocket;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.DocketVersionRelHistoryKey;
import com.trgr.dockets.core.entity.Process;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional(transactionManager = "transactionManager")
public class DocketHistoryServiceTest
{
	@Autowired
	private DocketHistoryService docketHistoryService;
	@Autowired
	private DocketService docketService;

	@Before
	public void setUp() 
	{

	}

	@Test
	public void testSaveDocketHistoryService()
	{
		
		DocketHistory docketHistory = new DocketHistory();
		docketHistory.setDocketHistoryId(UUIDGenerator.createUuid().toString());
		docketHistory.setLegacyId("TestNYLegacyId");  
		docketHistory.setType(DocketHistoryTypeEnum.ACQUISITION_HISTORY);
		docketHistory.setRequestName("TEST_REQUEST_NAME");
		docketHistory.setTimestamp(new Date());
		docketHistory.setFile(new File("TEST_SOURCE_FILENAME"));
		try 
		{
			docketHistory = this.docketHistoryService.save(docketHistory);
			List<DocketHistory> expectedDocketHistory = this.docketHistoryService.findDocketHistoryByLegacyId("TestNYLegacyId");
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketHistory.get(0).getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketHistory.get(0).getLegacyId()));
			Assert.assertTrue("type should be ACQUISITION_HISTORY but is " + expectedDocketHistory.get(0).getType()
					 , DocketHistoryTypeEnum.ACQUISITION_HISTORY.equals(expectedDocketHistory.get(0).getType()));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFindByLegacyIdType()
	{
		DocketHistory docketHistory = new DocketHistory();
		docketHistory.setDocketHistoryId(UUIDGenerator.createUuid().toString());
		docketHistory.setLegacyId("TestNYLegacyId");  
		docketHistory.setType(DocketHistoryTypeEnum.ACQUISITION_HISTORY);
		docketHistory.setRequestName("TEST_REQUEST_NAME");
		docketHistory.setTimestamp(new Date());
		docketHistory.setFile(new File("TEST_SOURCE_FILENAME"));
		
		DocketHistory docketHistory2 = new DocketHistory();
		docketHistory2.setDocketHistoryId(UUIDGenerator.createUuid().toString());
		docketHistory2.setLegacyId("TestNYLegacyId");  
		docketHistory2.setType(DocketHistoryTypeEnum.CONVERSION_HISTORY);
		docketHistory2.setRequestName("TEST_REQUEST_NAME");
		docketHistory2.setTimestamp(new Date());
		docketHistory2.setFile(new File("TEST_SOURCE_FILENAME"));
		try 
		{
			docketHistory2 = this.docketHistoryService.save(docketHistory2);
			docketHistory = this.docketHistoryService.save(docketHistory);
			List<DocketHistory> expectedDocketHistory = this.docketHistoryService.findDocketHistoryByLegacyIdType("TestNYLegacyId", DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketHistory.get(0).getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketHistory.get(0).getLegacyId()));
			Assert.assertTrue("type should be CONVERSION_HISTORY but is " + expectedDocketHistory.get(0).getType()
					 , DocketHistoryTypeEnum.CONVERSION_HISTORY.equals(expectedDocketHistory.get(0).getType()));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFindByLegacyId()
	{
		DocketHistory docketHistory = new DocketHistory();
		docketHistory.setDocketHistoryId(UUIDGenerator.createUuid().toString());
		docketHistory.setLegacyId("TestNYLegacyId");  
		docketHistory.setType(DocketHistoryTypeEnum.ACQUISITION_HISTORY);
		docketHistory.setRequestName("TEST_REQUEST_NAME");
		docketHistory.setTimestamp(new Date());
		docketHistory.setFile(new File("TEST_SOURCE_FILENAME"));
		
		DocketHistory docketHistory2 = new DocketHistory();
		docketHistory2.setDocketHistoryId(UUIDGenerator.createUuid().toString());
		docketHistory2.setLegacyId("TestNYLegacyId");  
		docketHistory2.setType(DocketHistoryTypeEnum.CONVERSION_HISTORY);
		docketHistory2.setRequestName("TEST_REQUEST_NAME");
		docketHistory2.setTimestamp(new Date());
		docketHistory2.setFile(new File("TEST_SOURCE_FILENAME"));
		try 
		{
			docketHistory2 = this.docketHistoryService.save(docketHistory2);
			docketHistory = this.docketHistoryService.save(docketHistory);
			List<DocketHistory> expectedDocketHistory = this.docketHistoryService.findDocketHistoryByLegacyId("TestNYLegacyId");
			Assert.assertTrue("Two with the legacyId TestNYLegacyId should be returned",expectedDocketHistory.size()==2);
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketHistory.get(0).getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketHistory.get(0).getLegacyId()));
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketHistory.get(1).getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketHistory.get(1).getLegacyId()));
			int foundAcquisitionHistory = 0;
			int foundConversionHistory = 0;
			for(DocketHistory docketHistoryObj: expectedDocketHistory)
			{
				if(DocketHistoryTypeEnum.ACQUISITION_HISTORY.equals(docketHistoryObj.getType()))
				{
					foundAcquisitionHistory++;
				}
				else if(DocketHistoryTypeEnum.CONVERSION_HISTORY.equals(docketHistoryObj.getType()))
				{
					foundConversionHistory++;
				}
					
			}
			if(foundAcquisitionHistory!=1 || foundConversionHistory!=1)
			{
				Assert.fail("Expected one of each Acquisition and Conversion History but #Acquisition :: " + foundAcquisitionHistory + "  and #Conversion :: " + foundConversionHistory);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	

	@Test
	public void testSaveDocketVersionRelHistory() {
		DocketVersionRelHistoryKey pk = new DocketVersionRelHistoryKey("TestNYDocketHistoryId", UUIDGenerator.createUuid().toString());
		DocketVersionRelHistory expected = new DocketVersionRelHistory(pk, "TestNYLegacyId");
	
		expected = this.docketHistoryService.save(expected);
		DocketVersionRelHistory actual = this.docketHistoryService.findDocketVersionRelHistoryByPrimaryKey(pk);
		Assert.assertEquals(expected,  actual);
	}
	
	//need to fix this later on
	//@Test
	public void testSaveNegativeDocketHistoryService()
	{
		try 
		{ 
		String legacyId = "testLegacyId";
		Process process = new Process(new UUID("I94133750d74811e195b8efc44a0a2d73"));   // must be an existing process ID
		BatchDroppedDocket badDocket = new BatchDroppedDocket(null, "TestDocketNum", "Test error", process, "TestCourtNum", legacyId);
		
		DroppedDocketHistory ddh = new DroppedDocketHistory(badDocket, legacyId);
		
		PublishingRequest publishingRequest = new PublishingRequest(null, "TEST_REQUEST_NAME", "JUNITS", false, false, false, new Date(), new Date(), new Date(), null, null, null, null, null, null, null,null,null, null, 0L, false, null, null, null, null);
			DocketHistory docketHistory = this.docketService.saveNegativeHistory(ddh, publishingRequest, DocketHistoryTypeEnum.ACQUISITION_HISTORY);
			List<DocketHistory> actualDocketHistory = this.docketHistoryService.findDocketHistoryByLegacyId(legacyId);
			Assert.assertNotNull(actualDocketHistory);
			Assert.assertEquals(legacyId, docketHistory.getLegacyId());
			Assert.assertEquals(DocketHistoryTypeEnum.ACQUISITION_HISTORY, docketHistory.getType());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testSaveDocketVersionRel()
	{
		
		DocketVersionRel docketVersionRel = new DocketVersionRel();
		docketVersionRel.setRelId(UUIDGenerator.createUuid().toString());
		docketVersionRel.setLegacyId("TestNYLegacyId");  
		docketVersionRel.setType(DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
		docketVersionRel.setSourceGuid("TEST_SOURCE_GUID");
		docketVersionRel.setTargetGuid("TEST_TARGET_GUID");
		docketVersionRel.setSourceVersion(999l);
		docketVersionRel.setTargetVersion(999l);
		docketVersionRel.setRelTimestamp(new Date());
	
		try 
		{
			docketVersionRel = this.docketHistoryService.save(docketVersionRel);
			DocketVersionRel expectedDocketVersionRel = this.docketHistoryService.findDocketVersionRel(docketVersionRel.getRelId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersionRel.getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersionRel.getLegacyId()));
			Assert.assertTrue("type should be SOURCE_TO_JAXML but is " + expectedDocketVersionRel.getType()
					 , DocketVersionRelTypeEnum.SOURCE_TO_JAXML.equals(expectedDocketVersionRel.getType()));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
}
