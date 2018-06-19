package com.trgr.dockets.core.dao;

import com.trgr.dockets.core.entity.ClusterControl;
import com.trgr.dockets.core.entity.LmsClusterControl;

public interface ClusterControlDao {

	ClusterControl findClusterControlByClusterId(long id);
	
	LmsClusterControl findLmsClusterControlByClusterId(long id);
}
