/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.service.ContentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional
public class ContentServiceTest
{
	@Autowired
	private ContentService contentService;
	
	@Before
	public void setUp() 
	{

	}

	@Test
	public void testSaveSourceContentService()
	{
		
		Content content = new Content();
		content.setArtifactDescriptorId(10510L);
		content.setArtifactPhaseId(2L);
		content.setArtifactUuid("");
		content.setCreatedBy("INTEGRATION_TESTS");
		content.setCreatedOn(new Date());
		content.setDbPartition("LITIGATOR_CONTENT");
		content.setFormatTypeId(5L);
		content.setTerminatedFlag("N");
		try 
		{
			content = this.contentService.saveContent(content);
			Content expectedContent = this.contentService.findContentByUuid(content.getContentUuid());
			Assert.assertTrue("Content id should be " + content.getContentUuid() + " but is " + 
					expectedContent.getContentUuid() , expectedContent.getContentUuid().equals( content.getContentUuid()));
		} 
		catch (Exception e) 
		{
			Assert.fail();
		}
	}
	
}
