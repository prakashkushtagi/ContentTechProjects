package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Embeddable
public class NovusLoadFileKey implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1286078233173793613L;
	
	private String batchId;
	private String subBatchId;
	private String filename;
	
	public NovusLoadFileKey() {
		super();
	}

	public NovusLoadFileKey(String batchId, String subBatchId, String filename)
	{
		setBatchId(batchId);
		setSubBatchId(subBatchId);
		setFilename(filename);
	}
	
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}

		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((subBatchId == null) ? 0 : subBatchId.hashCode());
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
		NovusLoadFileKey other = (NovusLoadFileKey) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (subBatchId == null) {
			if (other.subBatchId != null)
				return false;
		} else if (!subBatchId.equals(other.subBatchId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * @return the batchId
	 */
	public String getBatchId() {
		return batchId;
	}

	/**
	 * @param batchId the batchId to set
	 */
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	/**
	 * @return the subBatchId
	 */
	public String getSubBatchId() {
		return subBatchId;
	}

	/**
	 * @param subBatchId the subBatchId to set
	 */
	public void setSubBatchId(String subBatchId) {
		this.subBatchId = subBatchId;
	}

	
}
