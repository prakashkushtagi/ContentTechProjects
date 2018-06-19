/**Copyright 2015: Thomson Reuters. All Rights Reserved. 
 * Proprietary and Confidential information of Thomson Reuters. 
 * Disclosure, Use or Reproduction without the written 
 * authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;
import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@XmlRootElement(name="jobRequest") 
public class PBUIRequest extends PreProcessorRequest{
	
	/** The legal values for the name attribute of the parameter element. */
	public enum ParameterNameEnum { deleteOverride, prismClipDateOverride, product, requestId, 
									requestInitiatorType, requestName, requestOwner, requestType, 
									startTime, workflowType, sourceFile, metadataFile, courtName, 
									fifoOverride, priority, sharedServicePriority, 
									rpxTimeoutOverride, releaseFlagOverride, vendorId, aqTimeOverride, useDefaultBatchSize};
									
	
	private Parameters parameters = new Parameters();
									
	@XmlTransient
	public Boolean isDeleteOverride() {
		String value = findParameterValue(ParameterNameEnum.deleteOverride);
		return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : null;
	}
	@XmlTransient
	public Boolean isPrismClipDateOverride() {
		String value = findParameterValue(ParameterNameEnum.prismClipDateOverride);
		return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : null;
	}
	
	@XmlTransient
	public Boolean isFifoOverride() {
		String value = findParameterValue(ParameterNameEnum.fifoOverride);
		return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : false;
	}
	@XmlTransient
	public long getPublishingPriority() {
		String publishingPriorityString = findParameterValue(ParameterNameEnum.priority);
		return (publishingPriorityString != null) ? Long.valueOf(publishingPriorityString) : 1l;  // default is 1, 0 is "on hold" 
	}
	@XmlTransient
	public Boolean isUseDefaultBatchSize() {
		String value = findParameterValue(ParameterNameEnum.useDefaultBatchSize);
		return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : false;
	}
	@XmlTransient
	public String getSharedServicePriority() {
		String sharedServicePriorityString = findParameterValue(ParameterNameEnum.sharedServicePriority);
		return (sharedServicePriorityString != null) ? sharedServicePriorityString : "MEDIUM";  // default is MEDIUM
	}
	@XmlTransient
	public ProductEnum getProduct() {
		return ProductEnum.findByCode(findParameterValue(ParameterNameEnum.product));
	}
	@XmlTransient
	public UUID getRequestKey() {
		UUID uuid = null;
		String uuidString = findParameterValue(ParameterNameEnum.requestId);
		if (StringUtils.isNotBlank(uuidString)) {
			try { uuid = new UUID(uuidString); }
			catch (UUIDException e) { uuid = null; }
		}
		return uuid;
	}
	@XmlTransient
	public String getRequestName() {
		return findParameterValue(ParameterNameEnum.requestName);
	}
	@XmlTransient
	public String getRequestOwner() {
		return findParameterValue(ParameterNameEnum.requestOwner);
	}
	@XmlTransient
	public RequestTypeEnum getRequestType() {
		return RequestTypeEnum.findByDescription(findParameterValue(ParameterNameEnum.requestType));
	}
	@XmlTransient
	public RequestInitiatorTypeEnum getRequestInitiatorType() {
		return RequestInitiatorTypeEnum.findByCode(findParameterValue(ParameterNameEnum.requestInitiatorType));
	}
	@XmlTransient
	public Date getStartTime() {
		String startTime = findParameterValue(ParameterNameEnum.startTime);  // formatted start time
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
			return (StringUtils.isNotBlank(startTime) ? sdf.parse(startTime) : null);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	@XmlTransient
	public WorkflowTypeEnum getWorkflowType() {
		return WorkflowTypeEnum.findByCode(findParameterValue(ParameterNameEnum.workflowType));
	}
	
	@XmlTransient
	public File getSourceFile() {
		String srcFile = findParameterValue(ParameterNameEnum.sourceFile);
		File file = new File(srcFile);
		return file;
	}
	
	@XmlTransient
	public File getMetadataFile() {
		String metadataFile = findParameterValue(ParameterNameEnum.metadataFile);
		File file = new File(metadataFile);
		return file;
	}
	
	@XmlTransient
	public String getCourtName() {
		return findParameterValue(ParameterNameEnum.courtName);
	}
	@XmlTransient
	public String getReleaseFlagOverride() {
		return findParameterValue(ParameterNameEnum.releaseFlagOverride);
	}
	@XmlTransient
	public Long getRpxTimeoutOverride() {
		String rpxTimeoutOverrideString = findParameterValue(ParameterNameEnum.rpxTimeoutOverride);
		return (StringUtils.isNotBlank(rpxTimeoutOverrideString) ? new Long(rpxTimeoutOverrideString) : 0);  // return 0 if there is no vendor on the request 
	}
	@XmlTransient
	public long getVendorId() {
		String vendorId = findParameterValue(ParameterNameEnum.vendorId);
		return  (StringUtils.isNotBlank(vendorId) ? new Long(vendorId) : 0l); //default VendorId to 0 if not provided.
	}
	
	@XmlTransient
	public Boolean isAqTimeOverride() {
		String value = findParameterValue(ParameterNameEnum.aqTimeOverride);
		return StringUtils.isNotBlank(value) ? Boolean.valueOf(value) : false;
	}
	
	private Parameter findParameter(ParameterNameEnum searchName) {
		List<Parameter> parameterList = getParameters().getParameterList();
		for (Parameter param : parameterList) {
			if (param.getName().equals(searchName.name())) {
				return param;
			}
		}
		// Not found, so create it
		Parameter param = new Parameter(searchName.name(), null);
		parameterList.add(param);  // Add it to the set
		return param;
	}
	private String findParameterValue(ParameterNameEnum searchName) {
		Parameter param = findParameter(searchName);
		return (param != null) ? param.getValue() : null;
	}
	
	@XmlElement(name="parameters")
	public Parameters getParameters() {
		return parameters;
	}
	
	protected void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	@XmlType
	public static class Parameters {

		private List<Parameter> parameterList = new ArrayList<Parameter>();
		
		public void addParameter(Parameter param) {
			parameterList.add(param);
		}
		
		@XmlElement(name="parameter")
		public List<Parameter> getParameterList() {
			return parameterList;
		}
		public void setParameterList(List<Parameter> parameters) {
			this.parameterList = parameters;
		}
		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}
	
	@XmlType
	public static class Parameter {
		
		String name;
		String value;
		
		public Parameter() {
			super();
		}
		public Parameter(String name, String value) {
			setName(name);
			setValue(value);
		}
		
		@XmlAttribute(name="name")
		public String getName() {
			return name;
		}
		@XmlValue
		public String getValue() {
			return value;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setValue(String value) {
			this.value = value;
		}
		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		}
	}

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PBUIRequest other = (PBUIRequest) obj;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}
}
