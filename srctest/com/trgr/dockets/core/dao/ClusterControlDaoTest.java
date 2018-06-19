package com.trgr.dockets.core.dao;

import static com.trgr.dockets.core.dao.BaseDaoTest.equalsReqEq;
import static com.trgr.dockets.core.dao.ClusterControlDaoImpl.CLUSTER_ID;
import static org.junit.Assert.assertSame;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;

import com.trgr.dockets.core.entity.ClusterControl;
import com.trgr.dockets.core.entity.LmsClusterControl;

public class ClusterControlDaoTest {

	private ClusterControlDao clusterControlDao;

	private SessionFactory mockSessionFactory;
	private Session mockSession;
	private Criteria mockCriteria;

	@Before
	public void setUp() {

		mockSessionFactory = EasyMock.createMock(SessionFactory.class);
		mockSession = EasyMock.createMock(Session.class);
		mockCriteria = EasyMock.createMock(Criteria.class);
		clusterControlDao = new ClusterControlDaoImpl(mockSessionFactory);
	}

	@Test
	public void testFindClusterStatusByClusterId() {
		
		final long id = 123;
		final ClusterControl statusToFind = new ClusterControl();

		// Record expected behavior
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.createCriteria(ClusterControl.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.add(equalsReqEq(CLUSTER_ID,id))).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.uniqueResult()).andReturn(statusToFind);

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		assertSame(statusToFind, clusterControlDao.findClusterControlByClusterId(id));

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}	
	
	@Test
	public void testFindLmsClusterStatusByClusterId() {
		
		final long id = 123;
		final LmsClusterControl statusToFind = new LmsClusterControl();

		// Record expected behavior
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.createCriteria(LmsClusterControl.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.add(equalsReqEq(CLUSTER_ID,id))).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.uniqueResult()).andReturn(statusToFind);

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		assertSame(statusToFind, clusterControlDao.findLmsClusterControlByClusterId(id));

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}
}
