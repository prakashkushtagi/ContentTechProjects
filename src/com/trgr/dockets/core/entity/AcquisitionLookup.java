package com.trgr.dockets.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.westgroup.publishingservices.uuidgenerator.UUID;
import com.westgroup.publishingservices.uuidgenerator.UUIDException;

@Entity
@Table(name="ACQUISITION_LOOKUP", schema="DOCKETS_PUB")
public class AcquisitionLookup {
	
	private UUID primaryKey;  	//  request_id
	private String requestId;
	private String receiptId;
	
	
	/**
	 * Default constructor
	 */
	public AcquisitionLookup()
	{
		super();
	}

	/**
	 * @param primaryKey
	 * @param receiptId
	 */
	public AcquisitionLookup(UUID primaryKey, String receiptId) {
		setPrimaryKey(primaryKey);
		setReceiptId(receiptId);
	}
	
	@Id
	@Column(name="REQUEST_ID")
	public String getRequestId() {
		return (primaryKey != null) ? primaryKey.toString() : null;
	}
	@Transient
	public UUID getPrimaryKey() {
		return primaryKey;
	}
	public void setRequestId(String requestId) throws UUIDException {
		setPrimaryKey(new UUID(requestId));
	}
	public void setPrimaryKey(UUID requestId) {
		this.primaryKey = requestId;
	}
	
	@Column(name="RECEIPT_ID")
	public String getReceiptId(){
		return receiptId;
	}

	public void setReceiptId(String receiptId){
		this.receiptId = receiptId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AcquisitionLookup other = (AcquisitionLookup) obj;
		if (requestId == null)
		{
			if (other.requestId != null) return false;
		}
		else if (!requestId.equals(other.requestId)) return false;
		return true;
	}
	
}
