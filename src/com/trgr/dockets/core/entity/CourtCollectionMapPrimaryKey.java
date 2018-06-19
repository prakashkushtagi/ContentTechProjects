/*Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.*/
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class CourtCollectionMapPrimaryKey implements Serializable
{
	private static final long serialVersionUID = 1L;
	private Long courtId;
	private String mapKey;
	
	public Long getCourtId()
	{
		return courtId;
	}
	public void setCourtId(Long courtId)
	{
		this.courtId = courtId;
	}
	public String getMapKey()
	{
		return mapKey;
	}
	public void setMapKey(String mapKey)
	{
		this.mapKey = mapKey;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courtId == null) ? 0 : courtId.hashCode());
		result = prime * result + ((mapKey == null) ? 0 : mapKey.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		CourtCollectionMapPrimaryKey other = (CourtCollectionMapPrimaryKey) obj;
		if (courtId == null)
		{
			if (other.courtId != null) return false;
		}
		else if (!courtId.equals(other.courtId)) return false;
		if (mapKey == null)
		{
			if (other.mapKey != null) return false;
		}
		else if (!mapKey.equals(other.mapKey)) return false;
		return true;
	}
	
	
}
