/**
 * Copyright 2013: Thomson Reuters Global Resources. All Rights Reserved.
 *
 * Proprietary and Confidential information of TRGR.
 * Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.dao;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;

import com.trgr.dockets.core.entity.Court;
import com.trgr.dockets.core.entity.DocketVersion;

public class DocketDaoTest {
	private DocketDao classUnderTest;

	private SessionFactory mockSessionFactory;
	private Session mockSession;
	private Criteria mockCriteria;

	private String legacyId = "legacyIdValue";
	private long vendorId = 1;
	private long productId = 2;
	private Court court = new Court(3L);

	@Before
	public void setUp() {

		mockSessionFactory = EasyMock.createMock(SessionFactory.class);
		mockSession = EasyMock.createMock(Session.class);
		mockCriteria = EasyMock.createMock(Criteria.class);
		classUnderTest = new DocketDaoImpl(mockSessionFactory);
	}

	@Test
	public void testFindJudicialDocketVersionWithHighestVersionForVendorAndProduct() {

		// Record expected behavior
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.createCriteria(DocketVersion.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.add(((Criterion) EasyMock.anyObject()))).andReturn(mockCriteria).times(5);
		EasyMock.expect(mockCriteria.addOrder((Order) EasyMock.anyObject())).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.setMaxResults(1)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.uniqueResult()).andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		classUnderTest.findJudicialDocketVersionWithHighestVersion(legacyId, vendorId, productId, court);

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}

	@Test
	public void testFindSourceDocketVersionWithHighestVersionForVendorAndProduct() {

		// Record expected behavior
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.createCriteria(DocketVersion.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.add(((Criterion) EasyMock.anyObject()))).andReturn(mockCriteria).times(5);
		EasyMock.expect(mockCriteria.addOrder((Order) EasyMock.anyObject())).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.setMaxResults(1)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.uniqueResult()).andReturn(new DocketVersion());

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		classUnderTest.findSourceDocketVersionWithHighestVersionForCourtVendorAndProduct(legacyId, court, vendorId, productId);

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}

}