package com.trgr.dockets.core.domain.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@XmlRootElement(name="simpleRestResponse") 
public class SimpleRestResponse {
	
	private boolean success;
	private String message;
	
	public SimpleRestResponse() {
		super();
	}
	
	public SimpleRestResponse(boolean success, String message) {
		setSuccess(success);
		setMessage(message);
	}
	
	@XmlElement(name="success")
	public boolean isSuccess() {
		return success;
	}
	@XmlElement(name="message")
	public String getMessage() {
		return message;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
