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
public class WebsiteError
{
	private String reason;
	private String westgetStage;
	private String status;
	private String curlCode;
	private String httpErrorCode;
	private String courtErrorMessage;
	private String docketNumber;
	private String fileName;
	private String caseType;
	private String filingYear;
	private String subfolder;
	private String altDocketId;
	private String subDivision;
	

	
	public String getWestgetStage() {
		return westgetStage;
	}
	public void setWestgetStage(String westgetStage) {
		this.westgetStage = westgetStage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public String getSubfolder()
	{
		return subfolder;
	}
	public void setSubfolder(String subfolder)
	{
		this.subfolder = subfolder;
	}
	public String getSubDivision()
	{
		return subDivision;
	}
	public void setSubDivision(String subDivision)
	{
		this.subDivision = subDivision;
	}
	/**
	 * For NY, it hydrates the values from fileName.  
	 */
	public void hydrateNYAcquisitionDocketFromFileName(){
		String[] fileNameTokens = StringUtils.splitByCharacterType(fileName);
		setSubDivision(fileNameTokens[0]);
		setFilingYear(fileNameTokens[1].substring(0, 4));
	}
	

	
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
	public String getReason() {
		return reason;
	}
	public String getCurlCode() {
		return curlCode;
	}
	public String getHttpErrorCode() {
		return httpErrorCode;
	}
	public String getCourtErrorMessage() {
		return courtErrorMessage;
	}
	public String getAltDocketId() {
		return altDocketId;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setCurlCode(String curlCode) {
		this.curlCode = curlCode;
	}
	public void setHttpErrorCode(String httpErrorCode) {
		this.httpErrorCode = httpErrorCode;
	}
	public void setCourtErrorMessage(String courtErrorMessage) {
		this.courtErrorMessage = courtErrorMessage;
	}
	public void setAltDocketId(String altDocketId) {
		this.altDocketId = altDocketId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altDocketId == null) ? 0 : altDocketId.hashCode());
		result = prime * result + ((caseType == null) ? 0 : caseType.hashCode());
		result = prime * result + ((courtErrorMessage == null) ? 0 : courtErrorMessage.hashCode());
		result = prime * result + ((curlCode == null) ? 0 : curlCode.hashCode());
		result = prime * result + ((docketNumber == null) ? 0 : docketNumber.hashCode());
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + ((filingYear == null) ? 0 : filingYear.hashCode());
		result = prime * result + ((httpErrorCode == null) ? 0 : httpErrorCode.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((subDivision == null) ? 0 : subDivision.hashCode());
		result = prime * result + ((subfolder == null) ? 0 : subfolder.hashCode());
		result = prime * result + ((westgetStage == null) ? 0 : westgetStage.hashCode());
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
		WebsiteError other = (WebsiteError) obj;
		if (altDocketId == null) {
			if (other.altDocketId != null)
				return false;
		} else if (!altDocketId.equals(other.altDocketId))
			return false;
		if (caseType == null) {
			if (other.caseType != null)
				return false;
		} else if (!caseType.equals(other.caseType))
			return false;
		if (courtErrorMessage == null) {
			if (other.courtErrorMessage != null)
				return false;
		} else if (!courtErrorMessage.equals(other.courtErrorMessage))
			return false;
		if (curlCode == null) {
			if (other.curlCode != null)
				return false;
		} else if (!curlCode.equals(other.curlCode))
			return false;
		if (docketNumber == null) {
			if (other.docketNumber != null)
				return false;
		} else if (!docketNumber.equals(other.docketNumber))
			return false;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (filingYear == null) {
			if (other.filingYear != null)
				return false;
		} else if (!filingYear.equals(other.filingYear))
			return false;
		if (httpErrorCode == null) {
			if (other.httpErrorCode != null)
				return false;
		} else if (!httpErrorCode.equals(other.httpErrorCode))
			return false;
		if (reason == null) {
			if (other.reason != null)
				return false;
		} else if (!reason.equals(other.reason))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (subDivision == null) {
			if (other.subDivision != null)
				return false;
		} else if (!subDivision.equals(other.subDivision))
			return false;
		if (subfolder == null) {
			if (other.subfolder != null)
				return false;
		} else if (!subfolder.equals(other.subfolder))
			return false;
		if (westgetStage == null) {
			if (other.westgetStage != null)
				return false;
		} else if (!westgetStage.equals(other.westgetStage))
			return false;
		return true;
	}
	
	
}
