/**
 * 
 */
package com.trgr.dockets.core.acquisition;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Domain object used for the acquisition response.
 * 
 * @author C047166
 *
 */
public class AcquisitionResponse
{
	private String publishingRequestId;
	private String batchId;
	
	
	/**
	 * @param publishingRequestId
	 * @param batchId
	 */
	public AcquisitionResponse(String publishingRequestId, String batchId)
	{
		super();
		this.publishingRequestId = publishingRequestId;
		this.batchId = batchId;
	}
	
	public String getPublishingRequestId()
	{
		return publishingRequestId;
	}
	public void setPublishingRequestId(String publishingRequestId)
	{
		this.publishingRequestId = publishingRequestId;
	}
	public String getBatchId()
	{
		return batchId;
	}
	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((publishingRequestId == null) ? 0 : publishingRequestId.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AcquisitionResponse other = (AcquisitionResponse) obj;
		if (batchId == null)
		{
			if (other.batchId != null) return false;
		}
		else if (!batchId.equals(other.batchId)) return false;
		if (publishingRequestId == null)
		{
			if (other.publishingRequestId != null) return false;
		}
		else if (!publishingRequestId.equals(other.publishingRequestId)) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
