package com.trgr.dockets.core.entity;

import java.io.File;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;
import com.trgr.dockets.core.entity.CodeTableValues.RequestInitiatorTypeEnum;
import com.trgr.dockets.core.entity.CodeTableValues.WorkflowTypeEnum;



public abstract class PreProcessorRequest {
	protected static final String DATE_FORMAT_PATTERN = "yyyyMMdd.HHmmss";
	
	private String courtName;
	private File sourceFile;
	private RequestInitiatorTypeEnum requestInitiatorType;
	private ProductEnum product;
	private WorkflowTypeEnum workflowType;
		
	@XmlElement(name="courtName", required=false)
	public String getCourtName() {
		return courtName;
	}
	@XmlElement(name="sourceFile")
	private String getSourceFilename() {
		return (sourceFile != null) ? sourceFile.getPath() : null;
	}

	@XmlElement(name="requestInitiatorType")
	private String getRequestInitiatorTypeString() {
		return (requestInitiatorType != null) ? requestInitiatorType.name() : null;
	}
	@XmlElement(name="product")
	private String getProductString() {
		return (product != null) ? product.name() : null;
	}
	@XmlElement(name="workflowType")	
	private String getWorkflowTypeString() {
		return (workflowType != null) ? workflowType.getCode() : null;
	}
	
	/**
	 * Overridden in sub-class, but need to be here for the JAXB marshalling.
	 */
	@XmlTransient
	public Date getStartTime() {
		return null;
	}
	@XmlTransient
	public Date getEndTime() {
		return null;
	}
	@XmlTransient
	public File getSourceFile() {
		return sourceFile;
	}
	@XmlTransient
	public ProductEnum getProduct() {
		return product;
	}
	@XmlTransient
	public RequestInitiatorTypeEnum getRequestInitiatorType() {
		return requestInitiatorType;
	}
	@XmlTransient
	public WorkflowTypeEnum getWorkflowType() {
		return workflowType;
	}
	
	public void setProduct(ProductEnum product) {
		this.product = product;
	}
	protected void setProductString(String product) {
		setProduct((product != null) ? ProductEnum.valueOf(product) : null);
	}
	public void setRequestInitiatorType(RequestInitiatorTypeEnum type) {
		this.requestInitiatorType = type;
	}
	protected void setRequestInitiatorTypeString(String requestInitiatorType) {
		setRequestInitiatorType((requestInitiatorType != null) ? RequestInitiatorTypeEnum.valueOf(requestInitiatorType) : null);
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	public void setSourceFilename(String sourceFilename) {
		setSourceFile(StringUtils.isNotBlank(sourceFilename) ? new File(sourceFilename) : null);
	}
	public void setWorkflowType(WorkflowTypeEnum workflowType) {
		this.workflowType = workflowType;
	}
	protected void setWorkflowTypeString(String workflowTypeString) {
		setWorkflowType((workflowTypeString != null) ? WorkflowTypeEnum.findByCode(workflowTypeString) : null);
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((courtName == null) ? 0 : courtName.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime
				* result
				+ ((requestInitiatorType == null) ? 0 : requestInitiatorType
						.hashCode());
		result = prime * result
				+ ((sourceFile == null) ? 0 : sourceFile.hashCode());
		result = prime * result
				+ ((workflowType == null) ? 0 : workflowType.hashCode());
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
		PreProcessorRequest other = (PreProcessorRequest) obj;
		if (courtName == null) {
			if (other.courtName != null)
				return false;
		} else if (!courtName.equals(other.courtName))
			return false;
		if (product != other.product)
			return false;
		if (requestInitiatorType != other.requestInitiatorType)
			return false;
		if (sourceFile == null) {
			if (other.sourceFile != null)
				return false;
		} else if (!sourceFile.equals(other.sourceFile))
			return false;
		if (workflowType != other.workflowType)
			return false;
		return true;
	}
	
	@XmlTransient
	public abstract long getVendorId();
}
