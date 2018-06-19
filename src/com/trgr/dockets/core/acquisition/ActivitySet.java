/**
 * 
 */
package com.trgr.dockets.core.acquisition;

import java.util.Date;

import com.trgr.dockets.core.util.DateUtils;

/**
 * @author C047166
 *
 */
public class ActivitySet
{
	public static final String DATE_FORMAT_SLASHES = "MM/dd/yyyy HH:mm:ss";
	public static final String DATE_FORMAT_NO_SLASHES = "yyyyMMddHHmmss";
	
	private String receiptId;
	private Date startTime; //start time for the whole activity
	private Date endTime; //end time for the whole activity
	private Date conversionStartTime;
	private Date saberStartTime;
	private Date novusLoadStartTime;
	private String sourceFile;
	private String status;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReceiptId()
	{
		return receiptId;
	}
	public void setReceiptId(String receiptId)
	{
		this.receiptId = receiptId;
	}
	public Date getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTimeStr)
	{
		this.startTime = DateUtils.getFormattedDate(startTimeStr, DATE_FORMAT_SLASHES);
	}
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTimeStr)
	{
		this.endTime = DateUtils.getFormattedDate(endTimeStr, DATE_FORMAT_SLASHES);
	}
	public Date getConversionStartTime()
	{
		return conversionStartTime;
	}
	public void setConversionStartTime(String conversionStartTime)
	{
		this.conversionStartTime = DateUtils.getFormattedDate(conversionStartTime, DATE_FORMAT_NO_SLASHES);
	}
	public Date getSaberStartTime()
	{
		return saberStartTime;
	}
	public void setSaberStartTime(String saberStartTime)
	{
		this.saberStartTime =  DateUtils.getFormattedDate(saberStartTime, DATE_FORMAT_NO_SLASHES);
	}
	public Date getNovusLoadStartTime()
	{
		return novusLoadStartTime;
	}
	public void setNovusLoadStartTime(String novusLoadStartTime)
	{
		this.novusLoadStartTime = DateUtils.getFormattedDate(novusLoadStartTime, DATE_FORMAT_NO_SLASHES);
	}
	public String getSourceFile()
	{
		return sourceFile;
	}
	public void setSourceFile(String sourceFile)
	{
		this.sourceFile = sourceFile;
	}
	
}
