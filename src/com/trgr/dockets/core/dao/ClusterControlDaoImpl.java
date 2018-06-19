package com.trgr.dockets.core.dao;

import org.hibernate.SessionFactory;

import com.trgr.dockets.core.entity.ClusterControl;
import com.trgr.dockets.core.entity.LmsClusterControl;

public class ClusterControlDaoImpl extends BaseDaoImpl implements ClusterControlDao {
	
	public static final String CLUSTER_ID = "clusterId";

	public ClusterControlDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public ClusterControl findClusterControlByClusterId(final long id) {
		return findEntityByField(ClusterControl.class,CLUSTER_ID,id);
	}
	
	@Override
	public LmsClusterControl findLmsClusterControlByClusterId(final long id) {
		return findEntityByField(LmsClusterControl.class,CLUSTER_ID,id);
	}

}
