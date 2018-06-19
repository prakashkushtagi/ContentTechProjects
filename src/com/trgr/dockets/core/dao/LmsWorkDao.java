package com.trgr.dockets.core.dao;

import java.net.UnknownHostException;
import java.util.List;

import com.trgr.dockets.core.entity.JobExecutionEntity;
import com.trgr.dockets.core.entity.LmsSpringBatchHost;
import com.trgr.dockets.core.entity.LmsWorkItem;

public interface LmsWorkDao extends BaseDao {
	
	public boolean isLoadMonitorHostInActiveCluster(String environment) throws UnknownHostException;
	
	public List<LmsSpringBatchHost> findAllActiveLoadMonitorHosts();
	
	public void updateLmsWorkItem(LmsWorkItem lmsWorkItem);
	
	public List<LmsWorkItem> findAllLmsWorkItems();
	
	public List<LmsWorkItem> findNewLmsWorkItems();
	
	public LmsWorkItem findLmsWorkItemByMessageId(String messageId);
	
	public JobExecutionEntity findJobExecutionByExecutionId(long executionId);
	
}
