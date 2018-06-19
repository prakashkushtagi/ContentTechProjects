/**Copyright 2015: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.content.service.PublishContentService;
import com.trgr.dockets.core.domain.NovusDocketMetadata;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.entity.Status;
import com.trgr.dockets.core.service.ContentService;
import com.trgr.dockets.core.service.ContentVersionService;
import com.trgr.dockets.core.service.DocketEntityService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional
public class PublishContentServiceTest {

	private DocketEntity docketEntity;
	private PublishingRequest publishingRequest;
	private NovusDocketMetadata novusDocketMetadata;
	
	@Autowired
	PublishContentService publishContentService;
	@Autowired
	DocketService docketService;
	@Autowired
	ContentService contentService;
	@Autowired
	ContentVersionService contentVersionService;
	@Autowired
	DocketEntityService docketEntityService;
	@Autowired
	DocketHistoryService docketHistoryService;

	
//	@Before
//	public void setUp() {
//		// Instantiate all mock objects and 'class under test' here
//
//	
//	}

	@Test
	public void testCreatePublishHistory() {

		Product product = new Product();
		product.setPrimaryKey(ProductEnum.STATE);
		UUID pubReqUUID = UUIDGenerator.createUuid();
		String testNovusGuid = UUIDGenerator.createUuid().toString();
		publishingRequest = new PublishingRequest(
				pubReqUUID, "TestPublishingContentService",
				"auto", false, false, false,
				new Date(), null, null,
				null,
				RequestInitiatorTypeEnum.DAILY, product,
				WorkflowTypeEnum.STATEDOCKET_PREDOCKET, RequestTypeEnum.WCW,
				new Status(StatusEnum.RUNNING), null,null, null, null, 11L, false, null, null, null, null);
		Court court = new Court();
		court.setPrimaryKey(98L);
//		docketEntity = new DocketEntity("KGLegacyId", "DnKGid", court);
//		docketService.save(docketEntity);

		novusDocketMetadata = new NovusDocketMetadata(publishingRequest);
		novusDocketMetadata.setLegacyId("CASTANISLAUS1402768");
		novusDocketMetadata.setVendorId(11);
		novusDocketMetadata.setNovusGuid(testNovusGuid);
		
		// Run test
		try {
			publishContentService.createPublishHistory(novusDocketMetadata);
			
			docketEntity = docketService.findDocketByPrimaryKey("CASTANISLAUS1402768");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String strDate = sdf.format(docketEntity.getLastPublishDate());
			Calendar cal = Calendar.getInstance();
			String expectedDate = sdf.format(cal.getTime());

			Assert.assertEquals(expectedDate, strDate);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}