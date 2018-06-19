package com.trgr.dockets.core.domain.ltc;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com)
 * 
 */


@XmlRootElement(name="event.data")
public class EventData {

	private String eventTimestamp;
	private EventText eventText;
	private String eventPayload;


	public String getEventPayload() {
		return eventPayload;
	}

	public void setEventPayload(String eventPayload) {
		this.eventPayload = eventPayload;
	}

	public EventText getEventText() {
		return eventText;
	}

	@XmlElement(name="event.text")
	public void setEventText(EventText eventText) {
		this.eventText = eventText;
	}

	public String getEventTimestamp(){
		return eventTimestamp;
	}
	
	@XmlElement(name="event.datetime")
	public void setEventTimestamp(String eventTimestamp){
		this.eventTimestamp = eventTimestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventPayload == null) ? 0 : eventPayload.hashCode());
		result = prime * result
				+ ((eventText == null) ? 0 : eventText.hashCode());
		result = prime * result
				+ ((eventTimestamp == null) ? 0 : eventTimestamp.hashCode());
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
		EventData other = (EventData) obj;
		if (eventPayload == null) {
			if (other.eventPayload != null)
				return false;
		} else if (!eventPayload.equals(other.eventPayload))
			return false;
		if (eventText == null) {
			if (other.eventText != null)
				return false;
		} else if (!eventText.equals(other.eventText))
			return false;
		if (eventTimestamp == null) {
			if (other.eventTimestamp != null)
				return false;
		} else if (!eventTimestamp.equals(other.eventTimestamp))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventData [eventTimestamp=" + eventTimestamp + ", eventText="
				+ eventText + ", eventPayload=" + eventPayload + "]";
	}
}
