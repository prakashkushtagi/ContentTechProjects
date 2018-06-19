/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.domain.ltc;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 * 
 */

@XmlRootElement(name="ltc.load.event")
public class LtcLoadEvent {

	private String loadStatus;
	private String errorDescription;
	private LoadDataset loadDataset;

	public LtcLoadEvent(){
		super();
	}


	public LoadDataset getLoadDataset() {
		return loadDataset;
	}

	@XmlElement(name="load.dataset")
	public void setLoadDataset(LoadDataset loadDataset) {
		this.loadDataset = loadDataset;
	}

	public String getLoadStatus(){
		return loadStatus;
	}
	
	@XmlElement(name="load.status")
	public void setLoadStatus(String loadStatus){
		this.loadStatus= loadStatus;
	}
	
	public String getErrorDescription(){
		return errorDescription;
	}
	
	@XmlElement(name="system.message")
	public void setErrorDescription(String errorDescription){
		this.errorDescription= errorDescription;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((errorDescription == null) ? 0 : errorDescription.hashCode());
		result = prime * result
				+ ((loadDataset == null) ? 0 : loadDataset.hashCode());
		result = prime * result
				+ ((loadStatus == null) ? 0 : loadStatus.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LtcLoadEvent other = (LtcLoadEvent) obj;
		if (errorDescription == null) {
			if (other.errorDescription != null)
				return false;
		} else if (!errorDescription.equals(other.errorDescription))
			return false;
		if (loadDataset == null) {
			if (other.loadDataset != null)
				return false;
		} else if (!loadDataset.equals(other.loadDataset))
			return false;
		if (loadStatus == null) {
			if (other.loadStatus != null)
				return false;
		} else if (!loadStatus.equals(other.loadStatus))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LtcLoadEvent [loadStatus=" + loadStatus + ", errorDescription="
				+ errorDescription + ", loadDataset=" + loadDataset + "]";
	}



	
}
