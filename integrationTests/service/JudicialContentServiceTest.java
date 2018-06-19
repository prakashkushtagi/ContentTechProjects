/** Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited. */

package service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.CoreConstants.Phase;
import com.trgr.dockets.core.content.service.JudicialContentServiceImpl;
import com.trgr.dockets.core.content.service.SourceContentService;
import com.trgr.dockets.core.domain.JudicialDocketMetadata;
import com.trgr.dockets.core.domain.LargeDocketInfo;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketHistory;
import com.trgr.dockets.core.entity.DocketHistory.DocketHistoryTypeEnum;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.DocketVersionKey;
import com.trgr.dockets.core.entity.DocketVersionRel;
import com.trgr.dockets.core.entity.DocketVersionRel.DocketVersionRelTypeEnum;
import com.trgr.dockets.core.entity.DocketVersionRelHistory;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.service.ContentService;
import com.trgr.dockets.core.service.ContentVersionService;
import com.trgr.dockets.core.service.DocketEntityService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.util.UUIDGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional(transactionManager = "transactionManager")
public class JudicialContentServiceTest
{
	private static final String LEGACY_ID = "theLegacyId";
	private static final String BATCH_ID = "theBatchId";
	private File JAXML_FILE = null;
	private PublishingRequest publishingRequest;
	private DocketVersion docketVersion;
	@Autowired
	JudicialContentServiceImpl judicialDocketContentService;
	@Autowired
	SourceContentService sourceDocketContentServiceImpl;
	@Autowired
	DocketEntityService docketEntityService;
	@Autowired
	DocketService docketService;
	@Autowired
	ContentVersionService contentVersionService;
	@Autowired
	DocketHistoryService docketHistoryService;
	@Autowired
	ContentService contentService;

	private Date previousTimestamp = new Date();
	private Date currentTimestamp = new Date();
	private String pathString = null;
	
	@Before
	public void setUp() {
		Court court = new Court(95l);
		court.setCourtNorm("NY-SCT-DN");
		this.publishingRequest = new PublishingRequest(UUIDGenerator.createUuid());
		publishingRequest.setCourt(court);
		publishingRequest.setRequestName("thePubRequestName");
		publishingRequest.setDeleteOverride(true);
		publishingRequest.setProduct(new Product(ProductEnum.STATE));
		publishingRequest.setVendorId(9l);
		DocketEntity docket = new DocketEntity(LEGACY_ID, "ALBANY00002842011", court);
		docket.setProduct(new Product(ProductEnum.STATE));
		docket.setCaseTypeId(1l);
		docket.setDocLoadedFlag("N");
		docket.setPublishFlag("N");
		this.docketVersion = new DocketVersion(new DocketVersionKey(LEGACY_ID, 999l, Phase.SOURCE, new Date()));
		docketVersion.setDocket(docket);
		docketVersion.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		docketVersion.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
		currentTimestamp = new Date();
		docketVersion.setScrapeTimestamp(currentTimestamp);
		docketVersion.setSourceFile(new File("junitSourceDocketFile"));
		
		URL path = getClass().getResource("SourceContentServiceTest.class");
		pathString = path.getPath();
		pathString = pathString.substring(0,pathString.indexOf("service"));
		pathString = pathString + "/resources/" + "testsclmessage1.xml";
		JAXML_FILE = new File(pathString);
	}

