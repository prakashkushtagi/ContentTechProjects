/* Copyright 2017: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited. */
package com.trgr.dockets.core.job.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.trgr.dockets.core.entity.JobExecutionEntity;
import com.trgr.dockets.core.entity.JobParameterEntity;
import com.trgr.dockets.core.entity.JobParameterEntityKey;
import com.trgr.dockets.core.job.JobParameterKey;
import com.trgr.dockets.core.job.domain.JobFilter;
import com.trgr.dockets.core.job.domain.JobSort;
import com.trgr.dockets.core.job.domain.JobSummary;
import com.trgr.dockets.core.job.domain.JobSort.SortProperty;
import com.westgroup.publishingservices.uuidgenerator.UUID;

public class JobDaoImpl implements JobDao {
	
	private static final Logger log = Logger.getLogger(JobDaoImpl.class);
	private static final JobSummaryRowMapper JOB_SUMMARY_ROW_MAPPER = new JobSummaryRowMapper();
	private static final LongColumnRowMapper JOB_EXECUTION_ID_ROW_MAPPER = new LongColumnRowMapper("JOB_EXECUTION_ID");

	
	private String springBatchTablePrefix;
	private SessionFactory sessionFactory;
	private JdbcTemplate jdbcTemplate;
	
	public JobDaoImpl(SessionFactory sessionFactory, JdbcTemplate template, String springBatchTablePrefix) {
		this.sessionFactory = sessionFactory;
		this.jdbcTemplate = template;
		this.springBatchTablePrefix = springBatchTablePrefix;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<JobExecutionEntity> findJobExecutions(BatchStatus status) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(JobExecutionEntity.class);
		String statusValue = (status != null) ? status.toString() : null;
		criteria.add(Restrictions.eq("status", statusValue));
		return criteria.list();
	}
	
	public List<JobExecution> findJobExecutionsByUberBatchId(String uberBatchId) {
		return null;
	}
	
