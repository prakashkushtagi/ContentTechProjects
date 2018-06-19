package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="DOCKET_VERSION_REL", schema="DOCKETS_PUB")
public class DocketVersionRel
{
	
	public enum DocketVersionRelTypeEnum { SOURCE_TO_JAXML, JAXML_TO_NOVUS, NOVUS_TO_LOAD, SOURCE_TO_NOVUS };
	
//	REL_ID	VARCHAR2(40 BYTE)	No
//	SOURCE_GUID	VARCHAR2(40 BYTE)	No
//	SOURCE_VERSION	NUMBER	No
//	TARGET_GUID	VARCHAR2(40 BYTE)	No
//	TARGET_VERSION	NUMBER	No
//	TYPE	VARCHAR2(30 BYTE)	No
//	REL_TIMESTAMP	TIMESTAMP(6)	No
//	LEGACY_ID	VARCHAR2(30 BYTE)	No

	public static final String REL_ID = "relId";
	public static final String LEGACY_ID = "legacyId";
	public static final String REL_TIMESTAMP = "relTimestamp";
	public static final String REL_TYPE = "typeString";
	private String relId;
	private String sourceGuid;
	private Long sourceVersion;
	private String targetGuid;
	private Long targetVersion;
	private DocketVersionRelTypeEnum type;
	private Date relTimestamp;
	private String legacyId;
	
	public DocketVersionRel() {
		super();
	}
	
	public DocketVersionRel(String relId, String sourceGuid,
			Long sourceVersion, String targetGuid, Long targetVersion,
			DocketVersionRelTypeEnum type, Date relTimestamp, String legacyId) {
		this.relId = relId;
		this.sourceGuid = sourceGuid;
		this.sourceVersion = sourceVersion;
		this.targetGuid = targetGuid;
		this.targetVersion = targetVersion;
		this.type = type;
		this.relTimestamp = relTimestamp;
		this.legacyId = legacyId;
	}


	@Id
	@Column(name="REL_ID", length=40, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getRelId() 
	{
		return this.relId;
	}
	@Column(name="SOURCE_GUID", length=40, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getSourceGuid() 
	{
		return sourceGuid;
	}
	@Column(name="SOURCE_VERSION", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public Long getSourceVersion() 
	{
		return sourceVersion;
	}
	@Column(name="TARGET_GUID", length=40, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getTargetGuid() 
	{
		return targetGuid;
	}
	@Column(name="TARGET_VERSION", nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public Long getTargetVersion() 
	{
		return targetVersion;
	}
	@Column(name="TYPE", length=30, nullable = true)
	protected String getTypeString() 
	{
		return (type != null) ? type.name() : null;
	}
	@Column(name="REL_TIMESTAMP", nullable=false)
	public Date getRelTimestamp() 
	{
		return relTimestamp;
	}
	@Column(name="LEGACY_ID", length=30, nullable = true)
	public String getLegacyId() 
	{
		return this.legacyId; 
	}
	@Transient
	public DocketVersionRelTypeEnum getType() {
		return type;
	}
	@Override
	public String toString() 
	{
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * @param relId the relId to set
	 */
	public void setRelId(String relId)
	{
		this.relId = relId;
	}

	/**
	 * @param sourceGuid the sourceGuid to set
	 */
	public void setSourceGuid(String sourceGuid) 
	{
		this.sourceGuid = sourceGuid;
	}

	/**
	 * @param sourceVersion the sourceVersion to set
	 */
	public void setSourceVersion(Long sourceVersion) 
	{
		this.sourceVersion = sourceVersion;
	}

	/**
	 * @param targetGuid the targetGuid to set
	 */
	public void setTargetGuid(String targetGuid) 
	{
		this.targetGuid = targetGuid;
	}

	/**
	 * @param targetVersion the targetVersion to set
	 */
	public void setTargetVersion(Long targetVersion) 
	{
		this.targetVersion = targetVersion;
	}

	public void setType(DocketVersionRelTypeEnum type) {
		this.type = type;
	}
	protected void setTypeString(String type) {
		setType(DocketVersionRelTypeEnum.valueOf(type));
	}

	/**
	 * @param relTimestamp the relTimestamp to set
	 */
	public void setRelTimestamp(Date relTimestamp)
	{
		this.relTimestamp = relTimestamp;
	}

	/**
	 * @param legacyId the legacyId to set
	 */
	public void setLegacyId(String legacyId) 
	{
		this.legacyId = legacyId;
	}
	
}
