/*
 * Copyright 2016: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.job;

/**
 * Job parameter keys used to access values from the JobParameter container.
 */
public class JobParameterKey {
	
	public static final String PUBLISHING_REQUEST_ID = "publishingRequestId";	// PK of PUBLISHING_REQUEST table
	public static final String UBER_BATCH_ID = "uberBatchId";	// PK Uber-Batch created by the preprocessor component
	public static final String BATCH_ID = "batchId";	// PK of BATCH table
	public static final String HOST_NAME = "hostName";		// hostname on which the job is being run
	public static final String TIMESTAMP = "timestamp"; 	// differentiates each job execution
	public static final String DISPATCHER_WORK_ID = "dispatcherWorkId";
	
	// Product Builder UI client related
	public static final String DELETE_OVERRIDE = "deleteOverride";
	public static final String PRISM_CLIP_DATE_OVERRIDE = "prismClipDateOverride";
	public static final String REQUEST_TYPE = "requestType";
	public static final String COLLECTION_NAME = "collectionName";
	public static final String NOVUS_LOAD_REQUEST = "novusLoadRequest";
	public static final String COLLECTION_ID = "collectionId";
	public static final String METADOC_COLLECTION_NAME = "metadocCollectionName"; 
	public static final String COLLECTION_NUMBER = "collectionNumber"; 
}
