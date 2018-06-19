package com.trgr.dockets.core.domain.ltc;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author u0160964 Sagun Khanal (Sagun.Khanal@thomsonreuters.com) 
 *
 */

@XmlRootElement(name="load.dataset")
public class LoadDataset {

	private String loadDatasetId;
	private String loadRequestId;
	private String requestCollaborationKey;
	private String fileName;
	private LtcCollaborationKey ltcCollaborationKey;
	
	public LoadDataset() {
		super();
	}

	public LoadDataset(String loadDatasetId, String loadRequestId,
			LtcCollaborationKey requestCollaborationKey, String requestId, String uberBatchId, String batchId, String subBatchId, String fileName) 
	{
		this.loadDatasetId = loadDatasetId;
		this.loadRequestId = loadRequestId;
		this.requestCollaborationKey = requestCollaborationKey.getLtcCollaborationKey();
		this.ltcCollaborationKey = requestCollaborationKey;
		this.fileName = fileName;

	}	

	
	public String getFileName() {
		return fileName;
	}
	
	@XmlElement(name="load.dataset")
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRequestCollaborationKey() {
		return requestCollaborationKey;
	}

	@XmlAttribute(name="request.collaboration.key")
	public void setRequestCollaborationKey(String requestCollaborationKey) {
		this.requestCollaborationKey = requestCollaborationKey;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime
				* result
				+ ((requestCollaborationKey == null) ? 0
						: requestCollaborationKey.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoadDataset other = (LoadDataset) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (requestCollaborationKey == null) {
			if (other.requestCollaborationKey != null)
				return false;
		} else if (!requestCollaborationKey
				.equals(other.requestCollaborationKey))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoadDataset [requestCollaborationKey="
				+ requestCollaborationKey + ", fileName=" + fileName + "]";
	}

	/**
	 * @return the loadDatasetId
	 */
	public String getLoadDatasetId() {
		return loadDatasetId;
	}

	/**
	 * @param loadDatasetId the loadDatasetId to set
	 */
	public void setLoadDatasetId(String loadDatasetId) {
		this.loadDatasetId = loadDatasetId;
	}

	/**
	 * @return the loadRequestId
	 */
	public String getLoadRequestId() {
		return loadRequestId;
	}

	/**
	 * @param loadRequestId the loadRequestId to set
	 */
	public void setLoadRequestId(String loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public LtcCollaborationKey getLtcCollaborationKey() {
		return ltcCollaborationKey;
	}

	public void setLtcCollaborationKey(LtcCollaborationKey ltcCollaborationKey) {
		this.ltcCollaborationKey = ltcCollaborationKey;
	}

}
