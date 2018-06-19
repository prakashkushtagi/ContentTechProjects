/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.ContentVersionPK;
import com.trgr.dockets.core.service.ContentVersionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional
public class ContentVersionServiceTest
{
	@Autowired
	private ContentVersionService contentVersionService;
	
	@Before
	public void setUp() 
	{

	}

	@Test
	public void testSaveSourceContentService()
	{
		
		ContentVersion contentVersion = new ContentVersion();
		ContentVersionPK contentVesionPk = new ContentVersionPK("INTEGRATION_TEST_INITIAL_VERSION",1L);
		contentVersion.setPrimaryKey(contentVesionPk);  
		contentVersion.setCreatedBy("INTEGRATION_TESTS");
		contentVersion.setCreatedOn(new Date());
		try 
		{
			contentVersion = this.contentVersionService.saveContentVersion(contentVersion);
			List<ContentVersion> expectedContentVersions = this.contentVersionService.findContentVersionByUuid("INTEGRATION_TEST_INITIAL_VERSION");
			Assert.assertTrue("Content id should be INTEGRATION_TEST_INITIAL_VERSION but is " + expectedContentVersions.get(0).getPrimaryKey().getContentUuid()
					 , "INTEGRATION_TEST_INITIAL_VERSION".equals(expectedContentVersions.get(0).getPrimaryKey().getContentUuid()));
		} 
		catch (Exception e) 
		{
			Assert.fail();
		}
	}
	
	
	@Test
	public void testSaveContent()
	{
		ContentVersion contentVersion = new ContentVersion();
		ContentVersionPK contentVesionPk = new ContentVersionPK("INTEGRATION_TEST_INITIAL_VERSION1",1L);
		contentVersion.setPrimaryKey(contentVesionPk);  
		contentVersion.setCreatedBy("INTEGRATION_TESTS");
		contentVersion.setCreatedOn(new Date());
		
		StringBuffer data = new StringBuffer("");   
		
		InputStream in = getClass().getResourceAsStream("LCI_Oregon_Insert.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    	
    	String tempOutput = null;
    	
    	try 
    	{
			while((tempOutput=reader.readLine())!=null)
			{	        		
				data.append(tempOutput);
			}
		
		
   		contentVersion.setFileimage(data.toString().getBytes());
   		contentVersion = this.contentVersionService.saveContentVersion(contentVersion);
   		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
