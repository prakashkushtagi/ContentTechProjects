/**
 * Copyright 2016: Thomson Reuters Global Resources. All Rights Reserved. Proprietary and 
 * Confidential information of TRGR. Disclosure, Use or Reproduction without the 
 * written authorization of TRGR is prohibited.
 *
 */
package com.trgr.dockets.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 
 * @author c161238
 *
 */
@Entity
@Table(name = "DOCKETS_PUB.COURT_CONFIG")
public class CourtConfig implements Serializable
{   
    /**
	 * 
	 */
	private static final long serialVersionUID = 7011024945200273415L;
	public static String COURT_ID = "courtId";
	public static String ACTIVE = "active";
	public static String FIFO= "fifo";
	public static String PREDOCKET = "predocket";
	public static String PUBLICATION_ID = "publicationId";
	public static String RPX_WORKFLOW_TYPE_ID = "rpxWorkflowTypeId";
	public static String DISPLAY_NAME = "displayName";
	public static String DOCKET_EXAMPLE = "docketExample";
	public static String FILTER_NAME = "filterName";
	/**  */
    @Id
    @Column(name = "COURT_ID")
    private Long courtId;
	
	/**  */
    @Column(name = "ACTIVE")
    private String active;
    
    /**  */
    @Column(name = "FIFO")
    private String fifo;
    
    /**  */
    @Column(name = "PREDOCKET")
    private String predocket;
    
    /**  */
    @Column(name = "PUBLICATION_ID")
    private Long publicationId;
    
    /** */
    @Column(name = "RPX_WORKFLOW_TYPE_ID")
    private Long rpxWorkflowTypeId;
	
    /** */
    @Column(name = "DISPLAY_NAME")
    private String displayName;
    

    /** */
    @Column(name = "FILTER_NAME")
    private String filterName;
    
    /** */
    @Column(name = "DOCKET_EXAMPLE")
    private String docketExample;
    
    /** */
    @Column(name = "MAX_BATCH_DOCKET_SIZE")
    private Long maxBatchDocketSize;
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if ((this == other))
        {
            return true;
        }

        if ((other == null))
        {
            return false;
        }

        if (!(other instanceof CourtConfig))
        {
            return false;
        }

        CourtConfig castOther = (CourtConfig) other;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(this.active, castOther.getActive());
        equalsBuilder.append(this.fifo, castOther.getFifo());
        equalsBuilder.append(this.predocket, castOther.getPredocket());
        equalsBuilder.append(this.courtId, castOther.getCourtId());
        equalsBuilder.append(this.publicationId, castOther.getPublicationId());
        equalsBuilder.append(this.rpxWorkflowTypeId, castOther.getRpxWorkflowTypeId());
        equalsBuilder.append(this.displayName, castOther.getDisplayName());
        equalsBuilder.append(this.filterName, castOther.getFilterName());
        equalsBuilder.append(this.docketExample, castOther.getDocketExample());
        equalsBuilder.append(this.maxBatchDocketSize, castOther.getMaxBatchDocketSize());

        return equalsBuilder.isEquals();
    }
    
	public String getActive()
	{
		return active;
	}
    
    public Long getCourtId()
	{
		return courtId;
	}
    
    public Long getPublicationId()
	{
		return publicationId;
	}
    
    public Long getRpxWorkflowTypeId()
	{
		return rpxWorkflowTypeId;
	}

    public String getDisplayName()
    {
    	return displayName;
    }
    
    public String getDocketExample()
    {
    	return docketExample;
    }
    
    
	public String getFifo()
	{
		return fifo;
	}

	public String getPredocket()
	{
		return predocket;
	}

    public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	
	public Long getMaxBatchDocketSize() {
		return maxBatchDocketSize;
	}
	
	public void setMaxBatchDocketSize(final Long maxBatchDocketSize) {
		this.maxBatchDocketSize = maxBatchDocketSize;
	}
	

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(this.active);
        hashCodeBuilder.append(this.fifo);
        hashCodeBuilder.append(this.predocket);
        hashCodeBuilder.append(this.courtId);
        hashCodeBuilder.append(this.publicationId);
        hashCodeBuilder.append(this.rpxWorkflowTypeId);
        hashCodeBuilder.append(this.displayName);
        hashCodeBuilder.append(this.docketExample);
        hashCodeBuilder.append(this.maxBatchDocketSize);

        return hashCodeBuilder.toHashCode();
    }
    
    public void setActive(String active)
    {
    	this.active = active;
    }

    public void setCourtId(Long courtId)
	{
		this.courtId = courtId;
	}

	public void setPublicationId(Long publicationId)
	{
		this.publicationId = publicationId;
	}

	public void setRpxWorkflowTypeId(Long rpxWorkflowTypeId)
	{
		this.rpxWorkflowTypeId = rpxWorkflowTypeId;
	}
   
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	
	public void setDocketExample(String docketExample)
	{
		this.docketExample = docketExample;
	}
	
	public void setFifo(String fifo)
	{
		this.fifo = fifo;
	}
	
	public void setPredocket(String predocket)
	{
		this.predocket = predocket;
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("CourtConfig@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("active='").append(getActive()).append("' ");
        buffer.append("fifo='").append(getFifo()).append("' ");
        buffer.append("predocket='").append(getPredocket()).append("' ");
        buffer.append("courtId='").append(getCourtId()).append("' ");
        buffer.append("publicationId='").append(getPublicationId()).append("' ");
        buffer.append("rpxWorkflowTypeId='").append(getRpxWorkflowTypeId()).append("' ");
        buffer.append("displayName='").append(getDisplayName()).append("' ");
        buffer.append("filterName='").append(getFilterName()).append("' ");
        buffer.append("docketExample='").append(getDocketExample()).append("' ");
        buffer.append("maxBatchDocketSize='").append(getMaxBatchDocketSize()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }
}




