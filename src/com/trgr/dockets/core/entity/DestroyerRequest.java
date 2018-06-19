package com.trgr.dockets.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Entity
@Table(name="DESTROYER_REQUEST", schema="DOCKETS_PUB")
public class DestroyerRequest implements Serializable {
	
	private static final long serialVersionUID = 7952335590772116295L;
	private UUID requestId;  	// primary key
	private String requestName;
	private String requestOwner;
	private Court court;
	private String collection;
	private String guids;
	private Status deleteStatus;
	private Date startDate;
	private Date endDate;
	private Date loadRequestDate;
	
	public DestroyerRequest() {
		super();
	}
	
	public DestroyerRequest(UUID requestId, String requestName,
			String requestOwner, Court court, String collection,
			String guids, Status deleteStatus, Date startDate, 
			Date endDate, Date loadRequestDate) 
	{
		setPrimaryKey(requestId);
		setRequestName(requestName);
		setRequestOwner(requestOwner);
		setCourt(court);
		setCollection(collection);
		setGuids(guids);
		setDeleteStatus(deleteStatus);
		setStartDate(startDate);
		setEndDate(endDate);
		setLoadRequestDate(loadRequestDate);
	}

	public DestroyerRequest(UUID requestId) {
		setPrimaryKey(requestId);
	}
	public DestroyerRequest(String requestId) throws UUIDException {
		setRequestId(requestId);
	}
	@Id
	@Column(name="REQUEST_ID")
	public String getRequestId() {
		return (requestId != null) ? requestId.toString() : null;
	}
	@Column(name="REQUEST_NAME", nullable=false)
	public String getRequestName() {
		return requestName;
	}
	@Column(name="REQUEST_OWNER", nullable=false)
	public String getRequestOwner() {
		return requestOwner;
	}
	@OneToOne
	@JoinColumn(name="COURT_ID", nullable=false)
	public Court getCourt() {
		return court;
	}
	@Column(name="COLLECTION", nullable=false)
	public String getCollection() {
		return collection;
	}
	@Column(name="GUIDS")
	public String getGuids() {
		return guids;
	}
	@OneToOne
	@JoinColumn(name="STATUS_ID", nullable=false)
	public Status getDeleteStatus() {
		return deleteStatus;
	}
	@Column(name="START_TIMESTAMP")
	public Date getStartDate() {
		return startDate;
	}
	@Column(name="END_TIMESTAMP")
	public Date getEndDate() {
		return endDate;
	}
	@Column(name="LOAD_REQUEST_DATE")
	public Date getLoadRequestDate() {
		return loadRequestDate;
	}



	
	@Transient
	public UUID getPrimaryKey() {
		return requestId;
	}
	@Transient
	public List<String> getGuidList() {
		List<String> guidList = null;
		if (guids != null && !guids.isEmpty() && guids.startsWith("[") && guids.endsWith("]")) {
			String[] guidTokens = guids.substring(1, guids.length() - 1).split(",");
			guidList = new ArrayList<String>();
			for (String guidToken : guidTokens) {
				guidList.add(guidToken.trim());
			}
		}
		return guidList;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setRequestId(String requestId) throws UUIDException {
		setPrimaryKey(new UUID(requestId));
	}
	public void setPrimaryKey(UUID requestId) {
		this.requestId = requestId;
	}
	public void setLoadRequestDate(Date loadRequestDate) {
		this.loadRequestDate = loadRequestDate;
	}
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	public void setRequestOwner(String requestOwner) {
		this.requestOwner = requestOwner;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setDeleteStatus(Status status) {
		this.deleteStatus = status;
	}
	public void setCourt(Court court) {
		this.court = court;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public void setGuids(String guids) {
		this.guids = guids;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DeleteRequest [requestId=");
		sb.append(requestId);
		sb.append(", requestName=");
		sb.append(requestName);
		sb.append(", requestOwner=");
		sb.append(requestOwner);
		sb.append(", startDate=");
		sb.append(startDate);
		sb.append(", endDate=");
		sb.append(endDate);
		sb.append(", loadRequestDate=");
		sb.append(loadRequestDate);
		sb.append(", deleteStatus=");
		sb.append(deleteStatus);
		sb.append(", court=");
		sb.append(court);
		sb.append(", collection=");
		sb.append(collection);
		sb.append(", guids=");
		sb.append(guids);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((collection == null) ? 0 : collection.hashCode());
		result = prime * result
				+ ((deleteStatus == null) ? 0 : deleteStatus.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((guids == null) ? 0 : guids.hashCode());
		result = prime * result
				+ ((loadRequestDate == null) ? 0 : loadRequestDate.hashCode());
		result = prime * result
				+ ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result
				+ ((requestName == null) ? 0 : requestName.hashCode());
		result = prime * result
				+ ((requestOwner == null) ? 0 : requestOwner.hashCode());
		result = prime * result
				+ ((court == null) ? 0 : court.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
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
		DestroyerRequest other = (DestroyerRequest) obj;
		if (collection == null) {
			if (other.collection != null)
				return false;
		} else if (!collection.equals(other.collection))
			return false;
		if (deleteStatus == null) {
			if (other.deleteStatus != null)
				return false;
		} else if (!deleteStatus.equals(other.deleteStatus))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (guids == null) {
			if (other.guids != null)
				return false;
		} else if (!guids.equals(other.guids))
			return false;
		if (loadRequestDate == null) {
			if (other.loadRequestDate != null)
				return false;
		} else if (!loadRequestDate.equals(other.loadRequestDate))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (requestName == null) {
			if (other.requestName != null)
				return false;
		} else if (!requestName.equals(other.requestName))
			return false;
		if (requestOwner == null) {
			if (other.requestOwner != null)
				return false;
		} else if (!requestOwner.equals(other.requestOwner))
			return false;
		if (court == null) {
			if (other.court != null)
				return false;
		} else if (!court.equals(other.court))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
}