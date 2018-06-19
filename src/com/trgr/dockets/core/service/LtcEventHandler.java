package com.trgr.dockets.core.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.trgr.dockets.core.domain.EventError;
import com.trgr.dockets.core.domain.EventXml;
import com.trgr.dockets.core.domain.ltc.EventCollection;
import com.trgr.dockets.core.domain.ltc.EventData;
import com.trgr.dockets.core.domain.ltc.LoadDataset;
import com.trgr.dockets.core.domain.ltc.LtcLoadEvent;
import com.trgr.dockets.core.exception.LoadMonitoringException;
import com.trgr.dockets.core.util.UUIDGenerator;


/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */


public class LtcEventHandler{
	EventData eventData = new EventData();
	EventXml eventXml = new EventXml();
	EventError eventError = new EventError();
	String eventId = null;


	//create Event, Batch and SubBatch Ids
	public EventXml prepareIds(LoadDataset loadDataset, EventData eventData) throws LoadMonitoringException, ParseException{
		
		//EventId generator
		String eventId = UUIDGenerator.createUuid().toString();
		eventXml.setEventId(eventId);
		
		//Batch, SubBatch generator
		if(loadDataset.getRequestCollaborationKey().contains("_")){
			String str[]=loadDataset.getRequestCollaborationKey().split("_");
			eventXml.setBatchId(str[0]);
			eventXml.setSubBatchId(str[1]);
		}
		
		else {throw new LoadMonitoringException("Cannot create Batch and SubBatch Id");}
		
		//Transform current event timestamp to match database timestamp
		SimpleDateFormat formatter, FORMATTER;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String oldDate = eventData.getEventTimestamp();
		Date date = formatter.parse(oldDate);
		FORMATTER = new SimpleDateFormat("dd-MMM-yy HH:mm:ss.SSSSSSSSS a");
		eventXml.setEventTimestamp(FORMATTER.format(date));
		
		return eventXml;			
	}
	
	
	//set column defaults for Event table
	public EventError setEventStatus(LtcLoadEvent ltcLoadEvent) throws LoadMonitoringException{
		
		if(ltcLoadEvent.getLoadStatus().equals("Load Complete")){
			eventError.setErrorCode("");
			eventError.setErrorDescription("");	
		}

		else if(ltcLoadEvent.getLoadStatus().equals("Terminated"))
		{	
			eventError.setErrorCode("LTCError100");
			eventError.setErrorDescription(ltcLoadEvent.getErrorDescription());
		}

		else if(ltcLoadEvent.getLoadStatus().equals("Failed in Process"))
		{	
			eventError.setErrorCode("LTCError101");
			eventError.setErrorDescription(ltcLoadEvent.getErrorDescription());
		}
		
		else if(ltcLoadEvent.getLoadStatus().equals("Completed")) 
		{
			eventError.setErrorCode("LTCError102");
			eventError.setErrorDescription("Load Complete but not promoted");
		}
		
		else{
			throw new LoadMonitoringException("Invalid Status");
		}
		eventXml.setEventError(eventError);

		return eventError;
		
	}
	
	
	//set eventName, payload and file name
	public EventXml setColumnData(EventCollection eventCollection,EventData eventData, LtcLoadEvent ltcLoadEvent, LoadDataset loadDataset, String stringMessage)
	throws LoadMonitoringException{
		
		if(loadDataset.getFileName() != null){
			eventXml.setFileName(loadDataset.getFileName());
		}
		else eventXml.setFileName("");
		
		
		eventXml.setEventName("dockets.novusload.end");
		eventXml.setPayload(("<![CDATA[" +stringMessage+ "]]>" ));
		eventXml.setAppServer("");
		eventXml.setNumDocs("");
		eventXml.setErrorFolder("");
		eventXml.setWorkFolder("");
		eventXml.setNovusLoadFolder("");
		eventXml.setDroppedDockets("");
		eventXml.setLogFolder("");
		
		return eventXml;
	}
	
	//create load history for Load Complete LTC events
	public void createLtcLoadHistory(String batchId, String subBatchId, String fileName, String timestamp){
		
	}
}