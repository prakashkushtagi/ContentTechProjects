/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.dao.DocketDao;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.service.DocketEntityService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional(transactionManager = "transactionManager")
public class DocketServiceTest
{
	@Autowired
	private DocketEntityService docketEntityService;
	
	@Autowired
	private DocketDao docketDao;

	@Before
	public void setUp() 
	{

	}

	@Test
	public void testSaveDocketEntityService()
	{
		
		DocketEntity docketEntity = new DocketEntity();
		docketEntity.setPrimaryKey("TestNYLegacyId");  
		Court court = new Court();
		court.setPrimaryKey(95L);
		docketEntity.setCourt(court);
		docketEntity.setDocketNumber("ALBANY00002842011");
		Product product = new Product(ProductEnum.STATE);
		docketEntity.setProduct(product);
		docketEntity.setPublishFlag("Y");
		docketEntity.setDocLoadedFlag("N");
		docketEntity.setCaseTypeId(1L);
		try 
		{
			docketEntity = this.docketEntityService.saveDocketEntity(docketEntity);
			DocketEntity expectedDocketVersion = this.docketEntityService.findDocketByLegacyId("TestNYLegacyId");
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey()));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testFindCourtConfig() {
		docketDao.findRpxConfigByCourtId(101L);
	}
}
