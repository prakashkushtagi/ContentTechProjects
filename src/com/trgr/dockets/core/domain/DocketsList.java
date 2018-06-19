/*
 * Copyright 2012: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited
 */
package com.trgr.dockets.core.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author <a href="mailto:mahendra.survase@thomsonreuters.com">Mahendra Survase</a> u0105927 
 *
 */
@XmlRootElement(name="dockets")
public class DocketsList {

	private List<Docket> docketList ;

	public DocketsList() {
		super();
	}

	public List<Docket> getDocketList() {
		return docketList;
	}

	@XmlElement(name="docket",required = true)
	public void setDocketList(List<Docket> docketList) {
		this.docketList = docketList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((docketList == null) ? 0 : docketList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocketsList other = (DocketsList) obj;
		if (docketList == null) {
			if (other.docketList != null)
				return false;
		} else if (!docketList.equals(other.docketList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DocketsList [docketList=" + docketList + "]";
	}
	
}
