package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class DictionaryNormalizedKey implements Serializable {

	private static final long serialVersionUID = 6407236770619363056L;
	
	private Long courtId;
	private String originalName;
	private Long type;
	
	public DictionaryNormalizedKey() {
		super();
	}
	
	public DictionaryNormalizedKey(Long courtId, String originalName, Long type) {
		setCourtId(courtId);
		setOriginalName(originalName);
		setType(type);
	}

	public Long getCourtId() {
		return courtId;
	}

	public void setCourtId(Long courtId) {
		this.courtId = courtId;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((courtId == null) ? 0 : courtId.hashCode());
		result = prime * result
				+ ((originalName == null) ? 0 : originalName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		DictionaryNormalizedKey other = (DictionaryNormalizedKey) obj;
		if (courtId == null) {
			if (other.courtId != null)
				return false;
		} else if (!courtId.equals(other.courtId))
			return false;
		if (originalName == null) {
			if (other.originalName != null)
				return false;
		} else if (!originalName.equals(other.originalName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DictionaryNormalizedKey [courtId=" + courtId
				+ ", originalName=" + originalName + ", type=" + type + "]";
	}
}
