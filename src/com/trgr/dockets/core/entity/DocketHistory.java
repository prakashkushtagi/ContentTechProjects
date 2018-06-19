package com.trgr.dockets.core.entity;

import java.io.File;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="DOCKET_HISTORY", schema="DOCKETS_PUB")
public class DocketHistory 
{
	public enum DocketHistoryTypeEnum { ACQUISITION_HISTORY, CONVERSION_HISTORY, PUBLISH_HISTORY, LOAD_HISTORY , PREPROCESSOR_HISTORY };  // source, jaxml, novus xml types
	
	public static final String PROPERTY_LEGACY_ID = "legacyId";
	public static final String PROPERTY_TYPE = "typeString";
	public static final String PROPERTY_REQUEST_NAME = "requestName";
	
	private String docketHistoryId;
	private String legacyId;
	private DocketHistoryTypeEnum type;
	private Date timestamp;
	private String requestName;
	private String droppedDocketReason;
	private File file;
	private Date requestStartTimestamp;
	
	public DocketHistory() 
	{
		super();
	}
	
	/** Full constructor. */
	public DocketHistory(String pk, String legacyId,
			DocketHistoryTypeEnum type, Date timestamp, String requestName,
			String droppedDocketReason, File file) {
		this.docketHistoryId = pk;
		this.legacyId = legacyId;
		this.type = type;
		this.timestamp = timestamp;
		this.requestName = requestName;
		this.droppedDocketReason = droppedDocketReason;
		this.file = file;
	}

	public DocketHistory(String pk, String legacyId,
			DocketHistoryTypeEnum type, Date timestamp, String requestName,
			String droppedDocketReason, File file, Date reqStartTimestamp) {
		this(pk, legacyId, type, timestamp, requestName, droppedDocketReason, file);
		this.requestStartTimestamp = reqStartTimestamp;
	}
	
	@Id
	@Column(name="DOCKET_HISTORY_ID", length=40, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getDocketHistoryId() 
	{
		return this.docketHistoryId;
	}
	@Column(name="LEGACY_ID", length=30, nullable = false)
	@Basic(fetch = FetchType.EAGER)
	public String getLegacyId() 
	{
		return legacyId;
	}
	@Column(name="TYPE", length=30, nullable = false)
	public String getTypeString() {
		return (type != null) ? type.name() : null;
	}
	@Column(name="TIMESTAMP", nullable=false)
	public Date getTimestamp() 
	{
		return timestamp;
	}
	@Column(name="REQUEST_NAME", length=256, nullable = true)
	public String getRequestName() 
	{
		return this.requestName; 
	}
	@Column(name="DROPPED_DOCKET_REASON", length=500, nullable = true)
	public String getDroppedDocketReason() 
	{
		return this.droppedDocketReason; 
	}
	@Column(name="FILENAME", length=100, nullable = true)
	protected String getFilename() {
		return (file != null) ? file.getName() : null;
	}
	@Column(name="REQUEST_START_TIMESTAMP")
	public Date getRequestStartTimestamp()
	{
		return requestStartTimestamp;
	}

	@Transient
	public File getFile() {
		return file;
	}
	@Transient
	public DocketHistoryTypeEnum getType() {
		return type;
	}
	@Override
	public String toString() 
	{
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * @param docketHistoryId the docketHistoryId to set
	 */
	public void setDocketHistoryId(String docketHistoryId) 
	{
		this.docketHistoryId = docketHistoryId;
	}

	/**
	 * @param legacyId the legacyId to set
	 */
	public void setLegacyId(String legacyId)
	{
		this.legacyId = legacyId;
	}

	public void setType(DocketHistoryTypeEnum type) {
		this.type = type;
	}
	/**
	 * @param type the type to set
	 */
	protected void setTypeString(String type) 
	{
		setType(DocketHistoryTypeEnum.valueOf(type));
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) 
	{
		this.timestamp = timestamp;
	}

	/**
	 * @param requestName the requestName to set
	 */
	public void setRequestName(String requestName) 
	{
		this.requestName = requestName;
	}

	/**
	 * @param droppedDocketReason the droppedDocketReason to set
	 */
	public void setDroppedDocketReason(String droppedDocketReason) 
	{
		this.droppedDocketReason = StringUtils.substring(droppedDocketReason, 0, 500);
	}

	protected void setFilename(String filename)
	{
		setFile((filename != null) ? new File (filename) : null);
	}
	public void setFile(File file) {
		this.file = file;
	}

	public void setRequestStartTimestamp(Date requestStartTimestamp)
	{
		this.requestStartTimestamp = requestStartTimestamp;
	}
	
	

}
