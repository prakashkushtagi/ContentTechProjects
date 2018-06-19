package com.trgr.dockets.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.Court;

/**
 * Summary metadata read from the acquisition log file created by data capture following 
 * the Docket data content scrape.
 */
public class SourceMetadata implements Serializable {
	
	private static final long serialVersionUID = -1228467670293340960L;

	private Court court;	// court
	private RequestInitiatorTypeEnum requestInitiatorType;  // download.type
	private ProductEnum product;
	private Date startTime;
	private Date endTime;
	/** The raw docket number values as consumed from the file. */
	private List<String> docketNumbers;
	private Map<String, String> docNoAq; //Bug 133903

	public SourceMetadata() {
		this.docketNumbers = new ArrayList<String>();
		this.docNoAq = new HashMap<String, String>();
	}
	
	public Court getCourt() {
		return court;
	}

	public RequestInitiatorTypeEnum getRequestInitiatorType() {
		return requestInitiatorType;
	}
	public ProductEnum getProduct() {
		return product;
	}
	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public List<String> getDocketNumbers() {
		return Collections.unmodifiableList(docketNumbers);
	}
	//Bug 133903
	public Map<String, String> getDocNoAq() {
		return Collections.<String, String>unmodifiableMap(docNoAq);
	}
	//End

	public void setCourt(Court court) {
		this.court = court;
	}

	public void setRequestInitiatorType(RequestInitiatorTypeEnum type) {
		this.requestInitiatorType = type;
	}
	public void setProduct(ProductEnum product) {
		this.product = product;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void addDocketNumber(String docketNumber) {
		if (!docketNumbers.contains(docketNumber)) {
			docketNumbers.add(docketNumber);
		}
	}
	//Bug 133903
	public void addDocNoAq(String docNumber, String AcqDate) {
		if (!docNoAq.containsKey(docNumber)) {
			docNoAq.put(docNumber,AcqDate);
		}
	}
	//End
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
