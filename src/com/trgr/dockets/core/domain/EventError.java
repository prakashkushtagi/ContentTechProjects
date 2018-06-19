package com.trgr.dockets.core.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="error")
public class EventError {

	private String errorCode;
	private String errorDescription;

	/**
	 * 
	 */
	public EventError()
	{
	}
	/**
	 * 
	 * @param errorCode
	 * @param errorDescription
	 */
	public EventError(String errorCode, String errorDescription)
	{
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	public String getErrorCode() {
		return errorCode;
	}

	@XmlAttribute(name = "code")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	@XmlAttribute(name= "description")
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime
				* result
				+ ((errorDescription == null) ? 0 : errorDescription.hashCode());
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
		EventError other = (EventError) obj;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (errorDescription == null) {
			if (other.errorDescription != null)
				return false;
		} else if (!errorDescription.equals(other.errorDescription))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventError [errorCode=" + errorCode + ", errorDescription="
				+ errorDescription + "]";
	}
	
	
	
	
	

}

