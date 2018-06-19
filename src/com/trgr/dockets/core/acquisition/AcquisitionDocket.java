/**
 * 
 */
package com.trgr.dockets.core.acquisition;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Domain object to store the details about the docket.
 * 
 * @author C047166
 *
 */
public class AcquisitionDocket
{
	private String docketNumber;
	private String fileName;
	private String caseType;
	private String filingYear;
	private String sequenceNumber;
	private String subfolder;
	private String filingLocation;
	private String subDivision;
	private String pdfFileName;
	private boolean deleteOperation;
	
	public String getDocketNumber()
	{
		return docketNumber;
	}
	public void setDocketNumber(String docketNumber)
	{
		this.docketNumber = docketNumber;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	public String getCaseType()
	{
		return caseType;
	}
	public void setCaseType(String caseType)
	{
		this.caseType = caseType;
	}
	public String getFilingYear()
	{
		return filingYear;
	}
	public void setFilingYear(String filingYear)
	{
		this.filingYear = filingYear;
	}
	public String getSequenceNumber()
	{
		return sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}
	public String getSubfolder()
	{
		return subfolder;
	}
	public void setSubfolder(String subfolder)
	{
		this.subfolder = subfolder;
	}
	public String getFilingLocation()
	{
		return filingLocation;
	}
	public void setFilingLocation(String filingLocation)
	{
		this.filingLocation = filingLocation;
	}
	public String getSubDivision()
	{
		return subDivision;
	}
	public void setSubDivision(String subDivision)
	{
		this.subDivision = subDivision;
	}
	public String getPdfFileName()
	{
		return pdfFileName;
	}
	public void setPdfFileName(String pdfFileName)
	{
		this.pdfFileName = pdfFileName;
	}
	/**
	 * For NY, it hydrates the values from fileName.  
	 */
	public void hydrateNYAcquisitionDocketFromFileName(){
		//formats
		//	NEWYORKN_DNYMANHATTAN20170306034647
		//	NEWYORK20170306034647
		if(fileName.contains("_")){
			String[] fileNameTokens = StringUtils.splitByCharacterType(fileName);
			setSubDivision(fileNameTokens[0].substring(0, fileNameTokens[0].length()-1));
			setSequenceNumber(fileNameTokens[3].substring(4));
			setFilingYear(fileNameTokens[3].substring(0, 4));
		}
		else{
			String[] fileNameTokens = StringUtils.splitByCharacterType(fileName);
			setSubDivision(fileNameTokens[0]);
			setSequenceNumber(fileNameTokens[1].substring(4));
			setFilingYear(fileNameTokens[1].substring(0, 4));
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caseType == null) ? 0 : caseType.hashCode());
		result = prime * result + ((docketNumber == null) ? 0 : docketNumber.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((filingLocation == null) ? 0 : filingLocation.hashCode());
		result = prime * result + ((filingYear == null) ? 0 : filingYear.hashCode());
		result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result + ((subDivision == null) ? 0 : subDivision.hashCode());
		result = prime * result + ((subfolder == null) ? 0 : subfolder.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AcquisitionDocket other = (AcquisitionDocket) obj;
		if (caseType == null)
		{
			if (other.caseType != null) return false;
		}
		else if (!caseType.equals(other.caseType)) return false;
		if (docketNumber == null)
		{
			if (other.docketNumber != null) return false;
		}
		else if (!docketNumber.equals(other.docketNumber)) return false;
		if (fileName == null)
		{
			if (other.fileName != null) return false;
		}
		else if (!fileName.equals(other.fileName)) return false;
		if (filingLocation == null)
		{
			if (other.filingLocation != null) return false;
		}
		else if (!filingLocation.equals(other.filingLocation)) return false;
		if (filingYear == null)
		{
			if (other.filingYear != null) return false;
		}
		else if (!filingYear.equals(other.filingYear)) return false;
		if (sequenceNumber == null)
		{
			if (other.sequenceNumber != null) return false;
		}
		else if (!sequenceNumber.equals(other.sequenceNumber)) return false;
		if (subDivision == null)
		{
			if (other.subDivision != null) return false;
		}
		else if (!subDivision.equals(other.subDivision)) return false;
		if (subfolder == null)
		{
			if (other.subfolder != null) return false;
		}
		else if (!subfolder.equals(other.subfolder)) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	/**
	 * @return the deleteOperation
	 */
	public boolean isDeleteOperation() {
		return deleteOperation;
	}
	/**
	 * @param deleteOperation the deleteOperation to set
	 */
	public void setDeleteOperation(boolean deleteOperation) {
		this.deleteOperation = deleteOperation;
	}
	
	
}
