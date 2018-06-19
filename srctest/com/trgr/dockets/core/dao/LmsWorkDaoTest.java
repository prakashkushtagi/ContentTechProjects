package com.trgr.dockets.core.dao;

import static com.trgr.dockets.core.dao.LmsWorkDaoImpl.CC;
import static com.trgr.dockets.core.dao.LmsWorkDaoImpl.CLUSTER_CONTROL;
import static com.trgr.dockets.core.dao.LmsWorkDaoImpl.LMS_WORK_ROW_MAPPER;
import static com.trgr.dockets.core.dao.LmsWorkDaoImpl.NEW_WORK_ITEMS_QUERY;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.trgr.dockets.core.entity.LmsSpringBatchHost;
import com.trgr.dockets.core.entity.LmsWorkItem;

public class LmsWorkDaoTest {

	private LmsWorkDao lmsWorkDao;

	private SessionFactory mockSessionFactory;
	private Session mockSession;
	private Criteria mockCriteria;
	private JdbcTemplate mockJdbcTemplate;

	@Before
	public void setUp() {
		mockSessionFactory = EasyMock.createMock(SessionFactory.class);
		mockSession = EasyMock.createMock(Session.class);
		mockCriteria = EasyMock.createMock(Criteria.class);
		mockJdbcTemplate = EasyMock.createMock(JdbcTemplate.class);
		lmsWorkDao = new LmsWorkDaoImpl(mockSessionFactory, mockJdbcTemplate);
	}

	@Test
	public void testIsLoadMonitorHostInActiveCluster() throws UnknownHostException {
		// Record expected behavior
		String hostName = InetAddress.getLocalHost().getHostName();
		LmsSpringBatchHost lmsSpringBatchHost = new LmsSpringBatchHost(hostName, 4L, "Y");
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.get(LmsSpringBatchHost.class, hostName)).andReturn(lmsSpringBatchHost);
		BaseDao mockBaseDao = EasyMock.createMock(BaseDaoImpl.class);
		EasyMock.expect(mockBaseDao.findEntityById(LmsSpringBatchHost.class, hostName)).andReturn(lmsSpringBatchHost);
		
		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);

		// Run test
		assertTrue(lmsWorkDao.isLoadMonitorHostInActiveCluster("testEnv"));

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
	}
	
	@Test
	public void testFindAllActiveLoadMonitorHosts() {
		// Record expected behavior
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.createCriteria(LmsSpringBatchHost.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.createAlias(CLUSTER_CONTROL, CC)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.add(((Criterion) EasyMock.anyObject()))).andReturn(mockCriteria).times(1);
		EasyMock.expect(mockCriteria.list()).andReturn(new ArrayList<LmsSpringBatchHost>());

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		lmsWorkDao.findAllActiveLoadMonitorHosts();

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}
	
	@Test
	public void testFindAllLmsWorkItems() {
		// Record expected behavior
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.createCriteria(LmsWorkItem.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.addOrder((Order) EasyMock.anyObject())).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.list()).andReturn(new ArrayList<LmsWorkItem>());

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		lmsWorkDao.findAllLmsWorkItems();

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}
	
	@Test
	public void testFindNewLmsWorkItems() {
		// Record expected behavior
		EasyMock.expect(mockJdbcTemplate.query(NEW_WORK_ITEMS_QUERY, LMS_WORK_ROW_MAPPER)).andReturn(new ArrayList<LmsWorkItem>());

		// Switch mocks to replay state
		EasyMock.replay(mockJdbcTemplate);

		// Run test
		lmsWorkDao.findNewLmsWorkItems();

		// Verify behavior of mocks
		EasyMock.verify(mockJdbcTemplate);
	}
	
	@Test
	public void testFindLmsWorkItemByMessageId() {
		// Record expected behavior
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
		EasyMock.expect(mockSession.createCriteria(LmsWorkItem.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.add(((Criterion) EasyMock.anyObject()))).andReturn(mockCriteria).times(1);
		EasyMock.expect(mockCriteria.uniqueResult()).andReturn(new LmsWorkItem());

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		lmsWorkDao.findLmsWorkItemByMessageId("messageId");

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}
}
