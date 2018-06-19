package com.trgr.dockets.core;




public class CoreConstants {
	
	public enum AcquisitionMethod { PROSPECTIVE, RETROSPECTIVE, SMARTTEMPLATE }
	public enum UpdateType { ADD, DELETE }
	public enum Phase { JUDICIAL, SOURCE, NOVUS, LOAD,ATTEMPTED }
	public enum JobClient { DATA_CAPTURE, PB_UI }
	
	//LTC Message element constants
	public static final String LTC_EVENT_COLLECTION = "event.collection";
	public static final String LTC_EVENT_DATA = "event.data";
	public static final String LTC_EVENT_DATETIME = "event.datetime";
	public static final String LTC_EVENT_ID =  "event.id";
	public static final String LTC_LOAD_COLLECTION = "load.collection";
	public static final String LTC_LOAD_STATUS = "load.status";
	public static final String LTC_LOAD_ELEMENT = "load.element";
	public static final String LTC_SYSTEM_MESSAGE = "system.message";
	public static final String LTC_LOAD_DATASET = "load.dataset";
	public static final String LTC_LOAD_DATASET_ID = "id";
	public static final String LTC_LOAD_DATASET_REQUEST_ID = "load.request.id";
	public static final String LTC_LOAD_DATASET_REQUEST_COLLAB_KEY = "request.collaboration.key";
	public static final String LTC_LOAD_COMPLETE_STATUS = "Load Complete";
	public static final String LTC_COMPLETED_STATUS = "Completed";
	public static final String LTC_LOG_FILENAME = "LtcRequest.log";
	public static final String LTC_ERROR_LOG_FILENAME = "LtcRequest-error.log";

	public static final String LTC_LOAD_ELEMENT_COMPLETED_STATUS = "Novus Promote";
	public static final String LTC_LOAD_ELEMENT_3RD_PARTY_TRANSFER ="3rd Party Transfer";
	
	/** This constant is used to retrieve the env variable defined on server */
	public static String SERVER_ENVIRONMENT_ATTTRIBUTE = "env";
		/** This constant is used for prefix to a JNDI local lookup */
	public static String JNDI_LOCAL_LOOKUP_PREFIX = "java:comp/env";
	
	

	//Source Control Loader Message element constants
	public static final String  SOURCE_LOAD_REQUEST = "sourceLoadRequest";
	public static final String  ACQUISITION_METHOD = "acquisitionMethod";
	public static final String  ACQUISITION_START = "acquisitionStart";
	public static final String  ACQUISITION_TYPE = "acquisitionType";
	public static final String  BATCHID = "batchId";
	public static final String  DELETE_FLAG = "deleteFlag";
	public static final String  PRODUCT_CODE = "productCode";
	public static final String  SOURCE_FILE = "sourceFile";
	
	public static final String DOCKET_HEADER_ELEMENT = "dockets";
	public static final String NOVUS_EXTRACT_HEADER_ELEMENT = "n-extract-response";
	public static final String NOVUS_N_DOCUMENT_ELEMENT = "n-document";
	public static final String DOCKET_ELEMENT = "docket";
	public static final String DELIMITER_COLON = ":";
	public static final String DOCKET_NUMBER_ATTR = "number";
	public static final String DOCKET_SCRAPE_DATE_ATTR = "scrape.date";
	public static final String DOCKET_COURT_ATTR = "court";
	public static final String DOCKET_PLATFORM_ATTR = "platform";
	public static final String STANDALONE_ENVIRONMENT = "workStation";
	public static final String NOVUS_MD_DOCKETNUM_ELEMENT = "md.docketnum";
	public static final String NOVUS_LEGACY_ID_ELEMENT = "legacy.id";
	public static final String NOVUS_MD_JURIS_ABBREV_FOR_COURT = "md.jurisabbrev";
	public static final String NOVUS_SCRAPE_DATE_ELEMENT = "scrape.date";
	public static final String NOVUS_DOCUMENT_GUID = "guid";
	public static final String DOCKET_STATUS_ATTR = "status";
	public static final String WORK_FOLDER = "workFolder";
	
