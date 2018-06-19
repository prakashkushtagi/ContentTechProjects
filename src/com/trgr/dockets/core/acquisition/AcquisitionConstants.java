/*
 *  Copyright 2005
 *
 *  West, a Thomson Company
 *  All rights reserved
 *  This is the class for all the constants for the elements and attributes for the new aquisition record
 *  
 */
package com.trgr.dockets.core.acquisition;

public class AcquisitionConstants
{
    public static final String NEW_ACQUISITION_RECORD_ELEMENT = "new.acquisition.record";
    public static final String ACQUISITION_RECORD_VERSION_ATTRIBUTE = "acquisition.record.version";
    public static final String DCS_RECEIPT_ATTRIBUTE = "dcs.receipt.id";
    public static final String ACTIVITY_RECEIPT_ATTRIBUTE = "receipt.id";
    public static final String SENDER_ID_ATTRIBUTE = "sender.id";
    public static final String MERGED_FILE_NAME_ATTRIBUTE = "merged.file.name";
    public static final String MERGED_FILE_SIZE_ATTRIBUTE = "merged.file.size";
    public static final String SCRIPT_DATE_TIME_START_ATTRIBUTE = "script.start.date.time";
    public static final String SCRIPT_DATE_TIME_END_ATTRIBUTE = "script.end.date.time";
    public static final String ACTIVITY_DATE_TIME_START_ATTRIBUTE = "start.time";
    public static final String ACTIVITY_DATE_TIME_END_ATTRIBUTE = "end.time";
    public static final String ACTIVITY_SET_ELEMENT = "activity.set";
    public static final String ACTIVITY_RUN_ELEMENT = "activity.run";
    public static final String ACTIVITY_ELEMENT = "activity";
    public static final String DOCKET_CONVERSION_ACTIVITY_NAME = "DOCKETCONVERSION";
    public static final String SABER_ACTIVITY_NAME = "SABER";
    public static final String NOVUS_LOAD_ACTIVITY_NAME = "NOVUSLOAD";
    public static final String EHA_NOVUS_LOAD_ACTIVITY_NAME = "EHANOVUSLOAD";
    public static final String ACTIVITY_NAME_ATTRIBUTE = "name";
    public static final String RETRIEVE_TYPE_ATTRIBUTE = "retrieve.type";
    public static final String SOURCE_FILE_ATTRIBUTE = "source.file";
    public static final String DC_PROVIDER = "dc.provider";
    
    public static final String COURT_ELEMENT = "court";
    public static final String WESTLAW_CLUSTER_NAME_ATTRIBUTE = "westlaw.cluster.name";
    public static final String ACQUISITION_STATUS_ATTRIBUTE = "acquisition.status";
    public static final String COURT_TYPE_ATTRIBUTE = "court.type";
    
    public static final String WEBSITE_ERRORS_ELEMENT = "website.errors";
    public static final String WEB_ERROR_STATUS_ELEMENT = "web.error.status";
    public static final String WEB_ERROR_DOCKET_ELEMENT = "web.error.docket"; 
    public static final String REASON_ATTRIBUTE = "reason";
    public static final String WESTGET_STAGE_ATTRIBUTE = "westget.stage";
    public static final String STATUS_ATTRIBUTE = "status";
    public static final String CURL_CODE_ATTRIBUTE = "curl.code";
    public static final String HTTPERROR_CODE_ATTRIBUTE = "http.error.code";
    public static final String COURT_ERROR_MESSAGE_ATTRIBUTE = "court.error.message";
    
    public static final String SCRIPT_ERRORS_ELEMENT = "script.errors";
    public static final String SCRIPT_ERROR_STATUS_ELEMENT = "script.error.status";
    public static final String SCRIPT_ERROR_CODE_ATTRIBUTE = "script.error.code";
    public static final String SCRIPT_ERROR_MESSAGE_ATTRIBUTE = "script.error.message";
    
    public static final String SCRIPT_ERROR_DOCKET_LIST_ELEMENT = "script.error.docket.list";
    public static final String SCRIPT_ERROR_DOCKET_ELEMENT = "script.error.docket";
    public static final String SCRIPT_CODE_ATTRIBUTE = "script.code";
    
