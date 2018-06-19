package com.trgr.dockets.core.dao;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.junit.Before;
import org.junit.Test;

public class BaseDaoTest {
	
	private BaseDao baseDao;

	private SessionFactory mockSessionFactory;
	private Session mockSession;
	private Criteria mockCriteria;

	@Before
	public void setUp() {
		
		mockSessionFactory = EasyMock.createMock(SessionFactory.class);
		mockSession = EasyMock.createMock(Session.class);
		mockCriteria = EasyMock.createMock(Criteria.class);
		baseDao = new BaseDaoImpl(mockSessionFactory);
		
		EasyMock.expect(mockSessionFactory.getCurrentSession()).andReturn(mockSession);
	}

	@Test
	public void testClearAndFlush() {

		// Record expected behavior
		mockSession.clear();
		EasyMock.expectLastCall();
		mockSession.flush();
		EasyMock.expectLastCall();

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);

		// Run test
		baseDao.clearAndFlush();

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
	}
	
	@Test
	public void testDelete() {
		
		final Object objectToDelete = new Object();

		// Record expected behavior
		mockSession.delete(objectToDelete);
		EasyMock.expectLastCall();

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);

		// Run test
		baseDao.delete(objectToDelete);

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
	}
	
	@Test
	public void testFindAllEntities() {
		
		final ArrayList<Object> objectsToGet = new ArrayList<Object>();
		objectsToGet.add(new Object());
		objectsToGet.add(new Object());
		
		// Record expected behavior
		EasyMock.expect(mockSession.createCriteria(Object.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.list()).andReturn(objectsToGet);
		

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		assertSame(objectsToGet, baseDao.findAllEntities(Object.class));

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}
	
	@Test
	public void testFindEntityByField() {
		
		final String fieldName = "fieldName";
		final String fieldValue = "fieldValue";
		final Object objectToGet = new Object();
		
		// Record expected behavior
		EasyMock.expect(mockSession.createCriteria(Object.class)).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.add(equalsReqEq(fieldName,fieldValue))).andReturn(mockCriteria);
		EasyMock.expect(mockCriteria.uniqueResult()).andReturn(objectToGet);

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);
		EasyMock.replay(mockCriteria);

		// Run test
		assertSame(objectToGet,baseDao.findEntityByField(Object.class,fieldName,fieldValue));

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
		EasyMock.verify(mockCriteria);
	}
	
	public static Criterion equalsReqEq(final String name, final Object value) {
		final SimpleExpression expectedSimpExp = Restrictions.eq(name,value);
		EasyMock.reportMatcher(new IArgumentMatcher() {

			@Override
			public void appendTo(final StringBuffer buffer) {
				buffer.append("criterion matcher");
			}

			@Override
			public boolean matches(final Object actualSimpExp) {
				return (actualSimpExp instanceof SimpleExpression) &&
						expectedSimpExp.toString().equals(actualSimpExp.toString());
			}});
		return null;
	}
	
	@Test
	public void testFindEntityById() {
		
		final String id = "id";
		final Object objectToGet = new Object();
		
		// Record expected behavior
		EasyMock.expect(mockSession.get(Object.class,"id")).andReturn(objectToGet);

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);

		// Run test
		assertSame(objectToGet, baseDao.findEntityById(Object.class, id));

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
	}
	
	@Test
	public void testSave() {
		
		final Object objectToSave = new Object();

		// Record expected behavior
		mockSession.saveOrUpdate(objectToSave);
		EasyMock.expectLastCall();

		// Switch mocks to replay state
		EasyMock.replay(mockSessionFactory);
		EasyMock.replay(mockSession);

		// Run test
		baseDao.save(objectToSave);

		// Verify behavior of mocks
		EasyMock.verify(mockSessionFactory);
		EasyMock.verify(mockSession);
	}

}