	@Test
	public void testSaveNewDocketWithSource()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId(LEGACY_ID);
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(new Date());
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCaseTypeId(1l);
		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
		}
		catch(@SuppressWarnings("unused") Exception e)
		{
			Assert.fail("Could not insert source");
		}
		publishingRequest.setDeleteOverride(false);
		JudicialDocketMetadata judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
		judicialDocketMetadata.setScrapeTimeStamp(new Date());
		LargeDocketInfo largeDocketInfo3 = new LargeDocketInfo();
		largeDocketInfo3.setNbrOfDocketEntries(500);
		largeDocketInfo3.setNbrOfParties(500);
		largeDocketInfo3.setLargeDocket(false);
		judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo3);
		List<JudicialDocketMetadata> judicialMetaDocketMetadataList = new ArrayList<JudicialDocketMetadata>();
		judicialMetaDocketMetadataList.add(judicialDocketMetadata);
		try 
		{
			DocketVersion judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			DocketVersion expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue("Operation Type should be I but is " + expectedDocketVersion.getOperationType()
					 , "I".equals(expectedDocketVersion.getOperationType()));
			Content content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			//check docket entity last scrape date
			DocketEntity entity = docketService.findDocketByPrimaryKey(LEGACY_ID);
			Assert.assertEquals(expectedDocketVersion.getScrapeTimestamp(), entity.getLastScrapeDate());
			List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			Assert.assertTrue("Content Version Id should be 1",contentVersionList.get(0).getPrimaryKey().getVersionId()==1L);
			Assert.assertNotNull("Content Should be present", contentVersionList.get(0).getFileimage());
			
			List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 2 docketHistor should be present ", docketHistoryList.size()==2);
			List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			DocketVersionRelHistory docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testAlreadyExistingDocketForU()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId(LEGACY_ID);
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(currentTimestamp);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCaseTypeId(1l);
		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
		}
		catch(@SuppressWarnings("unused") Exception e)
		{
			Assert.fail("Could not insert source");
		}
		publishingRequest.setDeleteOverride(false);
		
		JudicialDocketMetadata judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
		judicialDocketMetadata.setScrapeTimeStamp(currentTimestamp);
		LargeDocketInfo largeDocketInfo = new LargeDocketInfo();
		largeDocketInfo.setNbrOfDocketEntries(500);
		largeDocketInfo.setNbrOfParties(500);
		largeDocketInfo.setLargeDocket(false);
		judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo);
		try 
		{
			DocketVersion judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			DocketVersion expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue("Operation Type should be I but is " + expectedDocketVersion.getOperationType()
					 , "I".equals(expectedDocketVersion.getOperationType()));
			Content content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			Assert.assertTrue("Content Version Id should be 1",contentVersionList.get(0).getPrimaryKey().getVersionId()==1L);
			Assert.assertNotNull("Content Should be present", contentVersionList.get(0).getFileimage());
			
			List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 2 docketHistory should be present ", docketHistoryList.size()==2);
			List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			DocketVersionRelHistory docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()));
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			Date newDate = c.getTime();
			
			sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata.setLegacyId(LEGACY_ID);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata.setDeleteOperation(false);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));

			sourceMetaDocketMetadataList.remove(0);
			sourceMetaDocketMetadataList.add(sourceDocketMetadata);
			try 
			{
				this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			}
			catch(@SuppressWarnings("unused") Exception e)
			{
				Assert.fail("Could not insert source");
			}
			publishingRequest.setDeleteOverride(false);

			docketVersion.setScrapeTimestamp(newDate);
			judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
			judicialDocketMetadata.setScrapeTimeStamp(new Date());
			LargeDocketInfo largeDocketInfo2 = new LargeDocketInfo();
			largeDocketInfo2.setNbrOfDocketEntries(500);
			largeDocketInfo2.setNbrOfParties(500);
			largeDocketInfo2.setLargeDocket(false);
			judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo2);
			judicialDocketVersion = null;
			judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			Assert.assertTrue("Operation Type should be U but is " + expectedDocketVersion.getOperationType()
					 , "U".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only two contentVersion should be present ", contentVersionList.size()==2);
			
			docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 4 docketHistory should be present ", docketHistoryList.size()==4);
			docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 2 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==2);
			
			docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 2 but is " + docketVersionRel.getSourceVersion(),2==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 2 but is " + docketVersionRel.getTargetVersion(),2==docketVersionRel.getTargetVersion());
			
			docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " or " + docketHistoryList2.get(1).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),
					
								(docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()) ||docketHistoryList2.get(1).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId())));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
			
	} 
	
	@Test
	public void testAlreadyExistingDocketForR()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId(LEGACY_ID);
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(currentTimestamp);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCaseTypeId(1l);
		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
		}
		catch(@SuppressWarnings("unused") Exception e)
		{
			Assert.fail("Could not insert source");
		}
		publishingRequest.setDeleteOverride(false);
		JudicialDocketMetadata judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
		judicialDocketMetadata.setScrapeTimeStamp(currentTimestamp);
		LargeDocketInfo largeDocketInfo2 = new LargeDocketInfo();
		largeDocketInfo2.setNbrOfDocketEntries(500);
		largeDocketInfo2.setNbrOfParties(500);
		largeDocketInfo2.setLargeDocket(false);
		judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo2);
		try 
		{
			DocketVersion judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			DocketVersion expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue("Operation Type should be I but is " + expectedDocketVersion.getOperationType()
					 , "I".equals(expectedDocketVersion.getOperationType()));
			Content content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			Assert.assertTrue("Content Version Id should be 1",contentVersionList.get(0).getPrimaryKey().getVersionId()==1L);
			Assert.assertNotNull("Content Should be present", contentVersionList.get(0).getFileimage());
			
			List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 2 docketHistory should be present ", docketHistoryList.size()==2);
			List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			DocketVersionRelHistory docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()));

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			Date newDate = c.getTime();

			
			sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata.setLegacyId(LEGACY_ID);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata.setDeleteOperation(false);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));

			sourceMetaDocketMetadataList.remove(0);
			sourceMetaDocketMetadataList.add(sourceDocketMetadata);
			try 
			{
				this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			}
			catch(@SuppressWarnings("unused") Exception e)
			{
				Assert.fail("Could not insert source");
			}
			publishingRequest.setDeleteOverride(false);
			judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
			judicialDocketMetadata.setScrapeTimeStamp(currentTimestamp);
			LargeDocketInfo largeDocketInfo = new LargeDocketInfo();
			largeDocketInfo.setNbrOfDocketEntries(500);
			largeDocketInfo.setNbrOfParties(500);
			largeDocketInfo.setLargeDocket(false);
			judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo);
			judicialDocketVersion = null;
			judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			Assert.assertTrue("Operation Type should be R but is " + expectedDocketVersion.getOperationType()
					 , "R".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only two contentVersion should be present ", contentVersionList.size()==2);
			
			docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 4 docketHistory should be present ", docketHistoryList.size()==4);
			docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 2 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==2);
			
			docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 2 but is " + docketVersionRel.getSourceVersion(),2==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 2 but is " + docketVersionRel.getTargetVersion(),2==docketVersionRel.getTargetVersion());
			
			docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " or " + docketHistoryList2.get(1).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),
					
								(docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()) ||docketHistoryList2.get(1).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId())));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testAlreadyExistingDocketForD()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId(LEGACY_ID);
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(currentTimestamp);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCaseTypeId(1l);
		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
		}
		catch(@SuppressWarnings("unused") Exception e)
		{
			Assert.fail("Could not insert source");
		}
		JudicialDocketMetadata judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
		judicialDocketMetadata.setScrapeTimeStamp(currentTimestamp);
		LargeDocketInfo largeDocketInfo = new LargeDocketInfo();
		largeDocketInfo.setNbrOfDocketEntries(500);
		largeDocketInfo.setNbrOfParties(500);
		largeDocketInfo.setLargeDocket(false);
		judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo);
		try 
		{
			DocketVersion judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			DocketVersion expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue(expectedDocketVersion.getOperationType()
					 , "D".equals(expectedDocketVersion.getOperationType()));
			Content content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			Assert.assertTrue("Content Version Id should be 1",contentVersionList.get(0).getPrimaryKey().getVersionId()==1L);
			Assert.assertNotNull("Content Should be present", contentVersionList.get(0).getFileimage());
			
			List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 2 docketHistory should be present ", docketHistoryList.size()==2);
			List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			DocketVersionRelHistory docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()));
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			Date newDate = c.getTime();
			
			sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata.setLegacyId(LEGACY_ID);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata.setDeleteOperation(true);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));

			sourceMetaDocketMetadataList.remove(0);
			sourceMetaDocketMetadataList.add(sourceDocketMetadata);
			try 
			{
				this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			}
			catch(@SuppressWarnings("unused") Exception e)
			{
				Assert.fail("Could not insert source");
			}
			
			judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
			judicialDocketMetadata.setScrapeTimeStamp(newDate);
			LargeDocketInfo largeDocketInfo2 = new LargeDocketInfo();
			largeDocketInfo2.setNbrOfDocketEntries(500);
			largeDocketInfo2.setNbrOfParties(500);
			largeDocketInfo2.setLargeDocket(false);
			judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo2);
			judicialDocketVersion = null;
			
			
			judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			Assert.assertTrue("Operation Type should be D but is " + expectedDocketVersion.getOperationType()
					 , "D".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only two contentVersion should be present ", contentVersionList.size()==2);
			
			docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 4 docketHistory should be present ", docketHistoryList.size()==4);
			docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 2 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==2);
			
			docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 2 but is " + docketVersionRel.getSourceVersion(),2==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 2 but is " + docketVersionRel.getTargetVersion(),2==docketVersionRel.getTargetVersion());
			
			docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " or " + docketHistoryList2.get(1).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),
					
								(docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()) ||docketHistoryList2.get(1).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId())));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testAlreadyExistingDocketForInvalidScrapeTimestamp()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId(LEGACY_ID);
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(currentTimestamp);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCaseTypeId(1l);
		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
		}
		catch(@SuppressWarnings("unused") Exception e)
		{
			Assert.fail("Could not insert source");
		}
		publishingRequest.setDeleteOverride(false);
		JudicialDocketMetadata judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
		judicialDocketMetadata.setScrapeTimeStamp(currentTimestamp);
		LargeDocketInfo largeDocketInfo = new LargeDocketInfo();
		largeDocketInfo.setNbrOfDocketEntries(500);
		largeDocketInfo.setNbrOfParties(500);
		largeDocketInfo.setLargeDocket(false);
		judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo);
		try 
		{
			DocketVersion judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			DocketVersion expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue("Operation Type should be I but is " + expectedDocketVersion.getOperationType()
					 , "I".equals(expectedDocketVersion.getOperationType()));
			Content content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			Assert.assertTrue("Content Version Id should be 1",contentVersionList.get(0).getPrimaryKey().getVersionId()==1L);
			Assert.assertNotNull("Content Should be present", contentVersionList.get(0).getFileimage());
			
			List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 2 docketHistory should be present ", docketHistoryList.size()==2);
			List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			DocketVersionRelHistory docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()));
			
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			Date newDate = c.getTime();
			
			sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata.setLegacyId(LEGACY_ID);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata.setDeleteOperation(false);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));

			sourceMetaDocketMetadataList.remove(0);
			sourceMetaDocketMetadataList.add(sourceDocketMetadata);
			try 
			{
				this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			}
			catch(@SuppressWarnings("unused") Exception e)
			{
				Assert.fail("Could not insert source");
			}
			docketVersion.setScrapeTimestamp(previousTimestamp);
			judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
			judicialDocketVersion = null;
			LargeDocketInfo largeDocketInfo2 = new LargeDocketInfo();
			largeDocketInfo2.setNbrOfDocketEntries(500);
			largeDocketInfo2.setNbrOfParties(500);
			largeDocketInfo2.setLargeDocket(false);
			judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo2);
			try{
				judicialDocketVersion  = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			}catch(Exception e){
				Assert.assertTrue("We should have 1 dropped Docket", judicialDocketVersion == null);
				Assert.assertTrue("We should have 1 dropped Docket with reason Scrape timestamp older but is " + e.getMessage(), e.getMessage().contains("older"));
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}

	}
	
	@Test
	public void testInsertingOldDocketWithoutAcquisitionOverride()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId(LEGACY_ID);
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(currentTimestamp);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCaseTypeId(1l);
		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
		}
		catch(@SuppressWarnings("unused") Exception e)
		{
			Assert.fail("Could not insert source");
		}
		publishingRequest.setDeleteOverride(false);
		JudicialDocketMetadata judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
		judicialDocketMetadata.setScrapeTimeStamp(currentTimestamp);
		LargeDocketInfo largeDocketInfo = new LargeDocketInfo();
		largeDocketInfo.setNbrOfDocketEntries(500);
		largeDocketInfo.setNbrOfParties(500);
		largeDocketInfo.setLargeDocket(false);
		judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo);
		try 
		{
			DocketVersion judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			DocketVersion expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue("Operation Type should be I but is " + expectedDocketVersion.getOperationType()
					 , "I".equals(expectedDocketVersion.getOperationType()));
			Content content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			Assert.assertTrue("Content Version Id should be 1",contentVersionList.get(0).getPrimaryKey().getVersionId()==1L);
			Assert.assertNotNull("Content Should be present", contentVersionList.get(0).getFileimage());
			
			List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 2 docketHistory should be present ", docketHistoryList.size()==2);
			List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			DocketVersionRelHistory docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()));

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			Date newDate = c.getTime();

			
			sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata.setLegacyId(LEGACY_ID);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata.setDeleteOperation(false);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));

			sourceMetaDocketMetadataList.remove(0);
			sourceMetaDocketMetadataList.add(sourceDocketMetadata);
			try 
			{
				this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			}
			catch(@SuppressWarnings("unused") Exception e)
			{
				Assert.fail("Could not insert source");
			}
			publishingRequest.setDeleteOverride(false);
			judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
			

			c = Calendar.getInstance();
			c.setTime(currentTimestamp);
			c.add(Calendar.DATE, -150);
			Date oldDate = c.getTime();

			judicialDocketMetadata.setScrapeTimeStamp(oldDate);
			LargeDocketInfo largeDocketInfo2 = new LargeDocketInfo();
			largeDocketInfo2.setNbrOfDocketEntries(500);
			largeDocketInfo2.setNbrOfParties(500);
			largeDocketInfo2.setLargeDocket(false);
			judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo2);
			judicialDocketVersion = null;
			try {
				judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			}
			catch (Exception e){
				if (!e.getMessage().contains("already processed Scrape timestamp")){
					throw new Exception("JudicialDocketContentService.persist threw an exception and not for incorrect scrape timestamp");
				}				
			}
			//there shouldn't be a new docket version created, so we just check to make sure one judicial content version exists
			//and that its an insert, not a reprocessing
			Assert.assertFalse("DroppedDocket should be present ", judicialDocketVersion != null);
			expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue("Operation Type should be I but is " + expectedDocketVersion.getOperationType()
					 , "I".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			
			docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 3 docketHistory should be present ", docketHistoryList.size()==3);
			docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId()  + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),
					
								(docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()) ));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testInsertingOldDocketWithAcquisitionOverride()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId(LEGACY_ID);
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(currentTimestamp);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCaseTypeId(1l);
		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
		}
		catch(@SuppressWarnings("unused") Exception e)
		{
			Assert.fail("Could not insert source");
		}
		publishingRequest.setDeleteOverride(false);
		JudicialDocketMetadata judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
		judicialDocketMetadata.setScrapeTimeStamp(currentTimestamp);
		LargeDocketInfo largeDocketInfo = new LargeDocketInfo();
		largeDocketInfo.setNbrOfDocketEntries(500);
		largeDocketInfo.setNbrOfParties(500);
		largeDocketInfo.setLargeDocket(false);
		judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo);
		try 
		{
			DocketVersion judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, false);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			DocketVersion expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 1 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==1);
			Assert.assertTrue("Operation Type should be I but is " + expectedDocketVersion.getOperationType()
					 , "I".equals(expectedDocketVersion.getOperationType()));
			Content content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			List<ContentVersion> contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only one contentVersion should be present ", contentVersionList.size()==1);
			Assert.assertTrue("Content Version Id should be 1",contentVersionList.get(0).getPrimaryKey().getVersionId()==1L);
			Assert.assertNotNull("Content Should be present", contentVersionList.get(0).getFileimage());
			
			List<DocketHistory> docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 2 docketHistory should be present ", docketHistoryList.size()==2);
			List<DocketHistory> docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 1 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==1);
			
			DocketVersionRel docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 1 but is " + docketVersionRel.getSourceVersion(),1==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 1 but is " + docketVersionRel.getTargetVersion(),1==docketVersionRel.getTargetVersion());
			
			DocketVersionRelHistory docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()));

			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			Date newDate = c.getTime();

			
			sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata.setLegacyId(LEGACY_ID);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata.setDeleteOperation(false);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));

			sourceMetaDocketMetadataList.remove(0);
			sourceMetaDocketMetadataList.add(sourceDocketMetadata);
			try 
			{
				this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			}
			catch(@SuppressWarnings("unused") Exception e)
			{
				Assert.fail("Could not insert source");
			}
			publishingRequest.setDeleteOverride(false);
			judicialDocketMetadata = new JudicialDocketMetadata(publishingRequest, docketVersion, JAXML_FILE, BATCH_ID);
			

			c = Calendar.getInstance();
			c.setTime(currentTimestamp);
			c.add(Calendar.DATE, -150);
			Date oldDate = c.getTime();

			judicialDocketMetadata.setScrapeTimeStamp(oldDate);
			LargeDocketInfo largeDocketInfo2 = new LargeDocketInfo();
			largeDocketInfo2.setNbrOfDocketEntries(500);
			largeDocketInfo2.setNbrOfParties(500);
			largeDocketInfo2.setLargeDocket(false);
			judicialDocketMetadata.setLargeDocketInfo(largeDocketInfo2);
			judicialDocketVersion = null;
			judicialDocketVersion = this.judicialDocketContentService.persistDocket(judicialDocketMetadata, true);
			Assert.assertTrue("No droppedDocket should be present ", judicialDocketVersion != null);
			expectedDocketVersion = docketService.findJudicialDocketVersionWithHighestVersion(LEGACY_ID,publishingRequest.getVendorId(), 
				publishingRequest.getProduct().getId(), publishingRequest.getCourt());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , LEGACY_ID.equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			Assert.assertTrue("Operation Type should be R but is " + expectedDocketVersion.getOperationType()
					 , "R".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Only two contentVersion should be present ", contentVersionList.size()==2);
			
			docketHistoryList = docketHistoryService.findDocketHistoryByLegacyId(LEGACY_ID);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList);
			Assert.assertTrue("Only 4 docketHistory should be present ", docketHistoryList.size()==4);
			docketHistoryList2 = docketHistoryService.findDocketHistoryByLegacyIdType(LEGACY_ID, DocketHistoryTypeEnum.CONVERSION_HISTORY);
			Assert.assertNotNull("docketHistory should be present",docketHistoryList2);
			Assert.assertTrue("Only 2 docketHistory should be present for type CONVERSION_HISTORY", docketHistoryList2.size()==2);
			
			docketVersionRel = docketHistoryService.findByLatestVersionRelBylegacyIdAndType(LEGACY_ID,DocketVersionRelTypeEnum.SOURCE_TO_JAXML);
			Assert.assertNotNull("docketVersionRel should be present",docketVersionRel);
			Assert.assertTrue("Source version should be 2 but is " + docketVersionRel.getSourceVersion(),2==docketVersionRel.getSourceVersion());
			Assert.assertTrue("Target version should be 2 but is " + docketVersionRel.getTargetVersion(),2==docketVersionRel.getTargetVersion());
			
			docketVersionRelHistory = docketHistoryService.findDocketVersionRelHistoryByRelId(docketVersionRel.getRelId()); 
			Assert.assertNotNull("docketVersionRelHistory should be present",docketVersionRelHistory);
			Assert.assertTrue("Legacy Id should be TestNYLegacyId but is " + docketVersionRelHistory.getLegacyId(),LEGACY_ID.equals(docketVersionRel.getLegacyId()));
			Assert.assertTrue("History ID  should be "  +docketHistoryList2.get(0).getDocketHistoryId() + " or " + docketHistoryList2.get(1).getDocketHistoryId() + " but is " + docketVersionRelHistory.getPrimaryKey().getDocketHistoryId(),
					
								(docketHistoryList2.get(0).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId()) ||docketHistoryList2.get(1).getDocketHistoryId().equals(docketVersionRelHistory.getPrimaryKey().getDocketHistoryId())));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
}