    public static final String OTHER_DATASOURCE_ERRORS_ELEMENT = "other.datasource.errors";
    public static final String OTHER_DATASOURCE_ERROR_DOCKET_ELEMENT = "other.datasource.error.docket";
    public static final String DATASOURCE_TYPE_ATTRIBUTE = "datasource.type";
    public static final String OTHER_ERROR_MESSAGE_ATTRIBUTE = "other.error.message";
    
    public static final String DELETED_DOCKETS_ELEMENT = "deleted.dockets";
    public static final String DELETE_DOCKET_STATUS_ELEMENT = "delete.docket.status";
    
    public static final String ACQUIRED_DOCKETS_ELEMENT = "acquired.dockets";
    public static final String ACQUIRED_DOCKET_STATUS_ELEMENT = "acquired.docket.status";
  
    public static final String SKIPPED_DOCKETS_ELEMENT = "skipped.dockets";
    public static final String SKIPPED_DOCKET_STATUS_ELEMENT = "skipped.dockets.status";
    
    public static final String SKIPPED_TYPE_OBJECT = "SKIPPED";
    public static final String WEB_ERRPR_TYPE_OBJECT = "WEB_ERROR";
    
    public static final String DCT_DOCKET_ELEMENT = "dct.docket";
    public static final String NUMBER_ATTRIBUTE = "docket.number";
    public static final String FILENAME_ATTRIBUTE = "filename";
    public static final String CASE_TYPE_ATTRIBUTE = "case.type";
    public static final String FILING_YEAR_ATTRIBUTE = "filing.year";
    public static final String SEQUENCE_NUMBER_ATTRIBUTE = "sequence.number";
    public static final String SUBFOLDER_ATTRIBUTE = "subfolder";
    public static final String FILING_LOCATION_ATTRIBUTE = "filing.location";
    public static final String SUBDIVISION_ATTRIBUTE = "subdivision";

    public static final String STATE_DOCKET_ELEMENT = "state.docket";
    public static final String ALT_DOCKET_ID_ATTRIBUTE = "alt.docket.id";
    
    public static final String BKR_DOCKET_ELEMENT = "bkr.docket";
    public static final String CTA_DOCKET_ELEMENT = "cta.docket";
    public static final String SCT_DOCKET_ELEMENT = "sct.docket";
    public static final String OTHER_DOCKET_ELEMENT = "other.docket";
    
    // pdf file names
    public static final String DOCKET_ENTRY_ELEMENT ="docket.entry";
    public static final String PDF_FILE_ELEMENT = "pdf.file";
    
    //email
    public static final String ACQUISITION_EMAIL_HOST_NAME = "relay.int.westgroup.com";
    public static final String ACQUISITION_EMAIL_FROM = "test@thomsonreuters.com";
    public static final String ACQUISITION_EMAIL_TO = "Thomson-DocketsEagan-QED@Thomsonreuters.com;Thomson-APSDocketsConversion@Thomsonreuters.com";
    public static final String ACQUISITION_EMAIL_SUBJECT = "Acquisition Request Failed in ";
    public static final String ACQUISITION_EMAIL_BODY = "Acquisition Request Failed for Acquisition Record ";
    
    public static final String GZIP_EXTENSION = ".gz";
    
    //pre docket source
    public static final String PRE_DOCKET_ELEMENT = "pre.docket";
    public static final String CASE_TITLE_ELEMENT = "case.title";
    public static final String FILING_DATE_ELEMENT = "filing.date";
    public static final String CASE_SUB_TYPE_ELEMENT = "casesubtype";
    public static final String PRE_DOCKET_CASE_TYPE = "casetype";
    
    public static final String PREDOCKET_ERROR_FILE = "PreDocket.err";
    public static final String PREDOCKET_LOG_FILE = "PreDocket.log";
    
    public static final String SKIPPED_DOCKET_ERROR_FILE = "SkippedDocket.err";
    public static final String SKIPPED_DOCKET_LOG_FILE = "SkippedDocket.log";

    
}
