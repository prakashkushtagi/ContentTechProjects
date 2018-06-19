/**
 * 
 */
package com.trgr.dockets.core.acquisition;


/**
 * Domain object used for the acquisition response.
 * 
 * @author 
 *
 */
public class WorkflowInformation
{
	private String workFolder;
	private String errorFolder;
	private String processFolder;
	private String novusFolder;
	private String appServerName;
	
	public WorkflowInformation()
	{
		
	}
	public WorkflowInformation(String workFolder, String errorFolder,String processFolder, String novusFolder, String appServerName) 
	{
		super();
		this.workFolder = workFolder;
		this.errorFolder = errorFolder;
		this.processFolder = processFolder;
		this.novusFolder = novusFolder;
		this.appServerName = appServerName;
	}
	
	
	/**
	 * @return the workFolder
	 */
	public String getWorkFolder() 
	{
		return workFolder;
	}
	/**
	 * @param workFolder the workFolder to set
	 */
	public void setWorkFolder(String workFolder)
	{
		this.workFolder = workFolder;
	}
	/**
	 * @return the errorFolder
	 */
	public String getErrorFolder() 
	{
		return errorFolder;
	}
	/**
	 * @param errorFolder the errorFolder to set
	 */
	public void setErrorFolder(String errorFolder) 
	{
		this.errorFolder = errorFolder;
	}
	/**
	 * @return the processFolder
	 */
	public String getProcessFolder() 
	{
		return processFolder;
	}
	/**
	 * @param processFolder the processFolder to set
	 */
	public void setProcessFolder(String processFolder)
	{
		this.processFolder = processFolder;
	}
	/**
	 * @return the novusFolder
	 */
	public String getNovusFolder() 
	{
		return novusFolder;
	}
	/**
	 * @param novusFolder the novusFolder to set
	 */
	public void setNovusFolder(String novusFolder) 
	{
		this.novusFolder = novusFolder;
	}
	/**
	 * @return the appServerName
	 */
	public String getAppServerName() 
	{
		return appServerName;
	}
	/**
	 * @param appServerName the appServerName to set
	 */
	public void setAppServerName(String appServerName) 
	{
		this.appServerName = appServerName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appServerName == null) ? 0 : appServerName.hashCode());
		result = prime * result
				+ ((errorFolder == null) ? 0 : errorFolder.hashCode());
		result = prime * result
				+ ((novusFolder == null) ? 0 : novusFolder.hashCode());
		result = prime * result
				+ ((processFolder == null) ? 0 : processFolder.hashCode());
		result = prime * result
				+ ((workFolder == null) ? 0 : workFolder.hashCode());
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
		WorkflowInformation other = (WorkflowInformation) obj;
		if (appServerName == null) {
			if (other.appServerName != null)
				return false;
		} else if (!appServerName.equals(other.appServerName))
			return false;
		if (errorFolder == null) {
			if (other.errorFolder != null)
				return false;
		} else if (!errorFolder.equals(other.errorFolder))
			return false;
		if (novusFolder == null) {
			if (other.novusFolder != null)
				return false;
		} else if (!novusFolder.equals(other.novusFolder))
			return false;
		if (processFolder == null) {
			if (other.processFolder != null)
				return false;
		} else if (!processFolder.equals(other.processFolder))
			return false;
		if (workFolder == null) {
			if (other.workFolder != null)
				return false;
		} else if (!workFolder.equals(other.workFolder))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorkflowInformation [workFolder=" + workFolder
				+ ", errorFolder=" + errorFolder + ", processFolder="
				+ processFolder + ", novusFolder=" + novusFolder
				+ ", appServerName=" + appServerName + "]";
	}
}
