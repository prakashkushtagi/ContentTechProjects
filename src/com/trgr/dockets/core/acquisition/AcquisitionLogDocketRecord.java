package com.trgr.dockets.core.acquisition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AcquisitionLogDocketRecord
{	

    private String acquisitionSourceName = null;
    private String acquisitionReceiptId = null;
    private String provider = null;
    private Date scriptStartDate = null;
    private Date scriptEndDate = null;
    private long totalRecsRetrieved = 0;
    private long mergedFileSize = 0;
    private String mergedFileName = null;
    private String retrievalType = null;
    private List<AcquisitionLogCourtRecord> acquisitionLogCourtRecordList = new ArrayList<AcquisitionLogCourtRecord>();
	
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
    
	public List<AcquisitionLogCourtRecord> getAcquisitionLogCourtRecordList() {
		return acquisitionLogCourtRecordList;
	}

	public void setAcquisitionLogCourtRecordList(List<AcquisitionLogCourtRecord> acquisitionLogCourtRecordList) {
		this.acquisitionLogCourtRecordList = acquisitionLogCourtRecordList;
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

    public String toString()
    {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public void setScriptStartDate(Date scriptStartDate) {
		this.scriptStartDate = scriptStartDate;
	}

	public void setScriptEndDate(Date scriptEndDate) {
		this.scriptEndDate = scriptEndDate;
	}


}

