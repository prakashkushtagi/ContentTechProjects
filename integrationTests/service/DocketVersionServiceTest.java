/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package service;

import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.service.DocketEntityService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional(transactionManager = "transactionManager")
public class DocketVersionServiceTest
{
	@Autowired
	private DocketService docketService;
	
	@Autowired
	private DocketEntityService docketEntityService;
	
	@Before
	public void setUp() 
	{

	}

	@Test
	public void testSaveDocketVersionService()
	{
		DocketEntity docketEntity = new DocketEntity();
		docketEntity.setPrimaryKey("TestNYLegacyId");  
		Court court = new Court();
		court.setPrimaryKey(95L);
		docketEntity.setCourt(court);
		docketEntity.setDocketNumber("ALBANY00002842011");
		Product product = new Product(ProductEnum.STATE);
		docketEntity.setProduct(product);
		docketEntity.setDocLoadedFlag("N");
		docketEntity.setPublishFlag("Y");
		docketEntity.setCaseTypeId(1L);
		try 
		{
			docketEntity = this.docketEntityService.saveDocketEntity(docketEntity);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
			Assert.fail();
		}
		DocketVersion docketVersion = new DocketVersion();
		DocketVersionKey docketVesionPk = new DocketVersionKey("TestNYLegacyId",9L,Phase.SOURCE,new Date());
		docketVersion.setPrimaryKey(docketVesionPk);  
		docketVersion.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		docketVersion.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		docketVersion.setAcquisitionTimestamp(new Date());
		docketVersion.setVersion(1L);
		docketVersion.setContentUuid(UUIDGenerator.createUuid());
		docketVersion.setOperationType("I");
		docketVersion.setProductId(3l);
		docketVersion.setCourt(court);
		docketVersion.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		try 
		{
			this.docketService.save(docketVersion);
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", 
				docketEntity.getCourt(), docketVersion.getPrimaryKey().getVendorKey(), docketEntity.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue(expectedDocketVersion.getPrimaryKey().getVendorKey() == 9L);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFindLatestVersion()
	{
		DocketEntity docketEntity = new DocketEntity();
		docketEntity.setPrimaryKey("TestNYLegacyId1");  
		Court court = new Court();
		court.setPrimaryKey(95L);
		docketEntity.setCourt(court);
		docketEntity.setDocketNumber("ALBANY00002842011");
		Product product = new Product(ProductEnum.STATE);
		docketEntity.setProduct(product);
		docketEntity.setDocLoadedFlag("N");
		docketEntity.setPublishFlag("Y");
		docketEntity.setCaseTypeId(1L);
		try 
		{
			docketEntity = this.docketEntityService.saveDocketEntity(docketEntity);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
			Assert.fail();
		}
		DocketVersion docketVersion = new DocketVersion();
		DocketVersionKey docketVesionPk = new DocketVersionKey("TestNYLegacyId1",9L,Phase.SOURCE,new Date());
		docketVersion.setPrimaryKey(docketVesionPk);  
		docketVersion.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		docketVersion.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);

		docketVersion.setAcquisitionTimestamp(new Date());
		docketVersion.setVersion(1L);
		docketVersion.setProductId(3l);
		docketVersion.setCourt(court);
		docketVersion.setContentUuid(UUIDGenerator.createUuid());
		docketVersion.setOperationType("I");
		docketVersion.setSourceFile(new File("TEST_SOURCE_FILENAME"));

		try 
		{
			docketService.save(docketVersion);
			Thread.sleep(10000);
			DocketVersion docketVersion1 = new DocketVersion();
			DocketVersionKey docketVesionPk1 = new DocketVersionKey("TestNYLegacyId1",9L,Phase.SOURCE,new Date());
			docketVersion1.setPrimaryKey(docketVesionPk1);  
			docketVersion.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			docketVersion.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);

			docketVersion1.setAcquisitionTimestamp(new Date());
			docketVersion1.setVersion(2L);
			docketVersion1.setProductId(3l);
			docketVersion1.setCourt(court);
			docketVersion1.setContentUuid(UUIDGenerator.createUuid());
			docketVersion1.setOperationType("U");
			docketVersion1.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			docketService.save(docketVersion1);
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId1", 
				docketEntity.getCourt(), docketVersion1.getPrimaryKey().getVendorKey() , docketEntity.getProduct().getId());
			
			Assert.assertTrue("legacy id should be TestNYLegacyI1d but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId1".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			Assert.assertTrue(expectedDocketVersion.getPrimaryKey().getVendorKey() == 9L);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFindLatestVersionNotFound()
	{
		DocketEntity docketEntity = new DocketEntity();
		docketEntity.setPrimaryKey("TestNYLegacyId2");  
		Court court = new Court();
		court.setPrimaryKey(95L);
		docketEntity.setCourt(court);
		docketEntity.setDocketNumber("ALBANY00002842011");
		Product product = new Product(ProductEnum.STATE);
		docketEntity.setProduct(product);
		docketEntity.setDocLoadedFlag("N");
		docketEntity.setPublishFlag("Y");
		docketEntity.setCaseTypeId(1L);
		try 
		{
			docketEntity = this.docketEntityService.saveDocketEntity(docketEntity);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
			Assert.fail();
		}
		DocketVersion docketVersion = new DocketVersion();
		DocketVersionKey docketVesionPk = new DocketVersionKey("TestNYLegacyId2",9L,Phase.SOURCE,new Date());
		docketVersion.setPrimaryKey(docketVesionPk);  
		docketVersion.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		docketVersion.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);

		docketVersion.setAcquisitionTimestamp(new Date());
		docketVersion.setVersion(1L);
		docketVersion.setContentUuid(UUIDGenerator.createUuid());
		docketVersion.setOperationType("I");
		docketVersion.setProductId(3l);
		docketVersion.setCourt(court);
		docketVersion.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		
		try 
		{
			docketService.save(docketVersion);
			Thread.sleep(10000);
			DocketVersion docketVersion1 = new DocketVersion();
			DocketVersionKey docketVesionPk1 = new DocketVersionKey("TestNYLegacyId2",9L,Phase.SOURCE,new Date());
			docketVersion1.setPrimaryKey(docketVesionPk1);  
			docketVersion.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			docketVersion.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);

			docketVersion1.setAcquisitionTimestamp(new Date());
			docketVersion1.setVersion(2L);
			docketVersion1.setProductId(3l);
			docketVersion1.setCourt(court);
			docketVersion1.setContentUuid(UUIDGenerator.createUuid());
			docketVersion1.setOperationType("U");
			docketVersion1.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			docketService.save(docketVersion1);
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyIdMissing", 
				docketEntity.getCourt(), docketVersion1.getPrimaryKey().getVendorKey() , docketEntity.getProduct().getId());
			Assert.assertNull("Not supposed to find docket version ", expectedDocketVersion);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
}
