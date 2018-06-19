/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.domain.ltc;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 * 
 */

@XmlRootElement(name="event.text")
public class EventText {
	

	private LtcLoadEvent ltcLoadEvent;

	public EventText(){
		super();
	}
	

	public LtcLoadEvent getLtcLoadEvent(){
		return ltcLoadEvent;
	}
	
	@XmlElement(name="ltc.load.event")
	public void setLtcLoadEvent(LtcLoadEvent ltcLoadEvent){
		this.ltcLoadEvent = ltcLoadEvent;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ltcLoadEvent == null) ? 0 : ltcLoadEvent.hashCode());
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
		EventText other = (EventText) obj;
		if (ltcLoadEvent == null) {
			if (other.ltcLoadEvent != null)
				return false;
		} else if (!ltcLoadEvent.equals(other.ltcLoadEvent))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventText [ltcLoadEvent=" + ltcLoadEvent + "]";
	}




}
