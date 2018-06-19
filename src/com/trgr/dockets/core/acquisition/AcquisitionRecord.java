/**
 * Copyright 2016: Thomson Reuters. All Rights Reserved. Proprietary and Confidential information of Thomson Reuters. Disclosure, Use or Reproduction without the written authorization of Thomson Reuters is prohibited.
 */
package com.trgr.dockets.core.acquisition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author C047166
 *
 */
public class AcquisitionRecord
{	

    private String acquisitionSourceName = null;
    private String acquisitionReceiptId = null;
    private String provider = null;
    private String westlawClusterName = null;
    private Date scriptStartDate = null;
    private Date scriptEndDate = null;
    private long totalRecsRetrieved = 0;
    private long mergedFileSize = 0;
    private String mergedFileName = null;
    private String retrievalType = null;
    private String acquisitionStatus = null;
	private String courtType = null;
	
	private List<AcquisitionDocket> acquiredDocketsList = new ArrayList<AcquisitionDocket>();
	private List<AcquisitionDocket> deletedDocketsList = new ArrayList<AcquisitionDocket>();
	    
    public String getAcquisitionSourceName()
    {
        return acquisitionSourceName;
    }

    public void setAcquisitionSourceName(String acquisitionSourceName)
    {
        this.acquisitionSourceName = acquisitionSourceName;
    }

    public String getAcquisitionReceiptId()
    {
        return acquisitionReceiptId;
    }

    public void setAcquisitionReceiptId(String acquisitionReceiptId)
    {
        this.acquisitionReceiptId = acquisitionReceiptId;
    }

    public String getProvider()
    {
        return provider;
    }

    public void setProvider(String provider)
    {
        this.provider = provider;
    }
    
    public String getWestlawClusterName()
    {
        if (westlawClusterName != null)
        {
            return westlawClusterName.toUpperCase();
        }
        else
        {
            return null;
        }
    }

    public void setWestlawClusterName(String westlawClusterName)
    {
        this.westlawClusterName = westlawClusterName;
    }

    public Date getScriptStartDate()
    {
        return scriptStartDate;
    }

    public void setScriptStartDate(String dateString)
    {
        this.scriptStartDate = getFormattedDate(dateString);
    }

    public Date getScriptEndDate()
    {
        return scriptEndDate;
    }

    public void setScriptEndDate(String dateString)
    {
        this.scriptEndDate = getFormattedDate(dateString);
    }

    public long getTotalRecsRetrieved()
    {
        return totalRecsRetrieved;
    }

    public void setTotalRecsRetrieved(long totalRecsRetrieved)
    {
        this.totalRecsRetrieved = totalRecsRetrieved;
    }

    public long getMergedFileSize()
    {
        return mergedFileSize;
    }

    public void setMergedFileSize(long mergedFileSize)
    {
        this.mergedFileSize = mergedFileSize;
    }

    public String getMergedFileName()
    {
        return mergedFileName;
    }

    public void setMergedFileName(String mergedFileName)
    {
        this.mergedFileName = mergedFileName;
    }

    public String getRetrievalType()
    {
        return retrievalType;
    }

    public void setRetrievalType(String retrievalType)
    {
        this.retrievalType = retrievalType;
    }
    
	public static Date getFormattedDate(String dateString)
    {
        //Tue May 25 16:22:39 CDT 2004
        SimpleDateFormat simple = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try
        {
            return simple.parse(dateString);
        }
        catch (Exception e)
        {
        }
        return null;
    }

    public void incrementTotalRecordsRetrieved()
    {
        totalRecsRetrieved++;
    }


    public String getAcquisitionStatus()
	{
		return acquisitionStatus;
	}

	public void setAcquisitionStatus(String acquisitionStatus)
	{
		this.acquisitionStatus = acquisitionStatus;
	}

	public String getCourtType()
	{
		return courtType;
	}

	public void setCourtType(String courtType)
	{
		this.courtType = courtType;
	}
	
	public List<AcquisitionDocket> getAcquiredDocketsList()
	{
		return acquiredDocketsList;
	}

	public List<AcquisitionDocket> getDeletedDocketsList()
	{
		return deletedDocketsList;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

