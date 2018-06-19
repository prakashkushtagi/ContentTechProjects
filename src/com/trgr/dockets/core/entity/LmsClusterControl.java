package com.trgr.dockets.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="LMS_CLUSTER_CONTROL", schema="DOCKETS_PREPROCESSOR")
public class LmsClusterControl extends AbstractClusterControl{

	public LmsClusterControl(){
		super();
	}
}
