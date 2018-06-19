/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.content.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.thomson.judicial.dockets.bankruptcycontentservice.common.bean.NovusDocketMetadata;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.service.DocketEntityService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/service/DocketServiceIntegrationTest-context.xml", "/service/DocketPersistence-context.xml" })
@Transactional(transactionManager = "transactionManager")
public class LoadContentServiceImplTest {

	@Autowired
	private DocketEntityService docketEntityService;

	@Autowired
	private DocketService service;

	@Autowired
	private DocketHistoryService docketHistoryService;

	private PublishingRequest pubReq;
	private List<NovusDocketMetadata> ndmList;
	private LoadContentServiceImpl lcs;

	private List<DocketEntity> docketEntities;

	@Test
	public void dummyTest() {
		Assert.assertTrue(true);
		}

//	@Before
//	public void setup() throws NumberFormatException, UUIDException {
//
//		docketEntities = new ArrayList<DocketEntity>();
//		// Retrieve publishing request
//		pubReq = service.findPublishingRequestByPrimaryKey(new UUID("Ic0000000000000000000000000000082"));
//
//		DocketEntity docketEntity = new DocketEntity();
//		docketEntity.setPrimaryKey("TestNYLegacyId");
//		Court court = new Court();
//		court.setPrimaryKey(95L);
//		CollectionEntity collectionEntity = new CollectionEntity(14L, "N_DNYUPSTATE");
//		court.setCollection(collectionEntity);
//		docketEntity.setCourt(court);
//		docketEntity.setDocketNumber("ALBANY00002842011");
//		Product product = new Product(ProductEnum.STATE);
//		docketEntity.setProduct(product);
//		docketEntity.setPublishFlag("Y");
//		docketEntity.setDocLoadedFlag("N");
//		docketEntity.setCaseTypeId(1L);
//
//		docketEntities.add(docketEntity);
//
//		try {
//			docketEntity = this.docketEntityService.saveDocketEntity(docketEntity);
//			DocketEntity expectedDocketVersion = this.docketEntityService.findDocketByLegacyId("TestNYLegacyId");
//			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey(),
//					"TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey()));
//		} catch (Exception e) {
//			e.printStackTrace();
//			Assert.fail();
//		}
//
//		ndmList = generateNDM();
//		lcs = new LoadContentServiceImpl();
//		lcs.setDocketService(service);
//		lcs.setDocketHistoryService(docketHistoryService);
//	}
//
//	@Test
//	public void noDrops() throws NumberFormatException, UUIDException {
//		for (DocketEntity de : docketEntities) {
//			DocketVersion dv = new DocketVersion();
//			dv.setDocket(de);
//			dv.setVersion(2L);
//			dv.setProductId(3L);
//			dv.setCourt(de.getCourt());
//			// Make up some UUID that refers to the content table
//			dv.setContentUuid(new UUID("Ic000000200d000000000000000000082"));
//
//			DocketVersionKey dvk = new DocketVersionKey();
//			dvk.setPhase(Phase.NOVUS);
//			dvk.setLegacyId(de.getPrimaryKey());
//			dvk.setVendorKey(7L);
//			dvk.setVersionDate(new Date());
//			dv.setPrimaryKey(dvk);
//
//			this.service.save(dv);
//		}
//
//		List<NovusDocketMetadata> ndmOutput = lcs.createLoadHistory(ndmList, pubReq, new Date(), "testing-testing-testing", "");
//		Assert.assertEquals(0, ndmOutput.size());
//
//	}
//
//	@Test
//	public void someDropsWithEmtpyVendorCode() throws NumberFormatException, UUIDException {
//		for (DocketEntity de : docketEntities) {
//			de.setPublishFlag("Y");
//			de.setDocLoadedFlag("N");
//			this.docketEntityService.saveDocketEntity(de);
//			DocketVersion dv = new DocketVersion();			
//			dv.setDocket(de);
//			dv.setVersion(2L);
//			dv.setProductId(3L);
//			dv.setCourt(de.getCourt());
//			// Make up some UUID that refers to the content table
//			dv.setContentUuid(new UUID("Ic000000200d000000000000000000082"));
//
//			DocketVersionKey dvk = new DocketVersionKey();
//			dvk.setPhase(Phase.NOVUS);
//			dvk.setLegacyId(de.getPrimaryKey());
//			dvk.setVendorKey(7L);
//			dvk.setVersionDate(new Date());
//			dv.setPrimaryKey(dvk);
//
//			this.service.save(dv);
//		}
//		// Pass in some novus docket metadata where there isn't a docket entity in the table.
//		NovusDocketMetadata ndm2 = new NovusDocketMetadata("testing-docket-two");
//		ndm2.setLegacyId("TestNYLegacyId2");
//		
//		// Guid here doesn't really matter, just needs to be non-null
//		ndm2.setUuid("Icd000032000000000000000000000082");
//		ndm2.setControl("add");
//
//		List<NovusDocketMetadata> novusList = new ArrayList<NovusDocketMetadata>(ndmList);
//		novusList.add(ndm2);
//		List<NovusDocketMetadata> ndmOutput = lcs.createLoadHistory(novusList, pubReq, new Date(), "testing-testing-testing", "");
//		Assert.assertEquals(1, ndmOutput.size());
//	}
//
//	@Test
//	public void allDrops() {
//		List<NovusDocketMetadata> ndmOutput = lcs.createLoadHistory(ndmList, pubReq, new Date(), "testing-testing-testing", "");
//		Assert.assertEquals(1, ndmOutput.size());
//	}
//	
//	@Test
//	public void testDroppedReasonWithPublishFlag() throws NumberFormatException, UUIDException{
//		for (DocketEntity de : docketEntities) {
//			de.setPublishFlag("N");
//			de.setDocLoadedFlag("N");
//			this.docketEntityService.saveDocketEntity(de);
//			
//			DocketVersion dv = new DocketVersion();
//			dv.setDocket(de);
//			dv.setVersion(2L);
//			dv.setProductId(3L);
//			dv.setCourt(de.getCourt());
//			// Make up some UUID that refers to the content table
//			dv.setContentUuid(new UUID("Ic000000200d000000000000000000082"));
//
//			DocketVersionKey dvk = new DocketVersionKey();
//			dvk.setPhase(Phase.NOVUS);
//			dvk.setLegacyId(de.getPrimaryKey());
//			dvk.setVendorKey(7L);
//			dvk.setVersionDate(new Date());
//			dv.setPrimaryKey(dvk);
//
//			this.service.save(dv);
//		}
//		List<NovusDocketMetadata> ndmOutput = lcs.createLoadHistory(ndmList, pubReq, new Date(), "testing-testing-testing", "BOGUS ERROR HERE");
//		Assert.assertEquals(0, ndmOutput.size());
//		
//		
//		
//	}
//	
//
//	@Test
//	public void testNullDocketEntityDrop() {
//		// Pass in some novus docket metadata where there isn't a docket entity in the table.
//		NovusDocketMetadata ndm2 = new NovusDocketMetadata("testing-docket-two");
//		ndm2.setLegacyId("TestNYLegacyId2");
//		ndm2.setVendorCode("7");
//		// Guid here doesn't really matter, just needs to be non-null
//		ndm2.setUuid("Icd000032000000000000000000000082");
//		ndm2.setControl("DEL");
//
//		List<NovusDocketMetadata> novusList = new ArrayList<NovusDocketMetadata>(ndmList);
//		novusList.add(ndm2);
//
//		List<NovusDocketMetadata> ndmOutput = lcs.createLoadHistory(novusList, pubReq, new Date(), "testing-testing-testing", "");
//		Assert.assertEquals(2, ndmOutput.size());
//	}

	private List<NovusDocketMetadata> generateNDM() {
		List<NovusDocketMetadata> novusList = new ArrayList<NovusDocketMetadata>();

		// Set novus docket metadata values as needed by the load content service
		NovusDocketMetadata ndm = new NovusDocketMetadata("testing-docket");
		ndm.setLegacyId("TestNYLegacyId");
		ndm.setVendorCode("7");
		// Guid here doesn't really matter, just needs to be non-null
		ndm.setUuid("Ic0000002000000000000000000000082");
		ndm.setControl("ADD");

		novusList.add(ndm);

		return novusList;
	}

}
