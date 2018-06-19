package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="PUBLISHING_PROCESS_CONTROL", schema="DOCKETS_PUB")
public class PublishingProcessControl implements Serializable {
	private static final long serialVersionUID = 8328812258469281338L;
	
	private PublishingProcessControlKey primaryKey;
	private boolean active;
	
	public PublishingProcessControl() {
		super();
	}
	public PublishingProcessControl(PublishingProcessControlKey pk, boolean active) {
		setPrimaryKey(pk);
		setActive(active);
	}
	
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="publishingControlId", column=@Column(name="PUBLISHING_CONTROL_ID")),
			@AttributeOverride(name="processTypeId", column=@Column(name="PROCESS_TYPE_ID"))
	})
	public PublishingProcessControlKey getPrimaryKey() {
		return primaryKey;
	}
	
	@Column(name="ACTIVE")
	public String getActiveString() {
		return (active) ? "Y" : "N";
	}
	
	@Transient
	public boolean isActive() {
		return active;
	}
	protected void setPrimaryKey(PublishingProcessControlKey pk) {
		this.primaryKey = pk;
	}
	protected void setActiveString(String active) {
		setActive("Y".equals(active));
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}

