/**
 * Copyright 2013: Thomson Reuters Global Resources. All Rights Reserved.
 *
 * Proprietary and Confidential information of TRGR.
 * Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.domain;

import org.apache.commons.lang.NotImplementedException;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketEntity;
import com.trgr.dockets.core.entity.DocketVersion;
import com.trgr.dockets.core.entity.PublishingRequest;

public class JudicialDocketMetadataTest {

	private DocketVersion mockDocketVersion;
	private PublishingRequest mockPublishingRequest;
	private JudicialDocketMetadata JudicialDocketMetadataInstance;
	
	@Before
	public void setUp() {
		// Instantiate all mock objects and 'class under test' here
		mockPublishingRequest = EasyMock.createMock(PublishingRequest.class);
		mockDocketVersion = EasyMock.createMock(DocketVersion.class);
		JudicialDocketMetadataInstance = new JudicialDocketMetadata(mockPublishingRequest, mockDocketVersion, null, null);
	}

	/**
	 * Per bug 126366 - Mock up DocketEntity, Product, Court and make sure that the right Vendor for those combination is returned.
	 * This one is probably going to require a Vendor class to be existing (that would store everything), so this is a test that
	 * would be written during refactoring as opposed
	 */
	@Test
	public void testGetVendorValidVendorID() {
		EasyMock.expect(mockPublishingRequest.getVendorId()).andReturn(1L).times(2);

		EasyMock.replay(mockPublishingRequest);

		long returnedVendorID = JudicialDocketMetadataInstance.getVendor();
		Assert.assertEquals(1L, returnedVendorID);

		EasyMock.verify(mockPublishingRequest);
	}
	
	@Test(expected=NullPointerException.class)
	public void testGetVendorNullVendorID() {
		EasyMock.expect(mockPublishingRequest.getVendorId()).andReturn(null);

		EasyMock.replay(mockPublishingRequest);

		try
		{
			JudicialDocketMetadataInstance.getVendor();
			Assert.fail("NullPointerException not thrown");
		}
		catch (NullPointerException e)
		{
			EasyMock.verify(mockPublishingRequest);
			throw e;
		}
	}
	
	@Test(expected=NotImplementedException.class)
	public void testGetVendorZeroVendorID() {
		EasyMock.expect(mockPublishingRequest.getVendorId()).andReturn(0L);
		EasyMock.expect(mockDocketVersion.getDocket()).andReturn(new DocketEntity("","", new Court(0L)));

		EasyMock.replay(mockPublishingRequest);
		EasyMock.replay(mockDocketVersion);
		
		try
		{
			JudicialDocketMetadataInstance.getVendor();
			Assert.fail("NotImplementedException not thrown");
		}
		catch (NotImplementedException e)
		{
			EasyMock.verify(mockPublishingRequest);
			EasyMock.verify(mockDocketVersion);
			throw e;
		}
	}
	
	@Test
	public void testGetVendor98LCourtID() {
		testGetVendorCourtOptions(0, 98, 11);
	}
	@Test
	public void testGetVendor94LCourtID() {
		testGetVendorCourtOptions(0, 94, 7);
	}
	@Test
	public void testGetVendor95LCourtID() {
		testGetVendorCourtOptions(0, 95, 9);
	}
	@Test
	public void testGetVendor96LCourtID() {
		testGetVendorCourtOptions(0, 96, 9);
	}
	@Test
	public void testGetVendor97LCourtID() {
		testGetVendorCourtOptions(0, 97, 9);
	}
	
	private void testGetVendorCourtOptions(long VendorID, long CourtPrimaryKey, long AssertedValue) {
		EasyMock.expect(mockPublishingRequest.getVendorId()).andReturn(VendorID);
		EasyMock.expect(mockDocketVersion.getDocket()).andReturn(new DocketEntity("","", new Court(CourtPrimaryKey)));

		EasyMock.replay(mockPublishingRequest);
		EasyMock.replay(mockDocketVersion);

		long returnedVendorID = JudicialDocketMetadataInstance.getVendor();
		Assert.assertEquals(AssertedValue, returnedVendorID);

		EasyMock.verify(mockPublishingRequest);
		EasyMock.verify(mockDocketVersion);
	}

	
}
