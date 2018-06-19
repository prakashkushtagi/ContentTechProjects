package com.trgr.dockets.core.entity;

import java.io.File;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.trgr.dockets.core.entity.CodeTableValues.StatusEnum;

@Entity
@Table(name="BATCH_MONITOR",schema="DOCKETS_PUB")
@org.hibernate.annotations.Entity(
		dynamicUpdate = true
)
public class BatchMonitor {
	
	private BatchMonitorKey primaryKey;
	private String serverHostname;
	private File workDirectory;
	private File errorDirectory;
	private File processDirectory;
	private File novusLoadDirectory;
	private Long docsInCount;
	private Long docsInSize;
	private Long docsOutCount;
	private Long docsOutSize;
	private Status status;
	public static final String BATCH_ID = "batchId";
	public static final String REQUEST_ID = "publishingRequestId";
	
   
    private String batchId;
    
    
    private String publishingRequestId;
    
	public BatchMonitor() {
		super();
	}
	public BatchMonitor(BatchMonitorKey primaryKey) {
		setPrimaryKey(primaryKey);
	}
	
	public BatchMonitor(BatchMonitorKey primaryKey, String serverHostname,
			File workDirectory, File errorDirectory, File processDirectory,
			File novusLoadDirectory, Long docsInCount, Long docsInSize,
			Long docsOutCount, Long docsOutSize, StatusEnum status) {
		setPrimaryKey(primaryKey);
		this.serverHostname = serverHostname;
		this.workDirectory = workDirectory;
		this.errorDirectory = errorDirectory;
		this.processDirectory = processDirectory;
		this.novusLoadDirectory = novusLoadDirectory;
		this.docsInCount = docsInCount;
		this.docsInSize = docsInSize;
		this.docsOutCount = docsOutCount;
		this.docsOutSize = docsOutSize;
		setStatus(status);
	}

	@Column(name="NUM_DOCS_IN")
	public Long getDocsInCount() {
		return docsInCount;
	}
	@Column(name="SIZE_DOCS_IN")
	public Long getDocsInSize() {
		return docsInSize;
	}
	@Column(name="NUM_DOCS_OUT")
	public Long getDocsOutCount() {
		return docsOutCount;
	}
	@Column(name="SIZE_DOCS_OUT")
	public Long getDocsOutSize() {
		return docsOutSize;
	}
	@Transient
	public File getErrorDirectory() {
		return errorDirectory;
	}
	@Column(name="ERROR_FOLDER")
	private String getErrorDirectoryString() {
		return (errorDirectory != null) ? errorDirectory.getPath() : null;
	}
	@Transient
	public File getNovusLoadDirectory() {
		return novusLoadDirectory;
	}
	@Column(name="NOVUS_LOAD_FOLDER")
	private String getNovusLoadDirectoryString() {
		return (novusLoadDirectory != null) ? novusLoadDirectory.getPath() : null;
	}
	@Id
	@AttributeOverrides({
			@AttributeOverride(name="publishingRequestId", column=@Column(name="REQUEST_ID")),
			@AttributeOverride(name="batchId", column=@Column(name="BATCH_ID"))
	})
	public BatchMonitorKey getPrimaryKey() {
		return primaryKey;
	}
	@Transient
	public File getProcessDirectory() {
		return processDirectory;
	}
	@Column(name="PROCESS_FOLDER")
	private String getProcessDirectoryString() {
		return (processDirectory != null) ? processDirectory.getPath() : null;
	}
	@Column(name="APP_SERVER")
	public String getServerHostname() {
		return serverHostname;
	}
	@OneToOne
	@JoinColumn(name="STATUS_ID")
	public Status getStatus() {
		return status;
	}
	@Transient
	public File getWorkDirectory() {
		return workDirectory;
	}
	@Column(name="WORK_FOLDER")
	private String getWorkDirectoryString() {
		return (workDirectory != null) ? workDirectory.getPath() : null;
	}
	