	public static final String DOCKETS_BKR_CONTEXT_PREFIX = "Root/PublicRecords/FedDCTDocket_";
	public static final String JRECS_ENVIRONMENT_LEVEL = "active.level";
	public static final String LTC_LOAD_STATUS_VIEWER_URL = "ltc.lsv.url";
	public static final String LTC_LOAD_STATUS_VIEWER_TIME = "ltc.lsv.checkTime";
	public static final String LTC_LOAD_STATUS_VIEWER_DEFAULT_TIME = "24";
	public static final String JRECS_STANDALONE_ENVIRONMENT = "test";
	public static final String LTC_LOAD_STATUS_VIEWER_QA_URL = "http://ltcqa.int.westgroup.com/API/LoadStatusViewerAPI.htm";
	public static final String CORNERSTONE_CONFIG = "cornerstone.config";
	public static final String CORNERSTONE_CONFIG_LOCATION = "com/westgroup/cornerstone/conf/jrecsconfig.xml";
	public static final String DOCKETS_CONTENT_SERVICE = "DOCKETS_CONTENT_SERVICE";
	public static final Long ARTIFACT_DESCRIPTOR_ID = 10510L;
	public static final Long ARTIFACT_PHASE_SOURCE_ID = 2L;
	public static final String LITIGATOR_CONTENT = "LITIGATOR";
	public static final Long ARTIFACT_SOURCE_FORMAT_TYPE = 5L;
	public static final Long ARTIFACT_JUDICIAL_FORMAT_TYPE = 3L;
	public static final Long ARTIFACT_PHASE_JUDICIAL_ID = 3L;
	public static final String PUBLISHABLE_FLAG_YES = "Y";
	
	public static final String FILE_TYPE_XML = "xml";
	public static final String FILE_TYPE_GZIP = "gz";
	public static final String FILE_TYPE_ZIP = "zip";
	public static final String WORKFLOW_CONFIG_COMPONENT = "component";
	public static final String WORKFLOW_CONFIG_COMPONENT_NAME_ATTR = "name";
	public static final String WORKFLOW_CONFIG_COMPONENT_VALUE_ATTR = "value";
	public static final String WORKFLOW_CONFIG_COMPONENT_REL_LOADER = "relloader.properties";
	public static final String WORKFLOW_CONFIG_COMPONENT_RELEASE_FLAG = "release.flag";
	public static final String WORKFLOW_CONFIG_PARAMETER = "parameter";
	public static final String WORKFLOW_CONFIG = "workflow.config";
	public static final String LSV_LOAD_REQEUST = "loadrequest";
	public static final String LSV_DATASET="dataset";
	public static final String LSV_DATASET_NAME = "datasetname";
	public static final String LSV_LTC_STEP = "step";
	public static final String LSV_LTC_STEP_STATUS = "status";
	public static final String LSV_LTC_SYSTEM_MESSAGE = "systemmessage";
	public static final String  LTC_LOAD_STATUS_VIEWER_PASSWORD = "ltc.lsv.password";
	public static final String LTC_LOAD_STATUS_VIEWER_PASSWORD_DEFAULT = "corp3Ltc";
	public static final String LTC_LOAD_STATUS_VIEWER_USERNAME = "ltc.lsv.username";
	public static final String LTC_LOAD_STATUS_VIEWER_USERNAME_DEFAULT = "0065133";
	public static final String  REQUEST_OWNER_DATACAP = "datacap";
	
	// CWH
	public static final String CWH_LOG_FILENAME = "CWHTransit.log";
	public static final String CWH_ERROR_LOG_FILENAME = "CWHTransit-error.log";

	// NAL
	public static final String NAL_LOG_FILENAME = "NALTransit.log";
	public static final String NAL_ERROR_LOG_FILENAME = "NALTransit-error.log";

}
