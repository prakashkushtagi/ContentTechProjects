package com.trgr.dockets.core.domain.ltc;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */

@XmlRootElement(name="event.collection")
public class EventCollection {
	
	private List<EventData> eventData;
	
	
	public EventCollection(){
		super();
	}
	
	public List<EventData> getEventData(){
		return eventData;
	}
	
	@XmlElement(name="event.data", required = true)
	public void setEventData(List<EventData> eventData){
		this.eventData = eventData;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventData == null) ? 0 : eventData.hashCode());
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
		EventCollection other = (EventCollection) obj;
		if (eventData == null) {
			if (other.eventData != null)
				return false;
		} else if (!eventData.equals(other.eventData))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventCollection [eventData=" + eventData + "]";
	}

	
	

}
