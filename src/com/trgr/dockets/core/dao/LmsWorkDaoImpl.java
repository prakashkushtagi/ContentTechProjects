package com.trgr.dockets.core.dao;

import static com.trgr.dockets.core.util.Environment.environmentIsWorkstation;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Table;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.trgr.dockets.core.entity.JobExecutionEntity;
import com.trgr.dockets.core.entity.LmsClusterControl;
import com.trgr.dockets.core.entity.LmsSpringBatchHost;
import com.trgr.dockets.core.entity.LmsWorkItem;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

public class LmsWorkDaoImpl extends BaseDaoImpl implements LmsWorkDao {

	public static final String CC = "cc";
	public static final String CC_ACTIVE_STRING = "cc.activeString";
	public static final String CLUSTER_CONTROL = "lmsClusterControl";
	public static final LmsWorkRowMapper LMS_WORK_ROW_MAPPER = new LmsWorkRowMapper();
	public static final String NEW_WORK_ITEMS_QUERY = "select * from DOCKETS_PREPROCESSOR." 
			+ LmsWorkItem.class.getAnnotation(Table.class).name()
			+ " lmsWork where lmsWork.start_time is null and lmsWork.server_name is null order by lmsWork.CREATE_TIME asc for update of lmsWork.WORK_ID skip locked";
	
	private JdbcTemplate jdbcTemplate;
	
	public LmsWorkDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public LmsWorkDaoImpl(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
		super(sessionFactory);
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	@Transactional
	public boolean isLoadMonitorHostInActiveCluster(String environment) throws UnknownHostException {
		if (environmentIsWorkstation(environment)) {
			return true;
		}
		final String serverName = InetAddress.getLocalHost().getHostName();
		final LmsSpringBatchHost host = findEntityById(LmsSpringBatchHost.class, serverName);
		if (host == null) {
			throw new UnknownHostException(serverName);
		}
		final LmsClusterControl clusterStatus = host.getLmsClusterControl();
		return (clusterStatus == null) || clusterStatus.isActive();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LmsSpringBatchHost> findAllActiveLoadMonitorHosts() {
		final Criteria criteria = createCriteria(LmsSpringBatchHost.class).
				createAlias(CLUSTER_CONTROL, CC).
				add(Restrictions.eq(CC_ACTIVE_STRING, "Y"));
		return criteria.list();
	}
	
	@Override
	@Transactional
	public void updateLmsWorkItem(LmsWorkItem lmsWorkItem) {
		super.update(lmsWorkItem);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LmsWorkItem> findAllLmsWorkItems() {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(LmsWorkItem.class)
				.addOrder(Order.asc("createTime"));
		return criteria.list();
	}

	@Override
	public List<LmsWorkItem> findNewLmsWorkItems() {
		List<LmsWorkItem> items = jdbcTemplate.query(NEW_WORK_ITEMS_QUERY, LMS_WORK_ROW_MAPPER);
		return items;
	}
	
	@Override
	@Transactional(readOnly = true)
	public LmsWorkItem findLmsWorkItemByMessageId(String messageId) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(LmsWorkItem.class);
		criteria.add(Restrictions.eq("messageId", messageId));
		return (LmsWorkItem) criteria.uniqueResult();	
	}
	
	@Override
	public JobExecutionEntity findJobExecutionByExecutionId(long executionId) {
		return findEntityById(JobExecutionEntity.class, executionId);
	}
}

class LmsWorkRowMapper implements RowMapper<LmsWorkItem> {
	private static final Logger log = Logger.getLogger(LmsWorkRowMapper.class);
	
	public LmsWorkItem mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		LmsWorkItem lmsWorkItem = new LmsWorkItem();
		lmsWorkItem.setPrimaryKey(Long.valueOf(resultSet.getString("WORK_ID")));
		try {
			lmsWorkItem.setPublishingRequestId(resultSet.getString("REQUEST_ID"));
		} catch (NumberFormatException nfe) {
			log.error(ExceptionUtils.getFullStackTrace(nfe));
		} catch (UUIDException uuide) {
			log.error(ExceptionUtils.getFullStackTrace(uuide));
		}
		lmsWorkItem.setMessageId(resultSet.getString("MESSAGE_ID"));
		lmsWorkItem.setProductCode(resultSet.getString("PRODUCT_CODE"));
		
		return lmsWorkItem;
	}
}