	@Override
	public JobParameterEntity findJobParameterByPrimaryKey(JobParameterEntityKey pk) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(JobParameterEntity.class);
		criteria.add(Restrictions.eq("primaryKey.jobExecutionId", pk.getJobExecutionId()));
		criteria.add(Restrictions.eq("primaryKey.keyName", pk.getKeyName()));
		JobParameterEntity param = (JobParameterEntity) criteria.uniqueResult();
		return param;
	}

	@Override
	public List<JobSummary> findJobSummary(List<Long> jobExecutionIds) {

		List<JobSummary> list = new ArrayList<JobSummary>(jobExecutionIds.size());
		for (long id : jobExecutionIds) {
			StringBuffer sql = new StringBuffer(
			"select instance.JOB_NAME, execution.JOB_INSTANCE_ID, execution.JOB_EXECUTION_ID, execution.STATUS, execution.START_TIME, execution.END_TIME ");
			sql.append("from ");
			sql.append(String.format("%sJOB_EXECUTION execution, %sJOB_INSTANCE instance ",
					   springBatchTablePrefix, springBatchTablePrefix));
			sql.append("where ");
			sql.append(String.format("(execution.JOB_EXECUTION_ID = %d) and (instance.JOB_INSTANCE_ID = execution.JOB_INSTANCE_ID)", id));
			List<JobSummary> rows = jdbcTemplate.query(sql.toString(), JOB_SUMMARY_ROW_MAPPER);
			if (rows.size() == 0) {
				log.warn(String.format("Job Execution ID %d was not found", id));
			} else if (rows.size() == 1) {
				JobSummary summary = rows.get(0);
				list.add(summary);
			} else {  // More than one result
				String mesg = String.format("Got %d job execution rows, expected 1", rows.size());
				log.error(mesg);
				throw new RuntimeException(mesg);
			}
		}
		
		// For each found job execution, fetch other job parameters from the BATCH_JOB_PARAMETERS table for the current execution.
		for (JobSummary summary : list) {
			JobParameterEntityKey jpKey = new JobParameterEntityKey(summary.getJobExecutionId(), JobParameterKey.BATCH_ID);
			JobParameterEntity jpe = findJobParameterByPrimaryKey(jpKey);
			summary.setBatchId((jpe!=null) ? jpe.getStringVal() : null);
		}
		return list;
	}
	
	@Override
	public List<Long> findJobExecutions(JobFilter filter, JobSort sort) {
//log.debug(filter + " " + sort);
		List<Long> jobExecutionIds = null;
		StringBuffer sql = new StringBuffer("select execution.JOB_EXECUTION_ID ");
		sql.append("from ");
		sql.append(String.format("%sJOB_EXECUTION execution ",springBatchTablePrefix));
		sql.append("where ");
		
		sql.append(addFiltersToQuery(filter));
		
		String orderByColumnName = getOrderByColumnName(sort.getSortProperty());
		String nullsPosition = (sort.isAscending() ? "first" : "last");
		sql.append(String.format("order by %s %s nulls %s", orderByColumnName, sort.getSortDirection(), nullsPosition));
//log.debug(sql);		
		Object[] args = createFilterArguments(filter);
		
		jobExecutionIds = jdbcTemplate.query(sql.toString(), JOB_EXECUTION_ID_ROW_MAPPER, args);
		
		return jobExecutionIds;
	}
	
	@Override
	public List<Long> findJobExecutionIdsByPublishingRequestId(UUID publishingRequestUuid) {
		String sql = String.format("select job_execution_id from %sJOB_EXECUTION_PARAMS where key_name = '%s' and string_val = '%s'",
						springBatchTablePrefix, JobParameterKey.PUBLISHING_REQUEST_ID, publishingRequestUuid.toString());
		List<Long> jobInstanceIds = jdbcTemplate.query(sql.toString(), JOB_EXECUTION_ID_ROW_MAPPER);
		return jobInstanceIds;
	}
	
	/* (non-Javadoc)
	 * @see com.thomsonreuters.uscl.dockets.batch.dao.JobDao#findJobInstanceIdsByBatchId(java.lang.String)
	 */
	@Override
	public List<Long> findJobExecutionIdsByBatchId(String batchId) {
		String sql = String.format("select job_execution_id from %sJOB_EXECUTION_PARAMS where key_name = '%s' and string_val = '%s'",
						springBatchTablePrefix, JobParameterKey.BATCH_ID, batchId);
		List<Long> jobInstanceIds = jdbcTemplate.query(sql.toString(), JOB_EXECUTION_ID_ROW_MAPPER);
		return jobInstanceIds;
	}
	
	@Override
	public List<Long> findTimedOutJobExecutions(String hostName) {
		List<Long> jobExecutionIds = null;
		StringBuffer sql = new StringBuffer("select je.JOB_EXECUTION_ID ");
		sql.append("from ");
		sql.append(String.format("%sJOB_EXECUTION je join ",springBatchTablePrefix));
		sql.append(String.format("%sJOB_EXECUTION_PARAMS jep ",springBatchTablePrefix));
		sql.append("on je.JOB_EXECUTION_ID = jep.JOB_EXECUTION_ID ");
		sql.append("where ");
		sql.append(String.format("(je.CREATE_TIME < ?) and (je.EXIT_CODE = '%s') ", BatchStatus.UNKNOWN.toString()));
		sql.append(String.format("and jep.key_name='hostName' and jep.string_val= ?", hostName));
		Calendar cal = Calendar.getInstance();
		int jobConsideredDeadIfNotFishishedInThisManyHours = 12;
		cal.add(Calendar.HOUR_OF_DAY, -jobConsideredDeadIfNotFishishedInThisManyHours);  // how many hours ago
		Object[] args = { cal.getTime(), hostName };
		jobExecutionIds = jdbcTemplate.query(sql.toString(), JOB_EXECUTION_ID_ROW_MAPPER, args);
		return jobExecutionIds;
	}
	

	
	/**
	 * Generate sql query that matches the filters user inputs
	 * @param filter
	 */
	private static String addFiltersToQuery(JobFilter filter) {
		StringBuffer sql = new StringBuffer();
		
		if (filter.getFrom() != null) {
		/* 	We want to show jobs that are in the STARTING status even if they have no start time because
			these are restarted jobs that are waiting for a thread (from the pool) to run and have not yet
			began to actually execute.  They will remain in the STARTING status until another job completes
			so the task can come off the ThreadPoolTaskExecutor blocking queue and be assigned a thread 
			from the pool when a thread frees up. */
			sql.append(String.format("((execution.STATUS = '%s') or (execution.START_TIME >= ?)) and ",
						BatchStatus.STARTING.toString()));
		}
		if (filter.getTo() != null) {
			sql.append("(execution.START_TIME < ?) and ");
		}
		if ((filter.getBatchStatus() != null) && (filter.getBatchStatus().length > 0)) {
			StringBuffer csvStatus = new StringBuffer();
			boolean firstTime = true;
			for (BatchStatus status : filter.getBatchStatus()) {
				if (!firstTime) {
					csvStatus.append(",");
				}
				firstTime = false;
				csvStatus.append(String.format("'%s'", status.toString()));
			}
			sql.append(String.format("(execution.STATUS in (%s)) and ", csvStatus));
		}
		
		sql.append("(1=1) "); // end of WHERE clause, ensure proper SQL syntax
		
		return sql.toString();
	}
	
	/**
	 * Creates the arguments list to use in jdbcTemplate.query.
	 * The order of the arguements being added needs to match the filter order in 
	 * @param filter
	 * @return
	 */
	private static Object[] createFilterArguments(JobFilter filter) {
		List<Object> args = new ArrayList<Object>();

		if (filter.getFrom() != null) {
			args.add(filter.getFrom());
		}
		if (filter.getTo() != null) {
			args.add(filter.getTo());
		}
		return args.toArray();
	}

	/**
	 * Map the sort column enumeration into the actual column identifier used in the HQL query.
	 * @param sortProperty enumerated value that reflects the database table sort column to sort on.
	 */
	private static String getOrderByColumnName(SortProperty sortProperty) {
		switch (sortProperty) {
			case JOB_EXECUTION_ID:
				return "execution.JOB_EXECUTION_ID";
			case JOB_INSTANCE_ID:
				return "execution.JOB_INSTANCE_ID";
			case BATCH_STATUS:
				return "execution.STATUS";
			case START_TIME:
				return "execution.START_TIME";
			default:
				throw new IllegalArgumentException("Unexpected sort property: " + sortProperty);
		}
	}
}

class JobSummaryRowMapper implements RowMapper<JobSummary> {
	@Override
	public JobSummary mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		String jobName = resultSet.getString("JOB_NAME");		
		Long jobExecutionId = resultSet.getLong("JOB_EXECUTION_ID");
		Long jobInstanceId = resultSet.getLong("JOB_INSTANCE_ID");
		BatchStatus batchStatus = BatchStatus.valueOf(resultSet.getString("STATUS"));
		Date startTime = resultSet.getTimestamp("START_TIME");
		Date endTime = resultSet.getTimestamp("END_TIME");
		JobSummary js = new JobSummary(jobName, jobInstanceId, jobExecutionId, batchStatus, null,
									   startTime, endTime);
		return js;
	}
}

//class JobExecutionIdRowMapper implements RowMapper<Long> {
//	public Long mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//		Long id = resultSet.getLong("JOB_EXECUTION_ID");
//		return id;
//	}
//}

class LongColumnRowMapper implements RowMapper<Long> {
	private String columnName;
	public LongColumnRowMapper(String columnName) {
		this.columnName = columnName;
	}
	@Override
	public Long mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Long id = resultSet.getLong(columnName);
		return id;
	}
}
