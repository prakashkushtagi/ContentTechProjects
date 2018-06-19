package com.trgr.dockets.core.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;

@Entity
@Table(name="NOVUS_LOAD_FILE", schema="DOCKETS_PUB")
public class NovusLoadFile {
	
//	request_id,filename
	private NovusLoadFileKey primaryKey;  	
	
	private String loadRequestId;
	private String filename;

	//	FILE_PATH
	private String filepath;
//	BATCH_ID
	private String batchId;
//	SUB_BATCH_ID
	private String subBatchId;
	private StatusEnum status;
	private Long collectionId;
	
	public static final String FILE_NAME = "filename";
	public static final String LOAD_REQUEST_ID = "loadRequestId";
	public static final String BATCH_ID = "batchId";
	public static final String SUB_BATCH_ID = "subBatchId";
	public static final String COLLECTION_ID = "collectionId";
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="batchId", column=@Column(name="BATCH_ID")),
			@AttributeOverride(name="subBatchId", column=@Column(name="SUB_BATCH_ID")),
			@AttributeOverride(name="filename", column=@Column(name="FILE_NAME"))
	})
	public NovusLoadFileKey getPrimaryKey()
	{
		return primaryKey;
	}
	
	@Column(name = "LOAD_REQUEST_ID")
	public String getLoadRequestId() {
		return loadRequestId;
	}
	@Column(name = "FILE_NAME", insertable = false, updatable = false)
	public String getFilename() {
		return filename;
	}
	@Column(name="FILE_PATH", nullable=false)
	public String getFilepath() 
	{
		return filepath;
	}
	@Column(name="BATCH_ID", insertable = false, updatable = false)
	public String getBatchId()
	{
		return batchId;
	}
	@Column(name="SUB_BATCH_ID", insertable = false, updatable = false)
	public String getSubBatchId()
	{
		return subBatchId;
	}
	@Column(name="STATUS_ID", nullable=false)
	public Long getStatusKey() 
	{
		return (status != null) ? status.getKey() : null;
	}
	@Column(name="COLLECTION_ID")
	public Long getCollectionId() 
	{
		return collectionId;
	}
	@Transient
	public StatusEnum getStatus() 
	{
		return status;
	}
	public void setStatus(StatusEnum status) 
	{
		this.status = status;
	}
	@Override
	public String toString() 
	{
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public void setPrimaryKey(NovusLoadFileKey primaryKey) 
	{
		this.primaryKey = primaryKey;
	}

	public void setLoadRequestId(String loadRequestId)
	{
		this.loadRequestId = loadRequestId;
	}

	public void setFilename(String filename) 
	{
		this.filename = filename;
	}

	public void setFilepath(String filepath) 
	{
		this.filepath = filepath;
	}

	public void setBatchId(String batchId)
	{
		this.batchId = batchId;
	}

	public void setSubBatchId(String subBatchId) 
	{
		this.subBatchId = subBatchId;
	}
	public void setStatusKey(Long statusId) 
	{
		this.status = StatusEnum.findByKey(statusId);
	}
	/**
	 * @param collectionId the collectionId to set
	 */
	public void setCollectionId(Long collectionId) 
	{
		this.collectionId = collectionId;
	}
}
