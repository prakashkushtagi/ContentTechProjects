package com.trgr.dockets.core.content.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trgr.dockets.core.entity.WiplaNovusContent;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/service/DocketServiceIntegrationTest-context.xml", "/service/DocketPersistence-context.xml" })
public class WiplaNovusContentServiceIntegrationTests {

	@Autowired
	private WiplaNovusContentService wiplaNovusContentService;

	@Test
	public void wiplaNovusContentSaveTest() 
	{
		WiplaNovusContent wiplaNovusContentSave = new WiplaNovusContent();
		wiplaNovusContentSave.setlegacyId("1234");
		wiplaNovusContentSave.setContent("<patent.information.block><patent.block></patent.block></patent.information.block>");
		
		wiplaNovusContentService.saveOrUpdate(wiplaNovusContentSave);
	
		WiplaNovusContent wiplaNovusContentFind = wiplaNovusContentService.findContentByLegacyId("1234");
		
		assertNotNull(wiplaNovusContentFind);
		assertNotNull(wiplaNovusContentFind.getLastUpdatedTimestamp());
		assertEquals(wiplaNovusContentSave.toString(), wiplaNovusContentFind.toString());
	}

	@Test
	public void wiplaNovusContentFindTest()
	{
		final WiplaNovusContent wiplaNovusContent = wiplaNovusContentService.findContentByLegacyId("1234");
		
		if (wiplaNovusContent != null) {
			wiplaNovusContentService.delete(wiplaNovusContent);
			
			final WiplaNovusContent wiplaNovusContent2 = wiplaNovusContentService.findContentByLegacyId("1234");
			
			assertNull(wiplaNovusContent2);
		}
	}
}
