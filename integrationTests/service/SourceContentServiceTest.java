/*
 * Copyright 2016: Thomson Reuters.
 * All Rights Reserved. Proprietary and Confidential information of Thomson Reuters.
 * Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.CoreConstants.AcquisitionMethod;
import com.trgr.dockets.core.content.service.SourceContentService;
import com.trgr.dockets.core.content.service.SourceContentServiceImpl;
import com.trgr.dockets.core.domain.DroppedDocketHistory;
import com.trgr.dockets.core.domain.SourceDocketMetadata;
import com.trgr.dockets.core.entity.CodeTableValues.DroppedProcessTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.Content;
import com.trgr.dockets.core.entity.ContentVersion;
import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.Product;
import com.trgr.dockets.core.entity.PublishingRequest;
import com.trgr.dockets.core.exception.DocketsPersistenceException;
import com.trgr.dockets.core.service.ContentService;
import com.trgr.dockets.core.service.ContentVersionService;
import com.trgr.dockets.core.service.DocketEntityService;
import com.trgr.dockets.core.service.DocketHistoryService;
import com.trgr.dockets.core.service.DocketService;
import com.trgr.dockets.core.service.TestContentVersionService;
import com.trgr.dockets.core.service.TestService;
import com.trgr.dockets.core.util.UUIDGenerator;
import com.westgroup.publishingservices.uuidgenerator.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DocketPersistence-context.xml" })
@Transactional(transactionManager = "transactionManager")
public class SourceContentServiceTest
{
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
	@Autowired
	TestService testService;
	@Autowired
	TestContentVersionService testContentVersionService;
	Date previousTimeStamp = new Date();
	String pathString = null;
	private PublishingRequest publishingRequest;
	
	@Before
	public void setUp() 
	{
		URL path = getClass().getResource("SourceContentServiceTest.class");
		pathString = path.getPath();
		pathString = pathString.substring(0,pathString.indexOf("service"));
//		pathString = pathString + "/resources/" + "testsclmessage1.xml";
		pathString = pathString + "/resources/" + "00000951891.xml";
		
		Court court = new Court(95l);
		court.setCourtNorm("NY-SCT-DN");
		this.publishingRequest = new PublishingRequest(UUIDGenerator.createUuid());
		publishingRequest.setCourt(court);
		publishingRequest.setRequestName("thePubRequestName");
		publishingRequest.setDeleteOverride(true);
		publishingRequest.setProduct(new Product(ProductEnum.STATE));
		publishingRequest.setRequestInitiatorType(RequestInitiatorTypeEnum.DAILY);
	}

	@Test
	public void testSaveNewDocket()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId("TestNYLegacyId");
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setCaseTypeId(1L);
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(new Date());
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
		sourceDocketMetadata.setCourt(publishingRequest.getCourt());
//		sourceDocketMetadata.setDocketContentFile(new File("/dockets/testLoad/testsclmessage1.xml"));

		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			List<DroppedDocketHistory> droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			if(droppedDocketList!=null && droppedDocketList.size()>0)
			{
				Assert.fail("No dropped dockets expected " + droppedDocketList);
			}
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata.getCourt(), sourceDocketMetadata.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
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
		sourceDocketMetadata.setLegacyId("TestNYLegacyId");
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(new Date());
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setCaseTypeId(0L);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));

		List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			List<DroppedDocketHistory> droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			if(droppedDocketList!=null && droppedDocketList.size()>0)
			{
				Assert.fail("No dropped dockets expected " + droppedDocketList);
			}
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata.getCourt(), sourceDocketMetadata.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
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
			sourceMetaDocketMetadataList.remove(0);
			SourceDocketMetadata sourceDocketMetadata1 = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata1.setLegacyId("TestNYLegacyId");
			sourceDocketMetadata1.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata1.setVendorId(9); 
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			Date newDate = c.getTime();
			sourceDocketMetadata1.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata1.setDeleteOperation(false);
			sourceDocketMetadata1.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata1.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
			sourceDocketMetadata1.setDocketContentFile(new File(pathString));

			sourceMetaDocketMetadataList.add(sourceDocketMetadata1);
			droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			if(droppedDocketList!=null && droppedDocketList.size()>0)
			{
				Assert.fail("No dropped dockets expected " + droppedDocketList);
			}
			expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata1.getCourt(), sourceDocketMetadata1.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			
			Assert.assertTrue("Operation Type should be U but is " + expectedDocketVersion.getOperationType()
					 , "U".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Two contentVersion should be present ", contentVersionList.size()==2);
			for(ContentVersion contentVersion : contentVersionList)
			{
				Assert.assertTrue("Content Version Id should be 1 or 2",(contentVersion.getPrimaryKey().getVersionId()==1L||contentVersion.getPrimaryKey().getVersionId()==2L));
				Assert.assertNotNull("Content Should be present", contentVersion.getFileimage());
			}
			
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
		Date equalTimeStamp = new Date();
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId("TestNYLegacyId");
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setCaseTypeId(0L);
		sourceDocketMetadata.setAcquisitionTimestamp(equalTimeStamp);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
				List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			List<DroppedDocketHistory> droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			if(droppedDocketList!=null && droppedDocketList.size()>0)
			{
				Assert.fail("No dropped dockets expected " + droppedDocketList);
			}
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata.getCourt(), sourceDocketMetadata.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
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
			
			sourceMetaDocketMetadataList.remove(0);
			SourceDocketMetadata sourceDocketMetadata1 = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata1.setLegacyId("TestNYLegacyId");
			sourceDocketMetadata1.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata1.setVendorId(9); 
			sourceDocketMetadata1.setAcquisitionTimestamp(equalTimeStamp);
			sourceDocketMetadata1.setDeleteOperation(false);
			sourceDocketMetadata1.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata1.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
			sourceDocketMetadata1.setDocketContentFile(new File(pathString));
			sourceMetaDocketMetadataList.add(sourceDocketMetadata1);
			 droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			if(droppedDocketList!=null && droppedDocketList.size()>0)
			{
				Assert.fail("No dropped dockets expected " + droppedDocketList);
			}
			expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata1.getCourt(), sourceDocketMetadata1.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			
			Assert.assertTrue("Operation Type should be R but is " + expectedDocketVersion.getOperationType()
					 , "R".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Two contentVersion should be present ", contentVersionList.size()==2);
			for(ContentVersion contentVersion : contentVersionList)
			{
				Assert.assertTrue("Content Version Id should be 1 or 2",(contentVersion.getPrimaryKey().getVersionId()==1L||contentVersion.getPrimaryKey().getVersionId()==2L));
				Assert.assertNotNull("Content Should be present", contentVersion.getFileimage());
			}
			
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
		sourceDocketMetadata.setLegacyId("TestNYLegacyId");
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(new Date());
		sourceDocketMetadata.setCaseTypeId(0L);
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
				List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		sourceMetaDocketMetadataList.add(sourceDocketMetadata);
		try 
		{
			List<DroppedDocketHistory> droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			if(droppedDocketList!=null && droppedDocketList.size()>0)
			{
				Assert.fail("No dropped dockets expected " + droppedDocketList);
			}
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata.getCourt(), sourceDocketMetadata.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
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
			
			sourceMetaDocketMetadataList.remove(0);
			SourceDocketMetadata sourceDocketMetadata1 = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata1.setLegacyId("TestNYLegacyId");
			sourceDocketMetadata1.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata1.setVendorId(9); 
			sourceDocketMetadata1.setAcquisitionTimestamp(new Date());
			sourceDocketMetadata1.setDeleteOperation(true);
			sourceDocketMetadata1.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata1.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
			sourceDocketMetadata1.setDocketContentFile(new File(pathString));
			sourceMetaDocketMetadataList.add(sourceDocketMetadata1);
			droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			if(droppedDocketList!=null && droppedDocketList.size()>0)
			{
				Assert.fail("No dropped dockets expected " + droppedDocketList);
			}
			expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata1.getCourt(), sourceDocketMetadata1.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
			Assert.assertTrue("version id should be 2 but is " + expectedDocketVersion.getVersion()
					 , expectedDocketVersion.getVersion()==2);
			
			Assert.assertTrue("Operation Type should be D but is " + expectedDocketVersion.getOperationType()
					 , "D".equals(expectedDocketVersion.getOperationType()));
			content = contentService.findContentByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("Content should be present",content);
			
			contentVersionList = contentVersionService.findContentVersionByUuid(expectedDocketVersion.getContentId());
			Assert.assertNotNull("ContentVersion should be present",contentVersionList);
			Assert.assertTrue("Two contentVersion should be present ", contentVersionList.size()==2);
			for(ContentVersion contentVersion : contentVersionList)
			{
				Assert.assertTrue("Content Version Id should be 1 or 2",(contentVersion.getPrimaryKey().getVersionId()==1L||contentVersion.getPrimaryKey().getVersionId()==2L));
				Assert.assertNotNull("Content Should be present", contentVersion.getFileimage());
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testAlreadyExistingDocketForInvalidAcquisitionTimestamp()
	{
		SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
		sourceDocketMetadata.setLegacyId("TestNYLegacyId");
		sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
		sourceDocketMetadata.setVendorId(9); 
		sourceDocketMetadata.setAcquisitionTimestamp(new Date());
		sourceDocketMetadata.setDeleteOperation(false);
		sourceDocketMetadata.setCaseTypeId(0L);
		sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
		sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
		sourceDocketMetadata.setDocketContentFile(new File(pathString));
				List<SourceDocketMetadata> sourceMetaDocketMetadataList = new ArrayList<SourceDocketMetadata>();
		try 
		{   
			
			sourceDocketMetadata.setAcquisitionTimestamp(new Date());
			sourceMetaDocketMetadataList.add(sourceDocketMetadata);
			this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			DocketVersion expectedDocketVersion = this.docketService.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct("TestNYLegacyId", sourceDocketMetadata.getCourt(), sourceDocketMetadata.getVendorId(), publishingRequest.getProduct().getId());
			Assert.assertTrue("legacy id should be TestNYLegacyId but is " + expectedDocketVersion.getPrimaryKey().getLegacyId()
					 , "TestNYLegacyId".equals(expectedDocketVersion.getPrimaryKey().getLegacyId()));
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
			
			sourceMetaDocketMetadataList.remove(0);
			SourceDocketMetadata sourceDocketMetadata1 = new SourceDocketMetadata(publishingRequest);
			sourceDocketMetadata1.setLegacyId("TestNYLegacyId");
			sourceDocketMetadata1.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata1.setVendorId(9); 
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DATE, -1);
			Date newDate = c.getTime();
			sourceDocketMetadata1.setAcquisitionTimestamp(newDate);
			sourceDocketMetadata1.setDeleteOperation(true);
			sourceDocketMetadata1.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata1.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
			sourceDocketMetadata1.setDocketContentFile(new File(pathString));
			sourceMetaDocketMetadataList.add(sourceDocketMetadata1);
			List<DroppedDocketHistory> droppedDocketList = this.sourceDocketContentServiceImpl.loadDockets(sourceMetaDocketMetadataList);
			Assert.assertTrue("We should have 1 dropped Docket", droppedDocketList.size()==1);
//			System.out.println(droppedDocketList.get(0).getBatchDroppedDocket().getErrorDescription());
			Assert.assertTrue("We should have 1 dropped Docket with reason Acquisition timestamp older but is " + droppedDocketList.get(0).getBatchDroppedDocket().getErrorDescription(), 
								droppedDocketList.get(0).getBatchDroppedDocket().getErrorDescription().contains("Newer version of docket already processed"));
			Assert.assertTrue("We should have 1 dropped Docket with legacyId \"TestNYLegacyId\" but is " + droppedDocketList.get(0).getLegacyId(), droppedDocketList.get(0).getLegacyId().contains("TestNYLegacyId"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	@Test
	public void testMultipleRequestsSameDockets(){
		List<SourceDocketMetadata> sourceMetadataList = new ArrayList<SourceDocketMetadata>();
		List<String> legacyIds = new ArrayList<String>();
		for (int i =0; i <10; i++){
			SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			String legacyId = "IMAGINARY"+getImaginaryDocketNumber();
			legacyIds.add(legacyId);
			sourceDocketMetadata.setLegacyId(legacyId);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(new Date());
			sourceDocketMetadata.setDeleteOperation(false);
			sourceDocketMetadata.setCaseTypeId(0L);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));
			sourceDocketMetadata.setAcquisitionTimestamp(new Date());
			sourceMetadataList.add(sourceDocketMetadata);
		}

		
		List<LoadContentServiceThread> loadThreads = new ArrayList<LoadContentServiceThread>();
		for (int i=0; i<10; i++){
			loadThreads.add(new LoadContentServiceThread(sourceMetadataList));
		}
		
		for (LoadContentServiceThread serviceThread : loadThreads){
			serviceThread.start();
		}
		List<DroppedDocketHistory> allDroppedDockets = new ArrayList<DroppedDocketHistory>();
		for (LoadContentServiceThread serviceThread : loadThreads){
			try {
				serviceThread.join();
				List<DroppedDocketHistory> badDockets = serviceThread.getBadDockets();
				if (badDockets != null && badDockets.size() > 0){
					allDroppedDockets.addAll(badDockets);
				}
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				Assert.fail();
			}			
		}
		for(String legacyId: legacyIds){
			AfterTestThread after = new AfterTestThread(legacyId);
			try {	
				after.start();
				after.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (allDroppedDockets.size() > 0){
			for (DroppedDocketHistory badDocket : allDroppedDockets){
				Assert.assertTrue(102l == badDocket.getDroppedReasonId());
				Assert.assertTrue(badDocket.getBatchDroppedDocket().getErrorDescription().contains("Data integrity violation exception"));
				Assert.assertTrue (DroppedProcessTypeEnum.PP.equals(badDocket.getProcessName()));
			}			
		}

		
	}
	@Transactional
	class AfterTestThread extends Thread {
		String legacyId;
		AfterTestThread(String legacyId) {
			this.legacyId = legacyId;
		}
		@Override
		@Transactional(propagation= Propagation.REQUIRES_NEW)
		public void run() {
			testService.deleteAllDocketVersionsForLegacyId(legacyId);
			testService.deleteAllDocketHistoryForLegacyId(legacyId);
			try {
				
			} catch (Exception e) {
				e.printStackTrace();
			} 		
		}
	}
	@Test
	public void testMultipleRequestsSameDocketsExpectFailures(){
		List<SourceDocketMetadata> sourceMetadataList = new ArrayList<SourceDocketMetadata>();
		List<String> legacyIds = new ArrayList<String>();
		for (int i =0; i <10; i++){
			SourceDocketMetadata sourceDocketMetadata = new SourceDocketMetadata(publishingRequest);
			String legacyId = "IMAGINARY"+getImaginaryDocketNumber();
			legacyIds.add(legacyId);
			sourceDocketMetadata.setLegacyId(legacyId);
			sourceDocketMetadata.setDocketNumber("ALBANY00002842011");
			sourceDocketMetadata.setVendorId(9); 
			sourceDocketMetadata.setAcquisitionTimestamp(new Date());
			sourceDocketMetadata.setDeleteOperation(false);
			sourceDocketMetadata.setCaseTypeId(0L);
			sourceDocketMetadata.setAcquisitionMethod(AcquisitionMethod.PROSPECTIVE);
			sourceDocketMetadata.setSourceFile(new File("TEST_SOURCE_FILENAME_2"));
			sourceDocketMetadata.setDocketContentFile(new File(pathString));
			sourceDocketMetadata.setAcquisitionTimestamp(new Date());
			sourceMetadataList.add(sourceDocketMetadata);
		}

		
		List<LoadContentServiceThread> loadThreads = new ArrayList<LoadContentServiceThread>();
		for (int i=0; i<10; i++){
			loadThreads.add(new LoadContentServiceThread(sourceMetadataList,1));
		}
		
		for (LoadContentServiceThread serviceThread : loadThreads){
			serviceThread.start();
		}
		List<DroppedDocketHistory> allDroppedDockets = new ArrayList<DroppedDocketHistory>();
		for (LoadContentServiceThread serviceThread : loadThreads){
			try {
				serviceThread.join();
				List<DroppedDocketHistory> badDockets = serviceThread.getBadDockets();
				if (badDockets != null && badDockets.size() > 0){
					allDroppedDockets.addAll(badDockets);
				}
			} catch (@SuppressWarnings("unused") InterruptedException e) {
				Assert.fail();
			}			
		}
		
		for(String legacyId: legacyIds){
			AfterTestThread after = new AfterTestThread(legacyId);
			try {	
				after.start();
				after.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//The above test should enforce at least one failure (due to trying to insert the same legacy id at the same time across
		//multiple threads)
		Assert.assertTrue(allDroppedDockets.size() > 0);
		for (DroppedDocketHistory badDocket : allDroppedDockets){
			Assert.assertTrue(102l == badDocket.getDroppedReasonId());
			Assert.assertTrue(badDocket.getBatchDroppedDocket().getErrorDescription().contains("Data integrity violation exception"));
			Assert.assertTrue (DroppedProcessTypeEnum.PP.equals(badDocket.getProcessName()));
		}			
		

		
	}
	
	private static String getImaginaryDocketNumber() {
		Long serialNumber = new Long(0);
		while (serialNumber.longValue() < 100) {
			Double temp = new Double(Math.random());
			if (temp.longValue() < 1) {
				temp = new Double(temp.doubleValue() * 100000000);
				serialNumber = new Long(temp.longValue());
			}
		}
		return serialNumber.toString();
	}
	
	private class LoadContentServiceThread extends Thread{
		private List<SourceDocketMetadata> sdmList;
		private List<DroppedDocketHistory> badDockets;
		private int maxRetry = -1;
		public LoadContentServiceThread(List<SourceDocketMetadata> sdmList){
			this.sdmList = sdmList;
		}
		
		public LoadContentServiceThread(List<SourceDocketMetadata> sdmList, int maxRetry){
			this.sdmList = sdmList;
			this.maxRetry = maxRetry;
		}
		
		public List<DroppedDocketHistory> getBadDockets(){
			return badDockets;
		}
		
		@Override
		public void run(){
			try {
				if (maxRetry > -1 && sourceDocketContentServiceImpl instanceof SourceContentServiceImpl){
					((SourceContentServiceImpl) sourceDocketContentServiceImpl).setMaxRetry(maxRetry);
				}
				badDockets = sourceDocketContentServiceImpl.loadDockets(sdmList);
			} catch (DocketsPersistenceException e) {
				e.printStackTrace();
				Assert.fail();
			}
		}
	}
	
}
