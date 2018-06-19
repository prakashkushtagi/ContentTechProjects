/**
 * 
 */
package com.trgr.dockets.core.acquisition.service;

import java.io.File;

import com.trgr.dockets.core.acquisition.AcquisitionResponse;
import com.trgr.dockets.core.acquisition.WorkflowInformation;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.trgr.dockets.core.entity.PublishingRequest;

/**
 * @author C047166
 *
 */
public interface AcquisitionRecordAndActivitySetService
{
	/**
	 * Handles a new acquisition record. The acquisition record should be a xml record following 
	 * new.acquisition.record.dtd definition.
	 * This method parses the input string and inserts data in publishing_request, process, batch, batch_monitor,batch_docket tables
	 * in DOCKETS_PUB schema.
	 *  
	 * @param acquisitionRecordXml - acquisition record xml
	 * @param requestTypeEnum - a constant of type RequestTypeEnum
	 * @param workflowTypeEnum - a constant of type WorkflowTypeEnum
	 * 
	 * @return - a instance of AcquisitionResponse
	 * 
	 * @throws Exception - when failure happens
	 */
	public AcquisitionResponse processBKRAcquisitionRecord(File acquisitionRecordFile, RequestTypeEnum requestTypeEnum, WorkflowTypeEnum workflowTypeEnum, WorkflowInformation workflowInformation, PublishingRequest... publishingRequests) throws Exception;
		
	/**
	 * Handles a new acquisition record. The acquisition record should be a xml record following 
	 * new.acquisition.record.dtd definition.
	 * This method parses the input string and inserts data in publishing_request, process, batch, batch_monitor,batch_docket, docket, docket_version tables
	 * in DOCKETS_PUB schema.
	 *  
	 * @param acquisitionRecordXml - acquisition record xml
	 * 
	 * @return - a instance of AcquisitionResponse
	 * 
	 * @throws Exception - when failure happens
	 */
	public AcquisitionResponse processAcquisitionRecord(String acquisitionRecord) throws Exception;
	
	/**
	 * Handles a activity set record. It creates a process entry in DOCKETS_PUB.PROCESS table.
	 * 
	 * @param activitySetRecord
	 * @throws Exception
	 */
	public void processActivitySetRecord(String activitySetRecord) throws Exception;
	
	public void setRootWorkDirectory(String rootWorkDirectory);
	public void setPreDocketRootDirectoryState(String preDocketRootDirectoryState);
	public void setPreDocketRootDirectoryFederal(String preDocketRootDirectoryFederal);
	
	/**
	 * Process failed(Skipped,WebError) dockets log records, add place holder records for dockets,docket history, version table.
	 * @param acquisitionRecordStr
	 * @return 
	 * @throws Exception
	 */
	public void processFailedDocketRecord(String acquisitionRecordStr) throws Exception;
	
	/**
	 * Process docgrabber acquisition record.
	 * @param acquisitionRecordStr
	 * @return
	 * @throws Exception
	 */
	public void processDocGrabberAcquisitionRecord(String acquisitionRecordStr) throws Exception;

	/**
	 * Process docgrabber activitySet record.
	 * @param acquisitionOrActivityText
	 */
	public void processDocGrabberActivitySetRecord(String acquisitionOrActivityText)throws Exception;
	
	
}
