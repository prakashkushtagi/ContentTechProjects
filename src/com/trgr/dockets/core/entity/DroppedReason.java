package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DROPPED_REASON", schema="DOCKETS_PUB")
public class DroppedReason implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long droppedReasonId;
	private String droppedReason;
	private String process;
	private boolean isSearchable;
	
	public DroppedReason() {
		super();
	}
	
	public DroppedReason(Long droppedReasonId, String droppedReason,
			String process, boolean isSearchable) {
		super();
		this.droppedReasonId = droppedReasonId;
		this.droppedReason = droppedReason;
		this.process = process;
		this.isSearchable = isSearchable;
	}
	
	@Id
	@Column(name="DROPPED_REASON_ID")
	public Long getDroppedReasonId() {
		return droppedReasonId;
	}

	public void setDroppedReasonId(Long droppedReasonId) {
		this.droppedReasonId = droppedReasonId;
	}

	@Column(name="DROPPED_REASON")
	public String getDroppedReason() {
		return droppedReason;
	}

	public void setDroppedReason(String droppedReason) {
		this.droppedReason = droppedReason;
	}

	@Column(name="PROCESS")
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	@Column(name="IS_SEARCHABLE")
	public boolean isSearchable() {
		return isSearchable;
	}

	public void setSearchable(boolean isSearchable) {
		this.isSearchable = isSearchable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((droppedReason == null) ? 0 : droppedReason.hashCode());
		result = prime * result
				+ ((droppedReasonId == null) ? 0 : droppedReasonId.hashCode());
		result = prime * result + (isSearchable ? 1231 : 1237);
		result = prime * result + ((process == null) ? 0 : process.hashCode());
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
		DroppedReason other = (DroppedReason) obj;
		if (droppedReason == null) {
			if (other.droppedReason != null)
				return false;
		} else if (!droppedReason.equals(other.droppedReason))
			return false;
		if (droppedReasonId == null) {
			if (other.droppedReasonId != null)
				return false;
		} else if (!droppedReasonId.equals(other.droppedReasonId))
			return false;
		if (isSearchable != other.isSearchable)
			return false;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DroppedReason [droppedReasonId=" + droppedReasonId
				+ ", droppedReason=" + droppedReason + ", process=" + process
				+ ", isSearchable=" + isSearchable + "]";
	}
	
	
}
