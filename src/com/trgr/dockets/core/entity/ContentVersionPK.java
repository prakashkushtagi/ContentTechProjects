package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ContentVersionPK  implements Serializable {
	private static final long serialVersionUID = 4569917980378635981L;
	
	private Long versionId;
    private String contentUuid;
    
    public ContentVersionPK() {
    	super();
    }
    public ContentVersionPK(String contentUuid, Long versionId) {
		this.contentUuid = contentUuid;
		this.versionId = versionId;
	}

	/**
	 * @return the versionId
	 */
	public Long getVersionId() 
	{
		return versionId;
	}

	/**
	 * @param versionId the versionId to set
	 */
	public void setVersionId(Long versionId) 
	{
		this.versionId = versionId;
	}

	/**
	 * @return the contentUuid
	 */
	public String getContentUuid() 
	{
		return contentUuid;
	}

	/**
	 * @param contentUuid the contentUuid to set
	 */
	public void setContentUuid(String contentUuid) 
	{
		this.contentUuid = contentUuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentUuid == null) ? 0 : contentUuid.hashCode());
		result = prime * result
				+ ((versionId == null) ? 0 : versionId.hashCode());
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
		ContentVersionPK other = (ContentVersionPK) obj;
		if (contentUuid == null) {
			if (other.contentUuid != null)
				return false;
		} else if (!contentUuid.equals(other.contentUuid))
			return false;
		if (versionId == null) {
			if (other.versionId != null)
				return false;
		} else if (!versionId.equals(other.versionId))
			return false;
		return true;
	} 
}
