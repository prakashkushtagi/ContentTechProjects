package com.trgr.dockets.core.acquisition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class AcquisitionLogCourtRecord
{	

    private String westlawClusterName = null;
    private String acquisitionStatus = null;
	private String courtType = null;
	
	private List<FailedAcquisitionDocket> failedDocketsList = new ArrayList<FailedAcquisitionDocket>();
	    
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
	
	public List<FailedAcquisitionDocket> getFailedDocketsList() {
		return failedDocketsList;
	}

	public void setFailedDocketsList(List<FailedAcquisitionDocket> skippedDocketsList) {
		this.failedDocketsList = skippedDocketsList;
	}

    public String toString()
    {
      return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}

