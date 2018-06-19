package com.trgr.dockets.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.Assert;

@Entity
@Table(name="STG_PUB_UI_REQUEST", schema="DOCKETS_PUB")
public class StgPubUiRequest {
	
	private StgPubUiRequestKey primaryKey;
	private boolean processing;
	private CollectionEntity collection;

	public StgPubUiRequest() {
		super();
	}
	public StgPubUiRequest(StgPubUiRequestKey pk) {
		setPrimaryKey(pk);
	}

	@Id
	@AttributeOverrides({
			@AttributeOverride(name="publishingRequestId", column=@Column(name="REQUEST_ID")),
			@AttributeOverride(name="legacyId", column=@Column(name="LEGACY_ID"))
	})
	public StgPubUiRequestKey getPrimaryKey() {
		return primaryKey;
	}
	
	public void setPrimaryKey(StgPubUiRequestKey pk) {
		this.primaryKey = pk;
	}
	@Transient
	public boolean isProcess() {
		return processing;
	}
	@Column(name="PROCESSING_IND")
	public String getProcessingString() {
		return processing ? "Y" : "N";
	}
	
	@OneToOne
	@JoinColumn(name="COLLECTION_NAME", referencedColumnName="COLLECTION_NAME")
	public CollectionEntity getCollection()
	{
		return collection;
	}
	public void setCollection(CollectionEntity collection)
	{
		this.collection = collection;
	}
	public void setProcessingString(String yn) {
		Assert.isTrue("Y".equals(yn) || "N".equals(yn));
		this.processing = "Y".equals(yn);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