	public void setDocsInCount(Long docsInCount) {
		this.docsInCount = docsInCount;
	}
	public void setDocsInSize(Long docsInSize) {
		this.docsInSize = docsInSize;
	}
	public void setDocsOutCount(Long docsOutCount) {
		this.docsOutCount = docsOutCount;
	}
	public void setDocsOutSize(Long docsOutSize) {
		this.docsOutSize = docsOutSize;
	}
	public void setErrorDirectory(File directory) {
		this.errorDirectory = directory;
	}
	@SuppressWarnings("unused")
	private void setErrorDirectoryString(String directory) {
		setErrorDirectory(StringUtils.isNotBlank(directory) ?  new File(directory) : null);
	}
	public void setNovusLoadDirectory(File directory) {
		this.novusLoadDirectory = directory;
	}
	@SuppressWarnings("unused")
	private void setNovusLoadDirectoryString(String directory) {
		setNovusLoadDirectory(StringUtils.isNotBlank(directory) ?  new File(directory) : null);
	}
	public void setPrimaryKey(BatchMonitorKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	public void setProcessDirectory(File directory) {
		this.processDirectory = directory;
	}
	@SuppressWarnings("unused")
	private void setProcessDirectoryString(String directory) {
		setProcessDirectory(StringUtils.isNotBlank(directory) ?  new File(directory) : null);
	}
	public void setServerHostname(String serverHostname) {
		this.serverHostname = serverHostname;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void setStatus(StatusEnum status) {
		setStatus(new Status(status));
	}
	public void setWorkDirectory(File directory) {
		this.workDirectory = directory;
	}
	@SuppressWarnings("unused")
	private void setWorkDirectoryString(String directory) {
		setWorkDirectory(StringUtils.isNotBlank(directory) ?  new File(directory) : null);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
	 @Column(name = "BATCH_ID", insertable = false, updatable = false)
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	@Column(name = "REQUEST_ID", insertable = false, updatable = false)
	public String getPublishingRequestId() {
		return publishingRequestId;
	}
	public void setPublishingRequestId(String publishingRequestId) {
		this.publishingRequestId = publishingRequestId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result
				+ ((docsInCount == null) ? 0 : docsInCount.hashCode());
		result = prime * result
				+ ((docsInSize == null) ? 0 : docsInSize.hashCode());
		result = prime * result
				+ ((docsOutCount == null) ? 0 : docsOutCount.hashCode());
		result = prime * result
				+ ((docsOutSize == null) ? 0 : docsOutSize.hashCode());
		result = prime * result
				+ ((errorDirectory == null) ? 0 : errorDirectory.hashCode());
		result = prime
				* result
				+ ((novusLoadDirectory == null) ? 0 : novusLoadDirectory
						.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime
				* result
				+ ((processDirectory == null) ? 0 : processDirectory.hashCode());
		result = prime
				* result
				+ ((publishingRequestId == null) ? 0 : publishingRequestId
						.hashCode());
		result = prime * result
				+ ((serverHostname == null) ? 0 : serverHostname.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((workDirectory == null) ? 0 : workDirectory.hashCode());
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
		BatchMonitor other = (BatchMonitor) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (docsInCount == null) {
			if (other.docsInCount != null)
				return false;
		} else if (!docsInCount.equals(other.docsInCount))
			return false;
		if (docsInSize == null) {
			if (other.docsInSize != null)
				return false;
		} else if (!docsInSize.equals(other.docsInSize))
			return false;
		if (docsOutCount == null) {
			if (other.docsOutCount != null)
				return false;
		} else if (!docsOutCount.equals(other.docsOutCount))
			return false;
		if (docsOutSize == null) {
			if (other.docsOutSize != null)
				return false;
		} else if (!docsOutSize.equals(other.docsOutSize))
			return false;
		if (errorDirectory == null) {
			if (other.errorDirectory != null)
				return false;
		} else if (!errorDirectory.equals(other.errorDirectory))
			return false;
		if (novusLoadDirectory == null) {
			if (other.novusLoadDirectory != null)
				return false;
		} else if (!novusLoadDirectory.equals(other.novusLoadDirectory))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (processDirectory == null) {
			if (other.processDirectory != null)
				return false;
		} else if (!processDirectory.equals(other.processDirectory))
			return false;
		if (publishingRequestId == null) {
			if (other.publishingRequestId != null)
				return false;
		} else if (!publishingRequestId.equals(other.publishingRequestId))
			return false;
		if (serverHostname == null) {
			if (other.serverHostname != null)
				return false;
		} else if (!serverHostname.equals(other.serverHostname))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (workDirectory == null) {
			if (other.workDirectory != null)
				return false;
		} else if (!workDirectory.equals(other.workDirectory))
			return false;
		return true;
	}
}
