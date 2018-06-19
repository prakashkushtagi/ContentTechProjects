/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.domain.ltc;

import java.util.ArrayList;
import java.util.List;

/**
 * LTCMessage. This POJO class represent information received from LTC.
 */
public class LtcMessage
{
	private List<LtcEvent> eventDataList;
	private String productCode;


	/**
	 * @return the eventDataList
	 */
	public List<LtcEvent> getEventDataList() {
		return eventDataList;
	}


	/**
	 * @param eventDataList the eventDataList to set
	 */
	public void setEventDataList(List<LtcEvent> eventDataList) {
		this.eventDataList = eventDataList;
	}

	/**
	 * 
	 * @param ltcEvent
	 */
	public void addLTCEvent(LtcEvent ltcEvent) {
		if(getEventDataList()==null)
		{
			this.eventDataList = new ArrayList<LtcEvent>();
		}
		getEventDataList().add(ltcEvent);
	}

	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
